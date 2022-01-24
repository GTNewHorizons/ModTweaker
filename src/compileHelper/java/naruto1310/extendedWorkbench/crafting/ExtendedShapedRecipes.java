package naruto1310.extendedWorkbench.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExtendedShapedRecipes implements IExtendedRecipe {

	public ExtendedShapedRecipes(int width, int height, ItemStack[] recipe, ItemStack stack) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRecipeSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ItemStack getRecipeOutput() {
		throw new UnsupportedOperationException();
	}

}
