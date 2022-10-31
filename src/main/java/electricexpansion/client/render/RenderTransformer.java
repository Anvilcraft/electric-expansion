package electricexpansion.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelTransformer;
import electricexpansion.common.tile.TileEntityTransformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.vector.Vector3;

@SideOnly(Side.CLIENT)
public class RenderTransformer extends TileEntitySpecialRenderer {
    private ModelTransformer model;
    private String textureToUse;

    public RenderTransformer() {
        this.model = new ModelTransformer();
    }

    @Override
    public void renderTileEntityAt(final TileEntity tileEntity, final double x,
            final double y, final double z,
            final float var5) {
        this.textureToUse = "textures/models/";
        final String status = ((TileEntityTransformer) tileEntity).stepUp ? "Step Up" : "Step Down";
        final EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().thePlayer;
        final MovingObjectPosition movingPosition = player.rayTrace(5.0, 1.0f);
        if (movingPosition != null &&
                new Vector3(tileEntity).equals(new Vector3(movingPosition))) {
            RenderFloatingText.renderFloatingText(status, (float) ((float) x + 0.5),
                    (float) y - 1.0f,
                    (float) ((float) z + 0.5));
        }
        final int metadata = tileEntity.getWorldObj().getBlockMetadata(
                tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        switch (metadata) {
            case 0:
            case 1:
            case 2:
            case 3:
                this.textureToUse += "transformer1.png";
                break;

            case 4:
            case 5:
            case 6:
            case 7:
                // case 8:
                this.textureToUse += "transformer2.png";
                break;

            case 8:
            case 9:
            case 10:
            case 11:
                // case 12:
                this.textureToUse += "transformer3.png";
                break;
        }
        this.bindTexture(
                new ResourceLocation("electricexpansion", this.textureToUse));
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        switch (metadata % 4) {
            case 0: {
                GL11.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 1: {
                GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 2: {
                GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 3: {
                GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
        }
        GL11.glScalef(1.0f, -1.0f, -1.0f);
        this.model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }
}
