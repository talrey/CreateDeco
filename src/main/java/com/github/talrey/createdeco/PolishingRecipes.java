package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.content.curiosities.tools.SandPaperPolishingRecipe;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class PolishingRecipes extends ProcessingRecipeWrapper<SandPaperPolishingRecipe> {
  {
    add("iron_bars_from_polishing",
      ts -> ts.require(Items.IRON_BARS).output(Registration.BAR_BLOCKS.get("Iron").get())
    );
  }
  public PolishingRecipes(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  public ProcessingRecipeBuilder<SandPaperPolishingRecipe> createBuilder(ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
      ((ProcessingRecipeSerializer<SandPaperPolishingRecipe>) AllRecipeTypes.SANDPAPER_POLISHING.getSerializer()).getFactory(), id
    );
  }
}
