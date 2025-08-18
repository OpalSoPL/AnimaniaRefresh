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
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation GetVanillaResource(String path)
    {
        return ResourceLocation.parse(path);
    }
}
