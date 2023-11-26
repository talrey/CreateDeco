package com.github.talrey.createdeco.fabric;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.blocks.CatwalkStairBlock;
import com.github.talrey.createdeco.blocks.DecalBlock;
import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.github.talrey.createdeco.blocks.SupportWedgeBlock;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.content.decoration.palettes.ConnectedPillarBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.block.MultiPartBlockStateBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Locale;
import java.util.function.Function;

public class BlockStateGeneratorImpl {
  public static void bar(
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

  public static void barItem(
    String base, String suf, ResourceLocation bartex,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    if (suf.isEmpty()) {
      prov.singleTexture(base, prov.mcLoc("item/generated"), "layer0", bartex);
    } else {
      prov.withExistingParent(base + suf, prov.mcLoc("item/generated"))
        .texture("layer0", bartex)
        .texture("layer1", prov.modLoc("block/palettes/metal_bars/" + base + suf));
    }
  }

  public static void sheetMetal(
    String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov) {
    var name = metal.toLowerCase().replace(" ", "_") + "_sheet_metal";
    var side = CreateDecoMod.id("block/palettes/sheet_metal/" + name);
    var end = CreateDecoMod.id("block/palettes/sheet_metal/" + name + "_top");
    prov.getVariantBuilder(ctx.getEntry())
      .forAllStatesExcept(state -> {
          Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
          if (axis == Direction.Axis.Y)
            return ConfiguredModel.builder()
              .modelFile(prov.models()
                .cubeColumn(ctx.getName(), side, end))
              .uvLock(false)
              .build();
          return ConfiguredModel.builder()
            .modelFile(prov.models()
              .cubeColumnHorizontal(ctx.getName() + "_horizontal", side, end))
            .uvLock(false)
            .rotationX(90)
            .rotationY(axis == Direction.Axis.X ? 90 : 0)
            .build();
        }, BlockStateProperties.WATERLOGGED, ConnectedPillarBlock.NORTH, ConnectedPillarBlock.SOUTH,
        ConnectedPillarBlock.EAST, ConnectedPillarBlock.WEST);
  }

  public static void fence(
    String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
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
      } else if (state[0] == 'f') {
        part.condition(BlockStateProperties.NORTH, false);
      } // else 'x' don't care
      if (state[1] == 't') {
        part.condition(BlockStateProperties.SOUTH, true);
      } else if (state[1] == 'f') {
        part.condition(BlockStateProperties.SOUTH, false);
      } // else 'x' don't care
      if (state[2] == 't') {
        part.condition(BlockStateProperties.EAST, true);
      } else if (state[2] == 'f') {
        part.condition(BlockStateProperties.EAST, false);
      } // else 'x' don't care
      if (state[3] == 't') {
        part.condition(BlockStateProperties.WEST, true);
      } else if (state[3] == 'f') {
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

  public static void decal(
    CreateRegistrate reg, String type,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String texture = reg.getModid() + ":block/palettes/decals/decal_" + type;

    prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
      int y = 69;
      int x = 69;
      Direction direction = state.getValue(DecalBlock.FACING);
      switch (state.getValue(DecalBlock.FACE)) {
        case FLOOR -> {
          switch (direction) {
            case EAST -> {
              y = 90;
              x = 90;
            }
            case WEST -> {
              y = 270;
              x = 90;
            }
            case SOUTH -> {
              y = 180;
              x = 90;
            }
            case NORTH -> {
              y = 0;
              x = 90;
            }
            case UP, DOWN -> {
              y = 0;
              x = 0;
            }
          }
        }
        case WALL -> {
          switch (direction) {
            case EAST -> {
              y = 270;
              x = 0;
            }
            case WEST -> {
              y = 90;
              x = 0;
            }
            case SOUTH -> {
              y = 0;
              x = 0;
            }
            case NORTH, UP, DOWN -> {
              y = 180;
              x = 0;
            }
          }
        }
        case CEILING -> {
          switch (direction) {
            case EAST -> {
              y = 90;
              x = 270;
            }
            case WEST -> {
              y = 270;
              x = 270;
            }
            case SOUTH -> {
              y = 180;
              x = 270;
            }
            case NORTH -> {
              y = 0;
              x = 270;
            }
            case UP, DOWN -> {
              y = 0;
              x = 0;
            }
          }
        }
      }

      return ConfiguredModel.builder().modelFile(prov.models()
        .withExistingParent(ctx.getName(), prov.modLoc("block/decal"))
        .texture("0", reg.getModid() + ":block/palettes/decals/decal_back")
        .texture("1", texture)
        .texture("particle", texture)
      ).rotationX(x).rotationY(y).build();
    });
  }

  public static void cageLamp(
    ResourceLocation cage, ResourceLocation lampOn, ResourceLocation lampOff,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
      int y = 0;
      int x = 90;
      switch (state.getValue(BlockStateProperties.FACING)) {
        case NORTH -> y = 0;
        case SOUTH -> y = 180;
        case WEST -> y = -90;
        case EAST -> y = 90;
        case DOWN -> x = 180;
        default -> x = 0; // up
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

  public static void catwalk(
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String texture = reg.getModid() + ":block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";

    prov.simpleBlock(ctx.get(), prov.models()
      .withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_top"))
      .texture("2", texture)
      .texture("particle", texture));
  }

  public static void catwalkItem(
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
    prov.simpleBlock(ctx.get(), prov.models()
      .withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_stairs"))
      .texture("2", texture)
      .texture("particle", texture));

//    BlockModelBuilder stair = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_stairs"))
//      .texture("2", texture + "_rail")
//      .texture("3", texture + "_stairs")
//      .texture("particle", texture + "_rail");
//    BlockModelBuilder rail_right = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_stairs_rail_right"))
//      .texture("3", texture + "_rail")
//      .texture("particle", texture + "_rail");
//    BlockModelBuilder rail_left = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_stairs_rail_left"))
//      .texture("3", texture + "_rail")
//      .texture("particle", texture + "_rail");
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(stair).rotationY(90).addModel()
//      .condition(BlockStateProperties.NORTH, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(stair).rotationY(-90).addModel()
//      .condition(BlockStateProperties.SOUTH, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(stair).rotationY(180).addModel()
//      .condition(BlockStateProperties.EAST, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(stair).rotationY(0).addModel()
//      .condition(BlockStateProperties.WEST, true).end();
//
//    //todo: determine reason it refuses to recognize CatwalkStairBlock.RAILING_LEFT and CatwalkStairBlock.RAILING_RIGHT
//    //todo: also port to forge half once this is working
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_right)
//      .rotationY(90).addModel()
//      .condition(BlockStateProperties.NORTH, true)
//      .condition(CatwalkStairBlock.RAILING_RIGHT, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_left)
//      .rotationY(90).addModel()
//      .condition(BlockStateProperties.NORTH, true)
//      .condition(CatwalkStairBlock.RAILING_LEFT, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_right)
//      .rotationY(270).addModel()
//      .condition(BlockStateProperties.SOUTH, true)
//      .condition(CatwalkStairBlock.RAILING_RIGHT, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_left)
//      .rotationY(270).addModel()
//      .condition(BlockStateProperties.SOUTH, true)
//      .condition(CatwalkStairBlock.RAILING_LEFT, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_right)
//      .rotationY(180).addModel()
//      .condition(BlockStateProperties.EAST, true).
//      condition(CatwalkStairBlock.RAILING_RIGHT, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_left)
//      .rotationY(180).addModel()
//      .condition(BlockStateProperties.EAST, true)
//      .condition(CatwalkStairBlock.RAILING_LEFT, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_right)
//      .rotationY(0).addModel()
//      .condition(BlockStateProperties.WEST, true)
//      .condition(CatwalkStairBlock.RAILING_RIGHT, true).end();
//
//    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_left)
//      .rotationY(0).addModel()
//      .condition(BlockStateProperties.WEST, true)
//      .condition(CatwalkStairBlock.RAILING_LEFT, true).end();
  }

  public static void catwalkRailing(
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String texture = reg.getModid() + ":block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";

    BlockModelBuilder rail_lower = prov.models().withExistingParent(ctx.getName(),
        prov.modLoc("block/catwalk_rail"))
      .texture("3", texture + "_rail")
      .texture("particle", texture + "_rail");

    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(90).addModel()
      .condition(BlockStateProperties.NORTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(-90).addModel()
      .condition(BlockStateProperties.SOUTH, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(180).addModel()
      .condition(BlockStateProperties.EAST, true).end();
    prov.getMultipartBuilder(ctx.get()).part().modelFile(rail_lower).rotationY(0).addModel()
      .condition(BlockStateProperties.WEST, true).end();
  }

  public static void catwalkRailingItem(
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
  }

  public static void door(
    CreateRegistrate reg, String metal, boolean locked,
    DataGenContext<DoorBlock, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String regName = (locked ? "locked_" : "")
      + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String path = "block/palettes/doors/" + regName;
    prov.doorBlock(ctx.get(), prov.modLoc(path + "_door_bottom"), prov.modLoc(path + "_door_top"));
  }

  public static void doorItem(
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
      "layer0", prov.modLoc("item/" + ctx.getName())
    );
  }

  public static void hull(
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String regName = ctx.getName();
    ResourceLocation front = prov.modLoc("block/palettes/hull/" + regName + "_front");
    ResourceLocation side = prov.modLoc("block/palettes/hull/" + regName + "_side");
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

  public static void support(
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String regName = ctx.getName();
    ResourceLocation texture = prov.modLoc("block/palettes/support/" + regName);

    prov.directionalBlock(ctx.get(),
      prov.models().withExistingParent(ctx.getName(), prov.modLoc("support"))
        .texture("0", texture)
        .texture("particle", texture)
    );
  }

  public static void supportWedge(
    CreateRegistrate reg, String metal,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {

    String regName = ctx.getName();
    ResourceLocation wedge = prov.modLoc("block/palettes/support_wedges/" + regName);

    prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
      int y = 0;
      int x = 0;
      var facing = state.getValue(SupportWedgeBlock.FACING);
      var orientation = state.getValue(SupportWedgeBlock.ORIENTATION);
      var horizontal = false;

      switch (facing) {
        case NORTH -> {
          switch (orientation) {
            case 1 -> {
              x = 0;
              y = 0;
            }
            case 2 -> {
              x = 0;
              y = 270;
              horizontal = true;
            }
            case 3 -> {
              x = 270;
              y = 0;
            }
            case 4 -> {
              x = 0;
              y = 0;
              horizontal = true;
            }
          }
        }
        case SOUTH -> {
          switch (orientation) {
            case 1 -> {
              x = 0;
              y = 180;
            }
            case 2 -> {
              x = 0;
              y = 180;
              horizontal = true;
            }
            case 3 -> {
              x = 270;
              y = 180;
            }
            case 4 -> {
              x = 0;
              y = 90;
              horizontal = true;
            }
          }
        }
        case EAST -> {
          switch (orientation) {
            case 1 -> {
              x = 0;
              y = 90;
            }
            case 2 -> {
              x = 0;
              y = 0;
              horizontal = true;
            }
            case 3 -> {
              x = 270;
              y = 90;
            }
            case 4 -> {
              x = 0;
              y = 90;
              horizontal = true;
            }
          }
        }
        case WEST -> {
          switch (orientation) {
            case 1 -> {
              x = 0;
              y = 270;
            }
            case 2 -> {
              x = 0;
              y = 270;
              horizontal = true;
            }
            case 3 -> {
              x = 270;
              y = 270;
            }
            case 4 -> {
              x = 0;
              y = 180;
              horizontal = true;
            }
          }
        }
      }
      return ConfiguredModel.builder().modelFile(
          prov.models().withExistingParent(
              ctx.getName() + (horizontal ? "_horizontal" : ""),
              prov.modLoc("block/support_wedge" + (horizontal ? "_horizontal" : ""))
            )
            .texture("0", wedge)
            .texture("particle", wedge))
        .rotationX(x)
        .rotationY(y)
        .build();
    });

  }

  public static void trapdoorItem(
    CreateRegistrate reg, String metal,
    DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    prov.withExistingParent(ctx.getName(), prov.mcLoc("block/template_trapdoor_bottom"))
      .texture("texture", prov.modLoc("block/palettes/doors/" + regName + "_trapdoor"));
  }

  public static void placard(
    CreateRegistrate reg, DyeColor color,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    String regName = color.name().toLowerCase(Locale.ROOT)
      .replaceAll(" ", "_") + "_placard";

    prov.horizontalFaceBlock(ctx.get(),
      prov.models().withExistingParent(regName, prov.modLoc("block/dyed_placard"))
        .texture("0", prov.modLoc("block/palettes/placard/" + regName))
        .texture("particle", prov.modLoc("block/palettes/placard/" + regName))
    );
  }

  public static void shippingContainer(
    CreateRegistrate reg, DyeColor color,
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov
  ) {
    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
      String regName = color.name().toLowerCase(Locale.ROOT)
        .replaceAll(" ", "_");

      return ConfiguredModel.builder().modelFile(prov.models().withExistingParent(
            regName + "_shipping_container", prov.modLoc("block/shipping_container"))
          .texture("0", prov.modLoc("block/palettes/shipping_containers/" + regName + "/vault_bottom_small"))
          .texture("1", prov.modLoc("block/palettes/shipping_containers/" + regName + "/vault_front_small"))
          .texture("2", prov.modLoc("block/palettes/shipping_containers/" + regName + "/vault_side_small"))
          .texture("3", prov.modLoc("block/palettes/shipping_containers/" + regName + "/vault_top_small"))
          .texture("particle", prov.modLoc("block/palettes/shipping_containers/" + regName + "/vault_top_small")))
        .rotationY(state.getValue(ShippingContainerBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0)
        .build();
    });
  }

  public static void coinstackBlock(
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

  public static void brick(
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    prov.simpleBlock(ctx.get(), prov.models().cubeAll(ctx.getName(),
      prov.modLoc("block/palettes/bricks/" + color + "/" + ctx.getName())
    ));
  }

  public static void brickStair(
    DataGenContext<Block, StairBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    prov.stairsBlock(ctx.get(),
      prov.modLoc("block/palettes/bricks/" + color + "/"
        + ctx.getName().replaceAll("_stair", "")
      )
    );
  }

  public static void brickSlab(
    DataGenContext<Block, SlabBlock> ctx, RegistrateBlockstateProvider prov, String color
  ) {
    String block = ctx.getName().replaceAll("_slab", "s");
    ResourceLocation blockModel = prov.modLoc("block/" + block);
    ResourceLocation texture = prov.modLoc(
      "block/palettes/bricks/" + color + "/" + block
    );
    prov.slabBlock(ctx.get(), blockModel, texture);
  }

  public static void window(
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov,
    NonNullFunction<String, ResourceLocation> sideTexture,
    NonNullFunction<String, ResourceLocation> endTexture
  ) {
    prov.simpleBlock(ctx.get(), prov.models()
      .cubeColumn(ctx.getName(), sideTexture.apply(ctx.getName()), endTexture.apply(ctx.getName()))
    );
  }

  public static NonNullBiConsumer<DataGenContext<Block, ConnectedGlassPaneBlock>, RegistrateBlockstateProvider> windowPane(
    String CGPparents, String prefix, ResourceLocation sideTexture, ResourceLocation topTexture
  ) {
    Function<RegistrateBlockstateProvider, ModelFile> post =
      getPaneModelProvider(CGPparents, prefix, "post", sideTexture, topTexture),
      side = getPaneModelProvider(CGPparents, prefix, "side", sideTexture, topTexture),
      sideAlt = getPaneModelProvider(CGPparents, prefix, "side_alt", sideTexture, topTexture),
      noSide = getPaneModelProvider(CGPparents, prefix, "noside", sideTexture, topTexture),
      noSideAlt = getPaneModelProvider(CGPparents, prefix, "noside_alt", sideTexture, topTexture);

    return (c, p) -> p.paneBlock(c.get(),
      post.apply(p),
      side.apply(p),
      sideAlt.apply(p),
      noSide.apply(p),
      noSideAlt.apply(p)
    );
  }

  private static Function<RegistrateBlockstateProvider, ModelFile> getPaneModelProvider(String CGPparents, String prefix, String partial, ResourceLocation sideTexture, ResourceLocation topTexture) {
    return p -> p.models()
      .withExistingParent(prefix + partial, CreateDecoMod.id(CGPparents + partial))
      .texture("pane", sideTexture)
      .texture("edge", topTexture);
  }

  public static void ladder(
    DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov, String regName
  ) {
    prov.horizontalBlock(ctx.get(), prov.models()
      .withExistingParent(ctx.getName(), prov.modLoc("block/ladder"))
      .texture("0", prov.modLoc("block/palettes/ladders/ladder_" + regName + "_hoop"))
      .texture("1", prov.modLoc("block/palettes/ladders/ladder_" + regName))
      .texture("particle", prov.modLoc("block/palettes/ladders/ladder_" + regName))
    );
  }
}
