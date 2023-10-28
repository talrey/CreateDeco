package com.github.talrey.createdeco;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;

import java.util.Locale;

public class BlockStateGenerator {
  @ExpectPlatform
  public static void bar (
    String base, String suf, ResourceLocation barTexture, ResourceLocation postTexture,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void barItem (
    String base, String suf, ResourceLocation bartex,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void fence (
    String metal, DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

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

  @ExpectPlatform
  public static void catwalkStair (
    String texture, DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void catwalkRailing (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void catwalkRailingItem (
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void door (
    CreateRegistrate reg, String metal, boolean locked,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void doorItem (
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void hull (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void support (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void trapdoorItem (
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    throw new AssertionError();
  }
}
