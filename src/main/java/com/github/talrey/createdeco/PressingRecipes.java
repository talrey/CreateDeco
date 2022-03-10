package com.github.talrey.createdeco;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.press.PressingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class PressingRecipes extends ProcessingRecipeWrapper<PressingRecipe> {
  {
    add("zinc_sheet",
      ts -> ts.require(AllItems.ZINC_INGOT.get()).output(Registration.ZINC_SHEET.get())
    );
    add("netherite_sheet",
      ts -> ts.require(Items.NETHERITE_INGOT).output(Registration.NETHERITE_SHEET.get())
    );
    Registration.COIN_ITEM.forEach((metal, coin) ->
      add(metal.toLowerCase() + "_coin",
        ts -> ts.require(TagFactory.ITEM.create(new ResourceLocation("c:nuggets/" + metal.toLowerCase())))
          .withCondition(new ConfigCondition(Config.CAN_PRESS_COINS))
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
