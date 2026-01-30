-- =====================================================
-- 1. USERS (10 rows)
-- =====================================================
INSERT INTO user_profiles (username, display_name, avatar_url)
VALUES
('gunpla_fan', 'Gunpla Fan', 'https://example.com/avatar1.png'),
('model_master', 'Model Master', 'https://example.com/avatar2.png'),
('collector101', 'Collector 101', NULL),
('hg_builder', 'HG Builder', NULL),
('mg_enthusiast', 'MG Enthusiast', 'https://example.com/avatar5.png'),
('pg_collector', 'PG Collector', NULL),
('figure_lover', 'Figure Lover', 'https://example.com/avatar7.png'),
('plush_hunter', 'Plush Hunter', NULL),
('merch_chaser', 'Merch Chaser', 'https://example.com/avatar10.png'),
('nendo_fan', 'Nendoroid Fan', NULL);

-- =====================================================
-- 2. WISHLISTS (15 rows)
-- =====================================================
INSERT INTO wishlists (user_id, name, description, is_public)
VALUES
(1, 'My First Wishlist', 'All the kits I want this year', TRUE),
(1, 'Rare Collectibles', 'Limited edition figures', FALSE),
(2, 'Gift Ideas', 'For upcoming birthdays', TRUE),
(3, 'Must-Have HG Kits', 'HG kits to build ASAP', TRUE),
(4, 'HG Favorites', 'HG kits I love', TRUE),
(5, 'MG Dream Kits', 'Must-have MG models', FALSE),
(6, 'PG Legends', 'PG kits I want', TRUE),
(7, 'Figures I Want', 'Collectible figures', TRUE),
(8, 'Soft & Cute', 'Plushies to grab', FALSE),
(9, 'Merch I Crave', 'Merchandise wishlist', TRUE),
(10, 'Nendo Collection', 'Nendoroids I want', TRUE),
(2, 'Second Wishlist', 'Backup wishlist', TRUE),
(3, 'Additional HG Kits', 'Extra HG kits', FALSE),
(5, 'MG Rare Kits', 'Limited MG kits', TRUE),
(6, 'PG Must-Haves', 'PG kits priority', TRUE);

-- =====================================================
-- 3. PRODUCTS (15 rows)
-- =====================================================

INSERT INTO products (name, brand, product_type, format, grade)
VALUES
('RX-78-2 Gundam', 'Bandai', 'model_kit', 'model_kit', 'HG'),
('Zaku II', 'Bandai', 'model_kit', 'model_kit', 'MG'),
('Gundam Barbatos', 'Bandai', 'model_kit', 'model_kit', 'HG'),
('Limited Edition Unicorn Gundam', 'Bandai', 'model_kit', 'model_kit', 'PG'),
('Chars Zaku', 'Bandai', 'model_kit', 'model_kit', 'MGEX'),
('Strike Freedom Gundam', 'Bandai', 'model_kit', 'model_kit', 'MG'),
('Nemo HG', 'Bandai', 'model_kit', 'model_kit', 'HG'),
('Delta Plus', 'Bandai', 'model_kit', 'model_kit', 'RE100'),
('Tallgeese', 'Bandai', 'model_kit', 'model_kit', 'MG'),
('Banshee Norn', 'Bandai', 'model_kit', 'model_kit', 'PG'),
('Exia', 'Bandai', 'model_kit', 'model_kit', 'RG'),
('Deathscythe', 'Bandai', 'model_kit', 'model_kit', 'MG'),
('Hi-Nu Gundam', 'Bandai', 'model_kit', 'model_kit', 'PG'),
('Red Frame', 'Bandai', 'model_kit', 'model_kit', 'HG'),
('Astray Blue Frame', 'Bandai', 'model_kit', 'model_kit', 'MG');


-- =====================================================
-- 4. WISHLIST ITEMS (15–20 rows)
-- =====================================================
INSERT INTO wishlist_items (wishlist_id, product_id, target_price, notes, priority)
VALUES
-- Gunpla Fan, My First Wishlist
(1, 1, 29.99, 'Pick this up when on sale', 1),
(1, 2, 49.99, NULL, 2),
(1, 3, 19.99, 'Maybe bundle with other kits', 3),
-- Gunpla Fan, Rare Collectibles
(2, 4, 199.99, 'Limited edition exclusive', 1),
-- Model Master, Gift Ideas
(3, 2, 39.99, 'Birthday gift for friend', 1),
(3, 5, 59.99, NULL, 2),
-- Collector 101, Must-Have HG Kits
(4, 1, 29.99, 'Start building immediately', 1),
(4, 3, 18.99, NULL, 2),
(4, 6, 49.99, 'High priority build', 3),
-- HG Builder, HG Favorites
(5, 7, 22.99, NULL, 1),
(5, 8, 34.99, 'Favorite design', 2),
(5, 9, 27.99, NULL, 3),
-- MG Enthusiast, MG Dream Kits
(6, 2, 59.99, NULL, 1),
(6, 5, 69.99, 'Special edition', 2),
(6, 14, 79.99, NULL, 3),
-- PG Collector, PG Legends
(7, 6, 129.99, NULL, 1),
(7, 12, 149.99, 'Priority build', 2),
-- Figure Lover, Figures I Want
(8, 10, 89.99, NULL, 1),
(8, 11, 99.99, 'Highly collectible', 2),
-- Plush Hunter, Soft & Cute
(9, 13, 29.99, NULL, 1),
(9, 15, 24.99, 'Limited stock', 2);



SELECT 
    u.id AS user_id,
    u.username,
    u.display_name,
    
    w.id AS wishlist_id,
    w.name AS wishlist_name,
    w.is_public,
    
    wi.id AS wishlist_item_id,
    wi.priority,
    wi.target_price,
    wi.notes,
    
    p.id AS product_id,
    p.name AS product_name,
    p.brand AS product_brand,
    p.product_type,
    p.format,
    p.grade
FROM user_profiles u
JOIN wishlists w ON w.user_id = u.id
JOIN wishlist_items wi ON wi.wishlist_id = w.id
JOIN products p ON p.id = wi.product_id
ORDER BY u.id, w.id, wi.priority;
