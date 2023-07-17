package com.github.talrey.createdeco.registry.fabric;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.registry.DecoCreativeModeTab;
import com.github.talrey.createdeco.registry.Props;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class DecoCreativeModeTabImpl {
  public static void build () {
    DecoCreativeModeTab.BRICKS_GROUP = FabricItemGroupBuilder.build(
      new ResourceLocation(CreateDecoMod.MODID, "bricks"),
      ()-> Registration.BRICK_BLOCK.get(DyeColor.LIGHT_BLUE).asStack()
    );
    DecoCreativeModeTab.METALS_GROUP = FabricItemGroupBuilder.build(
      new ResourceLocation(CreateDecoMod.MODID, "metals"),
      ()-> Registration.BAR_BLOCKS.get("brass").asStack()
    );
    DecoCreativeModeTab.PROPS_GROUP = FabricItemGroupBuilder.build(
      new ResourceLocation(CreateDecoMod.MODID, "props"),
      ()-> Props.COINSTACK_ITEM.get("Brass").asStack()
    );
  }
}
