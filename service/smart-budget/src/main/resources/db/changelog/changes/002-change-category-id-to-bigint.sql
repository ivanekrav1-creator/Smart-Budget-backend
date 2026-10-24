--liquibase formatted sql

--changeset migration:002
ALTER TABLE transactions DROP CONSTRAINT fk_transaction_category;

ALTER TABLE transactions DROP COLUMN category_id;
ALTER TABLE transactions ADD COLUMN category_id BIGINT;

DROP TABLE category;
CREATE TABLE category (
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    icon_link          TEXT,
    mcc_list           CHARACTER(4)[],
    merchant_name_list VARCHAR(255)[]
);

ALTER TABLE transactions
    ADD CONSTRAINT fk_transaction_category
    FOREIGN KEY (category_id) REFERENCES category (id);