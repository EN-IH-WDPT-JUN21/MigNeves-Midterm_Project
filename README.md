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

In this banking application there are three types of Users (Admin, AccountHolder and ThirdParty), however, ThirdParty User's do not require authentication through the usual username and password.

### Admin

The Admin User is responsible for the creation and management of Third Party users and Banking accounts. When authenticated they have permission to create new accounts, add third party users and access banking account information as well as change its balance.
The following information about the Admin user is stored in the database:
- id,
- name,
- encrypted password;

### Account Holder

An AccountHolder is a banking client and can own or co-own multiple banking accounts. Through this application the AccountHolder can access his accounts' informations as well as process transactions from one of his accounts to another account.
The following information about the Account Holder user is stored in the database:
- id,
- name,
- date of birth,
- primary address,
    - address,
    - city,
    - country,
    - postal code 
- (optionally) mailing address,
    - address,
    - city,
    - country,
    - postal code;

### Third Party

A Third Party can only send or receive money from existing accounts by providing his own unique HashedKey.
The following information about the Third Party user is stored in the database:
- id,
- name,
- hashed key;

## Banking Accounts

There are currently 4 diferent types of Banking Accounts, which are Savings, Credit Card, Checking and Student Checking
All accounts have some common functionalities. They all have a unique identifier that has a prefix, identifying which type of account the id belongs to. They also have a balance, a creation date and a secret key.
All accounts also have a status identifying if the account is active or frozen. A frozen account can not do transactions to other accounts.
All accounts have a default penalty fee of 40€, however Student Checking, and Credit Card accounts will never be penalized with this fee.
All accounts also have a primary owner and can optionally have a secondary owner. The owners are users of the type Account Holder.


### Savings
The Savings account has the following properties:
- id (SA_#),
- balance,
- creation date,
- secret key,
- penalty fee,
- primary owner,
- (optional) secondary owner,
- status,
- minimum balance,
- interest rate,
- last update date;

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
