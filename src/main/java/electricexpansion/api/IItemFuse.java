package electricexpansion.api;

import net.minecraft.item.ItemStack;

public interface IItemFuse {
  double getMaxVolts(final ItemStack p0);

  ItemStack onFuseTrip(final ItemStack p0);

  boolean isValidFuse(final ItemStack p0);

  boolean canReset(final ItemStack p0);

  ItemStack onReset(final ItemStack p0);

  String getUnlocalizedName(final ItemStack p0);
}
