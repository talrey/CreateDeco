package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.blocks.SupportWedgeBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Supplier;

public class Wedges {
  public static BlockBuilder<SupportWedgeBlock,?> build (
    CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_support_wedge";

    return reg.block(regName, SupportWedgeBlock::new)
      .properties(props -> props.strength(5, (metal.contains("Netherite")) ? 1200 : 6)
        .requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
        .noOcclusion()
        .isViewBlocking((a, b, c) -> false)
        .isSuffocating((a, b, c) -> false)
      )
      .addLayer(() -> RenderType::translucent)
      .item()
      .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .blockstate((ctx, prov) -> BlockStateGenerator.supportWedge(reg, metal, ctx, prov))
      .lang(metal + " Support Wedge");
  }

  public static <T extends Block> void recipe (
          String metal,
          DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
        .pattern(" p")
        .pattern("pp")
        .define('p', CDTags.of(metal, "plates").tag)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
        ))
        .save(prov);

  }
}
