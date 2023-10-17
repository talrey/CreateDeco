package com.github.talrey.createdeco;

import com.github.talrey.createdeco.api.CageLamps;
import com.github.talrey.createdeco.blocks.*;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.talrey.createdeco.api.CageLamps.*;

public class BlockRegistry {
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateDecoMod.MOD_ID);

	public static HashMap<String, BlockEntry<CageLampBlock>> YELLOW_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>    RED_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>  GREEN_CAGE_LAMPS = new HashMap<>();
	public static HashMap<String, BlockEntry<CageLampBlock>>   BLUE_CAGE_LAMPS = new HashMap<>();

	public static HashMap<String, Function<String, Item>> METAL_TYPES = new HashMap<>();

	public static void init() {
		// load the class and register everything
		CreateDecoMod.LOGGER.info("Registering blocks for " + CreateDecoMod.NAME);

		REGISTRATE.defaultCreativeTab(CreativeTabs.props(), "props_tab");
		METAL_TYPES.put("Andesite", (str) -> AllItems.ANDESITE_ALLOY.get());
		METAL_TYPES.put("Zinc", (str) -> AllItems.ZINC_INGOT.get());
		METAL_TYPES.put("Copper", (str) -> Items.COPPER_INGOT);
		METAL_TYPES.put("Brass", (str) -> AllItems.BRASS_INGOT.get());
		METAL_TYPES.put("Iron", (str) -> Items.IRON_INGOT);
		METAL_TYPES.put("Gold", (str) -> Items.GOLD_INGOT);
		METAL_TYPES.put("Netherite", (str) -> Items.NETHERITE_INGOT);
		//METAL_TYPES.put("Cast Iron", (str) -> CAST_IRON_INGOT.get());

		METAL_TYPES.forEach((metal, getter) -> {
			ResourceLocation cage = new ResourceLocation(CreateDecoMod.MOD_ID,
				"block/palettes/cage_lamp/"
					+ metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_lamp"
			);
			Supplier<Item> material = (metal == "Andesite" ? AllItems.ANDESITE_ALLOY : null);

			YELLOW_CAGE_LAMPS.put(metal, CageLamps.build(
				REGISTRATE, metal, DyeColor.YELLOW, cage, YELLOW_ON, YELLOW_OFF
				)
				.recipe(CageLamps.recipe(metal, ()-> Items.TORCH, material))
				.register());
			RED_CAGE_LAMPS.put(metal, CageLamps.build(
				REGISTRATE, metal, DyeColor.RED, cage, RED_ON, RED_OFF
				)
				.recipe(CageLamps.recipe(metal, ()->Items.REDSTONE_TORCH, material))
				.register());
			GREEN_CAGE_LAMPS.put(metal, CageLamps.build(
				REGISTRATE, metal, DyeColor.GREEN, cage, GREEN_ON, GREEN_OFF
				)
				.recipe(CageLamps.recipe(metal, ()->Items.GLOW_BERRIES, material))
				.register());
			BLUE_CAGE_LAMPS.put(metal, CageLamps.build(
				REGISTRATE, metal, DyeColor.BLUE, cage, BLUE_ON, BLUE_OFF
				)
				.recipe(CageLamps.recipe(metal, ()->Items.SOUL_TORCH, material))
				.register());
		});
	}
}
