package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Owner;
import com.bookstore.model.PurchaseRecord;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookstoreServiceTest {

    /** In-memory fakes so these tests don't touch the filesystem or a real database. */
    static class InMemoryBookRepo implements BookRepository {
        List<Book> saved = new ArrayList<>();
        public List<Book> findAll() { return saved; }
        public void saveAll(List<Book> books) { saved = new ArrayList<>(books); }
    }

    static class InMemoryCustomerRepo implements CustomerRepository {
        List<Customer> saved = new ArrayList<>();
        public List<Customer> findAll() { return saved; }
        public void saveAll(List<Customer> customers) { saved = new ArrayList<>(customers); }
    }

    static class InMemoryPurchaseRepo implements PurchaseRepository {
        List<PurchaseRecord> saved = new ArrayList<>();
        public List<PurchaseRecord> findAll() { return saved; }
        public void save(PurchaseRecord record) { saved.add(record); }
    }

    private BookstoreService service;

    @BeforeEach
    void setUp() {
        service = new BookstoreService(new InMemoryBookRepo(), new InMemoryCustomerRepo(), new InMemoryPurchaseRepo());
    }

    @Test
    void addBookRejectsDuplicateNameCaseInsensitive() {
        assertTrue(service.addBook(new Book("Dune", 20)));
        assertFalse(service.addBook(new Book("dune", 25)));
    }

    @Test
    void checkoutDecrementsStockAndKeepsBookWhileStockRemains() {
        Book book = new Book("Dune", 20, 3);
        service.addBook(book);
        Customer customer = new Customer("alice", "pw");

        service.checkout(customer, List.of(book), false);

        assertEquals(2, book.getQuantity());
        assertTrue(service.getBooks().contains(book));
    }

    @Test
    void checkoutRemovesBookWhenStockHitsZero() {
        Book book = new Book("Dune", 20, 1);
        service.addBook(book);
        Customer customer = new Customer("alice", "pw");

        service.checkout(customer, List.of(book), false);

        assertFalse(service.getBooks().contains(book));
    }

    @Test
    void checkoutLogsAPurchaseRecordPerBook() {
        Book a = new Book("Dune", 20, 5);
        Book b = new Book("1984", 10, 5);
        service.addBook(a);
        service.addBook(b);
        Customer customer = new Customer("alice", "pw");

        service.checkout(customer, List.of(a, b), false);

        assertEquals(2, service.getPurchaseHistory().size());
    }

    @Test
    void checkoutAwardsLoyaltyPointsOnPlainPurchase() {
        Book book = new Book("Dune", 20, 5);
        service.addBook(book);
        Customer customer = new Customer("alice", "pw");

        service.checkout(customer, List.of(book), false);

        assertEquals(200, customer.getPoints());
    }

    @Test
    void loginReturnsOwnerForAdminCredentials() {
        User u = service.login("admin", "admin");
        assertInstanceOf(Owner.class, u);
    }

    @Test
    void loginReturnsNullForBadCredentials() {
        assertNull(service.login("nobody", "wrong"));
    }

    @Test
    void searchBooksIsCaseInsensitiveSubstringMatch() {
        service.addBook(new Book("The Hobbit", 15));
        service.addBook(new Book("Dune", 20));

        List<Book> results = service.searchBooks("hob");

        assertEquals(1, results.size());
        assertEquals("The Hobbit", results.get(0).getName());
    }
}
