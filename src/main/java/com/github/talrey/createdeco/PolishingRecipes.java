package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.Locale;

public class PolishingRecipes extends ProcessingRecipeWrapper<SandPaperPolishingRecipe> {
  {
    add("iron_bars_from_polishing",
      ts -> ts.require(Items.IRON_BARS).output(Registration.BAR_BLOCKS.get("Iron".toLowerCase(Locale.ROOT)).get())
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
