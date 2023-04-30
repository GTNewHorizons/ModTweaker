package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thaumcraft.Thaumcraft;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.mods.thaumcraft.recipe.MTInfusionRecipe;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

@ZenClass("mods.thaumcraft.Infusion")
public class Infusion {

    public static final String name = "Thaumcraft Infusion";
    public static final String enchName = name + " Enchantment";

    @ZenMethod
    public static void addRecipe(String key, IItemStack input, IItemStack[] recipe, String aspects, IItemStack result,
            int instability) {

        MineTweakerAPI.apply(
                new Add(
                        new InfusionRecipe(
                                key,
                                toStack(result),
                                instability,
                                ThaumcraftHelper.parseAspects(aspects),
                                toStack(input),
                                toStacks(recipe))));
    }

    // A version that allows you to specify whether the detection should be
    // fuzzy or not
    @ZenMethod
    public static void addRecipe(String key, IItemStack input, IItemStack[] recipe, String aspects, IItemStack result,
            int instability, boolean fuzzyCentre, boolean[] fuzzyRecipe) {
        MineTweakerAPI.apply(
                new Add(
                        new MTInfusionRecipe(
                                key,
                                toStack(result),
                                instability,
                                ThaumcraftHelper.parseAspects(aspects),
                                toStack(input),
                                toStacks(recipe),
                                fuzzyCentre,
                                fuzzyRecipe)));
    }

    @ZenMethod
    public static void addEnchantment(String key, int enchantID, int instability, String aspects, IItemStack[] recipe) {
        MineTweakerAPI.apply(
                new AddEnchant(
                        new InfusionEnchantmentRecipe(
                                key,
                                Enchantment.enchantmentsList[enchantID],
                                instability,
                                ThaumcraftHelper.parseAspects(aspects),
                                toStacks(recipe))));
    }

    private static class Add extends BaseListAddition<InfusionRecipe> {

        public Add(InfusionRecipe recipe) {
            super(Infusion.name, ThaumcraftApi.getCraftingRecipes());
            recipes.add(recipe);
        }

        @Override
        public void apply() {
            super.apply();
            for (InfusionRecipe a : successful) {
                Thaumcraft.info(
                        "ThaumcraftApi.addInfusionCraftingRecipe(\"" + a.getResearch()
                                + "\", "
                                + Thaumcraft.convertStack((ItemStack) a.getRecipeOutput())
                                + ", "
                                + a.getInstability()
                                + ", "
                                + Thaumcraft.convertAspects(a.getAspects())
                                + ", "
                                + Thaumcraft.convertStack(a.getRecipeInput())
                                + ", "
                                + Thaumcraft.convertStacks(a.getComponents())
                                + ");");
            }
        }

        @Override
        protected String getRecipeInfo(InfusionRecipe recipe) {
            Object out = ((InfusionRecipe) recipe).getRecipeOutput();
            if (out instanceof ItemStack) {
                return ((ItemStack) out).getDisplayName();
            } else return "Unknown item";
        }
    }

    private static class AddEnchant extends BaseListAddition<InfusionEnchantmentRecipe> {

        public AddEnchant(InfusionEnchantmentRecipe inp) {
            super(Infusion.enchName, ThaumcraftApi.getCraftingRecipes());
            recipes.add(inp);
        }

        @Override
        public void apply() {
            super.apply();
            for (InfusionEnchantmentRecipe a : successful) {
                Thaumcraft.info(
                        "ThaumcraftApi.addInfusionCraftingRecipe(\"" + a.research
                                + "\", "
                                + "Enchantment.enchantmentsList["
                                + a.enchantment.effectId
                                + "]"
                                + ", "
                                + a.instability
                                + ", "
                                + Thaumcraft.convertAspects(a.getAspects())
                                + ", "
                                + Thaumcraft.convertStacks(a.components)
                                + ");");
            }
        }

        @Override
        protected String getRecipeInfo(InfusionEnchantmentRecipe recipe) {
            return recipe.getEnchantment().getName();
        }
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        List<InfusionRecipe> recipes = new LinkedList<InfusionRecipe>();

        for (Object o : ThaumcraftApi.getCraftingRecipes()) {
            if (o instanceof InfusionRecipe) {
                InfusionRecipe r = (InfusionRecipe) o;
                if (r.getRecipeOutput() != null && r.getRecipeOutput() instanceof ItemStack
                        && matches(output, toIItemStack((ItemStack) r.getRecipeOutput()))) {
                    recipes.add(r);
                }
            }
        }

        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(
                    String.format("No %s Recipe found for %s. Command ignored!", Infusion.name, output.toString()));
        }
    }

    @ZenMethod
    public static void removeEnchant(int id) {
        List<InfusionEnchantmentRecipe> recipes = new LinkedList<InfusionEnchantmentRecipe>();
        Enchantment ench = Enchantment.enchantmentsList[id];

        for (Object recipe : ThaumcraftApi.getCraftingRecipes()) {
            if (recipe instanceof InfusionEnchantmentRecipe) {
                InfusionEnchantmentRecipe enchRecipe = (InfusionEnchantmentRecipe) recipe;
                if (enchRecipe.getEnchantment() == ench) {
                    recipes.add(enchRecipe);
                }
            }
        }

        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveEnchant(recipes));
        } else {
            LogHelper.logWarning(
                    String.format(
                            "No %s Recipe found for %s. Command ignored!",
                            Infusion.enchName + " Enchantment",
                            ench.getName()));
        }
    }

    private static class Remove extends BaseListRemoval<InfusionRecipe> {

        public Remove(List<InfusionRecipe> recipes) {
            super(Infusion.name, ThaumcraftApi.getCraftingRecipes(), recipes);
        }

        @Override
        public void apply() {
            super.apply();
            if (!successful.isEmpty()) {
                for (InfusionRecipe a : successful) {
                    Thaumcraft.info(
                            "TCHelper.removeInfusionRecipe(" + Thaumcraft.convertStack((ItemStack) a.getRecipeOutput())
                                    + ");");
                }
            }
        }

        @Override
        protected String getRecipeInfo(InfusionRecipe recipe) {

            Object o = recipe.getRecipeOutput();

            if (o instanceof ItemStack) {
                return LogHelper.getStackDescription((ItemStack) o);
            } else {
                return "Unknown Item";
            }
        }
    }

    private static class RemoveEnchant extends BaseListRemoval<InfusionEnchantmentRecipe> {

        public RemoveEnchant(List<InfusionEnchantmentRecipe> recipes) {
            super(Infusion.enchName, ThaumcraftApi.getCraftingRecipes(), recipes);
        }

        @Override
        public void apply() {
            super.apply();
            if (!successful.isEmpty()) {
                for (InfusionEnchantmentRecipe a : successful) {
                    Thaumcraft.info("TCHelper.removeInfusionEnchantmentRecipe(" + a.getEnchantment().effectId + ");");
                }
            }
        }

        @Override
        protected String getRecipeInfo(InfusionEnchantmentRecipe recipe) {
            return recipe.getEnchantment().getName();
        }
    }
}
