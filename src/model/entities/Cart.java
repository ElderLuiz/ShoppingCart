package model.entities;

import Exceptions.DbException;

public class Cart {

    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private double totalValue;


    public Cart() {
    }


    public Cart(int id, String name, String category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.totalValue = price * quantity;
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        calculateTotalValue();
    }

    public int getQuantity() {
    	if(quantity < 0)throw new DbException("Error: quantity does't can be less that zero");
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalValue();
    }

    public double getTotalValue() {
        return totalValue;
    }

    private void calculateTotalValue() {
        this.totalValue = this.price * this.quantity;
    }

    @Override
    public String toString() {
        return "Product Name: " + name + ", Category: " + category +
               ", Product Price: " + price + ", Quantity: " + quantity +
               ", Total Value: " + totalValue + "";
    }
}
