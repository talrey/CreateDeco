package com.github.talrey.createdeco.connected;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Locale;

public class SpriteShifts {
  public static final HashMap<String, CTSpriteShiftEntry> SHEET_METAL_SIDES = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> CATWALK_TOPS      = new HashMap<>();

  static {
    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
      populateMaps();
  }

  private static void populateMaps () {
    for (String metal : Registration.METAL_TYPES.keySet()) {
      String path = "block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";
      ResourceLocation blockTexture     = new ResourceLocation(CreateDecoMod.MODID, path);
      ResourceLocation connectedTexture = new ResourceLocation(CreateDecoMod.MODID, path + "_connected");
      SHEET_METAL_SIDES.put(metal, CTSpriteShifter.getCT(AllCTTypes.VERTICAL, blockTexture, connectedTexture));

      path = "block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";
      blockTexture     = new ResourceLocation(CreateDecoMod.MODID, path);
      connectedTexture = new ResourceLocation(CreateDecoMod.MODID, path + "_connected");
      CATWALK_TOPS.put(metal, CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL, blockTexture, connectedTexture));
    }
  }
}
