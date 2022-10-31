package electricexpansion.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBasic extends Block {
    public BlockBasic(final Material material, final CreativeTabs tab,
            final float hardness, final float resistance,
            final String name, final float lightValue,
            final SoundType sound) {
        super(material);
        this.setCreativeTab(tab);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setBlockName(name);
        this.setStepSound(sound);
        this.setLightLevel(lightValue);
    }

    public BlockBasic(final Material material, final CreativeTabs tab,
            final float hardness, final float resistance,
            final String name, final float lightValue) {
        this(material, tab, hardness, resistance, name, lightValue,
                BlockBasic.soundTypeMetal);
    }

    public BlockBasic(final Material material, final CreativeTabs tab,
            final float hardness, final float resistance,
            final String name) {
        this(material, tab, hardness, resistance, name, 0.0f,
                BlockBasic.soundTypeMetal);
    }

    public BlockBasic(final Material material, final CreativeTabs tab,
            final float hardness, final String name) {
        this(material, tab, hardness, 1.0f, name, 0.0f, BlockBasic.soundTypeMetal);
    }

    public BlockBasic(final CreativeTabs tab, final float hardness,
            final float resistance, final String name) {
        this(Material.iron, tab, hardness, resistance, name, 0.0f,
                BlockBasic.soundTypeMetal);
    }

    public BlockBasic(final CreativeTabs tab, final float hardness,
            final String name) {
        this(Material.iron, tab, hardness, 1.0f, name, 0.0f,
                BlockBasic.soundTypeMetal);
    }

    public BlockBasic(final CreativeTabs tab, final String name) {
        this(Material.iron, tab, 1.0f, 1.0f, name, 0.0f, BlockBasic.soundTypeMetal);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister reg) {
        this.blockIcon = reg.registerIcon(
                this.getUnlocalizedName().replace("tile.", "electricexpansion:"));
    }
}
