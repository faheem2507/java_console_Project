import java.util.*;

class Booking {
    private String bookingId;
    private String passengerName;
    private int seatNumber;
    private double fare;
    private String bookingDate;

    public Booking(String bookingId, String passengerName, int seatNumber, double fare) {
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
        this.fare = fare;
        this.bookingDate = new Date().toString();
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public double getFare() {
        return fare;
    }

    @Override
    public String toString() {
        return String.format("Booking: %-12s | Passenger: %-20s | Seat: %2d | Fare: Rs. %.2f",
                bookingId, passengerName, seatNumber, fare);
    }
}

class Bus {
    private String busId;
    private String busName;
    private String source;
    private String destination;
    private int totalSeats;
    private boolean[] seatAvailability;
    private double farePerSeat;
    private ArrayList<Booking> bookings;
    private static int bookingCounter = 1000;

    public Bus(String busId, String busName, String source, String destination, 
               int totalSeats, double farePerSeat) {
        this.busId = busId;
        this.busName = busName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.seatAvailability = new boolean[totalSeats];
        Arrays.fill(seatAvailability, true); // true = available
        this.farePerSeat = farePerSeat;
        this.bookings = new ArrayList<>();
    }

    public String getBusId() {
        return busId;
    }

    public String getBusName() {
        return busName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public double getFarePerSeat() {
        return farePerSeat;
    }

    public int getAvailableSeats() {
        int count = 0;
        for (boolean seat : seatAvailability) {
            if (seat) count++;
        }
        return count;
    }

    public int getBookedSeats() {
        return totalSeats - getAvailableSeats();
    }

    public boolean isSeatAvailable(int seatNumber) {
        if (seatNumber < 1 || seatNumber > totalSeats) return false;
        return seatAvailability[seatNumber - 1];
    }

    public boolean bookSeat(String passengerName, int seatNumber) {
        if (!isSeatAvailable(seatNumber)) {
            return false;
        }

        seatAvailability[seatNumber - 1] = false;
        String bookingId = "BUS" + (++bookingCounter);
        Booking booking = new Booking(bookingId, passengerName, seatNumber, farePerSeat);
        bookings.add(booking);
        return true;
    }

    public boolean cancelBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                seatAvailability[booking.getSeatNumber() - 1] = true;
                bookings.remove(booking);
                return true;
            }
        }
        return false;
    }

    public Booking findBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }
        return null;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void displaySeatMap() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          SEAT AVAILABILITY MAP          ║");
        System.out.println("║  🟩 = Available    🟥 = Booked          ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        int seatsPerRow = 10;
        for (int i = 0; i < totalSeats; i++) {
            if (i > 0 && i % seatsPerRow == 0) {
                System.out.println();
            }
            if (seatAvailability[i]) {
                System.out.print("🟩" + String.format("%2d", (i + 1)) + " ");
            } else {
                System.out.print("🟥" + String.format("%2d", (i + 1)) + " ");
            }
        }
        System.out.println("\n");
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-20s | %s → %s | Total: %2d | Available: %2d | Rs. %.2f",
                busId, busName, source, destination, totalSeats, getAvailableSeats(), farePerSeat);
    }
}

public class BusReservation {
    private static ArrayList<Bus> buses;
    private static ArrayList<Booking> allBookings;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     BUS RESERVATION SYSTEM              ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        buses = new ArrayList<>();
        allBookings = new ArrayList<>();

        initializeBuses();

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Select an option (1-6): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAvailableBuses();
                    break;
                case "2":
                    bookBusSeat();
                    break;
                case "3":
                    viewMyBookings();
                    break;
                case "4":
                    cancelReservation();
                    break;
                case "5":
                    displayBusSeatStatus();
                    break;
                case "6":
                    System.out.println("\n✓ Thank you for using Bus Reservation System. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("❌ Invalid option! Please select 1-6.\n");
            }
        }

        scanner.close();
    }

    private static void initializeBuses() {
        buses.add(new Bus("B001", "Express 500", "Delhi", "Mumbai", 40, 1500));
        buses.add(new Bus("B002", "Comfort Plus", "Delhi", "Bangalore", 50, 1800));
        buses.add(new Bus("B003", "Travel King", "Mumbai", "Pune", 45, 800));
        buses.add(new Bus("B004", "Night Runner", "Delhi", "Kolkata", 35, 1200));
        buses.add(new Bus("B005", "Premium Travel", "Mumbai", "Goa", 55, 1000));
    }

    private static void displayMainMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                      ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. View Available Buses                 ║");
        System.out.println("║ 2. Book Bus Seat                        ║");
        System.out.println("║ 3. View My Bookings                     ║");
        System.out.println("║ 4. Cancel Reservation                   ║");
        System.out.println("║ 5. View Bus Seat Status                 ║");
        System.out.println("║ 6. Exit                                 ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void viewAvailableBuses() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         AVAILABLE BUSES                                   ║");
        System.out.println("║ Bus ID | Bus Name | From → To | Total | Available | Fare                 ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");

        for (Bus bus : buses) {
            System.out.println("║ " + bus + " ║");
        }

        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static void bookBusSeat() {
        viewAvailableBuses();

        System.out.print("Enter Bus ID to book: ");
        String busId = scanner.nextLine().trim().toUpperCase();

        Bus selectedBus = findBus(busId);
        if (selectedBus == null) {
            System.out.println("❌ Bus not found!\n");
            return;
        }

        if (selectedBus.getAvailableSeats() == 0) {
            System.out.println("❌ No seats available on this bus!\n");
            return;
        }

        selectedBus.displaySeatMap();

        System.out.print("Enter your name: ");
        String passengerName = scanner.nextLine().trim();

        if (passengerName.isEmpty()) {
            System.out.println("❌ Passenger name is required!\n");
            return;
        }

        System.out.print("Enter seat number (1-" + selectedBus.getTotalSeats() + "): ");
        try {
            int seatNumber = Integer.parseInt(scanner.nextLine().trim());

            if (selectedBus.bookSeat(passengerName, seatNumber)) {
                Booking booking = selectedBus.findBooking(selectedBus.getBookings().get(
                        selectedBus.getBookings().size() - 1).getBookingId());
                allBookings.add(booking);

                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║        ✓ BOOKING CONFIRMED              ║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║ Booking ID:    " + booking.getBookingId());
                System.out.println("║ Passenger:     " + passengerName);
                System.out.println("║ Bus:           " + selectedBus.getBusName());
                System.out.println("║ Route:         " + selectedBus.getSource() + " → " + 
                                selectedBus.getDestination());
                System.out.println("║ Seat Number:   " + seatNumber);
                System.out.println("║ Fare:          Rs. " + selectedBus.getFarePerSeat());
                System.out.println("╚════════════════════════════════════════╝\n");
            } else {
                System.out.println("❌ Seat not available or invalid seat number!\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid seat number!\n");
        }
    }

    private static void viewMyBookings() {
        if (allBookings.isEmpty()) {
            System.out.println("\n📋 You have no bookings!\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         MY BOOKINGS                                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");

        for (Booking booking : allBookings) {
            System.out.println("║ " + booking + " ║");
        }

        System.out.println("║                                                                            ║");
        double totalFare = 0;
        for (Booking booking : allBookings) {
            totalFare += booking.getFare();
        }
        System.out.printf("║ Total Bookings: %-67s║\n", allBookings.size());
        System.out.printf("║ Total Amount: Rs. %-62.2f║\n", totalFare);
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static void cancelReservation() {
        if (allBookings.isEmpty()) {
            System.out.println("\n❌ You have no bookings to cancel!\n");
            return;
        }

        System.out.print("Enter Booking ID to cancel: ");
        String bookingId = scanner.nextLine().trim().toUpperCase();

        Booking bookingToCancel = null;
        Bus busWithBooking = null;

        for (Bus bus : buses) {
            Booking booking = bus.findBooking(bookingId);
            if (booking != null) {
                bookingToCancel = booking;
                busWithBooking = bus;
                break;
            }
        }

        if (bookingToCancel == null) {
            System.out.println("❌ Booking not found!\n");
            return;
        }

        busWithBooking.cancelBooking(bookingId);
        allBookings.remove(bookingToCancel);

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      ✓ BOOKING CANCELLED                ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Booking ID: " + bookingId);
        System.out.println("║ Refund: Rs. " + String.format("%-26.2f", bookingToCancel.getFare()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    private static void displayBusSeatStatus() {
        System.out.println();
        System.out.print("Enter Bus ID to view seat status: ");
        String busId = scanner.nextLine().trim().toUpperCase();

        Bus selectedBus = findBus(busId);
        if (selectedBus == null) {
            System.out.println("❌ Bus not found!\n");
            return;
        }

        selectedBus.displaySeatMap();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          BUS STATUS REPORT              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Bus ID:           " + String.format("%-21s", selectedBus.getBusId()) + "║");
        System.out.println("║ Bus Name:         " + String.format("%-21s", selectedBus.getBusName()) + "║");
        System.out.println("║ Route:            " + String.format("%-21s", 
                selectedBus.getSource() + " → " + selectedBus.getDestination()) + "║");
        System.out.println("║ Total Seats:      " + String.format("%-21d", selectedBus.getTotalSeats()) + "║");
        System.out.println("║ Booked Seats:     " + String.format("%-21d", selectedBus.getBookedSeats()) + "║");
        System.out.println("║ Available Seats:  " + String.format("%-21d", selectedBus.getAvailableSeats()) + "║");
        System.out.println("║ Occupancy:        " + String.format("%-20.1f%%", 
                (selectedBus.getBookedSeats() * 100.0 / selectedBus.getTotalSeats())) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    private static Bus findBus(String busId) {
        for (Bus bus : buses) {
            if (bus.getBusId().equals(busId)) {
                return bus;
            }
        }
        return null;
    }
}
