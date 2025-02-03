package edn.stratodonut.drivebywire.items;

import edn.stratodonut.drivebywire.wire.ShipWireNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class WireCutterItem extends Item {
    public WireCutterItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        Level level = p_41427_.getLevel();
        if (!level.isClientSide) {
            BlockPos pos = p_41427_.getClickedPos();
            if (VSGameUtilsKt.getShipManagingPos(level, pos) instanceof ServerShip ss) {
                ShipWireNetworkManager.removeAllFromSource(level, pos);
                level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1, 1);
            }
        }

        return InteractionResult.CONSUME;
    }
}
