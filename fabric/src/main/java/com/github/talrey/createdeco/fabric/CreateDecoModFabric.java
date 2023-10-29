package com.github.talrey.createdeco.fabric;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import com.github.talrey.createdeco.CreateDecoMod;
import net.fabricmc.api.ModInitializer;

public class CreateDecoModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CreativeTabsImpl.register();
        CreateDecoMod.init();
        CreateDecoMod.LOGGER.info(EnvExecutor.unsafeRunForDist(
                () -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"
                ), CreateDecoMod.NAME);
        // on fabric, Registrates must be explicitly finalized and registered.
        CreateDecoMod.REGISTRATE.register();
    }
}
