CREATE DATABASE IF NOT EXISTS tp_bank;

USE tp_bank;

CREATE TABLE client (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(34) NOT NULL UNIQUE,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0,
    type ENUM('CURRENT', 'SAVINGS') NOT NULL,
    interest_rate DECIMAL(5, 2),
    client_id INT NOT NULL,

    FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE operation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    amount DECIMAL(15, 2) NOT NULL,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL,
    account_id INT NOT NULL,

    FOREIGN KEY (account_id) REFERENCES account(id)
);

INSERT INTO client (first_name, last_name, email)
VALUES ('Jean', 'Dupont', 'jean@example.fr');

INSERT INTO account
    (account_number, balance, type, client_id)
VALUES
    ('000001', 1000.00, 'CURRENT', 1);

INSERT INTO account
    (account_number, balance, type, interest_rate, client_id)
VALUES
    ('000002', 2500.00, 'SAVINGS', 2.50, 1);