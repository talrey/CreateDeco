package com.github.talrey.createdeco;

import com.tterrag.registrate.Registrate;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("createdeco")
public class CreateDecoMod
{
  // Directly reference a log4j logger.
  private static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "createdeco";
  public static Registrate createDecoRegistrar;
  private static Registration registration;

  private static ProcessingRecipeWrapper SPLASHING, PRESSING, POLISHING, COMPACTING;

  public CreateDecoMod() {
    // register configuration settings handler
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONF);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONF);

    MinecraftForge.EVENT_BUS.register(this);

    createDecoRegistrar = Registrate.create(MODID);

    registration = new Registration();
    registration.registerItems(createDecoRegistrar);
    registration.registerBlocks(createDecoRegistrar);
    // MovementCheckHandler.register();
  }

  @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
        @SubscribeEvent
    public static void gatherData (GatherDataEvent gde) {
      DataGenerator gen = gde.getGenerator();
      SPLASHING = new SplashingRecipes(gen);
      gen.addProvider(true, SPLASHING);
      PRESSING  = new PressingRecipes(gen);
      gen.addProvider(true, PRESSING);
      POLISHING = new PolishingRecipes(gen);
      gen.addProvider(true, POLISHING);
      COMPACTING = new CompactingRecipes(gen);
      gen.addProvider(true, COMPACTING);
    }
  }
}
