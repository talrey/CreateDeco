package com.github.talrey.createdeco.util.forge;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;

public class CDTagGenImpl {
  public static <T> TagsProvider.TagAppender append (RegistrateTagsProvider<T> prov, TagKey<T> tag) {
    return prov.tag(tag);
  }
}
