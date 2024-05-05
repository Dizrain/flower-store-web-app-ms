-- Drop tables if they already exist
DROP TABLE IF EXISTS payments;

-- Creation of the payments table
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    payment_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date DATETIME NOT NULL,
    payment_method ENUM('CreditCard', 'PayPal', 'Crypto', 'ETF') NOT NULL
);