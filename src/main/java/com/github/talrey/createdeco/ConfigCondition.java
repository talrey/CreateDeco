package com.github.talrey.createdeco;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ConfigCondition implements ICondition {
  private static final ResourceLocation NAME = new ResourceLocation(CreateDecoMod.MODID, "config");
  private final String configName;

  public ConfigCondition (String name) {
    configName = name;
  }

  @Override
  public ResourceLocation getID() {
    return NAME;
  }

  @Override
  public boolean test() {
  //  LogManager.getLogger(CreateDecoMod.MODID).debug("testing config for " + configName + " : returns " + Config.getSetting(configName));
    return Config.getSetting(configName);
  }

  public static class Serializer implements IConditionSerializer<ConfigCondition> {
    public static final Serializer INSTANCE = new Serializer();
    @Override
    public void write(JsonObject json, ConfigCondition value) {
      json.addProperty("config", value.configName);
    }
    @Override
    public ConfigCondition read (JsonObject json) {
      return new ConfigCondition(JsonUtils.getStringOr("config", json, "error"));
    }

    @Override
    public ResourceLocation getID() {
      return NAME;
    }
  }
}
