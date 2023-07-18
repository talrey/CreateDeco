package com.github.talrey.createdeco.registry;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.util.LoaderTag;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Locale;

public class CDTags {
  private enum Material {
    iron, gold, copper, zinc, brass, cast_iron, netherite;
  }

  private enum Form {
    nuggets, ingots, plates, blocks;
  }

  private static final HashMap<Material, HashMap<Form, LoaderTag<Item>>> tags = new HashMap<>();

  public static final TagKey<Item> PLACARD = TagKey.create(
    Registry.ITEM_REGISTRY,
    new ResourceLocation(CreateDecoMod.MODID, "placards")
  );

  static {
    init();
  }

  private static void init () {
    for (Material m : Material.values()) {
      tags.put(m, new HashMap<>());
      for (Form f : Form.values()) {
        tags.get(m).put(f, item(f.toString(), m.toString()));
      }
    }
  }

  public static LoaderTag<Item> of (String material, String form) {
    Material m = Material.valueOf(material.toLowerCase(Locale.ROOT).replaceAll(" ", "_"));
    Form f = Form.valueOf(form.toLowerCase(Locale.ROOT));
    if (tags.containsKey(m) && tags.get(m).containsKey(f))
      return tags.get(m).get(f);
    // else
    return tags.get(Material.iron).get(Form.ingots); // basically mod recipes in a nutshell
  }

  public static final LoaderTag<Block>
    STORAGE         = block("storage_blocks"),
    CAST_IRON_BLOCK = block("storage_blocks", "cast_iron");

  private static LoaderTag<Item> item (String type, String name) {
    String forge  = type + "/" + name;
    String fabric = name + "_" + type;
    String common = forge + "_"+ type;
    return LoaderTag.standard(Registry.ITEM_REGISTRY, common, forge, fabric);
  }

  private static LoaderTag<Block> block (String type, String name) {
    String forge  = type + "/" + name;
    String fabric = name + "_" + type;
    String common = forge + "_"+ type;
    return LoaderTag.standard(Registry.BLOCK_REGISTRY, common, forge, fabric);
  }

  private static LoaderTag<Block> block (String type) {
    return LoaderTag.same(Registry.BLOCK_REGISTRY, type);
  }
}
