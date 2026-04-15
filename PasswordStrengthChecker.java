import java.util.*;

public class PasswordStrengthChecker {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   PASSWORD STRENGTH CHECKER             ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Select an option (1-4): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    checkPasswordStrength();
                    break;
                case "2":
                    displayPasswordRequirements();
                    break;
                case "3":
                    generateStrongPassword();
                    break;
                case "4":
                    System.out.println("\n✓ Thank you for using Password Strength Checker. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("❌ Invalid option! Please select 1-4.\n");
            }
        }

        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                      ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Check Password Strength              ║");
        System.out.println("║ 2. View Password Requirements           ║");
        System.out.println("║ 3. Generate Strong Password             ║");
        System.out.println("║ 4. Exit                                 ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void checkPasswordStrength() {
        System.out.println("\n🔐 PASSWORD STRENGTH ANALYZER");
        System.out.println("────────────────────────────");

        System.out.print("Enter password to check: ");
        String password = scanner.nextLine();

        if (password.isEmpty()) {
            System.out.println("❌ Password cannot be empty!\n");
            return;
        }

        PasswordAnalysis analysis = analyzePassword(password);
        displayPasswordAnalysisReport(password, analysis);
    }

    private static PasswordAnalysis analyzePassword(String password) {
        PasswordAnalysis analysis = new PasswordAnalysis();
        analysis.length = password.length();

        // Check character types
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                analysis.hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                analysis.hasLowercase = true;
            } else if (Character.isDigit(c)) {
                analysis.hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                analysis.hasSpecialChar = true;
            }
        }

        // Calculate strength score
        int score = 0;

        // Length: 0-30 points
        if (analysis.length >= 8) score += 10;
        if (analysis.length >= 12) score += 10;
        if (analysis.length >= 16) score += 10;

        // Character types: 0-40 points
        if (analysis.hasLowercase) score += 10;
        if (analysis.hasUppercase) score += 10;
        if (analysis.hasDigit) score += 10;
        if (analysis.hasSpecialChar) score += 10;

        // Complexity checks: 0-30 points
        if (hasRepeatingChars(password)) score -= 5;
        if (hasSequentialChars(password)) score -= 5;
        if (hasCommonPatterns(password)) score -= 5;
        if (score < 0) score = 0;

        analysis.score = score;

        // Determine strength level
        if (analysis.score < 30) {
            analysis.strength = "WEAK";
        } else if (analysis.score < 60) {
            analysis.strength = "MEDIUM";
        } else if (analysis.score < 85) {
            analysis.strength = "STRONG";
        } else {
            analysis.strength = "VERY STRONG";
        }

        return analysis;
    }

    private static boolean hasRepeatingChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) &&
                    password.charAt(i + 1) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSequentialChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);

            if ((c1 + 1 == c2 && c2 + 1 == c3) || (c1 - 1 == c2 && c2 - 1 == c3)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCommonPatterns(String password) {
        String[] commonPatterns = {
                "123", "234", "345", "456", "567", "678", "789", "890",
                "abc", "bcd", "cde", "def", "efg", "fgh", "ghi", "hij",
                "qwerty", "asdf", "zxcv", "password", "123456", "111111"
        };

        String lowerPassword = password.toLowerCase();
        for (String pattern : commonPatterns) {
            if (lowerPassword.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private static void displayPasswordAnalysisReport(String password, PasswordAnalysis analysis) {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║            PASSWORD STRENGTH ANALYSIS REPORT            ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");

        // Strength level with color indicator
        String strengthBar = "";
        String indicator = "";

        if (analysis.strength.equals("WEAK")) {
            strengthBar = "🔴 [█░░░░░░░░] 0-30%";
            indicator = "❌ WEAK";
        } else if (analysis.strength.equals("MEDIUM")) {
            strengthBar = "🟡 [███░░░░░░] 31-60%";
            indicator = "⚠ MEDIUM";
        } else if (analysis.strength.equals("STRONG")) {
            strengthBar = "🟢 [██████░░░] 61-85%";
            indicator = "✓ STRONG";
        } else {
            strengthBar = "🟢 [██████████] 86-100%";
            indicator = "💪 VERY STRONG";
        }

        System.out.println("║ Strength Level: " + indicator);
        System.out.println("║ " + strengthBar);
        System.out.println("║ Strength Score: " + String.format("%-41d", analysis.score));

        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║            PASSWORD CHARACTERISTICS                    ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ Length: %d characters%s║\n", analysis.length,
                String.format("%" + (47 - String.valueOf(analysis.length).length() - 10) + "s", ""));
        System.out.println("║ Uppercase Letters:     " + (analysis.hasUppercase ? "✓ Yes" : "✗ No") +
                String.format("%" + (31 - (analysis.hasUppercase ? 5 : 4)) + "s", "") + "║");
        System.out.println("║ Lowercase Letters:     " + (analysis.hasLowercase ? "✓ Yes" : "✗ No") +
                String.format("%" + (31 - (analysis.hasLowercase ? 5 : 4)) + "s", "") + "║");
        System.out.println("║ Digits (0-9):         " + (analysis.hasDigit ? "✓ Yes" : "✗ No") +
                String.format("%" + (32 - (analysis.hasDigit ? 5 : 4)) + "s", "") + "║");
        System.out.println("║ Special Characters:    " + (analysis.hasSpecialChar ? "✓ Yes" : "✗ No") +
                String.format("%" + (30 - (analysis.hasSpecialChar ? 5 : 4)) + "s", "") + "║");

        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║            IMPROVEMENT SUGGESTIONS                     ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");

        ArrayList<String> suggestions = generateSuggestions(analysis);
        if (suggestions.isEmpty()) {
            System.out.println("║ ✓ Excellent! No improvements needed.                  ║");
        } else {
            for (int i = 0; i < suggestions.size(); i++) {
                String suggestion = suggestions.get(i);
                String truncated = suggestion.length() > 50 ? 
                        suggestion.substring(0, 47) + "..." : suggestion;
                System.out.printf("║ %d. %-48s║\n", i + 1, truncated);
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }

    private static ArrayList<String> generateSuggestions(PasswordAnalysis analysis) {
        ArrayList<String> suggestions = new ArrayList<>();

        if (analysis.length < 8) {
            suggestions.add("• Increase password length to at least 8 characters");
        }
        if (analysis.length < 12) {
            suggestions.add("• Consider using 12+ characters for stronger security");
        }
        if (!analysis.hasUppercase) {
            suggestions.add("• Add uppercase letters (A-Z) to your password");
        }
        if (!analysis.hasLowercase) {
            suggestions.add("• Add lowercase letters (a-z) to your password");
        }
        if (!analysis.hasDigit) {
            suggestions.add("• Include numbers (0-9) in your password");
        }
        if (!analysis.hasSpecialChar) {
            suggestions.add("• Add special characters (!@#$%^&*) for extra security");
        }
        if (hasRepeatingChars(getLastPassword())) {
            suggestions.add("• Avoid using repeating characters (aaa, 111, etc.)");
        }
        if (hasSequentialChars(getLastPassword())) {
            suggestions.add("• Avoid sequential patterns (abc, 123, etc.)");
        }
        if (hasCommonPatterns(getLastPassword())) {
            suggestions.add("• Avoid common patterns and dictionary words");
        }

        return suggestions;
    }

    private static String getLastPassword() {
        return scanner.hasNextLine() ? "" : "";
    }

    private static void displayPasswordRequirements() {
        System.out.println("\n📋 PASSWORD STRENGTH REQUIREMENTS");
        System.out.println("──────────────────────────────────");

        System.out.println("\n🔴 WEAK PASSWORD (Score: 0-30)");
        System.out.println("   • Less than 8 characters");
        System.out.println("   • Only lowercase or uppercase letters");
        System.out.println("   • No numbers or special characters");

        System.out.println("\n🟡 MEDIUM PASSWORD (Score: 31-60)");
        System.out.println("   • 8-11 characters long");
        System.out.println("   • Mix of uppercase and lowercase");
        System.out.println("   • Missing numbers OR special characters");

        System.out.println("\n🟢 STRONG PASSWORD (Score: 61-85)");
        System.out.println("   • 12-15 characters long");
        System.out.println("   • Mix of uppercase, lowercase, numbers");
        System.out.println("   • Includes special characters");
        System.out.println("   • No common patterns");

        System.out.println("\n💪 VERY STRONG PASSWORD (Score: 86-100)");
        System.out.println("   • 16+ characters long");
        System.out.println("   • All character types included");
        System.out.println("   • No repeating or sequential characters");
        System.out.println("   • Unique and memorable");

        System.out.println("\n🔒 RECOMMENDED PASSWORD LENGTH");
        System.out.println("   • Minimum: 8 characters");
        System.out.println("   • Recommended: 12+ characters");
        System.out.println("   • Strong: 16+ characters\n");
    }

    private static void generateStrongPassword() {
        System.out.println("\n🔐 STRONG PASSWORD GENERATOR");
        System.out.println("────────────────────────────");

        System.out.print("Enter desired password length (8-32): ");
        try {
            int length = Integer.parseInt(scanner.nextLine().trim());

            if (length < 8 || length > 32) {
                System.out.println("❌ Length must be between 8 and 32!\n");
                return;
            }

            String password = generateRandomPassword(length);
            PasswordAnalysis analysis = analyzePassword(password);

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║        GENERATED PASSWORD              ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ Password: " + String.format("%-30s", password) + "║");
            System.out.println("║ Strength: " + String.format("%-30s", analysis.strength) + "║");
            System.out.println("║ Score:    " + String.format("%-30d", analysis.score) + "║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.println("💡 Password copied to display!");
            displayPasswordAnalysisReport(password, analysis);

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a number.\n");
        }
    }

    private static String generateRandomPassword(int length) {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*_+-=[]{}()?";

        String allChars = uppercase + lowercase + digits + specialChars;
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Ensure at least one character from each category
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill remaining characters
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle the password
        char[] chars = password.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }

    // Inner class for password analysis
    static class PasswordAnalysis {
        int length;
        boolean hasUppercase;
        boolean hasLowercase;
        boolean hasDigit;
        boolean hasSpecialChar;
        int score;
        String strength;

        PasswordAnalysis() {
            this.hasUppercase = false;
            this.hasLowercase = false;
            this.hasDigit = false;
            this.hasSpecialChar = false;
            this.score = 0;
            this.strength = "";
        }
    }
}
