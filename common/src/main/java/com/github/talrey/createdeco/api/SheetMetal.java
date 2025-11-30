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
  public static BlockBuilder<ConnectedPillarBlock,?> buildBlock (
      CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";

    return reg.block(regName, ConnectedPillarBlock::new)
        .properties(props-> props.strength(5, 6)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
        .item()
        .build()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate((ctx,prov)-> BlockStateGenerator.sheetMetal(metal, ctx, prov))
        .lang(metal + " Sheet Metal")

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
        .define('m', CreateDecoTags.plate(metal))
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CreateDecoTags.plate(metal)).build()
        ))
        .save(prov);
  }

/*
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
                    (ItemLike) BlockRegistry.BRICKS.get(BlockRegistry.fromName(color)).get(
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

 */
}
