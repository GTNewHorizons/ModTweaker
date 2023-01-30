package modtweaker2.mods.tconstruct.commands;

import static modtweaker2.helpers.LogHelper.logPrinted;

import java.util.Map;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolMaterial;

public class MaterialLogger implements ICommandFunction {

    @Override
    public void execute(String[] arguments, IPlayer player) {
        MineTweakerAPI.logCommand(TConstructRegistry.toolMaterialStrings.entrySet().size() + " Materials:");
        for (Map.Entry<String, ToolMaterial> entry : TConstructRegistry.toolMaterialStrings.entrySet()) {
            MineTweakerAPI.logCommand(entry.getKey());
        }
        logPrinted(player);
    }
}
