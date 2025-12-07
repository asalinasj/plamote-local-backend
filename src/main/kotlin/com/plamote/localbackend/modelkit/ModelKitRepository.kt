package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.datasource.ModelKitDatasource
import com.plamote.localbackend.modelkit.model.ModelKit
import com.plamote.localbackend.modelkit.model.ModelKitPrices
import com.plamote.localbackend.modelkit.model.RetailerOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModelKitRepository(
  private val datasource: ModelKitDatasource
) {
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
