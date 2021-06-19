package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.mojang.datafixers.TypeRewriteRule;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.Registrate;
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
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelProvider;
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
  public static BlockEntry<Block> WORN_BRICK;
  public static HashMap<String, BlockEntry<CoinStackBlock>> COIN_BLOCKS  = new HashMap<>();
  public static HashMap<String, BlockEntry<DoorBlock>> DOOR_BLOCKS       = new HashMap<>();
  public static HashMap<String, BlockEntry<PaneBlock>> BAR_BLOCKS        = new HashMap<>();
  public static HashMap<String, BlockEntry<PaneBlock>> BAR_PANEL_BLOCKS  = new HashMap<>();

  public static HashMap<DyeColor, ItemEntry<Item>> BRICK_ITEM            = new HashMap<>();
  public static HashMap<String, ItemEntry<Item>> COIN_ITEM               = new HashMap<>();
  public static HashMap<String, ItemEntry<CoinStackItem>> COINSTACK_ITEM = new HashMap<>();

  public Registration () {
    BRICK_COLOR_NAMES.put(DyeColor.BLACK, "Dusk");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_GRAY, "Pearl");
    BRICK_COLOR_NAMES.put(DyeColor.RED, "Red");
    BRICK_COLOR_NAMES.put(DyeColor.YELLOW, "Dean");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_BLUE, "Blue");

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

  public static void registerBlocks (Registrate reg) {
    //reg.itemGroup(()->itemGroup, CreateDecoMod.MODID);

    BRICK_COLOR_NAMES.forEach((dye, name)->
      BRICK_BLOCK.put(dye, reg.block(name.toLowerCase() + "_bricks", Block::new)
      .initialProperties(Material.ROCK, dye)
      .properties(props -> props.hardnessAndResistance(2,6).harvestTool(ToolType.PICKAXE).requiresTool())
      .blockstate((ctx,prov) -> prov.simpleBlock(ctx.get(), prov.models().cubeAll(
        ctx.getName(), prov.modLoc("block/palettes/bricks/" + name.toLowerCase() + "/" + (name.equals("Red") ? "" : name.toLowerCase()+"_") + "bricks")
      )))
      .lang(name + " Bricks")
      .recipe((ctx, prov)-> ShapedRecipeBuilder.shapedRecipe(ctx.get())
        .patternLine("bb")
        .patternLine("bb")
        .key('b', BRICK_ITEM.get(dye).get())
        .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(BRICK_ITEM.get(dye).get()))
        .build(prov)
      )
      .defaultLoot()
      .simpleItem()
      .register())
    );

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

    BAR_TYPES.forEach((metal,getter) ->
      BAR_BLOCKS.put(metal.toLowerCase(), reg.block(metal.toLowerCase() + "_bars", PaneBlock::new)
        .properties(props -> props.nonOpaque().hardnessAndResistance(5, 6))
        /*
        .blockstate((ctx,prov) -> prov.paneBlock(ctx.get(),
          prov.models().panePost(ctx.getName()+"_post",
            prov.modLoc("block/palettes/metal_bars/" + ctx.getName()),
            prov.modLoc("block/palettes/metal_bars/" + ctx.getName() + (metal == "Brass" || metal == "Netherite"? "_top" : ""))
          ),
          prov.models().paneSide(ctx.getName()+"_side",
            prov.modLoc("block/palettes/metal_bars/" + ctx.getName()),
            prov.modLoc("block/palettes/metal_bars/" + ctx.getName() + (metal == "Brass" || metal == "Netherite"? "_top" : ""))
          ),
          prov.models().paneSideAlt(ctx.getName()+"_side_alt",
            prov.modLoc("block/palettes/metal_bars/" + ctx.getName()),
            prov.modLoc("block/palettes/metal_bars/" + ctx.getName() + (metal == "Brass" || metal == "Netherite"? "_top" : ""))
          ),
          prov.models().paneNoSide(ctx.getName()+"_noside", prov.modLoc("block/palettes/metal_bars/" + ctx.getName())),
          prov.models().paneNoSideAlt(ctx.getName()+"_noside_alt", prov.modLoc("block/palettes/metal_bars/" + ctx.getName()))
        ))
         */
        .blockstate((ctx,prov) -> {
          MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

          BlockModelBuilder sideModel = prov.models().withExistingParent(
            ctx.getName()+"_side", prov.mcLoc("block/iron_bars_side"))
            .texture("bars", prov.modLoc("block/palettes/metal_bars/" + ctx.getName()))
            .texture("edge", prov.modLoc("block/palettes/metal_bars/" + ctx.getName() + (metal=="Brass"||metal=="Netherite"?"_post":"")));
          BlockModelBuilder sideAltModel = prov.models().withExistingParent(
            ctx.getName()+"_side_alt", prov.mcLoc("block/iron_bars_side_alt"))
            .texture("bars", prov.modLoc("block/palettes/metal_bars/" + ctx.getName()))
            .texture("edge", prov.modLoc("block/palettes/metal_bars/" + ctx.getName() + (metal=="Brass"||metal=="Netherite"?"_post":"")));

          builder.part().modelFile(
            prov.models().withExistingParent(ctx.getName()+"_post", prov.mcLoc("block/iron_bars_post"))
            .texture("bars", prov.modLoc("block/palettes/metal_bars/" + ctx.getName() + (metal=="Brass"||metal=="Netherite"?"_post":"")))
          ).addModel()
            .condition(BlockStateProperties.NORTH, false)
            .condition(BlockStateProperties.SOUTH, false)
            .condition(BlockStateProperties.EAST, false)
            .condition(BlockStateProperties.WEST, false)
          .end();
          builder.part().modelFile(
            prov.models().withExistingParent(
          ctx.getName()+"_post_ends", prov.mcLoc("block/iron_bars_post_ends"))
            .texture("edge", prov.modLoc("block/palettes/metal_bars/" + ctx.getName() + (metal=="Brass"||metal=="Netherite"?"_post":"")))
          ).addModel().end();
          builder.part().modelFile(sideModel).addModel().condition(BlockStateProperties.NORTH, true).end();
          builder.part().modelFile(sideModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end();
          builder.part().modelFile(sideAltModel).addModel().condition(BlockStateProperties.SOUTH, true).end();
          builder.part().modelFile(sideAltModel).rotationY(90).addModel().condition(BlockStateProperties.WEST, true).end();
        //  prov.fourWayMultipart(builder,
        //    prov.models().withExistingParent(ctx.getName()+"_side", prov.mcLoc("block/iron_bars_side"))
        //    .texture("bars", prov.modLoc("block/palettes/metal_bars/" + ctx.getName()))
        //    .texture("edge", prov.modLoc("block/palettes/metal_bars/" + ctx.getName()))
        //  );
        })
        .lang(metal + " Bars")
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shapedRecipe(ctx.get(), 16)
          .patternLine("mmm")
          .patternLine("mmm")
          .key('m', getter.apply(metal))
          .addCriterion("has_item", InventoryChangeTrigger.Instance.forItems(getter.apply(metal)))
          .build(prov)
        )
        .item()
          .model((ctx,prov) ->
            prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
              "layer0", prov.modLoc("block/palettes/metal_bars/" + ctx.getName())
          ))
          .build()
        .register())
    );
  }

  public static void registerItems (Registrate reg) {
    reg.itemGroup(()->itemGroup, CreateDecoMod.MODID);

    BRICK_COLOR_NAMES.forEach((dye, name)->
      BRICK_ITEM.put(dye, reg.item(name.toLowerCase() + "_brick", Item::new)
        .lang(name + " Brick")
        .register())
    );

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
