# 📚 Bookstore Management System

A Java Swing desktop application for running a small bookstore: an **owner**
manages inventory and customer accounts, and **customers** log in to browse
stock, buy books, and earn loyalty points. Originally built as a team course
project (COE528); this version has been refactored into a layered
architecture, extended with new features, and covered by a JUnit 5 test
suite as a standalone portfolio project.

![CI](https://github.com/amaraulakh956/bookstore-management-system/actions/workflows/ci.yml/badge.svg)

## Features

- **Role-based login** — a single owner/admin account plus any number of customer accounts.
- **Inventory management** — add, restock, search, and delete titles; stock is tracked per book (a purchase decrements quantity instead of deleting the title).
- **Loyalty points (State pattern)** — customers earn points per dollar spent and move between **Silver** and **Gold** tiers; Gold customers who redeem below the threshold drop back to Silver.
- **Point redemption at checkout** — customers can pay full price or redeem points against the cost of their cart.
- **Live search** — filter the book catalog by name, both as the owner and as a shopping customer.
- **Sales report** — total revenue, units sold, best-selling title, and top customer by spend, computed from a logged purchase history.
- **Pluggable persistence** — run against the original flat-file format (`books.txt` / `customers.txt` / `purchases.txt`) or a **SQLite** database (`bookstore.db`), chosen with a command-line flag.

## Tech stack

- Java 17, Swing
- Maven
- SQLite via JDBC (`org.xerial:sqlite-jdbc`)
- JUnit 5
- GitHub Actions (CI on every push/PR)

## Architecture

The original submission was one flat package with a single `BookStore` class
handling UI wiring, business rules, and file I/O together. It's now split
into layers:

```
com.bookstore.model        Book, Customer, Owner, User, PurchaseRecord
com.bookstore.state        LoyaltyState interface + SilverState/GoldState (State pattern)
com.bookstore.repository   BookRepository / CustomerRepository / PurchaseRepository
                            interfaces, each with a File-based and a SQLite-based implementation
com.bookstore.service      BookstoreService (application facade), SalesReport, PersistenceType
com.bookstore.ui           Swing screens (Login, Owner*, Customer*)
```

`BookstoreService` depends only on the repository *interfaces*, so the UI
and business logic never know or care whether data is coming from text
files or SQLite — swapping the backend is a one-line change
(`BookstoreService.using(PersistenceType.SQLITE)`).

The original design docs — class diagram and use-case diagram from the
course submission — are kept in [`docs/`](docs) for reference; the class
diagram no longer reflects the current package layout but the State
pattern and core relationships it documents still hold.

## Getting started

**Requirements:** JDK 17+, Maven.

```bash
git clone https://github.com/amaraulakh956/bookstore-management-system.git
cd bookstore-management-system
mvn clean package
```

Run with the original flat-file storage:

```bash
java -jar target/bookstore-app.jar
```

Run with SQLite storage instead:

```bash
java -jar target/bookstore-app.jar --db=sqlite
```

Log in as the owner with `admin` / `admin`, or add a customer account from
the Owner → Customers screen and log in as them.

## Running the tests

```bash
mvn test
```

Covers the loyalty point math (earning, redemption, tier transitions),
stock-aware checkout, duplicate/validation rules, search, and both
persistence backends (using JUnit 5's `@TempDir` so tests never touch real
data files).

## Possible next steps

- Replace the plaintext password storage with hashing (e.g. BCrypt) — currently passwords are stored as-is, which was fine for a course demo but isn't how a real system should work.
- Multi-quantity checkout (buy 2 of the same title in one transaction) instead of one checkbox per copy.
- Export the sales report to CSV.


