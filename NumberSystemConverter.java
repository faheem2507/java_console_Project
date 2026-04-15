import java.util.*;

public class NumberSystemConverter {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   NUMBER SYSTEM CONVERSION UTILITY      ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Select an option (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    convertDecimalToBinary();
                    break;
                case "2":
                    convertDecimalToOctal();
                    break;
                case "3":
                    convertDecimalToHexadecimal();
                    break;
                case "4":
                    convertBinaryToDecimal();
                    break;
                case "5":
                    convertOctalToDecimal();
                    break;
                case "6":
                    convertHexadecimalToDecimal();
                    break;
                case "7":
                    convertBetweenAllSystems();
                    break;
                case "8":
                    System.out.println("\n✓ Thank you for using Number System Converter. Goodbye!");
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
        System.out.println("║ 1. Decimal to Binary                    ║");
        System.out.println("║ 2. Decimal to Octal                     ║");
        System.out.println("║ 3. Decimal to Hexadecimal               ║");
        System.out.println("║ 4. Binary to Decimal                    ║");
        System.out.println("║ 5. Octal to Decimal                     ║");
        System.out.println("║ 6. Hexadecimal to Decimal               ║");
        System.out.println("║ 7. Convert Between All Systems          ║");
        System.out.println("║ 8. Exit                                 ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void convertDecimalToBinary() {
        System.out.println();
        System.out.print("Enter decimal number: ");
        try {
            long decimal = Long.parseLong(scanner.nextLine().trim());
            if (decimal < 0) {
                System.out.println("❌ Please enter a non-negative number!\n");
                return;
            }
            String binary = Long.toBinaryString(decimal);
            displayResult("Decimal to Binary", decimal, binary);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid decimal number.\n");
        }
    }

    private static void convertDecimalToOctal() {
        System.out.println();
        System.out.print("Enter decimal number: ");
        try {
            long decimal = Long.parseLong(scanner.nextLine().trim());
            if (decimal < 0) {
                System.out.println("❌ Please enter a non-negative number!\n");
                return;
            }
            String octal = Long.toOctalString(decimal);
            displayResult("Decimal to Octal", decimal, octal);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid decimal number.\n");
        }
    }

    private static void convertDecimalToHexadecimal() {
        System.out.println();
        System.out.print("Enter decimal number: ");
        try {
            long decimal = Long.parseLong(scanner.nextLine().trim());
            if (decimal < 0) {
                System.out.println("❌ Please enter a non-negative number!\n");
                return;
            }
            String hexadecimal = Long.toHexString(decimal).toUpperCase();
            displayResult("Decimal to Hexadecimal", decimal, hexadecimal);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid decimal number.\n");
        }
    }

    private static void convertBinaryToDecimal() {
        System.out.println();
        System.out.print("Enter binary number: ");
        String input = scanner.nextLine().trim();
        try {
            if (!input.matches("[01]+")) {
                System.out.println("❌ Invalid binary format! Use only 0 and 1.\n");
                return;
            }
            long decimal = Long.parseLong(input, 2);
            displayResult("Binary to Decimal", input, Long.toString(decimal));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid binary number!\n");
        }
    }

    private static void convertOctalToDecimal() {
        System.out.println();
        System.out.print("Enter octal number: ");
        String input = scanner.nextLine().trim();
        try {
            if (!input.matches("[0-7]+")) {
                System.out.println("❌ Invalid octal format! Use only 0-7.\n");
                return;
            }
            long decimal = Long.parseLong(input, 8);
            displayResult("Octal to Decimal", input, Long.toString(decimal));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid octal number!\n");
        }
    }

    private static void convertHexadecimalToDecimal() {
        System.out.println();
        System.out.print("Enter hexadecimal number: ");
        String input = scanner.nextLine().trim().toUpperCase();
        try {
            if (!input.matches("[0-9A-F]+")) {
                System.out.println("❌ Invalid hexadecimal format! Use 0-9 and A-F.\n");
                return;
            }
            long decimal = Long.parseLong(input, 16);
            displayResult("Hexadecimal to Decimal", input, Long.toString(decimal));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid hexadecimal number!\n");
        }
    }

    private static void convertBetweenAllSystems() {
        System.out.println("\n🔄 MULTI-SYSTEM CONVERSION");
        System.out.println("───────────────────────────");
        System.out.print("Enter a decimal number: ");

        try {
            long decimal = Long.parseLong(scanner.nextLine().trim());
            if (decimal < 0) {
                System.out.println("❌ Please enter a non-negative number!\n");
                return;
            }

            String binary = Long.toBinaryString(decimal);
            String octal = Long.toOctalString(decimal);
            String hexadecimal = Long.toHexString(decimal).toUpperCase();

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║      NUMBER SYSTEM CONVERSION           ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.printf("║ Decimal:      %-30s║\n", decimal);
            System.out.printf("║ Binary:       %-30s║\n", binary);
            System.out.printf("║ Octal:        %-30s║\n", octal);
            System.out.printf("║ Hexadecimal:  %-30s║\n", hexadecimal);
            System.out.println("╚════════════════════════════════════════╝\n");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid decimal number.\n");
        }
    }

    private static void displayResult(String operation, Object from, Object to) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          CONVERSION RESULT              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Operation: %-28s║\n", operation);
        System.out.print("║ From: ");
        System.out.printf("%-35s║\n", String.valueOf(from));
        System.out.print("║ To:   ");
        System.out.printf("%-35s║\n", String.valueOf(to));
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
