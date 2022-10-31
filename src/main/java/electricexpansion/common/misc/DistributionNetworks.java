package electricexpansion.common.misc;

import electricexpansion.common.ElectricExpansion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;

public class DistributionNetworks {
    private MinecraftServer server;
    public static final byte maxFrequencies = Byte.MIN_VALUE;
    private Map<String, double[]> playerFrequencies;

    public DistributionNetworks() {
        this.server = MinecraftServer.getServer();
        this.playerFrequencies = new HashMap<>();
    }

    public double getJoules(final String player, final byte frequency) {
        if (player != null) {
            if (!this.playerFrequencies.containsKey(player)) {
                this.playerFrequencies.put(player, new double[128]);
            }
            return ((double[]) this.playerFrequencies.get(player))[frequency];
        }
        return 0.0;
    }

    public void setJoules(final String player, final short frequency,
            final double newJoules) {
        if (player != null) {
            if (!this.playerFrequencies.containsKey(player)) {
                this.playerFrequencies.put(player, new double[128]);
            }
            ((double[]) this.playerFrequencies.get(player))[frequency] = newJoules;
        }
    }

    public void addJoules(final String player, final short frequency,
            final double addedJoules) {
        if (player != null) {
            if (!this.playerFrequencies.containsKey(player)) {
                this.playerFrequencies.put(player, new double[128]);
            }
            ((double[]) this.playerFrequencies.get(player))[frequency] += addedJoules;
        }
    }

    public void removeJoules(final String player, final short frequency,
            final double removedJoules) {
        try {
            if (player != null) {
                ((double[]) this.playerFrequencies.get(player))[frequency] -= removedJoules;
            }
        } catch (final Exception ex) {
        }
    }

    public static double getMaxJoules() {
        return 5000000.0;
    }

    public void onWorldSave(final WorldEvent event) {
        String folder = "";
        if (this.server.isDedicatedServer()) {
            folder = this.server.getFolderName();
        } else {
            folder = Minecraft.getMinecraft().mcDataDir + File.separator + "saves" +
                    File.separator + this.server.getFolderName();
        }
        if (!event.world.isRemote) {
            try {
                final File file = new File(folder + File.separator + "ElectricExpansion");
                if (!file.exists()) {
                    file.mkdirs();
                }
                String[] players = new String[this.playerFrequencies.size()];
                players = (String[]) this.playerFrequencies.keySet().toArray(players);
                for (int i = 0; i < this.playerFrequencies.size(); ++i) {
                    final File var3 = new File(file + File.separator + players[i] + "_tmp.dat");
                    final File var4 = new File(file + File.separator + players[i] + ".dat");
                    final File var5 = new File(file + File.separator + players[i] + "_Backup.dat");
                    final NBTTagCompound nbt = new NBTTagCompound();
                    for (int j = 0; j < ((double[]) this.playerFrequencies.get(players[i])).length; ++j) {
                        if (((double[]) this.playerFrequencies.get(players[i]))[j] > 0.0) {
                            nbt.setDouble(j + "", ((double[]) this.playerFrequencies.get(
                                    players[i]))[j]);
                            CompressedStreamTools.writeCompressed(
                                    nbt, (OutputStream) new FileOutputStream(var3));
                        }
                    }
                    if (var5.exists()) {
                        var5.delete();
                    }
                    if (var4.exists()) {
                        var4.renameTo(var5);
                    }
                    var3.renameTo(var4);
                }
            } catch (final Exception e) {
                ElectricExpansion.eeLogger.severe(
                        "Failed to save the Quantum Battery Box Electricity Storage Data!");
            }
        }
        if (event instanceof WorldEvent.Unload) {
            this.playerFrequencies.clear();
        }
    }

    public void onWorldLoad() {
        try {
            for (final File playerFile : this.ListSaves()) {
                if (playerFile.exists()) {
                    String name = playerFile.getName();
                    if (!name.contains("_Backup")) {
                        if (name.endsWith(".dat")) {
                            name = name.substring(0, name.length() - 4);
                        }
                        this.playerFrequencies.put(name, new double[128]);
                        for (int i = 0; i < 128; ++i) {
                            try {
                                ((double[]) this.playerFrequencies.get(name))[i] = CompressedStreamTools
                                        .readCompressed(
                                                (InputStream) new FileInputStream(playerFile))
                                        .getDouble(i + "");
                            } catch (final Exception e) {
                                ((double[]) this.playerFrequencies.get(name))[i] = 0.0;
                            }
                        }
                    }
                }
            }
        } catch (final Exception e2) {
            ElectricExpansion.eeLogger.warning(
                    "Failed to load the Quantum Battery Box Electricity Storage Data!");
            ElectricExpansion.eeLogger.warning(
                    "If this is the first time loading the world after the mod was installed, there are no problems.");
        }
        String[] players = new String[this.playerFrequencies.size()];
        players = (String[]) this.playerFrequencies.keySet().toArray(players);
        String playerString = "";
        for (final String player : players) {
            playerString = playerString + ", " + player;
        }
        ElectricExpansion.eeLogger.warning(playerString);
    }

    public File[] ListSaves() {
        String folder = "";
        if (this.server.isDedicatedServer()) {
            folder = this.server.getFolderName() + File.separator + "ElectricExpansion";
        } else if (!this.server.isDedicatedServer()) {
            folder = Minecraft.getMinecraft().mcDataDir + File.separator + "saves" +
                    File.separator + this.server.getFolderName() + File.separator +
                    "ElectricExpansion";
        }
        final File folderToUse = new File(folder);
        final File[] listOfFiles = folderToUse.listFiles();
        return listOfFiles;
    }
}
