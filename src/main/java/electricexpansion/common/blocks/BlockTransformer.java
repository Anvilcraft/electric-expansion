//
// Decompiled by Procyon v0.6.0
//

package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityTransformer;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class BlockTransformer extends BlockAdvanced {
    public BlockTransformer() {
        super(UniversalElectricity.machine);
        this.setStepSound(BlockTransformer.soundTypeMetal);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
        this.setBlockName("transformer");
    }

    @Override
    public void onBlockPlacedBy(final World par1World, final int x, final int y,
            final int z,
            final EntityLivingBase par5EntityLiving,
            final ItemStack itemStack) {
        final int metadata = par1World.getBlockMetadata(x, y, z);
        final int tierStart = metadata - (metadata & 0x3);
        final int angle = MathHelper.floor_double(
                ((Entity) par5EntityLiving).rotationYaw * 4.0f / 360.0f + 0.5) &
                0x3;
        switch (angle) {
            case 0: {
                par1World.setBlock(x, y, z, this, tierStart + 3, 0);
                break;
            }
            case 1: {
                par1World.setBlock(x, y, z, this, tierStart + 1, 0);
                break;
            }
            case 2: {
                par1World.setBlock(x, y, z, this, tierStart + 2, 0);
                break;
            }
            case 3: {
                par1World.setBlock(x, y, z, this, tierStart + 0, 0);
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
        final int tierStart = metadata - (metadata & 0x3);
        final int original = metadata & 0x3;
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
        par1World.setBlock(x, y, z, this, change + tierStart, 0);
        par1World.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        ((TileEntityAdvanced) par1World.getTileEntity(x, y, z)).initiate();
        return true;
    }

    @Override
    public boolean onSneakUseWrench(final World par1World, final int x, final int y, final int z,
            final EntityPlayer par5EntityPlayer, final int side,
            final float hitX, final float hitY, final float hitZ) {
        if (!par1World.isRemote) {
            final TileEntityTransformer tileEntity = (TileEntityTransformer) par1World.getTileEntity(x, y, z);
            tileEntity.stepUp = !tileEntity.stepUp;
            par1World.markBlockForUpdate(x, y, z);
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isBlockSolid(final IBlockAccess world, final int x,
            final int y, final int z, final int side) {
        return side == ForgeDirection.DOWN.ordinal();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return ClientProxy.RENDER_ID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, final int metadata) {
        return new TileEntityTransformer();
    }

    @Override
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs,
            final List par3List) {
        for (int i = 0; i < 9; i += 4) {
            par3List.add(new ItemStack((Block) this, 1, i));
        }
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
    // final int metadata = world.getBlockMetadata(x, y, z);
    // final int tierStart = metadata - (metadata & 0x3);
    // return new ItemStack(id, 1, tierStart);
    // }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
    }

    @Override
    public int damageDropped(final int metadata) {
        return metadata - (metadata & 0x3);
    }
}
