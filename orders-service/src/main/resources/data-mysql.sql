-- Insert data into orders table
INSERT INTO orders (order_id, customer_id, payment_id, shipping_address, billing_information, status)
VALUES
('order_1', 'customer_1', 'payment_1', '123 Main St', '123 Main St', 'PLACED'),
('order_2', 'customer_2', 'payment_2', '456 Maple Ave', '456 Maple Ave', 'SHIPPED');

-- Insert data into order_items table
INSERT INTO order_items (order_id, product_id, quantity)
VALUES
(1, 'product-1', 2),
(1, 'product-2', 1),
(2, 'product_3', 3);