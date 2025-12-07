package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

public class ShoppingListItem {
    private final String name;
    private final double quantity;
    private final String unit;
    private boolean isChecked;

    public ShoppingListItem(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.isChecked = false;
    }

    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}