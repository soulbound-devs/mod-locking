package net.vakror.mod_locking.mod.point.obtain;

import net.vakror.mod_locking.mod.config.ConfigObject;

public abstract class PointObtainMethod implements ConfigObject {
    private int amount = 1;
    private final String pointType;
    private final String name;

    public PointObtainMethod(String name, int amount, String pointType) {
        this.amount = amount;
        this.pointType = pointType;
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public String getPointType() {
        return pointType;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFileName() {
        return name;
    }
}
