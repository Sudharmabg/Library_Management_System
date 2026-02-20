# Library Management System

A comprehensive Library Management System built with Spring Boot, demonstrating Object-Oriented Programming principles, SOLID design principles, and design patterns.

## Features

### Core Features
- **Book Management**: Add, update, remove, and search books by title, author, or ISBN
- **Patron Management**: Register patrons, update information, and track borrowing history
- **Lending Process**: Checkout and return books with automatic status tracking
- **Inventory Management**: Real-time tracking of available and borrowed books

### Advanced Features
- **Multi-branch Support**: Manage multiple library branches and transfer books between them
- **Reservation System**: Reserve books that are currently checked out with automatic notifications
- **Recommendation System**: Get personalized book recommendations based on borrowing history

## Technology Stack

- **Java 17**
- **Spring Boot 4.0.3**
- **Maven** for dependency management
- **SLF4J** for logging
- **In-memory storage** (ConcurrentHashMap)

## Architecture & Design

### OOP Principles Applied

1. **Encapsulation**: All domain models encapsulate their data with private fields and public getters/setters
2. **Abstraction**: Interfaces like `Observer`, `Subject`, and `SearchStrategy` define contracts
3. **Inheritance**: Not explicitly used but can be extended (e.g., different patron types)
4. **Polymorphism**: Strategy pattern allows different search implementations

### SOLID Principles

1. **Single Responsibility Principle (SRP)**
   - Each service class has one responsibility (BookService, PatronService, LoanService, etc.)
   - Controllers handle only HTTP requests/responses
   - Repositories handle only data storage

2. **Open/Closed Principle (OCP)**
   - SearchStrategy interface allows adding new search types without modifying existing code
   - Observer pattern allows adding new notification types

3. **Liskov Substitution Principle (LSP)**
   - All SearchStrategy implementations can be substituted for the interface
   - All Observer implementations can be substituted

4. **Interface Segregation Principle (ISP)**
   - Small, focused interfaces (Observer, Subject, SearchStrategy)
   - Clients depend only on methods they use

5. **Dependency Inversion Principle (DIP)**
   - Services depend on repository abstractions
   - High-level modules don't depend on low-level modules

### Design Patterns

1. **Factory Pattern** (`EntityFactory`)
   - Centralizes object creation for Book, Patron, Loan, Branch, and Reservation
   - Generates unique IDs automatically

2. **Observer Pattern** (`NotificationService`, `PatronObserver`)
   - Notifies patrons when reserved books become available
   - Decouples notification logic from business logic

3. **Strategy Pattern** (`SearchStrategy`, `TitleSearchStrategy`, `AuthorSearchStrategy`, `ISBNSearchStrategy`)
   - Allows different search algorithms to be selected at runtime
   - Easy to add new search types

4. **Repository Pattern** (All Repository classes)
   - Abstracts data access logic
   - Provides clean separation between business and data layers

## Project Structure

```
src/main/java/com/airtribe/library/
├── controller/          # REST API endpoints
│   ├── BookController.java
│   ├── PatronController.java
│   ├── LoanController.java
│   ├── BranchController.java
│   ├── ReservationController.java
│   ├── RecommendationController.java
│   └── GlobalExceptionHandler.java
├── domain/             # Domain models
│   ├── Book.java
│   ├── BookStatus.java
│   ├── Patron.java
│   ├── Loan.java
│   ├── Branch.java
│   ├── Reservation.java
│   └── ReservationStatus.java
├── exception/          # Custom exceptions
│   ├── BookNotFoundException.java
│   ├── PatronNotFoundException.java
│   ├── BookNotAvailableException.java
│   └── BranchNotFoundException.java
├── patterns/           # Design pattern implementations
│   ├── EntityFactory.java
│   ├── Observer.java
│   ├── Subject.java
│   ├── NotificationService.java
│   ├── PatronObserver.java
│   ├── SearchStrategy.java
│   ├── TitleSearchStrategy.java
│   ├── AuthorSearchStrategy.java
│   └── ISBNSearchStrategy.java
├── repository/         # Data access layer
│   ├── BookRepository.java
│   ├── PatronRepository.java
│   ├── LoanRepository.java
│   ├── BranchRepository.java
│   └── ReservationRepository.java
├── service/           # Business logic layer
│   ├── BookService.java
│   ├── PatronService.java
│   ├── LoanService.java
│   ├── BranchService.java
│   ├── ReservationService.java
│   └── RecommendationService.java
└── LibraryManagementSystemApplication.java
```

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           DOMAIN LAYER                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────┐      ┌──────────────┐      ┌──────────────┐              │
│  │    Book      │      │   Patron     │      │    Loan      │              │
│  ├──────────────┤      ├──────────────┤      ├──────────────┤              │
│  │ -isbn        │      │ -patronId    │      │ -loanId      │              │
│  │ -title       │      │ -name        │      │ -isbn        │              │
│  │ -author      │      │ -email       │      │ -patronId    │              │
│  │ -pubYear     │      │ -history     │      │ -borrowDate  │              │
│  │ -branchId    │      │ -borrowed    │      │ -dueDate     │              │
│  │ -status      │      └──────────────┘      │ -returnDate  │              │
│  └──────────────┘                            └──────────────┘              │
│                                                                               │
│  ┌──────────────┐      ┌──────────────┐                                     │
│  │   Branch     │      │ Reservation  │                                     │
│  ├──────────────┤      ├──────────────┤                                     │
│  │ -branchId    │      │ -reserveId   │                                     │
│  │ -name        │      │ -isbn        │                                     │
│  │ -location    │      │ -patronId    │                                     │
│  │ -bookIsbns   │      │ -date        │                                     │
│  └──────────────┘      │ -status      │                                     │
│                        └──────────────┘                                     │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                         PATTERN LAYER                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────────┐         ┌──────────────────┐                          │
│  │  <<interface>>   │         │  <<interface>>   │                          │
│  │    Observer      │         │     Subject      │                          │
│  ├──────────────────┤         ├──────────────────┤                          │
│  │ +update()        │         │ +attach()        │                          │
│  └──────────────────┘         │ +detach()        │                          │
│          △                    │ +notify()        │                          │
│          │                    └──────────────────┘                          │
│          │                            △                                      │
│  ┌───────┴────────┐                  │                                      │
│  │ PatronObserver │         ┌────────┴────────┐                             │
│  └────────────────┘         │ Notification    │                             │
│                             │    Service      │                             │
│                             └─────────────────┘                             │
│                                                                               │
│  ┌──────────────────┐                                                        │
│  │  <<interface>>   │                                                        │
│  │ SearchStrategy   │                                                        │
│  ├──────────────────┤                                                        │
│  │ +search()        │                                                        │
│  └──────────────────┘                                                        │
│          △                                                                    │
│          ├─────────────┬─────────────┐                                      │
│  ┌───────┴────────┐   │             │                                       │
│  │ TitleSearch    │   │             │                                       │
│  │   Strategy     │   │             │                                       │
│  └────────────────┘   │             │                                       │
│  ┌────────────────┐   │    ┌────────┴────────┐                             │
│  │ AuthorSearch   │   │    │  ISBNSearch     │                             │
│  │   Strategy     │   │    │   Strategy      │                             │
│  └────────────────┘   │    └─────────────────┘                             │
│                       │                                                      │
│  ┌────────────────────┴──────────┐                                          │
│  │     EntityFactory              │                                          │
│  ├────────────────────────────────┤                                          │
│  │ +createBook()                  │                                          │
│  │ +createPatron()                │                                          │
│  │ +createLoan()                  │                                          │
│  │ +createBranch()                │                                          │
│  │ +createReservation()           │                                          │
│  └────────────────────────────────┘                                          │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                       REPOSITORY LAYER                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                      │
│  │BookRepository│  │PatronRepo    │  │LoanRepository│                      │
│  ├──────────────┤  ├──────────────┤  ├──────────────┤                      │
│  │+save()       │  │+save()       │  │+save()       │                      │
│  │+findByIsbn() │  │+findById()   │  │+findById()   │                      │
│  │+findAll()    │  │+findAll()    │  │+findByPatron()│                     │
│  │+delete()     │  │+delete()     │  │+findActive() │                      │
│  └──────────────┘  └──────────────┘  └──────────────┘                      │
│                                                                               │
│  ┌──────────────┐  ┌──────────────┐                                         │
│  │BranchRepo    │  │ReservationRepo│                                        │
│  ├──────────────┤  ├──────────────┤                                         │
│  │+save()       │  │+save()       │                                         │
│  │+findById()   │  │+findById()   │                                         │
│  │+findAll()    │  │+findActive() │                                         │
│  └──────────────┘  └──────────────┘                                         │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                      │
│  │ BookService  │  │PatronService │  │ LoanService  │                      │
│  ├──────────────┤  ├──────────────┤  ├──────────────┤                      │
│  │+addBook()    │  │+addPatron()  │  │+checkout()   │                      │
│  │+updateBook() │  │+updatePatron()│  │+returnBook() │                      │
│  │+removeBook() │  │+getPatron()  │  │+getLoans()   │                      │
│  │+getBook()    │  │+getHistory() │  └──────────────┘                      │
│  │+searchBooks()│  └──────────────┘                                         │
│  └──────────────┘                                                            │
│                                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                      │
│  │BranchService │  │Reservation   │  │Recommendation│                      │
│  ├──────────────┤  │  Service     │  │   Service    │                      │
│  │+createBranch()│  ├──────────────┤  ├──────────────┤                      │
│  │+getBranch()  │  │+reserve()    │  │+getRecommend()│                     │
│  │+assignBook() │  │+notify()     │  └──────────────┘                      │
│  │+transfer()   │  └──────────────┘                                         │
│  └──────────────┘                                                            │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                       CONTROLLER LAYER (REST API)                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                      │
│  │Book          │  │Patron        │  │Loan          │                      │
│  │Controller    │  │Controller    │  │Controller    │                      │
│  └──────────────┘  └──────────────┘  └──────────────┘                      │
│                                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                      │
│  │Branch        │  │Reservation   │  │Recommendation│                      │
│  │Controller    │  │Controller    │  │Controller    │                      │
│  └──────────────┘  └──────────────┘  └──────────────┘                      │
└─────────────────────────────────────────────────────────────────────────────┘
```

## API Endpoints

### Book Management
- `POST /api/books` - Add a new book
- `PUT /api/books/{isbn}` - Update book details
- `DELETE /api/books/{isbn}` - Remove a book
- `GET /api/books/{isbn}` - Get book by ISBN
- `GET /api/books` - Get all books
- `GET /api/books/search?type={title|author|isbn}&query={query}` - Search books

### Patron Management
- `POST /api/patrons` - Register a new patron
- `PUT /api/patrons/{patronId}` - Update patron information
- `GET /api/patrons/{patronId}` - Get patron details
- `GET /api/patrons` - Get all patrons
- `GET /api/patrons/{patronId}/history` - Get borrowing history

### Loan Management
- `POST /api/loans/checkout` - Checkout a book
- `POST /api/loans/return/{isbn}` - Return a book
- `GET /api/loans/patron/{patronId}` - Get patron's loans

### Branch Management
- `POST /api/branches` - Create a new branch
- `GET /api/branches/{branchId}` - Get branch details
- `GET /api/branches` - Get all branches
- `POST /api/branches/assign` - Assign book to branch
- `POST /api/branches/transfer` - Transfer book between branches

### Reservation Management
- `POST /api/reservations` - Reserve a book
- `GET /api/reservations/book/{isbn}` - Get reservations for a book

### Recommendations
- `GET /api/recommendations/{patronId}` - Get book recommendations

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test with Indianized data
mvn test -Dtest=SahityaSadanLibraryTest

# Run controller verification tests
mvn test -Dtest=AllControllersVerificationTest
```

### Test Summary

**Total: 25 tests - All Passed **

#### Test Classes:

1. **SahityaSadanLibraryTest** (15 tests)
   - Indian authors: Arundhati Roy, Salman Rushdie, Vikram Seth, Aravind Adiga, Khushwant Singh, Kiran Desai
   - Indian patrons: Rajesh Kumar, Priya Sharma, Anil Verma, Deepak Mehta
   - Indian branches: Sahitya Sadan Delhi, Mumbai, Bangalore
   - Tests: Book management, patron registration, checkout/return, borrowing history, multi-branch operations, reservations, recommendations, complete workflows

2. **AllControllersVerificationTest** (9 tests)
   - Verifies all 6 REST controllers
   - BookController: Add, update, delete, get, search
   - PatronController: Register, update, get, history
   - LoanController: Checkout, return, get loans
   - BranchController: Create, get, assign, transfer
   - ReservationController: Reserve, get reservations
   - RecommendationController: Get recommendations

3. **LibraryManagementSystemApplicationTests** (1 test)
   - Spring Boot context loads successfully

```
Tests run: 25, Failures: 0, Errors: 0, Skipped: 0 
```

## Logging

The application uses SLF4J with Logback for logging. Important events are logged:
- Book additions, updates, and removals
- Patron registrations and updates
- Checkout and return operations
- Reservations and notifications
- Branch transfers
- Errors and exceptions

Logs can be found in the console output.

## Design Decisions

1. **In-Memory Storage**: Used ConcurrentHashMap for thread-safe operations without database complexity
2. **Factory Pattern**: Centralized entity creation with automatic ID generation
3. **Observer Pattern**: Decoupled notification system for reservation alerts
4. **Strategy Pattern**: Flexible search implementation allowing easy extension
5. **Repository Pattern**: Clean separation between business logic and data access
6. **REST API**: Standard HTTP methods for CRUD operations
7. **Exception Handling**: Global exception handler for consistent error responses

## Future Enhancements

- Add authentication and authorization
- Implement fine calculation for overdue books
- Add email/SMS notifications
- Implement pagination for large result sets
- Add database persistence (JPA/Hibernate)
- Add unit and integration tests
- Implement caching for frequently accessed data
- Add API documentation with Swagger/OpenAPI

## Author

Sudharma
