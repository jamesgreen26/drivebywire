package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerStopLecternPacket;
import edn.stratodonut.drivebywire.compat.LinkedControllerWireServerHandler;
import edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler;
import edn.stratodonut.drivebywire.util.HubItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TweakedLinkedControllerStopLecternPacket.class)
public class MixinTweakedControllerStopLecternPacket {
    @Inject(
            method = "handleLectern",
            at = @At("RETURN"),
            remap = false
    )
    private void mixinHandleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern, CallbackInfo ci) {
        TweakedControllerWireServerHandler.reset(player.level(), lectern.getBlockPos());
    }

    @Inject(
            method = "handleItem",
            at = @At("RETURN"),
            remap = false
    )
    private void mixinHandleItem(ServerPlayer player, ItemStack heldItem, CallbackInfo ci) {
        HubItem.ifHubPresent(heldItem, pos -> TweakedControllerWireServerHandler.reset(player.level(), pos));
    }
}
