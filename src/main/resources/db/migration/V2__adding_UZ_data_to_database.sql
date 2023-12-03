INSERT INTO currencies (num_code, char_code, nominal, currency_name, rate, comission_from, comission_to, date) VALUES ('860', 'UZS', 1, 'Узбекский Сум', 1, 0, 0, '31.12.2023');
INSERT INTO accounts (balance, currency_id) VALUES (1000000000000, (SELECT id FROM currencies WHERE char_code = 'UZS'));

