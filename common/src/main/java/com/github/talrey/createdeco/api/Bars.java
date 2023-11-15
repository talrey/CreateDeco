package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;
import java.util.function.Supplier;

public class Bars {
  public static BlockBuilder<IronBarsBlock, ?> build (
      CreateRegistrate reg, String metal, String suffix, boolean doPost
  ) {
    String base = metal.replace(' ', '_').toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_bars";
    String suf = suffix.equals("") ? "" : "_" + suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String post = "block/palettes/metal_bars/" + base + (doPost ? "_post" : "");

    ResourceLocation barTexture, postTexture;
    final ResourceLocation bartex, postex;
    //try {
    if (metal.equals("Iron")) {
      barTexture = new ResourceLocation("minecraft", "block/iron_bars");
      postTexture = barTexture;
    }
    else {
      barTexture = new ResourceLocation(reg.getModid(), "block/palettes/metal_bars/" + base);
      postTexture = new ResourceLocation(reg.getModid(), post);
    }

    // for lambda stuff, must be final
    bartex = barTexture;
    postex = postTexture;

    return reg.block(base + suf, IronBarsBlock::new)
        .properties(props -> props.noOcclusion().strength(5, (metal.equals("Netherite")) ? 1200 : 6)
            .sound(SoundType.NETHERITE_BLOCK))
        .blockstate((ctx, prov)-> BlockStateGenerator.bar(base, suf, bartex, postex, ctx, prov))
        .tag(BlockTags.WALLS)
        .addLayer(()-> RenderType::cutoutMipped)
        .item()
        .model((ctx, prov) -> BlockStateGenerator.barItem(base, suf, bartex, ctx, prov))
        .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
        .build();
  }

  public static <T extends Block> void recipeStonecutting (
      Supplier<Item> ingot, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingot.get()), RecipeCategory.DECORATIONS, ctx.get(), 1)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(ingot.get()).build()
        ))
        .save(prov);

  }
}
