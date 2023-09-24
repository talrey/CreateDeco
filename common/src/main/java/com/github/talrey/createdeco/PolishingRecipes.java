package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;

import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class PolishingRecipes extends ProcessingRecipeWrapper<SandPaperPolishingRecipe> {
  {
    add("iron_bars_from_polishing",
      ts -> ts.require(Items.IRON_BARS).output(Registration.BAR_BLOCKS.get("iron").get())
    );
  }

  public PolishingRecipes(FabricDataOutput output) {
    super(output);
  }

  @Override
  public ProcessingRecipeBuilder<SandPaperPolishingRecipe> createBuilder(ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
      ((ProcessingRecipeSerializer<SandPaperPolishingRecipe>) AllRecipeTypes.SANDPAPER_POLISHING.getSerializer()).getFactory(), id
    );
  }
}
