package electricexpansion.client.gui;

import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityLogisticsWire;
import electricexpansion.common.helpers.PacketLogisticsWireButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;

public class GuiLogisticsWire extends GuiScreen {
    private TileEntityLogisticsWire tileEntity;
    public final int xSizeOfTexture = 176;
    public final int ySizeOfTexture = 88;

    public GuiLogisticsWire(final TileEntityLogisticsWire LogisticsWire) {
        this.tileEntity = LogisticsWire;
    }

    @Override
    public void drawScreen(final int x, final int y, final float f) {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(getTexture());
        final int field_73880_f = this.width;
        this.getClass();
        final int posX = (field_73880_f - 176) / 2;
        final int field_73881_g = this.height;
        this.getClass();
        final int posY = (field_73881_g - 88) / 2;
        final int n = posX;
        final int n2 = posY;
        final int n3 = 0;
        final int n4 = 0;
        this.getClass();
        final int n5 = 176;
        this.getClass();
        this.drawTexturedModalRect(n, n2, n3, n4, n5, 88);
        final String s = "Logistics Wire";
        final int n6 = posX;
        this.getClass();
        this.fontRendererObj.drawString(s, n6 + 176 / 2 - 35, posY + 4, 4210752);
        super.drawScreen(x, y, f);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        // TODO: WTF
        // PacketDispatcher.sendPacketToServer(PacketManager.getPacket("ElecEx",
        // this.tileEntity, 7, false));
    }

    @Override
    public void actionPerformed(final GuiButton button) {
        boolean status = false;

        switch (button.id) {
            case 0:
                this.tileEntity.buttonStatus0 = !this.tileEntity.buttonStatus0;
                status = this.tileEntity.buttonStatus0;
                break;

            case 1:
                this.tileEntity.buttonStatus1 = !this.tileEntity.buttonStatus1;
                status = this.tileEntity.buttonStatus1;
                break;

            case 2:
                this.tileEntity.buttonStatus2 = !this.tileEntity.buttonStatus2;
                status = this.tileEntity.buttonStatus2;
                break;
        }

        ElectricExpansion.channel.sendToServer(new PacketLogisticsWireButton(
                new Vector3(this.tileEntity), button.id, status));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.buttonList.clear();
        final int field_73880_f = this.width;
        this.getClass();
        final int posX = (field_73880_f - 176) / 2;
        final int field_73881_g = this.height;
        this.getClass();
        final int posY = (field_73881_g - 88) / 2;
        this.buttonList.add(new GuiSwitchButton(0, posX + 13, posY + 15, 150, 16,
                "Output to World",
                this.tileEntity.buttonStatus0));
        this.buttonList.add(new GuiSwitchButton(1, posX + 13, posY + 38, 150, 16,
                "Output to RS Network",
                this.tileEntity.buttonStatus1));
        this.buttonList.add(new GuiSwitchButton(2, posX + 13, posY + 61, 150, 16,
                "Unused",
                this.tileEntity.buttonStatus2));
        if (!this.mc.thePlayer.isEntityAlive() ||
                ((Entity) this.mc.thePlayer).isDead) {
            this.mc.thePlayer.closeScreen();
        }
    }

    public static ResourceLocation getTexture() {
        return new ResourceLocation("electricexpansion",
                "textures/gui/GuiLogistics.png");
    }
}
