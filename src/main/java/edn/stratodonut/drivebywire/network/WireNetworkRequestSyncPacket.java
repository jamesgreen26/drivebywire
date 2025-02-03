package edn.stratodonut.drivebywire.network;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import edn.stratodonut.drivebywire.WirePackets;
import edn.stratodonut.drivebywire.wire.ShipWireNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class WireNetworkRequestSyncPacket extends SimplePacketBase {
    long shipId;

    public WireNetworkRequestSyncPacket() {}
    public WireNetworkRequestSyncPacket(long id) {
        shipId = id;
    }
    public WireNetworkRequestSyncPacket(FriendlyByteBuf buf) {
        shipId = buf.readLong();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(shipId);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            Ship s = VSGameUtilsKt.getAllShips(sender.level()).getById(shipId);
            if (s instanceof ServerShip ss) {
                ShipWireNetworkManager.get(ss).ifPresent(
                        m -> WirePackets.getChannel().send(PacketDistributor.PLAYER.with(() -> sender),
                                        new WireNetworkFullSyncPacket(shipId, m.serialiseToNbt(sender.level(), BlockPos.ZERO, true)))
                );
            }
        });
        return true;
    }
}
