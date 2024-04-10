-- Drop table if it already exists
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS customers;
SET FOREIGN_KEY_CHECKS = 1;

-- Creation of the customers table
CREATE TABLE customers
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id    VARCHAR(255) NOT NULL,
    name           VARCHAR(255) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255) NOT NULL,
    address        VARCHAR(255) NOT NULL,
    UNIQUE (customer_id),
    UNIQUE (email)
);