# Mytheresa Promotions Test

## Description

We want you to implement a REST API endpoint that given a list of products, applies some discounts to them and can be
filtered.

You are free to choose whatever language and tools you are most comfortable with. Please add instructions on how to run
it and publish it in Github.

## What we expect

- Code structure/architecture must fit this use case, as simple or as complex needed to complete what is asked for.
- Code must be testable without requiring networking or the filesystem. Tests should be runnable with 1 command.
- The project must be runnable with 1 simple command from any machine.
- Explanations on decisions taken

Given this list of products:

```json
{
  "products": [
    {
      "sku": "000001",
      "name": "BV Lean leather ankle boots",
      "category": "boots",
      "price": 89000
    },
    {
      "sku": "000002",
      "name": "BV Lean leather ankle boots",
      "category": "boots",
      "price": 99000
    },
    {
      "sku": "000003",
      "name": "Ashlington leather ankle boots",
      "category": "boots",
      "price": 71000
    },
    {
      "sku": "000004",
      "name": "Naima embellished suede sandals",
      "category": "sandals",
      "price": 79500
    },
    {
      "sku": "000005",
      "name": "Nathane leather sneakers",
      "category": "sneakers",
      "price": 59000
    }
  ]
}
```

The prices are integers for example, 100.00€ would be 10000.

You can store the products as you see fit (json file, in memory, rdbms of choice)

### Given that:

- Products in the boots category have a 30% discount.
- The product with sku = 000003 has a 15% discount.
- When multiple discounts collide, the most restrictive discount must be applied. Provide a single endpoint

### GET /products

- Can be filtered by category as a query string parameter
  (optional) Can be filtered by priceLessThan as a query string parameter, this filter applies before discounts are
  applied and will show products with prices lesser than or equal the value provided.
- Returns a list of Product with the given discounts applied when necessary

### Product model

- price.currency is always EUR
- When a product does not have a discount, price.final and price.original should be the same number and
  discount_percentage should be null.
- When a product has a discount price.original is the original price, price.final is the amount with the discount
  applied and discount_percentage represents the applied discount with the % sign.

Example product with a discount of 30% applied.

```json
{
  "sku": "000001",
  "name": "BV Lean leather ankle boots",
  "category": "boots",
  "price": {
    "original": 89000,
    "final": 62300,
    "discount_percentage": "30%",
    "currency": "EUR"
  }
}
```

Example product without a discount

```json
{
  "sku": "000001",
  "name": "BV Lean leather ankle boots",
  "category": "boots",
  "price": {
    "original": 89000,
    "final": 89000,
    "discount_percentage": null,
    "currency": "EUR"
  }
}
```

# Solution

The proposed solution is based on Java and Spring Boot with Maven. A maven wrapper is included so the project is self executable with the following command:
```bash
$ ./mvnw spring-boot:run
```

To execute the tests:
```bash
$ ./mvnw test
```

The design approach is using Hexagonal Architecture to organize the packages and classes. It's splited in three different parts:
1. Application: Contains the entry points on the application. In this case it's only a package called API that contains the RESTController used to handle the requests from the clients.
2. Domain: Contains the logic of the application, and it's composed by:
      1. Model: Package that contains all the data objects and basic domain logic (example: how to apply discounts to products).
      2. Ports: Package that contains all the external needed or provided functionality of the domain. In this case there are two external dependencies that are the two repositories, and the Service containing the logic to serve the API.
3. Infrastructure: All the packages needed to implement dependencies and datasource. In this case the only needed is a Repository to store the constants of Products and Discounts.

## Considerations
The list of assumptions and decisions made for this exercise are:
- The order in which the products are returned by the API does not matter, as it's not specified in the requirements.
- Detailed management of exceptions in the API not implemented, there is no specific codes for errors or information to return, so in case the parameters were not correct only a BadRequest is returned.
- Restrictive discount is understood as the discount that reduces the price the most, if otherwise a comparison on the discount calculation should be changed.
- Data in repository is static and no endpoint added to create new Products or Discounts, as was not part of the exercise. Basic tests implemented in the repository to verify the methods work correctly with the exercise data.
- Discounts stored as an Integer representing the percentage value. Example: 29% discount is stored as 29. 

## Examples
Some example requests to test the solution:

### No parameters provided
GET http://localhost:8080/api/products

Results in a list of all products stored in the service with the corresponding discounts applied.
```json
[
  {
    "sku": "000003",
    "name": "Ashlington leather ankle boots",
    "category": "Boots",
    "price": {
      "currency": "EUR",
      "originalPrice": 71000,
      "finalPrice": 49700,
      "discountPercentage": "30%"
    }
  },
  {
    "sku": "000002",
    "name": "BV Lean leather ankle boots",
    "category": "Boots",
    "price": {
      "currency": "EUR",
      "originalPrice": 99000,
      "finalPrice": 69300,
      "discountPercentage": "30%"
    }
  },
  {
    "sku": "000001",
    "name": "BV Lean leather ankle boots",
    "category": "Boots",
    "price": {
      "currency": "EUR",
      "originalPrice": 89000,
      "finalPrice": 62300,
      "discountPercentage": "30%"
    }
  },
  {
    "sku": "000005",
    "name": "Nathane leather sneakers",
    "category": "Sneakers",
    "price": {
      "currency": "EUR",
      "originalPrice": 59000,
      "finalPrice": 59000,
      "discountPercentage": null
    }
  },
  {
    "sku": "000004",
    "name": "Naima embellished suede sandals",
    "category": "Sandals",
    "price": {
      "currency": "EUR",
      "originalPrice": 79500,
      "finalPrice": 79500,
      "discountPercentage": null
    }
  }
]
```

### Category provided
GET http://localhost:8080/api/products?category=sandals

Only one product is returned, because only one sandal is present in the repository. Doesn't have any discount associated to it.

Note: Capitalisation in the parameter category is not relevant.
```json
[
  {
    "sku": "000004",
    "name": "Naima embellished suede sandals",
    "category": "Sandals",
    "price": {
      "currency": "EUR",
      "originalPrice": 79500,
      "finalPrice": 79500,
      "discountPercentage": null
    }
  }
]
```

### Invalid category provided
GET http://localhost:8080/api/products?category=sandalsss

This returns a Bad Request response with code 400, as the category string provided does not match any of the valid ones.

### PriceLessThan provided
GET http://localhost:8080/api/products?priceLessThan=71000

Returns a list of only those products which original price was less than 71000 regardless of the category.

Note: This parameter only accepts numerical values higher than 0. If not a Bad Request response with code 400 is returned.
```json
[
  {
    "sku": "000003",
    "name": "Ashlington leather ankle boots",
    "category": "Boots",
    "price": {
      "currency": "EUR",
      "originalPrice": 71000,
      "finalPrice": 49700,
      "discountPercentage": "30%"
    }
  },
  {
    "sku": "000005",
    "name": "Nathane leather sneakers",
    "category": "Sneakers",
    "price": {
      "currency": "EUR",
      "originalPrice": 59000,
      "finalPrice": 59000,
      "discountPercentage": null
    }
  }
]
```

### Both parameters provided
GET http://localhost:8080/api/products?category=boots&priceLessThan=71000

Returns only those products that match the two filters as expected.
```json
[
  {
    "sku": "000003",
    "name": "Ashlington leather ankle boots",
    "category": "Boots",
    "price": {
      "currency": "EUR",
      "originalPrice": 71000,
      "finalPrice": 49700,
      "discountPercentage": "30%"
    }
  }
]
```