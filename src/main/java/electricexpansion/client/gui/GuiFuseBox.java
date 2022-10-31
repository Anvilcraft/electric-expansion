package electricexpansion.client.gui;

import electricexpansion.api.IItemFuse;
import electricexpansion.common.containers.ContainerFuseBox;
import electricexpansion.common.tile.TileEntityFuseBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.prefab.TranslationHelper;

public class GuiFuseBox extends GuiContainer {
    public final TileEntityFuseBox tileEntity;
    private int containerWidth;
    private int containerHeight;

    public GuiFuseBox(final InventoryPlayer par1InventoryPlayer,
            final TileEntityFuseBox tileEntity) {
        super((Container) new ContainerFuseBox(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float var1,
            final int var2,
            final int var3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(getTexture());
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0,
                this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1,
            final int par2) {
        this.fontRendererObj.drawString(
                TranslationHelper.getLocal(this.tileEntity.getInventoryName()), 8, 6,
                4210752);
        final String displayVoltage = ElectricityDisplay.getDisplayShort(
                this.tileEntity.getVoltage(), ElectricityDisplay.ElectricUnit.VOLTAGE);
        this.fontRendererObj.drawString(
                StatCollector.translateToLocal("container.voltage") + ": " +
                        displayVoltage,
                65, 55, 4210752);
        if (this.tileEntity.getStackInSlot(0) != null) {
            final ItemStack fuseStack = this.tileEntity.getStackInSlot(0);
            final IItemFuse fuse = (IItemFuse) fuseStack.getItem();
            this.fontRendererObj.drawString(
                    this.tileEntity.getStackInSlot(0).getUnlocalizedName(), 30, 18,
                    4210752);
            this.fontRendererObj.drawString(
                    TranslationHelper.getLocal(fuse.getUnlocalizedName(fuseStack)), 30,
                    18, 4210752);
        }
    }

    public static ResourceLocation getTexture() {
        return new ResourceLocation("electricexpansion",
                "textures/gui/GuiFuseBox.png");
    }
}
