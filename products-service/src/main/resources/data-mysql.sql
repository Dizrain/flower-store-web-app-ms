-- Insert data into the categories table
INSERT INTO categories (category_id, name) VALUES ('category-1', 'Roses');
INSERT INTO categories (category_id, name) VALUES ('category-2', 'Tulips');
INSERT INTO categories (category_id, name) VALUES ('category-3', 'Orchids');

-- Insert data into the products table
INSERT INTO products (product_id, name, description, price, color, type, in_season)
VALUES ('product-1', 'Red Rose Bouquet', 'A bouquet of beautiful red roses', 30.00, 'Red', 'Rose', true);
INSERT INTO products (product_id, name, description, price, color, type, in_season)
VALUES ('product-2', 'Yellow Tulip Bouquet', 'A bouquet of vibrant yellow tulips', 25.00, 'Yellow', 'Tulip', true);
INSERT INTO products (product_id, name, description, price, color, type, in_season)
VALUES ('product-3', 'Purple Orchid Bouquet', 'A bouquet of exotic purple orchids', 35.00, 'Purple', 'Orchid', false);

-- Insert data into the product_category join table
INSERT INTO product_category (product_id, category_id) VALUES (1, 1); -- Red Rose Bouquet is a Rose
INSERT INTO product_category (product_id, category_id) VALUES (2, 2); -- Yellow Tulip Bouquet is a Tulip
INSERT INTO product_category (product_id, category_id) VALUES (3, 3); -- Purple Orchid Bouquet is an Orchid

-- Insert data into the stock_items table
INSERT INTO stock_items (stock_item_id, product_id, stock_level, reorder_threshold)
VALUES ('stock-1', 'product-1', 100, 10);
INSERT INTO stock_items (stock_item_id, product_id, stock_level, reorder_threshold)
VALUES ('stock-2', 'product-2', 200, 20);
INSERT INTO stock_items (stock_item_id, product_id, stock_level, reorder_threshold)
VALUES ('stock-3', 'product-3', 300, 30);
