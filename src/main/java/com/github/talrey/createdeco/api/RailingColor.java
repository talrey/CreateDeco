package com.github.talrey.createdeco.api;

import net.minecraft.util.StringRepresentable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum RailingColor implements StringRepresentable {
  NATURAL(null),
  BLUE("Blue"),
  GREEN("Green"),
  ORANGE("Orange"),
  PURPLE("Purple"),
  RED("Red"),
  WHITE("White"),
  YELLOW("Yellow");

  private final String registryKey;
  private final String serializedName;

  RailingColor (String registryKey) {
    this.registryKey = registryKey;
    this.serializedName = registryKey == null
      ? "natural"
      : registryKey.toLowerCase(Locale.ROOT).replace(' ', '_');
  }

  public boolean isNatural () {
    return this.registryKey == null;
  }

  public String getRegistryKey () {
    return this.registryKey;
  }

  @Override
  public String getSerializedName () {
    return this.serializedName;
  }

  private static final Map<String, RailingColor> BY_REGISTRY_KEY = new HashMap<>();
  static {
    for (RailingColor color : values()) {
      if (color.registryKey != null) {
        BY_REGISTRY_KEY.put(color.registryKey, color);
      }
    }
  }

  public static RailingColor fromRegistryKey (String key) {
    return BY_REGISTRY_KEY.get(key);
  }
}