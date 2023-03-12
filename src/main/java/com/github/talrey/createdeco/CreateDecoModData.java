package com.github.talrey.createdeco;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class CreateDecoModData implements DataGeneratorEntrypoint {

    private static ProcessingRecipeWrapper SPLASHING, PRESSING, POLISHING, COMPACTING;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        var existingData = System.getProperty("com.github.talrey.createdeco.existingData").split(";");
        var existingFileHelper = new ExistingFileHelper(Arrays.stream(existingData).map(Paths::get).toList(), Collections.emptySet(),
                true, null, null);
        CreateDecoMod.createDecoRegistrar.setupDatagen(gen, existingFileHelper);
        SPLASHING = new SplashingRecipes(gen);
        gen.addProvider(SPLASHING);
        PRESSING  = new PressingRecipes(gen);
        gen.addProvider(PRESSING);
        POLISHING = new PolishingRecipes(gen);
        gen.addProvider(POLISHING);
        COMPACTING = new CompactingRecipes(gen);
        gen.addProvider(COMPACTING);
    }
}
