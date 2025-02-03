package edn.stratodonut.drivebywire;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import edn.stratodonut.drivebywire.blocks.WireNetworkBackupBlockEntity;

import static edn.stratodonut.drivebywire.DriveByWireMod.REGISTRATE;

public class WireBlockEntities {
    public static final BlockEntityEntry<WireNetworkBackupBlockEntity> BACKUP_BLOCK_ENTITY = REGISTRATE
            .blockEntity("backup_block", WireNetworkBackupBlockEntity::new)
            .validBlocks(WireBlocks.BACKUP_BLOCK)
            .register();

    public static void register() {}
}
