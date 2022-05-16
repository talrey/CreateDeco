package com.github.talrey.createdeco.registry;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import io.github.fabricators_of_create.porting_lib.util.LazyItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DecoCreativeModeTab extends LazyItemGroup {
  private final Supplier<ItemStack> sup;
  private DecoCreativeModeTab (final String name, final Supplier<ItemStack> supplier) {
    super(name);
    sup = supplier;
  }
  @Override
  public @NotNull ItemStack makeIcon () {
    return sup.get();
  }

  public static final String BRICKS_NAME = "CreateDeco Bricks";
  public static final String METALS_NAME = "CreateDeco Metals";
  public static final String PROPS_NAME  = "CreateDeco Props";

  public static final CreativeModeTab BRICKS_GROUP = new DecoCreativeModeTab(
    CreateDecoMod.MODID + ".bricks",
    () -> Registration.BRICK_BLOCK.get(DyeColor.LIGHT_BLUE).asStack()
  );
  public static final CreativeModeTab METALS_GROUP = new DecoCreativeModeTab(
    CreateDecoMod.MODID + ".metals",
    () -> Registration.BAR_BLOCKS.get("brass").asStack()
  );
  public static final CreativeModeTab PROPS_GROUP = new DecoCreativeModeTab(
    CreateDecoMod.MODID + ".props",
    () -> Props.COINSTACK_ITEM.get("Brass").asStack()
  );
}
