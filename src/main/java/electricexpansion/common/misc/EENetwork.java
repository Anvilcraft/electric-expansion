package electricexpansion.common.misc;

import cpw.mods.fml.common.FMLLog;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.path.Pathfinder;
import universalelectricity.core.block.INetworkProvider;
import net.minecraft.world.IBlockAccess;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.path.PathfinderChecker;
import universalelectricity.core.block.IConnectionProvider;
import java.util.Iterator;
import electricexpansion.api.IRedstoneNetAccessor;
import net.minecraft.tileentity.TileEntity;
import java.util.Map;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import universalelectricity.core.block.IConductor;
import electricexpansion.common.tile.TileEntityRedstoneNetworkCore;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import universalelectricity.core.electricity.IElectricityNetwork;

public class EENetwork implements IElectricityNetwork
{
    private final HashMap<TileEntity, ElectricityPack> producers;
    private final HashMap<TileEntity, ElectricityPack> consumers;
    private final Set<IConductor> conductors;
    private final List redstoneInterfacers;
    public byte rsLevel;
    public TileEntityRedstoneNetworkCore coreProcessor;
    
    public EENetwork(final IConductor... conductors) {
        this.producers = new HashMap<>();
        this.consumers = new HashMap<>();
        this.conductors = new HashSet<>();
        this.redstoneInterfacers = new ArrayList<>();
        this.rsLevel = 0;
        this.conductors.addAll(Arrays.asList(conductors));
        this.cleanUpConductors();
    }
    
    public EENetwork(final IElectricityNetwork oldNetwork) {
        this.producers = new HashMap<>();
        this.consumers = new HashMap<>();
        this.conductors = new HashSet<>();
        this.redstoneInterfacers = new ArrayList<>();
        this.rsLevel = 0;
        this.conductors.addAll(oldNetwork.getConductors());
        this.consumers.putAll(oldNetwork.getConsumers());
        this.producers.putAll(oldNetwork.getProducers());
        this.cleanUpConductors();
    }
    
    @Override
    public void cleanUpConductors() {
        final Iterator<IConductor> it = this.conductors.iterator();
        this.redstoneInterfacers.clear();
        while (it.hasNext()) {
            final IConductor conductor = it.next();
            if (conductor == null) {
                it.remove();
            }
            else if (((TileEntity)conductor).isInvalid()) {
                it.remove();
            }
            else if (conductor instanceof IRedstoneNetAccessor) {
                conductor.setNetwork(this);
                this.redstoneInterfacers.add(conductor);
            }
            else {
                conductor.setNetwork(this);
            }
        }
    }
    
    @Override
    public void mergeConnection(final IElectricityNetwork network) {
        if (network != null && network != this && network instanceof EENetwork) {
            final EENetwork newNetwork = new EENetwork(new IConductor[0]);
            newNetwork.getConductors().addAll(this.getConductors());
            newNetwork.getConductors().addAll(network.getConductors());
            newNetwork.cleanUpConductors();
            if (this.coreProcessor != null && ((EENetwork)network).coreProcessor == null) {
                newNetwork.coreProcessor = this.coreProcessor;
            }
            else if (this.coreProcessor == null && ((EENetwork)network).coreProcessor != null) {
                newNetwork.coreProcessor = ((EENetwork)network).coreProcessor;
            }
        }
    }
    
    @Override
    public void splitNetwork(final IConnectionProvider splitPoint) {
        if (splitPoint instanceof TileEntity) {
            this.getConductors().remove(splitPoint);
            final TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();
            for (int i = 0; i < connectedBlocks.length; ++i) {
                final TileEntity connectedBlockA = connectedBlocks[i];
                if (connectedBlockA instanceof IConnectionProvider) {
                    for (int ii = 0; ii < connectedBlocks.length; ++ii) {
                        final TileEntity connectedBlockB = connectedBlocks[ii];
                        if (connectedBlockA != connectedBlockB && connectedBlockB instanceof IConnectionProvider) {
                            final Pathfinder finder = new PathfinderChecker(((TileEntity)splitPoint).getWorldObj(), (IConnectionProvider)connectedBlockB, new IConnectionProvider[] { splitPoint });
                            finder.init(new Vector3(connectedBlockA));
                            if (finder.results.size() > 0) {
                                for (final Vector3 node : (Iterable<Vector3>)finder.closedSet) {
                                    final TileEntity nodeTile = node.getTileEntity((IBlockAccess)((TileEntity)splitPoint).getWorldObj());
                                    if (nodeTile instanceof INetworkProvider && nodeTile != splitPoint) {
                                        ((INetworkProvider)nodeTile).setNetwork(this);
                                    }
                                }
                            }
                            else {
                                final IElectricityNetwork newNetwork = new EENetwork(new IConductor[0]);
                                for (final Vector3 node2 : (Iterable<Vector3>)finder.closedSet) {
                                    final TileEntity nodeTile2 = node2.getTileEntity((IBlockAccess)((TileEntity)splitPoint).getWorldObj());
                                    if (nodeTile2 instanceof INetworkProvider && nodeTile2 != splitPoint) {
                                        newNetwork.getConductors().add((IConductor) nodeTile2);
                                    }
                                }
                                newNetwork.cleanUpConductors();
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "RedstoneNetwork[" + this.hashCode() + "|Wires:" + this.getConductors().size() + "]";
    }
    
    @Override
    public void startProducing(final TileEntity tileEntity, final ElectricityPack electricityPack) {
        if (tileEntity != null && electricityPack.getWatts() > 0.0) {
            this.producers.put(tileEntity, electricityPack);
        }
    }
    
    @Override
    public void startProducing(final TileEntity tileEntity, final double amperes, final double voltage) {
        this.startProducing(tileEntity, new ElectricityPack(amperes, voltage));
    }
    
    @Override
    public boolean isProducing(final TileEntity tileEntity) {
        return this.producers.containsKey(tileEntity);
    }
    
    @Override
    public void stopProducing(final TileEntity tileEntity) {
        this.producers.remove(tileEntity);
    }
    
    @Override
    public void startRequesting(final TileEntity tileEntity, final ElectricityPack electricityPack) {
        if (tileEntity != null && electricityPack.getWatts() > 0.0) {
            this.consumers.put(tileEntity, electricityPack);
        }
    }
    
    @Override
    public void startRequesting(final TileEntity tileEntity, final double amperes, final double voltage) {
        this.startRequesting(tileEntity, new ElectricityPack(amperes, voltage));
    }
    
    @Override
    public boolean isRequesting(final TileEntity tileEntity) {
        return this.consumers.containsKey(tileEntity);
    }
    
    @Override
    public void stopRequesting(final TileEntity tileEntity) {
        this.consumers.remove(tileEntity);
    }
    
    @Override
    public ElectricityPack getProduced(final TileEntity... ignoreTiles) {
        final ElectricityPack totalElectricity = new ElectricityPack(0.0, 0.0);
        final Iterator<Map.Entry<TileEntity, ElectricityPack>> it = this.producers.entrySet().iterator();
    Label_0023:
        while (it.hasNext()) {
            final Map.Entry<TileEntity, ElectricityPack> pairs = it.next();
            if (pairs != null) {
                final TileEntity tileEntity = pairs.getKey();
                if (tileEntity == null) {
                    it.remove();
                }
                else if (tileEntity.isInvalid()) {
                    it.remove();
                }
                else if (tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) != tileEntity) {
                    it.remove();
                }
                else {
                    if (ignoreTiles != null) {
                        for (final TileEntity ignoreTile : ignoreTiles) {
                            if (tileEntity == ignoreTile) {
                                continue Label_0023;
                            }
                        }
                    }
                    final ElectricityPack pack = pairs.getValue();
                    if (pairs.getKey() == null || pairs.getValue() == null || pack == null) {
                        continue;
                    }
                    final double newWatts = totalElectricity.getWatts() + pack.getWatts();
                    final double newVoltage = Math.max(totalElectricity.voltage, pack.voltage);
                    totalElectricity.amperes = newWatts / newVoltage;
                    totalElectricity.voltage = newVoltage;
                }
            }
        }
        return totalElectricity;
    }
    
    @Override
    public ElectricityPack getRequest(final TileEntity... ignoreTiles) {
        final ElectricityPack totalElectricity = this.getRequestWithoutReduction();
        totalElectricity.amperes = Math.max(totalElectricity.amperes - this.getProduced(ignoreTiles).amperes, 0.0);
        return totalElectricity;
    }
    
    @Override
    public ElectricityPack getRequestWithoutReduction() {
        final ElectricityPack totalElectricity = new ElectricityPack(0.0, 0.0);
        final Iterator<Map.Entry<TileEntity, ElectricityPack>> it = this.consumers.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<TileEntity, ElectricityPack> pairs = it.next();
            if (pairs != null) {
                final TileEntity tileEntity = pairs.getKey();
                if (tileEntity == null) {
                    it.remove();
                }
                else if (tileEntity.isInvalid()) {
                    it.remove();
                }
                else if (tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) != tileEntity) {
                    it.remove();
                }
                else {
                    final ElectricityPack pack = pairs.getValue();
                    if (pack == null) {
                        continue;
                    }
                    final ElectricityPack electricityPack = totalElectricity;
                    electricityPack.amperes += pack.amperes;
                    totalElectricity.voltage = Math.max(totalElectricity.voltage, pack.voltage);
                }
            }
        }
        return totalElectricity;
    }
    
    @Override
    public ElectricityPack consumeElectricity(final TileEntity tileEntity) {
        ElectricityPack totalElectricity = new ElectricityPack(0.0, 0.0);
        try {
            final ElectricityPack tileRequest = this.consumers.get(tileEntity);
            if (this.consumers.containsKey(tileEntity) && tileRequest != null) {
                totalElectricity = this.getProduced(new TileEntity[0]);
                if (totalElectricity.getWatts() > 0.0) {
                    final ElectricityPack totalRequest = this.getRequestWithoutReduction();
                    final ElectricityPack electricityPack = totalElectricity;
                    electricityPack.amperes *= tileRequest.amperes / totalRequest.amperes;
                    final double ampsReceived = totalElectricity.amperes - totalElectricity.amperes * totalElectricity.amperes * this.getTotalResistance() / totalElectricity.voltage;
                    final double voltsReceived = totalElectricity.voltage - totalElectricity.amperes * this.getTotalResistance();
                    totalElectricity.amperes = ampsReceived;
                    totalElectricity.voltage = voltsReceived;
                    return totalElectricity;
                }
            }
        }
        catch (final Exception e) {
            FMLLog.severe("Failed to consume electricity!", new Object[0]);
            e.printStackTrace();
        }
        return totalElectricity;
    }
    
    @Override
    public HashMap getProducers() {
        return this.producers;
    }
    
    @Override
    public List getProviders() {
        final List providers = new ArrayList();
        providers.addAll(this.producers.keySet());
        return providers;
    }
    
    @Override
    public HashMap getConsumers() {
        return this.consumers;
    }
    
    @Override
    public List getReceivers() {
        final List receivers = new ArrayList();
        receivers.addAll(this.consumers.keySet());
        return receivers;
    }
    
    @Override
    public void refreshConductors() {
        this.cleanUpConductors();
        try {
            for (final IConductor conductor : this.conductors) {
                conductor.updateAdjacentConnections();
            }
        }
        catch (final Exception e) {
            FMLLog.severe("Universal Electricity: Failed to refresh conductor.", new Object[0]);
            e.printStackTrace();
        }
    }
    
    @Override
    public double getTotalResistance() {
        double resistance = 0.0;
        for (final IConductor conductor : this.conductors) {
            resistance += conductor.getResistance();
        }
        return resistance;
    }
    
    @Override
    public double getLowestCurrentCapacity() {
        double lowestAmp = 0.0;
        for (final IConductor conductor : this.conductors) {
            if (lowestAmp == 0.0 || conductor.getCurrentCapcity() < lowestAmp) {
                lowestAmp = conductor.getCurrentCapcity();
            }
        }
        return lowestAmp;
    }
    
    @Override
    public Set getConductors() {
        return this.conductors;
    }
    
    public List<IRedstoneNetAccessor> getRedstoneInterfacers() {
        return this.redstoneInterfacers;
    }
    
    public void addRsInterfacer(final IRedstoneNetAccessor interfacer) {
        this.redstoneInterfacers.add(interfacer);
    }

    public boolean isInactive() {
        return false;
    }

    public void tick() {
        
    }
}
