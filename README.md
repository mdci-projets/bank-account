# Bank Account Kata

## Directives
Dans la mesure de votre niveau de séniorité et d'expérience, nous vous proposons de résoudre ce kata avec une architecture hexagonale et en adoptant une routine TDD red/green/refactor.

## Bank Account
Bank account kata Think of your personal bank account experience When in doubt, go for the simplest solution Requirements

·         Deposit and Withdrawal

·         Account statement (date, amount, balance)

·         Statement printing

 
## User Story 1

In order to save money

As a bank client

I want to make a deposit in my account

 

## User Story 2

In order to retrieve some or all of my savings

As a bank client

I want to make a withdrawal from my account

 

## User Story 3

In order to check my operations

As a bank client

I want to see the history (operation, date, amount, balance) of my operations

## Credits
Inspiré par Les teams craftman de la SG CIB

## Starting the application
Normal Maven goals
```bash
mvn clean install
#or without tests
mvn clean install -DskipTests
```
```bash
java -jar katas-java/BankAccount/target/bank-0.0.1-SNAPSHOT.jar
```

Use rest APIs
```bash
The database already contains two clients with the following account_id (654321, 789123)
#Example for deposit use case
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST http://localhost:8200/account/654321/deposit?amount=200
#Example for withdraw use case
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST http://localhost:8200/account/654321/withdraw?amount=100
#Example for account statement use case
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8200/account/654321/operations
```
Functional tests are done via the rest APIs

