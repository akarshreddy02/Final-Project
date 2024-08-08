package Model;

/**
 * FoodItem.java
 * This is a model class that represents a Food Item entity.
 */
public class FoodItem {
    private int id;
    private String retailerName;
    private String itemName;
    private int quantity;
    private double price;
    private String expirationDate;

    public FoodItem() {
    }

    public FoodItem(String retailerName, String itemName, int quantity, double price, String expirationDate) {
        this.retailerName = retailerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    public FoodItem(int id, String retailerName, String itemName, int quantity, double price, String expirationDate) {
        this.id = id;
        this.retailerName = retailerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    public FoodItem(int id, String retailerName, String itemName, int quantity, String expirationDate) {
        this.id = id;
        this.retailerName = retailerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.price = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    @Override
    public String toString() {
        return "FoodItem{id=" + id + ", retailerName='" + retailerName + "', itemName='" + itemName + "', quantity=" + quantity + ", expirationDate='" + expirationDate + "'}";
    }

}
