package edn.stratodonut.drivebywire.network;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import edn.stratodonut.drivebywire.WireSounds;
import edn.stratodonut.drivebywire.wire.ShipWireNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class WireRemoveConnectionPacket extends SimplePacketBase {
    long shipId;
    long start;
    long end;
    int dir;
    String channel;

    public WireRemoveConnectionPacket() {}
    public WireRemoveConnectionPacket(long shipId, BlockPos start, BlockPos end, Direction dir, String channel) {
        this.shipId = shipId;
        this.start = start.asLong();
        this.end = end.asLong();
        this.dir = dir.get3DDataValue();
        this.channel = channel;
    }
    public WireRemoveConnectionPacket(FriendlyByteBuf buf) {
        this.shipId = buf.readLong();
        this.start = buf.readLong();
        this.end = buf.readLong();
        this.dir = buf.readInt();
        this.channel = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(shipId);
        buffer.writeLong(start);
        buffer.writeLong(end);
        buffer.writeInt(dir);
        buffer.writeUtf(channel);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;
            Ship s = VSGameUtilsKt.getAllShips(sender.level()).getById(shipId);
//            Ship s1 = VSGameUtilsKt.getShipManagingPos(sender.level(), BlockPos.of(start));
//            Ship s2 = VSGameUtilsKt.getShipManagingPos(sender.level(), BlockPos.of(end));
//            if (!Objects.equals(s, s1)) return; // WTF???
//            if (Objects.equals(s1, s2) && s1 instanceof ServerShip ss) {
            if (s instanceof ServerShip ss) {
                ShipWireNetworkManager.get(ss).ifPresent(m -> {
                    m.removeConnection(sender.level(), BlockPos.of(start), BlockPos.of(end), Direction.from3DDataValue(dir), channel);
                    sender.level().playSound(null, BlockPos.of(end), WireSounds.PLUG_OUT.get(),
                            SoundSource.BLOCKS, 1, 1);
                });
            }
//            } else if (s1 instanceof ServerShip ss1 && s2 instanceof ServerShip ss2) {
//                WireNetworkManager.get(ss1).ifPresent(
//                        m1 -> WireNetworkManager.get(ss2).ifPresent(
//                                m2 -> {
//                                    m1.removeConnection(sender.level(), m2, BlockPos.of(start), BlockPos.of(end), Direction.from3DDataValue(dir), channel);
//                                    m2.removeConnection(sender.level(), m1, BlockPos.of(start), BlockPos.of(end), Direction.from3DDataValue(dir), channel);
//                                }
//                        )
//                );
//            }
        });
        return true;
    }
}
