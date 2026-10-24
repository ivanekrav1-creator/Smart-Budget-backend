--liquibase formatted sql

--changeset migration:007
INSERT INTO category (name, mcc_list, merchant_name_list) VALUES
    ('Credit','{6010}','{MerchantBank}');