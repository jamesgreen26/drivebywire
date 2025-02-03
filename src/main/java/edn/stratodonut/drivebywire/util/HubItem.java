package edn.stratodonut.drivebywire.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class HubItem {
    public static void putHub(ItemStack itemStack, BlockPos pos) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putLong("Hub", pos.asLong());
        itemStack.setTag(nbt);
    }
    
    public static void ifHubPresent(ItemStack itemStack, Consumer<BlockPos> runnable) {
        if (itemStack.hasTag() && itemStack.getTag().contains("Hub", Tag.TAG_LONG)) {
            runnable.accept(BlockPos.of(itemStack.getTag().getLong("Hub")));
        }
    }
}
