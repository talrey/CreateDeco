package com.github.talrey.createdeco.registry.fabric;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.block.MultiPartBlockStateBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Locale;

public class PerLoaderRegistrationImpl {
  public static void hullBlockState (
    ResourceLocation front, ResourceLocation side,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    prov.getVariantBuilder(ctx.get())
      .forAllStates(state -> {
        Direction dir = state.getValue(BlockStateProperties.FACING);
        return ConfiguredModel.builder()
          .modelFile(prov.models().withExistingParent(
              ctx.getName(), prov.modLoc("train_hull"))
            .texture("0", front)
            .texture("1", side)
            .texture("particle", front))
          .rotationX(dir == Direction.DOWN ? 270 : dir.getAxis().isHorizontal() ? 0 : 90)
          .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 0) % 360)
          .build();
      });
  }

  public static void brickVertBlockState (
    String pre, String name, String suf,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String texLoc = "block/palettes/bricks/" + name.toLowerCase(Locale.ROOT) + "/" + pre + name.toLowerCase(Locale.ROOT)+"_" + suf;
    ResourceLocation tex = prov.modLoc(texLoc);
    BlockModelBuilder half = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/vertical_slab"))
      .texture("side", tex);
    BlockModelBuilder both = prov.models().cubeAll(ctx.getName()+"_double", tex);

    int y = 0;
    for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
      switch (dir) {
        case NORTH -> y = 0;
        case SOUTH -> y = 180;
        case WEST -> y = -90;
        case EAST -> y = 90;
      }
      prov.getMultipartBuilder(ctx.get()).part().modelFile(half).rotationY(y).addModel()
        .condition(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
        .condition(BlockStateProperties.HORIZONTAL_FACING, dir).end();
      prov.getMultipartBuilder(ctx.get()).part().modelFile(both).rotationY(y).addModel()
        .condition(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).end();
    }
  }

  public static void barBlockState (
    String name, String suf, ResourceLocation bartex, ResourceLocation postex,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    {
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
  }

  public static void fenceBlockState (
    String metal, DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String postDir = "block/palettes/sheet_metal/";
    String meshDir = "block/palettes/chain_link_fence/";
    ResourceLocation post = prov.modLoc(postDir + regName + "_sheet_metal");
    ResourceLocation mesh = prov.modLoc(meshDir + regName + "_chain_link");

    char[][] states = {
      // N    S      E      W
      {'f', 'f', 'f', 'f'}, // solo
      {'t', 'x', 't', 'x'},   // NE corner / tri / cross
      {'t', 'x', 'x', 't'},   // NW corner / tri / cross
      {'x', 't', 't', 'x'},   // SE corner / tri / cross
      {'x', 't', 'x', 't'},   // SW corner / tri / cross
      {'t', 'f', 'f', 'f'},  // N end
      {'f', 't', 'f', 'f'},  // S end
      {'f', 'f', 't', 'f'},  // E end
      {'f', 'f', 'f', 't'}   // W end
    };
    BlockModelBuilder center = prov.models().withExistingParent(
      ctx.getName() + "_post", prov.mcLoc("block/fence_post")
    ).texture("texture", post);
    BlockModelBuilder side = prov.models().withExistingParent(
        ctx.getName() + "_side", prov.modLoc("block/chainlink_fence_side")
      ).texture("particle", mesh)
      .texture("0", mesh);
    MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());
    for (char[] state : states) {
      MultiPartBlockStateBuilder.PartBuilder part = builder.part().modelFile(center).addModel();
      if (state[0] == 't') {
        part.condition(BlockStateProperties.NORTH, true);
      }
      else if (state[0] == 'f') {
        part.condition(BlockStateProperties.NORTH, false);
      } // else 'x' don't care
      if (state[1] == 't') {
        part.condition(BlockStateProperties.SOUTH, true);
      }
      else if (state[1] == 'f') {
        part.condition(BlockStateProperties.SOUTH, false);
      } // else 'x' don't care
      if (state[2] == 't') {
        part.condition(BlockStateProperties.EAST, true);
      }
      else if (state[2] == 'f') {
        part.condition(BlockStateProperties.EAST, false);
      } // else 'x' don't care
      if (state[3] == 't') {
        part.condition(BlockStateProperties.WEST, true);
      }
      else if (state[3] == 'f') {
        part.condition(BlockStateProperties.WEST, false);
      } // else 'x' don't care
      part.end();
    }
    builder.part().modelFile(side).addModel()
      .condition(BlockStateProperties.EAST, true).end();
    builder.part().modelFile(side)
      .rotationY(90).addModel()
      .condition(BlockStateProperties.SOUTH, true).end();
    builder.part().modelFile(side)
      .rotationY(180).addModel()
      .condition(BlockStateProperties.WEST, true).end();
    builder.part().modelFile(side)
      .rotationY(270).addModel()
      .condition(BlockStateProperties.NORTH, true).end();
  }

  public static void catwalkBlockState (
    Registrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String texture = reg.getModid() + ":block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";

    BlockModelBuilder lower = prov.models().withExistingParent(ctx.getName()+"_bottom", prov.modLoc("block/catwalk_bottom"))
      .texture("2", texture)
      .texture("particle", texture);
    BlockModelBuilder upper = prov.models().withExistingParent(ctx.getName()+"_top", prov.modLoc("block/catwalk_top"))
      .texture("2", texture)
      .texture("particle", texture);
    BlockModelBuilder rail_upper = prov.models().withExistingParent(ctx.getName()+"_rail_upper",
        prov.modLoc("block/catwalk_rail_upper"))
      .texture("3", texture + "_rail")
      .texture("particle", texture + "_rail");
    BlockModelBuilder rail_lower = prov.models().withExistingParent(ctx.getName()+"_rail_lower",
        prov.modLoc("block/catwalk_rail_lower"))
      .texture("3", texture + "_rail")
      .texture("particle", texture + "_rail");

    prov.getMultipartBuilder(ctx.get()).part().modelFile(lower).addModel().condition(BlockStateProperties.BOTTOM, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(upper).addModel().condition(BlockStateProperties.BOTTOM, false).end();

    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY( 90).addModel()
      .condition(BlockStateProperties.BOTTOM, true)
      .condition(BlockStateProperties.NORTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(-90).addModel()
      .condition(BlockStateProperties.BOTTOM, true)
      .condition(BlockStateProperties.SOUTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(180).addModel()
      .condition(BlockStateProperties.BOTTOM, true)
      .condition(BlockStateProperties.EAST,  true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(  0).addModel()
      .condition(BlockStateProperties.BOTTOM, true)
      .condition(BlockStateProperties.WEST,  true).end();

    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY( 90).addModel()
      .condition(BlockStateProperties.BOTTOM, false)
      .condition(BlockStateProperties.NORTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY(-90).addModel()
      .condition(BlockStateProperties.BOTTOM, false)
      .condition(BlockStateProperties.SOUTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY(180).addModel()
      .condition(BlockStateProperties.BOTTOM, false)
      .condition(BlockStateProperties.EAST,  true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_upper).rotationY(  0).addModel()
      .condition(BlockStateProperties.BOTTOM, false)
      .condition(BlockStateProperties.WEST,  true).end();
  }

  public static void catwalkStairBlockState (
    String texture,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    BlockModelBuilder builder = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_stairs"))
      .texture("2", texture + "_rail")
      .texture("3", texture + "_stairs")
      .texture("particle", texture  +"_rail");
    prov.horizontalBlock(ctx.get(), builder);
  }

  public static void coinStackBlockState (
    ResourceLocation side, ResourceLocation bottom, ResourceLocation top,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
      int layer = state.getValue(BlockStateProperties.LAYERS);
      return ConfiguredModel.builder().modelFile(prov.models().withExistingParent(
            ctx.getName() + "_" + layer, prov.modLoc("block/layers_bottom_top_" + layer)
          )
          .texture("side", side)
          .texture("bottom", bottom)
          .texture("top", top)
      ).build();
    });
  }

  public static void cageLampBlockState (
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
        .withExistingParent(ctx.getName() + (state.getValue(BlockStateProperties.LIT) ? "" : "_off"), prov.modLoc("block/cage_lamp"))
        .texture("cage", cage)
        .texture("lamp", state.getValue(BlockStateProperties.LIT) ? lampOn : lampOff)
        .texture("particle", cage)
      ).rotationX(x).rotationY(y).build(); });
  }

  public static void decalBlockState (
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    prov.getVariantBuilder(ctx.get()).forAllStates(state-> {
      int y = 0;
      switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
        case NORTH: y =   0; break;
        case SOUTH: y = 180; break;
        case WEST:  y = -90; break;
        case EAST:  y =  90; break;
      }
      return ConfiguredModel.builder().modelFile(prov.models()
        .withExistingParent(ctx.getName(), prov.modLoc("block/decal"))
        .texture("face", prov.modLoc("block/palettes/decal/" + ctx.getName()))
        .texture("particle", prov.modLoc("block/palettes/decal/" + ctx.getName()))
      ).rotationY(y).build(); });
  }

  public static void sheetMetalVertBlockState (
    ResourceLocation texture,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    BlockModelBuilder half = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/vertical_slab"))
      .texture("side", texture);
    BlockModelBuilder both = prov.models().cubeAll(ctx.getName()+"_double", texture);

    int y = 0;
    for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
      switch (dir) {
        case NORTH: y =   0; break;
        case SOUTH: y = 180; break;
        case WEST:  y = -90; break;
        case EAST:  y =  90; break;
      }
      prov.getMultipartBuilder(ctx.get()).part().modelFile(half).rotationY(y).addModel()
        .condition(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
        .condition(BlockStateProperties.HORIZONTAL_FACING, dir).end();
      prov.getMultipartBuilder(ctx.get()).part().modelFile(both).rotationY(y).addModel()
        .condition(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).end();
    }
  }
}
