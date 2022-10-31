package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import java.util.HashMap;
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
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class BlockAdvancedBatteryBox extends BlockAdvanced {
    private HashMap<String, IIcon> icons;

    public BlockAdvancedBatteryBox() {
        super(UniversalElectricity.machine);
        this.icons = new HashMap<>();
        this.setStepSound(BlockAdvancedBatteryBox.soundTypeMetal);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
        this.setBlockName("advbatbox");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
        this.icons.put(
                "top", par1IconRegister.registerIcon("electricexpansion:machineTop"));
        this.icons.put("out", par1IconRegister.registerIcon(
                "electricexpansion:machineOutput"));
        this.icons.put("input", par1IconRegister.registerIcon(
                "electricexpansion:machineInput"));
        this.icons.put("tier1",
                par1IconRegister.registerIcon("electricexpansion:batBoxT1"));
        this.icons.put("tier2",
                par1IconRegister.registerIcon("electricexpansion:batBoxT2"));
        this.icons.put("tier3",
                par1IconRegister.registerIcon("electricexpansion:batBoxT3"));
        this.icons.put("tier4",
                par1IconRegister.registerIcon("electricexpansion:batBoxT4"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess iBlockAccess, final int x,
            final int y, final int z, final int side) {
        final int metadata = iBlockAccess.getBlockMetadata(x, y, z);
        final TileEntityAdvancedBatteryBox tileEntity = (TileEntityAdvancedBatteryBox) iBlockAccess.getTileEntity(x, y,
                z);
        if (side == 0 || side == 1) {
            return this.icons.get("top");
        }
        if (side == metadata + 2) {
            return this.icons.get("out");
        }
        if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) {
            return this.icons.get("input");
        }
        if (tileEntity.getMaxJoules() <= 8000000.0) {
            return this.icons.get("tier1");
        }
        if (tileEntity.getMaxJoules() > 8000000.0 &&
                tileEntity.getMaxJoules() <= 1.2E7) {
            return this.icons.get("tier2");
        }
        if (tileEntity.getMaxJoules() > 1.2E7 &&
                tileEntity.getMaxJoules() <= 1.6E7) {
            return this.icons.get("tier3");
        }
        if (tileEntity.getMaxJoules() > 1.6E7) {
            return this.icons.get("tier4");
        }
        return this.icons.get("tier1");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        if (side == 0 || side == 1) {
            return this.icons.get("top");
        }
        if (side == metadata + 2) {
            return this.icons.get("out");
        }
        if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) {
            return this.icons.get("input");
        }
        return this.icons.get("tier1");
    }

    @Override
    public void onBlockPlacedBy(final World par1World, final int x, final int y,
            final int z, final EntityLivingBase entity,
            final ItemStack itemStack) {
        final int angle = MathHelper.floor_double(
                ((Entity) entity).rotationYaw * 4.0f / 360.0f + 0.5) &
                0x3;
        switch (angle) {
            case 0: {
                par1World.setBlock(x, y, z, this, 3, 0);
                break;
            }
            case 1: {
                par1World.setBlock(x, y, z, this, 1, 0);
                break;
            }
            case 2: {
                par1World.setBlock(x, y, z, this, 2, 0);
                break;
            }
            case 3: {
                par1World.setBlock(x, y, z, this, 0, 0);
                break;
            }
        }
        ((TileEntityAdvanced) par1World.getTileEntity(x, y, z)).initiate();
        par1World.notifyBlocksOfNeighborChange(x, y, z, this);
    }

    @Override
    public boolean onUseWrench(final World par1World, final int x, final int y,
            final int z, final EntityPlayer par5EntityPlayer,
            final int side, final float hitX, final float hitY,
            final float hitZ) {
        final int metadata = par1World.getBlockMetadata(x, y, z);
        int change = 0;
        switch (metadata) {
            case 0: {
                change = 3;
                break;
            }
            case 3: {
                change = 1;
                break;
            }
            case 1: {
                change = 2;
                break;
            }
            case 2: {
                change = 0;
                break;
            }
        }
        par1World.setBlock(x, y, z, this, change, 0);
        par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        ((TileEntityAdvanced) par1World.getTileEntity(x, y, z)).initiate();
        return true;
    }

    @Override
    public boolean onMachineActivated(final World par1World, final int x,
            final int y, final int z,
            final EntityPlayer par5EntityPlayer,
            final int side, final float hitX,
            final float hitY, final float hitZ) {
        if (!par1World.isRemote) {
            par5EntityPlayer.openGui((Object) ElectricExpansion.instance, 0, par1World,
                    x, y, z);
            return true;
        }
        return true;
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
    public TileEntity createNewTileEntity(final World world, final int metadata) {
        return new TileEntityAdvancedBatteryBox();
    }

    // TODO: WTF
    // @Override
    // public ItemStack getPickBlock(final MovingObjectPosition target,
    // final World world, final int x, final int y,
    // final int z) {
    // final int id = this.func_71922_a(world, x, y, z);
    // if (id == 0) {
    // return null;
    // }
    // final Item item = Item.field_77698_e[id];
    // if (item == null) {
    // return null;
    // }
    // return new ItemStack(id, 1, 0);
    // }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(final World par1World, final int x,
            final int y, final int z,
            final int meta) {
        final TileEntity tileEntity = par1World.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityAdvancedBatteryBox) {
            final TileEntityAdvancedBatteryBox te = (TileEntityAdvancedBatteryBox) tileEntity;
            final double max = te.getMaxJoules();
            final double current = te.getJoules();
            return (int) (current / max * 15.0);
        }
        return 0;
    }
}
