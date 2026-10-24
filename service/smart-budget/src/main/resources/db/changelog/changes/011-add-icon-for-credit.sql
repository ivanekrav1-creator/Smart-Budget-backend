--liquibase formatted sql

--changeset migration:011

UPDATE category SET icon = '💸' WHERE name = 'Credit';