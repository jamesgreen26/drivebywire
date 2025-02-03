package edn.stratodonut.drivebywire.network;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import edn.stratodonut.drivebywire.client.ClientWireNetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class WireNetworkFullSyncPacket extends SimplePacketBase {
    long shipId;
    CompoundTag nbt;

    public WireNetworkFullSyncPacket() {}

    public WireNetworkFullSyncPacket(long id, CompoundTag nbt) {
        this.shipId = id;
        this.nbt = nbt;
    }

    public WireNetworkFullSyncPacket(FriendlyByteBuf buffer) {
        this.shipId = buffer.readLong();
        this.nbt = buffer.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(shipId);
        buffer.writeNbt(nbt);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Player p = Minecraft.getInstance().player;
            if (p == null) return;
            ClientWireNetworkHandler.loadFrom(p.level(), shipId, nbt);
        });
        return true;
    }
}
