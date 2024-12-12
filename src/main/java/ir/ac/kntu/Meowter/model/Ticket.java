package ir.ac.kntu.Meowter.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private boolean isWarned;

    @Enumerated(EnumType.STRING)
    private TicketSubject subject;

    private String username;

    private String response;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Ticket() {}

    public Ticket(String description, TicketSubject subject, String username) {
        this.description = description;
        this.subject = subject;
        this.username = username;
        this.status = TicketStatus.SUBMITTED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isWarned = false;
    }

    public boolean getIsWarned(){
        return this.isWarned;
    }

    public void setIsWarned(boolean isWarned){
        this.isWarned = isWarned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketSubject getSubject() {
        return subject;
    }

    public void setSubject(TicketSubject subject) {
        this.subject = subject;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResponse() {
        return response == null ? "There is no response yet." : response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
