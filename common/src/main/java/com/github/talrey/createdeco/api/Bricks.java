package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.CreateDecoMod;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.*;
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

  public static HashMap<String, Block> SOURCE_BRICK = new HashMap<>() {{
    put("blue", BlockRegistry.BRICKS.get(DyeColor.BLUE).get("blue").get());
    put("dean", BlockRegistry.BRICKS.get(DyeColor.YELLOW).get("dean").get());
    put("dusk", BlockRegistry.BRICKS.get(DyeColor.BLACK).get("dusk").get());
    put("pearl", BlockRegistry.BRICKS.get(DyeColor.WHITE).get("pearl").get());
    put("scarlet", BlockRegistry.BRICKS.get(DyeColor.RED).get("scarlet").get());
    put("verdant", BlockRegistry.BRICKS.get(DyeColor.GREEN).get("verdant").get());
    put("umber", BlockRegistry.BRICKS.get(DyeColor.BROWN).get("umber").get());
  }};

  public static ArrayList<BlockBuilder<Block,?>> buildBlock (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<Block,?>> ret = new ArrayList<>();
    for (String prefix : TYPES) {
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_bricks";

      if (color.contains("red") && prefix.isEmpty()) continue;
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
          recipeVariants(color, prefix, ctx, prov);
          //if (prefix.contains("mossy") || prefix.contains("cracked")) recipeCrackedAndMossy(color, prefix, ctx, prov);
            //else if (!prefix.contains("mossy") || !prefix.contains("cracked")) recipeStonecutting(color, prefix, ctx, prov);
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
      ret.add(reg.block(name, p-> new StairBlock(Blocks.BRICK_STAIRS.defaultBlockState(), p))
          .initialProperties(()-> Blocks.BRICKS)
          .properties(props -> props
              .strength(2,6)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
          )
          .blockstate((ctx,prov)-> BlockStateGenerator.brickStair(ctx, prov, color))
          .tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .lang(
              CAPITALS.get(TYPES.indexOf(prefix))
                  + color.substring(0,1).toUpperCase()
                  + color.substring(1)
                  + " " + "Brick Stairs"
          )
          .defaultLoot()
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
      ret.add(reg.block(name, SlabBlock::new)
          .initialProperties(()-> Blocks.BRICKS)
          .properties(props -> props
              .strength(2,6)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
          )
          .blockstate((ctx,prov)-> BlockStateGenerator.brickSlab(ctx, prov, color))
          .tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .lang(
              CAPITALS.get(TYPES.indexOf(prefix))
                  + color.substring(0,1).toUpperCase()
                  + color.substring(1)
                  + " " + "Brick Slab"
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
          .simpleItem()
      );
    }
    return ret;
  }


  public static <T extends Block> void recipeCrafting (
      String color, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
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

  public static <T extends Block> void recipeVariants (
      String color, String type, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    if (type.isEmpty() && !color.contains("red")) {
      SingleItemRecipeBuilder.stonecutting(Ingredient.of(Bricks.BRICK_STONES.get(color).getBaseBlock().get()), RecipeCategory.BUILDING_BLOCKS, ctx.get(), 2)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
              ItemPredicate.Builder.item().of(Bricks.BRICK_STONES.get(color).getBaseBlock().get()).build()
          ))
          .save(prov, ctx.getName() + "_from_stonecutting_raw");
    }
    if (type.contains("cracked")) {
      SimpleCookingRecipeBuilder.smelting(Ingredient.of(SOURCE_BRICK.get(color)), RecipeCategory.BUILDING_BLOCKS, ctx.get(), 0.5f, 200)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
              ItemPredicate.Builder.item().of(Bricks.BRICK_STONES.get(color).getBaseBlock().get()).build()
          ))
          .save(prov, ctx.getName());
    }
    if (type.contains("mossy")) {
      ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
          .requires(Items.MOSS_BLOCK)
          .requires(SOURCE_BRICK.get(color))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
              ItemPredicate.Builder.item().of(SOURCE_BRICK.get(color)).build()
          ))
          .save(prov, ctx.getName() + "_from_moss");
      ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
          .requires(Items.VINE)
          .requires(SOURCE_BRICK.get(color))
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
              ItemPredicate.Builder.item().of(SOURCE_BRICK.get(color)).build()
          ))
          .save(prov, ctx.getName() + "_from_vines");
    }

  }


  public static <T extends Block> void recipeStonecuttingFromBrick (
      String color, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    SingleItemRecipeBuilder.stonecutting(Ingredient.of(ctx.get()), RecipeCategory.BUILDING_BLOCKS, ctx.get(), 1)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(AllPaletteStoneTypes.VERIDIUM.getBaseBlock().get()).build()
        ))
        .save(prov, ctx.getName() + color.toLowerCase().replaceAll(" ", "_") + "_bricks_from_stonecutting");
  }
}
