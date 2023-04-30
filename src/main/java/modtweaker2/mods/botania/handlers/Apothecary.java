package modtweaker2.mods.botania.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thaumcraft.Thaumcraft;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

@ZenClass("mods.botania.Apothecary")
public class Apothecary {

    protected static final String name = "Botania Petal";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input) {
        MineTweakerAPI.apply(new Add(new RecipePetals(toStack(output), toObjects(input))));
    }

    @ZenMethod
    public static void addRecipe(String output, IIngredient[] input) {
        addRecipe(toIItemStack(ItemBlockSpecialFlower.ofType(output)), input);
    }

    private static class Add extends BaseListAddition<RecipePetals> {

        public Add(RecipePetals recipe) {
            super("Botania Petal", BotaniaAPI.petalRecipes);
            recipes.add(recipe);
        }

        @Override
        public void apply() {
            super.apply();

            if (!successful.isEmpty()) {
                for (RecipePetals recipe : successful) {
                    Thaumcraft.info(
                            "BotaniaAPI.registerPetalRecipe(" + Thaumcraft.convertStack(recipe.getOutput())
                                    + ", "
                                    + Thaumcraft.convertArrayInLine(recipe.getInputs().toArray(new Object[0]))
                                    + ");");
                }
            }
        }

        @Override
        public String getRecipeInfo(RecipePetals recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        // Get list of existing recipes, matching with parameter
        LinkedList<RecipePetals> result = new LinkedList<RecipePetals>();

        for (RecipePetals entry : BotaniaAPI.petalRecipes) {
            if (entry != null && entry.getOutput() != null && matches(output, toIItemStack(entry.getOutput()))) {
                result.add(entry);
            }
        }

        // Check if we found the recipes and apply the action
        if (!result.isEmpty()) {
            MineTweakerAPI.apply(new Remove(result));
        } else {
            LogHelper.logWarning(
                    String.format("No %s Recipe found for %s. Command ignored!", Apothecary.name, output.toString()));
        }
    }

    @ZenMethod
    public static void removeRecipe(String output) {
        removeRecipe(toIItemStack(ItemBlockSpecialFlower.ofType(output)));
    }

    private static class Remove extends BaseListRemoval<RecipePetals> {

        public Remove(List<RecipePetals> recipes) {
            super(Apothecary.name, BotaniaAPI.petalRecipes, recipes);
        }

        @Override
        public void apply() {
            super.apply();

            if (!successful.isEmpty()) {
                for (RecipePetals recipe : successful) {
                    Thaumcraft.info(
                            "BotaniaHelper.removePetalRecipe(" + Thaumcraft.convertStack(recipe.getOutput()) + ");");
                }
            }
        }

        @Override
        public String getRecipeInfo(RecipePetals recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
}
