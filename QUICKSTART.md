# Quick Start Guide

## Running the Application

```bash
mvn spring-boot:run
```

## Example Usage Scenarios

### Scenario 1: Basic Book Management

```bash
# 1. Add books to the library
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0134685991","title":"Effective Java","author":"Joshua Bloch","publicationYear":2018}'

curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0596009205","title":"Head First Design Patterns","author":"Eric Freeman","publicationYear":2004}'

# 2. Search for books
curl "http://localhost:8080/api/books/search?type=author&query=Joshua"

# 3. Get all books
curl http://localhost:8080/api/books
```

### Scenario 2: Patron Registration and Borrowing

```bash
# 1. Register patrons
curl -X POST http://localhost:8080/api/patrons \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Smith","email":"alice@example.com"}'

# Note the patronId returned (e.g., P12345678)

# 2. Checkout a book
curl -X POST http://localhost:8080/api/loans/checkout \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0134685991","patronId":"P12345678"}'

# 3. View patron's borrowing history
curl http://localhost:8080/api/patrons/P12345678/history

# 4. Return the book
curl -X POST http://localhost:8080/api/loans/return/978-0134685991
```

### Scenario 3: Multi-Branch Operations

```bash
# 1. Create branches
curl -X POST http://localhost:8080/api/branches \
  -H "Content-Type: application/json" \
  -d '{"name":"Downtown Branch","location":"123 Main St"}'

curl -X POST http://localhost:8080/api/branches \
  -H "Content-Type: application/json" \
  -d '{"name":"Uptown Branch","location":"456 Oak Ave"}'

# Note the branchIds returned (e.g., B12345678, B87654321)

# 2. Assign book to a branch
curl -X POST http://localhost:8080/api/branches/assign \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0134685991","branchId":"B12345678"}'

# 3. Transfer book between branches
curl -X POST http://localhost:8080/api/branches/transfer \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0134685991","fromBranchId":"B12345678","toBranchId":"B87654321"}'
```

### Scenario 4: Reservation System

```bash
# 1. Checkout a book (make it unavailable)
curl -X POST http://localhost:8080/api/loans/checkout \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0134685991","patronId":"P12345678"}'

# 2. Another patron reserves the book
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0134685991","patronId":"P87654321"}'

# 3. View reservations for the book
curl http://localhost:8080/api/reservations/book/978-0134685991

# 4. Return the book (triggers notification to reserved patron)
curl -X POST http://localhost:8080/api/loans/return/978-0134685991
```

### Scenario 5: Book Recommendations

```bash
# 1. Patron borrows multiple books by the same author
curl -X POST http://localhost:8080/api/loans/checkout \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0134685991","patronId":"P12345678"}'

curl -X POST http://localhost:8080/api/loans/return/978-0134685991

curl -X POST http://localhost:8080/api/loans/checkout \
  -H "Content-Type: application/json" \
  -d '{"isbn":"978-0596009205","patronId":"P12345678"}'

# 2. Get recommendations based on borrowing history
curl http://localhost:8080/api/recommendations/P12345678
```

## Testing with Postman

Import the following collection to test all endpoints:

1. Create a new collection in Postman
2. Add requests for each endpoint listed in README.md
3. Set base URL to `http://localhost:8080`
4. Use JSON body for POST/PUT requests

## Verifying Design Patterns

### Factory Pattern
Check logs when creating entities - you'll see auto-generated IDs:
```
INFO  c.a.l.service.BookService - Adding book: ISBN=978-0134685991, Title=Effective Java
```

### Observer Pattern
When a book is returned and reservations exist, check logs:
```
INFO  c.a.l.p.NotificationService - Notifying 1 observers: Book 978-0134685991 is now available
INFO  c.a.l.p.PatronObserver - Notification sent to patron P87654321
```

### Strategy Pattern
Search using different strategies:
```bash
# Title search
curl "http://localhost:8080/api/books/search?type=title&query=Java"

# Author search
curl "http://localhost:8080/api/books/search?type=author&query=Bloch"

# ISBN search
curl "http://localhost:8080/api/books/search?type=isbn&query=978-0134685991"
```

## Common Issues

### Port Already in Use
If port 8080 is busy, change it in `application.properties`:
```properties
server.port=8081
```

### Build Errors
Clean and rebuild:
```bash
mvn clean install
```

## Next Steps

1. Explore the code structure in `src/main/java/com/airtribe/library/`
2. Review the class diagram in README.md
3. Check logs to see design patterns in action
4. Extend the system with your own features
