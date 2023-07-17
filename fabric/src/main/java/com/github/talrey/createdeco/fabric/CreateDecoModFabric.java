package com.github.talrey.createdeco.fabric;

import com.github.talrey.createdeco.CreateDecoMod;
import net.fabricmc.api.ModInitializer;

public class CreateDecoModFabric implements ModInitializer {
  @Override
  public void onInitialize () {
    CreateDecoMod.init();
    CreateDecoMod.createDecoRegistrar.register();
  }
}
