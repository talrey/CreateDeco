package com.github.talrey.createdeco.registry;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.blocks.VerticalSlabBlock;
import com.github.talrey.createdeco.connected.SheetMetalCTBehaviour;
import com.github.talrey.createdeco.connected.SheetMetalSlabCTBehaviour;
import com.github.talrey.createdeco.connected.SheetMetalVertCTBehaviour;
import com.github.talrey.createdeco.connected.SpriteShifts;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashMap;
import java.util.Locale;

public class SheetMetal {
  public static HashMap<String, BlockEntry<Block>> SHEET_METAL_BLOCKS           = new HashMap<>();
  public static HashMap<String, BlockEntry<StairBlock>> SHEET_STAIRS            = new HashMap<>();
  public static HashMap<String, BlockEntry<SlabBlock>> SHEET_SLABS              = new HashMap<>();
  public static HashMap<String, BlockEntry<VerticalSlabBlock>> SHEET_VERT_SLABS = new HashMap<>();

  static HashMap<String, NonNullSupplier<Item>> METAL_LOOKUP = new HashMap<>();

  private static void initialize () {
    METAL_LOOKUP.put("Andesite",  ()->AllBlocks.ANDESITE_CASING.get().asItem());
    METAL_LOOKUP.put("Zinc",      ()->AllBlocks.ZINC_BLOCK.get().asItem());
    METAL_LOOKUP.put("Copper",    ()->Items.COPPER_BLOCK);
    METAL_LOOKUP.put("Brass",     ()->AllBlocks.BRASS_BLOCK.get().asItem());
    METAL_LOOKUP.put("Iron",      ()->Items.IRON_BLOCK);
    METAL_LOOKUP.put("Gold",      ()->Items.GOLD_BLOCK);
    METAL_LOOKUP.put("Netherite", ()->Items.NETHERITE_BLOCK);
    METAL_LOOKUP.put("Cast Iron", ()->Registration.CAST_IRON_BLOCK.get().asItem());
  }

  public static BlockBuilder<Block,?> buildSheetMetalBlock (Registrate reg, NonNullSupplier<Item> material, String name, ResourceLocation texture) {
    return reg.block(name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal", Block::new)
      .properties(props-> props.strength(5, (name.contains("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .blockstate((ctx,prov)-> prov.simpleBlock(ctx.get(), prov.models().cubeAll(ctx.getName(), texture)))
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(name + " Sheet Metal")
      .defaultLoot()
      .item()
        .properties(p -> (name.contains("Netherite")) ? p.fireResistant() : p)
        .build()
      .recipe((ctx, prov)-> {
        prov.stonecutting(
          DataIngredient.items(material.get()),
          RecipeCategory.BUILDING_BLOCKS, ctx, 4
        );
      })
      .onRegister(CreateRegistrate.connectedTextures(
        new SheetMetalCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(name)).getSupplier()
      ));
  }

  public static BlockBuilder<StairBlock,?> buildSheetMetalStair (Registrate reg, NonNullSupplier<Item> material, String name, ResourceLocation texture) {
    return reg.block(name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_stairs",
    (props)->new StairBlock(Blocks.BRICK_STAIRS.defaultBlockState(), props))
      .properties(props-> props.strength(5, (name.contains("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .item()
      .properties(p -> (name.contains("Netherite")) ? p.fireResistant() : p)
      .build()
      .tag(BlockTags.STAIRS)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .blockstate((ctx,prov)-> prov.stairsBlock(ctx.get(), texture))
      .lang(name + " Sheet Stairs")
      .recipe((ctx, prov)-> {
        prov.stonecutting(
          DataIngredient.items(material.get()), RecipeCategory.BUILDING_BLOCKS, ctx
        );
        prov.stairs(
          DataIngredient.items(material.get()),
          RecipeCategory.BUILDING_BLOCKS, ctx, null, false
        );
      })
      .onRegister(CreateRegistrate.connectedTextures(
        new SheetMetalCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(name)).getSupplier()
      ));
  }

  public static BlockBuilder<SlabBlock,?> buildSheetMetalSlab (Registrate reg, NonNullSupplier<Item> material, String name, ResourceLocation texture) {
    return reg.block(name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_slab", SlabBlock::new)
      .properties(props-> props.strength(5, (name.contains("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .item()
      .properties(p -> (name.contains("Netherite")) ? p.fireResistant() : p)
      .build()
      .tag(BlockTags.SLABS)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .blockstate((ctx,prov)-> prov.slabBlock(ctx.get(), prov.modLoc("block/" + name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal"), texture))
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
      .lang(name + " Sheet Slab")
      .recipe((ctx, prov)-> {
        prov.stonecutting(DataIngredient.items(material.get()), RecipeCategory.BUILDING_BLOCKS, ctx, 2);
        prov.slab(
          DataIngredient.items(material.get()),
          RecipeCategory.BUILDING_BLOCKS, ctx, null, false
        );
      })
      .onRegister(CreateRegistrate.connectedTextures(
        new SheetMetalSlabCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(name)).getSupplier()
      ));
  }

  public static BlockBuilder<VerticalSlabBlock,?> buildSheetMetalVert (Registrate reg, NonNullSupplier<Item> material, String name, ResourceLocation texture) {
    return reg.block(name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_slab_vert", VerticalSlabBlock::new)
      .properties(props-> props.strength(5, (name.contains("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .item()
      .properties(p -> (name.contains("Netherite")) ? p.fireResistant() : p)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .blockstate((ctx,prov)-> PerLoaderRegistration.sheetMetalVertBlockState(texture, ctx, prov))
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
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get(), 3)
          .pattern("s")
          .pattern("s")
          .pattern("s")
          .define('s', SHEET_SLABS.get(name).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(material.get()))
          .save(prov);
        prov.stonecutting(
          DataIngredient.items(material.get()),
          RecipeCategory.BUILDING_BLOCKS, ctx, 2
        );
      })
      .onRegister(CreateRegistrate.connectedTextures(
        new SheetMetalVertCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(name)).getSupplier()
      ));
  }

  public static void registerBlocks (Registrate reg) {
    reg.defaultCreativeTab(DecoCreativeModeTab.METALS_GROUP, DecoCreativeModeTab.METALS_KEY);
    initialize();

    Registration.METAL_TYPES.forEach((metal, getter)-> {
      ResourceLocation texture = new ResourceLocation(
        CreateDecoMod.MODID, "block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal"
      );

      SHEET_METAL_BLOCKS.put(metal, buildSheetMetalBlock(reg, METAL_LOOKUP.get(metal), metal, texture).register());
      SHEET_STAIRS.put(metal, buildSheetMetalStair(reg, ()->SHEET_METAL_BLOCKS.get(metal).get().asItem(), metal, texture).register());
      SHEET_SLABS.put(metal, buildSheetMetalSlab(reg, ()->SHEET_METAL_BLOCKS.get(metal).get().asItem(), metal, texture).register());
      SHEET_VERT_SLABS.put(metal, buildSheetMetalVert(reg, ()->SHEET_METAL_BLOCKS.get(metal).get().asItem(), metal, texture).register());
    });
  }
}
