package electricexpansion.common.helpers;

import cpw.mods.fml.common.FMLCommonHandler;

public class PlayerHelper {
    public static boolean isPlayerOp(String playerName) {
        String[] ops = FMLCommonHandler.instance()
                .getMinecraftServerInstance()
                .getConfigurationManager()
                .func_152606_n();

        for (String op : ops) {
            if (playerName.equalsIgnoreCase(op))
                return true;
        }
        return false;
    }
}
