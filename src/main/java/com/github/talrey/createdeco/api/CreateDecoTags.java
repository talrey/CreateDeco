package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.ItemRegistry;
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

  public static final TagKey<Block> PLACARDS_BLOCK = ofBlock(CreateDecoMod.MOD_ID, "placards");
  public static final TagKey<Item> PLACARDS = of(CreateDecoMod.MOD_ID, "placards");
  public static final TagKey<Block> BARS    = ofBlock("minecraft", "bars");

  public static void init () {
    for (String metal : ItemRegistry.METAL_TYPES.keySet()) {
      String metalID = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
      if (metalID.equals("andesite")) metalID += "_alloy";
      PLATES.put(metal, commonItem("plates/" + metalID));
      NUGGETS.put(metal, commonItem("nuggets/" + metalID));
      INGOTS.put(metal, commonItem("ingots/" + metalID));
      BLOCK_ITEM.put(metal, commonItem("storage_blocks/" + metalID));
      BLOCKS.put(metal, commonBlock("storage_blocks/" + metalID)
      );
    }
    PLATES.put("Gold", commonItem("plates/gold"));
    NUGGETS.put("Gold", commonItem("nuggets/gold"));
    INGOTS.put("Gold", commonItem("ingots/gold"));
    BLOCK_ITEM.put("Gold", commonItem("storage_blocks/gold"));
    BLOCKS.put("Gold", commonBlock("storage_blocks/gold")
    );

    PLATES.put("Netherite", commonItem("plates/netherite"));
    NUGGETS.put("Netherite", commonItem("nuggets/netherite"));
    INGOTS.put("Netherite", commonItem("ingots/netherite"));
    BLOCK_ITEM.put("Netherite", commonItem("storage_blocks/netherite"));
    BLOCKS.put("Netherite", commonBlock("storage_blocks/netherite")
    );
  }

  public static TagKey<Item> plate (String metal) {
    return PLATES.get(metal);
  }

  public static TagKey<Item> plate () { return commonItem("plates"); }

  public static TagKey<Item> nugget (String metal) {
    return NUGGETS.get(metal);
  }

  public static TagKey<Item> nugget () { return commonItem("nuggets"); }

  public static TagKey<Item> ingot (String metal) {
    return INGOTS.get(metal);
  }

  public static TagKey<Item> ingot () { return commonItem("ingots"); }

  public static TagKey<Item> blockItem (String metal) {
    return BLOCK_ITEM.get(metal);
  }

  public static TagKey<Item> blockItem () { return commonItem("storage_blocks"); }

  public static TagKey<Block> block (String metal) {
    return BLOCKS.get(metal);
  }

  private static TagKey<Item> of (String namespace, String path) {
    return ItemTags.create(
      ResourceLocation.fromNamespaceAndPath(namespace, path)
    );
  }

  private static TagKey<Block> ofBlock (String namespace, String path) {
    return BlockTags.create(
      ResourceLocation.fromNamespaceAndPath(namespace, path)
    );
  }

  private static TagKey<Item> commonItem (String path) {
    return of("c", path);
  }

  private static TagKey<Block> commonBlock (String path) { return ofBlock("c", path); }
}
