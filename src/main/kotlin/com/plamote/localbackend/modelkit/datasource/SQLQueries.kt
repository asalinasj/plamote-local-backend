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
  SELECT * FROM PRODUCTS
""".trimIndent()

val SELECT_PRODUCT = """
  SELECT * FROM PRODUCTS WHERE id = ?
""".trimIndent()

val SELECT_PRODUCTS_CURRENT_DATA = """
  SELECT A.product_id, A.site_id, amount, currency, in_stock, scraped_at, product_url, name, type, base_url, created_at
  FROM CURRENT_DATA A
  INNER JOIN SITE_LINKS B
  ON A.product_id = B.product_id AND A.site_id = B.site_id
  INNER JOIN SITES C
  ON A.product_id = C.id
""".trimIndent()

val SELECT_PRODUCTS_IMAGES = """
  SELECT * FROM PRODUCTS_IMAGES
""".trimIndent()
