package net.vakror.mod_locking.mod.point.obtain;

public abstract class PointObtainMethod {
    private int amount = 1;
    private final String pointType;
    public PointObtainMethod(int amount, String pointType) {
        this.amount = amount;
        this.pointType = pointType;
    }

    public int getAmount() {
        return amount;
    }

    public String getPointType() {
        return pointType;
    }
}
