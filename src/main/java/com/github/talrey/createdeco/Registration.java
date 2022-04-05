package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.github.talrey.createdeco.blocks.DecalBlock;
import com.github.talrey.createdeco.blocks.VerticalSlabBlock;
import com.github.talrey.createdeco.connected.*;
import com.github.talrey.createdeco.items.CatwalkBlockItem;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllInteractionBehaviours;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.DoorMovingInteraction;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.Registrate;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.DataIngredient;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Material;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class Registration {

  private static HashMap<DyeColor, String> BRICK_COLOR_NAMES          = new HashMap<>();
  private static ArrayList<String> COIN_TYPES                         = new ArrayList<>();
  private static HashMap<String, Function<String, Item>> DOOR_TYPES   = new HashMap<>();
  public static HashMap<String, Function<String, Item>> METAL_TYPES   = new HashMap<>();
  private static HashMap<String, Function<String, Item>> METAL_LOOKUP = new HashMap<>();

  public static ItemEntry<Item> WORN_BRICK_ITEM;
  public static HashMap<String,   BlockEntry<Block>> WORN_BRICK_TYPES       = new HashMap<>();
  public static HashMap<String,   BlockEntry<StairBlock>> WORN_STAIRS       = new HashMap<>();
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

  public static HashMap<DyeColor, HashMap<String,BlockEntry<StairBlock>>> BRICK_STAIRS_BLOCK      = new HashMap<>();
  public static HashMap<DyeColor, HashMap<String,BlockEntry<SlabBlock>>> BRICK_SLAB_BLOCK         = new HashMap<>();
  public static HashMap<DyeColor, HashMap<String,BlockEntry<VerticalSlabBlock>>> BRICK_VERT_BLOCK = new HashMap<>();
  public static HashMap<DyeColor, HashMap<String,BlockEntry<WallBlock>>> BRICK_WALL_BLOCK         = new HashMap<>();

  public static HashMap<String, BlockEntry<CoinStackBlock>> COIN_BLOCKS     = new HashMap<>();
  public static HashMap<String, BlockEntry<DoorBlock>> DOOR_BLOCKS          = new HashMap<>();
  public static HashMap<String, BlockEntry<DoorBlock>> LOCK_DOOR_BLOCKS     = new HashMap<>();
  public static HashMap<String, BlockEntry<IronBarsBlock>> BAR_BLOCKS       = new HashMap<>();
  public static HashMap<String, BlockEntry<IronBarsBlock>> BAR_PANEL_BLOCKS = new HashMap<>();

  public static HashMap<String, BlockEntry<Block>> SHEET_METAL_BLOCKS    = new HashMap<>();
  public static HashMap<String, BlockEntry<StairBlock>> SHEET_STAIRS    = new HashMap<>();
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

  public static class DecoCreativeModeTab extends CreativeModeTab {
    private final Supplier<ItemStack> sup;
    public DecoCreativeModeTab (final String name, final Supplier<ItemStack> supplier) {
      super(name);
      sup = supplier;
    }
    @Override
    public @NotNull ItemStack makeIcon () {
      return sup.get();
    }
  }
  public static final CreativeModeTab BRICKS_GROUP = new DecoCreativeModeTab(
    CreateDecoMod.MODID + ".bricks",
    () -> BRICK_BLOCK.get(DyeColor.LIGHT_BLUE).asStack()
  );
  public static final CreativeModeTab METALS_GROUP = new DecoCreativeModeTab(
    CreateDecoMod.MODID + ".metals",
    () -> BAR_BLOCKS.get("brass").asStack()
  );
  public static final CreativeModeTab PROPS_GROUP = new DecoCreativeModeTab(
    CreateDecoMod.MODID + ".props",
    () -> COINSTACK_ITEM.get("brass").asStack()
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

    DOOR_TYPES.put("Andesite",   (str) -> AllItems.ANDESITE_ALLOY.get());
    DOOR_TYPES.put("Copper",     (str) -> Items.COPPER_INGOT);
    DOOR_TYPES.put("Zinc",       (str) -> AllItems.ZINC_INGOT.get());
    DOOR_TYPES.put("Brass",      (str) -> AllItems.BRASS_INGOT.get());

    METAL_TYPES.put("Andesite",  (str) -> AllItems.ANDESITE_ALLOY.get());
    METAL_TYPES.put("Zinc",      (str) -> AllItems.ZINC_INGOT.get());
    METAL_TYPES.put("Copper",    (str) -> Items.COPPER_INGOT);
    METAL_TYPES.put("Brass",     (str) -> AllItems.BRASS_INGOT.get());
    METAL_TYPES.put("Iron",      (str) -> Items.IRON_INGOT);
    METAL_TYPES.put("Gold",      (str) -> Items.GOLD_INGOT);
    METAL_TYPES.put("Netherite", (str) -> Items.NETHERITE_INGOT);

    METAL_LOOKUP.put("Andesite",  (str) -> str.equals("block") ? AllBlocks.ANDESITE_CASING.get().asItem() : AllItems.ANDESITE_ALLOY.get());
    METAL_LOOKUP.put("Zinc",      (str) -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:zinc_block")));
    METAL_LOOKUP.put("Copper",    (str) -> Items.COPPER_BLOCK);
    METAL_LOOKUP.put("Brass",     (str) -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:brass_block")));
    METAL_LOOKUP.put("Iron",      (str) -> Items.IRON_BLOCK);
    METAL_LOOKUP.put("Gold",      (str) -> Items.GOLD_BLOCK);
    METAL_LOOKUP.put("Netherite", (str) -> Items.NETHERITE_BLOCK);
  }

  private static BlockEntry<?> getBrickFromName (String overlay, DyeColor dye, String shape) {
    if (overlay.trim().equals ("Mossy")) {
      return switch (shape.trim()) {
        case "Brick Tiles" -> MOSSY_TILE_BLOCK.get(dye);
        case "Long Bricks" -> MOSSY_LONG_BLOCK.get(dye);
        case "Short Bricks" -> MOSSY_SHORT_BLOCK.get(dye);
        default -> MOSSY_BRICK_BLOCK.get(dye);
      };
    }
    else if (overlay.trim().equals ("Cracked")) {
      return switch (shape.trim()) {
        case "Brick Tiles" -> CRACKED_TILE_BLOCK.get(dye);
        case "Long Bricks" -> CRACKED_LONG_BLOCK.get(dye);
        case "Short Bricks" -> CRACKED_SHORT_BLOCK.get(dye);
        default -> CRACKED_BRICK_BLOCK.get(dye);
      };
    }
    return switch (shape.trim()) {
      case "Brick Tiles" -> TILE_BRICK_BLOCK.get(dye);
      case "Long Bricks" -> LONG_BRICK_BLOCK.get(dye);
      case "Short Bricks" -> SHORT_BRICK_BLOCK.get(dye);
      default -> BRICK_BLOCK.get(dye);
    };
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
    String suf = suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String pre = prefix.replace(' ', '_').toLowerCase(Locale.ROOT) + (prefix.equals("")?"":"_");
    BlockBuilder<Block,?> ret = reg.block(pre + name.toLowerCase(Locale.ROOT) + "_" + suf, Block::new);
    if (dye != null) {
      ret.initialProperties(Material.STONE, dye);
    } else {
      ret.initialProperties(Material.STONE);
    }
    return ret.properties(props -> props.strength(2,6).requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.simpleBlock(ctx.get(), prov.models().cubeAll(ctx.getName(),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase(Locale.ROOT) + "/" + pre + name.toLowerCase(Locale.ROOT)+"_" + suf)
      )))
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix)
      .defaultLoot()
      .simpleItem();
  }

  private static BlockBuilder<StairBlock,?> buildBrickStairs (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String pre = prefix.replace(' ', '_').toLowerCase(Locale.ROOT) + (prefix.equals("")?"":"_");
    BlockBuilder<StairBlock,?> ret = reg.block(pre + name.toLowerCase(Locale.ROOT) + "_" + suf + "_stairs",
      (props)->new StairBlock(Blocks.BRICK_STAIRS::defaultBlockState, props));
    if (dye != null) {
      ret.initialProperties(Material.STONE, dye);
    } else {
      ret.initialProperties(Material.STONE);
    }
    return ret.properties(props -> props.strength(2,6).requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.stairsBlock(ctx.get(),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase(Locale.ROOT) + "/" + pre + name.toLowerCase(Locale.ROOT)+"_" + suf)
      ))
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Stairs")
      .defaultLoot()
      .simpleItem();
  }

  private static BlockBuilder<SlabBlock,?> buildBrickSlabs (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String pre = prefix.replace(' ', '_').toLowerCase(Locale.ROOT) + (prefix.equals("")?"":"_");
    BlockBuilder<SlabBlock,?> ret = reg.block(pre + name.toLowerCase(Locale.ROOT) + "_" + suf  + "_slab", SlabBlock::new);
    if (dye != null) {
      ret.initialProperties(Material.STONE, dye);
    } else {
      ret.initialProperties(Material.STONE);
    }
    return ret.properties(props -> props.strength(2,6).requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.slabBlock(ctx.get(),
        prov.modLoc("block/" + pre + name.toLowerCase(Locale.ROOT) + (suf.equals("")?"":"_"+suf)),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase(Locale.ROOT) + "/" + pre + name.toLowerCase(Locale.ROOT)+"_" + suf)
      ))
      .loot((table, block) -> {
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder pool     = LootPool.lootPool();
        pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block)
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
              .setProperties(StatePropertiesPredicate.Builder.properties()
                .hasProperty(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
              )
            )
          )
        );
        table.add(block, builder.withPool(pool));
      })
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Slab")
      .simpleItem();
  }

  private static BlockBuilder<VerticalSlabBlock,?> buildBrickVerts (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String pre = prefix.replace(' ', '_').toLowerCase(Locale.ROOT) + (prefix.equals("")?"":"_");
    BlockBuilder<VerticalSlabBlock,?> ret = reg.block(pre + name.toLowerCase(Locale.ROOT) + "_" + suf  + "_slab_vert", VerticalSlabBlock::new);
    if (dye != null) {
      ret.initialProperties(Material.STONE, dye);
    } else {
      ret.initialProperties(Material.STONE);
    }
    return ret.properties(props -> props.strength(2,6).requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> {
        String texLoc = "block/palettes/bricks/" + name.toLowerCase(Locale.ROOT) + "/" + pre + name.toLowerCase(Locale.ROOT)+"_" + suf;
        ResourceLocation tex = prov.modLoc(texLoc);
        BlockModelBuilder half = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/vertical_slab"))
        .texture("side", tex);
        BlockModelBuilder both = prov.models().cubeAll(ctx.getName()+"_double", tex);

        int y = 0;
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
          switch (dir) {
            case NORTH -> y = 0;
            case SOUTH -> y = 180;
            case WEST -> y = -90;
            case EAST -> y = 90;
          }
          prov.getMultipartBuilder(ctx.get()).part().modelFile(half).rotationY(y).addModel()
          .condition(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
          .condition(BlockStateProperties.HORIZONTAL_FACING, dir).end();
          prov.getMultipartBuilder(ctx.get()).part().modelFile(both).rotationY(y).addModel()
          .condition(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).end();
        }
      })
      .loot((table, block) -> {
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder pool     = LootPool.lootPool();
        pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block)
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
              .setProperties(StatePropertiesPredicate.Builder.properties()
                .hasProperty(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
              )
            )
          )
        );
        table.add(block, builder.withPool(pool));
      })
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Vertical Slab")
      .simpleItem();
  }

  private static BlockBuilder<WallBlock,?> buildBrickWalls (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ','_').toLowerCase(Locale.ROOT);
    String pre = prefix.replace(' ','_').toLowerCase(Locale.ROOT) + (prefix.equals("")?"":"_");
    BlockBuilder<WallBlock,?> ret = reg.block(pre + name.toLowerCase(Locale.ROOT) + "_" + suf + "_wall", WallBlock::new);
    if (dye != null) {
      ret.initialProperties(Material.STONE, dye);
    } else {
      ret.initialProperties(Material.STONE);
    }
    return ret.properties(props-> props.strength(2,6).requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
      )
      .blockstate((ctx,prov)-> prov.wallBlock(ctx.get(),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase(Locale.ROOT) + "/" + pre + name.toLowerCase(Locale.ROOT)+"_" + suf)
      ))
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix + " Wall")
      .defaultLoot()
      .tag(BlockTags.WALLS)
      .item()
        .model((ctx,prov)-> prov.wallInventory("item/" + pre + name.toLowerCase(Locale.ROOT) + "_" + suf + "_wall",
          prov.modLoc("block/palettes/bricks/"
            + name.toLowerCase(Locale.ROOT) + "/" + pre + name.toLowerCase(Locale.ROOT)+"_" + suf
        )))
        .build();
  }

  private static BlockBuilder<IronBarsBlock,?> buildBars (Registrate reg, String metal, Function<String,Item> getter, String suffix) {
    String base = metal.replace(' ', '_').toLowerCase(Locale.ROOT) + "_bars";
    String suf = suffix.equals("") ? "" : "_" + suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String post = "block/palettes/metal_bars/" + base + (metal.equals("Brass") || metal.equals("Netherite") ? "_post" : "");

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

    return reg.block(base + suf, IronBarsBlock::new)
      .properties(props -> props.noOcclusion().strength(5, (metal.equals("Netherite")) ? 1200 : 6)
      .sound(SoundType.NETHERITE_BLOCK))
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
          if (suf.isEmpty()) {
            prov.singleTexture(base, prov.mcLoc("item/generated"),"layer0", bartex);
          }
          else {
            prov.withExistingParent(base + suf, prov.mcLoc("item/generated"))
              .texture("layer0", bartex)
              .texture("layer1", prov.modLoc("block/palettes/metal_bars/" + base + suf));
          }
        })
        .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
        .build();
  }

  public static void registerBlocks (Registrate reg) {
    reg.creativeModeTab(()->BRICKS_GROUP);

    BlockBuilder<Block, ?> wornBrick = buildBrick(reg, null, "", "Worn", "Bricks")
      .recipe((ctx,prov)-> ShapedRecipeBuilder.shaped(ctx.get())
        .pattern("bb")
        .pattern("bb")
        .define('b', WORN_BRICK_ITEM.get())
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(WORN_BRICK_ITEM.get()))
        .save(prov)
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
            ShapedRecipeBuilder.shaped(ctx.get())
              .pattern("s")
              .pattern("s")
              .pattern("s")
              .define('s', WORN_SLABS.get(full).get())
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(WORN_SLABS.get(full).get()))
              .save(prov);
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
        .recipe((ctx,prov)-> ShapedRecipeBuilder.shaped(ctx.get())
          .pattern("bb")
          .pattern("bb")
          .define('b', getBrickItemFromColor(dye))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(getBrickItemFromColor(dye)))
          .save(prov)
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

      HashMap<String, BlockEntry<StairBlock>>       stair = new HashMap<>();
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
              ShapedRecipeBuilder.shaped(ctx.get())
                .pattern("s")
                .pattern("s")
                .pattern("s")
                .define('s', getBrickSlabBlockFromColor(dye, full))
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(getBrickSlabBlockFromColor(dye, full)))
              .save(prov);
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

    reg.creativeModeTab(()->PROPS_GROUP);
    COIN_TYPES.forEach(metal ->
      COIN_BLOCKS.put(metal.toLowerCase(Locale.ROOT), reg.block(metal.toLowerCase(Locale.ROOT)+"_coinstack_block", (p)->new CoinStackBlock(p, metal.toLowerCase(Locale.ROOT)))
        .properties(props -> props.noOcclusion().strength(0.5f).sound(SoundType.CHAIN))
        .blockstate((ctx,prov)-> prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
          int layer = state.getValue(BlockStateProperties.LAYERS);
          return ConfiguredModel.builder().modelFile(prov.models().withExistingParent(
            ctx.getName() + "_" + layer, prov.modLoc("block/layers_bottom_top_" + layer)
          )
          .texture("side",   prov.modLoc("block/" + metal.toLowerCase(Locale.ROOT) + "_coinstack_side"))
          .texture("bottom", prov.modLoc("block/" + metal.toLowerCase(Locale.ROOT) + "_coinstack_bottom"))
          .texture("top",    prov.modLoc("block/" + metal.toLowerCase(Locale.ROOT) + "_coinstack_top"))
        ).build(); }))
        .addLayer(()-> RenderType::cutoutMipped)
        .lang(metal + " Stack Block")
        .loot((table, block) -> {
          LootTable.Builder builder      = LootTable.lootTable();
          LootPool.Builder pool          = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
          for (int layer = 1; layer <= 8; layer++) {
            LootItem.Builder<?> entry = LootItem.lootTableItem(COINSTACK_ITEM.get(metal).get());
            entry.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
              .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(BlockStateProperties.LAYERS, layer)
            )).apply(SetItemCountFunction.setCount(ConstantValue.exactly(layer)));
            pool.add(entry);
          }
          table.add(block, builder.withPool(pool));
        })
        .register())
    );

    for (DyeColor color : DyeColor.values()) {
      DECAL_BLOCKS.put(color, reg.block(color.name().toLowerCase(Locale.ROOT) + "_decal", DecalBlock::new)
        .initialProperties(Material.METAL)
        .properties(props-> props.noOcclusion().strength(0.5f).sound(SoundType.LANTERN))
        .blockstate((ctx,prov)-> prov.getVariantBuilder(ctx.get()).forAllStates(state-> {
          int y = 0;
          switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
        .addLayer(()-> RenderType::cutoutMipped)
        .lang(color.name().charAt(0) + color.name().substring(1).toLowerCase() + " Decal")
        .item()
          .model((ctx,prov)-> prov.singleTexture(ctx.getName(),
            prov.mcLoc("item/generated"),
            "layer0", prov.modLoc("block/palettes/decal/" + ctx.getName())
          ))
          .build()
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(ctx.get())
          .requires(AllItems.IRON_SHEET.get(), 1)
          .requires(DyeItem.byColor(color), 1)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.IRON_SHEET.get()))
          .unlockedBy("has_dye",  InventoryChangeTrigger.TriggerInstance.hasItems(DyeItem.byColor(color)))
          .save(prov)
        )
        .register());
    }

    reg.creativeModeTab(()->METALS_GROUP);
    DOOR_TYPES.forEach((metal,ingot) ->
      DOOR_BLOCKS.put(metal.toLowerCase(Locale.ROOT), reg.block(metal.toLowerCase(Locale.ROOT) + "_door", DoorBlock::new)
        .initialProperties(Material.NETHER_WOOD) // setting it to IRON locks it, and normal wood would burn probably
        .properties(props -> props.noOcclusion().strength(5, 5).requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .blockstate((ctx, prov) -> prov.doorBlock(ctx.get(),
          prov.modLoc("block/" + metal.toLowerCase(Locale.ROOT) + "_door_bottom"),
          prov.modLoc("block/" + metal.toLowerCase(Locale.ROOT) + "_door_top"))
        )
        .addLayer(()-> RenderType::cutoutMipped)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .tag(BlockTags.DOORS)
        .lang(metal + " Door")
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.get())
          .pattern("mm")
          .pattern("mm")
          .pattern("mm")
          .define('m', ingot.apply(metal))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ingot.apply(metal)))
          .save(prov)
        )
        .loot((table, block)-> {
          LootTable.Builder builder = LootTable.lootTable();
          LootPool.Builder pool     = LootPool.lootPool();
          pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block))
            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
              .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            ));
          table.add(block, builder.withPool(pool));
          })
        .item()
          .model((ctx,prov) ->
            prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
              "layer0", prov.modLoc("item/" + ctx.getName())
          ))
          .properties(props -> (metal.equals("Netherite") ? props.fireResistant() : props))
          .build()
        .onRegister( (door)-> AllInteractionBehaviours.addInteractionBehaviour(door, DoorMovingInteraction::new))
        .register()));

    DOOR_TYPES.forEach((metal, ingot)->
      LOCK_DOOR_BLOCKS.put(metal.toLowerCase(Locale.ROOT), reg.block("locked_" + metal.toLowerCase(Locale.ROOT) + "_door", DoorBlock::new)
        .initialProperties(Material.METAL)
        .properties(props -> props.noOcclusion().strength(5, 5).requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .blockstate((ctx, prov)-> prov.doorBlock(ctx.get(),
          prov.modLoc("block/locked_" + metal.toLowerCase(Locale.ROOT) + "_door_bottom"),
          prov.modLoc("block/locked_" + metal.toLowerCase(Locale.ROOT) + "_door_top"))
        )
        .addLayer(()-> RenderType::cutoutMipped)
        .loot((table, block)-> {
          LootTable.Builder builder = LootTable.lootTable();
          LootPool.Builder pool     = LootPool.lootPool();
          pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block))
            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
              .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            ));
          table.add(block, builder.withPool(pool));
        })
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .tag(BlockTags.DOORS)
        .lang("Locked " + metal + " Door")
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(ctx.get())
          .requires(Items.REDSTONE_TORCH, 1)
          .requires(DOOR_BLOCKS.get(metal.toLowerCase(Locale.ROOT)).get(), 1)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            DOOR_BLOCKS.get(metal.toLowerCase(Locale.ROOT)).asStack().getItem())
          )
          .save(prov)
        )
        .item()
          .model((ctx, prov)-> prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
            "layer0", prov.modLoc("item/" + ctx.getName())
          ))
          .properties(props -> (metal.equals("Netherite") ? props.fireResistant() : props))
          .build()
        .register()));

    METAL_TYPES.forEach((metal, getter) -> {
      BAR_BLOCKS.put(metal.toLowerCase(Locale.ROOT), buildBars(reg, (metal.equals("Iron")?"Polished Iron":metal), getter, "")
        .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
        .addLayer(()-> RenderType::cutoutMipped)
        .recipe((ctx, prov) -> {
          if (!metal.equals("Iron")) { // Iron will be handled as a polishing recipe
            ShapedRecipeBuilder.shaped(ctx.get(), 16)
              .pattern("mmm")
              .pattern("mmm")
              .define('m', getter.apply(metal))
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(getter.apply(metal)))
              .save(prov);
          }
          ShapelessRecipeBuilder.shapeless(ctx.get())
            .requires(BAR_PANEL_BLOCKS.get(metal).get())
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(BAR_PANEL_BLOCKS.get(metal).get()))
            .save(prov, new ResourceLocation(CreateDecoMod.MODID, metal.toLowerCase(Locale.ROOT) + "_bars_from_panel"));
        })
        .register());
      BAR_PANEL_BLOCKS.put(metal, buildBars(reg, (metal.equals("Iron")?"Polished Iron":metal), getter, "overlay")
        .addLayer(()-> RenderType::cutoutMipped)
        .lang((metal.equals("Iron")?"Polished Iron":metal) + " Panel Bars ")
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(ctx.get())
          .requires(BAR_BLOCKS.get(metal).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(BAR_BLOCKS.get(metal).get()))
          .save(prov)
        )
        .register());
      if (metal.equals("Iron")) { // add a panel version of the vanilla iron too
        BAR_PANEL_BLOCKS.put("Vanilla Iron", buildBars(reg, metal, getter, "overlay")
          .lang(metal + " Panel Bars")
          .recipe((ctx, prov)-> {
            ShapelessRecipeBuilder.shapeless(ctx.get())
              .requires(Items.IRON_BARS)
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_BARS))
              .save(prov);
            ShapelessRecipeBuilder.shapeless(Items.IRON_BARS)
              .requires(ctx.get())
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ctx.get()))
              .save(prov, new ResourceLocation(CreateDecoMod.MODID, "vanilla_metal_bars_from_panel"));
          })
          .register());
      }

      SHEET_METAL_BLOCKS.put(metal, reg.block(metal.toLowerCase(Locale.ROOT) + "_sheet_metal", Block::new)
        .initialProperties(Material.METAL)
        .properties(props-> props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .blockstate((ctx,prov)-> prov.simpleBlock(ctx.get(), prov.models().cubeAll(ctx.getName(),
          prov.modLoc("block/palettes/sheet_metal/" + ctx.getName())
        )))
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .lang(metal + " Sheet Metal")
        .defaultLoot()
        .item()
          .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
          .build()
        .recipe((ctx, prov)-> {
          prov.stonecutting(DataIngredient.items(METAL_LOOKUP.get(metal).apply("block")), ctx, 4);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal)).getSupplier()
        ))
        .register());

      SHEET_STAIRS.put(metal, reg.block(metal.toLowerCase(Locale.ROOT) + "_sheet_stairs",
        (props)->new StairBlock(Blocks.BRICK_STAIRS::defaultBlockState, props))
        .initialProperties(Material.METAL)
        .properties(props-> props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .item()
          .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
          .build()
        .tag(BlockTags.STAIRS)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate((ctx,prov)-> prov.stairsBlock(ctx.get(),
          prov.modLoc("block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT) + "_sheet_metal"))
        )
        .lang(metal + " Sheet Stairs")
        .recipe((ctx, prov)-> {
          prov.stonecutting(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal)), ctx);
          prov.stairs(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal).get()), ctx, null, false);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal)).getSupplier()
        ))
        .register());

      SHEET_SLABS.put(metal, reg.block(metal.toLowerCase(Locale.ROOT) + "_sheet_slab", SlabBlock::new)
        .initialProperties(Material.METAL)
        .properties(props-> props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .item()
          .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
          .build()
        .tag(BlockTags.SLABS)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate((ctx,prov)-> prov.slabBlock(ctx.get(),
          prov.modLoc("block/" + metal.toLowerCase(Locale.ROOT) + "_sheet_metal"),
          prov.modLoc("block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT) + "_sheet_metal"))
        )
        .loot((table, block) -> {
          LootTable.Builder builder = LootTable.lootTable();
          LootPool.Builder pool     = LootPool.lootPool();
          pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block)
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
              .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                  .hasProperty(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
                )
              )
            )
          );
          table.add(block, builder.withPool(pool));
        })
        .lang(metal + " Sheet Slab")
        .recipe((ctx, prov)-> {
          prov.stonecutting(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal)), ctx, 2);
          prov.slab(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal).get()), ctx, null, false);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalSlabCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal)).getSupplier()
        ))
        .register());

      SHEET_VERT_SLABS.put(metal, reg.block(metal.toLowerCase(Locale.ROOT) + "_sheet_slab_vert", VerticalSlabBlock::new)
        .initialProperties(Material.METAL)
        .properties(props-> props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .item()
          .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
          .build()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate(((ctx,prov)-> {
          String texLoc = "block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT) + "_sheet_metal";
          ResourceLocation tex = prov.modLoc(texLoc);
          BlockModelBuilder half = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/vertical_slab"))
            .texture("side", tex);
          BlockModelBuilder both = prov.models().cubeAll(ctx.getName()+"_double", tex);

          int y = 0;
          for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
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
          LootTable.Builder builder = LootTable.lootTable();
          LootPool.Builder pool     = LootPool.lootPool();
          pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block)
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
              .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                  .hasProperty(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
                )
              )
            )
          );
          table.add(block, builder.withPool(pool));
        })
        .recipe((ctx, prov)-> {
          ShapedRecipeBuilder.shaped(ctx.get(), 3)
            .pattern("s")
            .pattern("s")
            .pattern("s")
            .define('s', SHEET_SLABS.get(metal).get())
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(SHEET_METAL_BLOCKS.get(metal).asStack().getItem()))
            .save(prov);
          prov.stonecutting(DataIngredient.items(SHEET_METAL_BLOCKS.get(metal)), ctx, 2);
        })
        .onRegister(CreateRegistrate.connectedTextures(
          new SheetMetalVertCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal)).getSupplier()
        ))
        .register());

      MESH_FENCE_BLOCKS.put(metal, reg.block(metal.toLowerCase(Locale.ROOT) + "_mesh_fence", FenceBlock::new)
        .initialProperties(Material.METAL)
        .properties(props-> props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
          .sound(SoundType.CHAIN)
        )
        .addLayer(()-> RenderType::cutoutMipped)
        .tag(BlockTags.FENCES)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .item()
          .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
          .model((ctx,prov)-> prov.singleTexture(
            ctx.getName(), prov.mcLoc("item/generated"),
            "layer0", prov.modLoc("block/palettes/chain_link_fence/" + metal.toLowerCase(Locale.ROOT) + "_chain_link")))
          .build()
        .recipe((ctx,prov)-> {
          if (metal.equals("Andesite")) {
            ShapedRecipeBuilder.shaped(ctx.get(), 3)
              .pattern("psp")
              .pattern("psp")
              .define('p', AllItems.ANDESITE_ALLOY.get())
              .define('s', Items.STRING)
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.ANDESITE_ALLOY.get()))
              .save(prov);
          }
          else {
            Tag<Item> sheet = ItemTags.bind("forge:plates/" + metal.toLowerCase(Locale.ROOT));
            ShapedRecipeBuilder.shaped(ctx.get(), 3)
              .pattern("psp")
              .pattern("psp")
              .define('p', sheet)
              .define('s', Items.STRING)
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(sheet).build()))
              .save(prov);
          }
        })
        .blockstate((ctx,prov)-> {
          prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
            String dir = "chainlink_fence";
            boolean north,south,east,west;
            north = state.getValue(BlockStateProperties.NORTH);
            south = state.getValue(BlockStateProperties.SOUTH);
            east  = state.getValue(BlockStateProperties.EAST);
            west  = state.getValue(BlockStateProperties.WEST);
            int sides = (north?1:0) + (south?1:0) + (east?1:0) + (west?1:0);
            ResourceLocation mesh = prov.modLoc("block/palettes/chain_link_fence/" + metal.toLowerCase(Locale.ROOT) + "_chain_link");
            ResourceLocation wall = prov.modLoc("block/palettes/sheet_metal/"      + metal.toLowerCase(Locale.ROOT) + "_sheet_metal");
            switch (sides) {
              case 4: return ConfiguredModel.builder().modelFile(
                  prov.models().withExistingParent(ctx.getName() + "_four_way", prov.modLoc(dir + "_four_way"))
                  .texture("0", mesh).texture("1", wall).texture("particle", wall)
                ).build();
              case 3: return ConfiguredModel.builder().modelFile(
                  prov.models().withExistingParent(ctx.getName() + "_tri_way", prov.modLoc(dir + "_tri_way"))
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

      CATWALK_BLOCKS.put(metal, reg.block(metal.toLowerCase(Locale.ROOT) + "_catwalk", CatwalkBlock::new)
        .initialProperties(Material.METAL)
        .properties(props->
          props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops().noOcclusion()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .addLayer(()-> RenderType::cutoutMipped)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .item(CatwalkBlockItem::new)
          .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
          .model((ctx,prov)-> prov.withExistingParent(ctx.getName(), prov.mcLoc("block/template_trapdoor_bottom"))
            .texture("texture", prov.modLoc("block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT) + "_catwalk"))
          )
          .build()
        .recipe((ctx,prov)-> {
          if (metal.equals("Andesite")) {
            ShapedRecipeBuilder.shaped(ctx.get(), 3)
              .pattern(" p ")
              .pattern("pBp")
              .pattern(" p ")
              .define('p', AllItems.ANDESITE_ALLOY.get())
              .define('B', BAR_BLOCKS.get(metal).get())
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.ANDESITE_ALLOY.get()))
              .save(prov);
          }
          else {
            Tag<Item> sheet = ItemTags.bind("forge:plates/" + metal.toLowerCase(Locale.ROOT));
            ShapedRecipeBuilder.shaped(ctx.get(), 3)
              .pattern(" p ")
              .pattern("pBp")
              .pattern(" p ")
              .define('p', sheet)
              .define('B', BAR_BLOCKS.get(metal).get())
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(sheet).build()))
              .save(prov);
          }
        })
        .blockstate((ctx,prov)-> {
          String texture = "createdeco:block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT) + "_catwalk";

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
          new CatwalkCTBehaviour(SpriteShifts.CATWALK_TOPS.get(metal)).getSupplier()
        ))
        .register());
    });
  }

  public static void registerItems (Registrate reg) {
    reg.creativeModeTab(()->BRICKS_GROUP, BRICKS_NAME);
    BRICK_COLOR_NAMES.forEach((dye, name)-> {
      if (dye == null) {
        WORN_BRICK_ITEM = reg.item("worn_brick", Item::new)
          .recipe((ctx,prov)-> prov.blasting(DataIngredient.items(Items.BRICK), ctx, 0.3f))
          .register();
      } else {
        BRICK_ITEM.put(dye, reg.item(name.toLowerCase(Locale.ROOT) + "_brick", Item::new)
          .lang(name + " Brick")
          .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.get(), 8)
            .pattern("bbb")
            .pattern("bCb")
            .pattern("bbb")
            .define('b', Items.BRICK)
            .define('C', DyeItem.byColor(dye))
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(DyeItem.byColor(dye)))
            .save(prov)
          )
          .register());
      }
    });

    reg.creativeModeTab(()->METALS_GROUP, METALS_NAME);
    ZINC_SHEET = reg.item("zinc_sheet", Item::new)
      .tag(ItemTags.bind("forge:plates/zinc"))
      .lang("Zinc Sheet")
      .register();

    NETHERITE_SHEET = reg.item("netherite_sheet", Item::new)
      .properties(p -> p.fireResistant())
      .tag(ItemTags.bind("forge:plates/netherite"))
      .lang("Netherite Sheet")
      .register();

    NETHERITE_NUGGET = reg.item("netherite_nugget", Item::new)
      .properties(p -> p.fireResistant())
      .tag(ItemTags.bind("forge:nuggets/netherite"))
      .lang("Netherite Nugget")
      .recipe((ctx, prov)-> {
        prov.storage(ctx, ()->Items.NETHERITE_INGOT);
      })
      .register();

    reg.creativeModeTab(()->PROPS_GROUP, PROPS_NAME);
    for (String metal : COIN_TYPES) {
      COIN_ITEM.put(metal.toLowerCase(Locale.ROOT), reg.item(metal.toLowerCase(Locale.ROOT) + "_coin", Item::new)
        .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(ctx.get(), 4)
          .requires(COINSTACK_ITEM.get(metal).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(COINSTACK_ITEM.get(metal.toLowerCase(Locale.ROOT)).get()))
          .save(prov)
        )
        .lang(metal + " Coin")
        .register());
      COINSTACK_ITEM.put(metal.toLowerCase(Locale.ROOT), reg.item(metal.toLowerCase(Locale.ROOT) + "_coinstack", (p)-> new CoinStackItem(p, metal.toLowerCase(Locale.ROOT)))
        .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(ctx.get())
          .requires(COIN_ITEM.get(metal).get(), 4)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(COIN_ITEM.get(metal).get()))
          .save(prov)
        )
        .lang(metal + " Coinstack")
        .register());
    }
  }
}
