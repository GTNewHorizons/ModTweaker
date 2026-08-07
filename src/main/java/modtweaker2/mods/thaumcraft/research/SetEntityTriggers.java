package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;

public class SetEntityTriggers implements IUndoableAction {

    String key;
    String tab;
    String newTrigger;
    String[] oldTriggers;
    boolean clearTriggers = false;

    public SetEntityTriggers(String res, String entity) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        newTrigger = entity;
    }

    public SetEntityTriggers(String res) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        clearTriggers = true;
    }

    @Override
    public void apply() {
        oldTriggers = ResearchCategories.researchCategories.get(tab).research.get(key).getEntityTriggers();
        if (oldTriggers == null) oldTriggers = new String[0];
        if (!clearTriggers) {
            String[] combinedTriggers = new String[oldTriggers.length + 1];
            System.arraycopy(oldTriggers, 0, combinedTriggers, 0, oldTriggers.length);
            combinedTriggers[oldTriggers.length] = newTrigger;
            ResearchCategories.researchCategories.get(tab).research.get(key).setEntityTriggers(combinedTriggers);
        } else ResearchCategories.researchCategories.get(tab).research.get(key).setEntityTriggers();
    }

    @Override
    public String describe() {
        if (!clearTriggers) return "Adding entity triggers to " + key;
        else return "Clearing all entity triggers for " + key;
    }

    @Override
    public boolean canUndo() {
        return oldTriggers != null;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.get(tab).research.get(key).setEntityTriggers(oldTriggers);
    }

    @Override
    public String describeUndo() {
        return "Restoring entity triggers for " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }
}
