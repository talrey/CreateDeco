package com.github.talrey.createdeco.registry.fabric;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.registry.DecoCreativeModeTab;
import com.github.talrey.createdeco.registry.Props;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DecoCreativeModeTabImpl {
  public static void build () {
    DecoCreativeModeTab.BRICKS_GROUP = build(
      new ResourceLocation(CreateDecoMod.MODID, DecoCreativeModeTab.BRICKS_KEY),
      DecoCreativeModeTab.BRICKS_NAME,
      ()-> Registration.BRICK_BLOCK.get(DyeColor.LIGHT_BLUE).asStack()
    );
    DecoCreativeModeTab.METALS_GROUP = build(
      new ResourceLocation(CreateDecoMod.MODID, DecoCreativeModeTab.METALS_KEY),
      DecoCreativeModeTab.METALS_NAME,
      ()-> Registration.BAR_BLOCKS.get("brass").asStack()
    );
    DecoCreativeModeTab.PROPS_GROUP = build(
      new ResourceLocation(CreateDecoMod.MODID, DecoCreativeModeTab.PROPS_KEY),
      DecoCreativeModeTab.PROPS_NAME,
      ()-> Props.COINSTACK_ITEM.get("Brass").asStack()
    );
  }

  private static CreativeModeTab build (ResourceLocation rl, String name, Supplier<ItemStack> icon) {
    CreativeModeTab result = FabricItemGroup.builder()
      .title(Component.translatableWithFallback(rl.toLanguageKey(), name))
      .icon(icon)
      .build();
    ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, rl);
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, result);
    return result;
  }
}
