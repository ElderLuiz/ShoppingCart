package view;

import controller.CartControllerDAO;
import model.entities.Cart;
import model.entities.Product;

import java.util.List;
import java.util.Scanner;

public class CartView {

	 private CartControllerDAO cartController;
	    private Scanner scanner;

	    public CartView() {
	        cartController = new CartControllerDAO();
	        scanner = new Scanner(System.in);
	    }


	    public void displayMenu() {
	        while (true) {
	            System.out.println("\n--- Shopping Cart ---");
	            System.out.println("1. View Products");
	            System.out.println("2. Add Product to Cart");
	            System.out.println("3. View Cart");
	            System.out.println("4. Remove Product from Cart");
	            System.out.println("5. Calculate Total");
	            System.out.println("6. Exit");
	            System.out.print("Choose an option: ");

	            int choice = scanner.nextInt();
	            scanner.nextLine(); 

	            switch (choice) {
	                case 1:
	                    viewProducts();
	                    break;
	                case 2:
	                    addProductToCart();
	                    break;
	                case 3:
	                    viewCart();
	                    break;
	                case 4:
	                    removeProductFromCart();
	                    break;
	                case 5:
	                    calculateTotal();
	                    break;
	                case 6:
	                    System.out.println("Exiting...");
	                    return;
	                default:
	                    System.out.println("Invalid option, please try again.");
	            }
	           
	            }
	        }
	    
	    

    private void viewProducts() {
    	
        List<Product> products = cartController.getAllProducts();
        System.out.println("\n----------- Products Available -----------");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println(product);
            /*
            System.out.println("Product Id: "+(i + 1) + " | " + product.getName() + " - " + product.getCategory() +
                    " - $" + product.getPrice() + " - Stock: " + product.getQuantity());*/
        }
    }


    private void addProductToCart() {
        System.out.print("\nEnter product Name to add to cart: ");
        String productName = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); 

        Product selectedProduct = cartController.getProductByName(productName);

        if (selectedProduct != null && quantity <= selectedProduct.getQuantity()) {
            Cart cartItem = new Cart(selectedProduct.getId(), selectedProduct.getName(),
                    selectedProduct.getCategory(), selectedProduct.getPrice(), quantity);
            
            cartController.addProductToCart(cartItem);
            System.out.println("Product added to cart!");
        } else {
            System.out.println("Invalid quantity or product not found.");
        }
    }


    private void viewCart() {
        List<Cart> cartItems = cartController.getAllCartItems();
        System.out.println("\n--- Cart ---");
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            for (Cart cartItem : cartItems) {
                System.out.println(cartItem);
            }
        }
    }

    private void removeProductFromCart() {
        System.out.print("\nEnter cart Name of the product to remove: ");
        String cartName = scanner.nextLine();
     

        cartController.removeProductFromCart(cartName);
    }

  
 
    private void calculateTotal() {
        double total = cartController.calculateCartTotal();
        System.out.println("\nTotal Cart Value: $" + total);
    }

    public static void main(String[] args) {
        CartView cartView = new CartView();
        cartView.displayMenu();
    }
}
