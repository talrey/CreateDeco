package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.fan.SplashingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public class SplashingRecipes extends ProcessingRecipeWrapper<SplashingRecipe> {
  {
    add("worn_mossy_brick",
      ts -> ts.require(Registration.WORN_BRICK_TYPES.get("Worn Bricks").get())
        .output(Registration.WORN_BRICK_TYPES.get("Mossy Worn Bricks").asStack())
    );
    add("worn_mossy_long_brick",
      ts -> ts.require(Registration.WORN_BRICK_TYPES.get("Worn Long Bricks").get())
        .output(Registration.WORN_BRICK_TYPES.get("Mossy Worn Long Bricks").asStack())
    );
    add("worn_mossy_short_brick",
      ts -> ts.require(Registration.WORN_BRICK_TYPES.get("Worn Short Bricks").get())
        .output(Registration.WORN_BRICK_TYPES.get("Mossy Worn Short Bricks").asStack())
    );
    add("worn_mossy_brick_tile",
      ts -> ts.require(Registration.WORN_BRICK_TYPES.get("Worn Brick Tiles").get())
        .output(Registration.WORN_BRICK_TYPES.get("Mossy Worn Brick Tiles").asStack())
    );

    Registration.MOSSY_BRICK_BLOCK.forEach((color, entry) ->
      add(Registration.getBrickColorName(color).toLowerCase() + "_mossy_brick",
        ts -> ts.require(Registration.getBrickBlockFromColor(color)).output(entry.asStack())
      )
    );
    Registration.MOSSY_LONG_BLOCK.forEach((color, entry) ->
      add(Registration.getBrickColorName(color).toLowerCase() + "_mossy_long_brick",
        ts -> ts.require(Registration.LONG_BRICK_BLOCK.get(color).get()).output(entry.asStack())
      )
    );
    Registration.MOSSY_SHORT_BLOCK.forEach((color, entry) ->
      add(Registration.getBrickColorName(color).toLowerCase() + "_mossy_short_brick",
        ts -> ts.require(Registration.SHORT_BRICK_BLOCK.get(color).get()).output(entry.asStack())
      )
    );
    Registration.MOSSY_TILE_BLOCK.forEach((color, entry) ->
      add(Registration.getBrickColorName(color).toLowerCase() + "_mossy_tile_brick",
        ts -> ts.require(Registration.TILE_BRICK_BLOCK.get(color).get()).output(entry.asStack())
      )
    );
    for (DyeColor color : Registration.BRICK_BLOCK.keySet()) {
      Registration.BRICK_STAIRS_BLOCK.get(color).forEach( (name, brick) -> {
      //  HashMap<?,?> col = Registration.BRICK_STAIRS_BLOCK.get(color);
        RegistryEntry<?> blk = Registration.BRICK_STAIRS_BLOCK.get(color).get(name.substring(6));
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_stairs",
          ts -> ts.require(Registration.getBrickStairBlockFromColor(color, name.substring(6))).output(brick.asStack())
        );
      });
      Registration.BRICK_SLAB_BLOCK.get(color).forEach( (name, brick) -> {
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_slab",
          ts -> ts.require(Registration.getBrickSlabBlockFromColor(color, name.substring(6))).output(brick.asStack())
        );
      });
      Registration.BRICK_VERT_BLOCK.get(color).forEach( (name, brick) -> {
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_vert",
          ts -> ts.require(Registration.getBrickVertBlockFromColor(color, name.substring(6))).output(brick.asStack())
        );
      });
      Registration.BRICK_WALL_BLOCK.get(color).forEach( (name, brick) -> {
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_wall",
          ts -> ts.require(Registration.getBrickWallBlockFromColor(color, name.substring(6))).output(brick.asStack())
        );
      });
    }
  }

  public SplashingRecipes(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  public ProcessingRecipeBuilder<SplashingRecipe> createBuilder(ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
      ((ProcessingRecipeSerializer<SplashingRecipe>) AllRecipeTypes.SPLASHING.getSerializer()).getFactory(), id
    );
  }
}
