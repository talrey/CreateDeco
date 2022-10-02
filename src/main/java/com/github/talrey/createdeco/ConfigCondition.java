package com.github.talrey.createdeco;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ConfigCondition implements ICondition {
  public static final ResourceLocation NAME = new ResourceLocation(CreateDecoMod.MODID, "config");
  private final String configName;

  public static final RecipeSerializer<Recipe<?>> SERIALZIER = null;

  public ConfigCondition (String name) {
    configName = name;
  }

  @Override
  public ResourceLocation getID () { return NAME; }

  @Override
  public boolean test(ICondition.IContext context) {
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
      return new ConfigCondition(json.get("config").getAsString());
    }

    @Override
    public ResourceLocation getID() {
      return NAME;
    }
  }
}
