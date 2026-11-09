package modtweaker2.mods.thaumcraft.research;

import java.util.List;

import net.minecraft.item.ItemStack;

import minetweaker.IUndoableAction;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;

public class SetItemTriggers implements IUndoableAction {

    String key;
    String tab;
    ItemStack[] newTriggers;
    ItemStack[] oldTriggers;
    boolean clearTriggers = false;

    public SetItemTriggers(String res, List<IItemStack> item) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        newTriggers = InputHelper.toStacks(item.toArray(new IItemStack[0]));
    }

    public SetItemTriggers(String res) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        clearTriggers = true;
    }

    @Override
    public void apply() {
        oldTriggers = ResearchCategories.researchCategories.get(tab).research.get(key).getItemTriggers();
        if (oldTriggers == null) oldTriggers = new ItemStack[0];
        if (!clearTriggers) {
            ItemStack[] combinedTriggers = new ItemStack[oldTriggers.length + newTriggers.length];
            System.arraycopy(oldTriggers, 0, combinedTriggers, 0, oldTriggers.length);
            System.arraycopy(newTriggers, 0, combinedTriggers, oldTriggers.length, newTriggers.length);
            ResearchCategories.researchCategories.get(tab).research.get(key).setItemTriggers(combinedTriggers);
        } else ResearchCategories.researchCategories.get(tab).research.get(key).setItemTriggers();
    }

    @Override
    public String describe() {
        if (!clearTriggers) return "Adding item triggers to " + key;
        else return "Clearing all item triggers for " + key;
    }

    @Override
    public boolean canUndo() {
        return oldTriggers != null;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.get(tab).research.get(key).setItemTriggers(oldTriggers);
    }

    @Override
    public String describeUndo() {
        return "Restoring item triggers for " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }
}
