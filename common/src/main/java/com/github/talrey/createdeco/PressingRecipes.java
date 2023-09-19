package com.github.talrey.createdeco;

import com.github.talrey.createdeco.registry.CDTags;
import com.github.talrey.createdeco.registry.Props;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import java.util.Locale;

public class PressingRecipes extends ProcessingRecipeWrapper<PressingRecipe> {
  {
    add("zinc_sheet",
      ts -> ts.require(CDTags.of("zinc", "ingots").tag)
        .output(Registration.ZINC_SHEET.get())
    );
    add("netherite_sheet",
      ts -> ts.require(CDTags.of("netherite", "ingots").tag)
        .output(Registration.NETHERITE_SHEET.get())
    );
    add("cast_iron_sheet",
      ts -> ts.require(CDTags.of("cast_iron", "ingots").tag)
        .output(Registration.CAST_IRON_SHEET.get())
    );
    Props.COIN_ITEM.forEach((metal, coin) -> {
      String name = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
      add(name + "_coin",
        ts -> ts.require(CDTags.of(name, "nuggets").tag)
          .output(coin.get())
      );
    });
  }

  public PressingRecipes (FabricDataOutput output) {
    super(output);
  }

  @Override
  public ProcessingRecipeBuilder<PressingRecipe> createBuilder (ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
    ((ProcessingRecipeSerializer<PressingRecipe>) AllRecipeTypes.PRESSING.getSerializer()).getFactory(), id
    );
  }
}
