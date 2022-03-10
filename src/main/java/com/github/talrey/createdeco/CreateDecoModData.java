package com.github.talrey.createdeco;

import com.simibubi.create.Create;
import com.simibubi.create.CreateData;
import com.tterrag.registrate.fabric.GatherDataEvent;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class CreateDecoModData implements DataGeneratorEntrypoint {

    private static ProcessingRecipeWrapper SPLASHING, PRESSING, POLISHING;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        var existingData = System.getProperty("om.github.talrey.createdeco.existingData").split(";");
        var existingFileHelper = new ExistingFileHelper(Arrays.stream(existingData).map(Paths::get).toList(), Collections.emptySet(),
                true, null, null);
        GatherDataEvent.EVENT.invoker().gatherData(gen, existingFileHelper);
        SPLASHING = new SplashingRecipes(gen);
        gen.addProvider(SPLASHING);
        PRESSING  = new PressingRecipes(gen);
        gen.addProvider(PRESSING);
        POLISHING = new PolishingRecipes(gen);
        gen.addProvider(POLISHING);
    }
}
