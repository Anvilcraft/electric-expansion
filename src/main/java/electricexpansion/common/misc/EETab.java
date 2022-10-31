package electricexpansion.common.misc;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EETab extends CreativeTabs {
    public static final EETab INSTANCE;
    private ItemStack itemStack;

    public EETab(final String par2Str) {
        super(CreativeTabs.getNextID(), par2Str);
        LanguageRegistry.instance().addStringLocalization(
                "itemGroup.ElectricExpansion", "en_US", "Electric Expansion");
    }

    public void setItemStack(final ItemStack newItemStack) {
        if (this.itemStack == null) {
            this.itemStack = newItemStack;
        }
    }

    @Override
    public Item getTabIconItem() {
        return this.itemStack.getItem();
    }

    @Override
    public ItemStack getIconItemStack() {
        return this.itemStack;
    }

    static {
        INSTANCE = new EETab("ElectricExpansion");
    }
}
