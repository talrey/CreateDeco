package com.github.talrey.createdeco;

import com.github.talrey.createdeco.api.*;
import com.github.talrey.createdeco.blocks.*;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.placard.PlacardBlock;
import com.simibubi.create.content.decoration.placard.PlacardRenderer;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.talrey.createdeco.api.CageLamps.*;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class BlockRegistry {
	public static BlockEntry<Block> CAST_IRON;

	public static HashMap<DyeColor, String> BRICK_COLORS = new HashMap<>() {{
		put(DyeColor.LIGHT_BLUE, "blue");
		put(DyeColor.YELLOW, "dean");
		put(DyeColor.BLACK, "dusk");
		put(DyeColor.WHITE, "pearl");
		put(DyeColor.RED, "scarlet");
		put(null, "red");
	}};

	public static HashMap<DyeColor, HashMap<String, BlockEntry<Block>>>      BRICKS = new HashMap<>();
	public static HashMap<DyeColor, HashMap<String, BlockEntry<StairBlock>>> STAIRS = new HashMap<>();
	public static HashMap<DyeColor, HashMap<String, BlockEntry<SlabBlock>>>   SLABS = new HashMap<>();

	public static HashMap<String, BlockEntry<Block>>      WORN_BRICKS = new HashMap<>();
	public static HashMap<String, BlockEntry<StairBlock>> WORN_STAIRS = new HashMap<>();
	public static HashMap<String, BlockEntry<SlabBlock>>   WORN_SLABS = new HashMap<>();

	public static HashMap<String, BlockEntry<CageLampBlock>> YELLOW_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>    RED_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>  GREEN_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>   BLUE_CAGE_LAMPS = new HashMap<>();

	public static HashMap<String, BlockEntry<DoorBlock>> DOORS          = new HashMap<>();
	public static HashMap<String, BlockEntry<DoorBlock>> LOCK_DOORS     = new HashMap<>();
	public static HashMap<String, BlockEntry<TrapDoorBlock>> TRAPDOORS  = new HashMap<>();
	public static HashMap<String, BlockEntry<IronBarsBlock>> BARS       = new HashMap<>();
	public static HashMap<String, BlockEntry<IronBarsBlock>> BAR_PANELS = new HashMap<>();
	public static HashMap<String, BlockEntry<FenceBlock>> MESH_FENCES   = new HashMap<>();

	public static HashMap<String, BlockEntry<CatwalkBlock>> CATWALKS                = new HashMap<>();
	public static HashMap<String, BlockEntry<CatwalkStairBlock>> CATWALK_STAIRS     = new HashMap<>();
	public static HashMap<String, BlockEntry<CatwalkRailingBlock>> CATWALK_RAILINGS = new HashMap<>();

	public static HashMap<String, BlockEntry<HullBlock>> HULLS       = new HashMap<>();
	public static HashMap<String, BlockEntry<SupportBlock>> SUPPORTS = new HashMap<>();

	public static HashMap<DyeColor, BlockEntry<? extends PlacardBlock>> PLACARDS = new HashMap<>();
	public static BlockEntityEntry<DyedPlacardBlock.Entity> PLACARD_ENTITY;

	public static HashMap<String, BlockEntry<CoinStackBlock>> COIN_BLOCKS  = new HashMap<>();

	public static void init() {
		// load the class and register everything
		CreateDecoMod.LOGGER.info("Registering blocks for " + CreateDecoMod.NAME);

		// Props registration
		CreateDecoMod.REGISTRATE.defaultCreativeTab(CreativeTabs.PROPS_KEY);

		CAST_IRON = CreateDecoMod.REGISTRATE.block("cast_iron_block", Block::new)
			.properties(props->
				props.strength(5, 6).requiresCorrectToolForDrops().noOcclusion()
					.sound(SoundType.NETHERITE_BLOCK)
			)
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.tag(CDTags.STORAGE.forge, CDTags.STORAGE.fabric)
			.tag(CDTags.CAST_IRON_BLOCK.forge, CDTags.CAST_IRON_BLOCK.fabric)
			.lang("Block of Cast Iron")
			.simpleItem()
			.register();

		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerBars);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerFences);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerCageLamps);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerCatwalks);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerDoors);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerHulls);
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerSupports);
		registerPlacards();
		ItemRegistry.METAL_TYPES.forEach(BlockRegistry::registerCoins);

		// Bricks registration
		CreateDecoMod.REGISTRATE.defaultCreativeTab(CreativeTabs.BRICKS_KEY);
		registerBricks();
	}

	private static void registerBars (String metal, Function<String, Item> getter) {
		boolean postFlag = (metal.contains("Netherite")|| metal.contains("Cast Iron"));

		BARS.put(metal, Bars.build(CreateDecoMod.REGISTRATE, metal, "", postFlag).register());
		BAR_PANELS.put(metal, Bars.build(CreateDecoMod.REGISTRATE, metal, "overlay", postFlag).register());
	}

	private static void registerCageLamps (String metal, Function<String, Item> getter) {
		ResourceLocation cage = new ResourceLocation(CreateDecoMod.MOD_ID,
			"block/palettes/cage_lamp/"
				+ metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_lamp"
		);
		Supplier<Item> material = (metal == "Andesite" ? AllItems.ANDESITE_ALLOY : null);

		YELLOW_CAGE_LAMPS.put(metal, CageLamps.build(
				CreateDecoMod.REGISTRATE, metal, DyeColor.YELLOW, cage, YELLOW_ON, YELLOW_OFF
			)
			.recipe(CageLamps.recipe(metal, ()-> Items.TORCH, material))
			.register());
		RED_CAGE_LAMPS.put(metal, CageLamps.build(
				CreateDecoMod.REGISTRATE, metal, DyeColor.RED, cage, RED_ON, RED_OFF
			)
			.recipe(CageLamps.recipe(metal, ()->Items.REDSTONE_TORCH, material))
			.register());
		GREEN_CAGE_LAMPS.put(metal, CageLamps.build(
				CreateDecoMod.REGISTRATE, metal, DyeColor.GREEN, cage, GREEN_ON, GREEN_OFF
			)
			.recipe(CageLamps.recipe(metal, ()->Items.GLOW_BERRIES, material))
			.register());
		BLUE_CAGE_LAMPS.put(metal, CageLamps.build(
				CreateDecoMod.REGISTRATE, metal, DyeColor.BLUE, cage, BLUE_ON, BLUE_OFF
			)
			.recipe(CageLamps.recipe(metal, ()->Items.SOUL_TORCH, material))
			.register());
	}

	private static void registerCatwalks (String metal, Function<String, Item> getter) {
		//String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
		CATWALKS.put(metal, Catwalks.build(
			CreateDecoMod.REGISTRATE, metal, BARS.get(metal)).register());
		CATWALK_STAIRS.put(metal, Catwalks.buildStair(
			CreateDecoMod.REGISTRATE, metal, BARS.get(metal)).register());
		CATWALK_RAILINGS.put(metal, Catwalks.buildRailing(
			CreateDecoMod.REGISTRATE, metal, /*BAR_BLOCKS.get(metal)*/ Blocks.IRON_BARS).register());
	}

	private static void registerFences (String metal, Function<String, Item> getter) {
		MESH_FENCES.put(metal, MeshFences.build(CreateDecoMod.REGISTRATE, metal).register());
	}

	private static void registerDoors (String metal, Function<String, Item> getter) {
		if (metal.equals("Iron") || metal.equals("Gold") || metal.equals("Netherite")) {
			return;
		}

		DOORS.put(metal, Doors.build(CreateDecoMod.REGISTRATE, metal, false)
			.recipe(Doors.recipe(()->getter.apply("ingot")))
			.register());
		LOCK_DOORS.put(metal, Doors.build(CreateDecoMod.REGISTRATE, metal, true)
			.recipe(Doors.lockedRecipe(()->DOORS.get(metal).asItem()))
			.register());
		TRAPDOORS.put(metal, Doors.buildTrapdoor(CreateDecoMod.REGISTRATE, metal)
			.recipe(Doors.trapdoorRecipe(()->getter.apply("ingot")))
			.register());
	}

	private static void registerHulls (String metal, Function<String, Item> getter) {
		HULLS.put(metal, Hulls.build(CreateDecoMod.REGISTRATE, metal)
			.recipe(Hulls.recipe(()->getter.apply("ingot")))
			.register()
		);
	}

	private static void registerSupports (String metal, Function<String, Item> getter) {
		SUPPORTS.put(metal, Supports.build(CreateDecoMod.REGISTRATE, metal, BARS.get(metal)::asItem)
			.recipe(Supports.recipe(()->getter.apply("ingot")))
			.register()
		);
	}

	private static void registerPlacards () {
		for (DyeColor color : DyeColor.values()) {
			if (color == DyeColor.WHITE) { // Create's is the default
				continue;
			}
			String regName = color.name().toLowerCase(Locale.ROOT)
				.replaceAll(" ", "_") + "_placard";

			PLACARDS.put(color, CreateDecoMod.REGISTRATE.block(regName, DyedPlacardBlock::new)
				.initialProperties(SharedProperties::copperMetal)
				.transform(pickaxeOnly())
				.tag(AllTags.AllBlockTags.SAFE_NBT.tag)
				.blockstate((ctx,prov)->BlockStateGenerator.placard(CreateDecoMod.REGISTRATE, color, ctx, prov))
				.simpleItem()
				.recipe((ctx,prov)->
					ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
						.requires(CDTags.PLACARD)
						.requires(DyeItem.byColor(color))
						.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(CDTags.PLACARD).build()
						))
						.unlockedBy("has_dye", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(DyeItem.byColor(color)).build()
						))
						.group("dye_placard")
						.save(prov)
				)
				.onRegisterAfter(Registries.ITEM, placard -> {
					// none of this works. TODO ask about tooltips
					TooltipModifier original = TooltipModifier.REGISTRY.get(AllBlocks.PLACARD.asItem());
					if (original == null) {
						CreateDecoMod.LOGGER.info("placard tooltip was null"); // why is it null?
					} else if (original.equals(TooltipModifier.EMPTY)) {
						CreateDecoMod.LOGGER.info("placard tooltip was empty");
					}
					TooltipModifier.REGISTRY.register(placard.asItem(),
						TooltipModifier.REGISTRY.get(AllBlocks.PLACARD.asItem())
					);
				})
				.register());
		}

		@SuppressWarnings("unchecked")
		BlockEntry<? extends PlacardBlock>[] validPlacards = new BlockEntry[PLACARDS.size()];
		int color = 0;
		for (BlockEntry<? extends PlacardBlock> block : PLACARDS.values()) {
			validPlacards[color] = block;
		}
		PLACARD_ENTITY = CreateDecoMod.REGISTRATE.blockEntity("dyed_placard", DyedPlacardBlock.Entity::new)
			.renderer(()-> PlacardRenderer::new)
			.validBlocks(PLACARDS.values().toArray(validPlacards))
			.register();
	}

	private static void registerCoins (String metal, Function<String, Item> getter) {
		if (metal.equals("Andesite")) return;
		String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
		ResourceLocation side   = new ResourceLocation(CreateDecoMod.MOD_ID, "block/" + regName + "_coinstack_side");
		ResourceLocation top    = new ResourceLocation(CreateDecoMod.MOD_ID, "block/" + regName + "_coinstack_top");
		ResourceLocation bottom = new ResourceLocation(CreateDecoMod.MOD_ID, "block/" + regName + "_coinstack_bottom");

		COIN_BLOCKS.put(metal, Coins.buildCoinStackBlock(
			CreateDecoMod.REGISTRATE,
			()-> ItemRegistry.COINSTACKS.get(metal).get(),
			metal, side, bottom, top
		).register());
	}

	private static void registerBricks () {
		BRICK_COLORS.forEach( (color, name)-> {
			ArrayList<BlockBuilder<Block, ?>>     blocks;
			ArrayList<BlockBuilder<StairBlock,?>> stairs;
			ArrayList<BlockBuilder<SlabBlock,?>>  slabs;

			if (color == null) { // do worn brick stuff
				blocks = Bricks.buildBlock(CreateDecoMod.REGISTRATE, "worn");
				blocks.forEach(bb ->
					WORN_BRICKS.put(bb.getName(), bb.register())
				);
				stairs = Bricks.buildStair(CreateDecoMod.REGISTRATE, "worn");
				stairs.forEach(bb ->
					WORN_STAIRS.put(bb.getName(), bb.register())
				);
				slabs = Bricks.buildSlab(CreateDecoMod.REGISTRATE, "worn");
				slabs.forEach(bb ->
					WORN_SLABS.put(bb.getName(), bb.register())
				);
			}
			blocks = Bricks.buildBlock(CreateDecoMod.REGISTRATE, name);
			blocks.forEach(bb -> {
				BRICKS.putIfAbsent(color, new HashMap<>());
				BRICKS.get(color).put(bb.getName(), bb.register());
			});
			stairs = Bricks.buildStair(CreateDecoMod.REGISTRATE, name);
			stairs.forEach(bb -> {
				STAIRS.putIfAbsent(color, new HashMap<>());
				STAIRS.get(color).put(bb.getName(), bb.register());
			});
			slabs = Bricks.buildSlab(CreateDecoMod.REGISTRATE, name);
			slabs.forEach(bb -> {
				SLABS.putIfAbsent(color, new HashMap<>());
				SLABS.get(color).put(bb.getName(), bb.register());
			});
		});
	}
}
