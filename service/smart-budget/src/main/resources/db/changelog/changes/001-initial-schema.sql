--liquibase formatted sql

--changeset migration:001
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    fio VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    currency_code CHARACTER(3) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id)
        REFERENCES users (id)
);

CREATE TABLE category (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    icon_link TEXT,
    mcc_list CHARACTER(4)[],
    merchant_name_list VARCHAR(255)[]
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    operation_time TIMESTAMP WITH TIME ZONE NOT NULL,
    amount_value NUMERIC(19, 2) NOT NULL,
    amount_currency CHARACTER(3) NOT NULL,
    amount_in_account_currency NUMERIC(19, 2) NOT NULL,
    mcc CHARACTER(4),
    merchant_name VARCHAR(255),
    category_id UUID,
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id)
        REFERENCES account (id),
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id)
        REFERENCES category (id)
);