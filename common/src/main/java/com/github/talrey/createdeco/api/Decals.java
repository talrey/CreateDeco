package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.blocks.DecalBlock;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.PressingRecipeGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decals {
  public static List<String> TYPES = Arrays.asList(
      "warning", "creeper", "skull", "flow", "ice", "radioactive",
      "top_left", "up", "top_right", "left", "cross", "right", "down_left", "down", "down_right",
      "fluid", "fire", "electrical",
      "fire_diamond", "no_entry"

  );
  private static List<String> CAPITALS = Arrays.asList(
      "Warning", "Creeper", "Skull", "Flow", "Ice", "Radioactive",
      "Up Left Arrow", "Up Arrow", "Up Right Arrow", "Left Arrow", "Cross", "Right Arrow", "Down Left Arrow", "Down Arrow", "Down Right Arrow",
      "Fluid", "Fire", "Electrical",
      "Fire Diamond", "No Entry"

  );

  public static ArrayList<BlockBuilder<DecalBlock,?>> build (CreateRegistrate reg) {
    String name;
    ArrayList<BlockBuilder<DecalBlock,?>> ret = new ArrayList<>();
    for (String prefix : TYPES) {
      name = "decal_" + prefix;
      ret.add(reg.block(name, DecalBlock::new)
          //.initialProperties(()-> Blocks.BRICKS)
          .properties(props -> props
              .strength(2,2)
              .sound(SoundType.LANTERN)
              .noOcclusion()
              .isViewBlocking((a, b, c) -> false)
              .isSuffocating((a, b, c) -> false)
              .instabreak()
          )
          .blockstate((ctx, prov) -> BlockStateGenerator.decal(reg, prefix, ctx, prov))
          .lang(CAPITALS.get(TYPES.indexOf(prefix)) + " Decal")
          .defaultLoot()
          .recipe(Decals::recipeStonecutting)
          .simpleItem()
      );
    }
    return ret;
  }

  public static <T extends Block> void recipeStonecutting (
      DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    SingleItemRecipeBuilder.stonecutting(Ingredient.of(AllItems.IRON_SHEET), RecipeCategory.DECORATIONS, ctx.get(), 1)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(AllItems.IRON_SHEET).build()
        ))
        .save(prov);
  }
}
