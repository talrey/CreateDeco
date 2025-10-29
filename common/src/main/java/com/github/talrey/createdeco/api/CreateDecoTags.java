package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Locale;

public class CreateDecoTags {

  private static final HashMap<String, TagKey<Item>> PLATES = new HashMap<>();
  private static final HashMap<String, TagKey<Item>> NUGGETS = new HashMap<>();
  private static final HashMap<String, TagKey<Item>> INGOTS = new HashMap<>();
  private static final HashMap<String, TagKey<Item>> BLOCK_ITEM = new HashMap<>();
  private static final HashMap<String, TagKey<Block>> BLOCKS = new HashMap<>();

  public static final TagKey<Item> PLACARDS = of(CreateDecoMod.MOD_ID, "placards");
  public static final TagKey<Item> GLASS = of(CreateDecoMod.MOD_ID, "colorless_glass");

  public static void init () {
    for (String metal : ItemRegistry.METAL_TYPES.keySet()) {
      String metalID = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
      PLATES.put(metal, common("plates/" + metalID));
      NUGGETS.put(metal, common("nuggets/" + metalID));
      INGOTS.put(metal, common("ingots/" + metalID));
      BLOCK_ITEM.put(metal, common("storage_blocks/" + metalID));
      BLOCKS.put(metal, TagKey.create(Registries.BLOCK,
        ResourceLocation.tryBuild("c", "storage_blocks/" + metalID)
      ));
    }
    PLATES.put("Gold", common("plates/gold"));
    NUGGETS.put("Gold", common("nuggets/gold"));
    INGOTS.put("Gold", common("ingots/gold"));
    BLOCK_ITEM.put("Gold", common("storage_blocks/gold"));
    BLOCKS.put("Gold", TagKey.create(Registries.BLOCK,
      ResourceLocation.tryBuild("c", "storage_blocks/gold")
    ));

    PLATES.put("Netherite", common("plates/netherite"));
    NUGGETS.put("Netherite", common("nuggets/netherite"));
    INGOTS.put("Netherite", common("ingots/netherite"));
    BLOCK_ITEM.put("Netherite", common("storage_blocks/netherite"));
    BLOCKS.put("Netherite", TagKey.create(Registries.BLOCK,
      ResourceLocation.tryBuild("c", "storage_blocks/netherite")
    ));
  }

  public static TagKey<Item> plate (String metal) {
    return PLATES.get(metal);
  }

  public static TagKey<Item> nugget (String metal) {
    return NUGGETS.get(metal);
  }

  public static TagKey<Item> ingot (String metal) {
    return INGOTS.get(metal);
  }

  public static TagKey<Item> blockItem (String metal) {
    return BLOCK_ITEM.get(metal);
  }

  public static TagKey<Block> block (String metal) {
    return BLOCKS.get(metal);
  }

  private static TagKey<Item> of (String namespace, String path) {
    return TagKey.create (Registries.ITEM,
      ResourceLocation.tryBuild(namespace, path)
    );
  }

  private static TagKey<Item> common (String path) {
    return of("c", path);
  }
}
