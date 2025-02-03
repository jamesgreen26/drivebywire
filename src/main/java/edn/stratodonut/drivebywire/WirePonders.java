package edn.stratodonut.drivebywire;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import edn.stratodonut.drivebywire.client.PonderScenes;

public class WirePonders {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(DriveByWireMod.MOD_ID);

    public static void register() {
        HELPER.forComponents(WireItems.WIRE)
                .addStoryBoard("wires", PonderScenes::basicTut)
                .addStoryBoard("lectern", PonderScenes::lecternTut);

        HELPER.forComponents(WireBlocks.BACKUP_BLOCK)
                .addStoryBoard("save", PonderScenes::saveTut)
                .addStoryBoard("link", PonderScenes::linkTut);
    }
}
