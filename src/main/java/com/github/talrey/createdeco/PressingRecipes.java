package com.github.talrey.createdeco;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.press.PressingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

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
        ts -> ts.require(ItemTags.makeWrapperTag("forge:nuggets/" + metal.toLowerCase()))
          .withCondition(new ConfigCondition(Config.CAN_PRESS_COINS))
          .output(coin.get())
      )
    );
  }

  public PressingRecipes(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  public ProcessingRecipeBuilder<PressingRecipe> createBuilder(ResourceLocation id) {
    return new ProcessingRecipeBuilder<>(
    ((ProcessingRecipeSerializer<PressingRecipe>) AllRecipeTypes.PRESSING.serializer).getFactory(), id
    );
  }
}
