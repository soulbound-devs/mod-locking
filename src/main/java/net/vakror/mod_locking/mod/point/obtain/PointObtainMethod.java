package net.vakror.mod_locking.mod.point.obtain;

public abstract class PointObtainMethod {
    private String name = "rightClickItem";
    private int amount = 1;
    public PointObtainMethod(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    };
}
