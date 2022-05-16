package com.github.talrey.createdeco;

import com.tterrag.registrate.Registrate;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
public class CreateDecoMod implements ModInitializer
{
  // Directly reference a log4j logger.
  private static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "createdeco";
  public static Registrate createDecoRegistrar;
  private static Registration registration;

  private static ProcessingRecipeWrapper SPLASHING, PRESSING, POLISHING, COMPACTING;

  @Override
  public void onInitialize() {
    // register configuration settings handler
    ModLoadingContext.registerConfig(MODID, ModConfig.Type.CLIENT, Config.CLIENT_CONF);
    ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, Config.COMMON_CONF);

    // Register the setup method for modloading
    //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the enqueueIMC method for modloading
    //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
    // Register the processIMC method for modloading
    //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
    // Register the doClientStuff method for modloading
    //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

    // Register ourselves for server and other game events we are interested in

    createDecoRegistrar = Registrate.create(MODID);

    registration = new Registration();
    registration.registerItems(createDecoRegistrar);
    registration.registerBlocks(createDecoRegistrar);
    MovementCheckHandler.register();

    RegistryEvents.onRecipeSerializerRegistry();

    createDecoRegistrar.register();
  }
/*
  private void setup(final FMLCommonSetupEvent event)
  {

  }

  private void doClientStuff(final FMLClientSetupEvent event) {

  }

  private void enqueueIMC(final InterModEnqueueEvent event)
  {
    // some example code to dispatch IMC to another mod
    //InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
  }

  private void processIMC(final InterModProcessEvent event)
  {
    // some example code to receive and process InterModComms from other mods
    //LOGGER.info("Got IMC {}", event.getIMCStream().
    //        map(m->m.getMessageSupplier().get()).
    //        collect(Collectors.toList()));
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent event) {
    // do something when the server starts
    //LOGGER.info("HELLO from server starting");
  }

  public static <T extends Block> NonNullConsumer<? super T> connectedTextures (ConnectedTextureBehaviour behaviour) {
    return entry -> onClient(() -> () -> registerCTBehaviour(entry, behaviour));
  }

  @OnlyIn(Dist.CLIENT)
  private static void registerCTBehaviour (Block entry, ConnectedTextureBehaviour behaviour) {
    CreateClient.getCustomBlockModels().register(entry.delegate, model -> new CTModel(model, behaviour));
  }

  protected static void onClient (Supplier<Runnable> toRun) {
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, toRun);
  }
*/
  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  public static class RegistryEvents {
    public static void onRecipeSerializerRegistry() {
      ConfigCondition.registerConditon();
      COMPACTING = new CompactingRecipes(gen);
      gen.addProvider(COMPACTING);
    }
  }
}
