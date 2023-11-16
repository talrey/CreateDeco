package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.BlockStateGenerator;
import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.blocks.CageLampBlock;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Locale;
import java.util.function.Supplier;

public class CageLamps {

  public static final ResourceLocation YELLOW_ON  = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_default");
  public static final ResourceLocation YELLOW_OFF = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_default_off");
  public static final ResourceLocation RED_ON     = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_redstone");
  public static final ResourceLocation RED_OFF    = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_redstone_off");
  public static final ResourceLocation GREEN_ON   = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_green");
  public static final ResourceLocation GREEN_OFF  = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_green_off");
  public static final ResourceLocation BLUE_ON    = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_soul");
  public static final ResourceLocation BLUE_OFF   = new ResourceLocation(CreateDecoMod.MOD_ID, "block/palettes/cage_lamp/light_soul_off");

  private static ShapedRecipeBuilder cageLampRecipeBuilder (
    ItemLike item, Supplier<Item> light
  ) {
    return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item)
      .pattern("n")
      .pattern("t")
      .pattern("p")
      .define('t', light.get());
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> recipe (
    String metal,
    Supplier<Item> light
  ) {
    return recipe(metal, light, null);
  }

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> recipe (
    String metalName,
    Supplier<Item> light,
    @Nullable Supplier<Item> nonstandardMaterial
  ) {
    String metal = metalName.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    return (ctx, prov) -> {
      if (nonstandardMaterial != null) {
        cageLampRecipeBuilder(ctx.get(), light).unlockedBy("has_item",
            InventoryChangeTrigger.TriggerInstance.hasItems(nonstandardMaterial.get())
          )
          .define('n', nonstandardMaterial.get())
          .define('p', CDTags.of(metal, "plates").tag)
          .save(prov);
      }
      else {
        cageLampRecipeBuilder(ctx.get(), light).unlockedBy("has_item",
            InventoryChangeTrigger.TriggerInstance.hasItems(
              ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
            ))
          .define('n', CDTags.of(metal, "nuggets").tag)
          .define('p', CDTags.of(metal, "plates").tag)
          .save(prov);
      }
    };
  }

  public static BlockBuilder<CageLampBlock, ?> build (
    CreateRegistrate reg, String name, DyeColor color, ResourceLocation cage, ResourceLocation lampOn, ResourceLocation lampOff
  ) {
    return reg.block(color.getName().toLowerCase(Locale.ROOT) + "_" + name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_lamp",
        (p)-> new CageLampBlock(p, new Vector3f(0.3f, 0.3f, 0f)))
      .properties(props-> props.noOcclusion().strength(0.5f).sound(SoundType.LANTERN).lightLevel((state)-> state.getValue(BlockStateProperties.LIT)?15:0))
      .blockstate((ctx,prov)-> BlockStateGenerator.cageLamp(cage, lampOn, lampOff, ctx, prov))
      .addLayer(()-> RenderType::cutoutMipped)
      .lang(color.name().charAt(0) + color.name().substring(1).toLowerCase() + " " + name + " Cage Lamp")
      .simpleItem();
  }
}
