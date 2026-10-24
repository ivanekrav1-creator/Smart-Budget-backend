--liquibase formatted sql

--changeset migration:004
ALTER TABLE users ADD COLUMN password VARCHAR(255) NOT NULL;