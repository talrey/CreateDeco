package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.mixer.CompactingRecipe;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class CompactingRecipes extends ProcessingRecipeWrapper<CompactingRecipe> {
  {
    add("cast_iron_ingot",
      ts -> ts.require(Items.IRON_INGOT).requiresHeat(HeatCondition.HEATED).output(Registration.CAST_IRON_INGOT.get())
    );
  }

  public CompactingRecipes(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  public ProcessingRecipeBuilder<CompactingRecipe> createBuilder(ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
      ((ProcessingRecipeSerializer<CompactingRecipe>) AllRecipeTypes.COMPACTING.getSerializer()).getFactory(), id
    );
  }
}
