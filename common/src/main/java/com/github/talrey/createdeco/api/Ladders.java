package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.simibubi.create.content.decoration.MetalLadderBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;
import java.util.function.Supplier;

public class Ladders {
  public static BlockBuilder<MetalLadderBlock,?> build (
      CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");


    return reg.block(regName + "_ladder", MetalLadderBlock::new)
        .initialProperties(() -> Blocks.LADDER)
        .addLayer(() -> RenderType::cutout)
        .blockstate((ctx, prov)-> BlockStateGenerator.ladder(ctx,prov,regName)
        )
        .properties(p -> p.sound(SoundType.COPPER))
        .tag(BlockTags.CLIMBABLE)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .lang(metal + " Ladder")
        .item()
        .model((ctx, prov) -> prov.blockSprite(ctx::get, prov.modLoc("block/palettes/ladders/ladder_" + regName)))
        //.model((c, p) -> p.blockSprite(c::get, p.modLoc("block/ladder_" + regName)))
        .build();
  }


  public static <T extends Block> void recipeStonecutting (
      Supplier<Item> ingot, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingot.get()), RecipeCategory.DECORATIONS, ctx.get(), 2)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(ingot.get()).build()
        ))
        .save(prov, ctx.getName() + "_from_stonecutting");

  }
}
