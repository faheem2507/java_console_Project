import java.util.*;

public class OnlineQuiz {

    static class Question {
        String questionText;
        String[] options; // 4 options
        int correctOption; // 1-based index

        Question(String questionText, String[] options, int correctOption) {
            this.questionText = questionText;
            this.options = options;
            this.correctOption = correctOption;
        }
    }

    static Scanner sc = new Scanner(System.in);

    // Hardcoded question bank
    static List<Question> questionBank = new ArrayList<>(Arrays.asList(
            new Question("What is the size of int in Java?",
                    new String[] { "2 bytes", "4 bytes", "8 bytes", "Depends on OS" }, 2),
            new Question("Which keyword is used to inherit a class in Java?",
                    new String[] { "implements", "extends", "inherits", "super" }, 2),
            new Question("What does JVM stand for?",
                    new String[] { "Java Virtual Memory", "Java Visual Machine", "Java Virtual Machine",
                            "Java Variable Method" },
                    3),
            new Question("Which of these is NOT a primitive type in Java?",
                    new String[] { "int", "char", "String", "boolean" }, 3),
            new Question("What is the default value of a boolean variable in Java?",
                    new String[] { "true", "false", "0", "null" }, 2),
            new Question("Which collection does NOT allow duplicates?",
                    new String[] { "ArrayList", "LinkedList", "HashSet", "Vector" }, 3),
            new Question("What is the output of 5 % 2 in Java?",
                    new String[] { "2", "0", "1", "2.5" }, 3),
            new Question("Which access modifier allows access only within the class?",
                    new String[] { "public", "protected", "default", "private" }, 4),
            new Question("What is the parent class of all Java classes?",
                    new String[] { "Class", "Base", "Object", "Super" }, 3),
            new Question("Which loop is guaranteed to execute at least once?",
                    new String[] { "for", "while", "do-while", "foreach" }, 3)));

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Enter a number.");
            }
        }
    }

    static void startQuiz() {
        System.out.println("\n--- Quiz Started ---");
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty())
            name = "User";

        int score = 0;
        int[] userAnswers = new int[questionBank.size()];

        for (int i = 0; i < questionBank.size(); i++) {
            Question q = questionBank.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.questionText);
            for (int j = 0; j < q.options.length; j++) {
                System.out.println("  " + (j + 1) + ". " + q.options[j]);
            }

            int answer;
            // Timer: 20 seconds per question
            long startTime = System.currentTimeMillis();
            while (true) {
                System.out.print("  Your answer (1-4): ");
                try {
                    answer = Integer.parseInt(sc.nextLine().trim());
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    if (elapsed > 20) {
                        System.out.println("  Time up! Moving to next question.");
                        answer = 0;
                        break;
                    }
                    if (answer < 1 || answer > 4) {
                        System.out.println("  Enter a number between 1 and 4.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input.");
                    answer = 0;
                }
            }

            userAnswers[i] = answer;
            if (answer == q.correctOption) {
                System.out.println("  Correct!");
                score++;
            } else {
                System.out.println("  Wrong.");
            }
        }

        // Results
        System.out.println("\n==============================");
        System.out.println("  Quiz Complete! Results for: " + name);
        System.out.println("==============================");
        System.out.println("Score: " + score + " / " + questionBank.size());
        double percent = (score * 100.0) / questionBank.size();
        System.out.printf("Percentage: %.1f%%%n", percent);
        if (percent >= 80)
            System.out.println("Grade: Excellent");
        else if (percent >= 60)
            System.out.println("Grade: Good");
        else if (percent >= 40)
            System.out.println("Grade: Average");
        else
            System.out.println("Grade: Needs Improvement");

        // Show correct answers
        System.out.println("\n--- Answer Review ---");
        for (int i = 0; i < questionBank.size(); i++) {
            Question q = questionBank.get(i);
            System.out.println("Q" + (i + 1) + ": " + q.questionText);
            System.out.println("  Your answer    : "
                    + (userAnswers[i] == 0 ? "Not answered" : userAnswers[i] + ". " + q.options[userAnswers[i] - 1]));
            System.out.println("  Correct answer : " + q.correctOption + ". " + q.options[q.correctOption - 1]);
            System.out.println("  Result         : " + (userAnswers[i] == q.correctOption ? "Correct" : "Wrong"));
        }
    }

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("     Online Quiz Application  ");
        System.out.println("==============================");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Start Quiz");
            System.out.println("2. Exit");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> startQuiz();
                case 2 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice.");
            }
        }
    }
}