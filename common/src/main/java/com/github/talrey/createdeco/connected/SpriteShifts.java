package com.github.talrey.createdeco.connected;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.ItemRegistry;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Locale;

public class SpriteShifts {
  public static final HashMap<String, CTSpriteShiftEntry> METAL_WINDOWS     = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> SHEET_METAL_SIDES = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> CATWALK_TOPS      = new HashMap<>();

  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_TOP = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_FRONT = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_SIDE = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_BOTTOM = new HashMap<>();

  static {
    populateMaps();
  }

  private static Couple<CTSpriteShiftEntry> vault (DyeColor color, String face) {
    //final String prefixed = "block/vault/vault_" + name;
    final String prefixed = "block/palettes/shipping_containers/" + color + "/vault_" + face;
    return Couple.createWithContext(medium -> CTSpriteShifter.getCT(
      AllCTTypes.RECTANGLE, CreateDecoMod.id(prefixed + "_small"),
      CreateDecoMod.id(medium ? prefixed + "_medium" : prefixed + "_large")
    ));
  }

  public static void populateMaps () {
    CreateDecoMod.LOGGER.info("Populating connected texture maps...");
    for (DyeColor color : DyeColor.values()) {
      VAULT_TOP   .put(color, vault(color, "top"));
      VAULT_BOTTOM.put(color, vault(color, "bottom"));
      VAULT_FRONT .put(color, vault(color, "front"));
      VAULT_SIDE  .put(color, vault(color, "side"));
    }

    for (String metal : ItemRegistry.METAL_TYPES.keySet()) {
      String path = "block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";
      ResourceLocation blockTexture     = ResourceLocation.tryBuild(CreateDecoMod.MOD_ID, path);
      ResourceLocation connectedTexture = ResourceLocation.tryBuild(CreateDecoMod.MOD_ID, path + "_connected");
      SHEET_METAL_SIDES.put(metal, make(AllCTTypes.VERTICAL, blockTexture, connectedTexture));

      path = "block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";
      blockTexture     = ResourceLocation.tryBuild(CreateDecoMod.MOD_ID, path);
      connectedTexture = ResourceLocation.tryBuild(CreateDecoMod.MOD_ID, path + "_connected");
      CATWALK_TOPS.put(metal, make(AllCTTypes.OMNIDIRECTIONAL, blockTexture, connectedTexture));

      path = "block/palettes/windows/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_window";
      blockTexture     = ResourceLocation.tryBuild(CreateDecoMod.MOD_ID, path);
      connectedTexture = ResourceLocation.tryBuild(CreateDecoMod.MOD_ID, path + "_connected");
      METAL_WINDOWS.put(metal, make(AllCTTypes.VERTICAL, blockTexture, connectedTexture));
    }
  }

  private static CTSpriteShiftEntry make (CTType type, ResourceLocation blockTexture, ResourceLocation connectedTexture) {
    String key = blockTexture + "->" + connectedTexture + "+" + type.getId();
    CTSpriteShiftEntry entry = new CTSpriteShiftEntry(type);
    if (CatnipServices.PLATFORM.getEnv().isClient())
      entry.set(blockTexture, connectedTexture);
    return entry;
  }
}
