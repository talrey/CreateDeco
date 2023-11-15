package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.blocks.DecalBlock;
import com.github.talrey.createdeco.blocks.SupportWedgeBlock;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class Decals {
  public static List<String> TYPES = Arrays.asList(
      "radioactive", "biohazard", "flammable", "toxic", "explosive", "high_voltage", "caution"
  );
  private static List<String> CAPITALS = Arrays.asList(
      "Radioactive", "Biohazard", "Flammable", "Toxic", "Explosive", "High Voltage", "Caution"
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
              .sound(SoundType.CANDLE)
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
