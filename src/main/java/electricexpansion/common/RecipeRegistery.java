package electricexpansion.common;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.misc.WireMillRecipes;
import java.util.logging.Level;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import universalelectricity.prefab.RecipeHelper;

public class RecipeRegistery {
  private static ItemStack camo;
  private static final ItemStack insulationIS;

  public static void crafting() {
    if (Loader.isModLoaded("BasicComponents")) {
      for (final ItemStack wire : OreDictionary.getOres("copperWire")) {
        RecipeHelper.removeRecipe(wire);
      }
    }
    FurnaceRecipes.smelting().func_151396_a(
        ElectricExpansionItems.itemParts, // 3,
        new ItemStack(ElectricExpansionItems.itemParts, 4, 4), 0.0f);
    try {
      RecipeRegistery.camo =
          GameRegistry.findItemStack("ICBM|Contraption", "camouflage", 1);
    } catch (final NullPointerException ex) {
    }
    if (RecipeRegistery.camo == null) {
      ElectricExpansion.log(Level.INFO, "Failed to detect ICBM|Contraption",
                            new String[0]);
      ElectricExpansion.log(Level.INFO, "Using %s's items instead.",
                            "Electric Expansion");
      RecipeRegistery.camo = new ItemStack(ElectricExpansionItems.itemParts, 1, 5);
      GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
          RecipeRegistery.camo,
          new Object[] {Blocks.wool, Items.bucket, Items.slime_ball,
                        Items.redstone, "dyeRed", "dyeBlue", "dyeYellow",
                        "dyeBlack", "dyeWhite"}));
    }
    registerRawCables();
    registerInsulatedCables();
    registerSwitchCables();
    registerLogisticsCables();
    registerCamoCables();
    registerCamoSwitchCables();
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockWireMill),
        new Object[] {"#$#", "!%!", "@!@", '!', "motor", '#', "plateSteel", '@',
                      "plateBronze", '$', "circuitBasic", '%',
                      new ItemStack(ElectricExpansionItems.itemParts, 1, 0)}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockMultimeter),
        new Object[] {"$^$", "!@!", "$%$", '!', "plateCopper", '$',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      '%', "circuitBasic", '^', Blocks.glass, '@',
                      Items.stick}));
    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALEC " + ElectricExpansionItems.blockInsulatedWire);
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockAdvBatteryBox),
        new Object[] {"!!!", "@@@", "!#!", '!', "battery", '@', "copperWire",
                      '#', "circuitBasic"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulationMachine),
        new Object[] {"!@!", "@#@", "!$!", '!', "plateSteel", '@',
                      Blocks.obsidian, '#', Items.lava_bucket, '$',
                      Blocks.furnace}));
    if (OreDictionary.getOres("antimatterGram").size() > 0) {
      GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
          new ItemStack(ElectricExpansionItems.blockDistribution),
          new Object[] {"!!!", "!@!", "!!!", '@',
                        new ItemStack(ElectricExpansionItems.blockAdvBatteryBox), '!',
                        "antimatterGram"}));
    } else {
      GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
          new ItemStack(ElectricExpansionItems.blockDistribution),
          new Object[] {"!!!", "!@!", "!!!", '@',
                        new ItemStack(ElectricExpansionItems.blockAdvBatteryBox), '!',
                        new ItemStack(ElectricExpansionItems.itemParts, 1, 1)}));
    }
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockTransformer, 1, 0),
        new Object[] {"$&$", "#x#", "@@@", '@', "plateSteel", '#',
                      new ItemStack(ElectricExpansionItems.itemParts, 1, 6),
                      '$',
                      new ItemStack(ElectricExpansionItems.itemParts, 1, 8),
                      '&', "ingotCopper", 'x', "dyeGreen"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4),
        new Object[] {
            "$&$", "#x#", "@!@", '!',
            new ItemStack(ElectricExpansionItems.blockTransformer, 1, 0), '@',
            "plateSteel", '#',
            new ItemStack(ElectricExpansionItems.itemParts, 1, 6), '$',
            new ItemStack(ElectricExpansionItems.itemParts, 1, 8), '&',
            "ingotCopper", 'x', "dyeRed"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockTransformer, 1, 8),
        new Object[] {
            "$&$", "#x#", "@!@", '!',
            new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), '@',
            "plateSteel", '#',
            new ItemStack(ElectricExpansionItems.itemParts, 1, 6), '$',
            new ItemStack(ElectricExpansionItems.itemParts, 1, 8), '&',
            "ingotCopper", 'x', "dyeBlue"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 0),
        new Object[] {"$!$", "!@!", "#!#", '!',
                      new ItemStack((Item)ElectricExpansionItems.itemAdvBat, 1,
                                    Integer.MIN_VALUE),
                      '@', new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 0),
                      '#', "circuitAdvanced", '$', "plateSteel"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 1),
        new Object[] {"$!$", "!@!", "#!#", '!',
                      new ItemStack((Item)ElectricExpansionItems.itemAdvBat, 1,
                                    Integer.MIN_VALUE),
                      '@', new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 0),
                      '#', "circuitAdvanced", '$', "plateSteel"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 2),
        new Object[] {"#!#", "!@!", "#!#", '!',
                      new ItemStack((Item)ElectricExpansionItems.itemEliteBat,
                                    1, Integer.MIN_VALUE),
                      '@', new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 1),
                      '#', "circuitElite"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 3),
        new Object[] {"#!#", "!@!", "#!#", '!', "antimatterMilligram", '@',
                      new ItemStack((Item)ElectricExpansionItems.itemUltimateBat, 1,
                                    Integer.MIN_VALUE),
                      '#', "circuitElite"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 4),
        new Object[] {
            "#$#", "#!#", "#$#", '!',
            new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), '#',
            new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0), '$',
            "circuitBasic"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 5),
        new Object[] {
            "#$#", "#!#", "#$#", '!',
            new ItemStack(ElectricExpansionItems.blockTransformer, 1, 8), '#',
            new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3), '$',
            "circuitElite"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemUpgrade, 1, 6),
        new Object[] {
            "@$#", "@!#", "@$#", '!',
            new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), '#',
            new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3), '@',
            new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0), '$',
            "circuitAdvanced"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack((Item)ElectricExpansionItems.itemAdvBat),
        new Object[] {" T ", "TRT", "TRT", 'T', "ingotSilver", 'R',
                      Items.glowstone_dust}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack((Item)ElectricExpansionItems.itemEliteBat),
        new Object[] {"!@!", "#$#", "!@!", '!', "plateSteel", '@',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      '#', "ingotLead", '$', Items.ghast_tear}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack((Item)ElectricExpansionItems.itemUltimateBat),
        new Object[] {"!@!", "#$#", "!@!", '!', "plateGold", '@',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
                      '#', "antimatterMilligram", '$', "strangeMatter"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemParts, 1, 0),
        new Object[] {" # ", "! !", " ! ", '!', Items.iron_ingot, '#',
                      Items.diamond}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemParts, 1, 1),
        new Object[] {"!#!", "#@#", "!#!", '!', Items.gold_ingot, '#',
                      "ingotSilver", '@', Items.ender_eye}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemParts, 1, 1),
        new Object[] {"!#!", "#@#", "!#!", '#', Items.gold_ingot, '!',
                      "ingotSilver", '@', Items.ender_eye}));
    FurnaceRecipes.smelting().func_151396_a(
        ElectricExpansionItems.itemParts, // 1,
        new ItemStack(ElectricExpansionItems.itemParts, 4, 2), 0.0f);
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.itemParts, 9, 7),
        new Object[] {ElectricExpansionItems.blockLead});
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemParts, 1, 8),
        new Object[] {
            "AAA", "ABA", "AAA", 'B', Items.iron_ingot, 'A',
            new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0)}));
    GameRegistry.addSmelting(
        ElectricExpansionItems.blockSilverOre,
        new ItemStack(ElectricExpansionItems.itemParts, 1, 9), 0.8f);
    GameRegistry.addRecipe(
        new ItemStack(ElectricExpansionItems.blockLead, 1),
        new Object[] {"@@@", "@@@", "@@@", '@',
                      new ItemStack(ElectricExpansionItems.itemParts, 1, 7)});
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.itemMultimeter),
        new Object[] {"$^$", "!@!", "$%$", '!', "plateCopper", '$',
                      "copperWire", '%', "circuitAdvanced", '^', Blocks.glass,
                      '@', "battery"}));
  }

  public static void drawing() {
    if (!ElectricExpansion.debugRecipes) {
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotCopper", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 0), 60);
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotTin", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 1), 60);
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotSilver", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 2), 60);
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotAluminum", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 3),
          60);
      WireMillRecipes.INSTANCE.addProcessing(
          new ItemStack(ElectricExpansionItems.itemParts, 64, 2),
          new ItemStack(ElectricExpansionItems.blockRawWire, 64, 4), 24000);
      for (int i = 0; i < 16; ++i) {
        WireMillRecipes.INSTANCE.addProcessing(
            new ItemStack(Blocks.wool, 10, i), new ItemStack(Items.string, 40),
            300);
      }
    } else {
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotCopper", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 0), 20);
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotTin", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 1), 20);
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotSilver", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 2), 20);
      WireMillRecipes.INSTANCE.addProcessing(
          "ingotAluminum", new ItemStack(ElectricExpansionItems.blockRawWire, 3, 3),
          20);
      WireMillRecipes.INSTANCE.addProcessing(
          new ItemStack(ElectricExpansionItems.itemParts, 64, 2),
          new ItemStack(ElectricExpansionItems.blockRawWire, 64, 4), 20);
      for (int i = 0; i < 16; ++i) {
        WireMillRecipes.INSTANCE.addProcessing(
            new ItemStack(Blocks.wool, 10, i), new ItemStack(Items.string, 40),
            30);
      }
    }
  }

  public static void insulation() {
    final int ticks = ElectricExpansion.debugRecipes ? 1 : 8;
    for (int i = 0; i < 16; ++i) {
      InsulationRecipes.INSTANCE.addProcessing(
          new ItemStack(Blocks.wool, 32, i), 32, ticks * 80);
    }
    InsulationRecipes.INSTANCE.addProcessing(new ItemStack(Items.leather, 8),
                                             24, ticks * 25);
    InsulationRecipes.INSTANCE.addProcessing(
        new ItemStack(Items.rotten_flesh, 8), 16, ticks * 25);
    FurnaceRecipes.smelting().func_151396_a(Items.leather,
                                            RecipeRegistery.insulationIS, 0.7f);
    GameRegistry.addShapelessRecipe(
        RecipeRegistery.insulationIS,
        new Object[] {Blocks.wool, Blocks.wool, Blocks.wool, Blocks.wool});
    for (final ItemStack is : OreDictionary.getOres("itemRubber")) {
      if (!is.isItemEqual(RecipeRegistery.insulationIS)) {
        final ItemStack is2 = is.copy();
        is2.stackSize = 8;
        InsulationRecipes.INSTANCE.addProcessing(
            is2, is2.stackSize,
            (int)Math.floor(is2.stackSize * ticks * 10.0 / 8.0 + 0.5));
      }
    }
  }

  public static void registerRawCables() {
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRawWire, 6, 0),
        new Object[] {" @ ", " @ ", " @ ", '@', "ingotCopper"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRawWire, 6, 1),
        new Object[] {" @ ", " @ ", " @ ", '@', "ingotTin"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRawWire, 6, 2),
        new Object[] {" @ ", " @ ", " @ ", '@', "ingotSilver"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRawWire, 6, 3),
        new Object[] {" @ ", " @ ", " @ ", '@', "ingotAluminum"}));
  }

  public static void registerInsulatedCables() {
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 6, 0),
        new Object[] {"#@#", "#@#", "#@#", '#', RecipeRegistery.insulationIS,
                      '@', "ingotCopper"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 6, 1),
        new Object[] {"#@#", "#@#", "#@#", '#', RecipeRegistery.insulationIS,
                      '@', "ingotTin"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 6, 2),
        new Object[] {"#@#", "#@#", "#@#", '#', RecipeRegistery.insulationIS,
                      '@', "ingotSilver"}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 6, 3),
        new Object[] {"#@#", "#@#", "#@#", '#', RecipeRegistery.insulationIS,
                      '@', "ingotAluminum"}));
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 0),
                      RecipeRegistery.insulationIS});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 1),
                      RecipeRegistery.insulationIS});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 2),
                      RecipeRegistery.insulationIS});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 3),
                      RecipeRegistery.insulationIS});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 4),
                      new ItemStack(ElectricExpansionItems.itemParts, 3, 7)});
  }

  public static void registerSwitchCables() {
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 0),
                      RecipeRegistery.insulationIS, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 1),
                      RecipeRegistery.insulationIS, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 2),
                      RecipeRegistery.insulationIS, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 3),
                      RecipeRegistery.insulationIS, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 4),
                      new ItemStack(ElectricExpansionItems.itemParts, 3, 7),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
                      Blocks.lever});
  }

  public static void registerLogisticsCables() {
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 0),
                      RecipeRegistery.insulationIS, Blocks.lever,
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 1),
                      RecipeRegistery.insulationIS, Blocks.lever,
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 2),
                      RecipeRegistery.insulationIS, Blocks.lever,
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 3),
                      RecipeRegistery.insulationIS, Blocks.lever,
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 4),
                      new ItemStack(ElectricExpansionItems.itemParts, 3, 7),
                      Blocks.lever, Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      Blocks.lever, Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1),
                      Blocks.lever, Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2),
                      Blocks.lever, Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3),
                      Blocks.lever, Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
                      Blocks.lever, Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 0),
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 1),
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 2),
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 3),
                      Items.redstone}));
    GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
        new ItemStack(ElectricExpansionItems.blockLogisticsWire, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 4),
                      Items.redstone}));
  }

  public static void registerCamoCables() {
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 0),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 1),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 2),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 3),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 4),
                      new ItemStack(ElectricExpansionItems.itemParts, 3, 7),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
                      RecipeRegistery.camo});
  }

  public static void registerCamoSwitchCables() {
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 0),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo,
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 1),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo,
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 2),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo,
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 3),
                      RecipeRegistery.insulationIS, RecipeRegistery.camo,
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockRawWire, 1, 4),
                      new ItemStack(ElectricExpansionItems.itemParts, 3, 7),
                      RecipeRegistery.camo, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      RecipeRegistery.camo, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1),
                      RecipeRegistery.camo, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2),
                      RecipeRegistery.camo, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3),
                      RecipeRegistery.camo, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
                      RecipeRegistery.camo, Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 0),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 1),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 2),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 3),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockSwitchWire, 1, 4),
                      RecipeRegistery.camo});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 0),
        new Object[] {new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 0),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 1),
        new Object[] {new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 1),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 2),
        new Object[] {new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 2),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 3),
        new Object[] {new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 3),
                      Blocks.lever});
    GameRegistry.addShapelessRecipe(
        new ItemStack(ElectricExpansionItems.blockSwitchWireBlock, 1, 4),
        new Object[] {new ItemStack(ElectricExpansionItems.blockWireBlock, 1, 4),
                      Blocks.lever});
  }

  public static void registerRedstoneCables() {
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 0),
        new Object[] {"!@!", "@#@", "!@!", '!',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      '@', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 1),
        new Object[] {"!@!", "@#@", "!@!", '!',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1),
                      '@', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 2),
        new Object[] {"!@!", "@#@", "!@!", '!',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2),
                      '@', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 3),
        new Object[] {"!@!", "@#@", "!@!", '!',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3),
                      '@', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 4),
        new Object[] {"!@!", "@#@", "!@!", '!',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
                      '@', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 0),
        new Object[] {"!@!", "@#@", "!@!", '@',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0),
                      '!', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 1),
        new Object[] {"!@!", "@#@", "!@!", '@',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1),
                      '!', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 2),
        new Object[] {"!@!", "@#@", "!@!", '@',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2),
                      '!', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 3),
        new Object[] {"!@!", "@#@", "!@!", '@',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3),
                      '!', Items.redstone, '#', Items.slime_ball}));
    GameRegistry.addRecipe((IRecipe) new ShapedOreRecipe(
        new ItemStack(ElectricExpansionItems.blockRedstonePaintedWire, 4, 4),
        new Object[] {"!@!", "@#@", "!@!", '@',
                      new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4),
                      '!', Items.redstone, '#', Items.slime_ball}));
  }

  public static ItemStack getInsulationIS() {
    return RecipeRegistery.insulationIS;
  }

  static {
    insulationIS = new ItemStack(ElectricExpansionItems.itemParts, 1, 6);
  }
}
