package edn.stratodonut.drivebywire.network;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.Components;
import edn.stratodonut.drivebywire.WireSounds;
import edn.stratodonut.drivebywire.wire.ShipWireNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class WireAddConnectionPacket extends SimplePacketBase {
    long shipId;
    long start;
    long end;
    int dir;
    String channel;

    public WireAddConnectionPacket() {}
    public WireAddConnectionPacket(long shipId, BlockPos start, BlockPos end, Direction dir, String channel) {
        this.shipId = shipId;
        this.start = start.asLong();
        this.end = end.asLong();
        this.dir = dir.get3DDataValue();
        this.channel = channel;
    }
    public WireAddConnectionPacket(FriendlyByteBuf buf) {
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
//        Ship s1 = VSGameUtilsKt.getShipManagingPos(sender.level(), BlockPos.of(start));
//        Ship s2 = VSGameUtilsKt.getShipManagingPos(sender.level(), BlockPos.of(end));
//        if (!Objects.equals(s, s1)) return true; // WTF???
//        if (Objects.equals(s1, s2) && s1 instanceof ServerShip ss) {
            if (s instanceof ServerShip ss) {
                ShipWireNetworkManager m = ShipWireNetworkManager.getOrCreate(ss);
                ShipWireNetworkManager.CONNECTION_RESULT result = m.createConnection(sender.level(), BlockPos.of(start), BlockPos.of(end), Direction.from3DDataValue(dir), channel);
                if (result.isSuccess()) {
                    sender.level().playSound(null, BlockPos.of(end), WireSounds.PLUG_IN.get(),
                            SoundSource.BLOCKS, 1, 1);
                } else {
                    sender.displayClientMessage(Components.literal(result.getDescription()).withStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                }
                // TODO: Connection Error message?
            }
//        } else if (s1 instanceof ServerShip ss1 && s2 instanceof ServerShip ss2) {
//            WireNetworkManager m1 = WireNetworkManager.getOrCreate(ss1);
//            WireNetworkManager m2 = WireNetworkManager.getOrCreate(ss2);
//
//            if (m1.createConnection(sender.level(), m2, BlockPos.of(start), BlockPos.of(end), Direction.from3DDataValue(dir), channel).isSuccess()) {
//                m2.createConnection(sender.level(), m1, BlockPos.of(start), BlockPos.of(end), Direction.from3DDataValue(dir), channel);
//            }
//        }
        });
        return true;
    }
}
