package net.vakror.mod_locking.mod.point.obtain;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class RightClickItemObtainMethod extends PointObtainMethod {

    public String itemId;

    public RightClickItemObtainMethod(String item, int amount) {
        super("rightClickItem", amount);
        this.itemId = item;
    }
}
