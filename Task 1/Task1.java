import java.util.Random;
import java.util.Scanner;

public class Task1 {
    private static final int MAX_ATTEMPTS = 10;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 100;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int totalScore = 0;
        int roundsPlayed = 0;
        
        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("Guess a number between " + MIN_NUMBER + " and " + MAX_NUMBER + "!");
        
        while (true) {
            roundsPlayed++;
            int attempts = playRound(random, scanner);
            int score = Math.max(0, MAX_ATTEMPTS + 1 - attempts);
            totalScore += score;
            
            System.out.println("Your score for this round: " + score);
            System.out.println("Total score after " + roundsPlayed + " rounds: " + totalScore);
            
            System.out.print("Do you want to play another round? (yes/no): ");
            String playAgain = scanner.nextLine().trim().toLowerCase();
            
            if (!playAgain.equals("yes")) {
                System.out.println("Thanks for playing!");
                break;
            }
            System.out.println();
        }
        scanner.close();
    }
    
    private static int playRound(Random random, Scanner scanner) {
        int numberToGuess = random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
        int attempts = 0;
        
        System.out.println("I have generated a number between " + MIN_NUMBER + " and " + MAX_NUMBER);
        System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess it.\n");
        
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.print("Attempt " + (attempts + 1) + ": Enter your guess: ");
                int guess = Integer.parseInt(scanner.nextLine().trim());
                attempts++;
                
                if (guess == numberToGuess) {
                    System.out.println("Correct! You guessed the number in " + attempts + " attempts.");
                    return attempts;
                } else if (guess < numberToGuess) {
                    System.out.println("Too low!");
                } else {
                    System.out.println("Too high!");
                }
                
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
                attempts--; 
            }
        }
        
        System.out.println("Sorry, you failed to guess the number. It was " + numberToGuess + ".");
        return attempts;
    }
}
