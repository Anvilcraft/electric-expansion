package electricexpansion.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.containers.ContainerWireMill;
import electricexpansion.common.tile.TileEntityWireMill;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import universalelectricity.api.energy.UnitDisplay;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiWireMill extends GuiContainer {
    private TileEntityWireMill tileEntity;
    private int containerWidth;
    private int containerHeight;

    public GuiWireMill(final InventoryPlayer par1InventoryPlayer,
            final TileEntityWireMill tileEntity) {
        super((Container) new ContainerWireMill(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1,
            final int par2) {
        this.fontRendererObj.drawString("Wire Mill", 60, 6, 4210752);
        String displayText = "";
        if (this.tileEntity.isDisabled()) {
            displayText = "Disabled!";
        } else if (this.tileEntity.getDrawingTimeLeft() > 0) {
            displayText = "Working";
        } else {
            displayText = "Idle";
        }
        this.fontRendererObj.drawString("Status: " + displayText, 82, 45, 4210752);
        this.fontRendererObj.drawString(
                "Voltage: " + UnitDisplay.getDisplayShort(
                        this.tileEntity.getVoltage(),
                        UnitDisplay.Unit.VOLTAGE),
                82, 56, 4210752);
        final FontRenderer fontRendererObj = this.fontRendererObj;
        final StringBuilder append = new StringBuilder().append("Require: ");
        fontRendererObj.drawString(
                append
                        .append(UnitDisplay.getDisplayShort(
                                500.0 * 20.0, UnitDisplay.Unit.WATT))
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
        if (this.tileEntity.getDrawingTimeLeft() > 0) {
            final int scale = (int) (this.tileEntity.getDrawingTimeLeft() /
                    (double) this.tileEntity.getDrawingTime() * 23.0);
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
