## RESTful application that enables creating account in PLN and exchanging PLN to USD and vice versa ##
Exchange rates are official NBP exchange rates available from NBP API http://api.nbp.pl/ 
 
To run the app locally, first create database using docker-compose.yaml file provided
in resources.

After compiling and running the application locally, swagger will be available at: http://localhost:8080/exchangeCurrency/swagger-ui/index.html

### Functionalities/changes to be made: ###

1. History of all operations on an account (snapshots of account balances at points of time 
when the balance changes as well as exchange rates at which operations were performed).
2. Account number as a unique sequence of numbers and not UUID.
3. Return new account balance as well as exchange rate from PATCH /exchange endpoint instead of 204 no content.
Currently details of operation and exchange rate are logged but not returned in the response.
4. Functionality that deals with the situation that client 
with a given lastname (actually it should be pesel or email),
that is trying to create an account, exists in the database.
5. Creating account for customer for given login and password and then authenticating requests with JWT.
6. Dependency management section in pom. Currently the application has only one module, 
so managing dependencies is easy but in case the app gets bigger it is worth adding 
dependency management section to parent pom so that all modules use dependencies 
in the same versions.
7. If the app gets bigger, it could be worth to add facade layer over services. 
Currently the business logic in services is simple so adding facade would be unnecessary complication. 
8. E2E tests e.g. using wiremock library.
9. More currencies than just PLN and USD.
