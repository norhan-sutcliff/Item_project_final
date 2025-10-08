package itemservice;

public class ShowItemDetails extends ItemDetails {
    private int id;             // item.id
    private String name;        // item.name
    private double price;       // item.price
    private int totalNumber;    // item.total_number

    public ShowItemDetails() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getTotalNumber() { return totalNumber; }
    public void setTotalNumber(int totalNumber) { this.totalNumber = totalNumber; }
}
