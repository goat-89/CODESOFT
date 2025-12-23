import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task2 {
    private static final int MAX_SUBJECTS = 10;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Student Grade Calculator ===");
        System.out.print("Enter number of subjects (max " + MAX_SUBJECTS + "): ");
        int numSubjects = getValidInt(scanner, 1, MAX_SUBJECTS);
        
        List<Integer> marks = new ArrayList<>();
        System.out.println("\nEnter marks obtained (out of 100) for each subject:");
        
        for (int i = 0; i < numSubjects; i++) {
            System.out.print("Subject " + (i + 1) + ": ");
            int mark = getValidInt(scanner, 0, 100);
            marks.add(mark);
        }
        
        GradeResult result = calculateGrade(marks);
        
        displayResults(result, numSubjects);
        
        scanner.close();
    }
    
    private static int getValidInt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number between " + min + " and " + max + ": ");
            }
        }
    }
    
    private static GradeResult calculateGrade(List<Integer> marks) {
        int totalMarks = 0;
        for (int mark : marks) {
            totalMarks += mark;
        }
        
        int numSubjects = marks.size();
        double averagePercentage = (double) totalMarks / numSubjects;
        String grade = getGrade(averagePercentage);
        
        return new GradeResult(totalMarks, averagePercentage, grade, marks);
    }
    
    private static String getGrade(double percentage) {
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B+";
        else if (percentage >= 60) return "B";
        else if (percentage >= 50) return "C";
        else if (percentage >= 40) return "D";
        else return "F";
    }
    
    private static void displayResults(GradeResult result, int numSubjects) {
        System.out.println("\n=== RESULTS ===");
        System.out.printf("Total Marks: %d / %d\n", result.totalMarks, numSubjects * 100);
        System.out.printf("Average Percentage: %.2f%%\n", result.averagePercentage);
        System.out.println("Grade: " + result.grade);
        System.out.println("\nSubject-wise Marks:");
        for (int i = 0; i < result.marks.size(); i++) {
            System.out.printf("Subject %d: %d/100\n", (i + 1), result.marks.get(i));
        }
        
        System.out.println("\nGrade Scale:");
        System.out.println("A+ (90-100%) | A (80-89%) | B+ (70-79%)");
        System.out.println("B (60-69%) | C (50-59%) | D (40-49%) | F (<40%)");
    }
    
    private static class GradeResult {
        int totalMarks;
        double averagePercentage;
        String grade;
        List<Integer> marks;
        
        GradeResult(int totalMarks, double averagePercentage, String grade, List<Integer> marks) {
            this.totalMarks = totalMarks;
            this.averagePercentage = averagePercentage;
            this.grade = grade;
            this.marks = marks;
        }
    }
}
