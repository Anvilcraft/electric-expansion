package electricexpansion.common.blocks;

import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.tile.TileEntityWireMill;
import net.minecraft.tileentity.TileEntity;
import electricexpansion.common.ElectricExpansion;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import electricexpansion.common.misc.EETab;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;

public class BlockWireMill extends BlockAdvanced
{
    public BlockWireMill() {
        super(UniversalElectricity.machine);
        this.setStepSound(BlockWireMill.soundTypeMetal);
        this.setBlockName("wiremill");
        this.setCreativeTab((CreativeTabs)EETab.INSTANCE);
    }
    
    @Override
    public void onBlockPlacedBy(final World par1World, final int x, final int y, final int z, final EntityLivingBase par5EntityLiving, final ItemStack itemStack) {
        final int angle = MathHelper.floor_double(((Entity)par5EntityLiving).rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        switch (angle) {
            case 0: {
                par1World.setBlock(x, y, z, this, 1, 0);
                break;
            }
            case 1: {
                par1World.setBlock(x, y, z, this, 2, 0);
                break;
            }
            case 2: {
                par1World.setBlock(x, y, z, this, 0, 0);
                break;
            }
            case 3: {
                par1World.setBlock(x, y, z, this, 3, 0);
                break;
            }
        }
        ((TileEntityAdvanced)par1World.getTileEntity(x, y, z)).initiate();
        par1World.notifyBlocksOfNeighborChange(x, y, z, this);
    }
    
    @Override
    public boolean onUseWrench(final World par1World, final int x, final int y, final int z, final EntityPlayer par5EntityPlayer, final int side, final float hitX, final float hitY, final float hitZ) {
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
        ((TileEntityAdvanced)par1World.getTileEntity(x, y, z)).initiate();
        return true;
    }
    
    @Override
    public boolean onMachineActivated(final World par1World, final int x, final int y, final int z, final EntityPlayer par5EntityPlayer, final int side, final float hitX, final float hitY, final float hitZ) {
        if (!par1World.isRemote) {
            par5EntityPlayer.openGui((Object)ElectricExpansion.instance, 2, par1World, x, y, z);
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
    public TileEntity createNewTileEntity(final World var1, final int metadata) {
        return new TileEntityWireMill();
    }
    
    //TODO: WTF
    //public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z) {
    //    final int id = this.func_71922_a(world, x, y, z);
    //    if (id == 0) {
    //        return null;
    //    }
    //    final Item item = Item.field_77698_e[id];
    //    if (item == null) {
    //        return null;
    //    }
    //    return new ItemStack(id, 1, 0);
    //}
    
    @Override
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return ClientProxy.RENDER_ID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
    }
}
