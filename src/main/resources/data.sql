-- Claudio Menin
-- SQLite Database creation
-- INSERT queries are used to create fake data

-- Table used to store the historical transactions of the account balance
DROP TABLE IF EXISTS accountbalance;
CREATE TABLE IF NOT EXISTS accountbalance (
    id INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    date DATE,
    balance DOUBLE,
    available_balance DOUBLE,
    currency VARCHAR(255)
);
INSERT INTO accountbalance (id, date, balance, available_balance, currency) VALUES
    (1, '2023-01-01', 1000.50, 800.75, 'USD'),
    (2, '2023-01-02', 950.20, 700.50, 'EUR'),
    (3, '2023-01-03', 1200.75, 1000.25, 'GBP'),
    (4, '2023-01-04', 800.30, 600.10, 'JPY'),
    (5, '2023-01-05', 1500.80, 1300.40, 'AUD');

-- Table used to generate the ID
DROP TABLE IF EXISTS id_gen;
CREATE TABLE id_gen (
  idname VARCHAR(255) PRIMARY KEY NOT NULL,
  nextid INTEGER NOT NULL
);
INSERT INTO id_gen (idname, nextid) VALUES ('accountbalance', 5);



