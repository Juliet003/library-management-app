# Library Management System

## Project Description
Build a Library Management System API using Spring Boot. The system should allow librarians to manage books, patrons, and borrowing records.


### Prerequisites

Ensure that input and output data are formatted in JSON.

### Getting Started

To begin using the Library Management System Application, follow these steps:

1. Install the required dependencies by running:
   ```shell
   mvn install
    ```

2. Start the application with:
    ```shell
    mvn spring-boot:run
    ```

### API Endpoints
The API exposes the following endpoints:

#### Book management endpoints:
- GET /api/books: Retrieve a list of all books.
- GET /api/books/{id}: Retrieve details of a specific book by ID.
- POST /api/books: Add a new book to the library.
- PUT /api/books/{id}: Update an existing book's information.
- DELETE /api/books/{id}: Remove a book from the library.

#### Patron management endpoints:
- GET /api/patrons: Retrieve a list of all patrons.
- GET /api/patrons/{id}: Retrieve details of a specific patron by ID.
- POST /api/patrons: Add a new patron to the system.
- PUT /api/patrons/{id}: Update an existing patron's information.
- DELETE /api/patrons/{id}: Remove a patron from the system.

#### Borrowing endpoints:
- POST /api/borrow/{bookId}/patron/{patronId}: Allow a patron to borrow a book.
- PUT /api/return/{bookId}/patron/{patronId}: Record the return of a borrowed book by a patron.

### Testing
Unit tests can be run using the following command:

```shell
    mvn test
   ```

### Technology Used:
* Java
* SpringBoot
* Junit & Mockito
* Git and GitHub
* Postman

### Conclusion
The Library Management System API offers a robust solution for the management of books, patrons, and borrowing records. 
With a set of well-defined endpoints, clients can seamlessly interact with the system to perform various operations. 
Our service is designed to handle different scenarios and adapt to the dynamic nature of library operations.
We encourage you to explore the endpoints and functionalities provided by our API and learn more from our [Postman 
Documentation](https://documenter.getpostman.com/view/31876952/2sA3JRYyQk) for a detailed guide on using our service effectively.

Feel free to reach out with any questions, feedback, or suggestions.