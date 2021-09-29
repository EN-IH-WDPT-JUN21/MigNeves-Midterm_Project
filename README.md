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



### Functions



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

Apart from the common account functionalities Savings accounts have a minimum balance, an interest rate and a last update date. Every year the interest rate is applied to the balance's account through the following equation:
```
newBalance = oldBalance * (1 + interestRate)
```
The last update date keeps track of when the last interest rate was applied to the account's balance.
The minimum balance defines the minimum amount for the account balance. If the balance ever goes below this minimum balance the penalty fee is applied.
All transactions that lead to a negative balance will fail.

### Credit Card 
The Savings account has the following properties:
- id (CC_#),
- balance,
- creation date,
- secret key,
- penalty fee,
- primary owner,
- (optional) secondary owner,
- status,
- credit limit,
- interest rate,
- last update date;

Apart from the common account functionalities Credit Card accounts have a credit limit, an interest rate and a last update date. Every month the interest rate is applied to the balance's account through the following equation:
```
newBalance = oldBalance * (1 + (1/12)*interestRate)
```
The last update date keeps track of when the last interest rate was applied to the account's balance.
The credit limit defines the maximum amount of credit allowed for the account. The Credit Card can never reach a higher credit than the credit limit and, as such, the penalty fee is never applied. All transactions that lead to a credit higher than the credit limit fail.

### Checking
The Savings account has the following properties:
- id (CH_#),
- balance,
- creation date,
- secret key,
- penalty fee,
- primary owner,
- (optional) secondary owner,
- status,
- minimum balance,
- montly maintenance fee,
- last update date;

Apart from the common account functionalities Credit Card accounts have a minimum balance, a monthly maintenance fee and a last update date. Every month the montlhy maintenance fee is deducted from the balance's account through the following equation:
```
newBalance = oldBalance - montlyMaintenanceFee
```
The last update date keeps track of when the last montlhy maitenance fee was applied to the account's balance.
The minimum balance, similar to Savings accounts defines the minimum amount for the account balance. If the balance ever goes below this minimum balance the penalty fee is applied.
All transactions that lead to a negative balance will fail.

### Student Checking
The Savings account has the following properties:
- id (SC_#),
- balance,
- creation date,
- secret key,
- penalty fee,
- primary owner,
- (optional) secondary owner,
- status;

Student Checking accounts do not have any functionalities apart from the basic account functionalities.

## Walkthrough the Available Requests

The possible requests can be divided in terms of permissions.

### Admin Requests

1. Access Account Balance

To access the balance of a particular account the Admin user may do a get request through the following route:
```
/balance/{id}
```
where id is the Account's identifier (ex. CC_1)

2. Change Account Balance

## Use Case Diagram

![Use Case Diagram](https://user-images.githubusercontent.com/66126956/133005492-c50e2d46-3ceb-482f-8f9d-9accfb914a5d.png)

## Class Diagram

![use case diagram](https://user-images.githubusercontent.com/66126956/133005493-d86adb38-76d9-4151-8355-6e1dfa7322cd.png)


## Notes

- Some tests, from PrinterTest, will not pass in Ubuntu or Mac machines due to the different type of linebreak.

## The Team

by:  
- [MigNeves](https://github.com/MigNeves)
