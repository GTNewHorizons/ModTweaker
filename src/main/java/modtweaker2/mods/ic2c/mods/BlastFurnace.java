package modtweaker2.mods.ic2c.mods;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ic2classic.BlastFurnace")
public class BlastFurnace {

    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input) {
        MineTweakerAPI.apply(new Add(InputHelper.toStack(output), InputHelper.toStack(input)));
    }

    private static class Add extends BaseMapAddition<IRecipeInput, RecipeOutput> {

        protected Add(ItemStack output, ItemStack input) {
            super("BlastFurnace", Recipes.blastfurance.getRecipes());
            recipes.put(new RecipeInputItemStack(input), new RecipeOutput(null, output));
        }

        @Override
        protected String getRecipeInfo(Entry<IRecipeInput, RecipeOutput> recipe) {
            return recipe.toString();
        }

    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        LinkedHashMap<IRecipeInput, RecipeOutput> recipes = new LinkedHashMap<IRecipeInput, RecipeOutput>();
        for (Entry<IRecipeInput, RecipeOutput> set : Recipes.blastfurance.getRecipes().entrySet()) {
            for (ItemStack stack : set.getValue().items) {
                if (stack.isItemEqual(InputHelper.toStack(output))) {
                    recipes.put(set.getKey(), set.getValue());
                }
            }
        }
        MineTweakerAPI.apply(new Remove(recipes));
    }

    private static class Remove extends BaseMapRemoval<IRecipeInput, RecipeOutput> {

        protected Remove(Map<IRecipeInput, RecipeOutput> recipes) {
            super("BlastFurnace", Recipes.blastfurance.getRecipes(), recipes);
        }

        @Override
        protected String getRecipeInfo(Entry<IRecipeInput, RecipeOutput> recipe) {
            return recipe.toString();
        }

    }

}
