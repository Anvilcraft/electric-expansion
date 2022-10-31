package electricexpansion.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

public class RenderFloatingText {
    public static void renderFloatingText(final String text, final float x,
            final float y, final float z) {
        renderFloatingText(text, x, y, z, 16777215);
    }

    public static void renderFloatingText(final String text, final float x,
            final float y, final float z,
            final int color) {
        final RenderManager renderManager = RenderManager.instance;
        final FontRenderer fontRenderer = renderManager.getFontRenderer();
        final float scale = 0.027f;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 0.0f, y + 2.3f, z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        final Tessellator tessellator = Tessellator.instance;
        final int yOffset = 0;
        GL11.glDisable(3553);
        tessellator.startDrawingQuads();
        final int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        tessellator.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.5f);
        tessellator.addVertex((double) (-stringMiddle - 1), (double) (-1 + yOffset),
                0.0);
        tessellator.addVertex((double) (-stringMiddle - 1), (double) (8 + yOffset),
                0.0);
        tessellator.addVertex((double) (stringMiddle + 1), (double) (8 + yOffset),
                0.0);
        tessellator.addVertex((double) (stringMiddle + 1), (double) (-1 + yOffset),
                0.0);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2,
                yOffset, color);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2,
                yOffset, color);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
