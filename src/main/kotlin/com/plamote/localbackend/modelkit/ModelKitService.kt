package com.plamote.localbackend.modelkit

import com.plamote.localbackend.modelkit.model.v0.ModelKit
import com.plamote.localbackend.modelkit.model.v1.Product
import com.plamote.localbackend.modelkit.model.v1.Profile
import com.plamote.localbackend.modelkit.model.v1.UserOwnedKits
import com.plamote.localbackend.modelkit.model.v1.UserWishlists
import com.plamote.localbackend.modelkit.model.v1.UserWishlistItems

class ModelKitService(
  private val repository: ModelKitRepository
) {

  /**
   * V0 API - Original simple version
   */
  suspend fun getModelKits(): List<ModelKit> {
    val map = repository.getModelKits()
    val modelKits = map.map { it.value }
    return modelKits
  }

  /**
   * V1 API - Current version w/ new DB design
   */
  suspend fun getModelKitProducts(): MutableMap<Int, Product> {
    val map = repository.getProducts()
    val products = map.map { it.value } // can discuss returning list or map
    return map
  }

    suspend fun getModelKitProduct(id: Int): Product? {
    val productReturned = repository.getProduct(id)
    return productReturned
  }

  suspend fun getUserProfile(id: Int): Profile? {
    val profileReturned = repository.getProfile(id)
    return profileReturned
  }

  suspend fun getUserOwnedKits(id: Int): MutableMap<Int, UserOwnedKits> {
    val map = repository.getOwnedKits(id)
    val owned = map.map { it.value }
    return map
  }

suspend fun getUserWishlists(id: Int): List<UserWishlists> {
    return repository.getUserWishlists(id)
}

suspend fun getUserWishlistItems(userid: Int, wishlist_id: Int): List<UserWishlistItems> {
  return repository.getUserWishlistItems(userid, wishlist_id)
}


}

