package electricexpansion.common.misc;

import electricexpansion.common.RecipeRegistery;
import electricexpansion.common.nei.EEMachineRecipeHandler.RecipeOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class InsulationRecipes {
    public static final InsulationRecipes INSTANCE;
    private HashMap<String, Integer> inputToRecipe;
    private HashMap<Integer, ItemStack> recipeToInput;
    private HashMap<Integer, Integer> recipeToOutput;
    private HashMap<Integer, Integer> recipeToTicks;
    private HashMap<Integer, Integer> recipeToInputQTY;

    private InsulationRecipes() {
        this.inputToRecipe = new HashMap<>();
        this.recipeToInput = new HashMap<>();
        this.recipeToOutput = new HashMap<>();
        this.recipeToTicks = new HashMap<>();
        this.recipeToInputQTY = new HashMap<>();
    }

    public void addProcessing(final ItemStack input, int output, int ticks) {
        try {
            if (ticks <= 0) {
                ticks = 60;
            }
            if (output <= 0) {
                output = 1;
            }
            if (input != null && output > 0 && ticks > 0) {
                final int nextRecipeID = this.recipeToOutput.size();
                this.inputToRecipe.put(stackSizeToOne(input) + "", nextRecipeID);
                this.recipeToInput.put(nextRecipeID, stackSizeToOne(input));
                this.recipeToOutput.put(nextRecipeID, output);
                this.recipeToTicks.put(nextRecipeID, ticks);
                this.recipeToInputQTY.put(nextRecipeID, input.stackSize);
            } else if (input == null) {
                throw new IOException("Error: Input cannot be null.");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void addProcessing(final String input, final int output,
            final int ticks) {
        for (final ItemStack input2 : OreDictionary.getOres(input)) {
            this.addProcessing(input2, output, ticks);
        }
    }

    public int getProcessResult(final ItemStack input) {
        try {
            int recipeID = 0;
            recipeID = this.inputToRecipe.get(stackSizeToOne(input) + "");
            if (input.stackSize >= this.recipeToInputQTY.get(recipeID)) {
                return this.recipeToOutput.get(recipeID);
            }
            return -1;
        } catch (final NullPointerException e) {
            return -1;
        }
    }

    public int getInputQTY(final ItemStack input) {
        try {
            int recipeID = 0;
            recipeID = this.inputToRecipe.get(stackSizeToOne(input) + "");
            if (input.stackSize >= this.recipeToInputQTY.get(recipeID)) {
                return this.recipeToInputQTY.get(recipeID);
            }
            return 0;
        } catch (final NullPointerException e) {
            return 0;
        }
    }

    public Integer getProcessTicks(final ItemStack input) {
        try {
            int recipeID = 0;
            recipeID = this.inputToRecipe.get(stackSizeToOne(input) + "");
            if (input.stackSize >= this.recipeToInputQTY.get(recipeID)) {
                return this.recipeToTicks.get(recipeID);
            }
            return 0;
        } catch (final NullPointerException e) {
            return 0;
        }
    }

    public static ItemStack stackSizeToOne(final ItemStack i) {
        if (i != null) {
            return new ItemStack(i.getItem(), 1, i.getItemDamage());
        }
        return null;
    }

    public static ItemStack stackSizeChange(final ItemStack i, final int j) {
        if (i != null && j + "" != "") {
            return new ItemStack(i.getItem(), j, i.getItemDamage());
        }
        return null;
    }

    public Map<ItemStack, RecipeOutput> getRecipesForNEI() {
        final Map<ItemStack, RecipeOutput> recipes = new HashMap<>();
        for (int i = 0; i < this.recipeToInput.size(); ++i) {
            final ItemStack input = stackSizeChange(this.recipeToInput.get(i),
                    this.recipeToInputQTY.get(i));
            RecipeOutput output = new RecipeOutput(
                    new ItemStack(RecipeRegistery.getInsulationIS().getItem(),
                            this.recipeToOutput.get(i),
                            RecipeRegistery.getInsulationIS().getItemDamage()),
                    this.getProcessTicks(input));

            recipes.put(input, output);
        }
        return recipes;
    }

    static {
        INSTANCE = new InsulationRecipes();
    }
}
