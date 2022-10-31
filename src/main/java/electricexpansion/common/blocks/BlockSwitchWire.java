package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockConductor;

public class BlockSwitchWire extends BlockConductor {
    public BlockSwitchWire() {
        super(Material.cloth);
        this.setBlockName("SwitchWire");
        this.setStepSound(BlockSwitchWire.soundTypeCloth);
        this.setResistance(0.2f);
        this.setHardness(0.1f);
        this.setBlockBounds(0.3f, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
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
    public int damageDropped(final int i) {
        return i;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, int meta) {
        return new TileEntitySwitchWire();
    }

    @Override
    public boolean canProvidePower() {
        return true;
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
    public void setBlockBoundsBasedOnState(final IBlockAccess par1IBlockAccess,
            final int x, final int y,
            final int z) {
        final TileEntity tileEntity = par1IBlockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityConductorBase) {
            final TileEntityConductorBase te = (TileEntityConductorBase) tileEntity;
            this.minX = ((te.connectedBlocks[4] != null) ? 0.0 : 0.30000001192092896);
            this.minY = ((te.connectedBlocks[0] != null) ? 0.0 : 0.30000001192092896);
            this.minZ = ((te.connectedBlocks[2] != null) ? 0.0 : 0.30000001192092896);
            this.maxX = ((te.connectedBlocks[5] != null) ? 1.0 : 0.699999988079071);
            this.maxY = ((te.connectedBlocks[1] != null) ? 1.0 : 0.699999988079071);
            this.maxZ = ((te.connectedBlocks[3] != null) ? 1.0 : 0.699999988079071);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
    }
}
