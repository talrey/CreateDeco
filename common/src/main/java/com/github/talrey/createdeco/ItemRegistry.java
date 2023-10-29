package com.github.talrey.createdeco;

import com.github.talrey.createdeco.api.Coins;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.function.Function;

public class ItemRegistry {
  public static HashMap<String, Function<String, Item>> METAL_TYPES = new HashMap<>();

  public static HashMap<String, ItemEntry<Item>> COINS= new HashMap<>();
  public static HashMap<String, ItemEntry<CoinStackItem>> COINSTACKS = new HashMap<>();

  public static void init () {
    CreateDecoMod.LOGGER.info("Registering items for " + CreateDecoMod.NAME);
    CreateDecoMod.REGISTRATE.defaultCreativeTab("props_tab");

    METAL_TYPES.put("Andesite", (str) -> AllItems.ANDESITE_ALLOY.get());
    METAL_TYPES.put("Zinc", (str) -> AllItems.ZINC_INGOT.get());
    METAL_TYPES.put("Copper", (str) -> Items.COPPER_INGOT);
    METAL_TYPES.put("Brass", (str) -> AllItems.BRASS_INGOT.get());
    METAL_TYPES.put("Iron", (str) -> Items.IRON_INGOT);
    METAL_TYPES.put("Gold", (str) -> Items.GOLD_INGOT);
    METAL_TYPES.put("Netherite", (str) -> Items.NETHERITE_INGOT);
    //METAL_TYPES.put("Cast Iron", (str) -> CAST_IRON_INGOT.get());

    METAL_TYPES.forEach(ItemRegistry::registerCoins);
  }

  private static void registerCoins (String metal, Function<String, Item> getter) {
    if (metal.equals("Andesite")) return;

    COINS.put(metal, Coins.buildCoinItem(CreateDecoMod.REGISTRATE,
      () -> COINSTACKS.get(metal).get(), metal)
      .register()
    );
    COINSTACKS.put(metal, Coins.buildCoinStackItem(CreateDecoMod.REGISTRATE,
      () -> COINS.get(metal).get(), metal)
      .register()
    );
  }
}
