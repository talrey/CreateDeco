package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.simibubi.create.repack.registrate.Registrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("createdeco")
public class CreateDecoMod
{
  // Directly reference a log4j logger.
  private static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "createdeco";
  public static Registrate createDecoRegistrar;
  private static Registration registration;

  private static ProcessingRecipeWrapper SPLASHING, PRESSING, POLISHING;

  public CreateDecoMod() {
    // register configuration settings handler
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONF);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONF);

    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the enqueueIMC method for modloading
    //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
    // Register the processIMC method for modloading
    //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
    // Register the doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

    // Register ourselves for server and other game events we are interested in
    MinecraftForge.EVENT_BUS.register(this);

    createDecoRegistrar = Registrate.create(MODID);

    registration = new Registration();
    registration.registerItems(createDecoRegistrar);
    registration.registerBlocks(createDecoRegistrar);
  }

  private void setup(final FMLCommonSetupEvent event)
  {

  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    for (BlockEntry<CoinStackBlock> entry : Registration.COIN_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
    for (BlockEntry<?> entry : Registration.DOOR_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
    for (BlockEntry<?> entry : Registration.BAR_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
    for (BlockEntry<?> entry : Registration.BAR_PANEL_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
    for (BlockEntry<?> entry : Registration.DECAL_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
    for (BlockEntry<?> entry : Registration.LOCK_DOOR_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
    for (BlockEntry<?> entry : Registration.MESH_FENCE_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
    for (BlockEntry<?> entry : Registration.CATWALK_BLOCKS.values()) {
      RenderTypeLookup.setRenderLayer(entry.get(), RenderType.getCutoutMipped());
    }
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
/*
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
  @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void onRecipeSerializerRegistry(RegistryEvent.Register<IRecipeSerializer<?>> event) {
      CraftingHelper.register(ConfigCondition.Serializer.INSTANCE);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
      // register a new block here
      //  LOGGER.info("HELLO from Register Block");
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent gde) {
      //  LOGGER.info("gather-data-event triggered");
      DataGenerator gen = gde.getGenerator();
      SPLASHING = new SplashingRecipes(gen);
      gen.addProvider(SPLASHING);
      PRESSING  = new PressingRecipes(gen);
      gen.addProvider(PRESSING);
      POLISHING = new PolishingRecipes(gen);
      gen.addProvider(POLISHING);
    }
  }
}
