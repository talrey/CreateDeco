package com.github.talrey.createdeco;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.nio.file.Path;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class Config {
  public static final String CAT_GENERAL = "general";

  public static ForgeConfigSpec COMMON_CONF, CLIENT_CONF;

  public static HashMap<String, ForgeConfigSpec.ConfigValue<?>> SETTINGS = new HashMap<>();
  public static String CAN_PRESS_COINS = "can_press_coins";

  static {
    ForgeConfigSpec.Builder COMMON = new ForgeConfigSpec.Builder();
    ForgeConfigSpec.Builder CLIENT = new ForgeConfigSpec.Builder();

    COMMON.comment("General Settings").push(CAT_GENERAL);
    SETTINGS.put(CAN_PRESS_COINS, COMMON.comment("allow coin recipe").define(CAN_PRESS_COINS, false));
    COMMON.pop();

    COMMON_CONF = COMMON.build();
    CLIENT_CONF = CLIENT.build();
  }

  public static boolean getSetting (String name) {
    if (SETTINGS.containsKey(name)) return ((ForgeConfigSpec.BooleanValue)SETTINGS.get(name)).get();
    return false;
  }

  public static void loadConfig (ForgeConfigSpec spec, Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
    configData.load();
    spec.setConfig(configData);
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public static void onLoad (final ModConfigEvent.Loading loadEvent) {

  }
}
