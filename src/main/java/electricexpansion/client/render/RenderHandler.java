package electricexpansion.client.render;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.client.ClientProxy;
import electricexpansion.client.model.ModelTransformer;
import electricexpansion.client.model.ModelWireMill;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderHandler implements ISimpleBlockRenderingHandler {
    public ModelWireMill wireMill;
    public ModelTransformer transformer;

    public RenderHandler() {
        this.wireMill = new ModelWireMill();
        this.transformer = new ModelTransformer();
    }

    @Override
    public void renderInventoryBlock(final Block block, final int metadata,
            final int modelID,
            final RenderBlocks renderer) {
        GL11.glPushMatrix();
        if (block == ElectricExpansionItems.blockWireMill) {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                    new ResourceLocation("electricexpansion",
                            "textures/models/wiremill.png"));
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.5f, 0.8f, 0.5f);
            GL11.glScalef(1.0f, -1.0f, -1.0f);
            this.wireMill.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
            GL11.glPopMatrix();
        }
        if (block == ElectricExpansionItems.blockTransformer) {
            switch (metadata / 4) {
                case 0: {
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                            new ResourceLocation("electricexpansion",
                                    "textures/models/transformer1.png"));
                    break;
                }
                case 1: {
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                            new ResourceLocation("electricexpansion",
                                    "textures/models/transformer2.png"));
                    break;
                }
                case 2: {
                    FMLClientHandler.instance().getClient().renderEngine.bindTexture(
                            new ResourceLocation("electricexpansion",
                                    "textures/models/transformer3.png"));
                    break;
                }
            }
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.5f, 0.8f, 0.5f);
            GL11.glScalef(1.0f, -1.0f, -1.0f);
            this.transformer.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
            GL11.glPopMatrix();
        }
    }

    @Override
    public boolean renderWorldBlock(final IBlockAccess world, final int x,
            final int y, final int z, final Block block,
            final int modelId,
            final RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.RENDER_ID;
    }
}
