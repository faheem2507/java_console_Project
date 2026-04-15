import java.util.*;

public class LibraryManagement {

    static class Book {
        int id;
        String title;
        String author;
        boolean isIssued;
        String issuedTo;

        Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.isIssued = false;
            this.issuedTo = null;
        }

        void display() {
            System.out.println("-----------------------------");
            System.out.println("Book ID  : " + id);
            System.out.println("Title    : " + title);
            System.out.println("Author   : " + author);
            System.out.println("Status   : " + (isIssued ? "Issued to " + issuedTo : "Available"));
            System.out.println("-----------------------------");
        }
    }

    static List<Book> books = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Enter a whole number.");
            }
        }
    }

    static boolean idExists(int id) {
        for (Book b : books)
            if (b.id == id)
                return true;
        return false;
    }

    static Book findById(int id) {
        for (Book b : books)
            if (b.id == id)
                return b;
        return null;
    }

    static void addBook() {
        System.out.println("\n--- Add Book ---");
        int id;
        while (true) {
            id = readInt("Enter Book ID: ");
            if (idExists(id))
                System.out.println("  ID already exists.");
            else
                break;
        }
        System.out.print("Enter Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = sc.nextLine().trim();
        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("  Title and author cannot be empty.");
            return;
        }
        books.add(new Book(id, title, author));
        System.out.println("  Book added successfully.");
    }

    static void issueBook() {
        System.out.println("\n--- Issue Book ---");
        int id = readInt("Enter Book ID to issue: ");
        Book b = findById(id);
        if (b == null) {
            System.out.println("  Book not found.");
            return;
        }
        if (b.isIssued) {
            System.out.println("  Book already issued to " + b.issuedTo + ".");
            return;
        }
        System.out.print("Enter user name: ");
        String user = sc.nextLine().trim();
        if (user.isEmpty()) {
            System.out.println("  User name cannot be empty.");
            return;
        }
        b.isIssued = true;
        b.issuedTo = user;
        System.out.println("  Book issued to " + user + " successfully.");
    }

    static void returnBook() {
        System.out.println("\n--- Return Book ---");
        int id = readInt("Enter Book ID to return: ");
        Book b = findById(id);
        if (b == null) {
            System.out.println("  Book not found.");
            return;
        }
        if (!b.isIssued) {
            System.out.println("  Book was not issued.");
            return;
        }
        System.out.println("  Book returned from " + b.issuedTo + ".");
        b.isIssued = false;
        b.issuedTo = null;
    }

    static void displayAvailable() {
        System.out.println("\n--- Available Books ---");
        boolean found = false;
        for (Book b : books) {
            if (!b.isIssued) {
                b.display();
                found = true;
            }
        }
        if (!found)
            System.out.println("  No available books.");
    }

    static void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.print("Enter title or author keyword: ");
        String keyword = sc.nextLine().trim().toLowerCase();
        boolean found = false;
        for (Book b : books) {
            if (b.title.toLowerCase().contains(keyword) || b.author.toLowerCase().contains(keyword)) {
                b.display();
                found = true;
            }
        }
        if (!found)
            System.out.println("  No matching books found.");
    }

    static void displayAll() {
        System.out.println("\n--- All Books ---");
        if (books.isEmpty()) {
            System.out.println("  No books in library.");
            return;
        }
        for (Book b : books)
            b.display();
    }

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("   Library Management System  ");
        System.out.println("==============================");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Display Available Books");
            System.out.println("5. Search by Title/Author");
            System.out.println("6. Display All Books");
            System.out.println("7. Exit");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addBook();
                case 2 -> issueBook();
                case 3 -> returnBook();
                case 4 -> displayAvailable();
                case 5 -> searchBooks();
                case 6 -> displayAll();
                case 7 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice.");
            }
        }
    }
}