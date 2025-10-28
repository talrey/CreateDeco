package com.github.talrey.createdeco.events;

import com.github.talrey.createdeco.connected.SpriteShifts;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber
public class CreateDecoClientEvents {

  @SubscribeEvent
  public static void clientInit (final FMLClientSetupEvent event) {
    event.enqueueWork(SpriteShifts::populateMaps);
  }
}
