package com.github.talrey.createdeco.fabric;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Locale;

public class BlockStateGeneratorImpl {
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
