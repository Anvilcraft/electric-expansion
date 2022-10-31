package electricexpansion.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.containers.ContainerAdvBatteryBox;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.prefab.TranslationHelper;

@SideOnly(Side.CLIENT)
public class GuiAdvancedBatteryBox extends GuiContainer {
    private TileEntityAdvancedBatteryBox tileEntity;
    private int containerWidth;
    private int containerHeight;

    public GuiAdvancedBatteryBox(final InventoryPlayer par1InventoryPlayer,
            final TileEntityAdvancedBatteryBox AdvBatteryBox) {
        super((Container) new ContainerAdvBatteryBox(par1InventoryPlayer,
                AdvBatteryBox));
        this.tileEntity = AdvBatteryBox;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1,
            final int par2) {
        this.fontRendererObj.drawString(
                TranslationHelper.getLocal(this.tileEntity.getInventoryName()), 22, 6,
                4210752);
        final String displayJoules = ElectricityDisplay.getDisplayShort(
                this.tileEntity.getJoules(), ElectricityDisplay.ElectricUnit.JOULES);
        String displayMaxJoules = ElectricityDisplay.getDisplayShort(
                this.tileEntity.getMaxJoules(), ElectricityDisplay.ElectricUnit.JOULES);
        final String displayInputVoltage = ElectricityDisplay.getDisplayShort(
                this.tileEntity.getInputVoltage(),
                ElectricityDisplay.ElectricUnit.VOLTAGE);
        final String displayOutputVoltage = ElectricityDisplay.getDisplayShort(
                this.tileEntity.getVoltage(), ElectricityDisplay.ElectricUnit.VOLTAGE);
        if (this.tileEntity.isDisabled()) {
            displayMaxJoules = "Disabled";
        }
        this.fontRendererObj.drawString(displayJoules + " of",
                73 - displayJoules.length(), 25, 4210752);
        this.fontRendererObj.drawString(displayMaxJoules, 70, 35, 4210752);
        this.fontRendererObj.drawString("Voltage: " + displayOutputVoltage, 65, 55,
                4210752);
        this.fontRendererObj.drawString("Input: " + displayInputVoltage, 65, 65,
                4210752);
        this.fontRendererObj.drawString(
                StatCollector.translateToLocal("container.inventory"), 8,
                this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1,
            final int par2,
            final int par3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(getTexture());
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0,
                this.xSize, this.ySize);
        final int scale = (int) (this.tileEntity.getJoules() /
                this.tileEntity.getMaxJoules() * 72.0);
        this.drawTexturedModalRect(this.containerWidth + 64,
                this.containerHeight + 46, 176, 0, scale, 20);
    }

    public static ResourceLocation getTexture() {
        return new ResourceLocation("electricexpansion",
                "textures/gui/GuiBatBox.png");
    }
}
