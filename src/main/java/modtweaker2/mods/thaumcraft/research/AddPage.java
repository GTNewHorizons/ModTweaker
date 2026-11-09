package modtweaker2.mods.thaumcraft.research;

import static modtweaker2.helpers.StackHelper.areEqual;
import static modtweaker2.mods.thaumcraft.ThaumcraftHelper.getResearchSafe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.ResearchPage.PageType;

public class AddPage implements IUndoableAction {

    String key;
    String tab;
    ResearchPage newPage;
    ResearchPage[] oldPages;
    PageType type;
    List<ItemStack> targets;
    Set<Integer> recipe_numbers;
    Enchantment enchant;

    public AddPage(String res, PageType a, Object b, int[] numArr) {
        if (numArr != null && numArr.length > 0) {
            MineTweakerAPI.logInfo("got recipes number array!");
            recipe_numbers = new HashSet<>(numArr.length);
            for (int num : numArr) recipe_numbers.add(num);
        }
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        type = a;
        if (type == PageType.TEXT) newPage = new ResearchPage((String) b);
        else if (type == PageType.INFUSION_ENCHANTMENT) enchant = (Enchantment) b;
        if (b instanceof List) {
            List list = (List) b;
            if (list.isEmpty()) MineTweakerAPI.getLogger().logError("Got empty list!");
            else if (list.get(0) instanceof IItemStack) {
                List<IItemStack> list2 = (List<IItemStack>) b;
                targets = new ArrayList<>();
                for (IItemStack iitem : list2) {
                    targets.add(InputHelper.toStack(iitem));
                }
            } else MineTweakerAPI.getLogger().logError("Couldn't get List<IItemStack> from your IIngredient!");
        }
    }

    public AddPage(String res, String pagename, String prereq) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        if (!prereq.isEmpty()) newPage = new ResearchPage(prereq, pagename);
    }

    @Override
    public void apply() {
        if (type == PageType.NORMAL_CRAFTING) {
            List<IRecipe> crafts = new ArrayList<>();
            int recipe_number = 0;
            for (Object craft : CraftingManager.getInstance().getRecipeList()) {
                if (craft instanceof IRecipe) {
                    IRecipe theCraft = (IRecipe) craft;
                    ItemStack output = theCraft.getRecipeOutput();
                    if (output != null && targets.stream().anyMatch(target -> areEqual(output, target))) {
                        recipe_number++;
                        if (recipe_numbers == null || recipe_numbers.contains(recipe_number)) crafts.add(theCraft);
                    }
                }
            }
            if (crafts.isEmpty()) {
                MineTweakerAPI.getLogger().logError("Found zero recipes for page!");
            } else if (crafts.size() == 1) {
                newPage = new ResearchPage(crafts.get(0));
            } else {
                IRecipe[] craftsArray = crafts.toArray(new IRecipe[0]);
                newPage = new ResearchPage(craftsArray);
            }
        } else if (type == PageType.ARCANE_CRAFTING) {
            List<IArcaneRecipe> crafts = new ArrayList<>();
            int recipe_number = 0;
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof IArcaneRecipe) {
                    IArcaneRecipe theCraft = (IArcaneRecipe) craft;
                    ItemStack output = theCraft.getRecipeOutput();
                    if (output != null && targets.stream().anyMatch(target -> areEqual(output, target))) {
                        recipe_number++;
                        if (recipe_numbers == null || recipe_numbers.contains(recipe_number)) crafts.add(theCraft);
                    }
                }
            }
            if (crafts.isEmpty()) {
                MineTweakerAPI.getLogger().logError("Found zero recipes for page!");
            } else if (crafts.size() == 1) {
                newPage = new ResearchPage(crafts.get(0));
            } else {
                IArcaneRecipe[] craftsArray = crafts.toArray(new IArcaneRecipe[0]);
                newPage = new ResearchPage(craftsArray);
            }

        } else if (type == PageType.CRUCIBLE_CRAFTING) {
            List<CrucibleRecipe> crafts = new ArrayList<>();
            int recipe_number = 0;
            for (ItemStack item : targets) MineTweakerAPI.getLogger().logInfo(item.getUnlocalizedName() + ", ");
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof CrucibleRecipe) {
                    CrucibleRecipe theCraft = (CrucibleRecipe) craft;
                    ItemStack output = theCraft.getRecipeOutput();
                    if (output != null) {
                        if (targets.stream().anyMatch(target -> areEqual(output, target))) {
                            recipe_number++;
                            if (recipe_numbers == null || recipe_numbers.contains(recipe_number)) crafts.add(theCraft);
                        }
                    }
                }
            }
            if (crafts.isEmpty()) {
                MineTweakerAPI.getLogger().logError("Found zero recipes for page!");
            } else if (crafts.size() == 1) {
                newPage = new ResearchPage(crafts.get(0));
            } else {
                CrucibleRecipe[] craftsArray = crafts.toArray(new CrucibleRecipe[0]);
                newPage = new ResearchPage(craftsArray);
            }
        } else if (type == PageType.INFUSION_CRAFTING) {
            List<InfusionRecipe> crafts = new ArrayList<>();
            int recipe_number = 0;
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof InfusionRecipe) {
                    InfusionRecipe theCraft = (InfusionRecipe) craft;
                    Object output = theCraft.getRecipeOutput();
                    if (output != null && theCraft.getRecipeOutput() instanceof ItemStack) {
                        ItemStack item = (ItemStack) output;
                        if (targets.stream().anyMatch(target -> areEqual(item, target))) {
                            recipe_number++;
                            if (recipe_numbers == null || recipe_numbers.contains(recipe_number)) crafts.add(theCraft);
                        }
                    }
                }
            }
            if (crafts.isEmpty()) {
                MineTweakerAPI.getLogger().logError("Found zero recipes for page!");
            } else if (crafts.size() == 1) {
                newPage = new ResearchPage(crafts.get(0));
            } else {
                InfusionRecipe[] craftsArray = crafts.toArray(new InfusionRecipe[0]);
                newPage = new ResearchPage(craftsArray);
            }
        } else if (type == PageType.INFUSION_ENCHANTMENT) {
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof InfusionEnchantmentRecipe) {
                    InfusionEnchantmentRecipe theCraft = (InfusionEnchantmentRecipe) craft;
                    if (theCraft.getEnchantment() != null && theCraft.getEnchantment() == enchant) {
                        newPage = new ResearchPage(theCraft);
                        break;
                    }
                }
            }
        }
        if (newPage == null) return;
        oldPages = getResearchSafe(tab, key).getPages();
        if (oldPages == null) oldPages = new ResearchPage[0];
        ResearchPage[] newPages = new ResearchPage[oldPages.length + 1];
        System.arraycopy(oldPages, 0, newPages, 0, oldPages.length);
        newPages[oldPages.length] = newPage;
        getResearchSafe(tab, key).setPages(newPages);
    }

    @Override
    public String describe() {
        return "Adding Research Page to " + key;
    }

    @Override
    public boolean canUndo() {
        return oldPages != null;
    }

    @Override
    public void undo() {
        final ResearchItem research = getResearchSafe(tab, key);
        if (research == null) {
            return;
        }

        research.setPages(oldPages);
    }

    @Override
    public String describeUndo() {
        return "Removing Page from " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
