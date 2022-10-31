package electricexpansion.common.nei;

import electricexpansion.common.misc.InsulationRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.Map;
import electricexpansion.client.gui.GuiInsulationMachine;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.awt.Rectangle;

public class InsulatingMachineRecipeHandler extends EEMachineRecipeHandler
{
    @Override
    public String getRecipeName() {
        return "Insulation Refiner";
    }
    
    @Override
    public String getOverlayIdentifier() {
        return "insulation";
    }
    
    @Override
    public String getGuiTexture() {
        return "electricexpansion:textures/gui/GuiEEMachine.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(77, 27, 22, 12), "insulation", new Object[0]));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiInsulationMachine.class;
    }
    
    @Override
    public double getWattsPerTick() {
        return 500.0;
    }
    
    @Override
    public Map<ItemStack, RecipeOutput> getRecipes() {
        return InsulationRecipes.INSTANCE.getRecipesForNEI();
    }
}
