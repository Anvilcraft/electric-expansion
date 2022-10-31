package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EENetwork;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityRedstoneNetworkCore;
import java.util.HashMap;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class BlockRedstoneNetworkCore extends BlockAdvanced {
    private HashMap<String, IIcon> icons;

    public BlockRedstoneNetworkCore() {
        super(Material.iron);
        this.icons = new HashMap<>();
        this.setStepSound(BlockRedstoneNetworkCore.soundTypeMetal);
        this.setCreativeTab((CreativeTabs) EETab.INSTANCE);
        this.setBlockName("RsNetCore");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IconRegister) {
        this.icons.put(
                "top", par1IconRegister.registerIcon("electricexpansion:rsMachine"));
        this.icons.put("out", par1IconRegister.registerIcon(
                "electricexpansion:rsMachineOutput"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        return (side == metadata) ? this.icons.get("out") : this.icons.get("top");
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
                par1World.setBlock(x, y, z, this, 5, 0);
                break;
            }
            case 1: {
                par1World.setBlock(x, y, z, this, 3, 0);
                break;
            }
            case 2: {
                par1World.setBlock(x, y, z, this, 4, 0);
                break;
            }
            case 3: {
                par1World.setBlock(x, y, z, this, 2, 0);
                break;
            }
        }
        ((TileEntityAdvanced) par1World.getTileEntity(x, y, z)).initiate();
        par1World.markBlockForUpdate(x, y, z);
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
                change = 1;
                break;
            }
            case 1: {
                change = 2;
                break;
            }
            case 2: {
                change = 5;
                break;
            }
            case 3: {
                change = 4;
                break;
            }
            case 4: {
                change = 0;
                break;
            }
            case 5: {
                change = 3;
                break;
            }
        }
        par1World.setBlock(x, y, z, this, change, 0);
        par1World.markBlockForUpdate(x, y, z);
        ((TileEntityAdvanced) par1World.getTileEntity(x, y, z)).initiate();
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int metadata) {
        return new TileEntityRedstoneNetworkCore();
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
    public boolean onMachineActivated(final World world, final int x, final int y,
            final int z, final EntityPlayer player,
            final int par6, final float par7,
            final float par8, final float par9) {
        final TileEntityRedstoneNetworkCore te = (TileEntityRedstoneNetworkCore) world.getTileEntity(x, y, z);
        if (te.getNetwork() != null) {
            player.addChatMessage(new ChatComponentText(
                    "NetRsLevel: " + ((EENetwork) te.getNetwork()).rsLevel));
        } else {
            player.addChatMessage(
                    new ChatComponentText("NetRsLevel: NETWORK INVALID"));
        }
        return true;
    }
}
