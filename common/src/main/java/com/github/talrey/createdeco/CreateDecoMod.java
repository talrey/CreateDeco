package com.github.talrey.createdeco;

import com.github.talrey.createdeco.registry.DecoCreativeModeTab;
import com.tterrag.registrate.Registrate;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateDecoMod {
  private static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "createdeco";
  public static Registrate createDecoRegistrar;
  private static Registration registration;

  private static ProcessingRecipeWrapper SPLASHING, PRESSING, POLISHING, COMPACTING;

  public static void init () {
    // register configuration settings handler
    //ModLoadingContext.registerConfig(MODID, ModConfig.Type.CLIENT, Config.CLIENT_CONF);
    //ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, Config.COMMON_CONF);

    createDecoRegistrar = Registrate.create(MODID);
    DecoCreativeModeTab.build(); // prepare the item groups, which are built differently per loader
    registration = new Registration();
    registration.registerItems(createDecoRegistrar);
    registration.registerBlocks(createDecoRegistrar);
    MovementCheckHandler.register();

    RegistryEvents.onRecipeSerializerRegistry();
  }

  public static class RegistryEvents {
    public static void onRecipeSerializerRegistry() {
      //ConfigCondition.registerConditon();
    }
  }
}
