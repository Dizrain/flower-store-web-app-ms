-- Insert initial data into the orders table
INSERT INTO orders (order_id, status, customer_id, name, email, contact_number, address, total_price) VALUES
('ORD001', 'PLACED', 'CUST1', 'John Doe', 'john.doe@example.com', '1234567890', '1234 Elm Street, Springfield, USA', 50.00),
('ORD002', 'DELIVERED', 'CUST2', 'Jane Smith', 'jane.smith@example.com', '0987654321', '5678 Oak Street, Metropolis, USA', 15.00);

-- Insert initial data into the order_items table
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price) VALUES
('OI001', 1, 'product-1', 2, 20.00),
('OI002', 1, 'product-2', 1, 10.00),
('OI003', 2, 'product-3', 1, 15.00);