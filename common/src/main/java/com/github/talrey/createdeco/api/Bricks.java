package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.*;

public class Bricks {
  public static List<String> TYPES = Arrays.asList(
    "", "short", "tiled", "long", "cracked", "mossy"
  );
  private static List<String> CAPITALS = Arrays.asList(
    "", "Short ", "Tiled ", "Long ", "Cracked ", "Mossy "
  );

  public static ArrayList<BlockBuilder<Block,?>> buildBlock (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<Block,?>> ret = new ArrayList<>();
    for (String prefix : TYPES) {
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_bricks";
      ret.add(reg.block(name, Block::new)

        .initialProperties(()-> Blocks.BRICKS)
        .properties(props -> props
          .strength(2,6)
          .requiresCorrectToolForDrops()
          .sound(SoundType.STONE)
        )
        .blockstate((ctx,prov)-> BlockStateGenerator.brick(ctx, prov, color))
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .lang(
          CAPITALS.get(TYPES.indexOf(prefix))
          + color.substring(0,1).toUpperCase()
          + color.substring(1)
          + " " + "Bricks"
        )
        .defaultLoot()
        .simpleItem()
      );
    }
    return ret;
  }

  public static ArrayList<BlockBuilder<StairBlock,?>> buildStair (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<StairBlock, ?>> ret = new ArrayList<>();

    for (String prefix : TYPES) {
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_brick_stairs";
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
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_brick_slab";
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
}
