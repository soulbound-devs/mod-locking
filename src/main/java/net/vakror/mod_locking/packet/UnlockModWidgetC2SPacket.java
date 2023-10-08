package net.vakror.mod_locking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.mod_locking.mod.capability.ModTreeCapability;
import net.vakror.mod_locking.mod.capability.ModTreeProvider;
import net.vakror.mod_locking.mod.config.configs.ModPointsConfig;
import net.vakror.mod_locking.mod.config.configs.ModUnlocksConfig;
import net.vakror.mod_locking.mod.unlock.Unlock;
import net.vakror.mod_locking.screen.widget.ModWidget;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class UnlockModWidgetC2SPacket {
    private final String unlockName;

    public UnlockModWidgetC2SPacket(ModWidget widget) {
        this.unlockName = widget.getUnlock().getName();
    }

    public UnlockModWidgetC2SPacket(FriendlyByteBuf buf) {
        this.unlockName = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(unlockName);
    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            for (Unlock<?> unlock : ModUnlocksConfig.INSTANCE.getAll()) {
                if (unlock.getName().equals(unlockName)) {
                    assert context.getSender() != null;
                    ModTreeCapability tree = context.getSender().getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability());
                    if (unlock.canUnlock(tree.getPoints(), tree.getTrees(), true)) {
                        context.getSender().getCapability(ModTreeProvider.MOD_TREE).orElse(new ModTreeCapability()).addUnlockedUnlock(unlock, context.getSender());

                        AtomicReference<Map<String, Integer>> points = new AtomicReference<>(new HashMap<>());
                        ModPointsConfig.INSTANCE.points.forEach((point -> {
                            context.getSender().getCapability(ModTreeProvider.MOD_TREE).ifPresent((modTreeCapability -> {
                                points.set(modTreeCapability.getPoints());
                            }));
                        }));
                        ModPackets.sendToClient(new SyncPlayerPointsS2CPacket(points.get(), null), context.getSender());
                        ModPackets.sendToClient(new SyncModWidgetDescriptionsS2CPacket(), context.getSender());
                    }

                }
            }
        });
        return true;
    }
}

