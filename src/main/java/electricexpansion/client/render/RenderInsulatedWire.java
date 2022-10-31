package electricexpansion.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.client.model.ModelInsulatedWire;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderInsulatedWire extends TileEntitySpecialRenderer {
    private static final ModelInsulatedWire model;

    public void renderAModelAt(final TileEntity t, final double x, final double y,
            final double z, final float f) {
        String textureToUse = "textures/models/";
        final Block block = t.getWorldObj().getBlock(t.xCoord, t.yCoord, t.zCoord);
        final int metadata = t.getWorldObj().getBlockMetadata(t.xCoord, t.yCoord, t.zCoord);
        if (metadata != -1) {
            if (block == ElectricExpansionItems.blockInsulatedWire) {
                switch (metadata) {
                    case 0: {
                        textureToUse += "InsulatedCopperWire.png";
                        break;
                    }
                    case 1: {
                        textureToUse += "InsulatedTinWire.png";
                        break;
                    }
                    case 2: {
                        textureToUse += "InsulatedSilverWire.png";
                        break;
                    }
                    case 3: {
                        textureToUse += "InsulatedHVWire.png";
                        break;
                    }
                    case 4: {
                        textureToUse += "InsulatedSCWire.png";
                        break;
                    }
                }
            } else if (block == ElectricExpansionItems.blockLogisticsWire) {
                switch (metadata) {
                    case 0: {
                        textureToUse += "CopperLogisticsWire.png";
                        break;
                    }
                    case 1: {
                        textureToUse += "TinLogisticsWire.png";
                        break;
                    }
                    case 2: {
                        textureToUse += "SilverLogisticsWire.png";
                        break;
                    }
                    case 3: {
                        textureToUse += "HVLogisticsWire.png";
                        break;
                    }
                    case 4: {
                        textureToUse += "SCLogisticsWire.png";
                        break;
                    }
                }
            } else if (block == ElectricExpansionItems.blockSwitchWire) {
                if (t.getWorldObj().isBlockIndirectlyGettingPowered(t.xCoord, t.yCoord,
                        t.zCoord)) {
                    switch (metadata) {
                        case 0: {
                            textureToUse += "CopperSwitchWireOn.png";
                            break;
                        }
                        case 1: {
                            textureToUse += "TinSwitchWireOn.png";
                            break;
                        }
                        case 2: {
                            textureToUse += "SilverSwitchWireOn.png";
                            break;
                        }
                        case 3: {
                            textureToUse += "HVSwitchWireOn.png";
                            break;
                        }
                        case 4: {
                            textureToUse += "SCSwitchWireOn.png";
                            break;
                        }
                    }
                } else {
                    switch (metadata) {
                        case 0: {
                            textureToUse += "CopperSwitchWireOff.png";
                            break;
                        }
                        case 1: {
                            textureToUse += "TinSwitchWireOff.png";
                            break;
                        }
                        case 2: {
                            textureToUse += "SilverSwitchWireOff.png";
                            break;
                        }
                        case 3: {
                            textureToUse += "HVSwitchWireOff.png";
                            break;
                        }
                        case 4: {
                            textureToUse += "SCSwitchWireOff.png";
                            break;
                        }
                    }
                }
            } else if (block == ElectricExpansionItems.blockRedstonePaintedWire) {
                switch (metadata) {
                    case 0: {
                        textureToUse += "CopperRSWire.png";
                        break;
                    }
                    case 1: {
                        textureToUse += "TinRSWire.png";
                        break;
                    }
                    case 2: {
                        textureToUse += "SilverRSWire.png";
                        break;
                    }
                    case 3: {
                        textureToUse += "HVRSWire.png";
                        break;
                    }
                    case 4: {
                        textureToUse += "SCRSWire.png";
                        break;
                    }
                }
            }
        }
        final TileEntityConductorBase tileEntity = (TileEntityConductorBase) t;
        final boolean[] connectedSides = tileEntity.visuallyConnected;
        if (textureToUse != null && textureToUse != "" &&
                !textureToUse.equals("textures/models/")) {
            this.bindTexture(new ResourceLocation("electricexpansion", textureToUse));
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glScalef(1.0f, -1.0f, -1.0f);
        if (tileEntity instanceof TileEntitySwitchWire) {
            if (tileEntity.getWorldObj().isBlockIndirectlyGettingPowered(
                    t.xCoord, t.yCoord, t.zCoord)) {
                if (connectedSides[0]) {
                    RenderInsulatedWire.model.renderBottom();
                }
                if (connectedSides[1]) {
                    RenderInsulatedWire.model.renderTop();
                }
                if (connectedSides[2]) {
                    RenderInsulatedWire.model.renderBack();
                }
                if (connectedSides[3]) {
                    RenderInsulatedWire.model.renderFront();
                }
                if (connectedSides[4]) {
                    RenderInsulatedWire.model.renderLeft();
                }
                if (connectedSides[5]) {
                    RenderInsulatedWire.model.renderRight();
                }
            }
        } else {
            if (connectedSides[0]) {
                RenderInsulatedWire.model.renderBottom();
            }
            if (connectedSides[1]) {
                RenderInsulatedWire.model.renderTop();
            }
            if (connectedSides[2]) {
                RenderInsulatedWire.model.renderBack();
            }
            if (connectedSides[3]) {
                RenderInsulatedWire.model.renderFront();
            }
            if (connectedSides[4]) {
                RenderInsulatedWire.model.renderLeft();
            }
            if (connectedSides[5]) {
                RenderInsulatedWire.model.renderRight();
            }
        }
        RenderInsulatedWire.model.renderMiddle();
        GL11.glPopMatrix();
        if (tileEntity instanceof TileEntityInsulatedWire) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
            GL11.glScalef(1.0f, -1.0f, -1.0f);
            this.bindTexture(new ResourceLocation(
                    "electricexpansion", "textures/models/WirePaintOverlay.png"));
            final byte colorByte = ((TileEntityInsulatedWire) tileEntity).colorByte;
            switch (colorByte) {
                case -1: {
                    GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
                    break;
                }
                case 0: {
                    GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
                    break;
                }
                case 1: {
                    GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
                    break;
                }
                case 2: {
                    GL11.glColor4f(0.0f, 0.2f, 0.0f, 1.0f);
                    break;
                }
                case 3: {
                    GL11.glColor4f(0.2f, 0.0f, 0.0f, 1.0f);
                    break;
                }
                case 4: {
                    GL11.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
                    break;
                }
                case 5: {
                    GL11.glColor4f(0.6f, 0.0f, 0.4f, 1.0f);
                    break;
                }
                case 6: {
                    GL11.glColor4f(0.2f, 0.8f, 1.0f, 1.0f);
                    break;
                }
                case 7: {
                    GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
                    break;
                }
                case 8: {
                    GL11.glColor4f(0.4f, 0.4f, 0.4f, 1.0f);
                    break;
                }
                case 9: {
                    GL11.glColor4f(1.0f, 0.2f, 0.6f, 1.0f);
                    break;
                }
                case 10: {
                    GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
                    break;
                }
                case 11: {
                    GL11.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
                    break;
                }
                case 12: {
                    GL11.glColor4f(0.3f, 0.3f, 0.8f, 1.0f);
                    break;
                }
                case 13: {
                    GL11.glColor4f(0.8f, 0.2f, 0.4f, 1.0f);
                    break;
                }
                case 14: {
                    GL11.glColor4f(0.8f, 0.3f, 0.0f, 1.0f);
                    break;
                }
                case 15: {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
            }
            if (connectedSides[0]) {
                RenderInsulatedWire.model.renderBottom();
            }
            if (connectedSides[1]) {
                RenderInsulatedWire.model.renderTop();
            }
            if (connectedSides[2]) {
                RenderInsulatedWire.model.renderBack();
            }
            if (connectedSides[3]) {
                RenderInsulatedWire.model.renderFront();
            }
            if (connectedSides[4]) {
                RenderInsulatedWire.model.renderLeft();
            }
            if (connectedSides[5]) {
                RenderInsulatedWire.model.renderRight();
            }
            RenderInsulatedWire.model.renderMiddle();
            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderTileEntityAt(final TileEntity tileEntity, final double var2,
            final double var4, final double var6,
            final float var8) {
        this.renderAModelAt(tileEntity, var2, var4, var6, var8);
    }

    static {
        model = new ModelInsulatedWire();
    }
}
