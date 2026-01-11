INSERT INTO PRODUCTS(
	id,
    name,
    brand,
    series,
    scale,
    sku,
    in_stock,
    last_checked,
    created_at,
    updated_at
) VALUES
(
	'111111111111111111111111111111111111',
    'Wing Gundam Zero',
    'Bandai',
    'Mobile Suit Gundam Wing',
    '1/144',
    '1111',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
	'111111111111111111111111111111111112',
    'Wing Gundam',
    'Bandai',
    'Mobile Suit Gundam Wing',
    '1/144',
    '1112',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)

---

INSERT INTO CURRENT_DATA(
	id,
    product_id,
    site_id,
    amount,
    currency,
    in_stock,
    scraped_at
) VALUES
(
	'111111111111111111111111111111111111',
    (SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111111'),
    (SELECT ID from SITES WHERE ID = '111111111111111111111111111111111111'),
    40.02,
    'USD',
    true,
    CURRENT_TIMESTAMP
),
(
	'111111111111111111111111111111111112',
    (SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111112'),
    (SELECT ID from SITES WHERE ID = '111111111111111111111111111111111112'),
    35.02,
    'USD',
    true,
    CURRENT_TIMESTAMP
)

---

INSERT INTO SITES(
	id,
    name,
    type,
    base_url,
	created_at
) VALUES
(
	'111111111111111111111111111111111111',
    'Gundam Planet',
    'Gunpla',
    'https://www.gundamplanet.com/',
    CURRENT_TIMESTAMP
),
(
	'111111111111111111111111111111111112',
    'USA Gundam Store',
    'Gunpla',
    'https://www.usagundamstore.com/',
    CURRENT_TIMESTAMP
)

---

INSERT INTO SITE_LINKS(
	id,
    product_id,
    site_id,
    product_url
) VALUES
(
	'111111111111111111111111111111111111',
    (SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111111'),
    (SELECT ID from SITES WHERE ID = '111111111111111111111111111111111111'),
    'https://www.gundamplanet.com/products/rg-xxxg-00w0-wing-gundam-zero?_pos=1&_psq=wing+gundam+zero&_ss=e&_v=1.0'
),
(
	'111111111111111111111111111111111112',
    (SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111112'),
    (SELECT ID from SITES WHERE ID = '111111111111111111111111111111111112'),
    'https://www.usagundamstore.com/products/rg-1-144-35-wing-gundam'
)

---

INSERT INTO PRODUCTS_IMAGES(
	product_id,
    image_url
) VALUES
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111111'),
    'https://www.gundamplanet.com/cdn/shop/files/rg-xxxg-00w0-wing-gundam-zero-base.jpg?v=1758835725'
),
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111111'),
    'https://www.gundamplanet.com/cdn/shop/files/rg-xxxg-00w0-wing-gundam-zero-02_ec993823-e73e-4f4d-a1d3-ee66b953226f.jpg?v=1758835725'
),
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111111'),
    'https://www.gundamplanet.com/cdn/shop/files/rg-xxxg-00w0-wing-gundam-zero-06_05a3123e-215a-4772-a682-0da957ecf8c6.jpg?v=1758835725'
),
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111111'),
    'https://www.gundamplanet.com/cdn/shop/files/rg-xxxg-00w0-wing-gundam-zero-11.jpg?v=1758835725'
),
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111112'),
    'https://www.usagundamstore.com/cdn/shop/products/189936510_4080711701983337_7606518962261609205_n_550x.jpg?v=1671117683'
),
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111112'),
    'https://www.usagundamstore.com/cdn/shop/products/192353144_4080711831983324_1838485444110018850_n_550x.jpg?v=1671117683'
),
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111112'),
    'https://www.usagundamstore.com/cdn/shop/products/198164844_4080713805316460_3310837546613909189_n_550x.jpg?v=1671117683'
),
(
	(SELECT ID from PRODUCTS WHERE ID = '111111111111111111111111111111111112'),
    'https://www.usagundamstore.com/cdn/shop/products/196469202_4080712818649892_5707334468864781955_n_550x.png?v=1671117683'
)
