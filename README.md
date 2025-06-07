# 📚 Library Management System API

A simple Spring Boot-based RESTful API to manage library books, borrowers, and borrowing/returning operations.

## ✅ Features

- Add and list books 📖
- Register and list borrowers 🧑‍🎓
- Borrow a book (with availability check) 📕➡️👤
- Return a book (updates availability) 👤➡️📗
- In-memory data handling with mock persistence
- Jakarta validation with custom exceptions
- Modular and testable architecture

---

## 🔧 Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- Jakarta Validation
- Lombok
- JUnit + Mockito
- Maven

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- IDE (like IntelliJ or VS Code)

### Setup

```bash
git clone https://github.com/ArulSemmalai/library-system.git
cd library-system
mvn clean install
```

### Run the App

```bash
mvn spring-boot:run
```

App runs at: `http://localhost:8080`

---

## 📌 API Endpoints

| Method | Endpoint                        | Description                         |
|--------|----------------------------------|-------------------------------------|
| POST   | `/library/api/v1/books/add`     | Add new book                        |
| GET    | `/library/api/v1/books`         | List all books                      |
| POST   | `/library/api/v1/borrowers/add` | Register new borrower               |
| GET    | `/library/api/v1/borrowers`     | List all borrowers                  |
| GET    | `/library/api/v1/borrow`        | Borrow a book                       |
| GET    | `/library/api/v1/return`        | Return a borrowed book              |

---

## 🧪 Sample Borrow Request

```
GET /library/api/v1/borrow?bookId=1234&borrowerId=9999
```

## 🧪 Sample Return Request

```
GET /library/api/v1/return?bookId=1234&borrowerId=9999
```

---

## ✅ Testing

```bash
mvn test
```

Includes unit and integration tests for:
- Book creation
- Borrowing edge cases
- Exception handling
- Return logic

---

## 🧠 Design Decisions

- Ensured idempotency by preventing duplicate borrowing.
- Validation and exception classes improve reliability.
- Service layer is transactionally safe for borrowing/returning.

---

## 🤝 Contributions

Feel free to fork the repo, raise issues or suggest features.

---

## 📬 Contact

**Author**: ArulKumar Semmalai  
📧 Email: [arulsemmalai@gmail.com](mailto:arulsemmalai@gmail.com)  
🔗 GitHub: [ArulSemmalai](https://github.com/ArulSemmalai)

---

Let me know if you'd like a `Postman Collection`, `Dockerfile`, or Swagger/OpenAPI YAML too.
