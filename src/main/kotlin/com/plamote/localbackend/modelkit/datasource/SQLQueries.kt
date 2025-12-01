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

val SELECT_MODEL_KIT_RETAILERS = """
  SELECT model_kit_id, retailer_id, name, available, sub_total_price, tax_price, total_price
  FROM MODELKITPRICES A
  JOIN MODELKITRETAILER B
  ON A.retailer_id = B.id
""".trimIndent()
