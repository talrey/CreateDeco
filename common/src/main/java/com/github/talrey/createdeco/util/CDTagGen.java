package com.github.talrey.createdeco.util;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CDTagGen {

  public static TagsProvider.TagAppender<Item> append (RegistrateItemTagsProvider prov, TagKey<Item> tag) {
    return append(prov, tag);
  }

  @ExpectPlatform
  public static <T> TagsProvider.TagAppender append (RegistrateTagsProvider<T> prov, TagKey<T> tag) {
    throw new AssertionError();
  }
}
