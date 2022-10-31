package electricexpansion.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSwitchButton extends GuiButton {
    private boolean isActive;

    public GuiSwitchButton(final int par1, final int par2, final int par3,
            final String par4Str, final boolean initState) {
        this(par1, par2, par3, 200, 16, par4Str, initState);
    }

    public GuiSwitchButton(final int par1, final int par2, final int par3,
            final int par4, final int par5, final String par6Str,
            final boolean initState) {
        super(par1, par2, par3, par4, par5, par6Str);
        this.width = 200;
        this.height = 16;
        this.enabled = true;
        // TODO: WTF
        // this.drawButton = true;
        this.id = par1;
        this.xPosition = par2;
        this.yPosition = par3;
        this.width = par4;
        this.height = par5;
        this.displayString = par6Str;
        this.isActive = initState;
    }

    @Override
    public void drawButton(final Minecraft par1Minecraft, final int xpos,
            final int ypos) {
        final FontRenderer fontrenderer = par1Minecraft.fontRenderer;
        par1Minecraft.renderEngine.bindTexture(new ResourceLocation(
                "electricexpansion", "textures/gui/SwitchButton.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        // TODO: WTF
        // field_82253_i
        this.field_146123_n = (xpos >= this.xPosition && ypos >= this.yPosition &&
                xpos < this.xPosition + this.width &&
                ypos < this.yPosition + this.height);
        int var5 = 0;
        if (this.isActive) {
            var5 = 16;
        }
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, var5,
                this.width / 2, this.height);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition,
                200 - this.width / 2, var5, this.width / 2,
                this.height);
        this.mouseDragged(par1Minecraft, xpos, ypos);
        this.drawCenteredString(fontrenderer, this.displayString,
                this.xPosition + this.width / 2,
                this.yPosition + (this.height - 8) / 2,
                this.isActive ? 25600 : 16711680);
    }

    @Override
    public boolean mousePressed(final Minecraft par1Minecraft, final int par2,
            final int par3) {
        if (this.getHoverState(this.field_146123_n) == 2) {
            this.isActive = !this.isActive;
        }
        return this.enabled /* && this.drawButton */ && par2 >= this.xPosition &&
                par3 >= this.yPosition && par2 < this.xPosition + this.width &&
                par3 < this.yPosition + this.height;
    }
}
