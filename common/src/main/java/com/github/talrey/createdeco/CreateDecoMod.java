package com.github.talrey.createdeco;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDecoMod {
  public static final String MOD_ID = "createdeco";
  public static final String NAME = "Create Deco";
  public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

  public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

  public static void init() {
    ItemRegistry.init();
    BlockRegistry.init();
  }

  public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
