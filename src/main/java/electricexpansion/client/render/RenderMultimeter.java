package electricexpansion.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.tile.TileEntityMultimeter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.vector.VectorHelper;

@SideOnly(Side.CLIENT)
public class RenderMultimeter extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(final TileEntity var1, final double x,
            final double y, final double z,
            final float var8) {
        final TileEntityMultimeter te = (TileEntityMultimeter) var1;
        final ForgeDirection direction = te.getDirection(
                (IBlockAccess) te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
        for (int side = 0; side < 6; ++side) {
            final ForgeDirection relativeSide = VectorHelper.getOrientationFromSide(
                    direction, ForgeDirection.getOrientation(side));
            if (relativeSide == ForgeDirection.EAST ||
                    relativeSide == ForgeDirection.WEST ||
                    relativeSide == ForgeDirection.UP ||
                    relativeSide == ForgeDirection.DOWN ||
                    relativeSide == ForgeDirection.SOUTH) {
                GL11.glPushMatrix();
                GL11.glPolygonOffset(-10.0f, -10.0f);
                GL11.glEnable(32823);
                final float dx = 0.0625f;
                final float dz = 0.0625f;
                final float displayWidth = 0.875f;
                final float displayHeight = 0.875f;
                GL11.glTranslatef((float) x, (float) y, (float) z);
                switch (side) {
                    case 0: {
                        GL11.glTranslatef(1.0f, 1.0f, 0.0f);
                        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
                        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                        break;
                    }
                    case 3: {
                        GL11.glTranslatef(0.0f, 1.0f, 0.0f);
                        GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
                        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                        break;
                    }
                    case 2: {
                        GL11.glTranslatef(1.0f, 1.0f, 1.0f);
                        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                        break;
                    }
                    case 5: {
                        GL11.glTranslatef(0.0f, 1.0f, 1.0f);
                        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                        break;
                    }
                    case 4: {
                        GL11.glTranslatef(1.0f, 1.0f, 0.0f);
                        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
                        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                        break;
                    }
                }
                GL11.glTranslatef(dx + displayWidth / 2.0f, 1.0f,
                        dz + displayHeight / 2.0f);
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                final FontRenderer fontRenderer = this.func_147498_b();
                int maxWidth = 1;
                final String amperes = ElectricityDisplay.getDisplay(
                        te.electricityReading.amperes,
                        ElectricityDisplay.ElectricUnit.AMPERE);
                final String voltage = ElectricityDisplay.getDisplay(
                        te.electricityReading.voltage,
                        ElectricityDisplay.ElectricUnit.VOLTAGE);
                final String watt = ElectricityDisplay.getDisplay(te.electricityReading.getWatts(),
                        ElectricityDisplay.ElectricUnit.WATT);
                maxWidth = Math.max(fontRenderer.getStringWidth(amperes), maxWidth);
                maxWidth = Math.max(fontRenderer.getStringWidth(voltage), maxWidth);
                maxWidth = Math.max(fontRenderer.getStringWidth(watt), maxWidth);
                maxWidth += 4;
                final int lineHeight = fontRenderer.FONT_HEIGHT + 2;
                final int requiredHeight = lineHeight * 1;
                final float scaleX = displayWidth / maxWidth;
                final float scaleY = displayHeight / requiredHeight;
                final float scale = (float) (Math.min(scaleX, scaleY) * 0.8);
                GL11.glScalef(scale, -scale, scale);
                GL11.glDepthMask(false);
                final int realHeight = (int) Math.floor(displayHeight / scale);
                final int realWidth = (int) Math.floor(displayWidth / scale);
                final int offsetY = (realHeight - requiredHeight) / 2;
                final int offsetX = (realWidth - maxWidth) / 2 + 2 + 5;
                GL11.glDisable(2896);
                fontRenderer.drawString(amperes, offsetX - realWidth / 2,
                        1 + offsetY - realHeight / 2 - 1 * lineHeight,
                        1);
                fontRenderer.drawString(voltage, offsetX - realWidth / 2,
                        1 + offsetY - realHeight / 2 + 0 * lineHeight,
                        1);
                fontRenderer.drawString(watt, offsetX - realWidth / 2,
                        1 + offsetY - realHeight / 2 + 1 * lineHeight,
                        1);
                GL11.glEnable(2896);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDisable(32823);
                GL11.glPopMatrix();
            }
        }
    }
}
