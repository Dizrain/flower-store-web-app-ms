-- Drop tables if they already exist
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
SET FOREIGN_KEY_CHECKS = 1;

-- Creation of the categories table
CREATE TABLE categories
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    UNIQUE (category_id)
);

-- Creation of the products table with additional columns
CREATE TABLE products
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    color       VARCHAR(255) NOT NULL,
    type        VARCHAR(255) NOT NULL,
    inSeason    BOOLEAN NOT NULL,
    UNIQUE (product_id)
);

-- Creation of the product_category join table
CREATE TABLE product_category
(
    product_id  BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Creation of the stock_items table
CREATE TABLE stock_items
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_item_id      VARCHAR(255) NOT NULL,
    product_id         VARCHAR(255) NOT NULL,
    stock_level        INT NOT NULL CHECK (stock_level >= 0),
    reorder_threshold  INT NOT NULL CHECK (reorder_threshold >= 0),
    UNIQUE (stock_item_id)
);