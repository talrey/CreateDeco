package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.github.talrey.createdeco.blocks.SupportWedgeBlock;
import com.github.talrey.createdeco.connected.ShippingContainerCTBehavior;
import com.github.talrey.createdeco.items.RailingBlockItem;
import com.github.talrey.createdeco.items.ShippingContainerBlockItem;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.logistics.vault.ItemVaultCTBehaviour;
import com.simibubi.create.content.logistics.vault.ItemVaultItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ShippingContainers {
  public static BlockBuilder<ShippingContainerBlock,?> build (
          CreateRegistrate reg, String color
  ) {
    String regName = color.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_shipping_container";

    return reg.block(regName, ShippingContainerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops
            )
            //.properties(p -> p.mapColor(DyeColor.valueOf(color)))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK)
                    .explosionResistance(1200))
            .transform(pickaxeOnly())
            .item(ShippingContainerBlockItem::new)
            .build()
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .lang(color + " Shipping Container")
            .blockstate((ctx, prov) -> BlockStateGenerator.shippingContainer(CreateDecoMod.REGISTRATE, DyeColor.byName(color, null), ctx, prov))
            .onRegister(connectedTextures(ShippingContainerCTBehavior::new));

  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> recipe (
          DyeColor color
  ) {
    return (ctx,prov)-> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
            .pattern("CSC")
            .pattern("SBS")
            .pattern("CSC")
            .define('S', AllItems.IRON_SHEET)
            .define('C', DyeItem.byColor(color))
            .define('B', Items.BARREL)
            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(AllItems.IRON_SHEET).build()
            ))
            .save(prov);
  }
}
