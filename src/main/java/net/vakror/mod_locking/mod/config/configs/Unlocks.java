package net.vakror.mod_locking.mod.config.configs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.vakror.mod_locking.mod.unlock.FineGrainedModUnlock;
import net.vakror.mod_locking.mod.unlock.ModUnlock;

import java.util.List;

import static net.vakror.mod_locking.mod.config.configs.ModUnlocksConfig.createCostMap;

public class Unlocks {

    public static void addModStorageUnlocks(List<ModUnlock> modUnlocks) {

        modUnlocks.add(new ModUnlock("Refined Storage", createCostMap("Slotted Point", 5), 0f, 1.5f, "refinedstorage").withIcon("refinedstorage:lime_creative_controller").withTree("Storage"));
        modUnlocks.add(new ModUnlock("Refined Storage Addons", createCostMap("Slotted Point", 2), "Refined Storage", 1.5f, 1.5f, "refinedstorageaddons").withIcon("refinedstorageaddons:creative_wireless_crafting_grid").withTree("Storage"));

        modUnlocks.add(new ModUnlock("Applied Energistics 2", createCostMap("Slotted Point", 5), 4.5f, 1.5f, "ae2").withIcon("ae2:controller").withTree("Storage"));

        CompoundTag facadesTag = new CompoundTag();
        facadesTag.putString("item", "biomesoplenty:rose_quartz_block");
    }

    public static void addFineGrainedStorageUnlocks(List<FineGrainedModUnlock> fineGrainedModUnlocks) {

        CompoundTag facadesTag = new CompoundTag();
        facadesTag.putString("item", "biomesoplenty:rose_quartz_block");

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Applied Energistics Facades", createCostMap("Slotted Point", 2), 6f, 1.5f, "Applied Energistics 2").withItemRestriction("ae2:facade", true).withIcon("ae2:facade").withIconNbt(facadesTag).withTree("Storage"));


        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Uncolossal Chests", createCostMap("Slotted Point", 1), 9f, 1.5f, "Wooden Colossal Chests").withBlockItemRestriction("colossalchests:uncolossal_chest", true).withIcon("colossalchests:uncolossal_chest").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Wooden Colossal Chests", createCostMap("Slotted Point", 4), 9f, 3f, "").withBlockItemRestriction("colossalchests:chest_wall_wood", true).withBlockItemRestriction("colossalchests:interface_wood", true).withBlockItemRestriction("colossalchests:colossal_chest_wood", true).withIcon("colossalchests:interface_wood").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Copper Colossal Chests", createCostMap("Slotted Point", 6), 10.5f, 3f, "Wooden Colossal Chests").withBlockItemRestriction("colossalchests:chest_wall_copper", true).withBlockItemRestriction("colossalchests:interface_copper", true).withBlockItemRestriction("colossalchests:colossal_chest_copper", true).withIcon("colossalchests:interface_copper").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Iron Colossal Chests", createCostMap("Slotted Point", 8), 12f, 3f, "Copper Colossal Chests").withBlockItemRestriction("colossalchests:chest_wall_iron", true).withBlockItemRestriction("colossalchests:interface_iron", true).withBlockItemRestriction("colossalchests:colossal_chest_iron", true).withIcon("colossalchests:interface_iron").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Silver Colossal Chests", createCostMap("Slotted Point", 9), 13.5f, 3f, "Iron Colossal Chests").withBlockItemRestriction("colossalchests:chest_wall_silver", true).withBlockItemRestriction("colossalchests:interface_silver", true).withBlockItemRestriction("colossalchests:colossal_chest_silver", true).withIcon("colossalchests:interface_silver").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Gold Colossal Chests", createCostMap("Slotted Point", 9), 15f, 3f, "Silver Colossal Chests").withBlockItemRestriction("colossalchests:chest_wall_gold", true).withBlockItemRestriction("colossalchests:interface_gold", true).withBlockItemRestriction("colossalchests:colossal_chest_gold", true).withIcon("colossalchests:interface_gold").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Diamond Colossal Chests", createCostMap("Slotted Point", 9), 16.5f, 3f, "Gold Colossal Chests").withBlockItemRestriction("colossalchests:chest_wall_diamond", true).withBlockItemRestriction("colossalchests:interface_diamond", true).withBlockItemRestriction("colossalchests:colossal_chest_diamond", true).withIcon("colossalchests:interface_diamond").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Obsidian Colossal Chests", createCostMap("Slotted Point", 9), 18f, 3f, "Diamond Colossal Chests").withBlockItemRestriction("colossalchests:chest_wall_obsidian", true).withBlockItemRestriction("colossalchests:interface_obsidian", true).withBlockItemRestriction("colossalchests:colossal_chest_obsidian", true).withIcon("colossalchests:interface_obsidian").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Colossal Chest Upgrades", createCostMap("Slotted Point", 5), 9f, 4.5f, "Wooden Colossal Chests").withItemRestriction("colossalchests:upgrade_tool", true).withItemRestriction("colossalchests:upgrade_tool_reverse", true).withIcon("colossalchests:upgrade_tool").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Sacks", createCostMap("Slotted Point", 3), 1.5f, 0, "").withItemRestriction("soulbound:sack", true).withIcon("soulbound:sack").withTree("Storage"));

        CompoundTag enderTag = new CompoundTag();
        enderTag.putInt("code", 444);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Ender Chests", createCostMap("Slotted Point", 3), 4.5f, 0, "").withBlockItemRestriction("enderchests:ender_chest", true).withIcon("enderchests:ender_chest").withIconNbt(enderTag).withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Ender Bags", createCostMap("Slotted Point", 4), 6f, 0, "Ender Chests").withItemRestriction("enderchests:ender_bag", true).withIcon("enderchests:ender_bag").withIconNbt(enderTag).withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Ender Pouch", createCostMap("Slotted Point", 6), 7.5f, 0, "Ender Bags").withItemRestriction("enderchests:ender_pouch", true).withIcon("enderchests:ender_pouch").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Ender Tanks", createCostMap("Slotted Point", 3), 10.5f, 0, "").withItemRestriction("endertanks:ender_tank", true).withIcon("endertanks:ender_tank").withIconNbt(enderTag).withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Ender Buckets", createCostMap("Slotted Point", 3), 12f, 0, "Ender Tanks").withItemRestriction("endertanks:ender_bucket", true).withIcon("endertanks:ender_bucket").withIconNbt(enderTag).withTree("Storage"));

        addSophisticatedBackpacksUnlocks(fineGrainedModUnlocks);
        addSophisticatedStorageUnlocks(fineGrainedModUnlocks, 3, 6);
        addSophisticatedUpgradeUnlocks(fineGrainedModUnlocks);
    }

    public static void addSophisticatedBackpacksUnlocks(List<FineGrainedModUnlock> fineGrainedModUnlocks) {
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Backpacks", createCostMap("Slotted Point", 3), 0, 3f, "").withBlockItemRestriction("sophisticatedbackpacks:backpack", true).withIcon("sophisticatedbackpacks:backpack").withTree("Storage"));

        CompoundTag ironBackpackTag = new CompoundTag();
        ironBackpackTag.putInt("borderColor", 16383998);
        ironBackpackTag.putInt("clothColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Backpacks", createCostMap("Slotted Point", 4), 1.5f, 3f, "Tiny Backpacks").withBlockItemRestriction("sophisticatedbackpacks:iron_backpack", true).withIcon("sophisticatedbackpacks:backpack").withIconNbt(ironBackpackTag).withTree("Storage"));

        CompoundTag goldBackpackTag = new CompoundTag();
        goldBackpackTag.putInt("borderColor", 16701501);
        goldBackpackTag.putInt("clothColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Backpacks", createCostMap("Slotted Point", 6), 3f, 3f, "Small Backpacks").withBlockItemRestriction("sophisticatedbackpacks:gold_backpack", true).withIcon("sophisticatedbackpacks:backpack").withIconNbt(goldBackpackTag).withTree("Storage"));

        CompoundTag diamondBackpackTag = new CompoundTag();
        diamondBackpackTag.putInt("borderColor", 3847130);
        diamondBackpackTag.putInt("clothColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Backpacks", createCostMap("Slotted Point", 8), 4.5f, 3f, "Backpacks").withBlockItemRestriction("sophisticatedbackpacks:diamond_backpack", true).withIcon("sophisticatedbackpacks:backpack").withIconNbt(diamondBackpackTag).withTree("Storage"));

        CompoundTag netheriteBackpackTag = new CompoundTag();
        netheriteBackpackTag.putInt("borderColor", 4673362);
        netheriteBackpackTag.putInt("clothColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Backpacks", createCostMap("Slotted Point", 9), 6f, 3f, "Big Backpacks").withBlockItemRestriction("sophisticatedbackpacks:netherite_backpack", true).withIcon("sophisticatedbackpacks:backpack").withIconNbt(netheriteBackpackTag).withTree("Storage"));
    }

    public static void addSophisticatedUpgradeUnlocks(List<FineGrainedModUnlock> fineGrainedModUnlocks) {
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Upgrades!", createCostMap("Slotted Point", 2), 4.5f, 7.5f, "").withItemRestriction("sophisticatedbackpacks:upgrade_base", true).withItemRestriction("sophisticatedstorage:upgrade_base", true).withIcon("sophisticatedbackpacks:upgrade_base").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Filtering!", createCostMap("Slotted Point", 3), 4.5f, 6f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:filter_upgrade", true).withItemRestriction("sophisticatedstorage:filter_upgrade", true).withIcon("sophisticatedbackpacks:filter_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Filtering!", createCostMap("Slotted Point", 5), 4.5f, 4.5f, "Filtering!").withItemRestriction("sophisticatedbackpacks:advanced_filter_upgrade", true).withItemRestriction("sophisticatedstorage:advanced_filter_upgrade", true).withIcon("sophisticatedbackpacks:advanced_filter_upgrade").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Refilling!", createCostMap("Slotted Point", 4), 6f, 6f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:refill_upgrade", true).withIcon("sophisticatedbackpacks:refill_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Refilling!", createCostMap("Slotted Point", 6), 6f, 4.5f, "Refilling!").withItemRestriction("sophisticatedbackpacks:advanced_refill_upgrade", true).withIcon("sophisticatedbackpacks:advanced_refill_upgrade").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Picking Things Up!", createCostMap("Slotted Point", 5), 6f, 7.5f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:pickup_upgrade", true).withItemRestriction("sophisticatedstorage:pickup_upgrade", true).withIcon("sophisticatedbackpacks:pickup_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Getting Better At Picking Things Up!", createCostMap("Slotted Point", 6), 7.5f, 7.5f, "Picking Things Up!").withItemRestriction("sophisticatedbackpacks:advanced_pickup_upgrade", true).withItemRestriction("sophisticatedstorage:advanced_pickup_upgrade", true).withIcon("sophisticatedbackpacks:pickup_upgrade").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Voiding!", createCostMap("Slotted Point", 5), 4.5f, 10.5f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:void_upgrade", true).withItemRestriction("sophisticatedstorage:void_upgrade", true).withIcon("sophisticatedbackpacks:void_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Voiding!", createCostMap("Slotted Point", 6), 3f, 10.5f, "Voiding!").withItemRestriction("sophisticatedbackpacks:advanced_void_upgrade", true).withItemRestriction("sophisticatedstorage:advanced_void_upgrade", true).withIcon("sophisticatedbackpacks:advanced_void_upgrade").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Storing Fluids!", createCostMap("Slotted Point", 6), 4.5f, 9f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:tank_upgrade", true).withIcon("sophisticatedbackpacks:tank_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Storing Energy!", createCostMap("Slotted Point", 6), 3f, 9f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:battery_upgrade", true).withIcon("sophisticatedbackpacks:battery_upgrade").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Feeding!", createCostMap("Slotted Point", 7), 6f, 13.5f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:feeding_upgrade", true).withItemRestriction("sophisticatedstorage:feeding_upgrade", true).withIcon("sophisticatedbackpacks:feeding_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Feeding!", createCostMap("Slotted Point", 9), 7.5f, 13.5f, "Feeding!").withItemRestriction("sophisticatedbackpacks:advanced_feeding_upgrade", true).withItemRestriction("sophisticatedstorage:advanced_feeding_upgrade", true).withIcon("sophisticatedbackpacks:advanced_feeding_upgrade").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Playing Jenga!", createCostMap("Slotted Point", 4), 4.5f, 13.5f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:stack_upgrade_tier_1", true).withItemRestriction("sophisticatedstorage:stack_upgrade_tier_1", true).withIcon("sophisticatedbackpacks:stack_upgrade_tier_1").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Practicing Jenga!", createCostMap("Slotted Point", 7), 3f, 13.5f, "Playing Jenga!").withItemRestriction("sophisticatedbackpacks:stack_upgrade_tier_2", true).withItemRestriction("sophisticatedstorage:stack_upgrade_tier_2", true).withIcon("sophisticatedbackpacks:stack_upgrade_tier_2").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Getting Good At Jenga!", createCostMap("Slotted Point", 9), 1.5f, 13.5f, "Practicing Jenga!").withItemRestriction("sophisticatedbackpacks:stack_upgrade_tier_3", true).withItemRestriction("sophisticatedstorage:stack_upgrade_tier_3", true).withIcon("sophisticatedbackpacks:stack_upgrade_tier_3").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Jenga Perfection!", createCostMap("Slotted Point", 12), 0f, 13.5f, "Getting Good At Jenga!").withItemRestriction("sophisticatedbackpacks:stack_upgrade_tier_4", true).withItemRestriction("sophisticatedstorage:stack_upgrade_tier_4", true).withIcon("sophisticatedbackpacks:stack_upgrade_tier_4").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Crafting!", createCostMap("Slotted Point", 7), 4.5f, 12f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:crafting_upgrade", true).withItemRestriction("sophisticatedstorage:crafting_upgrade", true).withIcon("sophisticatedbackpacks:crafting_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Compacting!", createCostMap("Slotted Point", 4), 3f, 12f, "Crafting!").withItemRestriction("sophisticatedbackpacks:compacting_upgrade", true).withItemRestriction("sophisticatedstorage:compacting_upgrade", true).withIcon("sophisticatedbackpacks:compacting_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Compacting!", createCostMap("Slotted Point", 5), 1.5f, 12f, "Compacting!").withItemRestriction("sophisticatedbackpacks:advanced_compacting_upgrade", true).withItemRestriction("sophisticatedstorage:advanced_compacting_upgrade", true).withIcon("sophisticatedbackpacks:advanced_compacting_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Smelting!", createCostMap("Slotted Point", 8), 6f, 12f, "Crafting!").withItemRestriction("sophisticatedbackpacks:smelting_upgrade", true).withItemRestriction("sophisticatedstorage:smelting_upgrade", true).withIcon("sophisticatedbackpacks:smelting_upgrade").withTree("Storage"));


        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Hoppering!", createCostMap("Slotted Point", 3), 3f, 7.5f, "Upgrades!").withItemRestriction("sophisticatedstorage:hopper_upgrade", true).withIcon("sophisticatedstorage:hopper_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Hoppering!", createCostMap("Slotted Point", 4), 1.5f, 7.5f, "Hoppering!").withItemRestriction("sophisticatedstorage:advanced_hopper_upgrade", true).withIcon("sophisticatedstorage:advanced_hopper_upgrade").withTree("Storage"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Restocking!", createCostMap("Slotted Point", 4), 9f, 9f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:restock_upgrade", true).withIcon("sophisticatedbackpacks:restock_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Depositing!", createCostMap("Slotted Point", 4), 9f, 10.5f, "Upgrades!").withItemRestriction("sophisticatedbackpacks:deposit_upgrade", true).withIcon("sophisticatedbackpacks:deposit_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Restocking!", createCostMap("Slotted Point", 6), 9f, 7.5f, "Restocking!").withItemRestriction("sophisticatedbackpacks:advanced_restock_upgrade", true).withIcon("sophisticatedbackpacks:advanced_restock_upgrade").withTree("Storage"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Depositing!", createCostMap("Slotted Point", 6), 9f, 12f, "Depositing!").withItemRestriction("sophisticatedbackpacks:advanced_deposit_upgrade", true).withIcon("sophisticatedbackpacks:advanced_deposit_upgrade").withTree("Storage"));
    }

    public static void addSophisticatedStorageUnlocks(List<FineGrainedModUnlock> fineGrainedModUnlocks, float offsetX, float offsetY) {
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Sophisticated Storage", createCostMap("Slotted Point", 4), 9f + offsetX, 3f + offsetY, "").withBlockItemRestriction("sophisticatedstorage:controller", true).withItemRestriction("sophisticatedstorage:packing_tape", true).withItemRestriction("sophisticatedstorage:storage_tool", true).withIcon("sophisticatedstorage:controller").withTree("Storage"));

        // TODO: Move to right by 1.5;
        CompoundTag chestTag = new CompoundTag();
        chestTag.putString("woodType", "oak");
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Chests", createCostMap("Slotted Point", 3), 9f + offsetX, 4.5f + offsetY, "Sophisticated Storage").withBlockItemRestriction("sophisticatedstorage:chest", true).withIcon("sophisticatedstorage:chest").withIconNbt(chestTag).withTree("Storage"));

        CompoundTag ironChestTag = new CompoundTag();
        ironChestTag.putInt("mainColor", 16383998);
        ironChestTag.putInt("accentColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Chests", createCostMap("Slotted Point", 4), 10.5f + offsetX, 4.5f + offsetY, "Tiny Chests").withBlockItemRestriction("sophisticatedstorage:iron_chest", true).withIcon("sophisticatedstorage:iron_chest").withIconNbt(ironChestTag).withTree("Storage"));

        CompoundTag goldChestTag = new CompoundTag();
        goldChestTag.putInt("mainColor", 16701501);
        goldChestTag.putInt("accentColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Chests", createCostMap("Slotted Point", 6), 12f + offsetX, 4.5f + offsetY, "Small Chests").withBlockItemRestriction("sophisticatedstorage:gold_chest", true).withIcon("sophisticatedstorage:gold_chest").withIconNbt(goldChestTag).withTree("Storage"));

        CompoundTag diamondChestTag = new CompoundTag();
        diamondChestTag.putInt("mainColor", 3847130);
        diamondChestTag.putInt("accentColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Chests", createCostMap("Slotted Point", 8), 13.5f + offsetX, 4.5f + offsetY, "Chests").withBlockItemRestriction("sophisticatedstorage:diamond_chest", true).withIcon("sophisticatedstorage:diamond_chest").withIconNbt(diamondChestTag).withTree("Storage"));

        CompoundTag netheriteChestTag = new CompoundTag();
        netheriteChestTag.putInt("mainColor", 4673362);
        netheriteChestTag.putInt("accentColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Chests", createCostMap("Slotted Point", 9), 15f + offsetX, 4.5f + offsetY, "Big Chests").withBlockItemRestriction("sophisticatedstorage:netherite_chest", true).withIcon("sophisticatedstorage:netherite_chest").withIconNbt(netheriteChestTag).withTree("Storage"));


        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Shulker Boxes", createCostMap("Slotted Point", 5), 9f + offsetX, 1.5f + offsetY, "Sophisticated Storage").withBlockItemRestriction("sophisticatedstorage:shulker_box", true).withIcon("sophisticatedstorage:shulker_box").withTree("Storage"));

        CompoundTag ironShulkerTag = new CompoundTag();
        ironShulkerTag.putInt("mainColor", 16383998);
        ironShulkerTag.putInt("accentColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Shulker Boxes", createCostMap("Slotted Point", 6), 10.5f + offsetX, 1.5f + offsetY, "Tiny Shulker Boxes").withBlockItemRestriction("sophisticatedstorage:iron_shulker_box", true).withIcon("sophisticatedstorage:iron_shulker_box").withIconNbt(ironShulkerTag).withTree("Storage"));

        CompoundTag goldShulkerTag = new CompoundTag();
        goldShulkerTag.putInt("mainColor", 16701501);
        goldShulkerTag.putInt("accentColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Shulker Boxes", createCostMap("Slotted Point", 8), 12f + offsetX, 1.5f + offsetY, "Small Shulker Boxes").withBlockItemRestriction("sophisticatedstorage:gold_shulker_box", true).withIcon("sophisticatedstorage:gold_shulker_box").withIconNbt(goldShulkerTag).withTree("Storage"));

        CompoundTag diamondShulkerTag = new CompoundTag();
        diamondShulkerTag.putInt("mainColor", 3847130);
        diamondShulkerTag.putInt("accentColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Shulker Boxes", createCostMap("Slotted Point", 10), 13.5f + offsetX, 1.5f + offsetY, "Shulker Boxes").withBlockItemRestriction("sophisticatedstorage:diamond_shulker_box", true).withIcon("sophisticatedstorage:diamond_shulker_box").withIconNbt(diamondShulkerTag).withTree("Storage"));

        CompoundTag netheriteShulkerTag = new CompoundTag();
        netheriteShulkerTag.putInt("mainColor", 4673362);
        netheriteShulkerTag.putInt("accentColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Shulker Boxes", createCostMap("Slotted Point", 11), 15f + offsetX, 1.5f + offsetY, "Big Shulker Boxes").withBlockItemRestriction("sophisticatedstorage:netherite_shulker_box", true).withIcon("sophisticatedstorage:netherite_shulker_box").withIconNbt(netheriteShulkerTag).withTree("Storage"));



        CompoundTag barrelTag = new CompoundTag();
        barrelTag.putString("woodType", "spruce");
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Barrels", createCostMap("Slotted Point", 3), 12f + offsetX, 3f + offsetY, "Sophisticated Storage").withBlockItemRestriction("sophisticatedstorage:barrel", true).withIcon("sophisticatedstorage:barrel").withIconNbt(barrelTag).withTree("Storage"));

        CompoundTag ironBarrelTag = new CompoundTag();
        ironBarrelTag.putInt("mainColor", 16383998);
        ironBarrelTag.putInt("accentColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Barrels", createCostMap("Slotted Point", 4), 13.5f + offsetX, 3f + offsetY, "Tiny Barrels").withBlockItemRestriction("sophisticatedstorage:iron_barrel", true).withIcon("sophisticatedstorage:iron_barrel").withIconNbt(ironBarrelTag).withTree("Storage"));

        CompoundTag goldBarrelTag = new CompoundTag();
        goldBarrelTag.putInt("mainColor", 16701501);
        goldBarrelTag.putInt("accentColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Barrels", createCostMap("Slotted Point", 6), 15f + offsetX, 3f + offsetY, "Small Barrels").withBlockItemRestriction("sophisticatedstorage:gold_barrel", true).withIcon("sophisticatedstorage:gold_barrel").withIconNbt(goldBarrelTag).withTree("Storage"));

        CompoundTag diamondBarrelTag = new CompoundTag();
        diamondBarrelTag.putInt("mainColor", 3847130);
        diamondBarrelTag.putInt("accentColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Barrels", createCostMap("Slotted Point", 8), 16.5f + offsetX, 3f + offsetY, "Barrels").withBlockItemRestriction("sophisticatedstorage:diamond_barrel", true).withIcon("sophisticatedstorage:diamond_barrel").withIconNbt(diamondBarrelTag).withTree("Storage"));

        CompoundTag netheriteBarrelTag = new CompoundTag();
        netheriteBarrelTag.putInt("mainColor", 4673362);
        netheriteBarrelTag.putInt("accentColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Barrels", createCostMap("Slotted Point", 9), 18f + offsetX, 3f + offsetY, "Big Barrels").withBlockItemRestriction("sophisticatedstorage:netherite_barrel", true).withIcon("sophisticatedstorage:netherite_barrel").withIconNbt(netheriteBarrelTag).withTree("Storage"));


        CompoundTag tinyLimitedBarrelITag = new CompoundTag();
        tinyLimitedBarrelITag.putString("woodType", "Spruce");
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Limited Barrels I", createCostMap("Slotted Point", 3), 13.5f  + offsetX, 6f + offsetY, "Barrels").withBlockItemRestriction("sophisticatedstorage:limited_barrel_1", true).withIcon("sophisticatedstorage:limited_barrel_1").withIconNbt(tinyLimitedBarrelITag).withTree("Storage"));


        CompoundTag smallLimitedBarrelITag = new CompoundTag();
        smallLimitedBarrelITag.putInt("mainColor", 16383998);
        smallLimitedBarrelITag.putInt("accentColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Limited Barrels I", createCostMap("Slotted Point", 3), 13.5f  + offsetX, 7.5f + offsetY, "Tiny Limited Barrels I").withBlockItemRestriction("sophisticatedstorage:limited_iron_barrel_1", true).withIcon("sophisticatedstorage:limited_iron_barrel_1").withIconNbt(smallLimitedBarrelITag).withTree("Storage"));


        CompoundTag limitedBarrelITag = new CompoundTag();
        limitedBarrelITag.putInt("mainColor", 16701501);
        limitedBarrelITag.putInt("accentColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Limited Barrels I", createCostMap("Slotted Point", 3), 13.5f  + offsetX, 9f + offsetY, "Small Limited Barrels I").withBlockItemRestriction("sophisticatedstorage:limited_gold_barrel_1", true).withIcon("sophisticatedstorage:limited_gold_barrel_1").withIconNbt(limitedBarrelITag).withTree("Storage"));


        CompoundTag bigLimitedBarrelITag = new CompoundTag();
        bigLimitedBarrelITag.putInt("mainColor", 3847130);
        bigLimitedBarrelITag.putInt("accentColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Limited Barrels I", createCostMap("Slotted Point", 3), 13.5f  + offsetX, 10.5f + offsetY, "Limited Barrels I").withBlockItemRestriction("sophisticatedstorage:limited_diamond_barrel_1", true).withIcon("sophisticatedstorage:limited_diamond_barrel_1").withIconNbt(bigLimitedBarrelITag).withTree("Storage"));


        CompoundTag hugeLimitedBarrelITag = new CompoundTag();
        hugeLimitedBarrelITag.putInt("mainColor", 4673362);
        hugeLimitedBarrelITag.putInt("accentColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Limited Barrels I", createCostMap("Slotted Point", 3), 13.5f  + offsetX, 12f + offsetY, "Big Limited Barrels I").withBlockItemRestriction("sophisticatedstorage:limited_netherite_barrel_1", true).withIcon("sophisticatedstorage:limited_netherite_barrel_1").withIconNbt(hugeLimitedBarrelITag).withTree("Storage"));



        CompoundTag tinyLimitedBarrelIITag = new CompoundTag();
        tinyLimitedBarrelIITag.putString("woodType", "Spruce");
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Limited Barrels II", createCostMap("Slotted Point", 3), 15f  + offsetX, 6f + offsetY, "Barrels").withBlockItemRestriction("sophisticatedstorage:limited_barrel_2", true).withIcon("sophisticatedstorage:limited_barrel_2").withIconNbt(tinyLimitedBarrelIITag).withTree("Storage"));


        CompoundTag smallLimitedBarrelIITag = new CompoundTag();
        smallLimitedBarrelIITag.putInt("mainColor", 16383998);
        smallLimitedBarrelIITag.putInt("accentColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Limited Barrels II", createCostMap("Slotted Point", 3), 15f  + offsetX, 7.5f + offsetY, "Tiny Limited Barrels II").withBlockItemRestriction("sophisticatedstorage:limited_iron_barrel_2", true).withIcon("sophisticatedstorage:limited_iron_barrel_2").withIconNbt(smallLimitedBarrelIITag).withTree("Storage"));


        CompoundTag limitedBarrelIITag = new CompoundTag();
        limitedBarrelIITag.putInt("mainColor", 16701501);
        limitedBarrelIITag.putInt("accentColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Limited Barrels II", createCostMap("Slotted Point", 3), 15f  + offsetX, 9f + offsetY, "Small Limited Barrels II").withBlockItemRestriction("sophisticatedstorage:limited_gold_barrel_2", true).withIcon("sophisticatedstorage:limited_gold_barrel_2").withIconNbt(limitedBarrelIITag).withTree("Storage"));


        CompoundTag bigLimitedBarrelIITag = new CompoundTag();
        bigLimitedBarrelIITag.putInt("mainColor", 3847130);
        bigLimitedBarrelIITag.putInt("accentColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Limited Barrels II", createCostMap("Slotted Point", 3), 15f  + offsetX, 10.5f + offsetY, "Limited Barrels II").withBlockItemRestriction("sophisticatedstorage:limited_diamond_barrel_2", true).withIcon("sophisticatedstorage:limited_diamond_barrel_2").withIconNbt(bigLimitedBarrelIITag).withTree("Storage"));


        CompoundTag hugeLimitedBarrelIITag = new CompoundTag();
        hugeLimitedBarrelIITag.putInt("mainColor", 4673362);
        hugeLimitedBarrelIITag.putInt("accentColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Limited Barrels II", createCostMap("Slotted Point", 3), 15f  + offsetX, 12f + offsetY, "Big Limited Barrels II").withBlockItemRestriction("sophisticatedstorage:limited_netherite_barrel_2", true).withIcon("sophisticatedstorage:limited_netherite_barrel_2").withIconNbt(hugeLimitedBarrelIITag).withTree("Storage"));


        CompoundTag tinyLimitedBarrelIIITag = new CompoundTag();
        tinyLimitedBarrelIIITag.putString("woodType", "Spruce");
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Limited Barrels III", createCostMap("Slotted Point", 3), 16.5f  + offsetX, 6f + offsetY, "Barrels").withBlockItemRestriction("sophisticatedstorage:limited_barrel_3", true).withIcon("sophisticatedstorage:limited_barrel_3").withIconNbt(tinyLimitedBarrelIIITag).withTree("Storage"));


        CompoundTag smallLimitedBarrelIIITag = new CompoundTag();
        smallLimitedBarrelIIITag.putInt("mainColor", 16383998);
        smallLimitedBarrelIIITag.putInt("accentColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Limited Barrels III", createCostMap("Slotted Point", 3), 16.5f  + offsetX, 7.5f + offsetY, "Tiny Limited Barrels III").withBlockItemRestriction("sophisticatedstorage:limited_iron_barrel_3", true).withIcon("sophisticatedstorage:limited_iron_barrel_3").withIconNbt(smallLimitedBarrelIIITag).withTree("Storage"));


        CompoundTag limitedBarrelIIITag = new CompoundTag();
        limitedBarrelIIITag.putInt("mainColor", 16701501);
        limitedBarrelIIITag.putInt("accentColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Limited Barrels III", createCostMap("Slotted Point", 3), 16.5f  + offsetX, 9f + offsetY, "Small Limited Barrels III").withBlockItemRestriction("sophisticatedstorage:limited_gold_barrel_3", true).withIcon("sophisticatedstorage:limited_gold_barrel_3").withIconNbt(limitedBarrelIIITag).withTree("Storage"));


        CompoundTag bigLimitedBarrelIIITag = new CompoundTag();
        bigLimitedBarrelIIITag.putInt("mainColor", 3847130);
        bigLimitedBarrelIIITag.putInt("accentColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Limited Barrels III", createCostMap("Slotted Point", 3), 16.5f  + offsetX, 10.5f + offsetY, "Limited Barrels III").withBlockItemRestriction("sophisticatedstorage:limited_diamond_barrel_3", true).withIcon("sophisticatedstorage:limited_diamond_barrel_3").withIconNbt(bigLimitedBarrelIIITag).withTree("Storage"));


        CompoundTag hugeLimitedBarrelIIITag = new CompoundTag();
        hugeLimitedBarrelIIITag.putInt("mainColor", 4673362);
        hugeLimitedBarrelIIITag.putInt("accentColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Limited Barrels III", createCostMap("Slotted Point", 3), 16.5f + offsetX, 12f + offsetY, "Big Limited Barrels III").withBlockItemRestriction("sophisticatedstorage:limited_netherite_barrel_3", true).withIcon("sophisticatedstorage:limited_netherite_barrel_3").withIconNbt(hugeLimitedBarrelIIITag).withTree("Storage"));



        CompoundTag tinyLimitedBarrelIIIITag = new CompoundTag();
        tinyLimitedBarrelIIIITag.putString("woodType", "Spruce");
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Tiny Limited Barrels IV", createCostMap("Slotted Point", 3), 18f  + offsetX, 6f + offsetY, "Barrels").withBlockItemRestriction("sophisticatedstorage:limited_barrel_4", true).withIcon("sophisticatedstorage:limited_barrel_4").withIconNbt(tinyLimitedBarrelIIIITag).withTree("Storage"));


        CompoundTag smallLimitedBarrelIIIITag = new CompoundTag();
        smallLimitedBarrelIIIITag.putInt("mainColor", 16383998);
        smallLimitedBarrelIIIITag.putInt("accentColor", 16383998);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Small Limited Barrels IV", createCostMap("Slotted Point", 3), 18f  + offsetX, 7.5f + offsetY, "Tiny Limited Barrels IV").withBlockItemRestriction("sophisticatedstorage:limited_iron_barrel_4", true).withIcon("sophisticatedstorage:limited_iron_barrel_4").withIconNbt(smallLimitedBarrelIIIITag).withTree("Storage"));


        CompoundTag limitedBarrelIIIITag = new CompoundTag();
        limitedBarrelIIIITag.putInt("mainColor", 16701501);
        limitedBarrelIIIITag.putInt("accentColor", 16701501);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Limited Barrels IV", createCostMap("Slotted Point", 3), 18f  + offsetX, 9f + offsetY, "Small Limited Barrels IV").withBlockItemRestriction("sophisticatedstorage:limited_gold_barrel_4", true).withIcon("sophisticatedstorage:limited_gold_barrel_4").withIconNbt(limitedBarrelIIIITag).withTree("Storage"));


        CompoundTag bigLimitedBarrelIIIITag = new CompoundTag();
        bigLimitedBarrelIIIITag.putInt("mainColor", 3847130);
        bigLimitedBarrelIIIITag.putInt("accentColor", 3847130);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Big Limited Barrels IV", createCostMap("Slotted Point", 3), 18f  + offsetX, 10.5f + offsetY, "Limited Barrels IV").withBlockItemRestriction("sophisticatedstorage:limited_diamond_barrel_4", true).withIcon("sophisticatedstorage:limited_diamond_barrel_4").withIconNbt(bigLimitedBarrelIIIITag).withTree("Storage"));


        CompoundTag hugeLimitedBarrelIIIITag = new CompoundTag();
        hugeLimitedBarrelIIIITag.putInt("mainColor", 4673362);
        hugeLimitedBarrelIIIITag.putInt("accentColor", 4673362);
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Huge Limited Barrels IV", createCostMap("Slotted Point", 3), 18f  + offsetX, 12f + offsetY, "Big Limited Barrels IV").withBlockItemRestriction("sophisticatedstorage:limited_netherite_barrel_4", true).withIcon("sophisticatedstorage:limited_netherite_barrel_4").withIconNbt(hugeLimitedBarrelIIIITag).withTree("Storage"));
    }

    public static void addModTechUnlocks(List<ModUnlock> modUnlocks) {
        modUnlocks.add(new ModUnlock("Create", createCostMap("Quantum Point", 4), 0, 0, "create").withIcon("create:large_cogwheel").withTree("Technology"));
        modUnlocks.add(new ModUnlock("Powah!", createCostMap("Quantum Point", 3), 1.5f, 0f, "powah").withIcon("powah:energy_cell_niotic").withTree("Technology"));
        modUnlocks.add(new ModUnlock("RFTools Base", createCostMap("Quantum Point", 3), 0f, 1.5f, "rftoolsbase").withIcon("rftoolsbase:machine_infuser").withTree("Technology"));
        modUnlocks.add(new ModUnlock("Xnet", createCostMap("Quantum Point", 5), "RFTools Base", 1.5f, 1.5f, "xnet").withIcon("xnet:connector_green").withTree("Technology"));

        CompoundTag tag = new CompoundTag();
        tag.putString("Id", "ironjetpacks:diamond");
        modUnlocks.add(new ModUnlock("Iron Jetpacks", createCostMap("Quantum Point", 6), 3f, 0, "ironjetpacks").withIcon("ironjetpacks:jetpack").withTree("Technology").withIconNbt(tag));

        modUnlocks.add(new ModUnlock("LaserIO", createCostMap("Quantum Point", 6), 3f, 1.5f, "laserio").withIcon("laserio:laser_node").withTree("Technology"));

        modUnlocks.add(new ModUnlock("Extended Crafting", createCostMap("Quantum Point", 3), 1.5f, 7.5f, "extendedcrafting").withIcon("extendedcrafting:luminessence").withTree("Technology"));


        CompoundTag singularityIconNbt = new CompoundTag();
        singularityIconNbt.putString("Id", "extendedcrafting:gold");
    }

    public static void addFineGrainedTechUnlocks(List<FineGrainedModUnlock> fineGrainedModUnlocks) {
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Pipes!", createCostMap("Quantum Point", 2), 6f, 0f, "").withUnlockSound(SoundEvents.ANVIL_USE).withIcon("pipez:wrench").withBlockItemRestriction("pipez:item_pipe", true).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Item Pipes!", createCostMap("Quantum Point", 4), 7.5f, 0f, "Pipes!").withIcon("pipez:item_pipe").withItemRestriction("pipez:wrench", true).withItemRestriction("pipez:filter_destination_tool", true).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Fluid Pipes!", createCostMap("Quantum Point", 4), 9f, 0f, "Item Pipes!").withIcon("pipez:fluid_pipe").withBlockItemRestriction("pipez:fluid_pipe", true).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Energy Pipes!", createCostMap("Quantum Point", 4), 10.5f, 0f, "Fluid Pipes!").withIcon("pipez:energy_pipe").withBlockItemRestriction("pipez:energy_pipe", true).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Universal Pipes!", createCostMap("Quantum Point", 6), 12f, 0f, "Energy Pipes!").withIcon("pipez:universal_pipe").withBlockItemRestriction("pipez:universal_pipe", true).withTree("Technology"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Better Pipes!", createCostMap("Quantum Point", 3), 7.5f, 1.5f, "Item Pipes!").withIcon("pipez:basic_upgrade").withItemRestriction("pipez:basic_upgrade", true).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Improved Pipes!", createCostMap("Quantum Point", 4), 9f, 1.5f, "Better Pipes!").withIcon("pipez:improved_upgrade").withItemRestriction("pipez:improved_upgrade", true).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Pipes!", createCostMap("Quantum Point", 5), 10.5f, 1.5f, "Improved Pipes!").withIcon("pipez:advanced_upgrade").withItemRestriction("pipez:advanced_upgrade", true).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Perfect Pipes!", createCostMap("Quantum Point", 6), 12f, 1.5f, "Advanced Pipes!").withIcon("pipez:ultimate_upgrade").withItemRestriction("pipez:ultimate_upgrade", true).withTree("Technology"));
        CompoundTag tag = new CompoundTag();
        tag.putString("Id", "ironjetpacks:diamond");


        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Combination Crafting", createCostMap("Quantum Point", 5), 3f, 7.5f, "Extended Crafting").withBlockItemRestriction("extendedcrafting:crafting_core", true).withBlockItemRestriction("extendedcrafting:pedestal", true).withTree("Technology").withIcon("extendedcrafting:crafting_core"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Ender Crafting", createCostMap("Quantum Point", 4), 3f, 9f, "Combination Crafting").withBlockItemRestriction("extendedcrafting:ender_crafter", true).withBlockItemRestriction("extendedcrafting:auto_ender_crafter", true).withBlockItemRestriction("extendedcrafting:ender_alternator", true).withTree("Technology").withIcon("extendedcrafting:ender_crafter"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Flux Crafting", createCostMap("Quantum Point", 6), 3f, 10.5f, "Ender Crafting").withBlockItemRestriction("extendedcrafting:flux_crafter", true).withBlockItemRestriction("extendedcrafting:auto_flux_crafter", true).withBlockItemRestriction("extendedcrafting:flux_alternator", true).withTree("Technology").withIcon("extendedcrafting:flux_crafter"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Quantum Compression", createCostMap("Quantum Point", 6), 3f, 12f, "Flux Crafting").withBlockItemRestriction("extendedcrafting:compressor", true).withTree("Technology").withIcon("extendedcrafting:compressor"));


        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Basic Crafting", createCostMap("Quantum Point", 2), 0f, 7.5f, "Extended Crafting").withBlockItemRestriction("extendedcrafting:basic_table", true).withBlockItemRestriction("extendedcrafting:basic_auto_table", true).withTree("Technology").withIcon("extendedcrafting:basic_table"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Advanced Crafting", createCostMap("Quantum Point", 3), 0f, 9f, "Basic Crafting").withBlockItemRestriction("extendedcrafting:advanced_table", true).withBlockItemRestriction("extendedcrafting:advanced_auto_table", true).withTree("Technology").withIcon("extendedcrafting:advanced_table"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Elite Crafting", createCostMap("Quantum Point", 5), 0f, 10.5f, "Advanced Crafting").withBlockItemRestriction("extendedcrafting:elite_table", true).withBlockItemRestriction("extendedcrafting:elite_auto_table", true).withTree("Technology").withIcon("extendedcrafting:elite_table"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Ultimate Crafting", createCostMap("Quantum Point", 8), 0f, 12f, "Elite Crafting").withBlockItemRestriction("extendedcrafting:ultimate_table", true).withBlockItemRestriction("extendedcrafting:ultimate_auto_table", true).withTree("Technology").withIcon("extendedcrafting:ultimate_table"));


        fineGrainedModUnlocks.add(new FineGrainedModUnlock("A Table On A Stick", createCostMap("Quantum Point", 8), 0f, 6f, "Basic Crafting").withItemRestriction("extendedcrafting:handheld_table", true).withTree("Technology").withIcon("extendedcrafting:handheld_table"));

        CompoundTag singularityIconNbt = new CompoundTag();
        singularityIconNbt.putString("Id", "extendedcrafting:gold");
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Singularities!", createCostMap("Quantum Point", 5), 1.5f, 13.5f, "Quantum Compression", "Ultimate Crafting").withItemRestriction("extendedcrafting:ultimate_singularity", true).withItemRestriction("extendedcrafting:singularity", true).withIcon("extendedcrafting:singularity").withIconNbt(singularityIconNbt).withTree("Technology"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("The Final Singularity!", createCostMap("Quantum Point", 15), 1.5f, 15f, "Singularities!").withItemRestriction("extendedcrafting:ultimate_singularity", true).withIcon("extendedcrafting:ultimate_singularity").withTree("Technology"));
    }

    public static void addMagicUnlocks(List<ModUnlock> modUnlocks) {
        modUnlocks.add(new ModUnlock("Occultism", createCostMap("Bewitched Point", 6), 0, 0, "occultism").withBlockItemOverrideSimple("occultism:silver_ore", false).withBlockItemOverrideSimple("occultism:silver_ore_deepslate", false).withBlockItemOverrideSimple("occultism:silver_block", false).withItemOverrideSimple("occultism:silver_ingot", false).withItemOverrideSimple("occultism:raw_silver", false).withItemOverrideSimple("occultism:silver_nugget", false).withItemOverrideSimple("occultism:silver_dust", false).withIcon("occultism:miner_debug_unspecialized").withTree("Magic"));
        modUnlocks.add(new ModUnlock("Theurgy", createCostMap("Bewitched Point", 1), 1.5f, 0, "theurgy").withIcon("theurgy:divination_rod_t1").withTree("Magic"));
        modUnlocks.add(new ModUnlock("Waystones", createCostMap("Bewitched Point", 3), 3f, 0f, "waystones").withIcon("waystones:mossy_waystone").withTree("Magic"));
    }

    public static void addModResourceUnlocks(List<ModUnlock> modUnlocks) {
        modUnlocks.add(new ModUnlock("Mystical Agriculture", createCostMap("Bewitched Point", "Processed Point", 2, 6), 0, 0, "mysticalagriculture").withIcon("mysticalagriculture:inferium_essence").withTree("Resources & Processing"));
        modUnlocks.add(new ModUnlock("Mystical Agradditions", createCostMap("Bewitched Point", "Processed Point", 1, 4), "Mystical Agriculture", 1.5f, 0, "mysticalagradditions").withIcon("mysticalagradditions:insanium_essence").withTree("Resources & Processing"));
        modUnlocks.add(new ModUnlock("Productive Bees", createCostMap("Processed Point", 4), 3f, 0, "productivebees").withIcon("productivebees:honey_treat").withTree("Resources & Processing"));

        modUnlocks.add(new ModUnlock("Better Furnaces", createCostMap("Processed Point", 3), 0f, 1.5f, "ironfurnaces").withIcon("minecraft:furnace").withTree("Resources & Processing"));
    }

    public static void addFineGrainedResourceUnlocks(List<FineGrainedModUnlock> fineGrainedModUnlocks) {
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Copper Furnaces", createCostMap("Processed Point", "Quantum Point", 4, 3), 1.5f, 1.5f, "Better Furnaces").withBlockItemRestriction("ironfurnaces:copper_furnace", true).withItemRestriction("ironfurnaces:upgrade_copper", true).withIcon("ironfurnaces:copper_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Iron Furnaces", createCostMap("Processed Point", "Quantum Point", 5, 3), 3f, 1.5f, "Copper Furnaces").withBlockItemRestriction("ironfurnaces:iron_furnace", true).withItemRestriction("ironfurnaces:upgrade_iron", true).withItemRestriction("ironfurnaces:upgrade_iron2", true).withIcon("ironfurnaces:iron_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Silver Furnaces", createCostMap("Processed Point", "Quantum Point", 6, 5), 4.5f, 1.5f, "Iron Furnaces").withBlockItemRestriction("ironfurnaces:silver_furnace", true).withItemRestriction("ironfurnaces:upgrade_silver", true).withItemRestriction("ironfurnaces:upgrade_silver2", true).withIcon("ironfurnaces:silver_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Gold Furnaces", createCostMap("Processed Point", "Quantum Point", 6, 6), 6f, 1.5f, "Silver Furnaces").withBlockItemRestriction("ironfurnaces:gold_furnace", true).withItemRestriction("ironfurnaces:upgrade_gold", true).withItemRestriction("ironfurnaces:upgrade_gold2", true).withIcon("ironfurnaces:gold_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Diamond Furnaces", createCostMap("Processed Point", "Quantum Point", 7, 6), 7.5f, 1.5f, "Gold Furnaces").withBlockItemRestriction("ironfurnaces:diamond_furnace", true).withItemRestriction("ironfurnaces:upgrade_diamond", true).withIcon("ironfurnaces:diamond_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Crystal Furnaces", createCostMap("Processed Point", "Quantum Point", 7, 7), 9f, 3f, "Diamond Furnaces").withBlockItemRestriction("ironfurnaces:crystal_furnace", true).withItemRestriction("ironfurnaces:upgrade_crystal", true).withIcon("ironfurnaces:crystal_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Emerald Furnaces", createCostMap("Processed Point", "Quantum Point", 9, 7), 9f, 0f, "Diamond Furnaces").withBlockItemRestriction("ironfurnaces:emerald_furnace", true).withItemRestriction("ironfurnaces:upgrade_emerald", true).withIcon("ironfurnaces:emerald_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Obsidian Furnaces", createCostMap("Processed Point", "Quantum Point", 9, 9), 10.5f, 1.5f, "Emerald Furnaces", "Crystal Furnaces").withBlockItemRestriction("ironfurnaces:obsidian_furnace", true).withItemRestriction("ironfurnaces:upgrade_obsidian", true).withItemRestriction("ironfurnaces:upgrade_obsidian2", true).withIcon("ironfurnaces:obsidian_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Netherite Furnaces", createCostMap("Processed Point", "Quantum Point", 12, 11), 12f, 1.5f, "Obsidian Furnaces").withBlockItemRestriction("ironfurnaces:netherite_furnace", true).withItemRestriction("ironfurnaces:upgrade_netherite", true).withIcon("ironfurnaces:netherite_furnace").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("RAINBOW FURNACES!", createCostMap("Processed Point", "Quantum Point", 15, 15), 13.5f, 1.5f, "Netherite Furnaces").withBlockItemRestriction("ironfurnaces:million_furnace", true).withItemRestriction("ironfurnaces:item_linker", true).withItemRestriction("ironfurnaces:rainbow_core", true).withItemRestriction("ironfurnaces:rainbow_plating", true).withItemRestriction("ironfurnaces:rainbow_coal", true).withIcon("ironfurnaces:million_furnace").withTree("Resources & Processing"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Blasting Upgrades", createCostMap("Processed Point", "Quantum Point", 5, 3), 1.5f, 4.5f, "Iron Furnaces").withItemRestriction("ironfurnaces:augment_blasting", true).withIcon("ironfurnaces:augment_blasting").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Smoking Upgrades", createCostMap("Processed Point", "Quantum Point", 5, 3), 0f, 4.5f, "Iron Furnaces").withItemRestriction("ironfurnaces:augment_smoking", true).withIcon("ironfurnaces:augment_smoking").withTree("Resources & Processing"));

        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Speed Upgrades", createCostMap("Processed Point", "Quantum Point", 3, 5), 3f, 4.5f, "Iron Furnaces").withItemRestriction("ironfurnaces:augment_speed", true).withIcon("ironfurnaces:augment_speed").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Fuel Efficiency Upgrades", createCostMap("Processed Point", "Quantum Point", 4, 5), 4.5f, 4.5f, "Iron Furnaces").withItemRestriction("ironfurnaces:augment_fuel", true).withIcon("ironfurnaces:augment_fuel").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Factory Upgrades", createCostMap("Processed Point", "Quantum Point", 4, 7), 6f, 4.5f, "Iron Furnaces").withItemRestriction("ironfurnaces:augment_factory", true).withIcon("ironfurnaces:augment_factory").withTree("Resources & Processing"));
        fineGrainedModUnlocks.add(new FineGrainedModUnlock("Generator Upgrades", createCostMap("Processed Point", "Quantum Point", 5, 7), 7.5f, 4.5f, "Iron Furnaces").withItemRestriction("ironfurnaces:augment_generator", true).withIcon("ironfurnaces:augment_generator").withTree("Resources & Processing"));
    }

    public static void addMiscUnlocks(List<ModUnlock> modUnlocks) {
        modUnlocks.add(new ModUnlock("Supplementaries", createCostMap("QOL Point", 1), 0, 0, "supplementaries").withIcon("supplementaries:globe").withTree("Misc/QOL"));
        modUnlocks.add(new ModUnlock("Easy Villagers", createCostMap("QOL Point", 2), 1.5f, 0, "easy_villagers").withIcon("easy_villagers:villager").withTree("Misc/QOL"));
        modUnlocks.add(new ModUnlock("Elevators", createCostMap("QOL Point", 1), 3, 0, "elevatorid").withIcon("elevatorid:elevator_lime").withTree("Misc/QOL"));
        modUnlocks.add(new ModUnlock("Torchmaster", createCostMap("QOL Point", 3), 4.5f, 0, "torchmaster").withIcon("torchmaster:megatorch").withTree("Misc/QOL"));
    }
}
