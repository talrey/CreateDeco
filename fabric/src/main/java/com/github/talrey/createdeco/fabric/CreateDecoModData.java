package com.github.talrey.createdeco.fabric;

import com.github.talrey.createdeco.*;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreateDecoModData implements DataGeneratorEntrypoint {
  private static final List<ProcessingRecipeWrapper> GENERATORS = new ArrayList<>();

  @Override
  public void onInitializeDataGenerator (FabricDataGenerator gen) {
    Path existing = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
    //ExistingFileHelper helper = new ExistingFileHelper(
    //  Set.of(existing), Set.of(""), false, null, null
    //);

    ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
    FabricDataGenerator.Pack pack = gen.createPack();
    CreateDecoMod.createDecoRegistrar.setupDatagen(pack, helper);
    pack.addProvider(CreateDecoModData::registerAll);
  }

  public static DataProvider registerAll (FabricDataOutput output) {
    GENERATORS.add(new SplashingRecipes(output));
    GENERATORS.add(new PressingRecipes(output));
    GENERATORS.add(new PolishingRecipes(output));
    GENERATORS.add(new CompactingRecipes(output));

    return new DataProvider() {
      @Override
      public String getName() { return "Deco's Processing Recipes"; }
      @Override
      public CompletableFuture<?> run (CachedOutput co) {
        return CompletableFuture.allOf(GENERATORS.stream()
          .map(gen -> gen.run(co))
          .toArray(CompletableFuture[]::new)
        );
      }
    };
  }
}
