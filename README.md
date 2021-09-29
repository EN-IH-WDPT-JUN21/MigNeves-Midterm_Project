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

In this banking application there are three types of Users (Admin, AccountHolder and ThirdParty), however, ThirdParty User's do not need to authenticate through the common username and password method.

### Admin

The Admin User is responsible for the creation and management of User and Banking accounts. When authenticated they have permission to access banking account information as well as change the balance.

### AccountHolder

An AccountHolder is a banking client and can own or co-own multiple banking accounts. Through this application the AccountHolder can access his accounts' informations as well as process transactions from one of his accounts to another account.

### ThirdParty

A ThirdParty can only send or receive money from existing accounts by providing his own unique HashedKey. 


## Use Case Diagram

![Use Case Diagram](https://user-images.githubusercontent.com/66126956/133005492-c50e2d46-3ceb-482f-8f9d-9accfb914a5d.png)

## Class Diagram

![use case diagram](https://user-images.githubusercontent.com/66126956/133005493-d86adb38-76d9-4151-8355-6e1dfa7322cd.png)


## Notes

- Some tests, from PrinterTest, will not pass in Ubuntu or Mac machines due to the different type of linebreak.

## The Team

by:  
- [MigNeves](https://github.com/MigNeves)
