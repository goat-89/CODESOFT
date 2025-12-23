import java.util.ArrayList;
import java.util.Scanner;

public class ATMSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Create bank account
        BankAccount account = new BankAccount(5000.0); // Starting balance
        
        // Create ATM
        ATM atm = new ATM(account);
        
        System.out.println("=== Welcome to ATM System ===");
        atm.displayMenu(scanner);
        
        scanner.close();
    }
}

class BankAccount {
    private double balance;
    private ArrayList<String> transactions;
    
    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        transactions.add("Initial balance: Rs " + String.format("%.2f", balance));
    }
    
    public double getBalance() {
        return balance;
    }
    
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposited: Rs " + String.format("%.2f", amount) + 
                           " | New Balance: Rs " + String.format("%.2f", balance));
            return true;
        }
        return false;
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactions.add("Withdrew: Rs " + String.format("%.2f", amount) + 
                           " | New Balance: Rs " + String.format("%.2f", balance));
            return true;
        }
        return false;
    }
    
    public void printTransactionHistory() {
        System.out.println("\n=== Transaction History (Last 10) ===");
        int startIndex = Math.max(0, transactions.size() - 10);
        for (int i = startIndex; i < transactions.size(); i++) {
            System.out.println((i - startIndex + 1) + ". " + transactions.get(i));
        }
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        }
    }
}

class ATM {
    private BankAccount account;
    private static final double MIN_WITHDRAWAL = 100.0;
    private static final double MAX_WITHDRAWAL = 50000.0;
    private static final double MIN_DEPOSIT = 50.0;
    
    public ATM(BankAccount account) {
        this.account = account;
    }
    
    public void displayMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== ATM MENU ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");
            
            int choice = getValidInt(scanner, 1, 5);
            
            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    handleDeposit(scanner);
                    break;
                case 3:
                    handleWithdraw(scanner);
                    break;
                case 4:
                    account.printTransactionHistory();
                    break;
                case 5:
                    System.out.println("Thank you for using ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private void checkBalance() {
        double balance = account.getBalance();
        System.out.println("Current Balance: Rs " + String.format("%.2f", balance));
    }
    
    private void handleDeposit(Scanner scanner) {
        System.out.print("Enter deposit amount (min Rs " + MIN_DEPOSIT + "): Rs ");
        double amount = getValidDouble(scanner, MIN_DEPOSIT, Double.MAX_VALUE);
        
        if (account.deposit(amount)) {
            System.out.println("Rs " + String.format("%.2f", amount) + " deposited successfully!");
        } else {
            System.out.println("Deposit failed! Invalid amount.");
        }
    }
    
    private void handleWithdraw(Scanner scanner) {
        double balance = account.getBalance();
        System.out.println("Current Balance: Rs " + String.format("%.2f", balance));
        System.out.print("Enter withdrawal amount (Rs " + MIN_WITHDRAWAL + " - Rs " + 
                         Math.min(MAX_WITHDRAWAL, balance) + "): Rs ");
        
        double amount = getValidDouble(scanner, MIN_WITHDRAWAL, Math.min(MAX_WITHDRAWAL, balance));
        
        if (account.withdraw(amount)) {
            System.out.println("Rs " + String.format("%.2f", amount) + " withdrawn successfully!");
        } else {
            System.out.println("Withdrawal failed! Insufficient balance.");
        }
    }
    
    private int getValidInt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number between " + min + " and " + max + ": ");
            }
        }
    }
    
    private double getValidDouble(Scanner scanner, double min, double max) {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.print("Please enter an amount between " + String.format("%.2f", min) + 
                               " and " + String.format("%.2f", max) + ": Rs ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid amount between " + String.format("%.2f", min) + 
                               " and " + String.format("%.2f", max) + ": Rs ");
            }
        }
    }
}
