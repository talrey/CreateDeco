package com.github.talrey.createdeco.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.CreativeModeTab;

public class DecoCreativeModeTab  {
  public static final String BRICKS_NAME = "CreateDeco Bricks";
  public static final String METALS_NAME = "CreateDeco Metals";
  public static final String PROPS_NAME  = "CreateDeco Props";

  public static CreativeModeTab BRICKS_GROUP;
  public static CreativeModeTab METALS_GROUP;
  public static CreativeModeTab PROPS_GROUP;

  @ExpectPlatform
  public static void build () {
    throw new AssertionError();
  }
}
