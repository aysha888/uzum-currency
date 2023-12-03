CREATE TABLE currencies (
    id INT GENERATED ALWAYS AS IDENTITY,
    num_code VARCHAR(10) NOT NULL,
    char_code VARCHAR(10) NOT NULL UNIQUE,
    nominal INT NOT NULL,
    currency_name VARCHAR(100) NOT NULL,
    rate NUMERIC(15, 2) NOT NULL,
    comission_from INT NOT NULL,
    comission_to INT NOT NULL,
    date VARCHAR(10) NOT NULL
);

CREATE TABLE accounts (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    balance NUMERIC(20, 2) NOT NULL,
    currency_id INT NOT NULL
);