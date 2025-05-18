CREATE TABLE inventory
(
    inventory_id                 BIGSERIAL PRIMARY KEY,
    product_id         VARCHAR(50) NOT NULL,
    available_quantity INT         NOT NULL DEFAULT 0,
    reserved_quantity  INT         NOT NULL DEFAULT 0,
    last_updated       TIMESTAMP,
    version            INT         NOT NULL DEFAULT 0
);

CREATE TABLE inventory_history
(
    id          BIGSERIAL PRIMARY KEY,
    product_id  VARCHAR(50) NOT NULL,
    change_type VARCHAR(20) NOT NULL, -- RESERVE, RELEASE, RESTOCK
    quantity    INT         NOT NULL,
    order_id    VARCHAR(50), -- Для аудита
    created_at  TIMESTAMP
);