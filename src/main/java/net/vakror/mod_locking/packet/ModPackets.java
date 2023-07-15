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

        net.messageBuilder(OpenLockingScreenC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(OpenLockingScreenC2SPacket::new)
                .encoder(OpenLockingScreenC2SPacket::encode)
                .consumerNetworkThread(OpenLockingScreenC2SPacket::handle)
                .add();

        net.messageBuilder(RequestPointUpdateC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestPointUpdateC2SPacket::new)
                .encoder(RequestPointUpdateC2SPacket::encode)
                .consumerNetworkThread(RequestPointUpdateC2SPacket::handle)
                .add();

        net.messageBuilder(SyncPointsS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncPointsS2CPacket::new)
                .encoder(SyncPointsS2CPacket::encode)
                .consumerNetworkThread(SyncPointsS2CPacket::handle)
                .add();

        net.messageBuilder(SyncModTreesS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncModTreesS2CPacket::new)
                .encoder(SyncModTreesS2CPacket::encode)
                .consumerNetworkThread(SyncModTreesS2CPacket::handle)
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