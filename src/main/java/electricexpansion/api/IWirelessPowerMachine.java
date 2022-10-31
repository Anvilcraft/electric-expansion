package electricexpansion.api;

import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.block.IElectricityStorage;

public interface IWirelessPowerMachine extends IElectricityStorage {
  byte getFrequency();

  void setFrequency(final byte p0);

  String getType();

  void removeJoules(final double p0);

  void setPlayer(final EntityPlayer p0);
}
