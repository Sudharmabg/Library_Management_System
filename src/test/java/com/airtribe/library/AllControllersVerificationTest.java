package com.airtribe.library;

import com.airtribe.library.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AllControllersVerificationTest {

    @Autowired private BookService bookService;
    @Autowired private PatronService patronService;
    @Autowired private LoanService loanService;
    @Autowired private BranchService branchService;
    @Autowired private ReservationService reservationService;
    @Autowired private RecommendationService recommendationService;

    private static String patronId;
    private static String branchId;

    @Test
    @Order(1)
    void verifyBookController() {
        bookService.addBook("978-8129135728", "The God of Small Things", "Arundhati Roy", 1997);
        assertNotNull(bookService.getBook("978-8129135728"));
        assertEquals(1, bookService.getAllBooks().size());
        assertEquals(1, bookService.searchBooks("author", "Arundhati").size());
        bookService.updateBook("978-8129135728", "Updated Title", "Arundhati Roy", 1997);
        assertEquals("Updated Title", bookService.getBook("978-8129135728").getTitle());
    }

    @Test
    @Order(2)
    void verifyPatronController() {
        var patron = patronService.addPatron("Rajesh Kumar", "rajesh@gmail.com");
        patronId = patron.getPatronId();
        assertNotNull(patronService.getPatron(patronId));
        assertEquals(1, patronService.getAllPatrons().size());
        patronService.updatePatron(patronId, "Rajesh Singh", "rajesh.singh@gmail.com");
        assertEquals("Rajesh Singh", patronService.getPatron(patronId).getName());
        assertNotNull(patronService.getBorrowingHistory(patronId));
    }

    @Test
    @Order(3)
    void verifyLoanController() {
        var loan = loanService.checkoutBook("978-8129135728", patronId);
        assertNotNull(loan);
        assertEquals(1, loanService.getPatronLoans(patronId).size());
        loanService.returnBook("978-8129135728");
    }

    @Test
    @Order(4)
    void verifyBranchController() {
        var branch = branchService.createBranch("Delhi Branch", "Connaught Place");
        branchId = branch.getBranchId();
        assertNotNull(branchService.getBranch(branchId));
        assertEquals(1, branchService.getAllBranches().size());
        branchService.assignBookToBranch("978-8129135728", branchId);
    }

    @Test
    @Order(5)
    void verifyReservationController() {
        loanService.checkoutBook("978-8129135728", patronId);
        var patron2 = patronService.addPatron("Priya Sharma", "priya@gmail.com");
        var reservation = reservationService.reserveBook("978-8129135728", patron2.getPatronId());
        assertNotNull(reservation);
        assertEquals(1, reservationService.getReservationsByBook("978-8129135728").size());
        loanService.returnBook("978-8129135728");
    }

    @Test
    @Order(6)
    void verifyRecommendationController() {
        assertNotNull(recommendationService.getRecommendations(patronId));
    }

    @Test
    @Order(7)
    void verifyBranchTransfer() {
        var branch2 = branchService.createBranch("Mumbai Branch", "Nariman Point");
        branchService.transferBook("978-8129135728", branchId, branch2.getBranchId());
        assertTrue(branchService.getBranch(branch2.getBranchId()).getBookIsbns().contains("978-8129135728"));
    }

    @Test
    @Order(8)
    void verifyDeleteOperation() {
        bookService.addBook("978-0000000000", "Test Book", "Test Author", 2020);
        bookService.removeBook("978-0000000000");
        assertThrows(Exception.class, () -> bookService.getBook("978-0000000000"));
    }

    @Test
    @Order(9)
    void verifyErrorHandling() {
        assertThrows(Exception.class, () -> bookService.getBook("invalid-isbn"));
        assertThrows(Exception.class, () -> patronService.getPatron("invalid-id"));
    }
}
