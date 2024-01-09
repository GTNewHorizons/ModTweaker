package modtweaker2.mods.tconstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import mantle.utils.ItemMetaWrapper;
import modtweaker2.helpers.ReflectionHelper;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.AlloyMix;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.DryingRackRecipes.DryingRecipe;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.library.modifier.ItemModifier;
import tconstruct.library.tools.ToolMaterial;

public class TConstructHelper {

    public static HashMap<String, Integer> mappings = new HashMap<String, Integer>();
    public static List<AlloyMix> alloys = null;
    public static ArrayList<CastingRecipe> basinCasting = null;
    public static ArrayList<CastingRecipe> tableCasting = null;
    public static Map<ItemMetaWrapper, FluidStack> smeltingList = null;
    public static Map<ItemMetaWrapper, Integer> temperatureList = null;
    public static Map<ItemMetaWrapper, ItemStack> renderIndex = null;
    public static Map<Fluid, Integer[]> fuelList = null;
    public static List<ItemModifier> modifiers = null;
    public static List<ItemModifier> modifiers_clone = null;

    static {
        try {
            alloys = tconstruct.library.crafting.Smeltery.getAlloyList();
            smeltingList = tconstruct.library.crafting.Smeltery.getSmeltingList();
            temperatureList = tconstruct.library.crafting.Smeltery.getTemperatureList();
            renderIndex = tconstruct.library.crafting.Smeltery.getRenderIndex();
            basinCasting = TConstructRegistry.getBasinCasting().getCastingRecipes();
            tableCasting = TConstructRegistry.getTableCasting().getCastingRecipes();
            modifiers = ModifyBuilder.instance.itemModifiers;
            modifiers_clone = new ArrayList<ItemModifier>(modifiers);

            for (Map.Entry<Integer, ToolMaterial> entry : TConstructRegistry.toolMaterials.entrySet()) {
                mappings.put(entry.getValue().materialName, entry.getKey());
            }

            fuelList = ReflectionHelper.getObject(tconstruct.library.crafting.Smeltery.instance, "smelteryFuels");
        } catch (Exception e) {}
    }

    private TConstructHelper() {}

    public static int getIDFromString(String material) {
        if (!mappings.containsKey(material)) {
            return -1;
        } else return mappings.get(material);
    }

    // Returns a Drying Recipe, using reflection as the constructor is not visible
    public static DryingRecipe getDryingRecipe(ItemStack input, int time, ItemStack output) {
        return ReflectionHelper.getInstance(
                ReflectionHelper.getConstructor(DryingRecipe.class, ItemStack.class, int.class, ItemStack.class),
                input,
                time,
                output);
    }

}
