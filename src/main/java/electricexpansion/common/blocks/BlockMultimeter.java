package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityMultimeter;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class BlockMultimeter extends BlockAdvanced {
  private HashMap<String, IIcon> icons;

  public BlockMultimeter() {
    super(UniversalElectricity.machine);
    this.icons = new HashMap<>();
    this.setStepSound(Block.soundTypeMetal);
    this.setCreativeTab((CreativeTabs)EETab.INSTANCE);
    this.setBlockName("multimeter");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(final int side, final int metadata) {
    if (side == 3) {
      return this.icons.get("front");
    }
    return this.icons.get("top");
  }

  @Override
  public IIcon getIcon(final IBlockAccess par1IBlockAccess, final int x,
                       final int y, final int z, final int side) {
    final int metadata = par1IBlockAccess.getBlockMetadata(x, y, z);
    if (side ==
        ForgeDirection.getOrientation(metadata).getOpposite().ordinal()) {
      return this.icons.get("output");
    }
    return this.icons.get("top");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(final IIconRegister par1IconRegister) {
    this.icons.put(
        "top", par1IconRegister.registerIcon("electricexpansion:machineTop"));
    this.icons.put("output", par1IconRegister.registerIcon(
                                 "electricexpansion:machineOutput"));
    this.icons.put("machine",
                   par1IconRegister.registerIcon("electricexpansion:machine"));
    this.icons.put(
        "front", par1IconRegister.registerIcon("electricexpansion:multimeter"));
  }

  @Override
  public void onBlockPlacedBy(final World world, final int x, final int y,
                              final int z,
                              final EntityLivingBase par5EntityLiving,
                              final ItemStack itemStack) {
    final int angle =
        MathHelper.floor_double(
            ((Entity)par5EntityLiving).rotationYaw * 4.0f / 360.0f + 0.5) &
        0x3;
    int change = 2;
    switch (angle) {
    case 0: {
      change = 2;
      break;
    }
    case 1: {
      change = 5;
      break;
    }
    case 2: {
      change = 3;
      break;
    }
    case 3: {
      change = 4;
      break;
    }
    }
    world.setBlock(x, y, z, this, change, 0);
    ((TileEntityAdvanced)world.getTileEntity(x, y, z)).initiate();
    world.notifyBlocksOfNeighborChange(x, y, z, this);
  }

  @Override
  public boolean onUseWrench(final World world, final int x, final int y,
                             final int z, final EntityPlayer par5EntityPlayer,
                             final int side, final float hitX, final float hitY,
                             final float hitZ) {
    final int original = world.getBlockMetadata(x, y, z);
    int change = 2;
    switch (original) {
    case 2: {
      change = 5;
      break;
    }
    case 5: {
      change = 4;
      break;
    }
    case 4: {
      change = 3;
      break;
    }
    case 3: {
      change = 2;
      break;
    }
    }
    world.setBlock(x, y, z, this, change, 0);
    world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
    ((TileEntityAdvanced)world.getTileEntity(x, y, z)).initiate();
    world.notifyBlocksOfNeighborChange(x, y, z, this);
    return true;
  }

  @Override
  public int isProvidingStrongPower(final IBlockAccess par1IBlockAccess,
                                    final int x, final int y, final int z,
                                    final int side) {
    final TileEntity tileEntity = par1IBlockAccess.getTileEntity(x, y, z);
    if (tileEntity instanceof IRedstoneProvider) {
      return ((IRedstoneProvider)tileEntity)
                 .isPoweringTo(ForgeDirection.getOrientation(side))
          ? 15
          : 0;
    }
    return 0;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public TileEntity createNewTileEntity(final World var1, final int metadata) {
    return new TileEntityMultimeter();
  }
}
