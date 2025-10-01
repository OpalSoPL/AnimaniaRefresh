package dev.opalsopl.animania_refresh.helper;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public class ResourceHelper {
    public static ResourceLocation getModResourceLocation(String path)
    {
        return getModResourceLocation(AnimaniaRefresh.MODID, path);
    }

    public static ResourceLocation getModResourceLocation(String namespace, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(namespace.toLowerCase(), path.toLowerCase());
    }

    public static ResourceLocation getVanillaResourceLocation(String path)
    {
        return ResourceLocation.parse(path.toLowerCase());
    }

    public static ResourceLocation AddPathPrefix(ResourceLocation location, String pathPrefix)
    {
        return getModResourceLocation(location.getNamespace(), pathPrefix + "/" + location.getPath());
    }

    public static ResourceLocation AddPathSuffix(ResourceLocation location, String pathSuffix)
    {
        return getModResourceLocation(location.getNamespace(), location.getPath() + pathSuffix);
    }

    public static ResourceLocation AddPathSufPref(ResourceLocation location, String pathPrefix, String pathSuffix)
    {
        return AddPathSuffix(AddPathPrefix(location, pathPrefix), pathSuffix);
    }

    public static ResourceLocation ChangeNamespace(ResourceLocation location, String targetNamespace)
    {
        return getModResourceLocation(targetNamespace, location.getPath());
    }

    public static Resource getResource(ResourceLocation location)
    {
        Minecraft mc = Minecraft.getInstance();

        return mc.getResourceManager().getResource(location).orElseThrow();
    }
}
