package modtweaker2.commands;

import java.util.Set;

import net.minecraft.entity.EntityList;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.ModTweaker2;

public class EntityMappingLogger implements ICommandFunction {

    @Override
    public void execute(String[] arguments, IPlayer player) {

        @SuppressWarnings("unchecked")
        Set<Integer> keys = EntityList.stringToIDMapping.keySet();

        System.out.println("Mob Keys: " + keys.size());

        for (Integer key : keys) {
            ModTweaker2.logger.info("Mob Key " + EntityList.getStringFromID(key) + " : " + key);
            MineTweakerAPI.logCommand("Mob Key " + EntityList.getStringFromID(key) + " : " + key);

        }

        if (player != null) {
            player.sendChat(
                    MineTweakerImplementationAPI.platform
                            .getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
