package com.github.talrey.createdeco.registry;

import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CatwalkStairBlock;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.Nullable;

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
    //try {
    if (metal.equals("Iron")) {
      barTexture = new ResourceLocation("minecraft", "block/iron_bars");
      postTexture = barTexture;
    }
    else {
      barTexture = new ResourceLocation(reg.getModid(), "block/palettes/metal_bars/" + base);
      postTexture = new ResourceLocation(reg.getModid(), post);
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

  public static BlockBuilder<DoorBlock,?> buildDoor (Registrate reg, String name, String path) { return buildDoor(reg, name, path, Material.HEAVY_METAL); }

  public static BlockBuilder<DoorBlock,?> buildDoor (Registrate reg, String name, String path, Material mat) {
    return reg.block(name, DoorBlock::new)
      .initialProperties(mat)
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
          TagKey<Item> sheet = Registration.makeItemTag(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_plates");
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
          TagKey<Item> sheet = Registration.makeItemTag(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_plates");
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

  public static BlockBuilder<CatwalkStairBlock,?> buildCatwalkStair (Registrate reg, String metal) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String texture = reg.getModid() + ":block/palettes/catwalks/" + regName + "_catwalk";
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk_stairs", CatwalkStairBlock::new)
        .initialProperties(Material.METAL)
        .properties(props->
          props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops().noOcclusion()
            .sound(SoundType.NETHERITE_BLOCK)
        )
        .addLayer(()-> RenderType::cutoutMipped)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
        .blockstate((ctx,prov)-> {
          BlockModelBuilder builder = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/catwalk_stairs"))
            .texture("2", texture + "_rail")
            .texture("3", texture + "_stairs")
            .texture("particle", texture  +"_rail");
          prov.horizontalBlock(ctx.get(), builder);
        })
        .recipe((ctx,prov)-> ShapedRecipeBuilder.shaped(ctx.get(), 2)
          .pattern(" c")
          .pattern("cb")
          .define('c', Registration.CATWALK_BLOCKS.get(regName).get())
          .define('b', Registration.BAR_BLOCKS.get(regName).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            Registration.CATWALK_BLOCKS.get(regName).get()
          ))
          .save(prov)
        )
        .simpleItem();
  }
}
