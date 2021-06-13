package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.HashMap;

public class Registration {
  public static ItemGroup itemGroup = new ItemGroup(CreateDecoMod.MODID) {
    @Override
    public ItemStack createIcon() { return new ItemStack(Blocks.BRICKS); } //BRICK_BLOCK.get(DyeColor.BLUE).get()); }
  };

  private static HashMap<DyeColor, String> BRICK_COLOR_NAMES = new HashMap<>();
  private static ArrayList<String> COIN_TYPES = new ArrayList<>();

  public static HashMap<DyeColor, BlockEntry<Block>> BRICK_BLOCK         = new HashMap<>();
  public static BlockEntry<Block> WORN_BRICK;
  public static HashMap<String, BlockEntry<CoinStackBlock>> COIN_BLOCKS  = new HashMap<>();

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
  }

  public static void registerBlocks (Registrate reg) {
    //reg.itemGroup(()->itemGroup, CreateDecoMod.MODID);

    BRICK_COLOR_NAMES.forEach((dye, name)->
      BRICK_BLOCK.put(dye, reg.block(name.toLowerCase() + "_bricks", Block::new)
      .initialProperties(Material.ROCK, dye)
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
        .blockstate((ctx,prov)-> prov.simpleBlock(ctx.get(), prov.models().cubeBottomTop(ctx.getName(),
          prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_side"),
          prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_bottom"),
          prov.modLoc("block/" + metal.toLowerCase() + "_coinstack_top")
        )))
        .lang(metal + " Stack Block")
        .loot((table, block) -> {
          LootTable.Builder builder = LootTable.builder();
          LootPool.Builder pool     = LootPool.builder();
          for (int layer = 1; layer<=8; layer++) {
            pool.rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block)
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
