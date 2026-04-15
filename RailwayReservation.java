import java.util.*;

class Ticket {
    private static int ticketCounter = 1000;
    private String ticketId;
    private String passengerName;
    private String from;
    private String to;
    private int seatNumber;
    private double fare;
    private String bookingDate;

    public Ticket(String passengerName, String from, String to, int seatNumber, double fare) {
        this.ticketId = "TKT" + (++ticketCounter);
        this.passengerName = passengerName;
        this.from = from;
        this.to = to;
        this.seatNumber = seatNumber;
        this.fare = fare;
        this.bookingDate = new Date().toString();
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-20s | %s -> %s | Seat: %3d | Rs. %.2f | %s",
                ticketId, passengerName, from, to, seatNumber, fare, bookingDate);
    }
}

class Train {
    private String trainId;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private boolean[] seatAvailability;
    private double farePerSeat;
    private ArrayList<Ticket> bookedTickets;

    public Train(String trainId, String trainName, String source, String destination,
                 int totalSeats, double farePerSeat) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.seatAvailability = new boolean[totalSeats];
        Arrays.fill(seatAvailability, true); // true = available
        this.farePerSeat = farePerSeat;
        this.bookedTickets = new ArrayList<>();
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        int count = 0;
        for (boolean seat : seatAvailability) {
            if (seat) count++;
        }
        return count;
    }

    public double getFarePerSeat() {
        return farePerSeat;
    }

    public boolean bookSeat(int seatNumber) {
        if (seatNumber < 1 || seatNumber > totalSeats) {
            return false;
        }
        if (!seatAvailability[seatNumber - 1]) {
            return false;
        }
        seatAvailability[seatNumber - 1] = false;
        return true;
    }

    public void cancelSeat(int seatNumber) {
        if (seatNumber >= 1 && seatNumber <= totalSeats) {
            seatAvailability[seatNumber - 1] = true;
        }
    }

    public void displaySeatAvailability() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        SEAT AVAILABILITY MAP            ║");
        System.out.println("║ 🟦 = Available    🟥 = Booked           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        int seatsPerRow = 10;
        for (int i = 0; i < totalSeats; i++) {
            if (i > 0 && i % seatsPerRow == 0) {
                System.out.println();
            }
            if (seatAvailability[i]) {
                System.out.print("🟦" + (i + 1) + " ");
            } else {
                System.out.print("🟥" + (i + 1) + " ");
            }
        }
        System.out.println("\n");
    }

    public void addBookedTicket(Ticket ticket) {
        bookedTickets.add(ticket);
    }

    public Ticket findTicket(String ticketId) {
        for (Ticket ticket : bookedTickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }
        return null;
    }

    public void removeTicket(String ticketId) {
        for (Ticket ticket : bookedTickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                cancelSeat(ticket.getSeatNumber());
                bookedTickets.remove(ticket);
                break;
            }
        }
    }

    public ArrayList<Ticket> getBookedTickets() {
        return bookedTickets;
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-25s | %s -> %s | Available: %2d/%2d | Rs. %.2f",
                trainId, trainName, source, destination, getAvailableSeats(), totalSeats, farePerSeat);
    }
}

public class RailwayReservation {
    private static ArrayList<Train> trains;
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Ticket> userTickets = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  RAILWAY RESERVATION SYSTEM             ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        initializeTrains();

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Select an option (1-5): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAvailableTrains();
                    break;
                case "2":
                    bookTicket();
                    break;
                case "3":
                    viewMyTickets();
                    break;
                case "4":
                    cancelTicket();
                    break;
                case "5":
                    System.out.println("\n✓ Thank you for using Railway Reservation System. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("❌ Invalid option! Please select 1-5.\n");
            }
        }

        scanner.close();
    }

    private static void initializeTrains() {
        trains = new ArrayList<>();
        trains.add(new Train("T001", "Rajdhani Express", "Delhi", "Mumbai", 50, 3000));
        trains.add(new Train("T002", "Shatabdi Express", "Delhi", "Jaipur", 60, 1500));
        trains.add(new Train("T003", "AP Express", "Delhi", "Bangalore", 40, 4000));
        trains.add(new Train("T004", "Yuva Express", "Mumbai", "Pune", 55, 800));
        trains.add(new Train("T005", "Kerala Express", "Delhi", "Kochin", 45, 5000));
    }

    private static void displayMainMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                      ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. View Available Trains                ║");
        System.out.println("║ 2. Book Ticket                          ║");
        System.out.println("║ 3. View My Tickets                      ║");
        System.out.println("║ 4. Cancel Ticket                        ║");
        System.out.println("║ 5. Exit                                 ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void viewAvailableTrains() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         AVAILABLE TRAINS                                   ║");
        System.out.println("║ Train ID | Train Name        | From -> To           | Seats | Fare       ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");

        for (Train train : trains) {
            System.out.println("║ " + train + " ║");
        }

        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static void bookTicket() {
        viewAvailableTrains();

        System.out.print("Enter Train ID to book: ");
        String trainId = scanner.nextLine().toUpperCase();

        Train selectedTrain = findTrain(trainId);
        if (selectedTrain == null) {
            System.out.println("❌ Train not found!\n");
            return;
        }

        if (selectedTrain.getAvailableSeats() == 0) {
            System.out.println("❌ No seats available on this train!\n");
            return;
        }

        System.out.print("Enter your name: ");
        String passengerName = scanner.nextLine();

        selectedTrain.displaySeatAvailability();

        System.out.print("Enter seat number (1-" + (selectedTrain.getAvailableSeats() + 50) + "): ");
        try {
            int seatNumber = Integer.parseInt(scanner.nextLine());

            if (!selectedTrain.bookSeat(seatNumber)) {
                System.out.println("❌ Seat not available or invalid seat number!\n");
                return;
            }

            Ticket ticket = new Ticket(passengerName, selectedTrain.getSource(),
                    selectedTrain.getDestination(), seatNumber, selectedTrain.getFarePerSeat());
            selectedTrain.addBookedTicket(ticket);
            userTickets.add(ticket);

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       BOOKING CONFIRMATION              ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ Ticket ID: " + String.format("%-28s", ticket.getTicketId()) + "║");
            System.out.println("║ Passenger: " + String.format("%-28s", ticket.getPassengerName()) + "║");
            System.out.println("║ Train: " + String.format("%-32s", selectedTrain.getTrainName()) + "║");
            System.out.println("║ Route: " + String.format("%-31s", selectedTrain.getSource() + " → " + selectedTrain.getDestination()) + "║");
            System.out.println("║ Seat: " + String.format("%-33s", String.valueOf(seatNumber)) + "║");
            System.out.println("║ Fare: Rs. " + String.format("%-29.2f", selectedTrain.getFarePerSeat()) + "║");
            System.out.println("╚════════════════════════════════════════╝\n");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid seat number!\n");
        }
    }

    private static void viewMyTickets() {
        if (userTickets.isEmpty()) {
            System.out.println("\n📋 You have no bookings!\n");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            MY BOOKINGS                                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Ticket ID | Passenger Name | Route            | Seat | Fare      | Booked   ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");

        for (Ticket ticket : userTickets) {
            System.out.println("║ " + ticket + " ║");
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static void cancelTicket() {
        if (userTickets.isEmpty()) {
            System.out.println("\n❌ You have no bookings to cancel!\n");
            return;
        }

        System.out.print("Enter Ticket ID to cancel: ");
        String ticketId = scanner.nextLine().toUpperCase();

        Ticket ticketToCancel = null;
        for (Ticket ticket : userTickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                ticketToCancel = ticket;
                break;
            }
        }

        if (ticketToCancel == null) {
            System.out.println("❌ Ticket not found!\n");
            return;
        }

        for (Train train : trains) {
            if (train.findTicket(ticketId) != null) {
                train.removeTicket(ticketId);
                break;
            }
        }

        userTickets.remove(ticketToCancel);
        System.out.println("✓ Ticket cancelled successfully!");
        System.out.println("✓ Refund of Rs. " + 3000 + " will be processed.\n");
    }

    private static Train findTrain(String trainId) {
        for (Train train : trains) {
            if (train.getTrainId().equals(trainId)) {
                return train;
            }
        }
        return null;
    }
}
