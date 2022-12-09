package electricexpansion.common;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.blocks.BlockAdvancedBatteryBox;
import electricexpansion.common.blocks.BlockBasic;
import electricexpansion.common.blocks.BlockInsulatedWire;
import electricexpansion.common.blocks.BlockInsulationMachine;
import electricexpansion.common.blocks.BlockLogisticsWire;
import electricexpansion.common.blocks.BlockMultimeter;
import electricexpansion.common.blocks.BlockQuantumBatteryBox;
import electricexpansion.common.blocks.BlockRawWire;
import electricexpansion.common.blocks.BlockRedstoneNetworkCore;
import electricexpansion.common.blocks.BlockRedstonePaintedWire;
import electricexpansion.common.blocks.BlockSwitchWire;
import electricexpansion.common.blocks.BlockSwitchWireBlock;
import electricexpansion.common.blocks.BlockTransformer;
import electricexpansion.common.blocks.BlockWireBlock;
import electricexpansion.common.blocks.BlockWireMill;
import electricexpansion.common.helpers.PacketHandlerLogisticsWireButton;
import electricexpansion.common.helpers.PacketHandlerUpdateQuantumBatteryBoxFrequency;
import electricexpansion.common.helpers.PacketLogisticsWireButton;
import electricexpansion.common.helpers.PacketUpdateQuantumBatteryBoxFrequency;
import electricexpansion.common.itemblocks.ItemBlockInsulatedWire;
import electricexpansion.common.itemblocks.ItemBlockLogisticsWire;
import electricexpansion.common.itemblocks.ItemBlockRawWire;
import electricexpansion.common.itemblocks.ItemBlockRedstonePaintedWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWireBlock;
import electricexpansion.common.itemblocks.ItemBlockTransformer;
import electricexpansion.common.itemblocks.ItemBlockWireBlock;
import electricexpansion.common.items.ItemAdvancedBattery;
import electricexpansion.common.items.ItemEliteBattery;
import electricexpansion.common.items.ItemMultimeter;
import electricexpansion.common.items.ItemParts;
import electricexpansion.common.items.ItemUltimateBattery;
import electricexpansion.common.items.ItemUpgrade;
import electricexpansion.common.misc.DistributionNetworks;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.misc.EventHandler;
import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.ore.OreGenBase;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;

@Mod(
    modid = "ElectricExpansion", name = "Electric Expansion", version = ElectricExpansion.VERSION,
    dependencies =
        "after:basiccomponents;after:AtomicScience;after:ICBM|Contraption;after:MineFactoryReloaded;after:IC2",
    useMetadata = true)
public class ElectricExpansion {
  public static final String MAJOR_VERSION = "2";
  public static final String MINOR_VERSION = "1";
  public static final String REVIS_VERSION = "0";
  public static final String BUILD_VERSION = "43";
  public static final String MOD_ID = "ElectricExpansion";
  public static final String MOD_NAME = "Electric Expansion";
  public static final String VERSION = "1.0.0";
  public static final boolean USE_METADATA = true;
  public static final boolean USES_CLIENT = true;
  public static final boolean USES_SERVER = false;
  @Mod.Metadata("ElectricExpansion") public static ModMetadata meta;
  public static final String TEXTURE_NAME_PREFIX = "electricexpansion:";
  private static final String[] LANGUAGES_SUPPORTED;
  public static OreGenBase silverOreGeneration;
  public static final Configuration CONFIG;
  public static boolean configLoaded;
  static boolean debugRecipes;
  public static boolean useHashCodes;
  private static boolean useUeVoltageSensitivity;
  public static DistributionNetworks DistributionNetworksInstance;
  public static Logger eeLogger;
  @Mod.Instance("ElectricExpansion") public static ElectricExpansion instance;
  @SidedProxy(clientSide = "electricexpansion.client.ClientProxy",
              serverSide = "electricexpansion.common.CommonProxy")
  public static CommonProxy proxy;
  public static SimpleNetworkWrapper channel;

  public static void log(final Level level, String msg,
                         final String... replacements) {
    for (final String replace : replacements) {
      msg = msg.replace("%s", replace);
    }
    ElectricExpansion.eeLogger.log(level, msg);
  }

  public static boolean configLoad(final Configuration config) {
    config.load();
    ElectricExpansionItems.blockRawWire = (Block) new BlockRawWire();
    ElectricExpansionItems.blockInsulatedWire =
        (Block) new BlockInsulatedWire();
    ElectricExpansionItems.blockWireBlock = (Block) new BlockWireBlock();
    ElectricExpansionItems.blockSwitchWire = (Block) new BlockSwitchWire();
    ElectricExpansionItems.blockSwitchWireBlock =
        (Block) new BlockSwitchWireBlock();
    ElectricExpansionItems.blockRedstonePaintedWire =
        new BlockRedstonePaintedWire();
    ElectricExpansionItems.blockAdvBatteryBox =
        (Block) new BlockAdvancedBatteryBox();
    ElectricExpansionItems.blockMultimeter = (Block) new BlockMultimeter();
    ElectricExpansionItems.blockSilverOre =
        new BlockBasic(Material.rock, EETab.INSTANCE, 2.0f, "SilverOre");
    ElectricExpansionItems.blockInsulationMachine =
        (Block) new BlockInsulationMachine();
    ElectricExpansionItems.blockWireMill = (Block) new BlockWireMill();
    ElectricExpansionItems.blockTransformer = (Block) new BlockTransformer();
    ElectricExpansionItems.blockDistribution =
        (Block) new BlockQuantumBatteryBox();
    ElectricExpansionItems.blockLead =
        new BlockBasic(Material.iron, EETab.INSTANCE, 2.0f, "LeadBlock");
    ElectricExpansionItems.blockLogisticsWire =
        (Block) new BlockLogisticsWire(0);
    ElectricExpansionItems.blockRedstoneNetworkCore =
        (Block) new BlockRedstoneNetworkCore();
    ElectricExpansionItems.itemUpgrade = new ItemUpgrade(0);
    ElectricExpansionItems.itemEliteBat = new ItemEliteBattery();
    ElectricExpansionItems.itemUltimateBat = new ItemUltimateBattery();
    ElectricExpansionItems.itemParts = new ItemParts();
    ElectricExpansionItems.itemAdvBat = new ItemAdvancedBattery();
    ElectricExpansionItems.itemMultimeter = new ItemMultimeter();

    GameRegistry.registerItem(ElectricExpansionItems.itemUpgrade,
                              "itemUpgrade");
    GameRegistry.registerItem(ElectricExpansionItems.itemEliteBat,
                              "itemEliteBat");
    GameRegistry.registerItem(ElectricExpansionItems.itemUltimateBat,
                              "itemUltimateBat");
    GameRegistry.registerItem(ElectricExpansionItems.itemParts, "itemParts");
    GameRegistry.registerItem(ElectricExpansionItems.itemAdvBat, "itemAdvBat");
    GameRegistry.registerItem(ElectricExpansionItems.itemMultimeter,
                              "itemMultimeter");

    GameRegistry.registerBlock(ElectricExpansionItems.blockSilverOre,
                               ItemBlock.class, "blockSilverOre");
    ElectricExpansion.silverOreGeneration =
        new OreGenReplaceStone(
            "Silver Ore", "oreSilver",
            new ItemStack(ElectricExpansionItems.blockSilverOre), 36, 10, 3)
            .enable(config);
    ElectricExpansion.debugRecipes =
        config
            .get("General", "Debug_Recipes", false,
                 "Set to true for debug Recipes. This is considdered cheating.")
            .getBoolean(false);
    ElectricExpansion.useHashCodes =
        config
            .get(
                "General", "Use_Hashcodes", true,
                "Set to true to make clients use hash codes for the Quantum Battery Box Owner data.")
            .getBoolean(true);
    ElectricExpansion.useUeVoltageSensitivity =
        config
            .get(
                "General", "Use_UeVoltageSensitivity", false,
                "Set to true to use the setting in the UE config file for Voltage Sensitivity.")
            .getBoolean(false);
    if (config.hasChanged()) {
      config.save();
    }
    return ElectricExpansion.configLoaded = true;
  }

  @Mod.EventHandler
  public void preInit(final FMLPreInitializationEvent event) {
    ElectricExpansion.meta.modId = "ElectricExpansion";
    ElectricExpansion.meta.name = "Electric Expansion";
    ElectricExpansion.meta.description =
        "Electric Expansion is a Universal Electricity mod that focuses mainly on energy storage and transfer as well as adding more cables for better energy transfer. This mod will make Universal Electricity more complex and realistic. We try to make all aspects as realistic as possible, whether that means the items and block names or the processes and materials for each aspect of Electric Expansion.";
    ElectricExpansion.meta.url =
        "http://universalelectricity.com/electric%20expansion";
    ElectricExpansion.meta.logoFile = "/EELogo.png";
    ElectricExpansion.meta.version = "2.1.0.43";
    ElectricExpansion.meta.authorList =
        Arrays.asList("Mattredsox & Alex_hawks");
    ElectricExpansion.meta.credits = "Please see the website.";
    ElectricExpansion.meta.autogenerated = false;
    if (!ElectricExpansion.configLoaded) {
      configLoad(ElectricExpansion.CONFIG);
    }
    log(Level.INFO, "PreInitializing ElectricExpansion v.2.1.0", new String[0]);
    GameRegistry.registerBlock(ElectricExpansionItems.blockAdvBatteryBox,
                               ItemBlock.class, "blockAdvBatteryBox");
    GameRegistry.registerBlock(ElectricExpansionItems.blockWireMill,
                               ItemBlock.class, "blockWireMill");
    GameRegistry.registerBlock(ElectricExpansionItems.blockInsulationMachine,
                               ItemBlock.class, "blockInsulationMachine");
    GameRegistry.registerBlock(ElectricExpansionItems.blockMultimeter,
                               ItemBlock.class, "blockMultimeter");
    GameRegistry.registerBlock(ElectricExpansionItems.blockLead,
                               ItemBlock.class, "blockLead");
    GameRegistry.registerBlock(ElectricExpansionItems.blockTransformer,
                               ItemBlockTransformer.class, "blockTransformer");
    GameRegistry.registerBlock(ElectricExpansionItems.blockDistribution,
                               ItemBlock.class, "blockDistribution");
    GameRegistry.registerBlock(ElectricExpansionItems.blockRedstoneNetworkCore,
                               ItemBlock.class, "blockRsNetworkCore");
    GameRegistry.registerBlock(ElectricExpansionItems.blockRawWire,
                               ItemBlockRawWire.class, "blockRawWire");
    GameRegistry.registerBlock(ElectricExpansionItems.blockInsulatedWire,
                               ItemBlockInsulatedWire.class,
                               "blockInsulatedWire");
    GameRegistry.registerBlock(ElectricExpansionItems.blockSwitchWire,
                               ItemBlockSwitchWire.class, "blockSwitchWire");
    GameRegistry.registerBlock(ElectricExpansionItems.blockSwitchWireBlock,
                               ItemBlockSwitchWireBlock.class,
                               "blockSwitchWireBlock");
    GameRegistry.registerBlock(ElectricExpansionItems.blockWireBlock,
                               ItemBlockWireBlock.class, "blockWireBlock");
    GameRegistry.registerBlock(ElectricExpansionItems.blockLogisticsWire,
                               ItemBlockLogisticsWire.class,
                               "blockLogisticsWire");
    GameRegistry.registerBlock(ElectricExpansionItems.blockRedstonePaintedWire,
                               ItemBlockRedstonePaintedWire.class,
                               "blockRedstonePaintedWire");
    OreDictionary.registerOre("blockLead", ElectricExpansionItems.blockLead);
    OreDictionary.registerOre("advancedBattery",
                              (Item)ElectricExpansionItems.itemAdvBat);
    OreDictionary.registerOre("eliteBattery",
                              (Item)ElectricExpansionItems.itemEliteBat);
    OreDictionary.registerOre("advancedBattery",
                              (Item)ElectricExpansionItems.itemAdvBat);
    OreDictionary.registerOre("transformer",
                              ElectricExpansionItems.blockTransformer);
    OreDictionary.registerOre("wireMill", ElectricExpansionItems.blockWireMill);
    OreDictionary.registerOre("multimeter",
                              ElectricExpansionItems.blockMultimeter);
    OreDictionary.registerOre("itemMultimeter",
                              ElectricExpansionItems.itemMultimeter);
    OreDictionary.registerOre(
        "ingotElectrum", new ItemStack(ElectricExpansionItems.itemParts, 1, 2));
    OreDictionary.registerOre(
        "ingotLead", new ItemStack(ElectricExpansionItems.itemParts, 1, 7));
    OreDictionary.registerOre(
        "coil", new ItemStack(ElectricExpansionItems.itemParts, 1, 8));
    OreDictionary.registerOre(
        "ingotSilver", new ItemStack(ElectricExpansionItems.itemParts, 1, 9));
    OreDictionary.registerOre(
        "copperWire",
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0));
    OreDictionary.registerOre(
        "tinWire",
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1));
    OreDictionary.registerOre(
        "silverWire",
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2));
    OreDictionary.registerOre(
        "aluminumWire",
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3));
    OreDictionary.registerOre(
        "superconductor",
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4));
    OreDictionary.registerOre("insulation", RecipeRegistery.getInsulationIS());
    NetworkRegistry.INSTANCE.registerGuiHandler(
        (Object)this, (IGuiHandler)ElectricExpansion.proxy);
    if (!Loader.isModLoaded("BasicComponents")) {
      ElectricExpansion.eeLogger.fine(
          "Basic Components NOT detected! Basic Components is REQUIRED for survival crafting and gameplay!");
    }

    channel = NetworkRegistry.INSTANCE.newSimpleChannel("ElectricExpansion");
    int pkgId = 0;
    channel.registerMessage(PacketHandlerUpdateQuantumBatteryBoxFrequency.class,
                            PacketUpdateQuantumBatteryBoxFrequency.class,
                            pkgId++, Side.SERVER);

    channel.registerMessage(PacketHandlerLogisticsWireButton.class,
                            PacketLogisticsWireButton.class, pkgId++,
                            Side.SERVER);
  }

  @Mod.EventHandler
  public void load(final FMLInitializationEvent event) {
    log(Level.INFO, "Initializing ElectricExpansion v.2.1.0", new String[0]);
    ElectricExpansion.proxy.init();
    RecipeRegistery.crafting();
    EETab.INSTANCE.setItemStack(
        new ItemStack(ElectricExpansionItems.blockTransformer));
    TranslationHelper.loadLanguages("/assets/electricexpansion/lang/",
                                    ElectricExpansion.LANGUAGES_SUPPORTED);
    ElectricExpansion.eeLogger.info("Loaded languages");
    if (!ElectricExpansion.useUeVoltageSensitivity) {
      UniversalElectricity.isVoltageSensitive = true;
      ElectricExpansion.eeLogger.finest(
          "Successfully toggled Voltage Sensitivity!");
    }
    OreGenerator.addOre(ElectricExpansion.silverOreGeneration);
    UniversalElectricity.isNetworkActive = true;
  }

  @Mod.EventHandler
  public void postInit(final FMLPostInitializationEvent event) {
    log(Level.INFO, "PostInitializing ElectricExpansion v.2.1.0",
        new String[0]);
    MinecraftForge.EVENT_BUS.register((Object) new EventHandler());
    RecipeRegistery.drawing();
    RecipeRegistery.insulation();
  }

  @Mod.EventHandler
  public void onServerStarting(final FMLServerStartingEvent event) {
    ElectricExpansion.DistributionNetworksInstance = new DistributionNetworks();
  }

  static {
    LANGUAGES_SUPPORTED = new String[] {"en_US", "pl_PL"};
    CONFIG = new Configuration(
        new File(Loader.instance().getConfigDir(),
                 "UniversalElectricity/ElectricExpansion.cfg"));
    ElectricExpansion.configLoaded = false;
    ElectricExpansion.eeLogger = Logger.getLogger("ElectricExpansion");
  }
}
