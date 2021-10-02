package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.github.talrey.createdeco.blocks.DecalBlock;
import com.github.talrey.createdeco.blocks.VerticalSlabBlock;
import com.github.talrey.createdeco.connected.*;
import com.github.talrey.createdeco.items.CatwalkBlockItem;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.Registrate;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.DataIngredient;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class Registration {

  private static HashMap<DyeColor, String> BRICK_COLOR_NAMES          = new HashMap<>();
  private static ArrayList<String> COIN_TYPES                         = new ArrayList<>();
  private static HashMap<String, ItemEntry<Item>> DOOR_TYPES          = new HashMap<>();
  public static HashMap<String, Function<String, Item>> METAL_TYPES   = new HashMap<>();
  private static HashMap<String, Function<String, Item>> METAL_LOOKUP = new HashMap<>();

  public static ItemEntry<Item> WORN_BRICK_ITEM;
  public static HashMap<String,   BlockEntry<Block>> WORN_BRICK_TYPES       = new HashMap<>();
  public static HashMap<String,   BlockEntry<StairsBlock>> WORN_STAIRS      = new HashMap<>();
  public static HashMap<String,   BlockEntry<SlabBlock>> WORN_SLABS         = new HashMap<>();
  public static HashMap<String,   BlockEntry<VerticalSlabBlock>> WORN_VERTS = new HashMap<>();
  public static HashMap<String,   BlockEntry<WallBlock>> WORN_WALLS         = new HashMap<>();

  public static HashMap<DyeColor, BlockEntry<Block>> BRICK_BLOCK         = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> TILE_BRICK_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> LONG_BRICK_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> SHORT_BRICK_BLOCK   = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_BRICK_BLOCK = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_TILE_BLOCK  = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_LONG_BLOCK  = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_SHORT_BLOCK = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_BRICK_BLOCK   = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_TILE_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_LONG_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_SHORT_BLOCK   = new HashMap<>();

  public static HashMap<DyeColor, HashMap<String,BlockEntry<StairsBlock>>> BRICK_STAIRS_BLOCK     = new HashMap<>();
  public static HashMap<DyeColor, HashMap<String,BlockEntry<SlabBlock>>> BRICK_SLAB_BLOCK         = new HashMap<>();
  public static HashMap<DyeColor, HashMap<String,BlockEntry<VerticalSlabBlock>>> BRICK_VERT_BLOCK = new HashMap<>();
  public static HashMap<DyeColor, HashMap<String,BlockEntry<WallBlock>>> BRICK_WALL_BLOCK         = new HashMap<>();

  public static HashMap<String, BlockEntry<CoinStackBlock>> COIN_BLOCKS  = new HashMap<>();
  public static HashMap<String, BlockEntry<DoorBlock>> DOOR_BLOCKS       = new HashMap<>();
  public static HashMap<String, BlockEntry<DoorBlock>> LOCK_DOOR_BLOCKS  = new HashMap<>();
  public static HashMap<String, BlockEntry<PaneBlock>> BAR_BLOCKS        = new HashMap<>();
  public static HashMap<String, BlockEntry<PaneBlock>> BAR_PANEL_BLOCKS  = new HashMap<>();

  public static HashMap<String, BlockEntry<Block>> SHEET_METAL_BLOCKS    = new HashMap<>();
  public static HashMap<String, BlockEntry<StairsBlock>> SHEET_STAIRS    = new HashMap<>();
  public static HashMap<String, BlockEntry<SlabBlock>> SHEET_SLABS       = new HashMap<>();
  public static HashMap<String, BlockEntry<VerticalSlabBlock>> SHEET_VERT_SLABS = new HashMap<>();

  public static HashMap<String, BlockEntry<FenceBlock>> MESH_FENCE_BLOCKS = new HashMap<>();
  public static HashMap<String, BlockEntry<CatwalkBlock>> CATWALK_BLOCKS  = new HashMap<>();

  public static HashMap<DyeColor, BlockEntry<DecalBlock>> DECAL_BLOCKS   = new HashMap<>();

  public static HashMap<DyeColor, ItemEntry<Item>> BRICK_ITEM            = new HashMap<>();
  public static HashMap<String, ItemEntry<Item>> COIN_ITEM               = new HashMap<>();
  public static HashMap<String, ItemEntry<CoinStackItem>> COINSTACK_ITEM = new HashMap<>();

  public static ItemEntry<Item> ZINC_SHEET;
  public static ItemEntry<Item> NETHERITE_SHEET;
  public static ItemEntry<Item> NETHERITE_NUGGET;

  public static class DecoItemGroup extends ItemGroup {
    private final Supplier<ItemStack> sup;
    public DecoItemGroup (final String name, final Supplier<ItemStack> supplier) {
      super(name);
      sup = supplier;
    }
    @Override
    public ItemStack createIcon () {
      return sup.get();
    }
  }
  public static final ItemGroup BRICKS_GROUP = new DecoItemGroup(
    CreateDecoMod.MODID + ".bricks",
    () -> BRICK_BLOCK.get(DyeColor.LIGHT_BLUE).asStack()
  );
  public static final ItemGroup METALS_GROUP = new DecoItemGroup(
    CreateDecoMod.MODID + ".metals",
    () -> BAR_BLOCKS.get("Brass").asStack()
  );
  public static final ItemGroup PROPS_GROUP = new DecoItemGroup(
    CreateDecoMod.MODID + ".props",
    () -> COINSTACK_ITEM.get("Brass").asStack()
  );
  private static final String BRICKS_NAME = "CreateDeco Bricks";
  private static final String METALS_NAME = "CreateDeco Metals";
  private static final String PROPS_NAME  = "CreateDeco Props";

  public Registration () {
    BRICK_COLOR_NAMES.put(DyeColor.BLACK, "Dusk");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_GRAY, "Pearl");
    BRICK_COLOR_NAMES.put(DyeColor.RED, "Scarlet");
    BRICK_COLOR_NAMES.put(DyeColor.YELLOW, "Dean");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_BLUE, "Blue");
    BRICK_COLOR_NAMES.put(null, "Red"); // this is funky but it works, I swear.

    COIN_TYPES.add("Zinc");
    COIN_TYPES.add("Copper");
    COIN_TYPES.add("Brass");
    COIN_TYPES.add("Iron");
    COIN_TYPES.add("Gold");
    COIN_TYPES.add("Netherite");

    DOOR_TYPES.put("Andesite", AllItems.ANDESITE_ALLOY);
    DOOR_TYPES.put("Copper",   AllItems.COPPER_INGOT);
    DOOR_TYPES.put("Zinc",     AllItems.ZINC_INGOT);
    DOOR_TYPES.put("Brass",    AllItems.BRASS_INGOT);

    METAL_TYPES.put("Andesite",  (str) -> AllItems.ANDESITE_ALLOY.get());
    METAL_TYPES.put("Zinc",      (str) -> AllItems.ZINC_INGOT.get());
    METAL_TYPES.put("Copper",    (str) -> AllItems.COPPER_INGOT.get());
    METAL_TYPES.put("Brass",     (str) -> AllItems.BRASS_INGOT.get());
    METAL_TYPES.put("Iron",      (str) -> Items.IRON_INGOT);
    METAL_TYPES.put("Gold",      (str) -> Items.GOLD_INGOT);
    METAL_TYPES.put("Netherite", (str) -> Items.NETHERITE_INGOT);

    METAL_LOOKUP.put("Andesite",  (str) -> str.equals("block") ? AllBlocks.ANDESITE_CASING.get().asItem() : AllItems.ANDESITE_ALLOY.get());
    METAL_LOOKUP.put("Zinc",      (str) -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:zinc_block")));
    METAL_LOOKUP.put("Copper",    (str) -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:copper_block")));
    METAL_LOOKUP.put("Brass",     (str) -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:brass_block")));
    METAL_LOOKUP.put("Iron",      (str) -> Items.IRON_BLOCK);
    METAL_LOOKUP.put("Gold",      (str) -> Items.GOLD_BLOCK);
    METAL_LOOKUP.put("Netherite", (str) -> Items.NETHERITE_BLOCK);
  }

  private static BlockEntry<?> getBrickFromName (String overlay, DyeColor dye, String shape) {
    if (overlay.trim().equals ("Mossy")) {
      switch (shape.trim()) {
        case "Brick Tiles":  return MOSSY_TILE_BLOCK.get(dye);
        case "Long Bricks":  return MOSSY_LONG_BLOCK.get(dye);
        case "Short Bricks": return MOSSY_SHORT_BLOCK.get(dye);
        default:      return MOSSY_BRICK_BLOCK.get(dye);
      }
    }
    else if (overlay.trim().equals ("Cracked")) {
      switch (shape.trim()) {
        case "Brick Tiles":  return CRACKED_TILE_BLOCK.get(dye);
        case "Long Bricks":  return CRACKED_LONG_BLOCK.get(dye);
        case "Short Bricks": return CRACKED_SHORT_BLOCK.get(dye);
        default:      return CRACKED_BRICK_BLOCK.get(dye);
      }
    }
    switch (shape.trim()) {
      case "Brick Tiles":  return TILE_BRICK_BLOCK.get(dye);
      case "Long Bricks":  return LONG_BRICK_BLOCK.get(dye);
      case "Short Bricks": return SHORT_BRICK_BLOCK.get(dye);
      default:      return BRICK_BLOCK.get(dye);
    }
  }

  public static String getBrickColorName (DyeColor color) {
    return BRICK_COLOR_NAMES.getOrDefault(color, "");
  }

  public static Item getBrickItemFromColor        (DyeColor color) { return color != null ? BRICK_ITEM.get(color).get()  : Items.BRICK; }
  public static Block getBrickBlockFromColor      (DyeColor color) { return color != null ? BRICK_BLOCK.get(color).get() : Blocks.BRICKS; }
  public static Block getBrickStairBlockFromColor (DyeColor color, String suf) {
    if (color == null && !(suf.contains("Tiles") || suf.contains("Short") || suf.contains("Long"))) {
      return Blocks.BRICK_STAIRS;
    }
    else return BRICK_STAIRS_BLOCK.get(color).get(suf).get();
  }
  public static Block getBrickSlabBlockFromColor (DyeColor color, String suf) {
    if (color == null && !(suf.contains("Tiles") || suf.contains("Short") || suf.contains("Long"))) {
      return Blocks.BRICK_SLAB;
    }
    else return BRICK_SLAB_BLOCK.get(color).get(suf).get();
  }
  public static Block getBrickWallBlockFromColor (DyeColor color, String suf) {
    if (color == null && !(suf.contains("Tiles") || suf.contains("Short") || suf.contains("Long"))) {
      return Blocks.BRICK_WALL;
    }
    else return BRICK_WALL_BLOCK.get(color).get(suf).get();
  }
  public static Block getBrickVertBlockFromColor (DyeColor color, String suf) {
    return BRICK_VERT_BLOCK.get(color).get(suf).get(); // no special case here
  }

  private static BlockBuilder<Block,?> buildBrick (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase();
    String pre = prefix.replace(' ', '_').toLowerCase() + (prefix.equals("")?"":"_");
    BlockBuilder<Block,?> ret = reg.block(pre + name.toLowerCase() + "_" + suf, Block::new);
    if (dye != null) {
      ret.initialProperties(Material.ROCK, dye);
    } else {
      ret.initialProperties(Material.ROCK);
    }
    return ret.properties(props -> props.hardnessAndResistance(2,6).harvestTool(ToolType.PICKAXE).requiresTool()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.simpleBlock(ctx.get(), prov.models().cubeAll(ctx.getName(),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase() + "/" + pre + name.toLowerCase()+"_" + suf)
      )))
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix)
      .defaultLoot()
      .simpleItem();
  }

  private static BlockBuilder<StairsBlock,?> buildBrickStairs (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase();
    String pre = prefix.replace(' ', '_').toLowerCase() + (prefix.equals("")?"":"_");
    BlockBuilder<StairsBlock,?> ret = reg.block(pre + name.toLowerCase() + "_" + suf + "_stairs",
      (props)->new StairsBlock(Blocks.BRICK_STAIRS::getDefaultState, props));
    if (dye != null) {
      ret.initialProperties(Material.ROCK, dye);
    } else {
      ret.initialProperties(Material.ROCK);
    }
    return ret.properties(props -> props.hardnessAndResistance(2,6).harvestTool(ToolType.PICKAXE).requiresTool()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.stairsBlock(ctx.get(),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase() + "/" + pre + name.toLowerCase()+"_" + suf)
      ))
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Stairs")
      .defaultLoot()
      .simpleItem();
  }

  private static BlockBuilder<SlabBlock,?> buildBrickSlabs (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase();
    String pre = prefix.replace(' ', '_').toLowerCase() + (prefix.equals("")?"":"_");
    BlockBuilder<SlabBlock,?> ret = reg.block(pre + name.toLowerCase() + "_" + suf  + "_slab", SlabBlock::new);
    if (dye != null) {
      ret.initialProperties(Material.ROCK, dye);
    } else {
      ret.initialProperties(Material.ROCK);
    }
    return ret.properties(props -> props.hardnessAndResistance(2,6).harvestTool(ToolType.PICKAXE).requiresTool()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.slabBlock(ctx.get(),
        prov.modLoc("block/" + pre + name.toLowerCase() + (suf.equals("")?"":"_"+suf)),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase() + "/" + pre + name.toLowerCase()+"_" + suf)
      ))
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Slab")
      .defaultLoot()
      .simpleItem();
  }

  private static BlockBuilder<VerticalSlabBlock,?> buildBrickVerts (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase();
    String pre = prefix.replace(' ', '_').toLowerCase() + (prefix.equals("")?"":"_");
    BlockBuilder<VerticalSlabBlock,?> ret = reg.block(pre + name.toLowerCase() + "_" + suf  + "_slab_vert", VerticalSlabBlock::new);
    if (dye != null) {
      ret.initialProperties(Material.ROCK, dye);
    } else {
      ret.initialProperties(Material.ROCK);
    }
    return ret.properties(props -> props.hardnessAndResistance(2,6).harvestTool(ToolType.PICKAXE).requiresTool()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> {
        String texLoc = "block/palettes/bricks/" + name.toLowerCase() + "/" + pre + name.toLowerCase()+"_" + suf;
        ResourceLocation tex = prov.modLoc(texLoc);
        BlockModelBuilder half = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/vertical_slab"))
        .texture("side", tex);
        BlockModelBuilder both = prov.models().cubeAll(ctx.getName()+"_double", tex);

        int y = 0;
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getAllowedValues()) {
          switch (dir) {
            case NORTH: y =   0; break;
            case SOUTH: y = 180; break;
            case WEST:  y = -90; break;
            case EAST:  y =  90; break;
          }
          prov.getMultipartBuilder(ctx.get()).part().modelFile(half).rotationY(y).addModel()
          .condition(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
          .condition(BlockStateProperties.HORIZONTAL_FACING, dir).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(both).rotationY(y).addModel()
          .condition(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).end();
        }
      })
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Vertical Slab")
      .defaultLoot()
      .simpleItem();
  }

  private static BlockBuilder<WallBlock,?> buildBrickWalls (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ','_').toLowerCase();
    String pre = prefix.replace(' ','_').toLowerCase() + (prefix.equals("")?"":"_");
    BlockBuilder<WallBlock,?> ret = reg.block(pre + name.toLowerCase() + "_" + suf + "_wall", WallBlock::new);
    if (dye != null) {
      ret.initialProperties(Material.ROCK, dye);
    } else {
      ret.initialProperties(Material.ROCK);
    }
    return ret.properties(props-> props.hardnessAndResistance(2,6).harvestTool(ToolType.PICKAXE).requiresTool()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.wallBlock(ctx.get(),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase() + "/" + pre + name.toLowerCase()+"_" + suf)
      ))
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Wall")
      .defaultLoot()
      .tag(BlockTags.WALLS)
      .item()
        .model((ctx,prov)-> prov.wallInventory("item/" + pre + name.toLowerCase() + "_" + suf + "_wall",
          prov.modLoc("block/palettes/bricks/"
            + name.toLowerCase() + "/" + pre + name.toLowerCase()+"_" + suf
        )))
        .build();
  }

  private static BlockBuilder<PaneBlock,?> buildBars (Registrate reg, String metal, Function<String,Item> getter, String suffix) {
    String base = metal.replace(' ', '_').toLowerCase() + "_bars";
    String suf = suffix.equals("") ? "" : "_" + suffix.replace(' ', '_').toLowerCase();
    String post = "block/palettes/metal_bars/" + base + (metal == "Brass" || metal == "Netherite" ? "_post" : "");

    ResourceLocation barTexture, postTexture;
    final ResourceLocation bartex, postex;
    try {
      barTexture = new ResourceLocation(CreateDecoMod.MODID, "block/palettes/metal_bars/" + base);
      File touch = new File("../src/main/resources/assets/createdeco/textures/" + barTexture.getPath() + ".png"); // fuck it.
      if (!touch.exists()) throw new FileNotFoundException(base + " was not found!");
    } catch (FileNotFoundException fnfe) {
      barTexture = new ResourceLocation("block/" + base);
    }
    try {
      postTexture = new ResourceLocation(CreateDecoMod.MODID, post);
      File touch = new File("../src/main/resources/assets/createdeco/textures/" + postTexture.getPath() + ".png");
      if (!touch.exists()) throw new FileNotFoundException(base + " was not found!");
    } catch (FileNotFoundException fnfe) {
      postTexture = barTexture;
    }
    // for lambda stuff, must be final
    bartex = barTexture;
    postex = postTexture;

    return reg.block(base + suf, PaneBlock::new)
      .properties(props -> props.nonOpaque().hardnessAndResistance(5, 6).sound(SoundType.NETHERITE))
      .blockstate((ctx, prov) -> {
        MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());
        BlockModelBuilder sideModel = prov.models().withExistingParent(
          base + "_side", prov.mcLoc("block/iron_bars_side"))
          .texture("bars", bartex)
          .texture("edge", postex)
          .texture("particle", postex);
        BlockModelBuilder sideAltModel = prov.models().withExistingParent(
          base + "_side_alt", prov.mcLoc("block/iron_bars_side_alt"))
          .texture("bars", bartex)
          .texture("edge", postex)
          .texture("particle", postex);

        builder.part().modelFile(prov.models().withExistingParent(base + "_post", prov.mcLoc("block/iron_bars_post"))
          .texture("bars", postex).texture("particle", postex)
        ).addModel()
          .condition(BlockStateProperties.NORTH, false)
          .condition(BlockStateProperties.SOUTH, false)
          .condition(BlockStateProperties.EAST, false)
          .condition(BlockStateProperties.WEST, false)
          .end();
        builder.part().modelFile(
          prov.models().withExistingParent(base + "_post_ends", prov.mcLoc("block/iron_bars_post_ends"))
            .texture("edge", postex).texture("particle", postex)
        ).addModel().end();
          builder.part().modelFile(sideModel).addModel().condition(BlockStateProperties.NORTH, true).end();
          builder.part().modelFile(sideModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
          builder.part().modelFile(sideAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
          builder.part().modelFile(sideAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();

          if (!suf.equals("")) {
            BlockModelBuilder sideOverlayModel = prov.models().withExistingParent(
              base + suf, prov.mcLoc("block/iron_bars_side"))
              .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base + suf))
              .texture("edge", postex)
              .texture("particle", postex);
            BlockModelBuilder sideOverlayAltModel = prov.models().withExistingParent(
              base + suf + "_alt", prov.mcLoc("block/iron_bars_side_alt"))
              .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base + suf))
              .texture("edge", postex)
              .texture("particle", postex);

            builder.part().modelFile(sideOverlayModel).addModel().condition(BlockStateProperties.NORTH, true).end();
            builder.part().modelFile(sideOverlayModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
            builder.part().modelFile(sideOverlayAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
            builder.part().modelFile(sideOverlayAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();
          }
      })
      .tag(BlockTags.WALLS)
      .item()
        .model((ctx, prov) -> {
          if (suf.equals("")) {
            prov.singleTexture(base, prov.mcLoc("item/generated"),"layer0", bartex);
          }
          else {
            prov.withExistingParent(base + suf, prov.mcLoc("item/generated"))
              .texture("layer0", bartex)
              .texture("layer1", prov.modLoc("block/palettes/metal_bars/" + base + suf));
          }
        })
        .properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p)
        .build();
  }

  public static void registerBlocks (Registrate reg) {
    reg.itemGroup(()->BRICKS_GROUP);

    BlockBuilder<Block, ?> wornBrick = buildBrick(reg, null, "", "Worn", "Bricks")
      .recipe((ctx,prov)-> ShapedRecipeBuilder.shapedRecipe(ctx.get())
        .patternLine("bb")
        .patternLine("bb")
        .key('b', WORN_BRICK_ITEM.get())
        .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(WORN_BRICK_ITEM.get()))
        .build(prov)
      );
    WORN_BRICK_TYPES.put("Worn Bricks", wornBrick.register());
    String[] prefs = { "", "Cracked", "Mossy" };
    String[] sufs  = { "Bricks", "Brick Tiles", "Long Bricks", "Short Bricks"};
    for (String pre : prefs) {
      for (String suf : sufs) {
        String full = (pre.equals("") ? "" : pre + " ") + "Worn" + " " + suf;
        if (! (pre.equals("") && suf.equals("Bricks"))) {
          WORN_BRICK_TYPES.put(full, buildBrick(reg, null, pre, "Worn", suf)
            .recipe((ctx, prov) -> {
              prov.stonecutting(DataIngredient.items(WORN_BRICK_ITEM.get()), ctx);
              if (pre.equals("Cracked")) prov.blasting(DataIngredient.items(WORN_BRICK_ITEM.get()), ctx, 0.5f);
            })
            .register()
          );
        }
        WORN_STAIRS.put(full, buildBrickStairs(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            prov.stonecutting(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx);
            prov.stairs(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, null, false);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_STAIRS.get(full.substring(8))), ctx, 0.5f);
            }
          })
          .register()
        );
        WORN_SLABS.put(full, buildBrickSlabs(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            prov.stonecutting(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, 2);
            prov.slab(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, null, false);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_SLABS.get(full.substring(8))), ctx, 0.5f);
            }
          })
          .register()
        );
        WORN_VERTS.put(full, buildBrickVerts(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            prov.stonecutting(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, 2);
            ShapedRecipeBuilder.shapedRecipe(ctx.get())
              .patternLine("s")
              .patternLine("s")
              .patternLine("s")
              .key('s', WORN_SLABS.get(full).get())
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(WORN_SLABS.get(full).get()))
              .build(prov);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_VERTS.get(full.substring(8))), ctx, 0.5f);
            }
          }).register()
        );
        WORN_WALLS.put(full, buildBrickWalls(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            prov.wall(DataIngredient.items(WORN_BRICK_TYPES.get(full).get()), ctx);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_WALLS.get(full.substring(8))), ctx, 0.5f);
            }
          }).register()
        );
      }
    }

    BRICK_COLOR_NAMES.forEach((dye, name)-> {
      if (dye != null) BRICK_BLOCK.put(dye,         buildBrick(reg, dye, "", name, "Bricks")
        .recipe((ctx,prov)-> ShapedRecipeBuilder.shapedRecipe(ctx.get())
          .patternLine("bb")
          .patternLine("bb")
          .key('b', getBrickItemFromColor(dye))
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(getBrickItemFromColor(dye)))
          .build(prov)
        ).register());
      TILE_BRICK_BLOCK.put(dye,    buildBrick(reg, dye, "", name, "Brick Tiles")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      LONG_BRICK_BLOCK.put(dye,    buildBrick(reg, dye, "", name, "Long Bricks")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      SHORT_BRICK_BLOCK.put(dye,   buildBrick(reg, dye, "", name, "Short Bricks")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      CRACKED_BRICK_BLOCK.put(dye, buildBrick(reg, dye, "Cracked", name, "Bricks")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      CRACKED_TILE_BLOCK.put(dye,  buildBrick(reg, dye, "Cracked", name, "Brick Tiles")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(TILE_BRICK_BLOCK.get(dye).get()), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      CRACKED_LONG_BLOCK.put(dye,  buildBrick(reg, dye, "Cracked", name, "Long Bricks")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(LONG_BRICK_BLOCK.get(dye).get()), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      CRACKED_SHORT_BLOCK.put(dye, buildBrick(reg, dye, "Cracked", name, "Short Bricks")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(SHORT_BRICK_BLOCK.get(dye).get()), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      MOSSY_BRICK_BLOCK.put(dye,   buildBrick(reg, dye, "Mossy", name, "Bricks")
        // washing handled in wrapper class
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      MOSSY_TILE_BLOCK.put(dye,    buildBrick(reg, dye, "Mossy", name, "Brick Tiles")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      MOSSY_LONG_BLOCK.put(dye,    buildBrick(reg, dye, "Mossy", name, "Long Bricks")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      MOSSY_SHORT_BLOCK.put(dye,   buildBrick(reg, dye, "Mossy", name, "Short Bricks")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());

      HashMap<String, BlockEntry<StairsBlock>>       stair = new HashMap<>();
      HashMap<String, BlockEntry<SlabBlock>>         slab  = new HashMap<>();
      HashMap<String, BlockEntry<VerticalSlabBlock>> vert  = new HashMap<>();
      HashMap<String, BlockEntry<WallBlock>>         wall  = new HashMap<>();
      for (String pre : prefs) {
        for (String suf : sufs) {
          //if (pre.equals("") && suf.equals("Bricks")) continue;
          String full = (pre.equals("")?"":pre + " ") + name + " " + suf;
          if (! ((dye == null) && pre.equals("") && suf.equals("Bricks")) ) { // vanilla block, stairs, slab, and wall exist already
            stair.put(full, buildBrickStairs(reg, dye, pre, name, suf)
              .recipe((ctx, prov) -> {
                prov.stonecutting(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx);
                prov.stairs(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx, null, false);
                if (pre.equals("Cracked")) {
                  prov.blasting(DataIngredient.items(getBrickStairBlockFromColor(dye, full.substring(8))), ctx, 0.5f);
                }
                // handle washing in the wrapper
            }).register());

            slab.put(full, buildBrickSlabs(reg, dye, pre, name, suf)
              .recipe((ctx, prov) -> {
                prov.stonecutting(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx, 2);
                prov.slab(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx, null, false);
                if (pre.equals("Cracked")) {
                  prov.blasting(DataIngredient.items(getBrickSlabBlockFromColor(dye, full.substring(8))), ctx, 0.5f);
                }
                // handle washing in the wrapper
            }).register());

            wall.put(full, buildBrickWalls(reg, dye, pre, name, suf)
              .recipe((ctx, prov) -> {
                prov.wall(DataIngredient.items(getBrickFromName(pre, dye, suf).get()), ctx);
                if (pre.equals("Cracked")) {
                  prov.blasting(DataIngredient.items(getBrickWallBlockFromColor(dye, full.substring(8))), ctx, 0.5f);
                }
                // handle washing in the wrapper
            }).register());
          }

          vert.put(full, buildBrickVerts( reg, dye, pre, name, suf)
            .recipe((ctx, prov)-> {
              prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx, 2);
              ShapedRecipeBuilder.shapedRecipe(ctx.get())
                .patternLine("s")
                .patternLine("s")
                .patternLine("s")
                .key('s', getBrickSlabBlockFromColor(dye, full))
                .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(getBrickSlabBlockFromColor(dye, full)))
              .build(prov);
              if (pre.equals("Cracked")) {
                prov.blasting(DataIngredient.items(getBrickSlabBlockFromColor(dye, full.substring(8))), ctx, 0.5f);
              }
              // handle washing in the wrapper
          }).register());
        }
      }
      BRICK_STAIRS_BLOCK.put(dye, stair);
      BRICK_SLAB_BLOCK.put(dye, slab);
      BRICK_VERT_BLOCK.put(dye, vert);
      BRICK_WALL_BLOCK.put(dye, wall);
    });

    reg.itemGroup(()->PROPS_GROUP);
    COIN_TYPES.forEach(metal ->
      COIN_BLOCKS.put(metal.toLowerCase(), reg.block(metal.toLowerCase()+"_coinstack_block", CoinStackBlock::new)
        .properties(props -> props.nonOpaque().hardnessAndResistance(0.5f).sound(SoundType.CHAIN))
        .blockstate((ctx,prov)-> prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
          int layer = state.get(BlockStateProperties.LAYERS_1_8);
          return ConfiguredModel.builder().modelFile(prov.models().withExistingParent(
            ctx.getName() + "_" + layer, prov.modLoc("block/layers_bottom_top_" + layer)
          )
          .texture("side",   prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_side"))
          .texture("bottom", prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_bottom"))
          .texture("top",    prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_top"))
        ).build(); }))
        .lang(metal + " Stack Block")
        .loot((table, block) -> {
          LootTable.Builder builder      = LootTable.builder();
          LootPool.Builder pool          = LootPool.builder().rolls(ConstantRange.of(1));
          for (int layer = 1; layer <= 8; layer++) {
            ItemLootEntry.Builder<?> entry = ItemLootEntry.builder(COINSTACK_ITEM.get(metal).get());
            entry.acceptCondition(BlockStateProperty.builder(block).properties(StatePropertiesPredicate.Builder.create()
              .exactMatch(BlockStateProperties.LAYERS_1_8, layer)
            )).acceptFunction(SetCount.builder(ConstantRange.of(layer)));
            pool.addEntry(entry);
          }
          table.registerLootTable(block, builder.addLootPool(pool));
        })
        .register())
    );

    for (DyeColor color : DyeColor.values()) {
      DECAL_BLOCKS.put(color, reg.block(color.name().toLowerCase() + "_decal", DecalBlock::new)
        .initialProperties(Material.IRON)
        .properties(props-> props.nonOpaque().hardnessAndResistance(0.5f).sound(SoundType.LANTERN))
        .blockstate((ctx,prov)-> prov.getVariantBuilder(ctx.get()).forAllStates(state-> {
          int y = 0;
          switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH: y =   0; break;
            case SOUTH: y = 180; break;
            case WEST:  y = -90; break;
            case EAST:  y =  90; break;
          }
          return ConfiguredModel.builder().modelFile(prov.models()
            .withExistingParent(ctx.getName(), prov.modLoc("block/decal"))
            .texture("face", prov.modLoc("block/palettes/decal/" + ctx.getName()))
            .texture("particle", prov.modLoc("block/palettes/decal/" + ctx.getName()))
          ).rotationY(y).build(); }))
        .lang(color.name() + " Decal")
        .item()
          .model((ctx,prov)-> prov.singleTexture(ctx.getName(),
            prov.mcLoc("item/generated"),
            "layer0", prov.modLoc("block/palettes/decal/" + ctx.getName())
          ))
          .build()
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapelessRecipe(ctx.get())
          .addIngredient(AllItems.IRON_SHEET.get(), 1)
          .addIngredient(DyeItem.getItem(color), 1)
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(AllItems.IRON_SHEET.get()))
          .addCriterion("has_dye",  InventoryChangeTrigger.Instance.forItems(DyeItem.getItem(color)))
          .build(prov)
        )
        .register());
    }

    reg.itemGroup(()->METALS_GROUP);
    DOOR_TYPES.forEach((metal,ingot) ->
      DOOR_BLOCKS.put(metal.toLowerCase(), reg.block(metal.toLowerCase() + "_door", DoorBlock::new)
        .initialProperties(Material.NETHER_WOOD) // setting it to IRON locks it, and normal wood would burn probably
        .properties(props -> props.nonOpaque().hardnessAndResistance(5, 5).harvestTool(ToolType.PICKAXE).requiresTool()
          .sound(SoundType.NETHERITE)
        )
        .blockstate((ctx, prov) -> prov.doorBlock(ctx.get(),
          prov.modLoc("block/" + metal.toLowerCase() + "_door_bottom"),
          prov.modLoc("block/" + metal.toLowerCase() + "_door_top"))
        )
        .lang(metal + " Door")
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get())
          .patternLine("mm")
          .patternLine("mm")
          .patternLine("mm")
          .key('m', ingot.get())
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ingot.get()))
          .build(prov)
        )
        .loot((table, block)-> {
          LootTable.Builder builder = LootTable.builder();
          LootPool.Builder pool     = LootPool.builder();
          pool.rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block))
            .acceptCondition(BlockStateProperty.builder(block).properties(StatePropertiesPredicate.Builder.create()
              .exactMatch(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            ));
          table.registerLootTable(block, builder.addLootPool(pool));
          })
        .item()
          .model((ctx,prov) ->
            prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
              "layer0", prov.modLoc("item/" + ctx.getName())
          ))
          .build()
        .register()));

    DOOR_TYPES.forEach((metal, ingot)->
      LOCK_DOOR_BLOCKS.put(metal.toLowerCase(), reg.block("locked_" + metal.toLowerCase() + "_door", DoorBlock::new)
        .initialProperties(Material.IRON)
        .properties(props -> props.nonOpaque().hardnessAndResistance(5, 5).harvestTool(ToolType.PICKAXE).requiresTool()
          .sound(SoundType.NETHERITE)
        )
        .blockstate((ctx, prov)-> prov.doorBlock(ctx.get(),
          prov.modLoc("block/locked_" + metal.toLowerCase() + "_door_bottom"),
          prov.modLoc("block/locked_" + metal.toLowerCase() + "_door_top"))
        )
        .loot((table, block)-> {
          LootTable.Builder builder = LootTable.builder();
          LootPool.Builder pool     = LootPool.builder();
          pool.rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block))
            .acceptCondition(BlockStateProperty.builder(block).properties(StatePropertiesPredicate.Builder.create()
              .exactMatch(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            ));
          table.registerLootTable(block, builder.addLootPool(pool));
        })
        .lang("Locked " + metal + " Door")
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapelessRecipe(ctx.get())
          .addIngredient(Items.REDSTONE_TORCH, 1)
          .addIngredient(DOOR_BLOCKS.get(metal.toLowerCase()).get(), 1)
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(
            DOOR_BLOCKS.get(metal.toLowerCase()).asStack().getItem())
          )
          .build(prov)
        )
        .item()
          .model((ctx, prov)-> prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
            "layer0", prov.modLoc("item/" + ctx.getName())
          ))
          .build()
        .register()));

    METAL_TYPES.forEach((metal, getter) -> {
      BAR_BLOCKS.put(metal, buildBars(reg, (metal.equals("Iron")?"Polished Iron":metal), getter, "")
        .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
        .recipe((ctx, prov) -> {
          if (!metal.equals("Iron")) { // Iron will be handled as a polishing recipe
            ShapedRecipeBuilder.shapedRecipe(ctx.get(), 16)
              .patternLine("mmm")
              .patternLine("mmm")
              .key('m', getter.apply(metal))
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(getter.apply(metal)))
              .build(prov);
          }
          ShapelessRecipeBuilder.shapelessRecipe(ctx.get())
            .addIngredient(BAR_PANEL_BLOCKS.get(metal).get())
            .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(BAR_PANEL_BLOCKS.get(metal).get()))
            .build(prov, new ResourceLocation(CreateDecoMod.MODID, metal.toLowerCase() + "_bars_from_panel"));
        })
        .register());
      BAR_PANEL_BLOCKS.put(metal, buildBars(reg, (metal.equals("Iron")?"Polished Iron":metal), getter, "overlay")
        .lang((metal.equals("Iron")?"Polished Iron":metal) + " Panel Bars ")
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapelessRecipe(ctx.get())
          .addIngredient(BAR_BLOCKS.get(metal).get())
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(BAR_BLOCKS.get(metal).get()))
          .build(prov)
        )
        .register());
      if (metal.equals("Iron")) { // add a panel version of the vanilla iron too
        BAR_PANEL_BLOCKS.put("Vanilla Iron", buildBars(reg, metal, getter, "overlay")
          .lang(metal + " Panel Bars")
          .recipe((ctx, prov)-> {
            ShapelessRecipeBuilder.shapelessRecipe(ctx.get())
              .addIngredient(Items.IRON_BARS)
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(Items.IRON_BARS))
              .build(prov);
            ShapelessRecipeBuilder.shapelessRecipe(Items.IRON_BARS)
              .addIngredient(ctx.get())
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ctx.get()))
              .build(prov, new ResourceLocation(CreateDecoMod.MODID, "vanilla_iron_bars_from_panel"));
          })
          .register());
      }

      SHEET_METAL_BLOCKS.put(metal, reg.block(metal.toLowerCase() + "_sheet_metal", Block::new)
        .initialProperties(Material.IRON)
        .properties(props-> props.hardnessAndResistance(5, 3).harvestTool(ToolType.PICKAXE).requiresTool()
          .sound(SoundType.NETHERITE)
        )
        .blockstate((ctx,prov)-> prov.simpleBlock(ctx.get(), prov.models().cubeAll(ctx.getName(),
          prov.modLoc("block/palettes/sheet_metal/" + ctx.getName())
        )))
        .lang(metal + " Sheet Metal")
        .defaultLoot()
        .item().properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p).build()
        .recipe((ctx, prov)-> {
          prov.stonecutting(DataIngredient.items(METAL_LOOKUP.get(metal).apply("block")), ctx, 4);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal))
        ))
        .register());

      SHEET_STAIRS.put(metal, reg.block(metal.toLowerCase() + "_sheet_stairs",
        (props)->new StairsBlock(Blocks.BRICK_STAIRS::getDefaultState, props))
        .initialProperties(Material.IRON)
        .properties(props-> props.hardnessAndResistance(5, 3).harvestTool(ToolType.PICKAXE).requiresTool()
          .sound(SoundType.NETHERITE)
        )
        .item().properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p).build()
        .tag(BlockTags.STAIRS)
        .blockstate((ctx,prov)-> prov.stairsBlock(ctx.get(),
          prov.modLoc("block/palettes/sheet_metal/" + metal.toLowerCase() + "_sheet_metal"))
        )
        .lang(metal + " Sheet Stairs")
        .recipe((ctx, prov)-> {
          prov.stonecutting(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal)), ctx);
          prov.stairs(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal).get()), ctx, null, false);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal))
        ))
        .register());

      SHEET_SLABS.put(metal, reg.block(metal.toLowerCase() + "_sheet_slab", SlabBlock::new)
        .initialProperties(Material.IRON)
        .properties(props-> props.hardnessAndResistance(5, 3).harvestTool(ToolType.PICKAXE).requiresTool()
          .sound(SoundType.NETHERITE)
        )
        .item().properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p).build()
        .tag(BlockTags.SLABS)
        .blockstate((ctx,prov)-> prov.slabBlock(ctx.get(),
          prov.modLoc("block/" + metal.toLowerCase() + "_sheet_metal"),
          prov.modLoc("block/palettes/sheet_metal/" + metal.toLowerCase() + "_sheet_metal"))
        )
        .lang(metal + " Sheet Slab")
        .recipe((ctx, prov)-> {
          prov.stonecutting(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal)), ctx, 2);
          prov.slab(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal).get()), ctx, null, false);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalSlabCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal))
        ))
        .register());

      SHEET_VERT_SLABS.put(metal, reg.block(metal.toLowerCase() + "_sheet_slab_vert", VerticalSlabBlock::new)
        .initialProperties(Material.IRON)
        .properties(props-> props.hardnessAndResistance(5, 3).harvestTool(ToolType.PICKAXE).requiresTool()
          .sound(SoundType.NETHERITE)
        )
        .item().properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p).build()
        .blockstate(((ctx,prov)-> {
          String texLoc = "block/palettes/sheet_metal/" + metal.toLowerCase() + "_sheet_metal";
          ResourceLocation tex = prov.modLoc(texLoc);
          BlockModelBuilder half = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/vertical_slab"))
            .texture("side", tex);
          BlockModelBuilder both = prov.models().cubeAll(ctx.getName()+"_double", tex);

          int y = 0;
          for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getAllowedValues()) {
            switch (dir) {
              case NORTH: y =   0; break;
              case SOUTH: y = 180; break;
              case WEST:  y = -90; break;
              case EAST:  y =  90; break;
            }
            prov.getMultipartBuilder(ctx.get()).part().modelFile(half).rotationY(y).addModel()
              .condition(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
              .condition(BlockStateProperties.HORIZONTAL_FACING, dir).end();
            prov.getMultipartBuilder(ctx.get()).part().modelFile(both).rotationY(y).addModel()
              .condition(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).end();
          }
        }))
        .loot((table, block) -> {
          LootTable.Builder builder = LootTable.builder();
          LootPool.Builder pool     = LootPool.builder();
          pool.rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block)  // extra slab drop if it's a double
            .acceptFunction(SetCount.builder(ConstantRange.of(2)).acceptCondition(
              BlockStateProperty.builder(block).properties(StatePropertiesPredicate.Builder.create()
                .exactMatch(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
              )
          )));
          pool.rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block)
            .acceptFunction(SetCount.builder(ConstantRange.of(1))
          ));
          table.registerLootTable(block, builder.addLootPool(pool));
        })
        .recipe((ctx, prov)-> {
          ShapedRecipeBuilder.shapedRecipe(ctx.get(), 3)
            .patternLine("s")
            .patternLine("s")
            .patternLine("s")
            .key('s', SHEET_SLABS.get(metal).get())
            .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(SHEET_METAL_BLOCKS.get(metal).asStack().getItem()))
            .build(prov);
          prov.stonecutting(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal)), ctx, 2);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalVertCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal))
        ))
        .register());

      MESH_FENCE_BLOCKS.put(metal, reg.block(metal.toLowerCase() + "_mesh_fence", FenceBlock::new)
        .initialProperties(Material.IRON)
        .properties(props-> props.hardnessAndResistance(5, 3).harvestTool(ToolType.PICKAXE).requiresTool()
          .sound(SoundType.CHAIN)
        )
        .tag(BlockTags.FENCES)
        .item()
          .properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p)
          .model((ctx,prov)-> prov.singleTexture(
            ctx.getName(), prov.mcLoc("item/generated"),
            "layer0", prov.modLoc("block/palettes/chain_link_fence/" + metal.toLowerCase() + "_chain_link")))
          .build()
        .recipe((ctx,prov)-> {
          if (metal.equals("Andesite")) {
            ShapedRecipeBuilder.shapedRecipe(ctx.get(), 3)
              .patternLine("psp")
              .patternLine("psp")
              .key('p', AllItems.ANDESITE_ALLOY.get())
              .key('s', Items.STRING)
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(AllItems.ANDESITE_ALLOY.get()))
              .build(prov);
          }
          else {
            ITag<Item> sheet = ItemTags.makeWrapperTag("forge:plates/" + metal.toLowerCase());
            ShapedRecipeBuilder.shapedRecipe(ctx.get(), 3)
              .patternLine("psp")
              .patternLine("psp")
              .key('p', sheet)
              .key('s', Items.STRING)
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(sheet).build()))
              .build(prov);
          }
        })
        .blockstate((ctx,prov)-> {
          prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
            String dir = "chainlink_fence";
            boolean north,south,east,west;
            north = state.get(BlockStateProperties.NORTH);
            south = state.get(BlockStateProperties.SOUTH);
            east  = state.get(BlockStateProperties.EAST);
            west  = state.get(BlockStateProperties.WEST);
            int sides = (north?1:0) + (south?1:0) + (east?1:0) + (west?1:0);
            ResourceLocation mesh = prov.modLoc("block/palettes/chain_link_fence/" + metal.toLowerCase() + "_chain_link");
            ResourceLocation wall = prov.modLoc("block/palettes/sheet_metal/"      + metal.toLowerCase() + "_sheet_metal");
            switch (sides) {
              case 4: return ConfiguredModel.builder().modelFile(
                  prov.models().withExistingParent(ctx.getName() + "_four_way", prov.modLoc(dir + "_four_way"))
                  .texture("0", mesh).texture("1", wall).texture("particle", wall)
                ).build();
              case 3: return ConfiguredModel.builder().modelFile(
                  prov.models().withExistingParent(ctx.getName() + "_four_way", prov.modLoc(dir + "_tri_way"))
                  .texture("0", mesh).texture("1", wall).texture("particle", wall)
                ).rotationY(
                  (north? (south? (east? 90: -90): 0): 180)
                ).build();
              case 2:
                if ((north && south) || (east && west)) {
                  return ConfiguredModel.builder().modelFile(
                    prov.models().withExistingParent(ctx.getName() + "_straight", prov.modLoc(dir + "_straight"))
                    .texture("0", mesh).texture("1", wall).texture("particle", wall)
                  ).rotationY(east?0:90).build();
                } else {
                  return ConfiguredModel.builder().modelFile(
                    prov.models().withExistingParent(ctx.getName() + "_corner", prov.modLoc(dir + "_corner"))
                    .texture("0", mesh).texture("1", wall).texture("particle", wall)
                  ).rotationY( (north? (east? 0: -90): (east? 90: 180))).build();
                }
              case 1: return ConfiguredModel.builder().modelFile(
                  prov.models().withExistingParent(ctx.getName() + "_end", prov.modLoc(dir + "_end"))
                  .texture("0", mesh).texture("1", wall).texture("particle", wall)
                ).rotationY( (north? -90: south? 90: east? 0: 180) ).build();
              case 0: // fall through
              default: return ConfiguredModel.builder().modelFile(
                prov.models().withExistingParent(ctx.getName() + "_post", prov.modLoc(dir + "_post"))
                .texture("0", mesh).texture("1", wall).texture("particle", wall)
              ).build();
            }
          });
        })
        .register());

      CATWALK_BLOCKS.put(metal, reg.block(metal.toLowerCase() + "_catwalk", CatwalkBlock::new)
        .initialProperties(Material.IRON)
        .properties(props->
          props.hardnessAndResistance(5, 3).harvestTool(ToolType.PICKAXE).requiresTool().nonOpaque()
          .sound(SoundType.NETHERITE)
        )

        .item(CatwalkBlockItem::new)
          .properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p)
          .model((ctx,prov)-> prov.withExistingParent(ctx.getName(), prov.mcLoc("block/template_trapdoor_bottom"))
            .texture("texture", prov.modLoc("block/palettes/catwalks/" + metal.toLowerCase() + "_catwalk"))
          )
          .build()
        .recipe((ctx,prov)-> {
          if (metal.equals("Andesite")) {
            ShapedRecipeBuilder.shapedRecipe(ctx.get(), 3)
              .patternLine(" p ")
              .patternLine("pBp")
              .patternLine(" p ")
              .key('p', AllItems.ANDESITE_ALLOY.get())
              .key('B', BAR_BLOCKS.get(metal).get())
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(AllItems.ANDESITE_ALLOY.get()))
              .build(prov);
          }
          else {
            ITag<Item> sheet = ItemTags.makeWrapperTag("forge:plates/" + metal.toLowerCase());
            ShapedRecipeBuilder.shapedRecipe(ctx.get(), 3)
              .patternLine(" p ")
              .patternLine("pBp")
              .patternLine(" p ")
              .key('p', sheet)
              .key('B', BAR_BLOCKS.get(metal).get())
              .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ItemPredicate.Builder.create().tag(sheet).build()))
              .build(prov);
          }
        })
        .blockstate((ctx,prov)-> {
          String texture = "createdeco:block/palettes/catwalks/" + metal.toLowerCase() + "_catwalk";

          BlockModelBuilder lower = prov.models().withExistingParent(ctx.getName()+"_bottom", prov.modLoc("block/catwalk_bottom"))
            .texture("2", texture)
            .texture("particle", texture);
          BlockModelBuilder upper = prov.models().withExistingParent(ctx.getName()+"_top", prov.modLoc("block/catwalk_top"))
            .texture("2", texture)
            .texture("particle", texture);
          BlockModelBuilder rail_upper = prov.models().withExistingParent(ctx.getName()+"_rail_upper",
          prov.modLoc("block/catwalk_rail_upper"))
            .texture("3", texture + "_rail")
            .texture("particle", texture + "_rail");
          BlockModelBuilder rail_lower = prov.models().withExistingParent(ctx.getName()+"_rail_lower",
          prov.modLoc("block/catwalk_rail_lower"))
            .texture("3", texture + "_rail")
            .texture("particle", texture + "_rail");

          prov.getMultipartBuilder(ctx.get()).part().modelFile(lower).addModel().condition(BlockStateProperties.BOTTOM, true).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(upper).addModel().condition(BlockStateProperties.BOTTOM, false).end();

          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY( 90).addModel()
            .condition(BlockStateProperties.BOTTOM, true)
            .condition(BlockStateProperties.NORTH, true).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(-90).addModel()
            .condition(BlockStateProperties.BOTTOM, true)
            .condition(BlockStateProperties.SOUTH, true).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(180).addModel()
            .condition(BlockStateProperties.BOTTOM, true)
            .condition(BlockStateProperties.EAST,  true).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(  0).addModel()
            .condition(BlockStateProperties.BOTTOM, true)
            .condition(BlockStateProperties.WEST,  true).end();

          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY( 90).addModel()
            .condition(BlockStateProperties.BOTTOM, false)
            .condition(BlockStateProperties.NORTH, true).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY(-90).addModel()
            .condition(BlockStateProperties.BOTTOM, false)
            .condition(BlockStateProperties.SOUTH, true).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY(180).addModel()
            .condition(BlockStateProperties.BOTTOM, false)
            .condition(BlockStateProperties.EAST,  true).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY(  0).addModel()
            .condition(BlockStateProperties.BOTTOM, false)
            .condition(BlockStateProperties.WEST,  true).end();
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new CatwalkCTBehaviour(SpriteShifts.CATWALK_TOPS.get(metal))
        ))
        .register());
    });
  }

  public static void registerItems (Registrate reg) {
    reg.itemGroup(()->BRICKS_GROUP, BRICKS_NAME);
    BRICK_COLOR_NAMES.forEach((dye, name)-> {
      if (dye == null) {
        WORN_BRICK_ITEM = reg.item("worn_brick", Item::new)
          .recipe((ctx,prov)-> prov.blasting(DataIngredient.items(Items.BRICK), ctx, 0.3f))
          .register();
      } else {
        BRICK_ITEM.put(dye, reg.item(name.toLowerCase() + "_brick", Item::new)
          .lang(name + " Brick")
          .recipe((ctx, prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get(), 8)
            .patternLine("bbb")
            .patternLine("bCb")
            .patternLine("bbb")
            .key('b', Items.BRICK)
            .key('C', DyeItem.getItem(dye))
            .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(DyeItem.getItem(dye)))
            .build(prov)
          )
          .register());
      }
    });

    reg.itemGroup(()->METALS_GROUP, METALS_NAME);
    ZINC_SHEET = reg.item("zinc_sheet", Item::new)
      .tag(ItemTags.makeWrapperTag("forge:plates/zinc"))
      .lang("Zinc Sheet")
      .register();

    NETHERITE_SHEET = reg.item("netherite_sheet", Item::new)
      .properties(p -> p.fireproof())
      .tag(ItemTags.makeWrapperTag("forge:plates/netherite"))
      .lang("Netherite Sheet")
      .register();

    NETHERITE_NUGGET = reg.item("netherite_nugget", Item::new)
      .properties(p -> p.fireproof())
      .tag(ItemTags.makeWrapperTag("forge:nuggets/netherite"))
      .lang("Netherite Nugget")
      .recipe((ctx, prov)-> {
        prov.storage(ctx, ()->Items.NETHERITE_INGOT);
      })
      .register();

    reg.itemGroup(()->PROPS_GROUP, PROPS_NAME);
    for (String metal : COIN_TYPES) {
      COIN_ITEM.put(metal, reg.item(metal.toLowerCase() + "_coin", Item::new)
        .properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p)
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapelessRecipe(ctx.get(), 4)
          .addIngredient(COINSTACK_ITEM.get(metal).get())
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(COINSTACK_ITEM.get(metal).get()))
          .build(prov)
        )
        .lang(metal + " Coin")
        .register());
      COINSTACK_ITEM.put(metal, reg.item(metal.toLowerCase() + "_coinstack", CoinStackItem::new)
        .properties(p -> (metal.equals("Netherite")) ? p.fireproof() : p)
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapelessRecipe(ctx.get())
          .addIngredient(COIN_ITEM.get(metal).get(), 4)
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(COIN_ITEM.get(metal).get()))
          .build(prov)
        )
        .lang(metal + " Coinstack")
        .register());
    }
  }
}
