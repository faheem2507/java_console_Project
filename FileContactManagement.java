import java.io.*;
import java.util.*;

class Contact {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public Contact(String id, String name, String phoneNumber, String email, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Convert contact to file format: id|name|phone|email|address
    public String toFileFormat() {
        return id + "|" + name + "|" + phoneNumber + "|" + email + "|" + address;
    }

    // Parse contact from file format
    public static Contact fromFileFormat(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
            return new Contact(parts[0], parts[1], parts[2], parts[3], parts[4]);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("ID: %-5s | Name: %-20s | Phone: %-12s | Email: %-25s | Address: %-20s",
                id, name, phoneNumber, email, address);
    }
}

class ContactManager {
    private String filePath;
    private ArrayList<Contact> contacts;
    private static int contactCounter = 1000;

    public ContactManager(String filePath) {
        this.filePath = filePath;
        this.contacts = new ArrayList<>();
        loadContactsFromFile();
    }

    private void loadContactsFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("✓ New contact file will be created on first save.\n");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Contact contact = Contact.fromFileFormat(line);
                if (contact != null) {
                    contacts.add(contact);
                    // Update counter
                    try {
                        int id = Integer.parseInt(contact.getId());
                        if (id > contactCounter) {
                            contactCounter = id;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                }
            }
            System.out.println("✓ Loaded " + contacts.size() + " contacts from file.\n");
        } catch (IOException e) {
            System.out.println("⚠ Error reading file: " + e.getMessage() + "\n");
        }
    }

    public void saveContactsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Contact contact : contacts) {
                writer.write(contact.toFileFormat());
                writer.newLine();
            }
            System.out.println("✓ Contacts saved successfully to file.\n");
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage() + "\n");
        }
    }

    public void addContact(String name, String phoneNumber, String email, String address) {
        // Validate input
        if (name.isEmpty() || phoneNumber.isEmpty()) {
            System.out.println("❌ Name and phone number are required!\n");
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            System.out.println("❌ Invalid phone number format! (Should be 10 digits)\n");
            return;
        }

        if (!email.isEmpty() && !isValidEmail(email)) {
            System.out.println("❌ Invalid email format!\n");
            return;
        }

        String id = String.valueOf(++contactCounter);
        Contact contact = new Contact(id, name, phoneNumber, email, address);
        contacts.add(contact);

        System.out.println("✓ Contact added successfully!");
        System.out.println("  Contact ID: " + id + "\n");
    }

    public Contact searchById(String id) {
        for (Contact contact : contacts) {
            if (contact.getId().equals(id)) {
                return contact;
            }
        }
        return null;
    }

    public ArrayList<Contact> searchByName(String name) {
        ArrayList<Contact> results = new ArrayList<>();
        String searchLower = name.toLowerCase();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(searchLower)) {
                results.add(contact);
            }
        }
        return results;
    }

    public ArrayList<Contact> searchByPhoneNumber(String phoneNumber) {
        ArrayList<Contact> results = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getPhoneNumber().contains(phoneNumber)) {
                results.add(contact);
            }
        }
        return results;
    }

    public boolean deleteContact(String id) {
        for (Contact contact : contacts) {
            if (contact.getId().equals(id)) {
                contacts.remove(contact);
                System.out.println("✓ Contact deleted successfully.\n");
                return true;
            }
        }
        System.out.println("❌ Contact not found!\n");
        return false;
    }

    public void displayAllContacts() {
        if (contacts.isEmpty()) {
            System.out.println("\n📋 No contacts found!\n");
            return;
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              ALL CONTACTS                                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Contact contact : contacts) {
            System.out.println("║ " + contact + " ║");
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Contacts: " + contacts.size() + "\n");
    }

    public ArrayList<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}

public class FileContactManagement {
    private static Scanner scanner = new Scanner(System.in);
    private static ContactManager contactManager;
    private static final String DATA_FILE = "contacts.txt";

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   FILE-BASED CONTACT MANAGEMENT        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        contactManager = new ContactManager(DATA_FILE);

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Select an option (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addContact();
                    break;
                case "2":
                    displayAllContacts();
                    break;
                case "3":
                    searchContact();
                    break;
                case "4":
                    updateContact();
                    break;
                case "5":
                    deleteContact();
                    break;
                case "6":
                    contactManager.saveContactsToFile();
                    break;
                case "7":
                    exportContactsToFile();
                    break;
                case "8":
                    System.out.println("\n✓ Saving contacts before exit...");
                    contactManager.saveContactsToFile();
                    System.out.println("✓ Thank you for using Contact Management System. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("❌ Invalid option! Please select 1-8.\n");
            }
        }

        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                      ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Add Contact                          ║");
        System.out.println("║ 2. Display All Contacts                 ║");
        System.out.println("║ 3. Search Contact                       ║");
        System.out.println("║ 4. Update Contact                       ║");
        System.out.println("║ 5. Delete Contact                       ║");
        System.out.println("║ 6. Save to File                         ║");
        System.out.println("║ 7. Export Contacts (Detailed)           ║");
        System.out.println("║ 8. Exit                                 ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void addContact() {
        System.out.println("\n📝 ADD NEW CONTACT");
        System.out.println("─────────────────");

        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter phone number (10 digits): ");
        String phoneNumber = scanner.nextLine().trim();

        System.out.print("Enter email (optional): ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter address (optional): ");
        String address = scanner.nextLine().trim();

        contactManager.addContact(name, phoneNumber, email, address);
    }

    private static void displayAllContacts() {
        contactManager.displayAllContacts();
    }

    private static void searchContact() {
        System.out.println("\n🔍 SEARCH CONTACT");
        System.out.println("─────────────────");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Phone Number");
        System.out.print("Select search option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                System.out.print("Enter Contact ID: ");
                String id = scanner.nextLine().trim();
                Contact contact = contactManager.searchById(id);
                if (contact != null) {
                    System.out.println("\n" + contact + "\n");
                } else {
                    System.out.println("❌ Contact not found!\n");
                }
                break;

            case "2":
                System.out.print("Enter name to search: ");
                String name = scanner.nextLine().trim();
                ArrayList<Contact> nameResults = contactManager.searchByName(name);
                if (!nameResults.isEmpty()) {
                    System.out.println("\n📋 Search Results:");
                    for (Contact c : nameResults) {
                        System.out.println(c);
                    }
                    System.out.println();
                } else {
                    System.out.println("❌ No contacts found!\n");
                }
                break;

            case "3":
                System.out.print("Enter phone number: ");
                String phone = scanner.nextLine().trim();
                ArrayList<Contact> phoneResults = contactManager.searchByPhoneNumber(phone);
                if (!phoneResults.isEmpty()) {
                    System.out.println("\n📋 Search Results:");
                    for (Contact c : phoneResults) {
                        System.out.println(c);
                    }
                    System.out.println();
                } else {
                    System.out.println("❌ No contacts found!\n");
                }
                break;

            default:
                System.out.println("❌ Invalid option!\n");
        }
    }

    private static void updateContact() {
        System.out.println("\n✏ UPDATE CONTACT");
        System.out.println("────────────────");

        System.out.print("Enter Contact ID to update: ");
        String id = scanner.nextLine().trim();
        Contact contact = contactManager.searchById(id);

        if (contact == null) {
            System.out.println("❌ Contact not found!\n");
            return;
        }

        System.out.println("Current Details: " + contact);
        System.out.println("\nEnter new details (leave blank to keep current value)");

        System.out.print("New name [" + contact.getName() + "]: ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            contact.setName(newName);
        }

        System.out.print("New phone [" + contact.getPhoneNumber() + "]: ");
        String newPhone = scanner.nextLine().trim();
        if (!newPhone.isEmpty()) {
            contact.setPhoneNumber(newPhone);
        }

        System.out.print("New email [" + contact.getEmail() + "]: ");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            contact.setEmail(newEmail);
        }

        System.out.print("New address [" + contact.getAddress() + "]: ");
        String newAddress = scanner.nextLine().trim();
        if (!newAddress.isEmpty()) {
            contact.setAddress(newAddress);
        }

        System.out.println("✓ Contact updated successfully!\n");
    }

    private static void deleteContact() {
        System.out.println("\n🗑 DELETE CONTACT");
        System.out.println("─────────────────");

        System.out.print("Enter Contact ID to delete: ");
        String id = scanner.nextLine().trim();
        contactManager.deleteContact(id);
    }

    private static void exportContactsToFile() {
        System.out.println("\n📤 EXPORT CONTACTS");
        System.out.println("──────────────────");

        System.out.print("Enter file name (without extension): ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            System.out.println("❌ Invalid file name!\n");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
            writer.write("╔════════════════════════════════════════════════════════════════╗\n");
            writer.write("║                    CONTACT EXPORT REPORT                       ║\n");
            writer.write("╚════════════════════════════════════════════════════════════════╝\n\n");

            writer.write("Total Contacts: " + contactManager.getAllContacts().size() + "\n");
            writer.write("Export Date: " + new Date() + "\n\n");
            writer.write("────────────────────────────────────────────────────────────────\n");

            for (Contact contact : contactManager.getAllContacts()) {
                writer.write("\nID: " + contact.getId() + "\n");
                writer.write("Name: " + contact.getName() + "\n");
                writer.write("Phone: " + contact.getPhoneNumber() + "\n");
                writer.write("Email: " + contact.getEmail() + "\n");
                writer.write("Address: " + contact.getAddress() + "\n");
                writer.write("────────────────────────────────────────────────────────────────\n");
            }

            System.out.println("✓ Contacts exported successfully to: " + fileName + ".txt\n");
        } catch (IOException e) {
            System.out.println("❌ Error exporting file: " + e.getMessage() + "\n");
        }
    }
}
