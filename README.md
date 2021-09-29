# MigNeves-Midterm_Project

## Table of Contents

1. [**Introduction**](#Introduction)
    1. [Functions](#Functions)
2. [**Installation**](#Installation)
    1. [Setup](#Setup)
3. [**Users**](#Menu)
    1. [How to navigate the menu](#How-to-navigate-the-menu)
4. [**Accounts**](#Help-menu)
5. [**Walkthrough the Available Requests**](#Description-of-the-CRM-Components)
    1. [Admin Only Requests](#Lead)
    2. [Account Holder Requests](#Contact)
    3. [Requests Without Authorization](#SalesRep)

## Introduction

The banking application only works with euros


## Installation

Clone or download the project from this git repository.

**Setup** the recommended database through the following mySQL commands:
```
CREATE DATABASE IF NOT EXISTS banking;
CREATE DATABASE IF NOT EXISTS banking_test;

CREATE USER IF NOT EXISTS 'ironhacker'@'localhost' IDENTIFIED BY '1r0nh4ck3r';
GRANT ALL PRIVILEGES ON banking.* TO 'ironhacker'@'localhost';
GRANT ALL PRIVILEGES ON banking_test.* TO 'ironhacker'@'localhost';
FLUSH PRIVILEGES;
```

To run the application:
    - Open the repository as a project in a IDE;
    - Run the main application "BankingApplication.java", on the path:
      `./src/main/java/com/ironhack/midtermProject/BankingApplication.java`
    - Run the tests present in the tests' path:
      `./src/test/java`

It is also possible to change the properties in the application.properties file to a custom user and database. However, the tests
properties are defined in each test.

## Users

This banking application includes three types of Users: Administrator, Account Holder and Third Party.

### Administrator (Admin)

The Admin has the permission to:
- Create new banking accounts,
- Change the balance of existing accounts,
- Access banking accounts information,
- Add Third Party users.

### Account Holder

An Account Holder is a banking client and can own or co-own multiple banking accounts. 
The Account Holder has the permission to:
- Access his accounts informations (both owned or co-owned),
- Do transactions from his accounts.

### Third Party

The Third Party has the permission to:
- Make transactions with banking accounts when providing his own hashed key.

## Banking Accounts

There are currently 4 diferent types of Banking Accounts, which are Savings, Credit Card, Checking and Student Checking
All accounts have some common functionalities and properties:
-  All accounts have a unique identifier with a prefix, identifying which type of account the id belongs to:
    -  ```CC_#``` for Credit Card accounts,
    -  ```SA_#``` for Savings accounts,
    -  ```CH_#``` for Checking accounts,
    -  ```SC_#``` for Student Checking accounts. They also have a balance, a creation date and a secret key.
- Accounts can be frozen when a possible fraud is detected,
- When a account is frozen it can not do any transaction to other accounts,
- All accounts have a default penalty fee value of 40€,
- All accounts have a primary owner (Account Holder user) and may have a secondary owner.

### Savings

Apart from the common account functionalities Savings accounts also have:
- A minimum balance:
    - If the account balance reaches a lower value the penalty fee is applied
- An interest rate applied yearly through the equation:
    - ```newBalance = oldBalance * (1 + interestRate)```
- Transactions that would lead to a negative balance are not allowed.

### Credit Card 

Apart from the common account functionalities Credit Card accounts also have:
- A credit limit
- An interest rate applied monthly through the equation:
    - ```newBalance = oldBalance * (1 + (1/12)*interestRate)```
- Transactions that would lead to a credit greater than the limit are not allowed and, as so, the penalty fee is never applied.

### Checking

Apart from the common account functionalities Crhecking accounts also have:
- A minimum balance:
    - If the account balance reaches a lower value the penalty fee is applied
- A monthly maitenance fee applied monthly through the equation:
    - ```newBalance = oldBalance - montlyMaintenanceFee```
- Transactions that would lead to a negative balance are not allowed.

### Student Checking

Student Checking accounts do not have any other functionalities apart from the basic account functionalities.

## Walkthrough the Available Requests

The possible requests can be divided in terms of permissions.

### Admin Allowed Requests

#### Access Account Information

To access the balance and other informations of a particular account the Admin user may do a **get** request through the following route:
```/account/{id}``` where id is the Account's identifier, for example:
```
/account/CC_1
```
After the request the Admin user will receive a response such as:
```
{
    "id": "CC_1",
    "balance": {
        "currency": "EUR",
        "amount": 1488.09
    },
    "penaltyFee": {
        "currency": "EUR",
        "amount": 40.00
    },
    "creationDate": "2000-01-01",
    "status": "ACTIVE",
    "primaryOwnerName": "Jim Halpert",
    "secondaryOwnerName": "Pam Beesly",
    "creditLimit": {
        "currency": "EUR",
        "amount": 300.00
    },
    "interestRate": 0.2000
}
```

#### Change Account Balance

To change the balance of a particular account the Admin user may do a **patch** request through the following route:
```/balance/{id}``` where id is the Account's identifier, for example:
```
/balance/CC_1
```
The Admin user must also provide the new balance in euros (€) through the request body in the following way:
```
    "balance": {
        "amount": 300.00
    }
```

After the request the Admin user will receive a response with the updated balance value such as:
```
{
    "id": "CC_1",
    "balance": {
        "currency": "EUR",
        "amount": 300.00
    },
    "penaltyFee": {
        "currency": "EUR",
        "amount": 40.00
    },
    "creationDate": "2000-01-01",
    "status": "ACTIVE",
    "primaryOwnerName": "Jim Halpert",
    "secondaryOwnerName": "Pam Beesly",
    "creditLimit": {
        "currency": "EUR",
        "amount": 300.00
    },
    "interestRate": 0.2000
}
```
If the user provides an **invalid** value for the balance (lower than the credit limit for Credit Card accounts or negative for all other accounts) the balance will be updated with the **minimum** allowed value.

#### Create a Banking Account

To create a new banking account the Admin user may do a **post** request through the following route:
```/create/account```

The Admin user must also provide the new account information through the request body:
- accountType: Can have the values CHECKING, SAVINGS or CREDIT_CARD and defines what type of account to create,
- balance: Defines the balance value for the account, balance values ower than 0 or lower than the credit limit for Credit Card will default to the minimum allowed value,
- primaryOwnerId: Identifier of the primary Account Holder user,
- secondaryOwnerId (optional): Identifier of the secondary Account Holder user
```
    
    "balance": {
        "amount": 300.00
    }
```

After the request the Admin user will receive a response with the updated balance value such as:
```
{
    "id": "CC_1",
    "balance": {
        "currency": "EUR",
        "amount": 300.00
    },
    "penaltyFee": {
        "currency": "EUR",
        "amount": 40.00
    },
    "creationDate": "2000-01-01",
    "status": "ACTIVE",
    "primaryOwnerName": "Jim Halpert",
    "secondaryOwnerName": "Pam Beesly",
    "creditLimit": {
        "currency": "EUR",
        "amount": 300.00
    },
    "interestRate": 0.2000
}
```
If the user provides an **invalid** value for the balance (lower than the credit limit for Credit Card accounts or negative for all other accounts) the balance will be updated with the **minimum** allowed value.

## Use Case Diagram

![Use Case Diagram](https://user-images.githubusercontent.com/66126956/133005492-c50e2d46-3ceb-482f-8f9d-9accfb914a5d.png)

## Class Diagram

![use case diagram](https://user-images.githubusercontent.com/66126956/133005493-d86adb38-76d9-4151-8355-6e1dfa7322cd.png)


## Notes

- Some tests, from PrinterTest, will not pass in Ubuntu or Mac machines due to the different type of linebreak.

## The Team

by:  
- [MigNeves](https://github.com/MigNeves)
