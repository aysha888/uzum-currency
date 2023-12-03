# Currency Converter

## Description

Currency Converter is a Spring Boot application for currency conversion. It allows users to convert currency from one denomination to another.

## Prerequisites

Before you begin, ensure you have met the following requirements:
    docker and docker-compose
    or
    docker desktop

## Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/aysha888/uzum-currency.git
    cd uzum-currency
    ```



2. Run application using docker desktop with this command 
    ```bash
    docker compose up -d
    ```
    or docker-compose
    ```bash
    docker-compose up -d
    ```

4. Access the Application:

    Use your api client to access rest api [http://localhost:8080](http://localhost:8080).

## REST api docs

### 1. Get Official Rates

**Endpoint:** `/officialrates`

- **Method:** GET

**Request Parameters:**
- `date` (String, required): The date for which you want to get the official currency rates.
- `currency` (String, optional, default: "USD"): The currency code for which you want to get the official rate.

**Response:**
- *Successful Response:* Returns the official currency rates for the specified date and currency code.
    ```jsonschema
        {
            "rate": string
        }
    ```

### 2. Set Commission

**Endpoint:** `/comission`

- this method is protected by Api-Key header that specified in application.properties, returns 403 if wrong or missing.

- **Method:** POST

**Request Body:**
- `ComissionDto`: The commission details to be set.
    ```jsonschema
    {
        "charCode": string,
        "comissionFrom:: string, // if we convert from this charcode
        "comissionTo": string
    }
    ```

**Response:**
- *Successful Response:* Returns a success message after setting the commission.
    ```jsonschema
        {
            "success": boolean
        }
    ```

### 3. Dry Conversion

**Endpoint:** `/convert`

- **Method:** GET

**Request Parameters:**
- `from` (String, required): The currency code from which you want to convert.
- `to` (String, required): The currency code to which you want to convert.
- `amount` (BigDecimal, required): The amount to be converted.

**Response:**
- *Successful Response:* Returns the result of the dry conversion.
```jsonschema
{
    "result": string
}
```

### 4. Perform Conversion

**Endpoint:** `/convert`

- **Method:** POST

**Request Body:**
- `ConversionDto`: The details for the currency conversion.
```jsonschema
{
    "from": string,
    "to": string,
    "amount": number
}
```

**Response:**
- *Successful Response:* Returns the result of the currency conversion.
```jsonschema
{
    "result": string
}
```

### Error Handling

The controller handles the following custom exceptions:

1. `InvalidCurrencyCharCode`: Returns a 404 NOT FOUND response with error details.
2. `NotEnoughCurrency`: Returns a 403 FORBIDDEN response with error details.

*Note: The error responses follow the format specified in the `ErrorPresenter` class.*

# What can be improved?
## Write more test cases
## Refactor to adhere to Clean Architecture
