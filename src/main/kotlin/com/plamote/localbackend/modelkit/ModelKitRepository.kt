package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.datasource.ModelKitDatasource
import com.plamote.localbackend.modelkit.model.v0.ModelKit
import com.plamote.localbackend.modelkit.model.v0.ModelKitPrices
import com.plamote.localbackend.modelkit.model.v0.RetailerOption
import com.plamote.localbackend.modelkit.model.v1.Product
import com.plamote.localbackend.modelkit.model.v1.Profile
import com.plamote.localbackend.modelkit.model.v1.UserOwnedKits
import com.plamote.localbackend.modelkit.model.v1.ProductRetailer
import com.plamote.localbackend.modelkit.model.v1.UserWishlists
import com.plamote.localbackend.modelkit.model.v1.UserWishlistItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.ZoneOffset

class ModelKitRepository(
  private val datasource: ModelKitDatasource
) {

  suspend fun getProducts(): MutableMap<Int, Product> = withContext(Dispatchers.IO) {
    val res = mutableMapOf<Int, Product>()

    val productsRetailers = getProductsRetailers()

    val productsImages = getProductsImages()

    val products = datasource.selectProducts().await()
    products.forEach { r ->
      val productId = r.getInteger("id")
      val inStock = r.getInteger("in_stock")
      val product = Product(
        id = productId,
        title = r.getString("name"),
        brand = r.getString("brand"),
        series = r.getString("series"),
        scale = r.getString("scale"),
        sku = r.getString("sku"),
        inStock = inStock == 1,
        lastChecked = r.getLocalDateTime("last_checked")?.toInstant(ZoneOffset.UTC),
        createdAt = r.getLocalDateTime("created_at")?.toInstant(ZoneOffset.UTC),
        updatedAt = r.getLocalDateTime("updated_at")?.toInstant(ZoneOffset.UTC)
      )
      res.put(productId, product)
    }

    productsRetailers.forEach { p ->
      val product = res[p.productId]
      if(product != null && product.id == p.productId) {
        val currentBestPrice = product.bestTotalPrice
        if(p.amount < currentBestPrice || currentBestPrice == BigDecimal("0.00")) {
          val updatedProduct = product.copy(bestTotalPrice = p.amount)
          res.put(product.id, updatedProduct)
        } else {
          res.put(product.id, product)
        }
        product.productRetailers.add(p)
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

suspend fun getProduct(id: Int): Product? = withContext(Dispatchers.IO) {

    val row = datasource.selectProduct(id).await() ?: return@withContext null

    val inStock = row.getInteger("in_stock")
    val product = Product(
        id = row.getInteger("id"),
        title = row.getString("name"),
        brand = row.getString("brand"),
        series = row.getString("series"),
        scale = row.getString("scale"),
        sku = row.getString("sku"),
        inStock = inStock == 1,
        lastChecked = row.getLocalDateTime("last_checked")?.toInstant(ZoneOffset.UTC),
        createdAt = row.getLocalDateTime("created_at")?.toInstant(ZoneOffset.UTC),
        updatedAt = row.getLocalDateTime("updated_at")?.toInstant(ZoneOffset.UTC)
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

suspend fun getProfile(id: Int): Profile? = withContext(Dispatchers.IO) {
  val row = datasource.selectUserProfile(id).await() ?: return@withContext null

  val profile = Profile(
    id = row.getInteger("user_id"),
    username = row.getString("username"),
    display_name = row.getString("display_name"),
    avatar_url = row.getString("avatar_url")
  )

  profile

}

suspend fun getOwnedKits(id: Int): MutableMap<Int, UserOwnedKits> = withContext(Dispatchers.IO) {
  val res = mutableMapOf<Int, UserOwnedKits>()
  val rows = datasource.selectUserOwned(id).await() 
  
  rows.forEach { r ->   
      val userOwned = UserOwnedKits(
        id = r.getInteger("product_id"),
        name = r.getString("name"),
        description = r.getString("description"),
        status = r.getString("status"),
        quantity = r.getInteger("quantity"),
        notes = r.getString("notes")
  )
  res.put(r.getInteger("product_id"), userOwned)
  }

  res
}

suspend fun getUserWishlists(id: Int): List<UserWishlists> = withContext(Dispatchers.IO) {
    val rows = datasource.selectUserWishlists(id).await()

    rows.map { r ->
        UserWishlists(
            name = r.getString("name"),
            description = r.getString("description"),
            isPublic = r.getBoolean("is_public") ?: false,
            createdAt = r.getLocalDateTime("created_at")?.toInstant(ZoneOffset.UTC),
            updatedAt = r.getLocalDateTime("updated_at")?.toInstant(ZoneOffset.UTC)
        )
    }
}

suspend fun getUserWishlistItems(userid: Int, wishlist_id: Int): List<UserWishlistItems> = withContext(Dispatchers.IO) {
    val rows = datasource.selectUserWishlistsItems(userid, wishlist_id).await()

    rows.map { r ->
        UserWishlistItems(
            product_id = r.getInteger("product_id"),
            wishlist_id = r.getInteger("wishlist_id"),
            product_name = r.getString("product_name"),
            target_price = r.getBigDecimal("target_price"),
            notes = r.getString("notes")
        )
    }
}



  suspend fun getProductsRetailers(): MutableList<ProductRetailer> = withContext(Dispatchers.IO) {
    val res = mutableListOf<ProductRetailer>()
    val productsRetailers = datasource.selectProductsCurrentData().await()
    productsRetailers.forEach { r ->
      val inStock = r.getInteger("stock_status")
      val productRetailer = ProductRetailer(
        productId = r.getInteger("product_id"),
        siteId = r.getString("site_id"),
        amount = r.getBigDecimal("price"),
        currency = r.getString("currency"),
        inStock = inStock == 1,
        scrapedAt = r.getLocalDateTime("scraped_at").toInstant(ZoneOffset.UTC),
        productUrl = r.getString("product_url"),
        name = r.getString("name"),
        baseUrl = r.getString("base_url"),
        createdAt = r.getLocalDateTime("created_at").toInstant(ZoneOffset.UTC)
      )
      res.add(productRetailer)
    }
    res
  }

  suspend fun getProductsImages(): MutableMap<Int, MutableList<String>> = withContext(Dispatchers.IO) {
    val res = mutableMapOf<Int, MutableList<String>>()
    val productsImages = datasource.selectProductsImages().await()
    productsImages.forEach { r ->
      val productId = r.getInteger("product_id")
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
