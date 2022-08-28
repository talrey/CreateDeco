package com.github.talrey.createdeco.registry;

import com.github.talrey.createdeco.blocks.VerticalSlabBlock;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockModelBuilder;

import java.util.Locale;

public class BrickBuilders {
  public static BlockBuilder<Block,?> buildBrick (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
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

  public static BlockBuilder<StairBlock,?> buildBrickStairs (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
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

  public static BlockBuilder<SlabBlock,?> buildBrickSlabs (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
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

  public static BlockBuilder<VerticalSlabBlock,?> buildBrickVerts (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
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

  public static BlockBuilder<WallBlock,?> buildBrickWalls (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
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
}
