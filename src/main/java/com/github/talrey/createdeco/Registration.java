package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Registration {
  public static ItemGroup itemGroup = new ItemGroup(CreateDecoMod.MODID) {
    @Override
    public ItemStack createIcon() { return new ItemStack(Blocks.BRICKS); } //BRICK_BLOCK.get(DyeColor.BLUE).get()); }
  };

  private static HashMap<DyeColor, String> BRICK_COLOR_NAMES                     = new HashMap<>();
  private static ArrayList<String> COIN_TYPES                                    = new ArrayList<>();
  private static HashMap<String,
    com.simibubi.create.repack.registrate.util.entry.ItemEntry<Item>> DOOR_TYPES = new HashMap<>();
  private static HashMap<String, Function<String, Item>> BAR_TYPES               = new HashMap<>();

  public static HashMap<DyeColor, BlockEntry<Block>> BRICK_BLOCK         = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> TILE_BRICK_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> LONG_BRICK_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> SHORT_BRICK_BLOCK   = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_BRICK_BLOCK = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_TILE_BLOCK  = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_LONG_BLOCK  = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> CRACKED_SHORT_BLOCK = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_BRICK_BLOCK   = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_TILE_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_LONG_BLOCK    = new HashMap<>();
  public static HashMap<DyeColor, BlockEntry<Block>> MOSSY_SHORT_BLOCK   = new HashMap<>();
  public static BlockEntry<Block> WORN_BRICK;

  public static HashMap<String, BlockEntry<CoinStackBlock>> COIN_BLOCKS  = new HashMap<>();
  public static HashMap<String, BlockEntry<DoorBlock>> DOOR_BLOCKS       = new HashMap<>();
  public static HashMap<String, BlockEntry<PaneBlock>> BAR_BLOCKS        = new HashMap<>();
  public static HashMap<String, BlockEntry<PaneBlock>> BAR_PANEL_BLOCKS  = new HashMap<>();

  public static HashMap<DyeColor, ItemEntry<Item>> BRICK_ITEM            = new HashMap<>();
  public static ItemEntry<Item> WORN_BRICK_ITEM;
  public static HashMap<String, ItemEntry<Item>> COIN_ITEM               = new HashMap<>();
  public static HashMap<String, ItemEntry<CoinStackItem>> COINSTACK_ITEM = new HashMap<>();

  public Registration () {
    BRICK_COLOR_NAMES.put(DyeColor.BLACK, "Dusk");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_GRAY, "Pearl");
    BRICK_COLOR_NAMES.put(DyeColor.RED, "Red");
    BRICK_COLOR_NAMES.put(DyeColor.YELLOW, "Dean");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_BLUE, "Blue");
    BRICK_COLOR_NAMES.put(null, "Worn"); // this is funky but it works, I swear.

    COIN_TYPES.add("Zinc");
    COIN_TYPES.add("Copper");
    COIN_TYPES.add("Brass");
    COIN_TYPES.add("Iron");
    COIN_TYPES.add("Gold");
    COIN_TYPES.add("Netherite");

    DOOR_TYPES.put("Andesite", AllItems.ANDESITE_ALLOY);
    DOOR_TYPES.put("Copper",   AllItems.COPPER_INGOT);
    DOOR_TYPES.put("Zinc",     AllItems.ZINC_INGOT);
    DOOR_TYPES.put("Brass",    AllItems.BRASS_INGOT);

    BAR_TYPES.put("Andesite",  (str) -> AllItems.ANDESITE_ALLOY.get());
    BAR_TYPES.put("Zinc",      (str) -> AllItems.ZINC_INGOT.get());
    BAR_TYPES.put("Copper",    (str) -> AllItems.COPPER_INGOT.get());
    BAR_TYPES.put("Brass",     (str) -> AllItems.BRASS_INGOT.get());
    BAR_TYPES.put("Iron",      (str) -> Items.IRON_INGOT);
    BAR_TYPES.put("Gold",      (str) -> Items.GOLD_INGOT);
    BAR_TYPES.put("Netherite", (str) -> Items.NETHERITE_INGOT);
  }

  private static BlockBuilder<Block,?> buildBrick (Registrate reg, DyeColor dye, String prefix, String name, String suffix) {
    String suf = suffix.replace(' ', '_').toLowerCase();
    String pre = prefix.replace(' ', '_').toLowerCase() + (prefix.equals("")?"":"_");
    BlockBuilder<Block,?> ret = reg.block(pre + name.toLowerCase() + "_" + suf, Block::new);
    if (dye != null) {
      ret.initialProperties(Material.ROCK, dye);
    } else {
      ret.initialProperties(Material.ROCK);
    }
    return ret.properties(props -> props.hardnessAndResistance(2,6).harvestTool(ToolType.PICKAXE).requiresTool())
      .blockstate((ctx,prov)-> prov.simpleBlock(ctx.get(), prov.models().cubeAll(ctx.getName(),
        prov.modLoc("block/palettes/bricks/" + name.toLowerCase() + "/" + pre + (name.equals("Red")?"":name.toLowerCase()+"_") + suf)
      )))
      .lang(prefix + (prefix.equals("")?"":" ") + name + " " + suffix)
      .defaultLoot()
      .simpleItem();
  }

  private static BlockBuilder<PaneBlock,?> buildBars (Registrate reg, String metal, Function<String,Item> getter, String suffix) {
    String base = metal.toLowerCase() + "_bars";
    String suf  = suffix.equals("") ? "" : "_" + suffix.replace(' ','_').toLowerCase();
    String post = "block/palettes/metal_bars/" + base + (metal == "Brass" || metal == "Netherite" ? "_post" : "");
    return reg.block(base + suf, PaneBlock::new)
      .properties(props -> props.nonOpaque().hardnessAndResistance(5, 6))
      .blockstate((ctx, prov) -> {
        MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

        BlockModelBuilder sideModel = prov.models().withExistingParent(
          base + "_side", prov.mcLoc("block/iron_bars_side"))
          .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base))
          .texture("edge", prov.modLoc(post));
        BlockModelBuilder sideAltModel = prov.models().withExistingParent(
          base + "_side_alt", prov.mcLoc("block/iron_bars_side_alt"))
          .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base))
          .texture("edge", prov.modLoc(post));

        builder.part().modelFile(prov.models().withExistingParent(base + "_post", prov.mcLoc("block/iron_bars_post"))
          .texture("bars", prov.modLoc(post))
        ).addModel()
          .condition(BlockStateProperties.NORTH, false)
          .condition(BlockStateProperties.SOUTH, false)
          .condition(BlockStateProperties.EAST, false)
          .condition(BlockStateProperties.WEST, false)
          .end();
        builder.part().modelFile(
          prov.models().withExistingParent(base + "_post_ends", prov.mcLoc("block/iron_bars_post_ends"))
            .texture("edge", prov.modLoc(post))
        ).addModel().end();
          builder.part().modelFile(sideModel).addModel().condition(BlockStateProperties.NORTH, true).end();
          builder.part().modelFile(sideModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
          builder.part().modelFile(sideAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
          builder.part().modelFile(sideAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();

          if (!suf.equals("")) {
            BlockModelBuilder sideOverlayModel = prov.models().withExistingParent(
              base + suf, prov.mcLoc("block/iron_bars_side"))
              .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base + suf))
              .texture("edge", prov.modLoc(post));
            BlockModelBuilder sideOverlayAltModel = prov.models().withExistingParent(
              base + suf + "_alt", prov.mcLoc("block/iron_bars_side_alt"))
              .texture("bars", prov.modLoc("block/palettes/metal_bars/" + base + suf))
              .texture("edge", prov.modLoc(post));

            builder.part().modelFile(sideOverlayModel).addModel().condition(BlockStateProperties.NORTH, true).end();
            builder.part().modelFile(sideOverlayModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
            builder.part().modelFile(sideOverlayAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
            builder.part().modelFile(sideOverlayAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();
          }
      })
      .lang(metal + " Bars" + (suffix.equals("")?"":" " + suffix))
      .item()
        .model((ctx, prov) -> {
          if (suf.equals("")) {
            prov.singleTexture(base, prov.mcLoc("item/generated"),"layer0", prov.modLoc("block/palettes/metal_bars/" + base));
          }
          else {
            prov.withExistingParent(base + suf, prov.mcLoc("item/generated"))
              .texture("layer0", prov.modLoc("block/palettes/metal_bars/" + base))
              .texture("layer1", prov.modLoc("block/palettes/metal_bars/" + base + suf));
          }
        })
        .build();
  }

  public static void registerBlocks (Registrate reg) {
    //reg.itemGroup(()->itemGroup, CreateDecoMod.MODID);

    BRICK_COLOR_NAMES.forEach((dye, name)-> {
      BRICK_BLOCK.put(dye,         buildBrick(reg, dye, "", name, "Bricks")
        .recipe((ctx,prov)-> ShapedRecipeBuilder.shapedRecipe(ctx.get())
          .patternLine("bb")
          .patternLine("bb")
          .key('b', BRICK_ITEM.get(dye).get())
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(BRICK_ITEM.get(dye).get()))
          .build(prov)
        ).register());
      TILE_BRICK_BLOCK.put(dye,    buildBrick(reg, dye, "", name, "Brick Tiles").register());
      LONG_BRICK_BLOCK.put(dye,    buildBrick(reg, dye, "", name, "Long Bricks").register());
      SHORT_BRICK_BLOCK.put(dye,   buildBrick(reg, dye, "", name, "Short Bricks").register());
      CRACKED_BRICK_BLOCK.put(dye, buildBrick(reg, dye, "Cracked", name, "Bricks").register());
      CRACKED_TILE_BLOCK.put(dye,  buildBrick(reg, dye, "Cracked", name, "Brick Tiles").register());
      CRACKED_LONG_BLOCK.put(dye,  buildBrick(reg, dye, "Cracked", name, "Long Bricks").register());
      CRACKED_SHORT_BLOCK.put(dye, buildBrick(reg, dye, "Cracked", name, "Short Bricks").register());
      MOSSY_BRICK_BLOCK.put(dye,   buildBrick(reg, dye, "Mossy", name, "Bricks").register());
      MOSSY_TILE_BLOCK.put(dye,    buildBrick(reg, dye, "Mossy", name, "Brick Tiles").register());
      MOSSY_LONG_BLOCK.put(dye,    buildBrick(reg, dye, "Mossy", name, "Long Bricks").register());
      MOSSY_SHORT_BLOCK.put(dye,   buildBrick(reg, dye, "Mossy", name, "Short Bricks").register());
    });

    COIN_TYPES.forEach(metal ->
      COIN_BLOCKS.put(metal.toLowerCase(), reg.block(metal.toLowerCase()+"_coinstack_block", CoinStackBlock::new)
        .properties(props -> props.nonOpaque().hardnessAndResistance(0.5f))
        .blockstate((ctx,prov)-> prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
          int layer = state.get(BlockStateProperties.LAYERS_1_8);
          return ConfiguredModel.builder().modelFile(prov.models().withExistingParent(
            ctx.getName() + "_" + layer, prov.modLoc("block/layers_bottom_top_" + layer)
          )
          .texture("side",   prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_side"))
          .texture("bottom", prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_bottom"))
          .texture("top",    prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_top"))
        ).build(); }))
        .lang(metal + " Stack Block")
        .loot((table, block) -> {
          LootTable.Builder builder = LootTable.builder();
          LootPool.Builder pool     = LootPool.builder();
          for (int layer = 1; layer<=8; layer++) {
            pool.rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(COINSTACK_ITEM.get(metal.toLowerCase()).get())
              .acceptFunction(SetCount.builder(ConstantRange.of(layer)).acceptCondition(
                BlockStateProperty.builder(block).properties(StatePropertiesPredicate.Builder.create()
                  .exactMatch(BlockStateProperties.LAYERS_1_8, layer)
                )
              ))
            );
          }
          table.registerLootTable(block, builder.addLootPool(pool));
        })
        .register())
    );

    DOOR_TYPES.forEach((metal,ingot) ->
      DOOR_BLOCKS.put(metal.toLowerCase(), reg.block(metal.toLowerCase() + "_door", DoorBlock::new)
        .properties(props -> props.nonOpaque().hardnessAndResistance(5, 5))
        .blockstate((ctx, prov) -> prov.doorBlock(ctx.get(),
          prov.modLoc("block/" + metal.toLowerCase() + "_door_bottom"),
          prov.modLoc("block/" + metal.toLowerCase() + "_door_top"))
        )
        .lang(metal + " Door")
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get())
          .patternLine("mm")
          .patternLine("mm")
          .patternLine("mm")
          .key('m', ingot.get())
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(ingot.get()))
          .build(prov)
        )
        .item()
          .model((ctx,prov) ->
            prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
              "layer0", prov.modLoc("item/" + ctx.getName())
          ))
          .build()
        .register())
    );

    BAR_TYPES.forEach((metal,getter) -> {
      BAR_BLOCKS.put(metal, buildBars(reg, metal, getter, "")
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get(), 16)
          .patternLine("mmm")
          .patternLine("mmm")
          .key('m', getter.apply(metal))
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(getter.apply(metal)))
          .build(prov)
        )
        .register());
      BAR_PANEL_BLOCKS.put(metal, buildBars(reg, metal, getter, "Panel").register());
    });
  }

  public static void registerItems (Registrate reg) {
    reg.itemGroup(()->itemGroup, CreateDecoMod.MODID);

    BRICK_COLOR_NAMES.forEach((dye, name)-> {
      if (dye == null) {
        BRICK_ITEM.put(null, reg.item(name.toLowerCase() + "_brick", Item::new)
          .recipe((ctx,prov)-> prov.blasting(DataIngredient.items(Items.BRICK), ctx, 0.3f))
          .register());
      } else {
        BRICK_ITEM.put(dye, reg.item(name.toLowerCase() + "_brick", Item::new)
          .lang(name + " Brick")
          .recipe((ctx, prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get())
            .patternLine("bbb")
            .patternLine("bCb")
            .patternLine("bbb")
            .key('b', Items.BRICK)
            .key('C', DyeItem.getItem(dye))
          )
          .register());
      }
    });

    for (String metal : COIN_TYPES) {
      COIN_ITEM.put(metal.toLowerCase(), reg.item(metal.toLowerCase() + "_coin", Item::new)
        .lang(metal + " Coin")
        .register());
      COINSTACK_ITEM.put(metal.toLowerCase(), reg.item(metal.toLowerCase() + "_coinstack", CoinStackItem::new)
        .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapelessRecipe(ctx.get())
          .addIngredient(COIN_ITEM.get(metal.toLowerCase()).get(), 4)
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(COIN_ITEM.get(metal.toLowerCase()).get()))
          .build(prov)
        )
        .lang(metal + " Coinstack")
        .register());
    }
  }
}
