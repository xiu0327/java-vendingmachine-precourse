package vendingmachine;

public class Item {
    private String name;
    private int price;
    private int quantity;

    public Item(String name, int price, int count) {
        this.name = name;
        this.price = price;
        this.quantity = count;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
