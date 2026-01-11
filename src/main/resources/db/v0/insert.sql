INSERT INTO MODELKIT(
	title,
    brand,
    grade,
    scale,
    series,
    description
)
VALUES
(
	'Wing Gundam Zero',
	'Bandai',
	'RG',
	'1/144',
	'Mobile Suit Gundam Wing',
	'Mobile suit from Gundam Wing'
)


INSERT INTO MODELKITIMAGES(
	model_kit_id,
    image
)
VALUES
(
	1,
	'https://www.gundamplanet.com/cdn/shop/files/rg-xxxg-00w0-wing-gundam-zero-base.jpg?v=1758835725'
),
(
	1,
	'https://www.usagundamstore.com/cdn/shop/files/ku6CuJupLdrAHFGD_1100x.jpg?v=1758550130'
),
(
	1,
	'https://www.usagundamstore.com/cdn/shop/files/9TzLX1M1E2vzIQB7_1100x.jpg?v=1758550130'
),
(
	1,
	'https://www.usagundamstore.com/cdn/shop/files/GMaABHwlagapyl1d_1100x.jpg?v=1758550130'
)


INSERT INTO MODELKITPRICES(
	model_kit_id,
    retailer_id,
    name,
    available,
    sub_total_price,
    tax_price,
    total_price
)
VALUES
(1,1,'Gundam Planet',true,25.99,9.99,35.98),
(1,2,'Galactic Toys',true,24.99,9.99,34.98)
