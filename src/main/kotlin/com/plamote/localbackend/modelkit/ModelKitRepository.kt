package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.datasource.ModelKitDatasource
import com.plamote.localbackend.modelkit.model.v0.ModelKit
import com.plamote.localbackend.modelkit.model.v0.ModelKitPrices
import com.plamote.localbackend.modelkit.model.v0.RetailerOption
import com.plamote.localbackend.modelkit.model.v1.Product
import com.plamote.localbackend.modelkit.model.v1.ProductRetailer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneOffset

class ModelKitRepository(
  private val datasource: ModelKitDatasource
) {

  suspend fun getProducts(): MutableMap<String, Product> = withContext(Dispatchers.IO) {
    val res = mutableMapOf<String, Product>()

    val productsRetailers = getProductsRetailers()

    val productsImages = getProductsImages()

    val products = datasource.selectProducts().await()
    products.forEach { r ->
      val productId = r.getString("id")
      val inStock = r.getInteger("in_stock")
      val product = Product(
        id = productId,
        name = r.getString("name"),
        brand = r.getString("brand"),
        series = r.getString("series"),
        scale = r.getString("scale"),
        sku = r.getString("sku"),
        inStock = inStock == 1,
        lastChecked = r.getLocalDateTime("last_checked").toInstant(ZoneOffset.UTC),
        createdAt = r.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC),
        updatedAt = r.getLocalDateTime("updated_at").toInstant(ZoneOffset.UTC)
      )
      res.put(productId, product)
    }

    productsRetailers.forEach { p ->
      val product = res[p.productId]
      if(product != null && product.id == p.productId) {
        product.productRetailers.add(p)
        res.put(product.id, product)
      }
    }

    productsImages.forEach { p ->
      val product = res[p.key]
      if(product != null) {
        product.images = p.value
        res.put(product.id, product)
      }
    }

    res
  }

suspend fun getProduct(id: String): Product? = withContext(Dispatchers.IO) {

    val row = datasource.selectProduct(id).await() ?: return@withContext null

    val inStock = row.getInteger("in_stock")
    val product = Product(
        id = row.getString("id"),
        name = row.getString("name"),
        brand = row.getString("brand"),
        series = row.getString("series"),
        scale = row.getString("scale"),
        sku = row.getString("sku"),
        inStock = inStock == 1,
        lastChecked = row.getLocalDateTime("last_checked").toInstant(ZoneOffset.UTC),
        createdAt = row.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC),
        updatedAt = row.getLocalDateTime("updated_at").toInstant(ZoneOffset.UTC)
    )

    // Attach retailers for this product only
    val productsRetailers = getProductsRetailers()
    productsRetailers.filter { it.productId == id }.forEach {
        product.productRetailers.add(it)
    }

    // Attach images for this product only
    val productsImages = getProductsImages()
    productsImages[id]?.let { product.images = it }

    product
}



  suspend fun getProductsRetailers(): MutableList<ProductRetailer> = withContext(Dispatchers.IO) {
    val res = mutableListOf<ProductRetailer>()
    val productsRetailers = datasource.selectProductsCurrentData().await()
    productsRetailers.forEach { r ->
      val inStock = r.getInteger("in_stock")
      val productRetailer = ProductRetailer(
        productId = r.getString("product_id"),
        siteId = r.getString("site_id"),
        amount = r.getBigDecimal("amount"),
        currency = r.getString("currency"),
        inStock = inStock == 1,
        scrapedAt = r.getLocalDateTime("scraped_at").toInstant(ZoneOffset.UTC),
        productUrl = r.getString("product_url"),
        siteName = r.getString("name"),
        siteType = r.getString("type"),
        baseUrl = r.getString("base_url"),
        createdAt = r.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC)
      )
      res.add(productRetailer)
    }
    res
  }

  suspend fun getProductsImages(): MutableMap<String, MutableList<String>> = withContext(Dispatchers.IO) {
    val res = mutableMapOf<String, MutableList<String>>()
    val productsImages = datasource.selectProductsImages().await()
    productsImages.forEach { r ->
      val productId = r.getString("product_id")
      val imageUrl = r.getString("image_url")
      if(res.keys.contains(productId)) {
        val images = res[productId]
        images?.add(imageUrl)
        if (images != null) {
          res.put(productId, images)
        }
      } else {
        val images = mutableListOf(imageUrl)
        res.put(productId, images)
      }
    }
    res
  }

   suspend fun getModelKits(): MutableMap<Int, ModelKit> = withContext(Dispatchers.IO) {
    val res = mutableMapOf<Int, ModelKit>()
    val modelKits = datasource.selectModelKitsQuery().await()
    modelKits.forEach { r ->
      val id = r.getInteger("id")
      val modelKit = ModelKit(
        r.getInteger("id"),
        r.getString("title"),
        r.getString("brand"),
        r.getString("grade"),
        r.getString("scale"),
        r.getString("series"),
        r.getString("description")
      )
      res.put(id, modelKit)
    }
    val modelKitImages = getModelKitImages()
    modelKitImages.keys.
      forEach { id ->
        if(res.keys.contains(id)) {
          val modelKit = res[id]
          modelKit?.images = modelKitImages[id]!!
        }
      }
    val modelKitRetailerOptions = getModelKitPrices()
    modelKitRetailerOptions.forEach { r ->
      val modelKitId = r.modelKitId
      val modelKit = res[modelKitId]
      if(modelKit?.prices == null) {
        modelKit?.prices = ModelKitPrices(
          modelKitId = modelKitId,
          bestTotalPrice = r.totalPrice,
          retailerOptions = mutableListOf(r)
        )
      } else {
        if(r.totalPrice < modelKit.prices!!.bestTotalPrice) modelKit.prices!!.bestTotalPrice = r.totalPrice
        modelKit.prices!!.retailerOptions.add(r)
      }
    }

    res
  }

  suspend fun getModelKitImages(): MutableMap<Int, MutableList<String>> = withContext(Dispatchers.IO) {
    val modelKitImages: MutableMap<Int, MutableList<String>> = mutableMapOf()
    val images = datasource.selectModelKitImages().await()
    images.forEach { r ->
      val id = r.getInteger("id")
      val image = r.getString("image")
      if(modelKitImages.keys.contains(id)) {
        modelKitImages[id]?.add(image)
      } else {
        modelKitImages.put(id, mutableListOf(image))
      }
    }
    modelKitImages
  }

  suspend fun getModelKitPrices(): MutableList<RetailerOption> = withContext(Dispatchers.IO){
    val modelKitPrices = mutableListOf<RetailerOption>()
    datasource.selectModelKitPrices().await().forEach { r ->
      val available = r.getInteger("available")
      modelKitPrices.add(
        RetailerOption(
          modelKitId = r.getInteger("model_kit_id"),
          id = r.getInteger("retailer_id"),
          name = r.getString("name"),
          isAvailable = available == 1,
          subTotalPrice = r.getBigDecimal("sub_total_price"),
          taxPrice = r.getBigDecimal("tax_price"),
          totalPrice = r.getBigDecimal("total_price")
        )
      )
    }
    modelKitPrices
  }
}
