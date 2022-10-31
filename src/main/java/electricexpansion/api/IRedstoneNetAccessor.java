package electricexpansion.api;

import universalelectricity.core.block.INetworkProvider;

public interface IRedstoneNetAccessor extends INetworkProvider
{
    int getRsSignalFromBlock();
}
