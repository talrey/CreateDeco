package com.github.talrey.createdeco.util;

import com.github.talrey.createdeco.CreateDecoMod;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.function.Consumer;

// Stores a trio of tags that are used for per-loader tagging
public class LoaderTag<T> {
  public TagKey<T> tag, forge, fabric;

  public LoaderTag (TagKey<T> common, TagKey<T> forge, TagKey<T> fabric) {
    this.tag    = common;
    this.forge  = forge;
    this.fabric = fabric;
  }

  public LoaderTag (ResourceKey<? extends Registry<T>> registry,
                    ResourceLocation common, ResourceLocation forge, ResourceLocation fabric
  ) {
    this(TagKey.create(registry, common), TagKey.create(registry, forge), TagKey.create(registry, fabric));
  }

  public static <T> LoaderTag<T> standard (ResourceKey<? extends Registry<T>> registry,
                                           String common, String forge, String fabric
  ) {
    return new LoaderTag<>(registry,
      new ResourceLocation(CreateDecoMod.MODID, "internal/" + common),
      new ResourceLocation("forge", forge),
      new ResourceLocation("c", fabric)
    );
  }

  public static <T> LoaderTag<T> same (ResourceKey<? extends Registry<T>> registry, String path) {
    return standard(registry, path, path, path);
  }

  public LoaderTag<T> genBoth (RegistrateTagsProvider<T> tags, Consumer<TagsProvider.TagAppender> cons) {
    cons.accept(CDTagGen.append(tags, fabric));
    cons.accept(CDTagGen.append(tags, forge));
    return this;
  }

  public LoaderTag<T> genCommon (RegistrateTagsProvider<T> tags) {
    CDTagGen.append(tags, tag)
      .addOptional(forge.location())
      .addOptional(fabric.location());
    return this;
  }
}
