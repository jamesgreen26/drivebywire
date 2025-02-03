package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.ControllerRedstoneOutput;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerAxisPacket;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerButtonPacket;
import edn.stratodonut.drivebywire.compat.LinkedControllerWireServerHandler;
import edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler;
import edn.stratodonut.drivebywire.util.HubItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(TweakedLinkedControllerAxisPacket.class)
public abstract class MixinTweakedControllerAxisPacket {
    @Shadow
    private int axis;

    @Inject(
            method = "handleLectern",
            at = @At("RETURN"),
            remap = false
    )
    private void mixinHandleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern, CallbackInfo ci) {
        ControllerRedstoneOutput output = new ControllerRedstoneOutput();
        output.DecodeAxis(this.axis);
        List<Byte> axisValues = new ArrayList<>(10);

        // Copied directly from tweaked
        for(byte i = 0; i < 10; ++i) {
            byte dt = 0;
            if (i < 8) {
                boolean hasHighBit = (output.axis[i / 2] & 16) != 0;
                if (i % 2 == 1 == hasHighBit) {
                    dt = (byte)(output.axis[i / 2] & 15);
                }
            } else {
                dt = output.axis[i - 4];
            }

            axisValues.add(dt);
        }

        TweakedControllerWireServerHandler.receiveAxis(player.level(), lectern.getBlockPos(), axisValues);
    }

    @Inject(
            method = "handleItem",
            at = @At("RETURN"),
            remap = false
    )
    private void mixinHandleItem(ServerPlayer player, ItemStack heldItem, CallbackInfo ci) {
        HubItem.ifHubPresent(heldItem, pos -> {
            ControllerRedstoneOutput output = new ControllerRedstoneOutput();
            output.DecodeAxis(this.axis);
            List<Byte> axisValues = new ArrayList<>(10);

            // Copied directly from tweaked
            for(byte i = 0; i < 10; ++i) {
                byte dt = 0;
                if (i < 8) {
                    boolean hasHighBit = (output.axis[i / 2] & 16) != 0;
                    if (i % 2 == 1 == hasHighBit) {
                        dt = (byte)(output.axis[i / 2] & 15);
                    }
                } else {
                    dt = output.axis[i - 4];
                }

                axisValues.add(dt);
            }

            TweakedControllerWireServerHandler.receiveAxis(player.level(), pos, axisValues);
        });
    }
}
