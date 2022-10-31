package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntityRedstonePaintedWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;

public class BlockRedstonePaintedWire
        extends Block implements ITileEntityProvider {
    public BlockRedstonePaintedWire() {
        super(Material.cloth);
        this.setBlockName("RedstonePaintedWire");
        this.setStepSound(BlockRedstonePaintedWire.soundTypeCloth);
        this.setResistance(0.2f);
        this.setHardness(0.1f);
        this.setBlockBounds(0.3f, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
    }

    @Override
    public boolean canConnectRedstone(final IBlockAccess world, final int x,
            final int y, final int z, final int side) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityRedstonePaintedWire) {
            final TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) tileEntity;
            return side > -1 && side < 6 && te.connectedBlocks[side] == null;
        }
        return false;
    }

    @Override
    public int isProvidingStrongPower(final IBlockAccess world, final int x,
            final int y, final int z, final int side) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityRedstonePaintedWire) {
            final TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getTileEntity(x, y, z);
            if (te.smartNetwork != null) {
                return te.smartNetwork.rsLevel;
            }
        }
        return 0;
    }

    @Override
    public int isProvidingWeakPower(final IBlockAccess world, final int x,
            final int y, final int z, final int side) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityRedstonePaintedWire) {
            final TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getTileEntity(x, y, z);
            if (te.smartNetwork != null) {
                return te.smartNetwork.rsLevel;
            }
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
    public int damageDropped(final int i) {
        return i;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, int meta) {
        return new TileEntityRedstonePaintedWire();
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

    private void updateWireSwitch(final World world, final int x, final int y,
            final int z) {
        final TileEntityRedstonePaintedWire tileEntity = (TileEntityRedstonePaintedWire) world.getTileEntity(x, y, z);
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

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y,
            final int z, final EntityPlayer player,
            final int par6, final float par7,
            final float par8, final float par9) {
        final TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getTileEntity(x, y, z);
        if (te.smartNetwork != null) {
            player.addChatMessage(
                    new ChatComponentText("NetRsLevel: " + te.smartNetwork.rsLevel));
        } else {
            player.addChatMessage(
                    new ChatComponentText("NetRsLevel: NETWORK INVALID"));
        }
        player.addChatMessage(new ChatComponentText(
                "WldRsLevel: " + world.getBlockPowerInput(x, y, z)));
        return true;
    }
}
