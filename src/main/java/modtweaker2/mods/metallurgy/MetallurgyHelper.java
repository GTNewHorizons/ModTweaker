package modtweaker2.mods.metallurgy;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;

import com.teammetallurgy.metallurgy.recipes.AlloyerRecipes;
import com.teammetallurgy.metallurgy.recipes.AlloyerRecipes.AlloyRecipe;
import com.teammetallurgy.metallurgy.recipes.CrusherRecipes;

import modtweaker2.helpers.ReflectionHelper;

public class MetallurgyHelper {

    public static ArrayList<AlloyRecipe> alloyerRecipes = null;
    public static HashMap<String, ItemStack> crusherMetaList = null;
    public static HashMap<String, ItemStack[]> crusherInputList = null;

    static {
        try {
            alloyerRecipes = ReflectionHelper.getFinalObject(AlloyerRecipes.getInstance(), "recipes");
            crusherMetaList = ReflectionHelper.getFinalObject(CrusherRecipes.getInstance(), "metaList");
            crusherInputList = ReflectionHelper.getFinalObject(CrusherRecipes.getInstance(), "inputList");
        } catch (Exception e) {}
    }

    private MetallurgyHelper() {}

    public static String getCrusherKey(ItemStack input) {
        return input.getUnlocalizedName();
    }

    // Returns a Drying Recipe, using reflection as the constructor is not
    // visible
    public static AlloyRecipe getAlloyRecipe(ItemStack first, ItemStack base, ItemStack result) {
        try {
            // Constructor constructor =
            // AlloyRecipe.class.getDeclaredConstructor(ItemStack.class,
            // ItemStack.class, ItemStack.class);
            // constructor.setAccessible(true);
            // return (AlloyRecipe) constructor.newInstance(first, base,
            // result);
            return AlloyerRecipes.getInstance().new AlloyRecipe(first, base, result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("Failed to instantiate AlloyRecipe");
        }
    }
}
