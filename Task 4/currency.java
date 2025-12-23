import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class currency {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Currency Converter (CodSoft Task 4) ===");
        
        while (true) {
            System.out.print("Enter base currency (e.g., USD, EUR, INR): ");
            String baseCurrency = scanner.nextLine().trim().toUpperCase();
            
            System.out.print("Enter target currency (e.g., USD, EUR, INR): ");
            String targetCurrency = scanner.nextLine().trim().toUpperCase();
            
            System.out.print("Enter amount to convert: ");
            double amount;
            try {
                amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount <= 0) {
                    System.out.println("Amount must be greater than 0!");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount!");
                continue;
            }

            try {
                double convertedAmount = convertCurrency(baseCurrency, targetCurrency, amount);
                displayResult(baseCurrency, targetCurrency, amount, convertedAmount);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please check your internet connection or try different currencies.");
            }
            
            System.out.print("\nConvert another amount? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (!choice.equals("y")) {
                break;
            }
            System.out.println();
        }
        
        System.out.println("Thank you for using Currency Converter!");
        scanner.close();
    }
    
    private static double convertCurrency(String base, String target, double amount) throws Exception {
        if (base.equals(target)) {
            return amount;
        }
        
        String urlString = API_URL + base;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "CurrencyConverter/1.0");
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("API request failed with code: " + responseCode);
        }
        
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        String jsonResponse = response.toString();
        String targetRateStr = extractRate(jsonResponse, target);
        
        if (targetRateStr == null || targetRateStr.equals("null")) {
            throw new Exception("Exchange rate not found for " + target);
        }
        
        double targetRate = Double.parseDouble(targetRateStr);
        return amount * targetRate;
    }
    
    private static String extractRate(String json, String currency) {
        String searchKey = "\"" + currency + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return null;
        
        startIndex += searchKey.length();
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
        
        String rateStr = json.substring(startIndex, endIndex).trim();
        rateStr = rateStr.replaceAll("[^0-9.]", "");
        return rateStr;
    }
    
    private static void displayResult(String base, String target, double amount, double convertedAmount) {
        String baseSymbol = getCurrencySymbol(base);
        String targetSymbol = getCurrencySymbol(target);
        
        System.out.println("\n=== Conversion Result ===");
        System.out.printf("%s %.2f %s = %s %.2f %s%n", 
            baseSymbol, amount, base, 
            targetSymbol, convertedAmount, target);
        System.out.printf("Exchange Rate: 1 %s = %.4f %s%n", 
            base, convertedAmount/amount, target);
        System.out.println("========================\n");
    }
    
    private static String getCurrencySymbol(String currency) {
        return switch (currency.toUpperCase()) {
            case "USD" -> "$";
            case "EUR" -> "€";
            case "GBP" -> "£";
            case "INR" -> "₹";
            case "JPY" -> "¥";
            default -> "";
        };
    }
}
