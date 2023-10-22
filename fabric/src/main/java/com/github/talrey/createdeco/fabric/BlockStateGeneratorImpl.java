package com.github.talrey.createdeco.fabric;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.block.MultiPartBlockStateBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Locale;

public class BlockStateGeneratorImpl {
  public static void bar (
    String name, String suf, ResourceLocation bartex, ResourceLocation postex,
      DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());
    BlockModelBuilder sideModel = prov.models().withExistingParent(
        name + "_side", prov.mcLoc("block/iron_bars_side"))
      .texture("bars", bartex)
      .texture("edge", postex)
      .texture("particle", postex);
    BlockModelBuilder sideAltModel = prov.models().withExistingParent(
        name + "_side_alt", prov.mcLoc("block/iron_bars_side_alt"))
      .texture("bars", bartex)
      .texture("edge", postex)
      .texture("particle", postex);

    builder.part().modelFile(prov.models().withExistingParent(name + "_post", prov.mcLoc("block/iron_bars_post"))
        .texture("bars", postex).texture("particle", postex)
      ).addModel()
      .condition(BlockStateProperties.NORTH, false)
      .condition(BlockStateProperties.SOUTH, false)
      .condition(BlockStateProperties.EAST, false)
      .condition(BlockStateProperties.WEST, false)
      .end();
    builder.part().modelFile(
      prov.models().withExistingParent(name + "_post_ends", prov.mcLoc("block/iron_bars_post_ends"))
        .texture("edge", postex).texture("particle", postex)
    ).addModel().end();
    builder.part().modelFile(sideModel).addModel().condition(BlockStateProperties.NORTH, true).end();
    builder.part().modelFile(sideModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
    builder.part().modelFile(sideAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
    builder.part().modelFile(sideAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();

    if (!suf.equals("")) {
      BlockModelBuilder sideOverlayModel = prov.models().withExistingParent(
          name + suf, prov.mcLoc("block/iron_bars_side"))
        .texture("bars", prov.modLoc("block/palettes/metal_bars/" + name + suf))
        .texture("edge", postex)
        .texture("particle", postex);
      BlockModelBuilder sideOverlayAltModel = prov.models().withExistingParent(
          name + suf + "_alt", prov.mcLoc("block/iron_bars_side_alt"))
        .texture("bars", prov.modLoc("block/palettes/metal_bars/" + name + suf))
        .texture("edge", postex)
        .texture("particle", postex);

      builder.part().modelFile(sideOverlayModel).addModel().condition(BlockStateProperties.NORTH, true).end();
      builder.part().modelFile(sideOverlayModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
      builder.part().modelFile(sideOverlayAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
      builder.part().modelFile(sideOverlayAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();
    }
  }

  public static void barItem (
    String base, String suf, ResourceLocation bartex,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    if (suf.isEmpty()) {
      prov.singleTexture(base, prov.mcLoc("item/generated"),"layer0", bartex);
    }
    else {
      prov.withExistingParent(base + suf, prov.mcLoc("item/generated"))
        .texture("layer0", bartex)
        .texture("layer1", prov.modLoc("block/palettes/metal_bars/" + base + suf));
    }
  }

  public static void cageLamp (
    ResourceLocation cage, ResourceLocation lampOn, ResourceLocation lampOff,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    prov.getVariantBuilder(ctx.get()).forAllStates(state-> {
      int y = 0;
      int x = 90;
      switch (state.getValue(BlockStateProperties.FACING)) {
        case NORTH -> y =   0;
        case SOUTH -> y = 180;
        case WEST  -> y = -90;
        case EAST  -> y =  90;
        case DOWN  -> x = 180;
        default    -> x =   0; // up
      }
      return ConfiguredModel.builder().modelFile(prov.models()
        .withExistingParent(
          ctx.getName() + (state.getValue(BlockStateProperties.LIT) ? "" : "_off"),
          prov.modLoc("block/cage_lamp"))
        .texture("cage", cage)
        .texture("lamp", state.getValue(BlockStateProperties.LIT) ? lampOn : lampOff)
        .texture("particle", cage)
      ).rotationX(x).rotationY(y).build();
    });
  }

  public static void catwalk (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String texture = reg.getModid() + ":block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";

    prov.simpleBlock(ctx.get(), prov.models()
      .withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_top"))
      .texture("2", texture)
      .texture("particle", texture));
  }

  public static void catwalkItem (
    String metal, DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    prov.withExistingParent(ctx.getName(), prov.mcLoc("block/template_trapdoor_bottom"))
      .texture("texture",
        prov.modLoc("block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT)
          .replaceAll(" ", "_") + "_catwalk"
        )
      );
  }

  public static void catwalkStair (
    String texture, DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    BlockModelBuilder builder = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_stairs"))
      .texture("2", texture + "_rail")
      .texture("3", texture + "_stairs")
      .texture("particle", texture  +"_rail");
    prov.horizontalBlock(ctx.get(), builder);
  }

  public static void catwalkRailing (
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String texture = reg.getModid() + ":block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";

    BlockModelBuilder rail_lower = prov.models().withExistingParent(ctx.getName(),
        prov.modLoc("block/catwalk_rail"))
      .texture("3", texture + "_rail")
      .texture("particle", texture + "_rail");

    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY( 90).addModel()
      .condition(BlockStateProperties.NORTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(-90).addModel()
      .condition(BlockStateProperties.SOUTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(180).addModel()
      .condition(BlockStateProperties.EAST,  true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(  0).addModel()
      .condition(BlockStateProperties.WEST,  true).end();
  }

  public static void catwalkRailingItem (
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
  }
}
