package electricexpansion.common.nei;

import electricexpansion.common.misc.WireMillRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.Map;
import electricexpansion.client.gui.GuiWireMill;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.awt.Rectangle;

public class WireMillRecipeHandler extends EEMachineRecipeHandler
{
    @Override
    public String getRecipeName() {
        return "Wire Mill";
    }
    
    @Override
    public String getOverlayIdentifier() {
        return "wireMill";
    }

    @Override
    public String getGuiTexture() {
        return "electricexpansion:textures/gui/GuiEEMachine.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(77, 27, 22, 12), "wireMill", new Object[0]));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiWireMill.class;
    }
    
    @Override
    public double getWattsPerTick() {
        return 500.0;
    }
    
    @Override
    public Map<ItemStack, RecipeOutput> getRecipes() {
        return WireMillRecipes.INSTANCE.getRecipesForNEI();
    }
}
