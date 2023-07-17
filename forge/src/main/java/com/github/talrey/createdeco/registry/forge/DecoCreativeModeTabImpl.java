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
    DecoCreativeModeTab.BRICKS_GROUP = group("bricks",
      ()-> Registration.BRICK_BLOCK.get(DyeColor.LIGHT_BLUE).asStack()
    );
    DecoCreativeModeTab.METALS_GROUP = group("metals",
      ()-> Registration.BAR_BLOCKS.get("brass").asStack()
    );
    DecoCreativeModeTab.PROPS_GROUP = group("props",
      ()-> Props.COINSTACK_ITEM.get("Brass").asStack()
    );
  }

  private static CreativeModeTab group (String name, Supplier<ItemStack> item) {
    return new CreativeModeTab (CreateDecoMod.MODID) {
      @Override
      public ItemStack makeIcon () {
        return item.get();
      }
      @Override
      public Component getDisplayName () {
        return Component.translatable("itemGroup." + CreateDecoMod.MODID + "." + name);
      }
    };
  }
}
