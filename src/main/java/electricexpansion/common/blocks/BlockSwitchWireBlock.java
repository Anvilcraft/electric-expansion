package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntitySwitchWireBlock;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockConductor;

public class BlockSwitchWireBlock extends BlockConductor {
    public BlockSwitchWireBlock() {
        super(Material.rock);
        this.setBlockName("SwitchWireBlock");
        this.setStepSound(BlockSwitchWireBlock.soundTypeStone);
        this.setResistance(0.2f);
        this.setHardness(1.5f);
        this.setResistance(10.0f);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }

    @Override
    public int damageDropped(final int i) {
        return i;
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, int meta) {
        return new TileEntitySwitchWireBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs,
            final List par3List) {
        for (int var4 = 0; var4 < 5; ++var4) {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public boolean canConnectRedstone(final IBlockAccess world, final int x,
            final int y, final int z, final int side) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess par1IBlockAccess, final int x,
            final int y, final int z, final int side) {
        return (((TileEntityConductorBase) par1IBlockAccess.getTileEntity(x, y, z)).textureItemStack == null)
                ? this.blockIcon
                : ((TileEntityConductorBase) par1IBlockAccess.getTileEntity(x, y, z)).textureItemStack.getIconIndex();
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y,
            final int z, final EntityPlayer player,
            final int par6, final float par7,
            final float par8, final float par9) {
        if (!(world.getTileEntity(x, y, z) instanceof TileEntityConductorBase)) {
            return false;
        }
        if (player.inventory.getCurrentItem().getItem() != Item.getItemFromBlock(this)) {
            ((TileEntityConductorBase) world.getTileEntity(x, y, z)).textureItemStack = player.inventory
                    .getCurrentItem();
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            return true;
        }
        ((TileEntityConductorBase) world.getTileEntity(x, y, z)).textureItemStack = null;
        world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon("electricexpansion:CamoWire");
    }
}
