package electricexpansion.common;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.cables.TileEntityLogisticsWire;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.cables.TileEntityRedstonePaintedWire;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.cables.TileEntitySwitchWireBlock;
import electricexpansion.common.cables.TileEntityWireBlock;
import electricexpansion.common.containers.ContainerAdvBatteryBox;
import electricexpansion.common.containers.ContainerDistribution;
import electricexpansion.common.containers.ContainerFuseBox;
import electricexpansion.common.containers.ContainerInsulationMachine;
import electricexpansion.common.containers.ContainerWireMill;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityFuseBox;
import electricexpansion.common.tile.TileEntityInsulatingMachine;
import electricexpansion.common.tile.TileEntityMultimeter;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;
import electricexpansion.common.tile.TileEntityRedstoneNetworkCore;
import electricexpansion.common.tile.TileEntityTransformer;
import electricexpansion.common.tile.TileEntityWireMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {
  public void init() {
    GameRegistry.registerTileEntity(TileEntityRawWire.class,
                                    "TileEntityRawWire");
    GameRegistry.registerTileEntity(TileEntityInsulatedWire.class,
                                    "TileEntityInsulatedWire");
    GameRegistry.registerTileEntity(TileEntityWireBlock.class,
                                    "TileEntityWireBlock");
    GameRegistry.registerTileEntity(TileEntitySwitchWire.class,
                                    "TileEntitySwitchWire");
    GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class,
                                    "TileEntitySwitchWireBlock");
    GameRegistry.registerTileEntity(TileEntityLogisticsWire.class,
                                    "TileEntityLogisticsWire");
    GameRegistry.registerTileEntity(TileEntityWireMill.class,
                                    "TileEntityWireMill");
    GameRegistry.registerTileEntity(TileEntityRedstonePaintedWire.class,
                                    "TileEntityRedstonePaintedWire");
    GameRegistry.registerTileEntity(TileEntityAdvancedBatteryBox.class,
                                    "TileEntityAdvBox");
    GameRegistry.registerTileEntity(TileEntityMultimeter.class,
                                    "TileEntityVoltDet");
    GameRegistry.registerTileEntity(TileEntityTransformer.class,
                                    "TileEntityTransformer");
    GameRegistry.registerTileEntity(TileEntityQuantumBatteryBox.class,
                                    "TileEntityDistribution");
    GameRegistry.registerTileEntity(TileEntityInsulatingMachine.class,
                                    "TileEntityInsulatingMachine");
    GameRegistry.registerTileEntity(TileEntityRedstoneNetworkCore.class,
                                    "TileEntityRedstoneNetworkCore");
    GameRegistry.registerTileEntity(TileEntityFuseBox.class,
                                    "TileEntityFuseBox");
  }

  public Object getClientGuiElement(final int ID, final EntityPlayer player,
                                    final World world, final int x, final int y,
                                    final int z) {
    return null;
  }

  public Object getServerGuiElement(final int ID, final EntityPlayer player,
                                    final World world, final int x, final int y,
                                    final int z) {
    final TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity != null) {
      switch (ID) {
      case 0: {
        return new ContainerAdvBatteryBox(
            player.inventory, (TileEntityAdvancedBatteryBox)tileEntity);
      }
      case 2: {
        return new ContainerWireMill(player.inventory,
                                     (TileEntityWireMill)tileEntity);
      }
      case 4: {
        return new ContainerDistribution(
            player.inventory, (TileEntityQuantumBatteryBox)tileEntity);
      }
      case 5: {
        return new ContainerInsulationMachine(
            player.inventory, (TileEntityInsulatingMachine)tileEntity);
      }
      case 6: {
        return new ContainerFuseBox(player.inventory,
                                    (TileEntityFuseBox)tileEntity);
      }
      }
    }
    return null;
  }
}
