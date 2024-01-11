CREATE TABLE orders
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    productId  VARCHAR(120) NOT NULL,
    unitPrice  INT          NOT NULL,
    quantity   INT          NOT NULL,
    totalPrice INT          NOT NULL,
    userId     VARCHAR(255) NOT NULL,
    orderId    VARCHAR(255) NOT NULL UNIQUE,
    createdAt  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO orders
    (productId, unitPrice, quantity, totalPrice, userId, orderId, createdAt)
VALUES
    ('product1', 1000, 10, 2000, 'user', 'order', NOW());