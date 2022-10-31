package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.helpers.PlayerHelper;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class BlockQuantumBatteryBox extends BlockAdvanced {
    private HashMap<String, IIcon> icons;

    public BlockQuantumBatteryBox() {
        super(Material.iron);
        this.icons = new HashMap<>();
        this.setBlockName("Distribution");
        this.setStepSound(BlockQuantumBatteryBox.soundTypeMetal);
        this.setHardness(1.5f);
        this.setResistance(10.0f);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public int damageDropped(final int i) {
        return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public IIcon getIcon(final int side, final int metadata) {
        if (side == metadata + 2) {
            return this.icons.get("output");
        }
        if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) {
            return this.icons.get("input");
        }
        return this.icons.get("default");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
        this.icons.put("output", par1IconRegister.registerIcon(
                "electricexpansion:darkMachineOutput"));
        this.icons.put("input", par1IconRegister.registerIcon(
                "electricexpansion:darkMachineInput"));
        this.icons.put("default", par1IconRegister.registerIcon(
                "electricexpansion:darkMachineTop"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs,
            final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, final int meta) {
        return new TileEntityQuantumBatteryBox();
    }

    @Override
    public boolean onBlockActivated(final World par1World, final int x, final int y, final int z,
            final EntityPlayer player, final int par6, final float par7,
            final float par8, final float par9) {
        if (par1World.isRemote) {
            return true;
        }
        if (player.getDisplayName() == ((TileEntityQuantumBatteryBox) par1World.getTileEntity(x, y, z))
                .getOwningPlayer() ||
                PlayerHelper.isPlayerOp(player.getDisplayName())) {
            player.openGui((Object) ElectricExpansion.instance, 4, par1World, x, y, z);
            return true;
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(final World par1World, final int x, final int y,
            final int z,
            final EntityLivingBase par5EntityLiving,
            final ItemStack itemStack) {
        final int angle = MathHelper.floor_double(
                ((Entity) par5EntityLiving).rotationYaw * 4.0f / 360.0f + 0.5) &
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
        if (par5EntityLiving instanceof EntityPlayer &&
                ((TileEntityAdvanced) par1World.getTileEntity(x, y, z)) instanceof TileEntityQuantumBatteryBox) {
            ((TileEntityQuantumBatteryBox) par1World.getTileEntity(x, y, z))
                    .setPlayer((EntityPlayer) par5EntityLiving);
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

    // TODO: WTF
    // @Override
    // public ItemStack getPickBlock(final MovingObjectPosition target, final
    // World world, final int x, final int y, final int z) {
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
        if (tileEntity instanceof TileEntityQuantumBatteryBox) {
            final TileEntityAdvancedBatteryBox te = (TileEntityAdvancedBatteryBox) tileEntity;
            final double max = te.getMaxJoules();
            final double current = te.getJoules();
            return (int) (current / max * 15.0);
        }
        return 0;
    }
}
