package com.github.talrey.createdeco;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class Registration {
  public static ItemGroup itemGroup = new ItemGroup(CreateDecoMod.MODID) {
    @Override
    public ItemStack createIcon() { return new ItemStack(Items.MINECART); }
  };

  private static HashMap<DyeColor, String> BRICK_COLOR_NAMES =  new HashMap<>();

  public static HashMap<DyeColor, BlockEntry<Block>> BRICK_BLOCK = new HashMap<>();
  public static BlockEntry<Block> WORN_BRICK;

  public static HashMap<DyeColor, ItemEntry<Item>> BRICK_ITEM = new HashMap<>();

  public Registration () {
    BRICK_COLOR_NAMES.put(DyeColor.BLACK, "Dusk");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_GRAY, "Pearl");
    BRICK_COLOR_NAMES.put(DyeColor.RED, "Red");
    BRICK_COLOR_NAMES.put(DyeColor.YELLOW, "Dean");
    BRICK_COLOR_NAMES.put(DyeColor.LIGHT_BLUE, "Blue");
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
        .key('b', ()->BRICK_ITEM.get(dye).get())
      )
      .defaultLoot()
      .simpleItem()
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
  }


}
