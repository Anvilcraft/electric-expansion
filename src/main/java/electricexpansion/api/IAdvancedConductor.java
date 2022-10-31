package electricexpansion.api;

import universalelectricity.core.block.IConductor;

public interface IAdvancedConductor extends IConductor {
  EnumWireMaterial getWireMaterial(final int p0);

  EnumWireType getWireType(final int p0);
}
