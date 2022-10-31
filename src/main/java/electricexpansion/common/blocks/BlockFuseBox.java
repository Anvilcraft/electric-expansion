package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityFuseBox;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

public class BlockFuseBox extends BlockAdvanced {
    private HashMap<String, IIcon> icons;

    public BlockFuseBox() {
        super(UniversalElectricity.machine);
        this.icons = new HashMap<>();
        this.setBlockName("FuseBox");
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
        this.setStepSound(BlockFuseBox.soundTypeMetal);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess iBlockAccess, final int x,
            final int y, final int z, final int side) {
        final int metadata = iBlockAccess.getBlockMetadata(x, y, z);
        if (side == 0 || side == 1) {
            return this.icons.get("top");
        }
        if (side == metadata + 2) {
            return this.icons.get("output");
        }
        if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) {
            return this.icons.get("input");
        }
        return this.icons.get("side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
        this.icons.put(
                "top", par1IconRegister.registerIcon("electricexpansion:machineTop"));
        this.icons.put("output", par1IconRegister.registerIcon(
                "electricexpansion:machineOutput"));
        this.icons.put("input", par1IconRegister.registerIcon(
                "electricexpansion:machineInput"));
        this.icons.put("side",
                par1IconRegister.registerIcon("electricexpansion:fusebox"));
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y,
            final int z, final EntityLivingBase player,
            final ItemStack itemStack) {
        final int angle = MathHelper.floor_double(
                ((Entity) player).rotationYaw * 4.0f / 360.0f + 0.5) &
                0x3;
        switch (angle) {
            case 0: {
                world.setBlock(x, y, z, this, 3, 0);
                break;
            }
            case 1: {
                world.setBlock(x, y, z, this, 1, 0);
                break;
            }
            case 2: {
                world.setBlock(x, y, z, this, 2, 0);
                break;
            }
            case 3: {
                world.setBlock(x, y, z, this, 0, 0);
                break;
            }
        }
        ((TileEntityAdvanced) world.getTileEntity(x, y, z)).initiate();
        world.notifyBlocksOfNeighborChange(x, y, z, this);
    }

    @Override
    public boolean onUseWrench(final World par1World, final int x, final int y,
            final int z, final EntityPlayer par5EntityPlayer,
            final int side, final float hitX, final float hitY,
            final float hitZ) {
        final int original = par1World.getBlockMetadata(x, y, z);
        int change = 0;
        switch (original) {
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
    public boolean onSneakUseWrench(final World par1World, final int x, final int y, final int z,
            final EntityPlayer par5EntityPlayer, final int side,
            final float hitX, final float hitY, final float hitZ) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    public boolean isBlockSolidOnSide(final World world, final int x, final int y,
            final int z, final ForgeDirection side) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, final int metadata) {
        return new TileEntityFuseBox();
    }

    @Override
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs,
            final List par3List) {
        par3List.add(new ItemStack(this, 1, 0));
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
    // return new ItemStack(id, 1, 0);
    // }

    @Override
    public boolean onMachineActivated(final World par1World, final int x,
            final int y, final int z,
            final EntityPlayer par5EntityPlayer,
            final int side, final float hitX,
            final float hitY, final float hitZ) {
        if (!par1World.isRemote) {
            par5EntityPlayer.openGui((Object) ElectricExpansion.instance, 6, par1World,
                    x, y, z);
            return true;
        }
        return true;
    }
}
