package com.github.talrey.createdeco.forge;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.CreativeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CreativeTabsImpl {
  private static final DeferredRegister<CreativeModeTab> TABS =
    DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateDecoMod.MOD_ID);

  private static final RegistryObject<CreativeModeTab> PROPS = TABS.register(
    CreativeTabs.PROPS_KEY, ()->group(
      CreativeTabs.PROPS_KEY, ()-> BlockRegistry.GREEN_CAGE_LAMPS.get("Brass").asStack()
    )
  );

  public static void register (IEventBus eventBus) {
    TABS.register(eventBus);
  }

  public static CreativeModeTab props () {
    return PROPS.get();
  }

  private static CreativeModeTab group (String name, Supplier<ItemStack> item) {
    CreativeModeTab tab = CreativeModeTab.builder()
      .icon(item)
      .title(Component.translatableWithFallback(
        "itemGroup." + CreateDecoMod.MOD_ID + "." + name, "Create Deco Props"
      ))
      .build();
    return tab;
  }
}
