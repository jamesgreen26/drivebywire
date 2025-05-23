package edn.stratodonut.drivebywire;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import edn.stratodonut.drivebywire.network.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

public enum WirePackets {
    FULL_SYNC(WireNetworkFullSyncPacket.class, WireNetworkFullSyncPacket::new, PLAY_TO_CLIENT),

    CREATE_CONNECTION(WireAddConnectionPacket.class, WireAddConnectionPacket::new, PLAY_TO_SERVER),
    REMOVE_CONNECTION(WireRemoveConnectionPacket.class, WireRemoveConnectionPacket::new, PLAY_TO_SERVER),
    REQUEST_SYNC(WireNetworkRequestSyncPacket.class, WireNetworkRequestSyncPacket::new, PLAY_TO_SERVER),
    LINK_NETWORKS(WireLinkNetworksPacket .class, WireLinkNetworksPacket::new, PLAY_TO_SERVER);

    // DO NOT TOUCH ANYTHING BELOW THIS LINE, THANKS CREATE

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(DriveByWireMod.MOD_ID, "main");
    public static final int NETWORK_VERSION = 3;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static SimpleChannel channel;

    private final PacketType<?> packetType;

    <T extends SimplePacketBase> WirePackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
                                             NetworkDirection direction) {
        packetType = new WirePackets.PacketType<>(type, factory, direction);
    }

    public static void registerPackets() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();

        for (WirePackets packet : values())
            packet.packetType.register();
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                NetworkEvent.Context context = contextSupplier.get();
                if (packet.handle(context)) {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            getChannel().messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumerNetworkThread(handler)
                    .add();
        }
    }
}
