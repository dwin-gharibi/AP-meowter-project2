package ir.ac.kntu.Meowter.controller;

import ir.ac.kntu.Meowter.model.Role;
import ir.ac.kntu.Meowter.repository.TicketRepository;
import ir.ac.kntu.Meowter.service.TicketService;
import ir.ac.kntu.Meowter.model.Ticket;
import ir.ac.kntu.Meowter.model.TicketSubject;
import ir.ac.kntu.Meowter.model.User;
import ir.ac.kntu.Meowter.repository.UserRepository;


import java.util.Scanner;

public class TicketController {

    private TicketService ticketService;
    private final TicketRepository ticketRepository;

    public TicketController() {
        this.ticketService = new TicketService();
        this.ticketRepository = new TicketRepository();
    }

    public void displayTicketSection(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Ticket Section");
            System.out.println("1. Create Ticket");
            System.out.println("2. View My Tickets");
            if (loggedInUser.getRole() == Role.SUPPORT){
                System.out.println("3. Respond to Ticket");
                System.out.println("4. Close Ticket");
            }
            System.out.println("5. Back to User Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter ticket description: ");
                    String description = scanner.nextLine();
                    System.out.println("Choose ticket subject:");
                    System.out.println("1. Report Issue");
                    System.out.println("2. User Settings");
                    System.out.println("3. Profile Issue");
                    System.out.println("4. Other");
                    System.out.print("Choose an option: ");
                    int subjectChoice = scanner.nextInt();
                    scanner.nextLine();

                    TicketSubject subject = TicketSubject.values()[subjectChoice - 1];
                    Ticket ticket = ticketService.createTicket(description, subject, loggedInUser.getUsername());
                    System.out.println("Ticket created successfully. Ticket ID: " + ticket.getId());
                    break;
                case 2:
                    System.out.println("Your Tickets:");
                    ticketService.getUserTickets(loggedInUser.getUsername()).forEach(t -> {
                        System.out.println("Ticket ID: " + t.getId() + " | Status: " + t.getStatus());
                        if (!t.getResponse().isEmpty()) {
                            System.out.println("Response: " + t.getResponse());
                        }
                    });
                    break;
                case 3:
                    System.out.print("Enter Ticket ID to respond: ");
                    long ticketId = scanner.nextLong();
                    scanner.nextLine();
                    Ticket ticketToRespond = ticketRepository.findById(ticketId);
                    if (ticketToRespond != null && ticketToRespond.getUsername().equals(loggedInUser.getUsername())) {
                        System.out.print("Enter response: ");
                        String response = scanner.nextLine();
                        ticketService.respondToTicket(ticketToRespond, response);
                        System.out.println("Response added successfully.");
                    } else {
                        System.out.println("Ticket not found or invalid.");
                    }
                    break;
                case 4:
                    System.out.print("Enter Ticket ID to close: ");
                    long ticketIdToClose = scanner.nextLong();
                    Ticket ticketToClose = ticketRepository.findById(ticketIdToClose);
                    if (ticketToClose != null) {
                        ticketService.closeTicket(ticketToClose);
                        System.out.println("Ticket closed successfully.");
                    } else {
                        System.out.println("Ticket not found.");
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }
}
