package com.github.talrey.createdeco;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.fan.SplashingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public abstract class ProcessingRecipeWrapper<T extends ProcessingRecipe<?>> extends RecipeProvider {
  public List<ProcessingRecipeBuilder<T>> recipes = new ArrayList<>();

  public ProcessingRecipeWrapper(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    recipes.forEach(builder -> builder.build(consumer));
  }

  public abstract ProcessingRecipeBuilder<T> createBuilder (ResourceLocation id);

  public void add (String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
    ProcessingRecipeBuilder<T> builder = createBuilder(new ResourceLocation(name));
    transform.apply(builder);
    recipes.add(builder);
  }
}
