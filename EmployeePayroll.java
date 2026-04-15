import java.util.*;

public class EmployeePayroll {

    // Abstract base class - OOP inheritance
    static abstract class Employee {
        int id;
        String name;
        double baseSalary;

        Employee(int id, String name, double baseSalary) {
            this.id = id;
            this.name = name;
            this.baseSalary = baseSalary;
        }

        // Each subclass defines its own allowance
        abstract double getAllowance();

        String getDesignation() {
            return "Employee";
        }

        double getGrossSalary() {
            return baseSalary + getAllowance();
        }

        // Tax slabs based on annual gross
        double calculateTax() {
            double annual = getGrossSalary() * 12;
            double tax = 0;
            if (annual <= 250000)
                tax = 0;
            else if (annual <= 500000)
                tax = (annual - 250000) * 0.05;
            else if (annual <= 1000000)
                tax = 12500 + (annual - 500000) * 0.20;
            else
                tax = 112500 + (annual - 1000000) * 0.30;
            return tax / 12; // Monthly tax
        }

        double getNetSalary() {
            return getGrossSalary() - calculateTax();
        }

        void generateSlip() {
            System.out.println("\n========================================");
            System.out.println("             SALARY SLIP                ");
            System.out.println("========================================");
            System.out.println("Employee ID  : " + id);
            System.out.println("Name         : " + name);
            System.out.println("Designation  : " + getDesignation());
            System.out.println("----------------------------------------");
            System.out.printf("Base Salary  : Rs.%10.2f%n", baseSalary);
            System.out.printf("Allowances   : Rs.%10.2f%n", getAllowance());
            System.out.printf("Gross Salary : Rs.%10.2f%n", getGrossSalary());
            System.out.println("----------------------------------------");
            System.out.printf("Tax Deducted : Rs.%10.2f%n", calculateTax());
            System.out.println("----------------------------------------");
            System.out.printf("Net Salary   : Rs.%10.2f%n", getNetSalary());
            System.out.println("========================================");
        }
    }

    // Manager: gets HRA 20% + Travel 10% + Managerial 15%
    static class Manager extends Employee {
        Manager(int id, String name, double baseSalary) {
            super(id, name, baseSalary);
        }

        @Override
        double getAllowance() {
            return baseSalary * 0.20 // HRA
                    + baseSalary * 0.10 // Travel
                    + baseSalary * 0.15; // Managerial
        }

        @Override
        String getDesignation() {
            return "Manager";
        }
    }

    // Developer: gets HRA 15% + Travel 5% + Tech 10%
    static class Developer extends Employee {
        Developer(int id, String name, double baseSalary) {
            super(id, name, baseSalary);
        }

        @Override
        double getAllowance() {
            return baseSalary * 0.15 // HRA
                    + baseSalary * 0.05 // Travel
                    + baseSalary * 0.10; // Technical
        }

        @Override
        String getDesignation() {
            return "Developer";
        }
    }

    static List<Employee> employees = new ArrayList<>();
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
        for (Employee e : employees)
            if (e.id == id)
                return true;
        return false;
    }

    static Employee findById(int id) {
        for (Employee e : employees)
            if (e.id == id)
                return e;
        return null;
    }

    static void addEmployee() {
        System.out.println("\n--- Add Employee ---");
        int id;
        while (true) {
            id = readInt("Enter Employee ID: ");
            if (idExists(id))
                System.out.println("  ID already exists.");
            else
                break;
        }
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("  Name cannot be empty.");
            return;
        }
        double salary = readPositiveDouble("Enter Base Salary (Rs.): ");
        System.out.println("Employee Type: 1. Manager  2. Developer");
        int type = readInt("Enter type: ");
        if (type == 1)
            employees.add(new Manager(id, name, salary));
        else if (type == 2)
            employees.add(new Developer(id, name, salary));
        else {
            System.out.println("  Invalid type.");
            return;
        }
        System.out.println("  Employee added successfully.");
    }

    static void viewSlip() {
        System.out.println("\n--- Generate Salary Slip ---");
        int id = readInt("Enter Employee ID: ");
        Employee e = findById(id);
        if (e == null) {
            System.out.println("  Employee not found.");
            return;
        }
        e.generateSlip();
    }

    static void displayAll() {
        System.out.println("\n--- All Employees ---");
        if (employees.isEmpty()) {
            System.out.println("  No employees found.");
            return;
        }
        for (Employee e : employees) {
            System.out.println("ID: " + e.id + " | Name: " + e.name
                    + " | Type: " + e.getDesignation()
                    + " | Base: Rs." + e.baseSalary
                    + " | Net: Rs." + String.format("%.2f", e.getNetSalary()));
        }
    }

    static void deleteEmployee() {
        System.out.println("\n--- Delete Employee ---");
        int id = readInt("Enter Employee ID: ");
        Employee e = findById(id);
        if (e == null) {
            System.out.println("  Employee not found.");
            return;
        }
        employees.remove(e);
        System.out.println("  Employee removed.");
    }

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("   Employee Payroll System    ");
        System.out.println("==============================");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Employee");
            System.out.println("2. Generate Salary Slip");
            System.out.println("3. Display All Employees");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> viewSlip();
                case 3 -> displayAll();
                case 4 -> deleteEmployee();
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice.");
            }
        }
    }
}