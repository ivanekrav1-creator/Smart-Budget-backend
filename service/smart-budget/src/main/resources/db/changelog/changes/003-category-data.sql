--liquibase formatted sql

--changeset migration:003
INSERT INTO category (name, mcc_list, merchant_name_list) VALUES
    ('Grocery Stores and Supermarkets', '{5411,5412}',      '{GreenHill Market}'),
    ('Restaurants',                     '{5812,5813,5814}', '{PizzaFleet,BurgerPoint,CoffeeLike}'),
    ('Fuel',                            '{5541,5552}',      '{PetroStar,GreenCharge EV}'),
    ('Pharmacies',                      '{5912,5122}',      '{VitaPlus}'),
    ('Transport',                       '{4121,4111,4131}', '{CityMove Taxi}'),
    ('Digital Goods',                   '{5815,5816}',      '{StreamBox}'),
    ('Electronics',                     '{5732,5045}',      '{TechZone}'),
    ('Clothing',                        '{5651,5661,5699}', '{WearStyle}'),
    ('Sporting Goods',                  '{5941,7941}',      '{SportMaster}'),
    ('Cinema',                          '{7832,7922}',      '{CinemaCity}'),
    ('Beauty',                          '{7230,7298}',      '{HairMaster}'),
    ('Pet Shops',                       '{5995}',           '{PetStore}'),
    ('Lodging',                         '{7011,7012}',      '{SleepInn Hotel}');