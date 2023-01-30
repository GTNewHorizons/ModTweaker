package modtweaker2.mods.tconstruct.commands;

import static modtweaker2.helpers.LogHelper.logPrinted;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.mods.tconstruct.TConstructHelper;
import tconstruct.library.modifier.ItemModifier;

public class ModifierLogger implements ICommandFunction {

    @Override
    public void execute(String[] arguments, IPlayer player) {
        MineTweakerAPI.logCommand(TConstructHelper.modifiers.size() + " Tinker's Construct modifiers:");
        for (ItemModifier modifier : TConstructHelper.modifiers) {
            if (!modifier.key.equals("")) MineTweakerAPI.logCommand(modifier.key);
        }
        logPrinted(player);
    }
}
