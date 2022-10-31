package electricexpansion.common.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.util.Map;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class EEMachineRecipeHandler extends TemplateRecipeHandler {
    @Override
    public abstract String getRecipeName();

    @Override
    public abstract String getOverlayIdentifier();

    @Override
    public abstract Class<? extends GuiContainer> getGuiClass();

    @Override
    public abstract void loadTransferRects();

    public abstract double getWattsPerTick();

    public abstract Map<ItemStack, RecipeOutput> getRecipes();

    @Override
    public String getGuiTexture() {
        return "electricexpansion:textures/gui/GuiEEMachine.png";
    }

    @Override
    public void loadCraftingRecipes(final String outputId,
            final Object... results) {
        if (outputId.equals(this.getOverlayIdentifier())) {
            boolean woolAdded = false;
            for (final Map.Entry<ItemStack, RecipeOutput> recipe : this.getRecipes().entrySet()) {
                if (recipe.getKey().getItem() == Item.getItemFromBlock(Blocks.wool)) {
                    if (woolAdded) {
                        continue;
                    }
                    this.arecipes.add(
                            new EEMachineRecipe(recipe, this.getWattsPerTick()));
                    woolAdded = true;
                } else {
                    this.arecipes.add(
                            new EEMachineRecipe(recipe, this.getWattsPerTick()));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(final ItemStack result) {
        boolean woolAdded = false;
        for (final Map.Entry<ItemStack, RecipeOutput> recipe : this.getRecipes().entrySet()) {
            final ItemStack item = recipe.getValue().stack;
            if (recipe.getKey().getItem() == Item.getItemFromBlock(Blocks.wool)) {
                if (woolAdded ||
                        !NEIServerUtils.areStacksSameTypeCrafting(item, result)) {
                    continue;
                }
                this.arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
                woolAdded = true;
            } else {
                if (!NEIServerUtils.areStacksSameTypeCrafting(item, result)) {
                    continue;
                }
                this.arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(final ItemStack ingredient) {
        boolean woolAdded = false;
        for (final Map.Entry<ItemStack, RecipeOutput> recipe : this.getRecipes().entrySet()) {
            if (recipe.getKey().getItem() == Item.getItemFromBlock(Blocks.wool)) {
                if (woolAdded || !NEIServerUtils.areStacksSameTypeCrafting(
                        (ItemStack) recipe.getKey(), ingredient)) {
                    continue;
                }
                this.arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
                woolAdded = true;
            } else {
                if (!NEIServerUtils.areStacksSameTypeCrafting(
                        (ItemStack) recipe.getKey(), ingredient)) {
                    continue;
                }
                this.arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
            }
        }
    }

    @Override
    public void drawExtras(final int recipe) {
        this.drawProgressBar(72, 16, 176, 0, 22, 13, 48, 0);
        this.drawProgressBar(30, 9, 176, 13, 4, 10, 48, 1);
        final double energy = ((EEMachineRecipe) this.arecipes.get(recipe)).getEnergy();
        String energyString = "Uses ";
        if (energy >= 2000000.0) {
            energyString = energyString + String.valueOf(energy / 1000000.0) + " MJ";
        } else if (energy >= 2000.0) {
            energyString = energyString + String.valueOf(energy / 1000.0) + " kJ";
        } else {
            energyString = energyString + String.valueOf(energy) + " J";
        }
        GuiDraw.drawStringC(energyString, 115, 42, -1);
    }

    public class EEMachineRecipe extends TemplateRecipeHandler.CachedRecipe {
        private PositionedStack input;
        private PositionedStack output;
        private double energy;

        public double getEnergy() {
            return this.energy;
        }

        @Override
        public PositionedStack getResult() {
            return this.output;
        }

        @Override
        public PositionedStack getIngredient() {
            if (this.input.item.getItem() == Item.getItemFromBlock(Blocks.wool)) {
                final int cycle = EEMachineRecipeHandler.this.cycleticks / 48;
                final PositionedStack stack = this.input.copy();
                stack.item.setItemDamage(cycle % 14);
                return stack;
            }
            return this.input;
        }

        public EEMachineRecipe(final Map.Entry<ItemStack, RecipeOutput> recipe,
                final double wattsPerTick) {
            super();
            this.input = new PositionedStack(recipe.getKey(), 50, 14);
            this.output = new PositionedStack(recipe.getValue().stack,
                    103, 14);
            this.energy = recipe.getValue().processingTime * wattsPerTick;
        }
    }

    public static class RecipeOutput {
        public ItemStack stack;
        public int processingTime;

        public RecipeOutput(ItemStack stack, int processingTime) {
            this.stack = stack;
            this.processingTime = processingTime;
        }
    }
}
