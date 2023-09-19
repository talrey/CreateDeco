package com.github.talrey.createdeco.registry.forge;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.registry.DecoCreativeModeTab;
import com.github.talrey.createdeco.registry.Props;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DecoCreativeModeTabImpl {
  public static void build () {
    DecoCreativeModeTab.BRICKS_GROUP = group(DecoCreativeModeTab.BRICKS_KEY,
      ()-> Registration.BRICK_BLOCK.get(DyeColor.LIGHT_BLUE).asStack()
    );
    DecoCreativeModeTab.METALS_GROUP = group(DecoCreativeModeTab.METALS_KEY,
      ()-> Registration.BAR_BLOCKS.get("brass").asStack()
    );
    DecoCreativeModeTab.PROPS_GROUP = group(DecoCreativeModeTab.PROPS_KEY,
      ()-> Props.COINSTACK_ITEM.get("Brass").asStack()
    );
  }

  private static CreativeModeTab group (String name, Supplier<ItemStack> item) {
    return CreativeModeTab.builder()
      .icon(item)
      .title(Component.translatable("itemGroup." + CreateDecoMod.MODID + "." + name))
      .build();
  }
}
