package com.github.talrey.createdeco;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;

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
  protected void buildShapelessRecipes (Consumer<FinishedRecipe> consumer) {
    recipes.forEach(builder -> builder.build(consumer));
  }

  public abstract ProcessingRecipeBuilder<T> createBuilder (ResourceLocation id);

  public void add (String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
    ProcessingRecipeBuilder<T> builder = createBuilder(new ResourceLocation(name));
    transform.apply(builder);
    recipes.add(builder);
  }
}
