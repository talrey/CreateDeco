package com.github.talrey.createdeco.registry;

import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CatwalkStairBlock;
import com.github.talrey.createdeco.connected.CatwalkCTBehaviour;
import com.github.talrey.createdeco.connected.SpriteShifts;
import com.github.talrey.createdeco.items.CatwalkBlockItem;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class MetalDecoBuilders {

  public static BlockBuilder<IronBarsBlock,?> buildBars (Registrate reg, String metal, Function<String,Item> getter, String suffix) {
    return buildBars(reg, metal, getter, suffix, false);
  }

  public static BlockBuilder<IronBarsBlock,?> buildBars (Registrate reg, String metal, Function<String,Item> getter, String suffix, boolean doPost) {
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
      .blockstate((ctx, prov) -> PerLoaderRegistration.barBlockState(
        base, post, bartex, postex, ctx, prov
      ))
      .tag(BlockTags.WALLS)
      .addLayer(()-> RenderType::cutoutMipped)
      .item()
      .model((ctx, prov) -> {
        if (suf.isEmpty()) {
          prov.singleTexture(base, prov.mcLoc("item/generated"),"layer0", bartex);
        }
        else {
          prov.withExistingParent(base + suf, prov.mcLoc("item/generated"))
            .texture("layer0", bartex)
            .texture("layer1", prov.modLoc("block/palettes/metal_bars/" + base + suf));
        }
      })
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      .build();
  }

  public static BlockBuilder<DoorBlock,?> buildDoor (Registrate reg, String name, String path) {
    return buildDoor(reg, name, path, BlockSetType.GOLD);
  }

  public static BlockBuilder<DoorBlock,?> buildDoor (Registrate reg, String name, String path, BlockSetType material) {
    return reg.block(name, p -> new DoorBlock(p, material))
      .initialProperties(()->Blocks.IRON_DOOR)
      .properties(props -> props.noOcclusion().strength(5, 5).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .blockstate((ctx, prov)-> prov.doorBlock(ctx.get(),
        prov.modLoc(path + "_door_bottom"),
        prov.modLoc(path + "_door_top"))
      )
      .addLayer(()-> RenderType::cutoutMipped)
      .loot((table, block)-> {
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder pool     = LootPool.lootPool();
        pool.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block))
          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
            .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
            ));
        table.add(block, builder.withPool(pool));
      })
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(BlockTags.DOORS)
      .item()
      .model((ctx, prov)-> prov.singleTexture(ctx.getName(), prov.mcLoc("item/generated"),
        "layer0", prov.modLoc("item/" + ctx.getName())
      ))
      .properties(props -> (name.contains("Netherite") ? props.fireResistant() : props))
      .build();
  }

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

  public static BlockBuilder<FenceBlock,?> buildFence (Registrate reg, String metal) {
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
      .blockstate((ctx,prov)-> PerLoaderRegistration.fenceBlockState(metal, ctx, prov));
  }

  private static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider> catwalkRecipe (
    String metal,
    @Nullable Supplier<Item> nonstandardMaterial
  ) {
    return (ctx,prov)-> {
      if (nonstandardMaterial != null) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
          .pattern(" p ")
          .pattern("pBp")
          .pattern(" p ")
          .define('p', nonstandardMaterial.get())
          .define('B', Registration.BAR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(nonstandardMaterial.get()))
          .save(prov);
      }
      else {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
          .pattern(" p ")
          .pattern("pBp")
          .pattern(" p ")
          .define('p', CDTags.of(metal, "plates").tag)
          .define('B', Registration.BAR_BLOCKS.get(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
          ))
          .save(prov, ctx.getName() + "_forge");
      }
    };
  }

  public static BlockBuilder<CatwalkBlock,?> buildCatwalk (Registrate reg, String metal) {
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
      .model((ctx,prov)-> prov.withExistingParent(ctx.getName(), prov.mcLoc("block/template_trapdoor_bottom"))
        .texture("texture", prov.modLoc("block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk"))
      )
      .build()
      .recipe(catwalkRecipe(metal, (metal == "Andesite" ? AllItems.ANDESITE_ALLOY : null)))
      .blockstate((ctx,prov)-> PerLoaderRegistration.catwalkBlockState(reg, metal, ctx, prov))
      .onRegister(CreateRegistrate.connectedTextures(
        new CatwalkCTBehaviour(SpriteShifts.CATWALK_TOPS.get(metal)).getSupplier()
      ));
  }

  public static BlockBuilder<CatwalkStairBlock,?> buildCatwalkStair (Registrate reg, String metal) {
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
        .blockstate((ctx,prov)-> PerLoaderRegistration.catwalkStairBlockState(texture, ctx, prov))
        .recipe((ctx,prov)-> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 2)
          .pattern(" c")
          .pattern("cb")
          .define('c', Registration.CATWALK_BLOCKS.get(regName).get())
          .define('b', Registration.BAR_BLOCKS.get(regName).get())
          .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            Registration.CATWALK_BLOCKS.get(regName).get()
          ))
          .save(prov)
        )
        .simpleItem();
  }
}
