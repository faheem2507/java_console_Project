import java.util.*;

public class StudentManagement {

    // Inner class to represent a student
    static class Student {
        int id;
        String name;
        double[] marks;
        int numSubjects;

        Student(int id, String name, double[] marks) {
            this.id = id;
            this.name = name;
            this.marks = marks;
            this.numSubjects = marks.length;
        }

        double getAverage() {
            double sum = 0;
            for (double m : marks) sum += m;
            return sum / numSubjects;
        }

        String getGrade() {
            double avg = getAverage();
            if (avg >= 90) return "A+";
            else if (avg >= 80) return "A";
            else if (avg >= 70) return "B";
            else if (avg >= 60) return "C";
            else if (avg >= 50) return "D";
            else return "F";
        }

        void display() {
            System.out.println("-----------------------------");
            System.out.println("ID      : " + id);
            System.out.println("Name    : " + name);
            System.out.print("Marks   : ");
            for (int i = 0; i < numSubjects; i++) {
                System.out.print("Sub" + (i + 1) + "=" + marks[i] + " ");
            }
            System.out.println();
            System.out.printf("Average : %.2f%n", getAverage());
            System.out.println("Grade   : " + getGrade());
            System.out.println("-----------------------------");
        }
    }

    static List<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // Reads a valid integer from user
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a whole number.");
            }
        }
    }

    // Reads a valid double from user
    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val < 0 || val > 100) {
                    System.out.println("  Marks must be between 0 and 100.");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a number.");
            }
        }
    }

    // Checks if ID already exists
    static boolean idExists(int id) {
        for (Student s : students) {
            if (s.id == id) return true;
        }
        return false;
    }

    // Finds student by ID; returns null if not found
    static Student findById(int id) {
        for (Student s : students) {
            if (s.id == id) return s;
        }
        return null;
    }

    static void addStudent() {
        System.out.println("\n--- Add New Student ---");
        int id;
        while (true) {
            id = readInt("Enter Student ID: ");
            if (idExists(id)) {
                System.out.println("  ID already exists. Try a different one.");
            } else {
                break;
            }
        }
        System.out.print("Enter Student Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("  Name cannot be empty. Student not added.");
            return;
        }
        int numSub = readInt("Enter number of subjects: ");
        if (numSub <= 0) {
            System.out.println("  Number of subjects must be at least 1.");
            return;
        }
        double[] marks = new double[numSub];
        for (int i = 0; i < numSub; i++) {
            marks[i] = readDouble("  Enter marks for Subject " + (i + 1) + " (0-100): ");
        }
        students.add(new Student(id, name, marks));
        System.out.println("  Student added successfully.");
    }

    static void displayAll() {
        System.out.println("\n--- All Students ---");
        if (students.isEmpty()) {
            System.out.println("  No student records found.");
            return;
        }
        for (Student s : students) s.display();
    }

    static void searchById() {
        System.out.println("\n--- Search Student ---");
        int id = readInt("Enter Student ID to search: ");
        Student s = findById(id);
        if (s == null) {
            System.out.println("  Student with ID " + id + " not found.");
        } else {
            s.display();
        }
    }

    static void updateStudent() {
        System.out.println("\n--- Update Student ---");
        int id = readInt("Enter Student ID to update: ");
        Student s = findById(id);
        if (s == null) {
            System.out.println("  Student not found.");
            return;
        }
        System.out.println("  Current record:");
        s.display();
        System.out.println("  What to update?");
        System.out.println("  1. Name");
        System.out.println("  2. Marks");
        int choice = readInt("  Choice: ");
        if (choice == 1) {
            System.out.print("  Enter new name: ");
            String newName = sc.nextLine().trim();
            if (newName.isEmpty()) {
                System.out.println("  Name cannot be empty. Update cancelled.");
            } else {
                s.name = newName;
                System.out.println("  Name updated.");
            }
        } else if (choice == 2) {
            for (int i = 0; i < s.numSubjects; i++) {
                s.marks[i] = readDouble("  Enter new marks for Subject " + (i + 1) + ": ");
            }
            System.out.println("  Marks updated.");
        } else {
            System.out.println("  Invalid choice.");
        }
    }

    static void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        int id = readInt("Enter Student ID to delete: ");
        Student s = findById(id);
        if (s == null) {
            System.out.println("  Student not found.");
            return;
        }
        System.out.print("  Confirm delete '" + s.name + "'? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) {
            students.remove(s);
            System.out.println("  Student deleted.");
        } else {
            System.out.println("  Delete cancelled.");
        }
    }

    public static void main(String[] args) {
        System.out.println("=============================");
        System.out.println("  Student Management System  ");
        System.out.println("=============================");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayAll();
                case 3 -> searchById();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> {
                    System.out.println("Exiting. Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice. Try again.");
            }
        }
    }
}