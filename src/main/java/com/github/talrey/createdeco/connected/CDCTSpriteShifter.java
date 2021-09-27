package com.github.talrey.createdeco.connected;

import com.github.talrey.createdeco.CreateDecoMod;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter.CTType;
import com.simibubi.create.foundation.block.render.SpriteShifter;
import net.minecraft.util.ResourceLocation;

public class CDCTSpriteShifter extends SpriteShifter {
  public static CTSpriteShiftEntry getCT (CTType type, ResourceLocation blockTexture, String connectedTexture) {
    String key = type.name() + ":" + blockTexture.getNamespace() + ":" + blockTexture.getPath() + "->" + connectedTexture;
    if (textures.containsKey(key)) {
      return (CTSpriteShiftEntry) textures.get(key);
    }
    CTSpriteShiftEntry entry = create(type);
    entry.set(blockTexture, new ResourceLocation(CreateDecoMod.MODID, connectedTexture));
    textures.put(key, entry);
    return entry;
  }

  private static CTSpriteShiftEntry create (CTType type) {
    switch (type) {
      case OMNIDIRECTIONAL: return new CTSpriteShiftEntry.Omnidirectional();
      case VERTICAL:        return new CTSpriteShiftEntry.Vertical();
      case HORIZONTAL:      return new CTSpriteShiftEntry.Horizontal();
      case CROSS:           return new CTSpriteShiftEntry.Cross();
      default: return null;
    }
  }
}
