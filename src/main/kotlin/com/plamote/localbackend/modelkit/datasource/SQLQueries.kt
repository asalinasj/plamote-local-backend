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
