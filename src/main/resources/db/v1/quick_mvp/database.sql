DROP DATABASE IF EXISTS plamote;

CREATE DATABASE plamote
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE plamote;

SET default_storage_engine = 'InnoDB';
SET SESSION sql_mode = 'STRICT_ALL_TABLES';

-- =====================================================
-- PRODUCTS
-- =====================================================
CREATE TABLE products (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  name VARCHAR(500) NOT NULL,
  description TEXT,

  brand VARCHAR(100) NOT NULL,
  franchise VARCHAR(150),
  series VARCHAR(100),

  product_type ENUM(
    'model_kit','figure','statue','plush','merch','other'
  ) NOT NULL DEFAULT 'model_kit',

  format ENUM(
    'model_kit','scale_figure','prize_figure','statue',
    'plush','acrylic','nendoroid','other'
  ),

  grade ENUM(
    'SD','EG','HG','HGUC','RG','MG','MGEX',
    'PG','RE100','FULL_MECHANICS','HI_RES','NO_GRADE'
  ),

  scale VARCHAR(50),
  sku VARCHAR(100),

  gtin CHAR(13),
  gtin_unique CHAR(13)
    GENERATED ALWAYS AS (
      CASE WHEN gtin IS NULL OR gtin = '' THEN NULL ELSE gtin END
    ) STORED,

  release_date DATE,
  preorder_date DATE,

  in_stock BOOLEAN NOT NULL DEFAULT TRUE,
  last_checked TIMESTAMP,

  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

  FULLTEXT KEY ft_products_search (
    name, description, franchise, series, brand
  )
) ENGINE=InnoDB;

CREATE UNIQUE INDEX ux_products_gtin ON products (gtin_unique);

-- =====================================================
-- PRODUCT TAGS
-- =====================================================
CREATE TABLE product_tags (
  product_id INT UNSIGNED NOT NULL,
  tag VARCHAR(100) NOT NULL,

  PRIMARY KEY (product_id, tag),
  INDEX idx_tag (tag),

  FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- SITES
-- =====================================================
CREATE TABLE sites (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  name VARCHAR(255) NOT NULL,

  site_category ENUM(
    'retailer','marketplace','official','reseller','other'
  ) NOT NULL,

  base_url VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================================================
-- SITE LINKS
-- =====================================================
CREATE TABLE site_links (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  product_id INT UNSIGNED NOT NULL,
  site_id INT UNSIGNED NOT NULL,

  product_url TEXT NOT NULL,

  UNIQUE KEY ux_product_site (product_id, site_id),

  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
  FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- PRODUCT IMAGES
-- =====================================================
CREATE TABLE product_images (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  product_id INT UNSIGNED NOT NULL,
  site_id INT UNSIGNED,

  image_url TEXT NOT NULL,

  image_hash BINARY(32)
    GENERATED ALWAYS AS (UNHEX(SHA2(image_url, 256))) STORED,

  position INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE KEY ux_product_site_image (product_id, site_id, image_hash),

  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
  FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- =====================================================
-- CURRENT DATA (LATEST PRICING)
-- =====================================================
CREATE TABLE current_data (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  product_id INT UNSIGNED NOT NULL,
  site_id INT UNSIGNED NOT NULL,

  price DECIMAL(10,2) NOT NULL,
  shipping_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  tax_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,

  total_price DECIMAL(10,2)
    GENERATED ALWAYS AS (price + shipping_cost + tax_cost)
    STORED,

  currency CHAR(3) NOT NULL DEFAULT 'USD',

  stock_status ENUM(
    'in_stock','out_of_stock','preorder','unknown'
  ) NOT NULL DEFAULT 'unknown',

  is_preorder BOOLEAN NOT NULL DEFAULT FALSE,
  scraped_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE KEY ux_product_site (product_id, site_id),
  INDEX idx_total_price (total_price),

  CONSTRAINT chk_price_positive
    CHECK (price >= 0 AND shipping_cost >= 0 AND tax_cost >= 0),

  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
  FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- PRICE HISTORY (APPEND-ONLY)
-- =====================================================
CREATE TABLE price_history (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  product_id INT UNSIGNED NOT NULL,
  site_id INT UNSIGNED NOT NULL,

  price DECIMAL(10,2) NOT NULL,
  shipping_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  tax_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,

  scraped_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
  FOREIGN KEY (site_id) REFERENCES sites(id) ON DELETE CASCADE,

  INDEX idx_price_history_product_time (product_id, scraped_at)
) ENGINE=InnoDB;

-- =====================================================
-- USER PROFILES
-- =====================================================
CREATE TABLE user_profiles (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  username VARCHAR(50) NOT NULL UNIQUE,
  display_name VARCHAR(100),
  avatar_url TEXT,

  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================================================
-- WISHLISTS
-- =====================================================
CREATE TABLE wishlists (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  user_id INT UNSIGNED NOT NULL,

  name VARCHAR(200) NOT NULL,
  description TEXT,
  is_public BOOLEAN NOT NULL DEFAULT FALSE,

  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

  FOREIGN KEY (user_id)
    REFERENCES user_profiles(id)
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- WISHLIST ITEMS
-- =====================================================
CREATE TABLE wishlist_items (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),

  wishlist_id INT UNSIGNED NOT NULL,
  product_id INT UNSIGNED NOT NULL,

  target_price DECIMAL(10,2),
  notes TEXT,
  priority INT NOT NULL DEFAULT 0,

  added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  notified_at TIMESTAMP,

  UNIQUE KEY ux_wishlist_product (wishlist_id, product_id),

  FOREIGN KEY (wishlist_id) REFERENCES wishlists(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- USER PRODUCT OWNERSHIP
-- =====================================================
CREATE TABLE user_product_ownership (
  user_id INT UNSIGNED NOT NULL,
  product_id INT UNSIGNED NOT NULL,

  status ENUM(
    'owned','built','preordered','wishlist','sold'
  ) NOT NULL,

  quantity INT NOT NULL DEFAULT 1,
  notes TEXT,

  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (user_id, product_id),

  FOREIGN KEY (user_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- PRODUCT RATINGS
-- =====================================================
CREATE TABLE product_ratings (
  user_id INT UNSIGNED NOT NULL,
  product_id INT UNSIGNED NOT NULL,

  rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
  review TEXT,

  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (user_id, product_id),

  FOREIGN KEY (user_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_product_ratings_product
  ON product_ratings (product_id);

-- =====================================================
-- VIEWS
-- =====================================================
CREATE VIEW product_rating_stats AS
SELECT
  product_id,
  COUNT(*) AS rating_count,
  AVG(rating) AS avg_rating
FROM product_ratings
GROUP BY product_id;

CREATE VIEW product_best_price AS
SELECT
  product_id,
  MIN(total_price) AS best_price
FROM current_data
WHERE stock_status = 'in_stock'
GROUP BY product_id;
