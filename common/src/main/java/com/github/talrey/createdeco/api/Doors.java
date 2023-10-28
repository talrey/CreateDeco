package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.CreateDecoMod;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Locale;
import java.util.function.Supplier;

public class Doors {
  public static final BlockSetType OPEN_METAL_DOOR = new BlockSetType(
    "metal", true, SoundType.METAL,
    SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
    SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
    SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON,
    SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
  );

  public static NonNullBiConsumer<DataGenContext<Block, DoorBlock>, RegistrateRecipeProvider> recipe (
    Supplier<Item> ingot
  ) {
    return (ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
      .pattern("mm")
      .pattern("mm")
      .pattern("mm")
      .define('m', ingot.get())
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance
        .hasItems(ingot.get())
      )
      .save(prov);
  }

  public static BlockBuilder<DoorBlock,?> build (
    CreateRegistrate reg, String metal, boolean locked
  ) {
    String regName = (locked ? "locked_" : "")
      + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")
      + "_door";

    return reg.block(regName, p -> new DoorBlock(p, locked ? BlockSetType.GOLD : OPEN_METAL_DOOR))
      .initialProperties(()-> Blocks.IRON_DOOR)
      .properties(props -> props.noOcclusion().strength(5, 5).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .blockstate((ctx, prov)-> BlockStateGenerator.door(reg, metal, locked, ctx, prov)
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
      .item()
      .model((ctx,prov)->BlockStateGenerator.doorItem(reg, metal, ctx, prov))
      .properties(props -> (metal.contains("Netherite") ? props.fireResistant() : props))
      .build();
  }

  public static NonNullBiConsumer<DataGenContext<Block, DoorBlock>, RegistrateRecipeProvider> lockedRecipe (
    Supplier<Item> originalDoor
  ) {
    return (ctx,prov)->ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
      .requires(Items.REDSTONE_TORCH, 1)
      .requires(originalDoor.get())
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
        originalDoor.get()
      ))
      .save(prov);
  }

  public static BlockBuilder<TrapDoorBlock,?> buildTrapdoor (
    CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")
      + "_trapdoor";
    String path = "block/palettes/doors/" + regName ;
    ResourceLocation texture = new ResourceLocation(CreateDecoMod.MOD_ID, path);

    return reg.block(regName, p->new TrapDoorBlock(p, OPEN_METAL_DOOR))
      .properties(props -> props.noOcclusion().strength(5, 5)
        .requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .blockstate((ctx, prov)-> prov.trapdoorBlock(ctx.get(), texture, true))
      .lang(metal + " Trapdoor")
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(BlockTags.TRAPDOORS)
      .addLayer(()-> RenderType::cutoutMipped)
      .item()
      .model((ctx,prov)->BlockStateGenerator.trapdoorItem(reg, metal, ctx, prov))
      .build();
  }

  public static NonNullBiConsumer<DataGenContext<Block, TrapDoorBlock>, RegistrateRecipeProvider> trapdoorRecipe (
    Supplier<Item> ingot
  ) {
    return (ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
      .pattern("mm")
      .pattern("mm")
      .define('m', ingot.get())
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
        ingot.get()
      ))
      .save(prov);
  }
}
