--liquibase formatted sql

--changeset migration:009
ALTER TABLE transactions ADD COLUMN is_excluded BOOLEAN NOT NULL DEFAULT FALSE;