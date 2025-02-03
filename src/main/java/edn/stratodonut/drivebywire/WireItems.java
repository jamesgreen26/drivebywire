package edn.stratodonut.drivebywire;

import com.tterrag.registrate.util.entry.ItemEntry;
import edn.stratodonut.drivebywire.items.WireItem;
import edn.stratodonut.drivebywire.items.WireCutterItem;

import static edn.stratodonut.drivebywire.DriveByWireMod.REGISTRATE;

public class WireItems {
    static {
        REGISTRATE.setCreativeTab(WireCreativeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<WireItem> WIRE =
            REGISTRATE.item("wire", WireItem::new)
                    .register();

    public static final ItemEntry<WireCutterItem> WIRE_CUTTER =
            REGISTRATE.item("wire_cutter", WireCutterItem::new)
                    .properties(p -> p.stacksTo(1))
                    .register();

    public static void register() {}
}
