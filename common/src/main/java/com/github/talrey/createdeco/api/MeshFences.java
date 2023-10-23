package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.simibubi.create.AllItems;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Supplier;

public class MeshFences {
  private static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> fenceRecipe (
    String metal,
    @Nullable Supplier<Item> nonstandardMaterial
  ) {
    return (ctx,prov)-> {
      if (nonstandardMaterial != null) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
          .pattern("psp")
          .pattern("psp")
          .define('p', nonstandardMaterial.get())
          .define('s', Items.STRING)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            nonstandardMaterial.get()
          ))
          .save(prov);
      }
      else {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
          .pattern("psp")
          .pattern("psp")
          .define('p', CDTags.of(metal, "plates").tag)
          .define('s', Items.STRING)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
          ))
          .save(prov);
      }
    };
  }

  public static BlockBuilder<FenceBlock,?> build (CreateRegistrate reg, String metal) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_mesh_fence", FenceBlock::new)
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
      .recipe(fenceRecipe(metal, (metal == "Andesite" ? AllItems.ANDESITE_ALLOY : null)))
      .blockstate((ctx,prov)-> BlockStateGenerator.fence(metal, ctx, prov));
  }
}
