package electricexpansion.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.containers.ContainerInsulationMachine;
import electricexpansion.common.tile.TileEntityInsulatingMachine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;

@SideOnly(Side.CLIENT)
public class GuiInsulationMachine extends GuiContainer {
    private TileEntityInsulatingMachine tileEntity;
    private int containerWidth;
    private int containerHeight;

    public GuiInsulationMachine(final InventoryPlayer par1InventoryPlayer,
            final TileEntityInsulatingMachine tileEntity) {
        super((Container) new ContainerInsulationMachine(par1InventoryPlayer,
                tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1,
            final int par2) {
        this.fontRendererObj.drawString("Insulation Refiner", 60, 6, 4210752);
        String displayText = "";
        if (this.tileEntity.isDisabled()) {
            displayText = "Disabled!";
        } else if (this.tileEntity.getProcessTimeLeft() > 0) {
            displayText = "Working";
        } else {
            displayText = "Idle";
        }
        this.fontRendererObj.drawString("Status: " + displayText, 82, 45, 4210752);
        this.fontRendererObj.drawString(
                "Voltage: " + ElectricityDisplay.getDisplayShort(
                        this.tileEntity.getVoltage(),
                        ElectricityDisplay.ElectricUnit.VOLTAGE),
                82, 56, 4210752);
        final StringBuilder append = new StringBuilder().append("Require: ");
        this.fontRendererObj.drawString(
                append
                        .append(ElectricityDisplay.getDisplayShort(
                                500.0 * 20.0, ElectricityDisplay.ElectricUnit.WATT))
                        .toString(),
                82, 68, 4210752);
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
        if (this.tileEntity.getProcessTimeLeft() >= 0) {
            final int scale = (int) (this.tileEntity.getProcessTimeLeft() /
                    (double) this.tileEntity.getProcessingTime() * 23.0);
            this.drawTexturedModalRect(this.containerWidth + 77,
                    this.containerHeight + 27, 176, 0, 23 - scale,
                    13);
        }
        if (this.tileEntity.getJoules() >= 0.0) {
            final int scale = (int) (this.tileEntity.getJoules() /
                    this.tileEntity.getMaxJoules() * 50.0);
            this.drawTexturedModalRect(this.containerWidth + 35,
                    this.containerHeight + 20, 176, 13, 4,
                    50 - scale);
        }
    }

    public static ResourceLocation getTexture() {
        return new ResourceLocation("electricexpansion",
                "textures/gui/GuiEEMachine.png");
    }
}
