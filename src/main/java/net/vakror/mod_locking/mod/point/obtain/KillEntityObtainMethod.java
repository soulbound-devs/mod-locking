package net.vakror.mod_locking.mod.point.obtain;

public class KillEntityObtainMethod extends PointObtainMethod {

    public String entityId;

    public KillEntityObtainMethod(String entity, int amount, String pointType) {
        super(amount, pointType);
        this.entityId = entity;
    }
}
