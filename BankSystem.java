import java.util.*;

public class BankSystem {

    static class Account {
        String accountNumber;
        String holderName;
        double balance;
        List<String> transactionHistory;

        Account(String accountNumber, String holderName, double initialBalance) {
            this.accountNumber = accountNumber;
            this.holderName = holderName;
            this.balance = initialBalance;
            this.transactionHistory = new ArrayList<>();
            transactionHistory.add("Account created with balance: Rs." + initialBalance);
        }

        void deposit(double amount) {
            balance += amount;
            transactionHistory.add("Deposited: Rs." + amount + " | Balance: Rs." + balance);
            System.out.printf("  Rs.%.2f deposited. New balance: Rs.%.2f%n", amount, balance);
        }

        boolean withdraw(double amount) {
            if (amount > balance) {
                System.out.println("  Insufficient balance. Withdrawal denied.");
                return false;
            }
            balance -= amount;
            transactionHistory.add("Withdrawn: Rs." + amount + " | Balance: Rs." + balance);
            System.out.printf("  Rs.%.2f withdrawn. New balance: Rs.%.2f%n", amount, balance);
            return true;
        }

        void display() {
            System.out.println("-----------------------------");
            System.out.println("Account No : " + accountNumber);
            System.out.println("Name       : " + holderName);
            System.out.printf("Balance    : Rs.%.2f%n", balance);
            System.out.println("-----------------------------");
        }

        void printHistory() {
            System.out.println("\n--- Transaction History for " + accountNumber + " ---");
            for (String t : transactionHistory) {
                System.out.println("  " + t);
            }
        }
    }

    static Map<String, Account> accounts = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val <= 0) {
                    System.out.println("  Amount must be greater than zero.");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Enter a valid amount.");
            }
        }
    }

    static void createAccount() {
        System.out.println("\n--- Create Account ---");
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine().trim();
        if (accNo.isEmpty()) {
            System.out.println("  Account number cannot be empty.");
            return;
        }
        if (accounts.containsKey(accNo)) {
            System.out.println("  Account number already exists.");
            return;
        }
        System.out.print("Enter Account Holder Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("  Name cannot be empty.");
            return;
        }
        double initialBalance = readPositiveDouble("Enter Initial Deposit (Rs.): ");
        accounts.put(accNo, new Account(accNo, name, initialBalance));
        System.out.println("  Account created successfully.");
    }

    static Account getAccount(String prompt) {
        System.out.print(prompt);
        String accNo = sc.nextLine().trim();
        Account acc = accounts.get(accNo);
        if (acc == null) {
            System.out.println("  Account not found.");
        }
        return acc;
    }

    static void deposit() {
        System.out.println("\n--- Deposit ---");
        Account acc = getAccount("Enter Account Number: ");
        if (acc == null)
            return;
        double amount = readPositiveDouble("Enter deposit amount (Rs.): ");
        acc.deposit(amount);
    }

    static void withdraw() {
        System.out.println("\n--- Withdraw ---");
        Account acc = getAccount("Enter Account Number: ");
        if (acc == null)
            return;
        double amount = readPositiveDouble("Enter withdrawal amount (Rs.): ");
        acc.withdraw(amount);
    }

    static void viewAccount() {
        System.out.println("\n--- View Account ---");
        Account acc = getAccount("Enter Account Number: ");
        if (acc == null)
            return;
        acc.display();
    }

    static void transferMoney() {
        System.out.println("\n--- Transfer Money ---");
        Account from = getAccount("Enter Source Account Number: ");
        if (from == null)
            return;
        Account to = getAccount("Enter Destination Account Number: ");
        if (to == null)
            return;
        if (from.accountNumber.equals(to.accountNumber)) {
            System.out.println("  Cannot transfer to the same account.");
            return;
        }
        double amount = readPositiveDouble("Enter transfer amount (Rs.): ");
        if (from.balance < amount) {
            System.out.println("  Insufficient balance. Transfer failed.");
            return;
        }
        from.balance -= amount;
        from.transactionHistory
                .add("Transferred to " + to.accountNumber + ": Rs." + amount + " | Balance: Rs." + from.balance);
        to.balance += amount;
        to.transactionHistory
                .add("Received from " + from.accountNumber + ": Rs." + amount + " | Balance: Rs." + to.balance);
        System.out.printf("  Rs.%.2f transferred from %s to %s successfully.%n", amount, from.accountNumber,
                to.accountNumber);
    }

    static void viewHistory() {
        System.out.println("\n--- Transaction History ---");
        Account acc = getAccount("Enter Account Number: ");
        if (acc == null)
            return;
        acc.printHistory();
    }

    static void displayAll() {
        System.out.println("\n--- All Accounts ---");
        if (accounts.isEmpty()) {
            System.out.println("  No accounts found.");
            return;
        }
        for (Account acc : accounts.values())
            acc.display();
    }

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("   Bank Account Simulation    ");
        System.out.println("==============================");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Account Details");
            System.out.println("5. Transfer Money");
            System.out.println("6. View Transaction History");
            System.out.println("7. Display All Accounts");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            String input = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Enter a number from the menu.");
                continue;
            }
            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> viewAccount();
                case 5 -> transferMoney();
                case 6 -> viewHistory();
                case 7 -> displayAll();
                case 8 -> {
                    System.out.println("Exiting. Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice. Try again.");
            }
        }
    }
}