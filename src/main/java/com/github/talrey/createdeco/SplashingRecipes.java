package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.fan.SplashingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

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
    for (DyeColor color : Registration.BRICK_BLOCK.keySet()) {
      Registration.BRICK_STAIRS_BLOCK.get(color).forEach( (name, brick) -> {
        HashMap<?,?> col = Registration.BRICK_STAIRS_BLOCK.get(color);
        RegistryEntry<?> blk = Registration.BRICK_STAIRS_BLOCK.get(color).get(name.substring(6));
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_stairs",
          ts -> ts.require(Registration.BRICK_STAIRS_BLOCK.get(color).get(name.substring(6)).get()).output(brick.asStack())
        );
      });
      Registration.BRICK_SLAB_BLOCK.get(color).forEach( (name, brick) -> {
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_slab",
          ts -> ts.require(Registration.BRICK_SLAB_BLOCK.get(color).get(name.substring(6)).get()).output(brick.asStack())
        );
      });
      Registration.BRICK_VERT_BLOCK.get(color).forEach( (name, brick) -> {
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_vert",
          ts -> ts.require(Registration.BRICK_VERT_BLOCK.get(color).get(name.substring(6)).get()).output(brick.asStack())
        );
      });
      Registration.BRICK_WALL_BLOCK.get(color).forEach( (name, brick) -> {
        if (name.contains("Mossy")) add(name.toLowerCase().replaceAll(" ", "_") + "_wall",
          ts -> ts.require(Registration.BRICK_WALL_BLOCK.get(color).get(name.substring(6)).get()).output(brick.asStack())
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
      ((ProcessingRecipeSerializer<SplashingRecipe>) AllRecipeTypes.SPLASHING.serializer).getFactory(), id
    );
  }
}
