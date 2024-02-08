package com.github.talrey.createdeco.forge;

import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class BlockStateGeneratorImpl {
  public static void bar (
    String base, String post, ResourceLocation barTexture, ResourceLocation postTexture,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void barItem (
    String base, String suf, ResourceLocation bartex,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.getExistingFile(prov.modLoc(ctx.getName())));
  }

  public static void fence (
    String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void cageLamp (
    ResourceLocation cage, ResourceLocation lampOn, ResourceLocation lampOff,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
      return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
        ctx.getName() + (state.getValue(BlockStateProperties.LIT) ? "" : "_off")
      ))).build();
    });
  }

  public static void catwalk (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void catwalkItem (
    String metal, DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.getExistingFile(prov.modLoc(ctx.getName())));
  }

  public static void catwalkStair (
    String texture, DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void catwalkRailing (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void catwalkRailingItem (
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
  }

  public static void door (
    CreateRegistrate reg, String metal, boolean locked,
    DataGenContext<DoorBlock, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void doorItem (
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
  }

  public static void hull (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void support (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }


  public static void supportWedge (
          CreateRegistrate reg, String metal,
          DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
            ctx.getName()
    )));
  }

  public static void trapdoorItem (
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
  }

  public static void placard (
    CreateRegistrate reg, DyeColor color,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void shippingContainer (
    CreateRegistrate reg, DyeColor color,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void coinstackBlock (
    ResourceLocation side, ResourceLocation bottom, ResourceLocation top,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void brick (
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void brickStair (
    DataGenContext<Block, StairBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void brickSlab (
    DataGenContext<Block, SlabBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void brickWall(
          DataGenContext<Block, WallBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static void window (
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov,
    NonNullFunction<String, ResourceLocation> sideTexture,
    NonNullFunction<String, ResourceLocation> endTexture
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }

  public static NonNullBiConsumer<DataGenContext<Block, ConnectedGlassPaneBlock>, RegistrateBlockstateProvider> windowPane (
    String CGPparents, String prefix, ResourceLocation sideTexture, ResourceLocation topTexture
  ) {
    // will this explode...?
    return (ctx,prov)-> BlockStateGeneratorImpl.window(ctx,prov, s->sideTexture, s->topTexture);
  }

  public static void ladder (
    DataGenContext<Block,?> ctx, RegistrateBlockstateProvider prov, String regName
  ) {
    ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
      ctx.getName()
    )));
  }
}
