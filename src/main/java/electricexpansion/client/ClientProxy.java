package electricexpansion.client;

import net.minecraft.tileentity.TileEntity;
import electricexpansion.client.gui.GuiFuseBox;
import electricexpansion.common.tile.TileEntityFuseBox;
import electricexpansion.client.gui.GuiInsulationMachine;
import electricexpansion.client.gui.GuiQuantumBatteryBox;
import electricexpansion.client.gui.GuiLogisticsWire;
import electricexpansion.client.gui.GuiWireMill;
import electricexpansion.client.gui.GuiAdvancedBatteryBox;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.client.render.RenderMultimeter;
import electricexpansion.common.tile.TileEntityMultimeter;
import electricexpansion.client.render.RenderTransformer;
import electricexpansion.common.tile.TileEntityTransformer;
import electricexpansion.common.tile.TileEntityRedstoneNetworkCore;
import electricexpansion.common.tile.TileEntityInsulatingMachine;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;
import electricexpansion.common.cables.TileEntitySwitchWireBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.common.cables.TileEntityWireBlock;
import electricexpansion.common.cables.TileEntityRedstonePaintedWire;
import electricexpansion.common.cables.TileEntityLogisticsWire;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.client.render.RenderRawWire;
import electricexpansion.common.cables.TileEntityRawWire;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import electricexpansion.client.render.RenderWireMill;
import electricexpansion.common.tile.TileEntityWireMill;
import electricexpansion.client.render.RenderInsulatedWire;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import electricexpansion.client.render.RenderHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.CommonProxy;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    public static int RENDER_ID;
    
    @Override
    public void init() {
        ClientProxy.RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderHandler());
        final RenderInsulatedWire insulatedWireRenderer = new RenderInsulatedWire();
        ClientRegistry.registerTileEntity(TileEntityWireMill.class, "TileEntityWireMill", (TileEntitySpecialRenderer)new RenderWireMill());
        ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", (TileEntitySpecialRenderer)new RenderRawWire());
        ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", (TileEntitySpecialRenderer)insulatedWireRenderer);
        ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", (TileEntitySpecialRenderer)insulatedWireRenderer);
        ClientRegistry.registerTileEntity(TileEntityLogisticsWire.class, "TileEntityLogisticsWire", (TileEntitySpecialRenderer)insulatedWireRenderer);
        ClientRegistry.registerTileEntity(TileEntityRedstonePaintedWire.class, "TileEntityRedstonePaintedWire", (TileEntitySpecialRenderer)insulatedWireRenderer);
        GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
        GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
        GameRegistry.registerTileEntity(TileEntityQuantumBatteryBox.class, "TileEntityDistribution");
        GameRegistry.registerTileEntity(TileEntityInsulatingMachine.class, "TileEntityInsulatingMachine");
        GameRegistry.registerTileEntity(TileEntityRedstoneNetworkCore.class, "TileEntityRedstoneNetworkCore");
        ClientRegistry.registerTileEntity(TileEntityTransformer.class, "TileEntityTransformer", (TileEntitySpecialRenderer)new RenderTransformer());
        ClientRegistry.registerTileEntity(TileEntityMultimeter.class, "TileEntityMultimeter", (TileEntitySpecialRenderer)new RenderMultimeter());
        GameRegistry.registerTileEntity(TileEntityAdvancedBatteryBox.class, "TileEntityAdvBox");
    }
    
    @Override
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            switch (ID) {
                case 0: {
                    return new GuiAdvancedBatteryBox(player.inventory, (TileEntityAdvancedBatteryBox)tileEntity);
                }
                case 2: {
                    return new GuiWireMill(player.inventory, (TileEntityWireMill)tileEntity);
                }
                case 3: {
                    return new GuiLogisticsWire((TileEntityLogisticsWire)tileEntity);
                }
                case 4: {
                    return new GuiQuantumBatteryBox(player.inventory, (TileEntityQuantumBatteryBox)tileEntity);
                }
                case 5: {
                    return new GuiInsulationMachine(player.inventory, (TileEntityInsulatingMachine)tileEntity);
                }
                case 6: {
                    return new GuiFuseBox(player.inventory, (TileEntityFuseBox)tileEntity);
                }
            }
        }
        return null;
    }
}
