import java.util.*;

class ATMAccount {
    private String accountHolder;
    private String pin;
    private double balance;
    private double dailyWithdrawalLimit;
    private double dailyWithdrawn;
    private ArrayList<String> transactionHistory;
    private Calendar lastWithdrawalDate;

    public ATMAccount(String accountHolder, String pin, double initialBalance, double dailyLimit) {
        this.accountHolder = accountHolder;
        this.pin = pin;
        this.balance = initialBalance;
        this.dailyWithdrawalLimit = dailyLimit;
        this.dailyWithdrawn = 0;
        this.transactionHistory = new ArrayList<>();
        this.lastWithdrawalDate = Calendar.getInstance();
        transactionHistory.add("Account Created - Balance: " + this.balance);
    }

    public boolean verifyPIN(String enteredPIN) {
        return this.pin.equals(enteredPIN);
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        resetDailyLimitIfNeeded();
        if (amount <= 0) {
            System.out.println("❌ Invalid amount! Amount must be greater than 0.");
            return false;
        }
        if (amount > balance) {
            System.out.println("❌ Insufficient funds! Available balance: " + balance);
            return false;
        }
        if (dailyWithdrawn + amount > dailyWithdrawalLimit) {
            System.out.println("❌ Daily withdrawal limit exceeded!");
            System.out.println("   Daily limit: " + dailyWithdrawalLimit + " | Already withdrawn: " + dailyWithdrawn);
            System.out.println("   Remaining daily limit: " + (dailyWithdrawalLimit - dailyWithdrawn));
            return false;
        }

        balance -= amount;
        dailyWithdrawn += amount;
        transactionHistory.add("Withdrawal: -" + amount + " | Balance: " + balance);
        System.out.println("✓ Successfully withdrawn: " + amount);
        System.out.println("  Remaining balance: " + balance);
        return true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Invalid amount! Amount must be greater than 0.");
            return false;
        }
        balance += amount;
        transactionHistory.add("Deposit: +" + amount + " | Balance: " + balance);
        System.out.println("✓ Successfully deposited: " + amount);
        System.out.println("  Current balance: " + balance);
        return true;
    }

    public void checkBalance() {
        System.out.println("==========================================");
        System.out.println("         BALANCE INFORMATION");
        System.out.println("==========================================");
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Current Balance: Rs. " + balance);
        resetDailyLimitIfNeeded();
        System.out.println("Daily Withdrawal Limit: Rs. " + dailyWithdrawalLimit);
        System.out.println("Already Withdrawn Today: Rs. " + dailyWithdrawn);
        System.out.println("Remaining Daily Limit: Rs. " + (dailyWithdrawalLimit - dailyWithdrawn));
        System.out.println("==========================================");
    }

    public void miniStatement() {
        System.out.println("\n==========================================");
        System.out.println("         MINI STATEMENT");
        System.out.println("==========================================");
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Statement Period: Last 10 Transactions");
        System.out.println("------------------------------------------");
        int start = Math.max(0, transactionHistory.size() - 10);
        for (int i = start; i < transactionHistory.size(); i++) {
            System.out.println((i + 1) + ". " + transactionHistory.get(i));
        }
        System.out.println("==========================================\n");
    }

    private void resetDailyLimitIfNeeded() {
        Calendar today = Calendar.getInstance();
        if (lastWithdrawalDate.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR) ||
                lastWithdrawalDate.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {
            dailyWithdrawn = 0;
            lastWithdrawalDate = today;
        }
    }
}

public class ATMSimulation {
    private static final int MAX_PIN_ATTEMPTS = 3;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     WELCOME TO ATM SIMULATION SYSTEM    ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Create sample account
        ATMAccount account = new ATMAccount("John Doe", "1234", 50000, 20000);

        // Authenticate user
        if (authenticateUser(account)) {
            displayMenu(account);
        } else {
            System.out.println("\n❌ Authentication failed! Maximum attempts exceeded.");
            System.out.println("🔒 Card has been blocked for security reasons.");
        }

        scanner.close();
    }

    private static boolean authenticateUser(ATMAccount account) {
        System.out.println("🔐 Authentication Required");
        System.out.println("------------------------");
        for (int attempt = 1; attempt <= MAX_PIN_ATTEMPTS; attempt++) {
            System.out.print("Enter your PIN (Attempt " + attempt + "/" + MAX_PIN_ATTEMPTS + "): ");
            String pin = scanner.nextLine();

            if (account.verifyPIN(pin)) {
                System.out.println("✓ Authentication successful!\n");
                return true;
            } else {
                System.out.println("❌ Invalid PIN! Attempts remaining: " + (MAX_PIN_ATTEMPTS - attempt));
            }
        }
        return false;
    }

    private static void displayMenu(ATMAccount account) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          ATM MAIN MENU                  ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 1. Check Balance                        ║");
            System.out.println("║ 2. Withdraw Cash                        ║");
            System.out.println("║ 3. Deposit Cash                         ║");
            System.out.println("║ 4. Mini Statement                       ║");
            System.out.println("║ 5. Exit                                 ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Select an option (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    account.checkBalance();
                    break;
                case "2":
                    withdrawCash(account);
                    break;
                case "3":
                    depositCash(account);
                    break;
                case "4":
                    account.miniStatement();
                    break;
                case "5":
                    System.out.println("\n✓ Thank you for using ATM. Have a great day!");
                    exit = true;
                    break;
                default:
                    System.out.println("❌ Invalid option! Please select from 1-5.");
            }
        }
    }

    private static void withdrawCash(ATMAccount account) {
        System.out.print("\nEnter withdrawal amount: Rs. ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            account.withdraw(amount);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount entered!");
        }
    }

    private static void depositCash(ATMAccount account) {
        System.out.print("\nEnter deposit amount: Rs. ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            account.deposit(amount);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount entered!");
        }
    }
}
