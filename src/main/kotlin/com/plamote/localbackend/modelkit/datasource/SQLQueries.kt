val SELECT_MODEL_KITS = """
  SELECT id, title, brand, grade, scale, series, description
  FROM MODELKIT
""".trimIndent()

val SELECT_MODEL_KIT_IMAGES = """
  SELECT id, image
  FROM MODELKIT A
  INNER JOIN MODELKITIMAGES B
  ON A.id = B.model_kit_id
""".trimIndent()

val SELECT_MODEL_KIT_RETAILER_PRICES = """
  SELECT model_kit_id, retailer_id, name, available, sub_total_price, tax_price, total_price
  FROM MODELKITPRICES
""".trimIndent()

/*
* Updated queries based on new ER Schema design, created new row in SITE_LINKS table
* */

val SELECT_PRODUCTS = """
  SELECT * FROM products
""".trimIndent()

val SELECT_PRODUCT = """
  SELECT * FROM products WHERE id = ?
""".trimIndent()

val GET_USER_PROFILE = """
select id as user_id ,username, display_name, avatar_url from user_profiles WHERE id = ?
""".trimIndent()

val SELECT_USER_OWNED_KITS = """
select product_id, status, quantity, notes,name, description
from user_profiles
inner join user_product_ownership on user_profiles.id = user_product_ownership.user_id
inner join products on user_product_ownership.product_id = products.id
where user_id = ?
""".trimIndent()

val SELECT_USER_WISHLISTS = """
select DISTINCT user_profiles.id as user_id, name, description, is_public, wishlists.created_at, wishlists.updated_at
from user_profiles
inner join wishlist_items on user_profiles.id = wishlist_items.wishlist_id
inner join wishlists on wishlists.user_id = wishlist_items.wishlist_id
where user_profiles.id = ?
""".trimIndent()

val SELECT_USER_WISHLIST_ITEMS = """
select wishlists.id as wishlist_id , product_id, products.name as product_name, target_price, notes
from user_profiles
inner join wishlist_items on user_profiles.id = wishlist_items.wishlist_id
inner join wishlists on wishlists.user_id = wishlist_items.wishlist_id
inner join products on wishlist_items.product_id = products.id
where user_profiles.id = ? AND wishlists.id = ?
order by wishlists.id
""".trimIndent()

val SELECT_PRODUCTS_CURRENT_DATA = """
  SELECT A.product_id, A.site_id, price, currency, stock_status, scraped_at, product_url, name, base_url, created_at
  FROM current_data A
  INNER JOIN site_links B
  ON A.product_id = B.product_id AND A.site_id = B.site_id
  INNER JOIN sites C
  ON A.product_id = C.id
""".trimIndent()

val SELECT_PRODUCTS_IMAGES = """
  SELECT * FROM product_images
""".trimIndent()
