package com.github.talrey.createdeco;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;

public class ConfigCondition implements ConditionJsonProvider {
  private static final ResourceLocation NAME = new ResourceLocation(CreateDecoMod.MODID, "config");
  private final String configName;

  public ConfigCondition (String name) {
    configName = name;
  }

  @Override
  public ResourceLocation getConditionId() {
    return NAME;
  }

  @Override
  public void writeParameters(JsonObject json) {
    json.addProperty("config", configName);
  }

  public static void registerConditon() {
    ResourceConditions.register(NAME, json -> Config.getSetting(json.get("config").getAsString()));
  }
}
