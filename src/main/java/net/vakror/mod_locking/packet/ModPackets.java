package net.vakror.mod_locking.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vakror.mod_locking.ModLockingMod;

public class ModPackets {
    public static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ModLockingMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncAllDataS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncAllDataS2CPacket::new)
                .encoder(SyncAllDataS2CPacket::encode)
                .consumerNetworkThread(SyncAllDataS2CPacket::handle)
                .add();

        net.messageBuilder(RequestAllDataC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestAllDataC2SPacket::new)
                .encoder(RequestAllDataC2SPacket::encode)
                .consumerNetworkThread(RequestAllDataC2SPacket::handle)
                .add();

        net.messageBuilder(SyncPlayerPointsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncPlayerPointsS2CPacket::new)
                .encoder(SyncPlayerPointsS2CPacket::encode)
                .consumerNetworkThread(SyncPlayerPointsS2CPacket::handle)
                .add();

        net.messageBuilder(RequestPlayerPointsC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestPlayerPointsC2SPacket::new)
                .encoder(RequestPlayerPointsC2SPacket::encode)
                .consumerNetworkThread(RequestPlayerPointsC2SPacket::handle)
                .add();

        net.messageBuilder(UnlockModWidgetC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UnlockModWidgetC2SPacket::new)
                .encoder(UnlockModWidgetC2SPacket::encode)
                .consumerNetworkThread(UnlockModWidgetC2SPacket::handle)
                .add();

        net.messageBuilder(SyncModWidgetDescriptionsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncModWidgetDescriptionsS2CPacket::new)
                .encoder(SyncModWidgetDescriptionsS2CPacket::encode)
                .consumerNetworkThread(SyncModWidgetDescriptionsS2CPacket::handle)
                .add();

        net.messageBuilder(SyncPlayerTreesS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncPlayerTreesS2CPacket::new)
                .encoder(SyncPlayerTreesS2CPacket::encode)
                .consumerNetworkThread(SyncPlayerTreesS2CPacket::handle)
                .add();

        net.messageBuilder(RequestPlayerTreesC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestPlayerTreesC2SPacket::new)
                .encoder(RequestPlayerTreesC2SPacket::encode)
                .consumerNetworkThread(RequestPlayerTreesC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}