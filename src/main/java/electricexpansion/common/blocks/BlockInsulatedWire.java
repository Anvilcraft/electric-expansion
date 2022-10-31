package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.prefab.block.BlockConductor;

public class BlockInsulatedWire extends BlockConductor {
    public BlockInsulatedWire() {
        super(Material.cloth);
        this.setBlockName("InsulatedWire");
        this.setStepSound(BlockInsulatedWire.soundTypeCloth);
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
        return new TileEntityInsulatedWire();
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
    public void onBlockAdded(final World world, final int x, final int y,
            final int z) {
        super.onBlockAdded(world, x, y, z);
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof IConductor) {
            ((IConductor) tileEntity).updateAdjacentConnections();
            this.updateWireSwitch(world, x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated(final World par1World, final int x, final int y, final int z,
            final EntityPlayer par5EntityPlayer, final int par6,
            final float par7, final float par8, final float par9) {
        final TileEntityInsulatedWire tileEntity = (TileEntityInsulatedWire) par1World.getTileEntity(x, y, z);
        if (!par1World.isRemote &&
                par5EntityPlayer.inventory.getCurrentItem() != null &&
                par5EntityPlayer.inventory.getCurrentItem().getItem() instanceof ItemDye) {
            final int dyeColor = par5EntityPlayer.inventory.getCurrentItem().getItemDamageForDisplay();
            tileEntity.colorByte = (byte) dyeColor;
            --par5EntityPlayer.inventory.getCurrentItem().stackSize;
            // TODO: WTF
            // PacketManager.sendPacketToClients(PacketManager.getPacket(
            // "ElecEx", tileEntity, 0, tileEntity.colorByte));
            tileEntity.updateAdjacentConnections();
            this.updateWireSwitch(par1World, x, y, z);
            return true;
        }
        return false;
    }

    private void updateWireSwitch(final World world, final int x, final int y,
            final int z) {
        final TileEntityInsulatedWire tileEntity = (TileEntityInsulatedWire) world.getTileEntity(x, y, z);
        if (!world.isRemote && tileEntity != null) {
            for (byte i = 0; i < 6; ++i) {
                TileEntity tileEntity2 = null;
                switch (i) {
                    case 0: {
                        tileEntity2 = world.getTileEntity(x + 1, y, z);
                        break;
                    }
                    case 1: {
                        tileEntity2 = world.getTileEntity(x - 1, y, z);
                        break;
                    }
                    case 2: {
                        tileEntity2 = world.getTileEntity(x, y + 1, z);
                        break;
                    }
                    case 3: {
                        tileEntity2 = world.getTileEntity(x, y - 1, z);
                        break;
                    }
                    case 4: {
                        tileEntity2 = world.getTileEntity(x, y, z + 1);
                        break;
                    }
                    case 5: {
                        tileEntity2 = world.getTileEntity(x, y, z - 1);
                        break;
                    }
                    default: {
                        tileEntity2 = world.getTileEntity(x, y, z);
                        break;
                    }
                }
                if (tileEntity2 instanceof IConductor) {
                    ((IConductor) tileEntity2).updateAdjacentConnections();
                    tileEntity2.getWorldObj().markBlockForUpdate(
                            tileEntity2.xCoord, tileEntity2.yCoord, tileEntity2.zCoord);
                }
            }
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
