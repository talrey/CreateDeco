package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.connected.SpriteShifts;
import com.simibubi.create.content.decoration.palettes.ConnectedPillarBlock;
import com.simibubi.create.foundation.block.connected.RotatedPillarCTBehaviour;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class SheetMetal {
  public static BlockBuilder<ConnectedPillarBlock,?> build (
      CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";

    return reg.block(regName, ConnectedPillarBlock::new)
        .properties(props-> props.strength(5, (metal.contains("Netherite")) ? 1200 : 6)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
            .noOcclusion()
            .isViewBlocking((a,b,c)->false)
        )
        .addLayer(() -> RenderType::cutoutMipped)
        .item()
        .build()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate((ctx,prov)-> BlockStateGenerator.sheetMetal(metal, ctx, prov))
        .lang(metal + " Sheet Metal")

        //.connectedTextures(v -> new RotatedPillarCTBehaviour(ct(v, PaletteBlockPattern.CTs.PILLAR), ct(v, PaletteBlockPattern.CTs.CAP)))
        .onRegister(CreateRegistrate.connectedTextures(() ->
            new RotatedPillarCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal), null)
        ));
  }

  public static <T extends Block> void recipeCrafting (
      String metal, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
        .pattern("mm")
        .pattern("mm")
        .define('m', CDTags.of(metal, "plates").tag)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
        ))
        .save(prov);
  }

}
