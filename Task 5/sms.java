import java.io.*;
import java.util.*;

public class sms {
    private static List<Student> students = new ArrayList<>();
    private static final String DATA_FILE = "students.txt";
    
    public static void main(String[] args) {
        loadStudentsFromFile();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Student Management System (CodSoft Task 5) ===");
        
        while (true) {
            displayMenu();
            int choice = getValidInt(scanner, "Enter your choice (1-6): ", 1, 6);
            
            switch (choice) {
                case 1 -> addStudent(scanner);
                case 2 -> editStudent(scanner);
                case 3 -> searchStudent(scanner);
                case 4 -> displayAllStudents();
                case 5 -> removeStudent(scanner);
                case 6 -> {
                    saveStudentsToFile();
                    System.out.println("Thank you for using Student Management System!");
                    scanner.close();
                    return;
                }
            }
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n1. Add New Student");
        System.out.println("2. Edit Student");
        System.out.println("3. Search Student");
        System.out.println("4. Display All Students");
        System.out.println("5. Remove Student");
        System.out.println("6. Exit & Save");
    }
    
    private static void addStudent(Scanner scanner) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        if (!isValidName(name)) {
            System.out.println("❌ Invalid name! Must be 2-50 characters.");
            return;
        }
        
        String rollNo = getValidRollNo(scanner);
        if (rollNo == null) return;
        
        String grade = getValidGrade(scanner);
        if (grade == null) return;
        
        Student student = new Student(name, rollNo, grade);
        students.add(student);
        System.out.println("✅ Student added successfully!");
    }
    
    private static void editStudent(Scanner scanner) {
        String rollNo = getValidRollNo(scanner, "Enter roll number to edit: ");
        if (rollNo == null) return;
        
        Student student = findStudentByRollNo(rollNo);
        if (student == null) {
            System.out.println("❌ Student not found!");
            return;
        }
        
        System.out.print("Enter new name (current: " + student.getName() + "): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty() && isValidName(newName)) {
            student.setName(newName);
        }
        
        System.out.print("Enter new grade (current: " + student.getGrade() + "): ");
        String newGrade = scanner.nextLine().trim().toUpperCase();
        if (!newGrade.isEmpty() && isValidGrade(newGrade)) {
            student.setGrade(newGrade);
        }
        
        System.out.println("✅ Student updated successfully!");
    }
    
    private static void searchStudent(Scanner scanner) {
        System.out.print("Enter roll number or name to search: ");
        String searchTerm = scanner.nextLine().trim();
        
        List<Student> found = new ArrayList<>();
        for (Student s : students) {
            if (s.getRollNo().equalsIgnoreCase(searchTerm) || 
                s.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                found.add(s);
            }
        }
        
        if (found.isEmpty()) {
            System.out.println("❌ No students found!");
        } else {
            System.out.println("\nFound " + found.size() + " student(s):");
            for (Student s : found) {
                s.displayInfo();
            }
        }
    }
    
    private static void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }
        
        System.out.println("\n=== All Students (" + students.size() + ") ===");
        System.out.printf("%-5s %-20s %-10s %-5s%n", "ID", "NAME", "ROLL NO", "GRADE");
        System.out.println("-".repeat(45));
        
        int index = 1;
        for (Student s : students) {
            System.out.printf("%-5d %-20s %-10s %-5s%n", 
                index++, s.getName(), s.getRollNo(), s.getGrade());
        }
    }
    
    private static void removeStudent(Scanner scanner) {
        String rollNo = getValidRollNo(scanner, "Enter roll number to remove: ");
        if (rollNo == null) return;
        
        Student student = findStudentByRollNo(rollNo);
        if (student == null) {
            System.out.println("❌ Student not found!");
            return;
        }
        
        System.out.print("Are you sure? (y/n): ");
        if (scanner.nextLine().trim().toLowerCase().equals("y")) {
            students.remove(student);
            System.out.println("✅ Student removed successfully!");
        }
    }
    
    private static String getValidRollNo(Scanner scanner) {
        return getValidRollNo(scanner, "Enter roll number: ");
    }
    
    private static String getValidRollNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String rollNo = scanner.nextLine().trim();
            if (isValidRollNo(rollNo) && !rollNoExists(rollNo)) {
                return rollNo;
            }
            if (!isValidRollNo(rollNo)) {
                System.out.println("❌ Invalid roll number! Use format: 23CSE001");
            } else {
                System.out.println("❌ Roll number already exists!");
            }
        }
    }
    
    private static String getValidGrade(Scanner scanner) {
        while (true) {
            System.out.print("Enter grade (A, B, C, D, F): ");
            String grade = scanner.nextLine().trim().toUpperCase();
            if (isValidGrade(grade)) {
                return grade;
            }
            System.out.println("❌ Invalid grade! Use A, B, C, D, F");
        }
    }
    
    private static boolean isValidName(String name) {
        return name.length() >= 2 && name.length() <= 50 && name.matches("[a-zA-Z\\s]+");
    }
    
    private static boolean isValidRollNo(String rollNo) {
        return rollNo.matches("^[0-9]{2}[A-Z]{3}[0-9]{3}$");
    }
    
    private static boolean isValidGrade(String grade) {
        return grade.matches("[ABCD F]");
    }
    
    private static boolean rollNoExists(String rollNo) {
        return students.stream().anyMatch(s -> s.getRollNo().equalsIgnoreCase(rollNo));
    }
    
    private static Student findStudentByRollNo(String rollNo) {
        return students.stream()
            .filter(s -> s.getRollNo().equalsIgnoreCase(rollNo))
            .findFirst().orElse(null);
    }
    
    private static void saveStudentsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student student : students) {
                writer.println(student.getName() + "|" + student.getRollNo() + "|" + student.getGrade());
            }
            System.out.println("💾 Data saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }
    
    private static void loadStudentsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    Student student = new Student(parts[0], parts[1], parts[2]);
                    students.add(student);
                }
            }
            System.out.println("📂 Loaded " + students.size() + " students from file");
        } catch (FileNotFoundException e) {
            System.out.println("📄 No existing data file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("❌ Error loading data: " + e.getMessage());
        }
    }
    
    private static int getValidInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    static class Student {
        private String name;
        private String rollNo;
        private String grade;
        private final Date enrollmentDate;
        
        public Student(String name, String rollNo, String grade) {
            this.name = name;
            this.rollNo = rollNo;
            this.grade = grade;
            this.enrollmentDate = new Date();
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRollNo() { return rollNo; }
        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }
        public Date getEnrollmentDate() { return enrollmentDate; }
        
        public void displayInfo() {
            System.out.printf("📋 Name: %s | Roll No: %s | Grade: %s | Joined: %s%n",
                name, rollNo, grade, enrollmentDate);
        }
    }
}
