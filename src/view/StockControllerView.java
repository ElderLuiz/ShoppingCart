package view;

import controller.StockControllerDAO;
import java.util.Scanner;

public class StockControllerView {

	 private static final StockControllerDAO stockController = new StockControllerDAO();
	    private static final Scanner scanner = new Scanner(System.in);


	    public static void showMenu() {
	        while (true) {
	            System.out.println("\n--- Stock Management Menu ---");
	            System.out.println("1. View Stock");
	            System.out.println("2. Add Product to Stock");
	            System.out.println("3. Check Product Quantity");
	            System.out.println("4. Remove Product from Stock");
	            System.out.println("5. Exit");
	            System.out.print("Please choose an option: ");
	            int choice = scanner.nextInt();
	            scanner.nextLine(); 

	            switch (choice) {
	                case 1:
	                    viewStock();
	                    break;
	                case 2:
	                    addProductToStock();
	                    break;
	                case 3:
	                    checkProductQuantity();
	                    break;
	                case 4:
	                    removeProductFromStock();
	                    break;
	                case 5:
	                    System.out.println("Exiting Stock Management...");
	                    return;
	                default:
	                    System.out.println("Invalid option. Please try again.");
	            }
	        }
	    }

	  
	    private static void viewStock() {
	        System.out.println("\n--- Stock List ---");
	        stockController.viewStock();
	    }


	    private static void addProductToStock() {
	        System.out.print("Enter Product ID: ");
	        int productId = scanner.nextInt();
	        scanner.nextLine();  
	        System.out.print("Enter Quantity to Add: ");
	        int quantityToAdd = scanner.nextInt();
	        scanner.nextLine();  
	        
	        stockController.addProductToStock(productId, quantityToAdd);
	    }


	    private static void checkProductQuantity() {
	        System.out.print("Enter Product ID to Check Quantity: ");
	        int productId = scanner.nextInt();
	        scanner.nextLine();  

	        int quantity = stockController.getStockQuantity(productId);
	        if (quantity != -1) {
	            System.out.println("Quantity available for product ID " + productId + ": " + quantity);
	        } else {
	            System.out.println("Product not found in stock.");
	        }
	    }

	    private static void removeProductFromStock() {
	        System.out.print("Enter Product Name to Remove: ");
	        String productName = scanner.nextLine();
	        scanner.nextLine();  
	        System.out.print("Enter Quantity to Remove: ");
	        int quantityToRemove = scanner.nextInt();
	        scanner.nextLine(); 

	        stockController.removeProductFromStock(productName, quantityToRemove);
	    }

	    public static void main(String[] args) {
	        showMenu();  
	    }
}
