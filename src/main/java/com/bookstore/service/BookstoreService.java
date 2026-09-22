package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Owner;
import com.bookstore.model.PurchaseRecord;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.repository.FileBookRepository;
import com.bookstore.repository.FileCustomerRepository;
import com.bookstore.repository.FilePurchaseRepository;
import com.bookstore.repository.PurchaseRepository;
import com.bookstore.repository.SqliteBookRepository;
import com.bookstore.repository.SqliteCustomerRepository;
import com.bookstore.repository.SqlitePurchaseRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Application facade the UI talks to. Replaces the original {@code BookStore}
 * god-class: persistence is delegated to a {@link BookRepository} /
 * {@link CustomerRepository} / {@link PurchaseRepository} chosen at
 * construction time, so switching between the flat-file format the project
 * originally used and a SQLite database is a one-line change.
 */
public class BookstoreService {

    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;

    private final List<Book> books = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final Owner owner = new Owner();

    public BookstoreService(BookRepository bookRepository,
                             CustomerRepository customerRepository,
                             PurchaseRepository purchaseRepository) {
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
    }

    /** Builds a service backed by the chosen persistence type, using sensible default file/db names. */
    public static BookstoreService using(PersistenceType type) {
        return switch (type) {
            case FILE -> new BookstoreService(
                    new FileBookRepository(), new FileCustomerRepository(), new FilePurchaseRepository());
            case SQLITE -> new BookstoreService(
                    new SqliteBookRepository(), new SqliteCustomerRepository(), new SqlitePurchaseRepository());
        };
    }

    public void loadData() {
        books.clear();
        books.addAll(bookRepository.findAll());
        customers.clear();
        customers.addAll(customerRepository.findAll());
    }

    public void saveData() {
        bookRepository.saveAll(books);
        customerRepository.saveAll(customers);
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<PurchaseRecord> getPurchaseHistory() {
        return purchaseRepository.findAll();
    }

    public SalesReport getSalesReport() {
        return SalesReport.from(getPurchaseHistory());
    }

    /** Adds a new title. Returns false if a book with that name (case-insensitive) already exists. */
    public boolean addBook(Book newBook) {
        for (Book b : books) {
            if (b.getName().equalsIgnoreCase(newBook.getName())) {
                return false;
            }
        }
        books.add(newBook);
        return true;
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    /** Adds stock to an existing title rather than creating a duplicate entry. */
    public void restock(Book book, int amount) {
        book.addStock(amount);
    }

    public boolean addCustomer(Customer customer) {
        for (Customer c : customers) {
            if (c.getUsername().equalsIgnoreCase(customer.getUsername())) {
                return false;
            }
        }
        customers.add(customer);
        return true;
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    public User login(String username, String password) {
        if (owner.getUsername().equals(username) && owner.getPassword().equals(password)) {
            return owner;
        }
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Completes a purchase of the given books for a customer: decrements
     * stock (removing a title once it hits zero), applies the loyalty math,
     * and logs a {@link PurchaseRecord} per book for reporting.
     */
    public double checkout(Customer customer, List<Book> selectedBooks, boolean redeemPoints) {
        double totalCost = selectedBooks.stream().mapToDouble(Book::getPrice).sum();
        double finalCost = redeemPoints ? customer.redeem(totalCost) : totalCost;
        if (!redeemPoints) {
            customer.buy(totalCost);
        }

        // Split the (possibly discounted) final cost proportionally across each book for the log.
        double discountRatio = totalCost == 0 ? 0 : finalCost / totalCost;
        LocalDateTime now = LocalDateTime.now();
        for (Book book : selectedBooks) {
            purchaseRepository.save(new PurchaseRecord(
                    customer.getUsername(), book.getName(), book.getPrice() * discountRatio, now));
            book.decrementStock();
            if (!book.isInStock()) {
                books.remove(book);
            }
        }

        return finalCost;
    }

    /** Book titles whose name contains the given text (case-insensitive), for the search bar. */
    public List<Book> searchBooks(String query) {
        String needle = query.toLowerCase();
        return books.stream().filter(b -> b.getName().toLowerCase().contains(needle)).toList();
    }
}
