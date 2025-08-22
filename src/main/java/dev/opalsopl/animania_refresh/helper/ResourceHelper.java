package dev.opalsopl.animania_refresh.helper;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import net.minecraft.resources.ResourceLocation;

public class ResourceHelper {
    public static ResourceLocation GetModResource(String path)
    {
        return GetModResource(AnimaniaRefresh.MODID, path);
    }

    public static ResourceLocation GetModResource(String namespace, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(namespace.toLowerCase(), path.toLowerCase());
    }

    public static ResourceLocation GetVanillaResource(String path)
    {
        return ResourceLocation.parse(path.toLowerCase());
    }

    public static ResourceLocation AddPathPrefix(ResourceLocation location, String pathPrefix)
    {
        return GetModResource(location.getNamespace(), pathPrefix + "/" + location.getPath());
    }

    public static ResourceLocation AddPathSuffix(ResourceLocation location, String pathSuffix)
    {
        return GetModResource(location.getNamespace(), location.getPath() + pathSuffix);
    }

    public static ResourceLocation AddPathSufPref(ResourceLocation location, String pathPrefix, String pathSuffix)
    {
        return AddPathSuffix(AddPathPrefix(location, pathPrefix), pathSuffix);
    }

    public static ResourceLocation ChangeNamespace(ResourceLocation location, String targetNamespace)
    {
        return GetModResource(targetNamespace, location.getPath());
    }
}
