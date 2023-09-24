package com.github.talrey.createdeco.registry;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class PerLoaderRegistration {
  @ExpectPlatform
  public static void hullBlockState (
    ResourceLocation front, ResourceLocation side,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void brickVertBlockState (
    String pre, String name, String suf,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void barBlockState (
    String name, String suf, ResourceLocation bartex, ResourceLocation postex,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void fenceBlockState (
    String metal, DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void catwalkBlockState (
    Registrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void catwalkStairBlockState (
    String texture,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void coinStackBlockState (
    ResourceLocation side, ResourceLocation bottom, ResourceLocation top,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void cageLampBlockState (
    ResourceLocation cage, ResourceLocation lampOn, ResourceLocation lampOff,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void decalBlockState (
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void sheetMetalVertBlockState (
    ResourceLocation texture,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }
}
