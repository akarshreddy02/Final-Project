/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author lijoy
 */

public class ConsumerItem {
    private int id;
    private String retailerName;
    private String itemName;
    private int quantity;
    private double price;
    private String expirationDate;

    public ConsumerItem(int id, String retailerName, String itemName, int quantity, double price, String expirationDate) {
        this.id = id;
        this.retailerName = retailerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getRetailerName() { return retailerName; }
    public void setRetailerName(String retailerName) { this.retailerName = retailerName; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
}
