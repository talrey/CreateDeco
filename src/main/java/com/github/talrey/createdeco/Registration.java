package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.*;
import com.github.talrey.createdeco.registry.*;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Registration {

  private static HashMap<DyeColor, String> BRICK_COLOR_NAMES          = new HashMap<>();

  private static HashMap<String, Function<String, Item>> DOOR_TYPES   = new HashMap<>();
  public static HashMap<String, Function<String, Item>> METAL_TYPES   = new HashMap<>();

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

  public static HashMap<String, BlockEntry<DoorBlock>> DOOR_BLOCKS          = new HashMap<>();
  public static HashMap<String, BlockEntry<DoorBlock>> LOCK_DOOR_BLOCKS     = new HashMap<>();
  public static HashMap<String, BlockEntry<TrapDoorBlock>> TRAPDOOR_BLOCKS  = new HashMap<>();
  public static HashMap<String, BlockEntry<IronBarsBlock>> BAR_BLOCKS       = new HashMap<>();
  public static HashMap<String, BlockEntry<IronBarsBlock>> BAR_PANEL_BLOCKS = new HashMap<>();

  public static HashMap<String, BlockEntry<FenceBlock>> MESH_FENCE_BLOCKS     = new HashMap<>();
  public static HashMap<String, BlockEntry<CatwalkBlock>> CATWALK_BLOCKS      = new HashMap<>();
  public static HashMap<String, BlockEntry<CatwalkStairBlock>> CATWALK_STAIRS = new HashMap<>();

  public static HashMap<String, BlockEntry<HullBlock>> HULL_BLOCKS       = new HashMap<>();
  public static HashMap<String, BlockEntry<SupportBlock>> SUPPORT_BLOCKS = new HashMap<>();

  public static HashMap<DyeColor, ItemEntry<Item>> BRICK_ITEM = new HashMap<>();

  public static ItemEntry<Item> ZINC_SHEET;
  public static ItemEntry<Item> NETHERITE_SHEET;
  public static ItemEntry<Item> NETHERITE_NUGGET;
  public static ItemEntry<Item> CAST_IRON_NUGGET;
  public static ItemEntry<Item> CAST_IRON_INGOT;
  public static ItemEntry<Item> CAST_IRON_SHEET;
  public static BlockEntry<Block> CAST_IRON_BLOCK;

  public Registration () {
    BRICK_COLOR_NAMES.put(DyeColor.BLACK, "Dusk");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_GRAY, "Pearl");
    BRICK_COLOR_NAMES.put(DyeColor.RED, "Scarlet");
    BRICK_COLOR_NAMES.put(DyeColor.YELLOW, "Dean");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_BLUE, "Blue");
    BRICK_COLOR_NAMES.put(null, "Red"); // this is funky but it works, I swear.

    DOOR_TYPES.put("Andesite",   (str) -> AllItems.ANDESITE_ALLOY.get());
    DOOR_TYPES.put("Copper",     (str) -> Items.COPPER_INGOT);
    DOOR_TYPES.put("Zinc",       (str) -> AllItems.ZINC_INGOT.get());
    DOOR_TYPES.put("Brass",      (str) -> AllItems.BRASS_INGOT.get());
    DOOR_TYPES.put("Cast Iron",  (str) -> CAST_IRON_INGOT.get());

    METAL_TYPES.put("Andesite",  (str) -> AllItems.ANDESITE_ALLOY.get());
    METAL_TYPES.put("Zinc",      (str) -> AllItems.ZINC_INGOT.get());
    METAL_TYPES.put("Copper",    (str) -> Items.COPPER_INGOT);
    METAL_TYPES.put("Brass",     (str) -> AllItems.BRASS_INGOT.get());
    METAL_TYPES.put("Iron",      (str) -> Items.IRON_INGOT);
    METAL_TYPES.put("Gold",      (str) -> Items.GOLD_INGOT);
    METAL_TYPES.put("Netherite", (str) -> Items.NETHERITE_INGOT);
    METAL_TYPES.put("Cast Iron", (str) -> CAST_IRON_INGOT.get());

    Props.COIN_TYPES.add("Zinc");
    Props.COIN_TYPES.add("Copper");
    Props.COIN_TYPES.add("Brass");
    Props.COIN_TYPES.add("Iron");
    Props.COIN_TYPES.add("Gold");
    Props.COIN_TYPES.add("Netherite");
    Props.COIN_TYPES.add("Cast Iron");
  }

  public static TagKey<Item> makeItemTag (String path) {
    return makeItemTag("forge", path);
  }

  public static TagKey<Item> makeItemTag(String id, String path) {
    return ForgeRegistries.ITEMS.tags().createOptionalTagKey(new ResourceLocation(id, path), Collections.emptySet());
  }

  public static TagKey<Item> makeItemTagWith (String id, String path, Supplier<Item> item) {
    Set<Supplier<Item>> include = Collections.emptySet();
    include.add(item);
    return ForgeRegistries.ITEMS.tags().createOptionalTagKey(new ResourceLocation(id, path), include);
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

  public static void registerBlocks (Registrate reg) {
    reg.creativeModeTab(()-> DecoCreativeModeTab.BRICKS_GROUP);

    BlockBuilder<Block, ?> wornBrick = BrickBuilders.buildBrick(reg, null, "", "Worn", "Bricks")
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
          WORN_BRICK_TYPES.put(full, BrickBuilders.buildBrick(reg, null, pre, "Worn", suf)
            .recipe((ctx, prov) -> {
              prov.stonecutting(DataIngredient.items(wornBrick.get()), ctx);
              if (pre.equals("Cracked")) prov.blasting(
                DataIngredient.items(WORN_BRICK_TYPES.get("Worn " + suf)), ctx, 0.5f
              );
            })
            .register()
          );
        }
        WORN_STAIRS.put(full, BrickBuilders.buildBrickStairs(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            if (! (pre.equals("") && suf.equals("Bricks"))) prov.stonecutting(DataIngredient.items(wornBrick.get()), ctx);
            prov.stonecutting(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx);
            prov.stairs(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, null, false);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_STAIRS.get("Worn " + suf)), ctx, 0.5f);
            }
          })
          .register()
        );
        WORN_SLABS.put(full, BrickBuilders.buildBrickSlabs(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            if (! (pre.equals("") && suf.equals("Bricks"))) prov.stonecutting(DataIngredient.items(wornBrick.get()), ctx, 2);
            prov.stonecutting(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, 2);
            prov.slab(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, null, false);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_SLABS.get("Worn " + suf)), ctx, 0.5f);
            }
          })
          .register()
        );
        WORN_VERTS.put(full, BrickBuilders.buildBrickVerts(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            if (! (pre.equals("") && suf.equals("Bricks"))) prov.stonecutting(DataIngredient.items(wornBrick.get()), ctx, 2);
            prov.stonecutting(DataIngredient.items(WORN_BRICK_TYPES.get(full)), ctx, 2);
            ShapedRecipeBuilder.shaped(ctx.get(), 3)
              .pattern("s")
              .pattern("s")
              .pattern("s")
              .define('s', WORN_SLABS.get(full).get())
              .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(WORN_SLABS.get(full).get()))
              .save(prov);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_VERTS.get("Worn " + suf)), ctx, 0.5f);
            }
          }).register()
        );
        WORN_WALLS.put(full, BrickBuilders.buildBrickWalls(reg, null, pre, "Worn", suf)
          .recipe((ctx, prov)-> {
            if (! (pre.equals("") && suf.equals("Bricks"))) prov.stonecutting(DataIngredient.items(wornBrick.get()), ctx);
            prov.wall(DataIngredient.items(WORN_BRICK_TYPES.get(full).get()), ctx);
            if (pre.equals("Cracked")) {
              prov.blasting(DataIngredient.items(WORN_WALLS.get("Worn " + suf)), ctx, 0.5f);
            }
          }).register()
        );
      }
    }

    BRICK_COLOR_NAMES.forEach((dye, name)-> {
      if (dye != null) BRICK_BLOCK.put(dye, BrickBuilders.buildBrick(reg, dye, "", name, "Bricks")
        .recipe((ctx,prov)-> ShapedRecipeBuilder.shaped(ctx.get())
          .pattern("bb")
          .pattern("bb")
          .define('b', getBrickItemFromColor(dye))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(getBrickItemFromColor(dye)))
          .save(prov)
        ).register());
      TILE_BRICK_BLOCK.put(dye,    BrickBuilders.buildBrick(reg, dye, "", name, "Brick Tiles")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      LONG_BRICK_BLOCK.put(dye,    BrickBuilders.buildBrick(reg, dye, "", name, "Long Bricks")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      SHORT_BRICK_BLOCK.put(dye,   BrickBuilders.buildBrick(reg, dye, "", name, "Short Bricks")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      CRACKED_BRICK_BLOCK.put(dye, BrickBuilders.buildBrick(reg, dye, "Cracked", name, "Bricks")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      CRACKED_TILE_BLOCK.put(dye,  BrickBuilders.buildBrick(reg, dye, "Cracked", name, "Brick Tiles")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(TILE_BRICK_BLOCK.get(dye).get()), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      CRACKED_LONG_BLOCK.put(dye,  BrickBuilders.buildBrick(reg, dye, "Cracked", name, "Long Bricks")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(LONG_BRICK_BLOCK.get(dye).get()), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      CRACKED_SHORT_BLOCK.put(dye, BrickBuilders.buildBrick(reg, dye, "Cracked", name, "Short Bricks")
        .recipe((ctx, prov)-> {
          prov.blasting(DataIngredient.items(SHORT_BRICK_BLOCK.get(dye).get()), ctx, 0.5f);
          prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx);
        })
        .register());
      MOSSY_BRICK_BLOCK.put(dye,   BrickBuilders.buildBrick(reg, dye, "Mossy", name, "Bricks")
        // washing handled in wrapper class
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      MOSSY_TILE_BLOCK.put(dye,    BrickBuilders.buildBrick(reg, dye, "Mossy", name, "Brick Tiles")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      MOSSY_LONG_BLOCK.put(dye,    BrickBuilders.buildBrick(reg, dye, "Mossy", name, "Long Bricks")
        .recipe((ctx, prov)-> prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx))
        .register());
      MOSSY_SHORT_BLOCK.put(dye,   BrickBuilders.buildBrick(reg, dye, "Mossy", name, "Short Bricks")
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
            stair.put(full, BrickBuilders.buildBrickStairs(reg, dye, pre, name, suf)
              .recipe((ctx, prov) -> {
                prov.stonecutting(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx);
                prov.stairs(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx, null, false);
                if (pre.equals("Cracked")) {
                  prov.blasting(DataIngredient.items(getBrickStairBlockFromColor(dye, full.substring(8))), ctx, 0.5f);
                }
                // handle washing in the wrapper
            }).register());

            slab.put(full, BrickBuilders.buildBrickSlabs(reg, dye, pre, name, suf)
              .recipe((ctx, prov) -> {
                prov.stonecutting(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx, 2);
                prov.slab(DataIngredient.items(getBrickFromName(pre, dye, suf)), ctx, null, false);
                if (pre.equals("Cracked")) {
                  prov.blasting(DataIngredient.items(getBrickSlabBlockFromColor(dye, full.substring(8))), ctx, 0.5f);
                }
                // handle washing in the wrapper
            }).register());

            wall.put(full, BrickBuilders.buildBrickWalls(reg, dye, pre, name, suf)
              .recipe((ctx, prov) -> {
                prov.wall(DataIngredient.items(getBrickFromName(pre, dye, suf).get()), ctx);
                if (pre.equals("Cracked")) {
                  prov.blasting(DataIngredient.items(getBrickWallBlockFromColor(dye, full.substring(8))), ctx, 0.5f);
                }
                // handle washing in the wrapper
            }).register());
          }

          vert.put(full, BrickBuilders.buildBrickVerts( reg, dye, pre, name, suf)
            .recipe((ctx, prov)-> {
              prov.stonecutting(DataIngredient.items(getBrickBlockFromColor(dye)), ctx, 2);
              ShapedRecipeBuilder.shaped(ctx.get(), 3)
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

    reg.creativeModeTab(()->DecoCreativeModeTab.METALS_GROUP);
    SheetMetal.registerBlocks(reg);

    DOOR_TYPES.forEach((metal,ingot) ->
      DOOR_BLOCKS.put(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_"), MetalDecoBuilders.buildDoor(
        reg, metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_door",
          "block/palettes/doors/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_"),
        Material.HEAVY_METAL
      )
        .lang(metal + " Door")
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.get(), 3)
          .pattern("mm")
          .pattern("mm")
          .pattern("mm")
          .define('m', ingot.apply(metal))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ingot.apply(metal)))
          .save(prov)
        )
        .register()));

    DOOR_TYPES.forEach((metal, ingot)->
      LOCK_DOOR_BLOCKS.put(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_"), MetalDecoBuilders.buildDoor(
        reg, "locked_" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_door", "block/palettes/doors/locked_" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")
      )
        .lang("Locked " + metal + " Door")
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(ctx.get())
          .requires(Items.REDSTONE_TORCH, 1)
          .requires(Registration.DOOR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get(), 1)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            Registration.DOOR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).asStack().getItem())
          )
          .save(prov)
        )
        .register()));

    DOOR_TYPES.forEach((metal, ingot)-> {
      String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
      ResourceLocation texture = new ResourceLocation(
        CreateDecoMod.MODID, "block/palettes/doors/" + regName + "_trapdoor"
      );
      TRAPDOOR_BLOCKS.put(regName, reg.block(regName + "_trapdoor", TrapDoorBlock::new)
          .initialProperties(Material.HEAVY_METAL)
        .properties(props -> props.noOcclusion().strength(5, 5)
          .requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
        )
        .blockstate((ctx, prov)-> prov.trapdoorBlock(ctx.get(), texture, true))
        .lang(metal + " Trapdoor")
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .tag(BlockTags.TRAPDOORS)
        .addLayer(()-> RenderType::cutoutMipped)
        .item().model((ctx,prov)->prov.withExistingParent(ctx.getName(),
          prov.mcLoc("block/template_trapdoor_bottom"))
          .texture("texture", prov.modLoc("block/palettes/doors/" + regName + "_trapdoor"))
        ).build()
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.get(), 2)
          .pattern("mmm")
          .pattern("mmm")
          .define('m', ingot.apply(metal))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ingot.apply(metal)))
          .save(prov)
        )
        .register());
    });

    METAL_TYPES.forEach((metal, getter) -> {
      boolean postFlag = (metal.contains("Netherite") || metal.contains("Gold") || metal.contains("Cast Iron"));
      BAR_BLOCKS.put(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_"),
        MetalDecoBuilders.buildBars(reg, (metal.equals("Iron")?"Polished Iron":metal), getter, "", postFlag
      )
        .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
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
            .requires(BAR_PANEL_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get())
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(BAR_PANEL_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get()))
            .save(prov, new ResourceLocation(CreateDecoMod.MODID, metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_bars_from_panel"));
        })
        .register());

      BAR_PANEL_BLOCKS.put(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_"),
        MetalDecoBuilders.buildBars(reg, (metal.equals("Iron")?"Polished Iron":metal), getter, "overlay", postFlag
      )
        .lang((metal.equals("Iron")?"Polished Iron":metal) + " Panel Bars ")
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(ctx.get())
          .requires(BAR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(BAR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get()))
          .save(prov)
        )
        .register());
      if (metal.equals("Iron")) { // add a panel version of the vanilla iron too
        BAR_PANEL_BLOCKS.put("vanilla_iron", MetalDecoBuilders.buildBars(reg, metal, getter, "overlay")
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
      MESH_FENCE_BLOCKS.put(metal, MetalDecoBuilders.buildFence(reg, metal).register());
      CATWALK_BLOCKS.put(metal, MetalDecoBuilders.buildCatwalk(reg, metal).register());
      CATWALK_STAIRS.put(metal, MetalDecoBuilders.buildCatwalkStair(reg, metal).register());
    });

    CAST_IRON_BLOCK = reg.block("cast_iron_block", Block::new)
      .initialProperties(Material.METAL)
      .properties(props->
        props.strength(5, 6).requiresCorrectToolForDrops().noOcclusion()
          .sound(SoundType.NETHERITE_BLOCK)
      )
      .lang("Block of Cast Iron")
      .item()
        .tag(makeItemTag("storage_blocks"))
        .tag(makeItemTag("storage_blocks/cast_iron"))
        .build()
      .register();


    METAL_TYPES.forEach((metal, getter)-> {
      String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
      ResourceLocation front = new ResourceLocation(
        CreateDecoMod.MODID, "block/palettes/hull/" + regName + "_hull_front"
      );
      ResourceLocation side = new ResourceLocation(
        CreateDecoMod.MODID, "block/palettes/hull/" + regName + "_hull_side"
      );
      HULL_BLOCKS.put(regName, reg.block(regName + "_hull", HullBlock::new)
        .initialProperties(Material.METAL)
        .properties(props-> props.strength(5, (metal.contains("Netherite")) ? 1200 : 6)
          .requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
          .noOcclusion()
          .isViewBlocking((a,b,c)->false)
        )
        .addLayer(() -> RenderType::cutoutMipped)
        .item()
          .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
          .build()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate((ctx,prov)->
          prov.getVariantBuilder(ctx.get())
            .forAllStates(state -> {
              Direction dir = state.getValue(BlockStateProperties.FACING);
              return ConfiguredModel.builder()
                .modelFile(prov.models().withExistingParent(
                  ctx.getName(), prov.modLoc("train_hull"))
                  .texture("0", front)
                  .texture("1", side)
                  .texture("particle", front))
                .rotationX(dir == Direction.DOWN ? 270 : dir.getAxis().isHorizontal() ? 0 : 90)
                .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 0) % 360)
                .build();
            })
        )
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.get(), 2)
          .pattern(" m ")
          .pattern("m m")
          .pattern(" m ")
          .define('m', getter.apply(metal))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(getter.apply(metal)))
          .save(prov)
        )
        .simpleItem()
        .lang(metal + " Train Hull")
        .register()
      );
    });

    METAL_TYPES.forEach((metal, getter)-> {
      String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
      ResourceLocation texture = new ResourceLocation(
        CreateDecoMod.MODID, "block/palettes/support/" + regName + "_support"
      );
      SUPPORT_BLOCKS.put(regName, reg.block(regName + "_support", SupportBlock::new)
        .properties(props-> props.strength(5, (metal.contains("Netherite")) ? 1200 : 6)
          .requiresCorrectToolForDrops()
          .sound(SoundType.NETHERITE_BLOCK)
          .noOcclusion()
          .isViewBlocking((a,b,c)->false)
          .isSuffocating((a,b,c)->false)
        )
        .addLayer(() -> RenderType::translucent)
        .item()
        .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
        .build()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate((ctx,prov)-> prov.directionalBlock(ctx.get(),
          prov.models().withExistingParent(ctx.getName(), prov.modLoc("support"))
            .texture("0", texture)
            .texture("particle", texture)
        ))
        .simpleItem()
        .lang(metal + " Support")
        .register()
      );
    });

    Props.registerBlocks(reg);
  }

  public static void registerItems (Registrate reg) {
    reg.creativeModeTab(()->DecoCreativeModeTab.BRICKS_GROUP, DecoCreativeModeTab.BRICKS_NAME);
    BRICK_COLOR_NAMES.forEach((dye, name)-> {
      if (dye == null) {
        WORN_BRICK_ITEM = reg.item("worn_brick", Item::new)
          .recipe((ctx,prov)-> prov.blasting(DataIngredient.items(Items.BRICK), ctx, 0.3f))
          .register();
      } else {
        BRICK_ITEM.put(dye, reg.item(name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_brick", Item::new)
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

    reg.creativeModeTab(()->DecoCreativeModeTab.METALS_GROUP, DecoCreativeModeTab.METALS_NAME);
    ZINC_SHEET = reg.item("zinc_sheet", Item::new)
      .tag(makeItemTag("plates/zinc"))
      .lang("Zinc Sheet")
      .register();

    NETHERITE_SHEET = reg.item("netherite_sheet", Item::new)
      .properties(p -> p.fireResistant())
      .tag(makeItemTag("plates/netherite"))
      .lang("Netherite Sheet")
      .register();

    NETHERITE_NUGGET = reg.item("netherite_nugget", Item::new)
      .properties(p -> p.fireResistant())
      .tag(makeItemTag("nuggets/netherite"))
      .lang("Netherite Nugget")
      .recipe((ctx, prov)-> {
        prov.storage(ctx, ()->Items.NETHERITE_INGOT);
      })
      .register();

    CAST_IRON_NUGGET = reg.item("cast_iron_nugget", Item::new)
      .tag(makeItemTag("nuggets/cast_iron"))
      .lang("Cast Iron Nugget")
      .recipe((ctx, prov)-> {
        prov.storage(ctx, ()->CAST_IRON_INGOT.get());
      })
      .register();

    CAST_IRON_INGOT = reg.item("cast_iron_ingot", Item::new)
      .tag(makeItemTag("ingots/cast_iron"))
      .lang("Cast Iron Ingot")
      .recipe((ctx, prov)-> {
        prov.storage(ctx, ()->CAST_IRON_BLOCK.get().asItem());
      })
      .register();

    CAST_IRON_SHEET = reg.item("cast_iron_sheet", Item::new)
      .tag(makeItemTag("plates/cast_iron"))
      .lang("Cast Iron Sheet")
      .register();

    Props.registerItems(reg);
  }
}
