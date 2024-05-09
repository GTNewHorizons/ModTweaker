package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.FluidInput;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.recipe.machines.ThermalEvaporationRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IngredientAny;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StackHelper;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.ThermalEvaporation")
public class ThermalEvaporation {

    public static final String name = "Mekanism Thermal Evaporation";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    @ZenMethod
    public static void addRecipe(ILiquidStack liquidInput, ILiquidStack liquidOutput) {
        if (liquidInput == null || liquidOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }

        ThermalEvaporationRecipe recipe = new ThermalEvaporationRecipe(toFluid(liquidInput), toFluid(liquidOutput));

        MineTweakerAPI.apply(new AddMekanismRecipe(name, Recipe.THERMAL_EVAPORATION_PLANT.get(), recipe));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ZenMethod
    public static void removeRecipe(IIngredient liquidInput, @Optional IIngredient liquidOutput) {
        if (liquidInput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }

        if (liquidOutput == null) liquidOutput = IngredientAny.INSTANCE;

        Map<MachineInput, MachineRecipe> recipes = new HashMap<MachineInput, MachineRecipe>();

        for (Entry<FluidInput, ThermalEvaporationRecipe> entry : ((Map<FluidInput, ThermalEvaporationRecipe>) Recipe.THERMAL_EVAPORATION_PLANT
                .get()).entrySet()) {
            ILiquidStack inputLiquid = InputHelper.toILiquidStack(entry.getKey().ingredient);
            ILiquidStack outputLiquid = InputHelper.toILiquidStack(entry.getValue().recipeOutput.output);

            if (!StackHelper.matches(liquidInput, inputLiquid)) continue;
            if (!StackHelper.matches(liquidOutput, outputLiquid)) continue;

            recipes.put(entry.getKey(), entry.getValue());
        }

        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMekanismRecipe(name, Recipe.THERMAL_EVAPORATION_PLANT.get(), recipes));
        } else {
            LogHelper.logWarning(
                    String.format(
                            "No %s recipe found for %s and %s. Command ignored!",
                            name,
                            liquidInput.toString(),
                            liquidOutput.toString()));
        }
    }
}
