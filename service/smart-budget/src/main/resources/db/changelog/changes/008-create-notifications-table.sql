--liquibase formatted sql

--changeset migration:007
CREATE TABLE notifications (
    id            UUID         NOT NULL PRIMARY KEY,
    user_id       UUID         NOT NULL REFERENCES users (id),
    budget_id     UUID         NOT NULL REFERENCES budget (id),
    email         VARCHAR(255) NOT NULL,
    error_message TEXT,
    sent_at       TIMESTAMP WITH TIME ZONE NOT NULL
);