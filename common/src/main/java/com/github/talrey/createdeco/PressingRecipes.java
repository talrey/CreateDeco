package com.github.talrey.createdeco;

import com.github.talrey.createdeco.registry.Props;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.Locale;

public class PressingRecipes extends ProcessingRecipeWrapper<PressingRecipe> {
  {
    add("zinc_sheet",
      ts -> ts.require(AllItems.ZINC_INGOT.get()).output(Registration.ZINC_SHEET.get())
    );
    add("netherite_sheet",
      ts -> ts.require(Items.NETHERITE_INGOT).output(Registration.NETHERITE_SHEET.get())
    );
    add("cast_iron_sheet",
      ts -> ts.require(Registration.CAST_IRON_INGOT.get()).output(Registration.CAST_IRON_SHEET.get())
    );
    Props.COIN_ITEM.forEach((metal, coin) ->
      add(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_coin",
        ts -> ts.require(Registration.makeItemTag(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_nuggets"))
        //  .withCondition(new ConfigCondition(Config.CAN_PRESS_COINS))
          .output(coin.get())
      )
    );
  }

  public PressingRecipes(FabricDataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  public ProcessingRecipeBuilder<PressingRecipe> createBuilder(ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
    ((ProcessingRecipeSerializer<PressingRecipe>) AllRecipeTypes.PRESSING.getSerializer()).getFactory(), id
    );
  }
}
