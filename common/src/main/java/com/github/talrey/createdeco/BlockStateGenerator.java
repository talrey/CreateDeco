package com.github.talrey.createdeco;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Locale;

public class BlockStateGenerator {
  @ExpectPlatform
  public static void cageLamp (
    ResourceLocation cage, ResourceLocation lampOn, ResourceLocation lampOff,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void catwalk (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void catwalkItem (
    String metal, DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    throw new AssertionError();
  }
}
