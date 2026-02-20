package com.airtribe.library;

import com.airtribe.library.domain.*;
import com.airtribe.library.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SahityaSadanLibraryTest {

    @Autowired private BookService bookService;
    @Autowired private PatronService patronService;
    @Autowired private LoanService loanService;
    @Autowired private BranchService branchService;
    @Autowired private ReservationService reservationService;
    @Autowired private RecommendationService recommendationService;

    private static String rajeshPatronId;
    private static String priyaPatronId;
    private static String anilPatronId;
    private static String delhiBranchId;
    private static String mumbaiBranchId;

    @Test
    @Order(1)
    void testAddIndianBooks() {
        bookService.addBook("978-8129135728", "The God of Small Things", "Arundhati Roy", 1997);
        bookService.addBook("978-0143065883", "Midnight's Children", "Salman Rushdie", 1981);
        bookService.addBook("978-0143419327", "The White Tiger", "Aravind Adiga", 2008);
        bookService.addBook("978-0143424062", "A Suitable Boy", "Vikram Seth", 1993);
        bookService.addBook("978-0143031031", "Train to Pakistan", "Khushwant Singh", 1956);
        bookService.addBook("978-0143419334", "The Inheritance of Loss", "Kiran Desai", 2006);

        List<Book> books = bookService.getAllBooks();
        assertEquals(6, books.size());
    }

    @Test
    @Order(2)
    void testSearchBooksByIndianAuthors() {
        List<Book> royBooks = bookService.searchBooks("author", "Arundhati");
        assertEquals(1, royBooks.size());
        assertEquals("The God of Small Things", royBooks.get(0).getTitle());

        List<Book> titleSearch = bookService.searchBooks("title", "Tiger");
        assertEquals(1, titleSearch.size());

        List<Book> isbnSearch = bookService.searchBooks("isbn", "978-8129135728");
        assertEquals(1, isbnSearch.size());
    }

    @Test
    @Order(3)
    void testRegisterIndianPatrons() {
        Patron rajesh = patronService.addPatron("Rajesh Kumar", "rajesh.kumar@gmail.com");
        Patron priya = patronService.addPatron("Priya Sharma", "priya.sharma@yahoo.in");
        Patron anil = patronService.addPatron("Anil Verma", "anil.verma@outlook.com");

        rajeshPatronId = rajesh.getPatronId();
        priyaPatronId = priya.getPatronId();
        anilPatronId = anil.getPatronId();

        assertNotNull(rajeshPatronId);
        assertEquals("Rajesh Kumar", rajesh.getName());
        assertEquals(3, patronService.getAllPatrons().size());
    }

    @Test
    @Order(4)
    void testUpdatePatronDetails() {
        Patron updated = patronService.updatePatron(rajeshPatronId, "Rajesh Kumar Singh", "rajesh.singh@gmail.com");
        assertEquals("Rajesh Kumar Singh", updated.getName());
        assertEquals("rajesh.singh@gmail.com", updated.getEmail());
    }

    @Test
    @Order(5)
    void testCheckoutAndReturnBooks() {
        Loan loan1 = loanService.checkoutBook("978-8129135728", rajeshPatronId);
        assertNotNull(loan1.getLoanId());
        assertEquals(rajeshPatronId, loan1.getPatronId());

        Book book = bookService.getBook("978-8129135728");
        assertEquals(BookStatus.BORROWED, book.getStatus());

        loanService.returnBook("978-8129135728");
        book = bookService.getBook("978-8129135728");
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }

    @Test
    @Order(6)
    void testBorrowingHistory() {
        loanService.checkoutBook("978-0143065883", priyaPatronId);
        loanService.returnBook("978-0143065883");
        
        loanService.checkoutBook("978-0143419327", priyaPatronId);
        loanService.returnBook("978-0143419327");

        List<String> history = patronService.getBorrowingHistory(priyaPatronId);
        assertEquals(2, history.size());
        assertTrue(history.contains("978-0143065883"));
        assertTrue(history.contains("978-0143419327"));
    }

    @Test
    @Order(7)
    void testCreateIndianBranches() {
        Branch delhi = branchService.createBranch("Sahitya Sadan Delhi", "Connaught Place, New Delhi");
        Branch mumbai = branchService.createBranch("Sahitya Sadan Mumbai", "Nariman Point, Mumbai");
        branchService.createBranch("Sahitya Sadan Bangalore", "MG Road, Bangalore");

        delhiBranchId = delhi.getBranchId();
        mumbaiBranchId = mumbai.getBranchId();

        assertEquals(3, branchService.getAllBranches().size());
        assertEquals("Sahitya Sadan Delhi", delhi.getName());
    }

    @Test
    @Order(8)
    void testAssignBooksToDelhi() {
        branchService.assignBookToBranch("978-8129135728", delhiBranchId);
        branchService.assignBookToBranch("978-0143065883", delhiBranchId);
        branchService.assignBookToBranch("978-0143419327", delhiBranchId);

        Branch delhi = branchService.getBranch(delhiBranchId);
        assertEquals(3, delhi.getBookIsbns().size());
    }

    @Test
    @Order(9)
    void testTransferBookBetweenBranches() {
        branchService.transferBook("978-8129135728", delhiBranchId, mumbaiBranchId);

        Branch delhi = branchService.getBranch(delhiBranchId);
        Branch mumbai = branchService.getBranch(mumbaiBranchId);

        assertFalse(delhi.getBookIsbns().contains("978-8129135728"));
        assertTrue(mumbai.getBookIsbns().contains("978-8129135728"));
    }

    @Test
    @Order(10)
    void testReservationSystem() {
        loanService.checkoutBook("978-0143424062", rajeshPatronId);

        Reservation reservation = reservationService.reserveBook("978-0143424062", priyaPatronId);
        assertNotNull(reservation.getReservationId());
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());

        List<Reservation> reservations = reservationService.getReservationsByBook("978-0143424062");
        assertEquals(1, reservations.size());

        loanService.returnBook("978-0143424062");
    }

    @Test
    @Order(11)
    void testMultipleReservations() {
        loanService.checkoutBook("978-0143031031", rajeshPatronId);

        reservationService.reserveBook("978-0143031031", priyaPatronId);
        reservationService.reserveBook("978-0143031031", anilPatronId);

        List<Reservation> reservations = reservationService.getReservationsByBook("978-0143031031");
        assertEquals(2, reservations.size());

        loanService.returnBook("978-0143031031");
    }

    @Test
    @Order(12)
    void testRecommendationSystem() {
        loanService.checkoutBook("978-0143419334", anilPatronId);
        loanService.returnBook("978-0143419334");

        loanService.checkoutBook("978-0143419327", anilPatronId);
        loanService.returnBook("978-0143419327");

        List<Book> recommendations = recommendationService.getRecommendations(anilPatronId);
        assertNotNull(recommendations);
    }

    @Test
    @Order(13)
    void testGetPatronLoans() {
        loanService.checkoutBook("978-8129135728", rajeshPatronId);
        
        List<Loan> loans = loanService.getPatronLoans(rajeshPatronId);
        assertTrue(loans.size() > 0);
        
        loanService.returnBook("978-8129135728");
    }

    @Test
    @Order(14)
    void testUpdateBookDetails() {
        Book updated = bookService.updateBook("978-8129135728", "The God of Small Things (Anniversary Edition)", "Arundhati Roy", 2017);
        assertEquals("The God of Small Things (Anniversary Edition)", updated.getTitle());
        assertEquals(2017, updated.getPublicationYear());
    }

    @Test
    @Order(15)
    void testCompleteWorkflow() {
        bookService.addBook("978-0143440345", "The Palace of Illusions", "Chitra Banerjee Divakaruni", 2008);
        
        Patron deepak = patronService.addPatron("Deepak Mehta", "deepak.mehta@gmail.com");
        String deepakId = deepak.getPatronId();

        branchService.assignBookToBranch("978-0143440345", delhiBranchId);

        Loan loan = loanService.checkoutBook("978-0143440345", deepakId);
        assertNotNull(loan);

        List<String> history = patronService.getBorrowingHistory(deepakId);
        assertEquals(1, history.size());

        loanService.returnBook("978-0143440345");

        Book book = bookService.getBook("978-0143440345");
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }
}
