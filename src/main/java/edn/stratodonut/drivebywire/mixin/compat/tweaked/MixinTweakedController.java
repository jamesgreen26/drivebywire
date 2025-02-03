package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import edn.stratodonut.drivebywire.mixinducks.TweakedControllerDuck;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TweakedLinkedControllerItem.class)
public class MixinTweakedController implements TweakedControllerDuck {
}
