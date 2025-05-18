CREATE TABLE inventories
(
    inventories_id                 BIGSERIAL PRIMARY KEY,
    product_id         VARCHAR(50) NOT NULL,
    available_quantity BIGINT         NOT NULL DEFAULT 0,
    reserved_quantity  BIGINT         NOT NULL DEFAULT 0,
    last_updated       TIMESTAMP,
    version            BIGINT         NOT NULL DEFAULT 0
);

CREATE TABLE inventory_histories
(
    inventory_histories_id          BIGSERIAL PRIMARY KEY,
    product_id  VARCHAR(50) NOT NULL,
    change_type VARCHAR(20) NOT NULL, -- RESERVE, RELEASE, RESTOCK
    quantity    BIGINT         NOT NULL,
    order_id    VARCHAR(50), -- Для аудита
    created_at  TIMESTAMP
);

CREATE TABLE reserve_requests
(
    reserve_requests_id             BIGSERIAL PRIMARY KEY,
    product_id  VARCHAR(50) NOT NULL,
    order_id    VARCHAR(50) NOT NULL,
    quantity    BIGINT         NOT NULL,
    status      VARCHAR(50) NOT NULL
)