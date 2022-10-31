package electricexpansion.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelRawWire;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderRawWire extends TileEntitySpecialRenderer {
    private ModelRawWire model;

    public RenderRawWire() {
        this.model = new ModelRawWire();
    }

    public void renderAModelAt(final TileEntityRawWire t, final double x,
            final double y, final double z, final float f) {
        String textureToUse = "textures/models/";
        final int meta = t.getBlockMetadata();
        if (meta != -1) {
            if (meta == 0) {
                textureToUse += "RawCopperWire.png";
            } else if (meta == 1) {
                textureToUse += "RawTinWire.png";
            } else if (meta == 2) {
                textureToUse += "RawSilverWire.png";
            } else if (meta == 3) {
                textureToUse += "RawHVWire.png";
            } else if (meta == 4) {
                textureToUse += "RawSCWire.png";
            }
        }
        this.bindTexture(new ResourceLocation("electricexpansion", textureToUse));
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glScalef(1.0f, -1.0f, -1.0f);
        final TileEntityConductorBase tileEntity = t;
        final boolean[] connectedSides = tileEntity.visuallyConnected;
        if (connectedSides[0]) {
            this.model.renderBottom();
        }
        if (connectedSides[1]) {
            this.model.renderTop();
        }
        if (connectedSides[2]) {
            this.model.renderBack();
        }
        if (connectedSides[3]) {
            this.model.renderFront();
        }
        if (connectedSides[4]) {
            this.model.renderLeft();
        }
        if (connectedSides[5]) {
            this.model.renderRight();
        }
        this.model.renderMiddle();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(final TileEntity tileEntity, final double var2,
            final double var4, final double var6,
            final float var8) {
        this.renderAModelAt((TileEntityRawWire) tileEntity, var2, var4, var6, var8);
    }
}
