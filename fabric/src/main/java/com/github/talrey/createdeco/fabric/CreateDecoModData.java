package com.github.talrey.createdeco.fabric;

import com.github.talrey.createdeco.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class CreateDecoModData implements DataGeneratorEntrypoint {

    private static ProcessingRecipeWrapper SPLASHING, PRESSING, POLISHING, COMPACTING;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        Path existing = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
        ExistingFileHelper helper = new ExistingFileHelper(
          Set.of(existing), Set.of(""), false, null, null
        );
        CreateDecoMod.createDecoRegistrar.setupDatagen(gen, helper);
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
