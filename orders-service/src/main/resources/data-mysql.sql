-- Insert initial data into the orders table
INSERT INTO orders (order_identifier, status, customer_id, customer_name, customer_email, customer_contact_number, customer_address) VALUES
('ORD001', 'PLACED', 101, 'John Doe', 'john.doe@example.com', '1234567890', '1234 Elm Street, Springfield, USA'),
('ORD002', 'DELIVERED', 102, 'Jane Smith', 'jane.smith@example.com', '0987654321', '5678 Oak Street, Metropolis, USA');

-- Insert initial data into the order_items table
INSERT INTO order_items (order_item_identifier, order_id, product_id, quantity, price) VALUES
('OI001', 1, 'P001', 2, 20.00),
('OI002', 1, 'P002', 1, 10.00),
('OI003', 2, 'P003', 1, 15.00);