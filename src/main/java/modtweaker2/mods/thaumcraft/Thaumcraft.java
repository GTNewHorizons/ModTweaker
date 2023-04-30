package modtweaker2.mods.thaumcraft;

import java.util.Iterator;
import java.util.Map;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.thaumcraft.aspect.AspectBracketHandler;
import modtweaker2.mods.thaumcraft.handlers.Arcane;
import modtweaker2.mods.thaumcraft.handlers.Aspects;
import modtweaker2.mods.thaumcraft.handlers.Crucible;
import modtweaker2.mods.thaumcraft.handlers.Infusion;
import modtweaker2.mods.thaumcraft.handlers.Loot;
import modtweaker2.mods.thaumcraft.handlers.Research;
import modtweaker2.mods.thaumcraft.handlers.Warp;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import cpw.mods.fml.common.registry.GameRegistry;

public class Thaumcraft {

    private static final Logger TCLOG = LogManager.getLogger("[SCRIPTS TO CODE TRANSLATOR]");
    public static String addingResearch = null;
    public static int setting = 0;
    public static int wasSettingBefore = 0;

    public static void info(String s) {
        if (addingResearch != null) {
            if (setting != 0) TCLOG.info(")");
            TCLOG.info(".registerResearchItem();");
            addingResearch = null;
        }
        TCLOG.info(s);
        addingResearch = null;
        setting = 0;
        wasSettingBefore = 0;
    }

    public static void researchInfo(String r, String s) {
        if (addingResearch == null || !addingResearch.equals(r)) {
            info("ResearchCategories.getResearch(\"" + r + "\")" + s + ";");
            return;
        }
        if (setting != 0) {
            wasSettingBefore |= setting;
            setting = 0;
            TCLOG.info(")");
        }
        TCLOG.info(s);
    }

    public static void addPageInfo(String r, String s) {
        final int setID = 0b1;
        if (addingResearch == null || !addingResearch.equals(r)) {
            info("TCHelper.addResearchPage(\"" + r + "\", " + s + ");");
            return;
        }
        if ((setting & setID) == 0) {
            if ((wasSettingBefore & setID) != 0) {
                info("TCHelper.addResearchPage(\"" + r + "\", " + s + ");");
                return;
            }
            if (setting != 0) {
                wasSettingBefore |= setting;
                setting = 0;
                TCLOG.info(")");
            }
            setting = setID;
            TCLOG.info(".setPages(" + s);
        } else TCLOG.info(", " + s);
    }

    public static void addPrereq(String r, String s, boolean hidden) {
        if (hidden) addPrereqHidden(r, s);
        else addPrereqNormal(r, s);
    }

    public static void addPrereqNormal(String r, String s) {
        final int setID = 0b10;
        if (addingResearch == null || !addingResearch.equals(r)) {
            info("TCHelper.addResearchPrereq(\"" + r + "\", " + s + ", false);");
            return;
        }
        if ((setting & setID) == 0) {
            if ((wasSettingBefore & setID) != 0) {
                info("TCHelper.addResearchPrereq(\"" + addingResearch + "\", " + s + ", false);");
                return;
            }
            if (setting != 0) {
                wasSettingBefore |= setting;
                setting = 0;
                TCLOG.info(")");
            }
            setting = setID;
            TCLOG.info(".setParents(" + s);
        } else TCLOG.info(", " + s);
    }

    public static void addPrereqHidden(String r, String s) {
        final int setID = 0b100;
        if (addingResearch == null || !addingResearch.equals(r)) {
            info("TCHelper.addResearchPrereq(\"" + r + "\", " + s + ", true);");
            return;
        }
        if ((setting & setID) == 0) {
            if ((wasSettingBefore & setID) != 0) {
                info("TCHelper.addResearchPrereq(\"" + addingResearch + "\", " + s + ", true);");
                return;
            }
            if (setting != 0) {
                wasSettingBefore |= setting;
                setting = 0;
                TCLOG.info(")");
            }
            setting = setID;
            TCLOG.info(".setParentsHidden(" + s);
        } else TCLOG.info(", " + s);
    }

    public static void addSibling(String r, String s) {
        final int setID = 0b1000;
        if (addingResearch == null || !addingResearch.equals(r)) {
            info("TCHelper.addResearchSibling(\"" + r + "\", " + s + ");");
            return;
        }
        if ((setting & setID) == 0) {
            if ((wasSettingBefore & setID) != 0) {
                info("TCHelper.addResearchSibling(\"" + addingResearch + "\", " + s + ");");
                return;
            }
            if (setting != 0) {
                wasSettingBefore |= setting;
                setting = 0;
                TCLOG.info(")");
            }
            setting = setID;
            TCLOG.info(".setSiblings(" + s);
        } else TCLOG.info(", " + s);
    }

    public static String convertBlock(Block block) {
        if (block == null) return "null";
        GameRegistry.UniqueIdentifier itemIdentifier = GameRegistry.findUniqueIdentifierFor(block);
        return "GameRegistry.findBlock(\"" + itemIdentifier.modId + "\", \"" + itemIdentifier.name + "\")";
    }

    public static String convertStack(ItemStack stack) {
        if (stack == null) return "null";
        GameRegistry.UniqueIdentifier itemIdentifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        int meta = stack.getItemDamage();
        int size = stack.stackSize;
        NBTTagCompound tagCompound = stack.stackTagCompound;
        if (tagCompound == null || tagCompound.hasNoTags()) {
            return "getModItem(\"" + itemIdentifier.modId
                    + "\", \""
                    + itemIdentifier.name
                    + "\", "
                    + size
                    + ", "
                    + meta
                    + ", missing)";
        } else {
            return "createItemStack(\"" + itemIdentifier.modId
                    + "\", \""
                    + itemIdentifier.name
                    + "\", "
                    + size
                    + ", "
                    + meta
                    + ", "
                    + "\""
                    + tagCompound.toString().replace("\"", "\\\"")
                    + "\""
                    + ", missing)";
        }
    }

    public static String convertStacks(ItemStack[] list) {
        StringBuilder stacks = new StringBuilder("new ItemStack[]{");
        for (ItemStack stack : list) {
            stacks.append(convertStack(stack)).append(", ");
        }
        stacks.append("}");
        return stacks.toString();
    }

    public static String convertAspects(AspectList list) {
        StringBuilder aspects = new StringBuilder("new AspectList()");
        for (Map.Entry<Aspect, Integer> aspect : list.aspects.entrySet()) {
            aspects.append(".add(Aspect.getAspect(\"").append(aspect.getKey().getTag()).append("\"), ")
                    .append(aspect.getValue()).append(")");
        }
        return aspects.toString();
    }

    public static String convertAspectsInLine(AspectList list) {
        StringBuilder aspects = new StringBuilder();
        for (Iterator<Map.Entry<Aspect, Integer>> iterator = list.aspects.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Aspect, Integer> aspect = iterator.next();
            aspects.append("Aspect.getAspect(\"").append(aspect.getKey().getTag()).append("\")");
            if (iterator.hasNext()) aspects.append(", ");
        }
        return aspects.toString();
    }

    public static String convertArray(Object[] arr) {
        StringBuilder arrayString = new StringBuilder("new Object[] {");
        for (Object o : arr) {
            if (o instanceof String) arrayString.append((String) o);
            else if (o instanceof ItemStack) arrayString.append(convertStack((ItemStack) o));
            else arrayString.append(o);
            arrayString.append(", ");
        }
        arrayString.append("}");
        return arrayString.toString();
    }

    public static String convertArray(String[] arr) {
        StringBuilder arrayString = new StringBuilder("new String[] {");
        for (String o : arr) {
            arrayString.append("\"").append(o).append("\"").append(", ");
        }
        arrayString.append("}");
        return arrayString.toString();
    }

    public static String convertArrayInLine(Object[] arr) {
        StringBuilder arrayString = new StringBuilder();
        for (int i = 0, arrLength = arr.length; i < arrLength; i++) {
            Object o = arr[i];
            if (o instanceof String) arrayString.append("\"").append((String) o).append("\"");
            else if (o instanceof Character) arrayString.append("'").append((char) o).append("'");
            else if (o instanceof ItemStack) arrayString.append(convertStack((ItemStack) o));
            else if (o instanceof FluidStack) arrayString.append(convertFluidStack((FluidStack) o));
            else arrayString.append(o);
            if (i + 1 < arrLength) arrayString.append(", ");
        }
        return arrayString.toString();
    }

    public static String convertFluidStack(FluidStack stack) {
        if (stack == null) return "null";
        return "FluidRegistry.getFluidStack(\"" + stack.getFluid().getName() + "\", " + stack.amount + ")";
    }

    public static String convertFluidStack(Fluid stack) {
        if (stack == null) return "null";
        return "FluidRegistry.getFluidStack(\"" + stack.getName() + "\", 1000)";
    }

    public static String convertFluid(Fluid fluid) {
        if (fluid == null) return "null";
        return "FluidRegistry.getFluid(\"" + fluid.getName() + "\")";
    }

    public Thaumcraft() {
        MineTweakerAPI.registerBracketHandler(new AspectBracketHandler());
        MineTweakerAPI.registerClass(Arcane.class); // DONE
        MineTweakerAPI.registerClass(Aspects.class); // DONE
        MineTweakerAPI.registerClass(Crucible.class); // DONE
        MineTweakerAPI.registerClass(Infusion.class); // DONE
        MineTweakerAPI.registerClass(Research.class); // DONE
        MineTweakerAPI.registerClass(Warp.class); // DONE
        MineTweakerAPI.registerClass(Loot.class); // DONE
    }
}
