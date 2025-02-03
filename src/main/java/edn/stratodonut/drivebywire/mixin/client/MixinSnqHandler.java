package edn.stratodonut.drivebywire.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.schematics.client.SchematicAndQuillHandler;
import com.simibubi.create.foundation.utility.LangBuilder;
import edn.stratodonut.drivebywire.WirePackets;
import edn.stratodonut.drivebywire.network.WireNetworkRequestSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(SchematicAndQuillHandler.class)
public abstract class MixinSnqHandler {
    @Shadow public BlockPos firstPos;

    @WrapOperation(
            method = "onMouseInput",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/LangBuilder;sendStatus(Lnet/minecraft/world/entity/player/Player;)V",
                    ordinal = 2
            ),
            remap = false
    )
    private void mixinFirstPos(LangBuilder instance, Player player, Operation<Void> original) {
        Ship s = VSGameUtilsKt.getShipManagingPos(Minecraft.getInstance().player.level(), firstPos);
        if (s != null)
            WirePackets.getChannel().sendToServer(new WireNetworkRequestSyncPacket(s.getId()));
    }
}
