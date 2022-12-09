package electricexpansion.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.containers.ContainerDistribution;
import electricexpansion.common.helpers.PacketUpdateQuantumBatteryBoxFrequency;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.TranslationHelper;

@SideOnly(Side.CLIENT)
public class GuiQuantumBatteryBox extends GuiContainer {
    private TileEntityQuantumBatteryBox tileEntity;
    private GuiTextField textFieldFrequency;
    private int containerWidth;
    private int containerHeight;
    private byte frequency;

    public GuiQuantumBatteryBox(final InventoryPlayer par1InventoryPlayer,
            final TileEntityQuantumBatteryBox tileEntity) {
        super(
                (Container) new ContainerDistribution(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();
        final int var1 = (this.width - this.xSize) / 2;
        final int var2 = (this.height - this.ySize) / 2;
        (this.textFieldFrequency = new GuiTextField(this.fontRendererObj, 6, 45, 49, 13))
                .setMaxStringLength(3);
        this.textFieldFrequency.setText(this.tileEntity.getFrequency() + "");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, var1 + 6, var2 + 60, 50, 20, "Set"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1,
            final int par2) {
        this.textFieldFrequency.drawTextBox();
        final String displayJoules = UnitDisplay.getDisplayShort(
                this.tileEntity.getJoulesForDisplay(new Object[0]),
                UnitDisplay.Unit.JOULES);
        this.fontRendererObj.drawString(
                TranslationHelper.getLocal(this.tileEntity.getInventoryName()), 42, 6,
                4210752);
        this.fontRendererObj.drawString("Current Frequency: " +
                this.tileEntity.getFrequency(),
                10, 20, 4210752);
        this.fontRendererObj.drawString("Current Storage: " + displayJoules, 10, 30,
                4210752);
        if (this.tileEntity.getOwningPlayer() != null) {
            this.fontRendererObj.drawString(
                    "Player: " + this.tileEntity.getOwningPlayer(), 65, 66, 4210752);
        } else {
            this.fontRendererObj.drawString("I have no owner. BUG!", 62, 66, 4210752);
        }
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
        if (this.tileEntity.getJoulesForDisplay(new Object[0]) > 0.0) {
            final int scale = (int) (this.tileEntity.getJoulesForDisplay(new Object[0]) /
                    this.tileEntity.getMaxJoules() * 72.0);
            this.drawTexturedModalRect(this.containerWidth + 70,
                    this.containerHeight + 51, 0, 166, scale, 5);
        }
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        super.mouseClicked(par1, par2, par3);
        this.textFieldFrequency.mouseClicked(par1 - this.containerWidth,
                par2 - this.containerHeight, par3);
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        super.keyTyped(par1, par2);
        if (par2 == 28) {
            ElectricExpansion.channel.sendToServer(
                    new PacketUpdateQuantumBatteryBoxFrequency(
                            new Vector3(this.tileEntity), this.frequency));
        }
        this.textFieldFrequency.textboxKeyTyped(par1, par2);
        try {
            final byte newFrequency = (byte) Math.max(Byte.parseByte(this.textFieldFrequency.getText()), 0);
            this.frequency = newFrequency;
        } catch (final Exception ex) {
        }
    }

    @Override
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0:
                ElectricExpansion.channel.sendToServer(
                        new PacketUpdateQuantumBatteryBoxFrequency(
                                new Vector3(this.tileEntity), this.frequency));
                break;
        }
    }

    @Override
    public void updateScreen() {
        if (!this.textFieldFrequency.isFocused()) {
            this.textFieldFrequency.setText(this.tileEntity.getFrequency() + "");
        }
    }

    public static ResourceLocation getTexture() {
        return new ResourceLocation("electricexpansion",
                "textures/gui/GuiLogistics.png");
    }
}
