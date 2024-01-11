CREATE TABLE catalog
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id   VARCHAR(120) NOT NULL UNIQUE,
    product_name VARCHAR(255) NOT NULL,
    stock        INT          NOT NULL,
    unit_price   INT          NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    mod_date     TIMESTAMP NULL,
);
insert into catalog(product_id, product_name, stock, unit_price)
values ('product1', 'product1', 10, 1000);
insert into catalog(product_id, product_name, stock, unit_price)
values ('product2', 'product2', 10, 1000);
insert into catalog(product_id, product_name, stock, unit_price)
values ('product3', 'product3', 1, 1000);
insert into catalog(product_id, product_name, stock, unit_price)
values ('pen', 'pen', 10, 1000);