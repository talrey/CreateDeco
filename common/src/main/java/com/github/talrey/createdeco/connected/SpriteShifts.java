package com.github.talrey.createdeco.connected;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.ItemRegistry;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.vault.ItemVaultCTBehaviour;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Locale;

public class SpriteShifts {
  public static final HashMap<String, CTSpriteShiftEntry> SHEET_METAL_SIDES = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> CATWALK_TOPS      = new HashMap<>();

  public static final Couple<CTSpriteShiftEntry> VAULT_TOP = vault("top"), VAULT_FRONT = vault("front"),
          VAULT_SIDE = vault("side"), VAULT_BOTTOM = vault("bottom");

  static {
    populateMaps();
  }

  private static Couple<CTSpriteShiftEntry> vault(String name) {
    //for (DyeColor color : DyeColor.values()) {
      //final String prefixed = "block/vault/vault_" + name;
      final String prefixed = "block/palettes/shipping_containers/" + "blue" + "/vault_" + name;
      return Couple.createWithContext(
              medium -> CTSpriteShifter.getCT(AllCTTypes.RECTANGLE, CreateDecoMod.id(prefixed + "_small"),
                      CreateDecoMod.id(medium ? prefixed + "_medium" : prefixed + "_large")));
    //}
  }

  private static void populateMaps () {

    for (String metal : ItemRegistry.METAL_TYPES.keySet()) {
      String path = "block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";
      ResourceLocation blockTexture     = new ResourceLocation(CreateDecoMod.MOD_ID, path);
      ResourceLocation connectedTexture = new ResourceLocation(CreateDecoMod.MOD_ID, path + "_connected");
      SHEET_METAL_SIDES.put(metal, CTSpriteShifter.getCT(AllCTTypes.VERTICAL, blockTexture, connectedTexture));

      path = "block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";
      blockTexture     = new ResourceLocation(CreateDecoMod.MOD_ID, path);
      connectedTexture = new ResourceLocation(CreateDecoMod.MOD_ID, path + "_connected");
      CATWALK_TOPS.put(metal, CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL, blockTexture, connectedTexture));
    }
  }
}
