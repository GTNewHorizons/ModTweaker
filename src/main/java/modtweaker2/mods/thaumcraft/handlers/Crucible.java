package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObject;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thaumcraft.Thaumcraft;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;

import net.minecraft.item.ItemStack;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

@ZenClass("mods.thaumcraft.Crucible")
public class Crucible {

    public static final String name = "Thaumcraft Crucible";

    @ZenMethod
    public static void addRecipe(String key, IItemStack result, IIngredient catalyst, String aspects) {
        MineTweakerAPI.apply(
                new Add(
                        new CrucibleRecipe(
                                key,
                                toStack(result),
                                toObject(catalyst),
                                ThaumcraftHelper.parseAspects(aspects)),
                        toObject(catalyst)));
    }

    private static class Add extends BaseListAddition<CrucibleRecipe> {

        Object catalystRaw;

        public Add(CrucibleRecipe recipe, Object catalystRaw) {
            super(Crucible.name, ThaumcraftApi.getCraftingRecipes());
            recipes.add(recipe);
            this.catalystRaw = catalystRaw;
        }

        @Override
        public void apply() {
            super.apply();
            for (CrucibleRecipe a : successful) {
                Thaumcraft.info(
                        "ThaumcraftApi.addCrucibleRecipe(\"" + a.key
                                + "\", "
                                + Thaumcraft.convertStack(a.getRecipeOutput())
                                + ", "
                                + (catalystRaw instanceof String ? catalystRaw
                                        : Thaumcraft.convertStack((ItemStack) catalystRaw))
                                + ", "
                                + Thaumcraft.convertAspects(a.aspects)
                                + ");");
            }
        }

        @Override
        protected String getRecipeInfo(CrucibleRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        List<CrucibleRecipe> recipes = new LinkedList<CrucibleRecipe>();

        for (Object o : ThaumcraftApi.getCraftingRecipes()) {
            if (o instanceof CrucibleRecipe) {
                CrucibleRecipe r = (CrucibleRecipe) o;
                if (r.getRecipeOutput() != null && matches(output, toIItemStack(r.getRecipeOutput()))) {
                    recipes.add(r);
                }
            }
        }

        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(
                    String.format("No %s Recipe found for %s. Command ignored!", Crucible.name, output.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<CrucibleRecipe> {

        public Remove(List<CrucibleRecipe> recipes) {
            super(Crucible.name, ThaumcraftApi.getCraftingRecipes(), recipes);
        }

        @Override
        public void apply() {
            super.apply();
            if (!successful.isEmpty()) {
                for (CrucibleRecipe a : successful) {
                    Thaumcraft.info(
                            "TCHelper.removeCrucibleRecipe(" + Thaumcraft.convertStack(a.getRecipeOutput()) + ");");
                }
            }
        }

        @Override
        protected String getRecipeInfo(CrucibleRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }
}
