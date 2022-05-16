package com.github.talrey.createdeco.registry;

import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.connected.CatwalkCTBehaviour;
import com.github.talrey.createdeco.connected.SpriteShifts;
import com.github.talrey.createdeco.items.CatwalkBlockItem;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.function.Function;

public class MetalDecoBuilders {

  public static BlockBuilder<IronBarsBlock,?> buildBars (Registrate reg, String metal, Function<String,Item> getter, String suffix) {
    return buildBars(reg, metal, getter, suffix, false);
  }

  public static BlockBuilder<IronBarsBlock,?> buildBars (Registrate reg, String metal, Function<String,Item> getter, String suffix, boolean doPost) {
    String base = metal.replace(' ', '_').toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_bars";
    String suf = suffix.equals("") ? "" : "_" + suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String post = "block/palettes/metal_bars/" + base + (doPost ? "_post" : "");

    ResourceLocation barTexture, postTexture;
    final ResourceLocation bartex, postex;
    try {
      barTexture = new ResourceLocation(reg.getModid(), "block/palettes/metal_bars/" + base);
      File touch = new File("../src/main/resources/assets/" + reg.getModid() + "/textures/" + barTexture.getPath() + ".png"); // fuck it.
      if (!touch.exists()) throw new FileNotFoundException(base + " was not found!");
    } catch (FileNotFoundException fnfe) {
      barTexture = new ResourceLocation("block/" + base);
    }
    try {
      postTexture = new ResourceLocation(reg.getModid(), post);
      File touch = new File("../src/main/resources/assets/" + reg.getModid() + "/textures/" + postTexture.getPath() + ".png");
      if (!touch.exists()) throw new FileNotFoundException(base + " was not found!");
    } catch (FileNotFoundException fnfe) {
      postTexture = barTexture;
    }
    // for lambda stuff, must be final
    bartex = barTexture;
    postex = postTexture;

    return reg.block(base + suf, IronBarsBlock::new)
      .properties(props -> props.noOcclusion().strength(5, (metal.equals("Netherite")) ? 1200 : 6)
        .sound(SoundType.NETHERITE_BLOCK))
      .blockstate((ctx, prov) -> {
        MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());
        BlockModelBuilder sideModel = prov.models().withExistingParent(
            base + "_side", prov.mcLoc("block/iron_bars_side"))
          .texture("bars", bartex)
          .texture("edge", postex)
          .texture("particle", postex);
        BlockModelBuilder sideAltModel = prov.models().withExistingParent(
            base + "_side_alt", prov.mcLoc("block/iron_bars_side_alt"))
          .texture("bars", bartex)
          .texture("edge", postex)
          .texture("particle", postex);

        builder.part().modelFile(prov.models().withExistingParent(base + "_post", prov.mcLoc("block/iron_bars_post"))
            .texture("bars", postex).texture("particle", postex)
          ).addModel()
          .condition(BlockStateProperties.NORTH, false)
          .condition(BlockStateProperties.SOUTH, false)
          .condition(BlockStateProperties.EAST, false)
          .condition(BlockStateProperties.WEST, false)
          .end();
        builder.part().modelFile(
          prov.models().withExistingParent(base + "_post_ends", prov.mcLoc("block/iron_bars_post_ends"))
            .texture("edge", postex).texture("particle", postex)
        ).addModel().end();
        builder.part().modelFile(sideModel).addModel().condition(BlockStateProperties.NORTH, true).end();
        builder.part().modelFile(sideModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
        builder.part().modelFile(sideAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
        builder.part().modelFile(sideAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();

        if (!suf.equals("")) {
          BlockModelBuilder sideOverlayModel = prov.models().withExistingParent(
              base + suf, prov.mcLoc("block/iron_bars_side"))
            .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base + suf))
            .texture("edge", postex)
            .texture("particle", postex);
          BlockModelBuilder sideOverlayAltModel = prov.models().withExistingParent(
              base + suf + "_alt", prov.mcLoc("block/iron_bars_side_alt"))
            .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base + suf))
            .texture("edge", postex)
            .texture("particle", postex);

          builder.part().modelFile(sideOverlayModel).addModel().condition(BlockStateProperties.NORTH, true).end();
          builder.part().modelFile(sideOverlayModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
          builder.part().modelFile(sideOverlayAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
          builder.part().modelFile(sideOverlayAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();
        }
      })
      .tag(BlockTags.WALLS)
      .addLayer(()-> RenderType::cutoutMipped)
      .item()
      .model((ctx, prov) -> {
        if (suf.isEmpty()) {
          prov.singleTexture(base, prov.mcLoc("item/generated"),"layer0", bartex);
        }
        else {
          prov.withExistingParent(base + suf, prov.mcLoc("item/generated"))
            .texture("layer0", bartex)
            .texture("layer1", prov.modLoc("block/palettes/metal_bars/" + base + suf));
        }
      })
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      .build();
  }

  public static BlockBuilder<DoorBlock,?> buildDoor (Registrate reg, String name, String path) {
    return reg.block(name, DoorBlock::new)
      .initialProperties(Material.METAL)
      .properties(props -> props.noOcclusion().strength(5, 5).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .blockstate((ctx, prov)-> prov.doorBlock(ctx.get(),
        prov.modLoc(path + "_door_bottom"),
        prov.modLoc(path + "_door_top"))
      )
      .addLayer(()-> RenderType::cutoutMipped)
      .loot((table, block)-> {
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder pool     = LootPool.lootPool();
        pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block))
          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
            .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            ));
        table.add(block, builder.withPool(pool));
      })
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(BlockTags.DOORS)
      .item()
      .model((ctx, prov)-> prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
        "layer0", prov.modLoc("item/" + ctx.getName())
      ))
      .properties(props -> (name.contains("Netherite") ? props.fireResistant() : props))
      .build();
  }

  public static BlockBuilder<FenceBlock,?> buildFence (Registrate reg, String metal) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_mesh_fence", FenceBlock::new)
      .initialProperties(Material.METAL)
      .properties(props-> props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops()
        .sound(SoundType.CHAIN)
      )
      .addLayer(()-> RenderType::cutoutMipped)
      .tag(BlockTags.FENCES)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      .model((ctx,prov)-> prov.singleTexture(
        ctx.getName(), prov.mcLoc("item/generated"),
        "layer0", prov.modLoc("block/palettes/chain_link_fence/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_chain_link")))
      .build()
      .recipe((ctx,prov)-> {
        if (metal.equals("Andesite")) {
          ShapedRecipeBuilder.shaped(ctx.get(), 3)
            .pattern("psp")
            .pattern("psp")
            .define('p', AllItems.ANDESITE_ALLOY.get())
            .define('s', Items.STRING)
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.ANDESITE_ALLOY.get()))
            .save(prov);
        }
        else {
          TagKey<Item> sheet = Registration.makeItemTag("plates/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_"));
          ShapedRecipeBuilder.shaped(ctx.get(), 3)
            .pattern("psp")
            .pattern("psp")
            .define('p', sheet)
            .define('s', Items.STRING)
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(sheet).build()))
            .save(prov);
        }
      })
      .blockstate((ctx,prov)-> {
        prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
          String dir = "chainlink_fence";
          boolean north,south,east,west;
          north = state.getValue(BlockStateProperties.NORTH);
          south = state.getValue(BlockStateProperties.SOUTH);
          east  = state.getValue(BlockStateProperties.EAST);
          west  = state.getValue(BlockStateProperties.WEST);
          int sides = (north?1:0) + (south?1:0) + (east?1:0) + (west?1:0);
          ResourceLocation mesh = prov.modLoc("block/palettes/chain_link_fence/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_chain_link");
          ResourceLocation wall = prov.modLoc("block/palettes/sheet_metal/"      + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal");
          switch (sides) {
            case 4: return ConfiguredModel.builder().modelFile(
              prov.models().withExistingParent(ctx.getName() + "_four_way", prov.modLoc(dir + "_four_way"))
                .texture("0", mesh).texture("1", wall).texture("particle", wall)
            ).build();
            case 3: return ConfiguredModel.builder().modelFile(
              prov.models().withExistingParent(ctx.getName() + "_tri_way", prov.modLoc(dir + "_tri_way"))
                .texture("0", mesh).texture("1", wall).texture("particle", wall)
            ).rotationY(
              (north? (south? (east? 90: -90): 0): 180)
            ).build();
            case 2:
              if ((north && south) || (east && west)) {
                return ConfiguredModel.builder().modelFile(
                  prov.models().withExistingParent(ctx.getName() + "_straight", prov.modLoc(dir + "_straight"))
                    .texture("0", mesh).texture("1", wall).texture("particle", wall)
                ).rotationY(east?0:90).build();
              } else {
                return ConfiguredModel.builder().modelFile(
                  prov.models().withExistingParent(ctx.getName() + "_corner", prov.modLoc(dir + "_corner"))
                    .texture("0", mesh).texture("1", wall).texture("particle", wall)
                ).rotationY( (north? (east? 0: -90): (east? 90: 180))).build();
              }
            case 1: return ConfiguredModel.builder().modelFile(
              prov.models().withExistingParent(ctx.getName() + "_end", prov.modLoc(dir + "_end"))
                .texture("0", mesh).texture("1", wall).texture("particle", wall)
            ).rotationY( (north? -90: south? 90: east? 0: 180) ).build();
            case 0: // fall through
            default: return ConfiguredModel.builder().modelFile(
              prov.models().withExistingParent(ctx.getName() + "_post", prov.modLoc(dir + "_post"))
                .texture("0", mesh).texture("1", wall).texture("particle", wall)
            ).build();
          }
        });
      });
  }

  public static BlockBuilder<CatwalkBlock,?> buildCatwalk (Registrate reg, String metal) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk", CatwalkBlock::new)
      .initialProperties(Material.METAL)
      .properties(props->
        props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops().noOcclusion()
          .sound(SoundType.NETHERITE_BLOCK)
      )
      .addLayer(()-> RenderType::cutoutMipped)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
      .item(CatwalkBlockItem::new)
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      .model((ctx,prov)-> prov.withExistingParent(ctx.getName(), prov.mcLoc("block/template_trapdoor_bottom"))
        .texture("texture", prov.modLoc("block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk"))
      )
      .build()
      .recipe((ctx,prov)-> {
        if (metal.equals("Andesite")) {
          ShapedRecipeBuilder.shaped(ctx.get(), 3)
            .pattern(" p ")
            .pattern("pBp")
            .pattern(" p ")
            .define('p', AllItems.ANDESITE_ALLOY.get())
            .define('B', Registration.BAR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get())
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.ANDESITE_ALLOY.get()))
            .save(prov);
        }
        else {
          TagKey<Item> sheet = Registration.makeItemTag("plates/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_"));
          ShapedRecipeBuilder.shaped(ctx.get(), 3)
            .pattern(" p ")
            .pattern("pBp")
            .pattern(" p ")
            .define('p', sheet)
            .define('B', Registration.BAR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get())
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(sheet).build()))
            .save(prov);
        }
      })
      .blockstate((ctx,prov)-> {
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
      })
      .onRegister(CreateRegistrate.connectedTextures(
        new CatwalkCTBehaviour(SpriteShifts.CATWALK_TOPS.get(metal)).getSupplier()
      ));
  }
}
