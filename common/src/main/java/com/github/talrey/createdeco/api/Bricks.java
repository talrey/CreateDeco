package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.CreateDecoMod;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Bricks {
  public static List<String> TYPES = Arrays.asList(
      "", "short", "tiled", "long", "cracked", "mossy"
  );
  private static List<String> CAPITALS = Arrays.asList(
      "", "Short ", "Tiled ", "Long ", "Cracked ", "Mossy "
  );

  public static HashMap<String, AllPaletteStoneTypes> BRICK_STONES = new HashMap<>() {{
    put("blue", AllPaletteStoneTypes.ASURINE);
    put("dean", AllPaletteStoneTypes.OCHRUM);
    put("dusk", AllPaletteStoneTypes.SCORCHIA);
    put("pearl", AllPaletteStoneTypes.LIMESTONE);
    put("scarlet", AllPaletteStoneTypes.CRIMSITE);
    put("verdant", AllPaletteStoneTypes.VERIDIUM);
    put("umber", AllPaletteStoneTypes.SCORIA);
  }};

  public static ArrayList<BlockBuilder<Block,?>> buildBlock (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<Block,?>> ret = new ArrayList<>();
    for (String prefix : TYPES) {
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_bricks";

      if (color.contains("red") && prefix.isEmpty()) continue;
      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, Block::new)

          .initialProperties(() -> Blocks.BRICKS)
          .properties(props -> props
              .strength(2, 6)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
          )
          .blockstate((ctx, prov) -> BlockStateGenerator.brick(ctx, prov, color))
          .tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .lang(
              CAPITALS.get(TYPES.indexOf(prefix))
                  + color.substring(0, 1).toUpperCase()
                  + color.substring(1)
                  + " " + "Bricks"
          )
          .defaultLoot()
          .recipe((ctx, prov) -> {
            if (prefix.isEmpty()) recipeCrafting(color, ctx, prov);
            if (prefix.equals("mossy")) recipeCraftingMossy(color, ctx, prov);
            if (prefix.equals("cracked")) recipeSmeltingCracked(color, ctx, prov);
            recipeStonecuttingBrick(finalName, color, prefix, ctx, prov);
          })
          .simpleItem()
      );
    }
    return ret;
  }

  public static ArrayList<BlockBuilder<StairBlock,?>> buildStair (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<StairBlock, ?>> ret = new ArrayList<>();

    for (String prefix : TYPES) {
      if (color.isEmpty() && prefix.isEmpty()) continue;
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_brick_stairs";

      if (color.contains("red") && prefix.isEmpty()) continue;

      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, p -> new StairBlock(Blocks.BRICK_STAIRS.defaultBlockState(), p))
        .initialProperties(() -> Blocks.BRICKS)
        .properties(props -> props
          .strength(2, 6)
          .requiresCorrectToolForDrops()
          .sound(SoundType.STONE)
        )
        .blockstate((ctx, prov) -> BlockStateGenerator.brickStair(ctx, prov, color))
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .lang(
          CAPITALS.get(TYPES.indexOf(prefix))
            + color.substring(0, 1).toUpperCase()
            + color.substring(1)
            + " " + "Brick Stairs"
        )
        .defaultLoot()
        .recipe((ctx, prov) -> {
          prov.stairs(
            DataIngredient.items(
              (ItemLike)BlockRegistry.BRICKS.get(BlockRegistry.fromName(color)).get(
                (prefix.isEmpty() ? "" : prefix + "_") + color + "_bricks"
            )),
            RecipeCategory.BUILDING_BLOCKS,
            ctx,
            CreateDecoMod.MOD_ID,
            true
          );
          recipeStonecuttingStair(finalName, color, prefix, ctx, prov);
        })
        .simpleItem()
      );
    }
    return ret;
  }

  public static ArrayList<BlockBuilder<SlabBlock,?>> buildSlab (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<SlabBlock, ?>> ret = new ArrayList<>();

    for (String prefix : TYPES) {
      if (color.isEmpty() && prefix.isEmpty()) continue;
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_brick_slab";

      if (color.contains("red") && prefix.isEmpty()) continue;

      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, SlabBlock::new)
        .initialProperties(() -> Blocks.BRICKS)
        .properties(props -> props
          .strength(2, 6)
          .requiresCorrectToolForDrops()
          .sound(SoundType.STONE)
        )
        .blockstate((ctx, prov) -> BlockStateGenerator.brickSlab(ctx, prov, color))
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .lang(
          CAPITALS.get(TYPES.indexOf(prefix))
            + color.substring(0, 1).toUpperCase()
            + color.substring(1)
            + " " + "Brick Slab"
        )
        .loot((table, block) -> {
          LootTable.Builder builder = LootTable.lootTable();
          LootPool.Builder pool = LootPool.lootPool();
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
        .recipe((ctx, prov) -> {
          prov.slab(
            DataIngredient.items(
              (ItemLike)BlockRegistry.BRICKS.get(BlockRegistry.fromName(color)).get(
                (prefix.isEmpty() ? "" : prefix + "_") + color + "_bricks"
              )),
            RecipeCategory.BUILDING_BLOCKS,
            ctx,
            CreateDecoMod.MOD_ID,
            true
          );
          recipeStonecuttingSlab(finalName, color, prefix, ctx, prov);
        })
        .simpleItem()
      );
    }
    return ret;
  }

  public static <T extends Block> void recipeCrafting (
      String color, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get(), 4)
        .pattern("bbb")
        .pattern("bab")
        .pattern("bbb")
        //.define('m', ingot.get())
        .define('b', Items.BRICK)
        .define('a', Bricks.BRICK_STONES.get(color).getBaseBlock().get())
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            Bricks.BRICK_STONES.get(color).getBaseBlock().get()
        ))
        .save(prov);
  }

  public static <T extends Block> void recipeCraftingMossy (
    String color, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    String original = color + "_bricks";
    DyeColor dye = BlockRegistry.fromName(color);
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ctx.get())
      .requires(DataIngredient.items( (dye == null)
        ? Blocks.BRICKS
        : BlockRegistry.BRICKS.get(dye).get(original)
      ))
      .requires(Blocks.VINE)
      .unlockedBy("hasitem", InventoryChangeTrigger.TriggerInstance.hasItems(
        (dye == null)
          ? Blocks.BRICKS
          : BlockRegistry.BRICKS.get(dye).get(original)
      ))
      .save(prov);
  }

  public static <T extends Block> void recipeSmeltingCracked (
    String color, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    String original = color + "_bricks";
    DyeColor dye = BlockRegistry.fromName(color);
    prov.smeltingAndBlasting(
      DataIngredient.items( (dye == null)
        ? Blocks.BRICKS
        : BlockRegistry.BRICKS.get(dye).get(original)
      ),
      RecipeCategory.BUILDING_BLOCKS,
      ctx,
      0.1f // this is the amount of XP smelting a block of stone gets you
    );
  }

  public static <T extends Block> void recipeStonecuttingBrick (
    String original, String color, String prefix,
    DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    recipeStonecutting(original, color, prefix, 1, 0, ctx, prov);
  }

  public static <T extends Block> void recipeStonecuttingStair (
    String original, String color, String prefix,
    DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    recipeStonecutting(original, color, prefix, 1, 1, ctx, prov);
  }

  public static <T extends Block> void recipeStonecuttingSlab (
    String original, String color, String prefix,
    DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    recipeStonecutting(original, color, prefix, 2, 2, ctx, prov);
  }

  private static <T extends Block> void recipeStonecuttingMossy (
    String original, String color, String prefix, int amount, int type,
    DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
//    if (!prefix.equals("mossy")) return;
//    if (type == 0) return;
//    DyeColor dye = BlockRegistry.fromName(color);
//    prov.stonecutting(
//      DataIngredient.items((ItemLike)BlockRegistry.BRICKS.get(dye).get(original)),
//      RecipeCategory.BUILDING_BLOCKS,
//      ctx, amount
//    );
  }

  private static <T extends Block> void recipeStonecuttingCracked (
    String original, String color, String prefix, int amount, int type,
    DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
//    if (!prefix.equals("cracked")) return;
//    if (type == 0) return;
//    DyeColor dye = BlockRegistry.fromName(color);
//    prov.stonecutting(DataIngredient.items(
//        (dye == null)
//          ? Blocks.BRICKS
//          : BlockRegistry.BRICKS.get(dye).get(original)
//      ),
//      RecipeCategory.BUILDING_BLOCKS,
//      ctx, amount
//    );
  }

  private static <T extends Block> void recipeStonecutting (
    String original, String color, String prefix, int amount, int type,
    DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    DyeColor dye = BlockRegistry.fromName(color);

    // don't allow crossover between mossy, cracked, and "normal"
    if (prefix.equals("mossy")) {
      recipeStonecuttingMossy("mossy_" + color + "_bricks", color, prefix, amount, type, ctx, prov);
      return;
    }

    if (prefix.equals("cracked")) {
      recipeStonecuttingCracked("cracked_" + color + "_bricks", color, prefix, amount, type, ctx, prov);
      return;
    }

    for (String pref : TYPES) {
      String fix = (pref.isEmpty() ? "" : pref + "_");
      String otherName = fix + color + "_bricks";
      // this line prevents bricks from stonecutting into themselves
      if (pref.equals(prefix)) continue;
      // this line prevents mossy / cracked variants from being stonecut
      if (pref.equals("mossy") || pref.equals("cracked")) continue;

      // this check handles the vanilla brick (which is not in our map)
      if (otherName.equals("red_bricks")) {
        prov.stonecutting(
          DataIngredient.items(Blocks.BRICKS),
          RecipeCategory.BUILDING_BLOCKS,
          ctx, amount
        );
        continue;
      } // else it's one of our dyed bricks
      prov.stonecutting(
        DataIngredient.items((ItemLike)BlockRegistry.BRICKS.get(dye).get(otherName)),
        RecipeCategory.BUILDING_BLOCKS,
        ctx, amount
      );
      if (type == 1) {
        String stairName = fix + color + "_brick_stairs";
        prov.stonecutting(
          DataIngredient.items((ItemLike)BlockRegistry.STAIRS.get(dye).get(stairName)),
          RecipeCategory.BUILDING_BLOCKS,
          ctx, amount
        );
      }
      if (type == 2) {
        String slabName = fix + color + "_brick_slab";
        prov.stonecutting(
          DataIngredient.items((ItemLike)BlockRegistry.SLABS.get(dye).get(slabName)),
          RecipeCategory.BUILDING_BLOCKS,
          ctx, 1
        );
      }
    }
  }
}
