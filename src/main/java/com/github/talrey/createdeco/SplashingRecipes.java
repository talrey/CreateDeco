package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.fan.SplashingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;

public class SplashingRecipes extends ProcessingRecipeWrapper<SplashingRecipe> {
  {
    Registration.MOSSY_BRICK_BLOCK.forEach((color, entry) ->
      add(Registration.getBrickColorName(color).toLowerCase() + "_mossy_brick",
        ts -> ts.require(Registration.BRICK_BLOCK.get(color).get()).output(entry.asStack())
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
  }

  public SplashingRecipes(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  public ProcessingRecipeBuilder<SplashingRecipe> createBuilder(ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
      ((ProcessingRecipeSerializer<SplashingRecipe>) AllRecipeTypes.SPLASHING.serializer).getFactory(), id
    );
  }
}
