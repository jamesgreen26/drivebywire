package edn.stratodonut.drivebywire.network;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.Components;
import edn.stratodonut.drivebywire.blocks.WireNetworkBackupBlockEntity;
import edn.stratodonut.drivebywire.wire.ShipWireNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.Optional;

public class WireLinkNetworksPacket extends SimplePacketBase {
    long start;
    long end;

    public WireLinkNetworksPacket() {}

    public WireLinkNetworksPacket(BlockPos start, BlockPos end) {
        this.start = start.asLong();
        this.end = end.asLong();
    }

    public WireLinkNetworksPacket(FriendlyByteBuf buf) {
        this.start = buf.readLong();
        this.end = buf.readLong();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.start);
        buffer.writeLong(this.end);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;
            if (sender.level().getBlockEntity(BlockPos.of(start)) instanceof WireNetworkBackupBlockEntity backupBlockEntity) {
                CompoundTag nbt = backupBlockEntity.serializeNBT();

                if (!nbt.contains("WireNetwork", Tag.TAG_COMPOUND)) return;
                if (!nbt.getCompound("WireNetwork").contains("Network", Tag.TAG_COMPOUND)) return;

                Ship s1 = VSGameUtilsKt.getShipManagingPos(sender.level(), BlockPos.of(start));
                Ship s2 = VSGameUtilsKt.getShipManagingPos(sender.level(), BlockPos.of(end));
                if (s1 instanceof ServerShip ss1 && s2 instanceof ServerShip ss2) {
                    Optional<ShipWireNetworkManager> m1 = ShipWireNetworkManager.get(ss1);
                    Optional<ShipWireNetworkManager> m2 = ShipWireNetworkManager.get(ss2);

                    if (m1.isPresent() && m2.isPresent()) {
                        nbt = nbt.getCompound("WireNetwork").getCompound("Network");
                        if (!nbt.contains(m2.get().getName(), Tag.TAG_COMPOUND)) {
                            sender.displayClientMessage(Components.literal(
                                    String.format("No backup to load for %s to %s!", m1.get().getName(), m2.get().getName()))
                                    .withStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true
                            );
                            return;
                        }

                        sender.displayClientMessage(Components.literal(String.format("Relinking %s to %s", m1.get().getName(), m2.get().getName())), true);

                        m1.get().linkNetwork(m2.get(), ss2.getId(), nbt.getCompound(m2.get().getName()));
                    }
                }
            }
        });
        return true;
    }
}
