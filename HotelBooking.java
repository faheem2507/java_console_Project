import java.util.*;

public class HotelBooking {

    enum RoomType {
        AC, NON_AC
    }

    static class Room {
        int roomNumber;
        RoomType type;
        double pricePerNight;
        boolean isBooked;
        String guestName;
        String guestPhone;
        int nights;

        Room(int roomNumber, RoomType type, double pricePerNight) {
            this.roomNumber = roomNumber;
            this.type = type;
            this.pricePerNight = pricePerNight;
            this.isBooked = false;
        }

        void display() {
            System.out.println("-----------------------------");
            System.out.println("Room No  : " + roomNumber);
            System.out.println("Type     : " + type);
            System.out.printf("Price    : Rs.%.2f/night%n", pricePerNight);
            if (isBooked) {
                System.out.println("Status   : Booked");
                System.out.println("Guest    : " + guestName);
                System.out.println("Phone    : " + guestPhone);
                System.out.println("Nights   : " + nights);
                System.out.printf("Total    : Rs.%.2f%n", pricePerNight * nights);
            } else {
                System.out.println("Status   : Available");
            }
            System.out.println("-----------------------------");
        }
    }

    static List<Room> rooms = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input.");
            }
        }
    }

    // Pre-load rooms on startup
    static void initRooms() {
        // AC Rooms: 101-105 at Rs.2500/night
        for (int i = 101; i <= 105; i++)
            rooms.add(new Room(i, RoomType.AC, 2500));
        // Non-AC Rooms: 201-205 at Rs.1200/night
        for (int i = 201; i <= 205; i++)
            rooms.add(new Room(i, RoomType.NON_AC, 1200));
    }

    static Room findRoom(int roomNo) {
        for (Room r : rooms)
            if (r.roomNumber == roomNo)
                return r;
        return null;
    }

    static void showAvailable() {
        System.out.println("\n--- Available Rooms ---");
        System.out.println("  AC Rooms (Rs.2500/night):");
        boolean anyAC = false;
        for (Room r : rooms) {
            if (r.type == RoomType.AC && !r.isBooked) {
                System.out.println("    Room " + r.roomNumber);
                anyAC = true;
            }
        }
        if (!anyAC)
            System.out.println("    None available.");

        System.out.println("  Non-AC Rooms (Rs.1200/night):");
        boolean anyNAC = false;
        for (Room r : rooms) {
            if (r.type == RoomType.NON_AC && !r.isBooked) {
                System.out.println("    Room " + r.roomNumber);
                anyNAC = true;
            }
        }
        if (!anyNAC)
            System.out.println("    None available.");
    }

    static void bookRoom() {
        System.out.println("\n--- Book Room ---");
        showAvailable();
        int roomNo = readInt("Enter room number to book: ");
        Room r = findRoom(roomNo);
        if (r == null) {
            System.out.println("  Room not found.");
            return;
        }
        if (r.isBooked) {
            System.out.println("  Room already booked. Choose another.");
            return;
        }

        System.out.print("Enter guest name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("  Name cannot be empty.");
            return;
        }
        System.out.print("Enter guest phone: ");
        String phone = sc.nextLine().trim();
        if (phone.isEmpty()) {
            System.out.println("  Phone cannot be empty.");
            return;
        }
        int nights = readInt("Enter number of nights: ");
        if (nights <= 0) {
            System.out.println("  Nights must be at least 1.");
            return;
        }

        r.isBooked = true;
        r.guestName = name;
        r.guestPhone = phone;
        r.nights = nights;

        System.out.println("  Room " + roomNo + " booked for " + name + ".");
        System.out.printf("  Total charge: Rs.%.2f%n", r.pricePerNight * nights);
    }

    static void cancelBooking() {
        System.out.println("\n--- Cancel Booking ---");
        int roomNo = readInt("Enter room number to cancel: ");
        Room r = findRoom(roomNo);
        if (r == null) {
            System.out.println("  Room not found.");
            return;
        }
        if (!r.isBooked) {
            System.out.println("  Room is not currently booked.");
            return;
        }
        System.out.print("  Confirm cancel booking for " + r.guestName + "? (yes/no): ");
        if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
            r.isBooked = false;
            r.guestName = null;
            r.guestPhone = null;
            r.nights = 0;
            System.out.println("  Booking cancelled. Room " + roomNo + " is now available.");
        } else {
            System.out.println("  Cancellation aborted.");
        }
    }

    static void displayAll() {
        System.out.println("\n--- All Rooms ---");
        for (Room r : rooms)
            r.display();
    }

    static void viewBookedRooms() {
        System.out.println("\n--- Booked Rooms ---");
        boolean found = false;
        for (Room r : rooms) {
            if (r.isBooked) {
                r.display();
                found = true;
            }
        }
        if (!found)
            System.out.println("  No rooms currently booked.");
    }

    public static void main(String[] args) {
        initRooms();
        System.out.println("==============================");
        System.out.println("   Hotel Room Booking System  ");
        System.out.println("==============================");
        System.out.println("  AC Rooms   : 101-105 | Rs.2500/night");
        System.out.println("  Non-AC Rooms: 201-205 | Rs.1200/night");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Show Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Rooms");
            System.out.println("5. View Booked Rooms");
            System.out.println("6. Exit");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> showAvailable();
                case 2 -> bookRoom();
                case 3 -> cancelBooking();
                case 4 -> displayAll();
                case 5 -> viewBookedRooms();
                case 6 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice.");
            }
        }
    }
}