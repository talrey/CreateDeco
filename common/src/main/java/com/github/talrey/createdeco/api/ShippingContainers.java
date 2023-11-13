package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.github.talrey.createdeco.connected.ShippingContainerCTBehavior;
import com.github.talrey.createdeco.items.ShippingContainerBlockItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ShippingContainers {
  public static BlockBuilder<ShippingContainerBlock,?> build (
    CreateRegistrate reg, DyeColor color
  ) {
    String regName = color.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_shipping_container";

    return reg.block(regName, p -> new ShippingContainerBlock(p, color))
      .initialProperties(SharedProperties::softMetal)
      .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
//      .properties(p -> p.mapColor(DyeColor.valueOf(color)))
      .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).explosionResistance(1200))
      .transform(pickaxeOnly())
      .item(ShippingContainerBlockItem::new)
        .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(color + " Shipping Container")
      .blockstate((ctx, prov) -> BlockStateGenerator.shippingContainer(CreateDecoMod.REGISTRATE, color, ctx, prov))
      .onRegister(connectedTextures(ShippingContainerCTBehavior::new));
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> recipe (
    DyeColor color
  ) {
    return (ctx,prov)-> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 1)
      .pattern("CS")
      .pattern("SB")
      .define('S', AllItems.IRON_SHEET)
      .define('C', DyeItem.byColor(color))
      .define('B', Items.BARREL)
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
        ItemPredicate.Builder.item().of(AllItems.IRON_SHEET).build()
      ))
      .save(prov);
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> redyeRecipe (
    DyeColor color
  ) {
    return (ctx,prov)-> ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
      .requires(DyeItem.byColor(color))
      .requires(AllBlocks.ITEM_VAULT.asItem())
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
        ItemPredicate.Builder.item().of(AllBlocks.ITEM_VAULT.asItem()).build()
      ))
      .save(prov);
  }
}
