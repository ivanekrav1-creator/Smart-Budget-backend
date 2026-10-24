--liquibase formatted sql

--changeset migration:010
ALTER TABLE category RENAME COLUMN icon_link TO icon;


UPDATE category SET icon = '🛒' WHERE name = 'Grocery Stores and Supermarkets';
UPDATE category SET icon = '🍕' WHERE name = 'Restaurants';
UPDATE category SET icon = '⛽' WHERE name = 'Fuel';
UPDATE category SET icon = '💊' WHERE name = 'Pharmacies';
UPDATE category SET icon = '🚕' WHERE name = 'Transport';
UPDATE category SET icon = '🎮' WHERE name = 'Digital Goods';
UPDATE category SET icon = '💻' WHERE name = 'Electronics';
UPDATE category SET icon = '👗' WHERE name = 'Clothing';
UPDATE category SET icon = '🏋️' WHERE name = 'Sporting Goods';
UPDATE category SET icon = '🎬' WHERE name = 'Cinema';
UPDATE category SET icon = '💇' WHERE name = 'Beauty';
UPDATE category SET icon = '🐾' WHERE name = 'Pet Shops';
UPDATE category SET icon = '🏨' WHERE name = 'Lodging';