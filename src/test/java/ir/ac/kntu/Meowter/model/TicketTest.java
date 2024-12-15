package ir.ac.kntu.Meowter.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class TicketTest {

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket("Issue with login", TicketSubject.REPORT_ISSUE, "john_doe");
    }

    @Test
    void testTicketCreation() {
        assertNotNull(ticket);
        assertEquals("Issue with login", ticket.getDescription());
        assertEquals(TicketSubject.REPORT_ISSUE, ticket.getSubject());
        assertEquals("john_doe", ticket.getUsername());
        assertEquals(TicketStatus.SUBMITTED, ticket.getStatus());
        assertNotNull(ticket.getCreatedAt());
        assertNotNull(ticket.getUpdatedAt());
        assertFalse(ticket.getIsWarned());
    }

    @Test
    void testSetAndGetDescription() {
        ticket.setDescription("Updated description");
        assertEquals("Updated description", ticket.getDescription());
    }

    @Test
    void testSetAndGetSubject() {
        ticket.setSubject(TicketSubject.REPORT_ISSUE);
        assertEquals(TicketSubject.REPORT_ISSUE, ticket.getSubject());
    }

    @Test
    void testSetAndGetUsername() {
        ticket.setUsername("jane_doe");
        assertEquals("jane_doe", ticket.getUsername());
    }

    @Test
    void testDefaultResponse() {
        assertEquals("There is no response yet.", ticket.getResponse());
    }

    @Test
    void testSetAndGetResponse() {
        ticket.setResponse("We are working on it.");
        assertEquals("We are working on it.", ticket.getResponse());
    }

    @Test
    void testSetAndGetStatus() {
        ticket.setStatus(TicketStatus.CLOSED);
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
    }

    @Test
    void testSetAndGetIsWarned() {
        ticket.setIsWarned(true);
        assertTrue(ticket.getIsWarned());
    }

    @Test
    void testSetAndGetReportUsername() {
        ticket.setReportUsername("reporter123");
        assertEquals("reporter123", ticket.getReportUsername());
    }

    @Test
    void testSetAndGetReportWarning() {
        ticket.setReportWarning("User violated terms");
        assertEquals("User violated terms", ticket.getReportWarning());
    }

    @Test
    void testCreatedAtInitialization() {
        assertNotNull(ticket.getCreatedAt());
    }

    @Test
    void testUpdatedAtInitialization() {
        assertNotNull(ticket.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        ticket.setUpdatedAt(newTime);
        assertEquals(newTime, ticket.getUpdatedAt());
    }


    @Test
    void testEqualityDifferentId() {
        Ticket otherTicket = new Ticket("Another issue", TicketSubject.OTHER, "jane_doe");
        otherTicket.setId(999L);
        assertNotEquals(ticket, otherTicket);
    }

    @Test
    void testHashCodeDifferentId() {
        Ticket otherTicket = new Ticket("Another issue", TicketSubject.OTHER, "jane_doe");
        otherTicket.setId(999L);
        assertNotEquals(ticket.hashCode(), otherTicket.hashCode());
    }
}

