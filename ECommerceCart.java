import java.util.*;

class Product {
    private String productId;
    private String name;
    private double price;
    private int stock;

    public Product(String productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean updateStock(int quantity) {
        if (quantity > stock) {
            return false;
        }
        stock -= quantity;
        return true;
    }

    public void restoreStock(int quantity) {
        stock += quantity;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - Rs. %.2f (Stock: %d)", productId, name, price, stock);
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%-20s | Qty: %2d | Price: Rs. %-8.2f | Subtotal: Rs. %.2f",
                product.getName(), quantity, product.getPrice(), getSubtotal());
    }
}

class ShoppingCart {
    private ArrayList<CartItem> items;
    private double discountPercentage;

    public ShoppingCart() {
        items = new ArrayList<>();
        discountPercentage = 0;
    }

    public void addProduct(Product product, int quantity) {
        if (product.getStock() < quantity) {
            System.out.println("❌ Insufficient stock! Available: " + product.getStock());
            return;
        }

        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.setQuantity(item.getQuantity() + quantity);
                product.updateStock(quantity);
                System.out.println("✓ Updated quantity for: " + product.getName());
                return;
            }
        }

        CartItem newItem = new CartItem(product, quantity);
        items.add(newItem);
        product.updateStock(quantity);
        System.out.println("✓ Added " + quantity + " x " + product.getName() + " to cart");
    }

    public void removeProduct(String productId) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.getProduct().restoreStock(item.getQuantity());
                items.remove(item);
                System.out.println("✓ Product removed from cart");
                return;
            }
        }
        System.out.println("❌ Product not found in cart");
    }

    public double getSubtotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public double getDiscount() {
        return getSubtotal() * (discountPercentage / 100);
    }

    public double getTotal() {
        return getSubtotal() - getDiscount();
    }

    public void applyDiscount(double percentage) {
        if (percentage >= 0 && percentage <= 100) {
            discountPercentage = percentage;
            System.out.println("✓ Discount of " + percentage + "% applied");
        } else {
            System.out.println("❌ Invalid discount! Please enter 0-100");
        }
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("\n📦 Your cart is empty\n");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        SHOPPING CART                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Product              | Qty | Price      | Subtotal                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.println("║ " + item + " ║");
        }

        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Subtotal                                         Rs. %-12.2f  ║\n", getSubtotal());
        if (discountPercentage > 0) {
            System.out.printf("║ Discount (%d%%)                                    Rs. %-12.2f  ║\n", 
                    (int) discountPercentage, getDiscount());
        }
        System.out.printf("║ TOTAL                                            Rs. %-12.2f  ║\n", getTotal());
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void checkout() {
        if (isEmpty()) {
            System.out.println("❌ Cart is empty! Add products before checkout.");
            return;
        }

        displayCart();
        System.out.println("✓ Checkout successful!");
        System.out.println("✓ Payment processed for Rs. " + getTotal());
        items.clear();
        discountPercentage = 0;
        System.out.println("✓ Cart cleared. Thank you for your purchase!\n");
    }
}

public class ECommerceCart {
    private static Scanner scanner = new Scanner(System.in);
    private static ShoppingCart cart;
    private static ArrayList<Product> productCatalog;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO E-COMMERCE SHOPPING CART  ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        initializeProductCatalog();
        cart = new ShoppingCart();

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Select an option (1-5): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    browseCatalog();
                    break;
                case "2":
                    addToCart();
                    break;
                case "3":
                    cart.displayCart();
                    break;
                case "4":
                    applyDiscount();
                    break;
                case "5":
                    cart.checkout();
                    exit = true;
                    break;
                default:
                    System.out.println("❌ Invalid option! Please select 1-5.\n");
            }
        }

        System.out.println("✓ Thank you for shopping with us!");
        scanner.close();
    }

    private static void initializeProductCatalog() {
        productCatalog = new ArrayList<>();
        productCatalog.add(new Product("P001", "Laptop", 45000, 10));
        productCatalog.add(new Product("P002", "Mouse", 500, 50));
        productCatalog.add(new Product("P003", "Keyboard", 1500, 30));
        productCatalog.add(new Product("P004", "Monitor", 15000, 15));
        productCatalog.add(new Product("P005", "Headphones", 2000, 25));
        productCatalog.add(new Product("P006", "USB Cable", 300, 100));
        productCatalog.add(new Product("P007", "Webcam", 3000, 12));
        productCatalog.add(new Product("P008", "Speaker", 5000, 20));
    }

    private static void displayMainMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                      ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Browse Product Catalog               ║");
        System.out.println("║ 2. Add Product to Cart                  ║");
        System.out.println("║ 3. View Cart                            ║");
        System.out.println("║ 4. Apply Discount                       ║");
        System.out.println("║ 5. Checkout                             ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void browseCatalog() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     PRODUCT CATALOG                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        for (Product product : productCatalog) {
            System.out.println(product);
        }
        System.out.println();
    }

    private static void addToCart() {
        browseCatalog();
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine().toUpperCase();

        Product product = findProduct(productId);
        if (product == null) {
            System.out.println("❌ Product not found!\n");
            return;
        }

        System.out.print("Enter quantity: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            if (quantity <= 0) {
                System.out.println("❌ Quantity must be greater than 0!\n");
                return;
            }
            cart.addProduct(product, quantity);
            System.out.println();
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid quantity!\n");
        }
    }

    private static void applyDiscount() {
        if (cart.isEmpty()) {
            System.out.println("❌ Cart is empty! Add products first.\n");
            return;
        }

        System.out.print("Enter discount percentage (0-100): ");
        try {
            double discount = Double.parseDouble(scanner.nextLine());
            cart.applyDiscount(discount);
            System.out.println();
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid discount value!\n");
        }
    }

    private static Product findProduct(String productId) {
        for (Product product : productCatalog) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
}
