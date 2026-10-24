--liquibase formatted sql

--changeset migration:005
INSERT INTO users (id, fio, email, password) VALUES
    ('aaaaaaaa-0000-0000-0000-000000000001', 'Gleb Crab',     'test@example.com',    'password'),
    ('aaaaaaaa-0000-0000-0000-000000000002', 'Sarah Lobster', 'newtest@example.com', 'password');

INSERT INTO account (id, user_id, currency_code, amount) VALUES
    ('bbbbbbbb-0000-0000-0000-000000000001', 'aaaaaaaa-0000-0000-0000-000000000001', 'BYN', 100.00),
    ('bbbbbbbb-0000-0000-0000-000000000002', 'aaaaaaaa-0000-0000-0000-000000000002', 'BYN', 200.00);