package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.Thaumcraft;

import net.minecraft.item.ItemStack;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class AddResearch implements IUndoableAction {

    String key;
    String tab;
    ResearchItem research;
    ResearchItem oldResearch;
    ItemStack[] itemTriggers;
    String[] entityTriggers;
    AspectList aspectTriggers;

    public AddResearch(ResearchItem res, ItemStack[] itemTriggers, String[] entityTriggers, AspectList aspectTriggers) {
        research = res;
        tab = research.category;
        key = research.key;
        this.itemTriggers = itemTriggers;
        this.entityTriggers = entityTriggers;
        this.aspectTriggers = aspectTriggers;
    }

    @Override
    public void apply() {
        oldResearch = ResearchCategories.getResearch(research.key);
        String a = "new ResearchItem(\"" + research.key
                + "\", \""
                + research.category
                + "\", "
                + Thaumcraft.convertAspects(research.tags)
                + ", "
                + research.displayColumn
                + ", "
                + research.displayRow
                + ", "
                + research.getComplexity()
                + ", "
                + Thaumcraft.convertStack(research.icon_item)
                + ")";
        if (itemTriggers != null) {
            research = research.setItemTriggers(itemTriggers);
            a += ".setItemTriggers(" + Thaumcraft.convertStacks(itemTriggers) + ")";
        }
        if (entityTriggers != null) {
            research = research.setEntityTriggers(entityTriggers);
            a += ".setEntityTriggers(" + Thaumcraft.convertArray(entityTriggers) + ")";
        }
        if (aspectTriggers != null) {
            research = research.setAspectTriggers(aspectTriggers.getAspects());
            a += ".setAspectTriggers(" + Thaumcraft.convertAspectsInLine(aspectTriggers) + ")";
        }
        research.registerResearchItem();

        Thaumcraft.info(a);
        Thaumcraft.addingResearch = research.key;
    }

    @Override
    public String describe() {
        return "Registering Research: " + key;
    }

    @Override
    public boolean canUndo() {
        return tab != null && key != null;
    }

    @Override
    public void undo() {
        if (oldResearch == null) ResearchCategories.researchCategories.get(tab).research.remove(key);
        else ResearchCategories.researchCategories.get(tab).research.put(key, oldResearch);
    }

    @Override
    public String describeUndo() {
        return "Removing Research: " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
