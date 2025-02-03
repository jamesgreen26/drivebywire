package edn.stratodonut.drivebywire.client;

import com.simibubi.create.content.redstone.link.controller.LecternControllerBlockEntity;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import edn.stratodonut.drivebywire.WireItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class PonderScenes {
    public static void basicTut(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("wires", "What are cables for?");
        scene.showBasePlate();

        ItemStack wireItem = WireItems.WIRE.asStack();
        BlockPos lever1 = util.grid.at(1,1,1);
        BlockPos lever2 = util.grid.at(1,1,2);
        BlockPos block1 = util.grid.at(1,1,3);
        BlockPos lamp1 = util.grid.at(4,1,1);
        BlockPos lamp2 = util.grid.at(4,1,3);

        scene.overlay.showText(60)
                .independent(20)
                .text("Disclaimer: Cable networks only work on assembled VS2 ships!");
        scene.idle(60);

        scene.world.showSection(util.select.fromTo(lever1, lamp1), Direction.DOWN);
        scene.world.showSection(util.select.fromTo(lever2, lamp2), Direction.DOWN);

        scene.overlay.showSelectionWithText(util.select.position(lever1), 40)
                .text("Connect a redstone source to a block,")
                .colored(PonderPalette.INPUT)
                .pointAt(lever1.getCenter())
                .attachKeyFrame();
        scene.idle(20);

        scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(lever1, Direction.UP), Pointing.DOWN).rightClick()
                .withItem(wireItem), 20);
        scene.idle(20);
        
        scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(lamp1, Direction.WEST), Pointing.LEFT).rightClick()
                .withItem(wireItem), 20);
        scene.idle(20);

        scene.idle(20);

        scene.world.toggleRedstonePower(util.select.fromTo(lever1, lamp1));
        scene.effects.indicateRedstone(lever1);
        scene.idle(5);
        scene.overlay.showText(60)
                .text("And the redstone power is transmitted over cable");
        scene.effects.indicateRedstone(lamp1);
        scene.idle(35);
        scene.world.toggleRedstonePower(util.select.fromTo(lever1, lamp1));
        
        scene.idle(40);

        scene.overlay.showSelectionWithText(util.select.position(block1),40)
                .text("Connect a block to a block,")
                .colored(PonderPalette.INPUT)
                .pointAt(block1.getCenter())
                .attachKeyFrame();
        scene.idle(20);
        
        scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(block1, Direction.UP), Pointing.DOWN).rightClick()
                .withItem(wireItem), 20);
        scene.idle(20);

        scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(lamp2, Direction.WEST), Pointing.LEFT).rightClick()
                .withItem(wireItem), 20);
        scene.idle(20);

        scene.idle(20);

        scene.world.toggleRedstonePower(util.select.fromTo(lever2, lamp2));
        scene.effects.indicateRedstone(lever2);
        scene.overlay.showText(60)
                .text("And adjacent redstone power is transmitted over cable");
        scene.idle(5);
        scene.effects.indicateRedstone(lamp2);
        scene.idle(35);
        scene.world.toggleRedstonePower(util.select.fromTo(lever2, lamp2));

        scene.idle(20);
    }
    
    public static void lecternTut(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("lectern", "Drive-By-Wire");
        scene.showBasePlate();

        ItemStack wireItem = WireItems.WIRE.asStack();
        BlockPos lectern = util.grid.at(0,1,2);
        
        BlockPos lampW = util.grid.at(4,1,2);
        BlockPos lampA = util.grid.at(3,1,1);
        BlockPos lampS = util.grid.at(2,1,2);
        BlockPos lampD = util.grid.at(3,1,3);

        scene.world.showSection(util.select.fromTo(0,1,0, 4,1,4), Direction.DOWN);
        scene.idle(10);
        
        scene.overlay.showText(60)
                .text("Connections from lectern controllers have a special feature,")
                .placeNearTarget()
                .pointAt(lectern.getCenter());
        scene.idle(60);
        
        scene.overlay.showText(80)
                .text("they have multiple channels to transmit over, corresponding to the controller keys.");

        scene.world.toggleRedstonePower(util.select.position(lampW));
        scene.idle(10);
        scene.world.toggleRedstonePower(util.select.position(lampW));
        scene.world.toggleRedstonePower(util.select.position(lampA));
        scene.idle(10);
        scene.world.toggleRedstonePower(util.select.position(lampA));
        scene.world.toggleRedstonePower(util.select.position(lampS));
        scene.idle(10);
        scene.world.toggleRedstonePower(util.select.position(lampS));
        scene.world.toggleRedstonePower(util.select.position(lampD));
        scene.idle(10);
        scene.world.toggleRedstonePower(util.select.position(lampD));

        scene.idle(60);

        scene.overlay.showText(80)
                .text("Select the channel you want using the scrollwheel when holding Cables.")
                .attachKeyFrame();
        scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(lectern, Direction.UP), Pointing.RIGHT).scroll()
                .withItem(wireItem), 60);
        scene.idle(60);
    }

    public static void saveTut(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("save", "Saving Private Ryan");
        scene.showBasePlate();

        Selection ship = util.select.fromTo(1,1,1, 3,2,3);

        BlockPos lamp = util.grid.at(3,2,3);
        BlockPos lever = util.grid.at(3,2,1);
        BlockPos backup = util.grid.at(1,1,1);

        scene.world.showSection(ship, Direction.DOWN);
        scene.overlay.showText(60)
                .text("What if you wanted to save your network to schematic, and reload it?");
        scene.idle(60);
        
        scene.world.toggleRedstonePower(util.select.fromTo(lever, lamp));
        scene.idle(20);
        scene.world.toggleRedstonePower(util.select.fromTo(lever, lamp));
        scene.idle(20);

        scene.overlay.showSelectionWithText(util.select.position(backup), 40)
                .text("Place a Backup Block on your ship before you schematic it,")
                .pointAt(backup.getCenter());
        scene.idle(40);

        scene.overlay.showOutline(PonderPalette.BLUE, "wirePonder", ship, 40);
        scene.idle(40);

        scene.world.hideSection(ship, Direction.UP);
        scene.idle(40);
        
        scene.overlay.showOutline(PonderPalette.BLUE, "wirePonder", ship, 10);
        scene.world.showSection(ship, Direction.UP);
        scene.idle(40);

        scene.overlay.showText(80)
                .text("And all connections will be restored when the placed schematic is shipified.");
        scene.idle(20);
        scene.world.toggleRedstonePower(util.select.fromTo(lever, lamp));
        scene.idle(20);
        scene.world.toggleRedstonePower(util.select.fromTo(lever, lamp));
        scene.idle(20);
    }

    public static void linkTut(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("link", "Reliving Lost Memories");
        scene.showBasePlate();

        ItemStack wireItem = WireItems.WIRE.asStack();
        Selection ship1 = util.select.fromTo(0,1,1, 3,2,1);
        Selection ship2 = util.select.fromTo(0,1,3, 3,2,3);

        BlockPos lever = util.grid.at(3,2,1);
        BlockPos lamp = util.grid.at(3,2,3);
        BlockPos backup1 = util.grid.at(1,1,1);
        BlockPos backup2 = util.grid.at(3,1,3);

        scene.idle(20);
        scene.world.showSection(ship1, Direction.DOWN);
        scene.idle(20);
        scene.world.showSection(ship2, Direction.DOWN);
        scene.idle(20);

        scene.overlay.showText(80)
                .text("What if you wanted to reload networks across ships placed from schematics?");
        scene.idle(80);
        
        scene.world.toggleRedstonePower(util.select.position(lever));
        scene.idle(20);
        scene.world.toggleRedstonePower(util.select.position(lever));
        scene.idle(20);

        scene.overlay.showSelectionWithText(util.select.position(backup1), 40)
                .text("Select the Backup Block on the source ship,")
                .colored(PonderPalette.INPUT);
        scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(backup1, Direction.UP), Pointing.DOWN).rightClick()
                .withItem(wireItem), 20);
        scene.idle(40);

        scene.overlay.showSelectionWithText(util.select.position(backup2), 40)
                .text("and right-click the Backup Block on the target ship.");
        scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(backup2, Direction.UP), Pointing.DOWN).rightClick()
                .withItem(wireItem), 20);
        scene.idle(40);

        scene.overlay.showText(40)
                .text("Tada, connections are automatically reloaded!");
        scene.world.toggleRedstonePower(util.select.fromTo(lever, lamp));
        scene.idle(20);
        scene.world.toggleRedstonePower(util.select.fromTo(lever, lamp));
        scene.idle(20);

        scene.idle(40);
    }
}
