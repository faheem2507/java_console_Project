import java.util.*;

public class InventoryManagement {

    static final int LOW_STOCK_THRESHOLD = 5;

    static class Product {
        int id;
        String name;
        int quantity;
        double price;

        Product(int id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        double totalValue() {
            return quantity * price;
        }

        void display() {
            String alert = quantity <= LOW_STOCK_THRESHOLD ? "  *** LOW STOCK ***" : "";
            System.out.println("-----------------------------");
            System.out.println("Product ID : " + id);
            System.out.println("Name       : " + name);
            System.out.println("Quantity   : " + quantity + alert);
            System.out.printf("Price      : Rs.%.2f%n", price);
            System.out.printf("Total Value: Rs.%.2f%n", totalValue());
            System.out.println("-----------------------------");
        }
    }

    static List<Product> inventory = new ArrayList<>();
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

    static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val <= 0)
                    System.out.println("  Must be greater than zero.");
                else
                    return val;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input.");
            }
        }
    }

    static boolean idExists(int id) {
        for (Product p : inventory)
            if (p.id == id)
                return true;
        return false;
    }

    static Product findById(int id) {
        for (Product p : inventory)
            if (p.id == id)
                return p;
        return null;
    }

    static void addProduct() {
        System.out.println("\n--- Add Product ---");
        int id;
        while (true) {
            id = readInt("Enter Product ID: ");
            if (idExists(id))
                System.out.println("  ID already exists.");
            else
                break;
        }
        System.out.print("Enter Product Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("  Name cannot be empty.");
            return;
        }
        int qty = readInt("Enter Quantity: ");
        if (qty < 0) {
            System.out.println("  Quantity cannot be negative.");
            return;
        }
        double price = readPositiveDouble("Enter Price (Rs.): ");
        inventory.add(new Product(id, name, qty, price));
        System.out.println("  Product added successfully.");
        if (qty <= LOW_STOCK_THRESHOLD)
            System.out.println("  Warning: Stock is already low for this product.");
    }

    static void updateStock() {
        System.out.println("\n--- Update Stock ---");
        int id = readInt("Enter Product ID: ");
        Product p = findById(id);
        if (p == null) {
            System.out.println("  Product not found.");
            return;
        }
        System.out.println("  Current quantity: " + p.quantity);
        System.out.println("  1. Add stock   2. Remove stock");
        int choice = readInt("  Choice: ");
        int amount = readInt("  Enter amount: ");
        if (amount <= 0) {
            System.out.println("  Amount must be positive.");
            return;
        }
        if (choice == 1) {
            p.quantity += amount;
            System.out.println("  Stock updated. New quantity: " + p.quantity);
        } else if (choice == 2) {
            if (amount > p.quantity) {
                System.out.println("  Cannot remove more than current stock (" + p.quantity + ").");
                return;
            }
            p.quantity -= amount;
            System.out.println("  Stock updated. New quantity: " + p.quantity);
            if (p.quantity <= LOW_STOCK_THRESHOLD)
                System.out.println("  Warning: Stock is now low for " + p.name + ".");
        } else {
            System.out.println("  Invalid choice.");
        }
    }

    static void removeProduct() {
        System.out.println("\n--- Remove Product ---");
        int id = readInt("Enter Product ID: ");
        Product p = findById(id);
        if (p == null) {
            System.out.println("  Product not found.");
            return;
        }
        System.out.print("  Confirm remove '" + p.name + "'? (yes/no): ");
        if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
            inventory.remove(p);
            System.out.println("  Product removed.");
        } else {
            System.out.println("  Cancelled.");
        }
    }

    static void displayInventory() {
        System.out.println("\n--- Inventory ---");
        if (inventory.isEmpty()) {
            System.out.println("  No products found.");
            return;
        }
        double totalValue = 0;
        for (Product p : inventory) {
            p.display();
            totalValue += p.totalValue();
        }
        System.out.printf("Total Inventory Value: Rs.%.2f%n", totalValue);
    }

    static void lowStockAlert() {
        System.out.println("\n--- Low Stock Alert (qty <= " + LOW_STOCK_THRESHOLD + ") ---");
        boolean found = false;
        for (Product p : inventory) {
            if (p.quantity <= LOW_STOCK_THRESHOLD) {
                System.out.println("  [ALERT] " + p.name + " (ID: " + p.id + ") - Qty: " + p.quantity);
                found = true;
            }
        }
        if (!found)
            System.out.println("  All products have sufficient stock.");
    }

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("  Inventory Management System ");
        System.out.println("==============================");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Product");
            System.out.println("2. Update Stock");
            System.out.println("3. Remove Product");
            System.out.println("4. Display Inventory");
            System.out.println("5. Low Stock Alerts");
            System.out.println("6. Exit");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addProduct();
                case 2 -> updateStock();
                case 3 -> removeProduct();
                case 4 -> displayInventory();
                case 5 -> lowStockAlert();
                case 6 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice.");
            }
        }
    }
}