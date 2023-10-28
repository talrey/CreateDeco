package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.blocks.HullBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;
import java.util.function.Supplier;

public class Hulls {
  public static BlockBuilder<HullBlock,?> build (
    CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_hull";

    return reg.block(regName, HullBlock::new)
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
      .tag(BlockTags.STAIRS)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .blockstate((ctx,prov)-> BlockStateGenerator.hull(reg, metal, ctx, prov))
      .lang(metal + " Train Hull");
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> recipe (
    Supplier<Item> ingot
  ) {
    return (ctx, prov)-> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 2)
      .pattern(" m ")
      .pattern("m m")
      .pattern(" m ")
      .define('m', ingot.get())
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ingot.get()))
      .save(prov);
  }
}
