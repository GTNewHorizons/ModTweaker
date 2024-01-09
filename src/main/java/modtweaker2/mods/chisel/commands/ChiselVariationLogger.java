package modtweaker2.mods.chisel.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.mods.chisel.ChiselHelper;

public class ChiselVariationLogger implements ICommandFunction {

    @Override
    public void execute(String[] arguments, IPlayer player) {
        Map<ICarvingVariation, ICarvingGroup> variations = new HashMap<ICarvingVariation, ICarvingGroup>();
        List<String> keys = CarvingUtils.getChiselRegistry().getSortedGroupNames();
        if (arguments.length > 0) {
            ICarvingGroup group = ChiselHelper.getGroup(arguments[0]);
            if (group == null) {
                MineTweakerAPI.getLogger().logError("Group not found (" + arguments[0] + ")");
                return;
            } else {
                keys.clear();
                keys.add(arguments[0]);
            }
        }
        for (String key : keys) {
            ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(key);
            for (ICarvingVariation variation : group.getVariations()) variations.put(variation, group);
        }
        System.out.println("Chisel Variations: " + variations.size());
        for (Entry<ICarvingVariation, ICarvingGroup> entry : variations.entrySet()) {
            String stringedVariation = "<" + Item.itemRegistry.getNameForObject(
                    Item.getItemFromBlock(entry.getKey().getBlock())) + ":" + entry.getKey().getBlockMeta() + ">";
            if (arguments.length == 0) stringedVariation += " " + entry.getValue().getName();
            System.out.println("Chisel Variation " + stringedVariation);
            MineTweakerAPI.logCommand(stringedVariation);
        }

        if (player != null) {
            player.sendChat(
                    MineTweakerImplementationAPI.platform
                            .getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
