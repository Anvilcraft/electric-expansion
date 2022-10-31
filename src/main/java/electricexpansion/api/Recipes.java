package electricexpansion.api;

import cpw.mods.fml.common.Loader;
import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.misc.WireMillRecipes;
import net.minecraft.item.ItemStack;

public class Recipes {
  public boolean addInsulationRecipe(final ItemStack input, final int output,
                                     final int ticks) {
    if (Loader.isModLoaded("ElectricExpansion")) {
      try {
        InsulationRecipes.INSTANCE.addProcessing(input, output, ticks);
        return true;
      } catch (final Exception e) {
        return false;
      }
    }
    return false;
  }

  public boolean addInsulationRecipe(final String input, final int output,
                                     final int ticks) {
    if (Loader.isModLoaded("ElectricExpansion")) {
      try {
        InsulationRecipes.INSTANCE.addProcessing(input, output, ticks);
        return true;
      } catch (final Exception e) {
        return false;
      }
    }
    return false;
  }

  public boolean addDrawingRecipe(final ItemStack input, final ItemStack output,
                                  final int ticks) {
    if (Loader.isModLoaded("ElectricExpansion")) {
      try {
        WireMillRecipes.INSTANCE.addProcessing(input, output, ticks);
        return true;
      } catch (final Exception e) {
        return false;
      }
    }
    return false;
  }

  public boolean addDrawingRecipe(final String input, final ItemStack output,
                                  final int ticks) {
    if (Loader.isModLoaded("ElectricExpansion")) {
      try {
        WireMillRecipes.INSTANCE.addProcessing(input, output, ticks);
        return true;
      } catch (final Exception e) {
        return false;
      }
    }
    return false;
  }
}
