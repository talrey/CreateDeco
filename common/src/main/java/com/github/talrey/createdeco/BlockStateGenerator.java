package com.github.talrey.createdeco;

import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

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
  public static void sheetMetal (
      String metal, DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
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
  public static void decal (
      CreateRegistrate reg, String type,
      DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
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
  public static void supportWedge (
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

  @ExpectPlatform
  public static void shippingContainer (
          CreateRegistrate reg, DyeColor color,
          DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void placard (
          CreateRegistrate reg, DyeColor color,
          DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void coinstackBlock (
    ResourceLocation side, ResourceLocation bottom, ResourceLocation top,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void brick (
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void brickStair (
    DataGenContext<Block, StairBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void brickSlab (
    DataGenContext<Block, SlabBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void brickWall (
          DataGenContext<Block, WallBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void brickWallItem (
    DataGenContext<Item, BlockItem> ctx, RegistrateItemModelProvider prov, String color
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void window (
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov,
    NonNullFunction<String, ResourceLocation> sideTexture,
    NonNullFunction<String, ResourceLocation> endTexture
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static NonNullBiConsumer<DataGenContext<Block, ConnectedGlassPaneBlock>, RegistrateBlockstateProvider> windowPane (
    String CGPparents, String prefix, ResourceLocation sideTexture, ResourceLocation topTexture
  ) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void ladder (
    DataGenContext<Block,?> ctx, RegistrateBlockstateProvider prov, String regName
  ) {
    throw new AssertionError();
  }
}
