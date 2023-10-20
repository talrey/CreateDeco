package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.CatwalkBlockItem;
import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CatwalkStairBlock;
import com.github.talrey.createdeco.connected.CatwalkCTBehaviour;
import com.github.talrey.createdeco.connected.SpriteShifts;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Supplier;

public class Catwalks {
  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> recipe (
    String metal,
    ItemLike barItem,
    @Nullable Supplier<Item> nonstandardMaterial
  ) {
    return (ctx,prov)-> {
      if (nonstandardMaterial != null) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
          .pattern(" p ")
          .pattern("pBp")
          .pattern(" p ")
          .define('p', nonstandardMaterial.get())
          .define('B', barItem)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(nonstandardMaterial.get()))
          .save(prov);
      }
      else {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
          .pattern(" p ")
          .pattern("pBp")
          .pattern(" p ")
          .define('p', CDTags.of(metal, "plates").tag)
          .define('B', barItem)
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
          ))
          .save(prov, ctx.getName() + "_forge");
      }
    };
  }

  public static BlockBuilder<CatwalkBlock,?> build (
    CreateRegistrate reg, String metal, ItemLike barItem
  ) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk", CatwalkBlock::new)
      .properties(props->
        props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops().noOcclusion()
          .sound(SoundType.NETHERITE_BLOCK)
      )
      .addLayer(()-> RenderType::cutoutMipped)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
      .item(CatwalkBlockItem::new)
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      .model((ctx,prov)-> BlockStateGenerator.catwalkItem(metal, ctx, prov))
      .build()
      .recipe(recipe(metal, barItem, (metal == "Andesite" ? AllItems.ANDESITE_ALLOY : null)))
      .blockstate((ctx,prov)-> BlockStateGenerator.catwalk(reg, metal, ctx, prov))
      .onRegister(CreateRegistrate.connectedTextures(
        new CatwalkCTBehaviour(SpriteShifts.CATWALK_TOPS.get(metal)).getSupplier()
      ));
  }

  public static BlockBuilder<CatwalkStairBlock,?> buildStair (
    CreateRegistrate reg, String metal, ItemLike barItem
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String texture = reg.getModid() + ":block/palettes/catwalks/" + regName + "_catwalk";
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk_stairs", CatwalkStairBlock::new)
      .properties(props->
        props.strength(5, (metal.equals("Netherite")) ? 1200 : 6).requiresCorrectToolForDrops().noOcclusion()
          .sound(SoundType.NETHERITE_BLOCK)
      )
      .addLayer(()-> RenderType::cutoutMipped)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
      .blockstate((ctx,prov)-> BlockStateGenerator.catwalkStair(texture, ctx, prov))
      .recipe((ctx,prov)-> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 2)
        .pattern(" c")
        .pattern("cb")
        .define('c', BlockRegistry.CATWALKS.get(regName).get())
        .define('b', barItem)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
          BlockRegistry.CATWALKS.get(regName).get()
        ))
        .save(prov)
      )
      .simpleItem();
  }
}
