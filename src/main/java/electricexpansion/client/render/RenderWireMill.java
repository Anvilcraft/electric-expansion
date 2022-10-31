package electricexpansion.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelWireMill;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWireMill extends TileEntitySpecialRenderer {
    private ModelWireMill model;

    public RenderWireMill() {
        this.model = new ModelWireMill();
    }

    @Override
    public void renderTileEntityAt(final TileEntity var1, final double var2,
            final double var3, final double var4,
            final float var5) {
        this.bindTexture(new ResourceLocation("electricexpansion", "textures/models/wiremill.png"));
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2 + 0.5f, (float) var3 + 1.5f,
                (float) var4 + 0.5f);
        switch (var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord,
                var1.zCoord)) {
            case 0: {
                GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 1: {
                GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 2: {
                GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 3: {
                GL11.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
        }
        GL11.glScalef(1.0f, -1.0f, -1.0f);
        this.model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }
}
