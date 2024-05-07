-- Drop tables if they already exist
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE orders
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id            VARCHAR(255) NOT NULL,
    customer_id         VARCHAR(255) NOT NULL,
    payment_id          VARCHAR(255) NOT NULL,
    shipping_address    VARCHAR(255) NOT NULL,
    billing_information VARCHAR(255) NOT NULL,
    status              VARCHAR(255) NOT NULL
);

CREATE TABLE order_items
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT      NOT NULL,
    product_id     VARCHAR(255) NOT NULL,
    quantity       INTEGER     NOT NULL
);


-- Add foreign key constraints to order_items table
ALTER TABLE order_items
ADD CONSTRAINT FK_order_items_orders
FOREIGN KEY (order_id) REFERENCES orders(id);