package modtweaker2.mods.chisel.handlers;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.chisel.ChiselHelper;
import modtweaker2.mods.thaumcraft.Thaumcraft;

import net.minecraft.item.ItemStack;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

@ZenClass("mods.chisel.Groups")
public class Groups {

    @ZenMethod
    public static void addVariation(String groupName, IItemStack stack) {
        ICarvingGroup group = ChiselHelper.getGroup(groupName);
        ICarvingVariation variation = ChiselHelper.makeVariation(stack);
        if (group == null) {
            MineTweakerAPI.getLogger().logError("Cannot find group " + groupName);
            return;
        }
        if (variation == null) {
            MineTweakerAPI.getLogger().logError("Can't create variation from " + stack);
            return;
        }
        MineTweakerAPI.apply(new AddVariation(group, variation, stack.toString(), stack));
    }

    static class AddVariation implements IUndoableAction {

        ICarvingGroup group;
        ICarvingVariation variation;
        String variationName;
        IItemStack stack;

        public AddVariation(ICarvingGroup group, ICarvingVariation variation, String variationName, IItemStack stack) {
            this.group = group;
            this.variation = variation;
            this.variationName = variationName;
            this.stack = stack;
        }

        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().addVariation(group.getName(), variation);
            Thaumcraft.info(
                    "ChiselHelper.addVariationFromStack(\"" + group.getName()
                            + "\", "
                            + Thaumcraft.convertStack((ItemStack) stack.getInternal())
                            + ");");
        }

        @Override
        public boolean canUndo() {
            return group != null && variation != null;
        }

        @Override
        public String describe() {
            return "Adding Variation: " + variationName;
        }

        @Override
        public String describeUndo() {
            return "Removing Variation: " + variationName;
        }

        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().removeVariation(variation.getBlock(), variation.getBlockMeta());
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeVariation(IItemStack stack) {
        ICarvingVariation variation = ChiselHelper.getVariation(stack);
        if (variation == null) {
            MineTweakerAPI.getLogger().logError("Can't find variation from " + stack);
            return;
        }
        MineTweakerAPI.apply(new RemoveVariation(variation, stack.toString(), stack));
    }

    static class RemoveVariation implements IUndoableAction {

        ICarvingVariation variation;
        String variationName;
        ICarvingGroup group;
        IItemStack stack;

        public RemoveVariation(ICarvingVariation variation, String variationName, IItemStack stack) {
            this.variation = variation;
            this.variationName = variationName;
            this.stack = stack;
        }

        @Override
        public void apply() {
            group = CarvingUtils.getChiselRegistry().getGroup(variation.getBlock(), variation.getBlockMeta());
            CarvingUtils.getChiselRegistry().removeVariation(variation.getBlock(), variation.getBlockMeta());
            Thaumcraft.info(
                    "ChiselHelper.removeVariationStack(" + Thaumcraft.convertStack((ItemStack) stack.getInternal())
                            + ");");
        }

        @Override
        public boolean canUndo() {
            return group != null && variation != null;
        }

        @Override
        public String describe() {
            return "Removing Variation: " + variationName;
        }

        @Override
        public String describeUndo() {
            return "Adding Variation: " + variationName;
        }

        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().addVariation(group.getName(), variation);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void addGroup(String groupName) {
        ICarvingGroup group = ChiselHelper.getGroup(groupName);
        if (group != null) {
            MineTweakerAPI.getLogger().logError("Group already exists " + groupName);
            return;
        }
        group = ChiselHelper.makeGroup(groupName);
        MineTweakerAPI.apply(new AddGroup(group));
    }

    static class AddGroup implements IUndoableAction {

        ICarvingGroup group;

        public AddGroup(ICarvingGroup group) {
            this.group = group;
        }

        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().addGroup(group);
            Thaumcraft.info("ChiselHelper.addGroup(\"" + group.getName() + "\");");
        }

        @Override
        public boolean canUndo() {
            return group != null;
        }

        @Override
        public String describe() {
            return "Adding Group: " + group.getName();
        }

        @Override
        public String describeUndo() {
            return "Removing Group: " + group.getName();
        }

        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().removeGroup(group.getName());
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeGroup(String groupName) {
        ICarvingGroup group = ChiselHelper.getGroup(groupName);
        if (group == null) {
            MineTweakerAPI.getLogger().logError("Could not find group " + groupName);
            return;
        }
        MineTweakerAPI.apply(new RemoveGroup(group));
    }

    static class RemoveGroup implements IUndoableAction {

        ICarvingGroup group;

        public RemoveGroup(ICarvingGroup group) {
            this.group = group;
        }

        @Override
        public void apply() {
            CarvingUtils.getChiselRegistry().removeGroup(group.getName());
            Thaumcraft.info("CarvingUtils.getChiselRegistry().removeGroup(\"" + group.getName() + "\");");
        }

        @Override
        public boolean canUndo() {
            return group != null;
        }

        @Override
        public String describe() {
            return "Removing Group: " + group.getName();
        }

        @Override
        public String describeUndo() {
            return "Adding Group: " + group.getName();
        }

        @Override
        public void undo() {
            CarvingUtils.getChiselRegistry().addGroup(group);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

}
