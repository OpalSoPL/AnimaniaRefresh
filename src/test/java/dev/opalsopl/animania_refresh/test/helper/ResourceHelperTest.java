package dev.opalsopl.animania_refresh.test.helper;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResourceHelperTest {

    @ParameterizedTest
    @ValueSource(strings = {"test", "2137", "TEST", "/TEST/---.."})
    public void getModResource_Path_Match(String path)
    {
        assertEquals(String.format("%s:%s", AnimaniaRefresh.MODID, path.toLowerCase()),
                ResourceHelper.GetModResource(path).toString());
    }

    @ParameterizedTest
    @CsvSource({
            "test_namespace, test",
            "nam, 2137",
            "'TEST', '/TEST/---..'",
            "'test', '2137'",
            "'TEST', '/TEST/---..'"
    })
    public void getModResource_Full_Match(String namespace, String path)
    {
        assertEquals(String.format("%s:%s", namespace.toLowerCase(), path.toLowerCase()),
                ResourceHelper.GetModResource(namespace, path).toString());
    }

    @ParameterizedTest
    @CsvSource({"test", "2137", "TEST", "/TEST/---.."})
    public void getVanillaResource_Full_Match(String path)
    {
        assertEquals(String.format("%s:%s", "minecraft", path.toLowerCase()),
                ResourceHelper.GetVanillaResource(path).toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"kremówka", "space! SPACEEEEEEEEEE", "wooooo:oooooow", "lol$lol" })
    public void getModResource_Path_ThrowsException(String path)
    {
        assertThrows(ResourceLocationException.class, () ->
                ResourceHelper.GetModResource(path));
    }

    @ParameterizedTest
    @ValueSource(strings = {"kremówka", "space! SPACEEEEEEEEEE", "lol$lol" })
    public void getVanillaResource_ThrowsException(String path)
    {
        assertThrows(ResourceLocationException.class, () ->
                ResourceHelper.GetVanillaResource(path));
    }

    @ParameterizedTest
    @CsvSource({"namespace!, path%",
                "name space, path"})
    public void getModResource_NamespacePath_ThrowsException(String namespace, String path)
    {
        assertThrows(ResourceLocationException.class, () ->
                ResourceHelper.GetModResource(namespace, path));
    }

    @ParameterizedTest
    @CsvSource({"namespace, path, prefix",
            "animania_refresh, assets/textures, minecraft",
            "random-mod, random_png, v2"})
    public void addPathPrefix_Match(String namespace, String basePath, String prefix)
    {
        ResourceLocation res = ResourceHelper.GetModResource(namespace, basePath);
        ResourceLocation resultRes = ResourceHelper.AddPathPrefix(res, prefix);

        assertEquals(String.format("%s:%s/%s", namespace, prefix, basePath), resultRes.toString());
    }

    @ParameterizedTest
    @CsvSource({"namespace, path, suffix",
            "animania_refresh, assets/textures, _additional.png",
            "random-mod, random_png, -baby"})
    public void addPathSuffix_Match(String namespace, String basePath, String suffix)
    {
        ResourceLocation res = ResourceHelper.GetModResource(namespace, basePath);
        ResourceLocation resultRes = ResourceHelper.AddPathSuffix(res, suffix);

        assertEquals(String.format("%s:%s%s", namespace, basePath, suffix), resultRes.toString());
    }

    @ParameterizedTest
    @CsvSource({"namespace, path, prefix, suffix",
            "animania_refresh, assets/textures, more, _color",
            "random-mod, random_png, new, .png"})
    public void addPathSufPref_Match(String namespace, String basePath, String prefix, String suffix)
    {
        ResourceLocation res = ResourceHelper.GetModResource(namespace, basePath);
        ResourceLocation resultRes = ResourceHelper.AddPathSufPref(res, prefix, suffix);

        assertEquals(String.format("%s:%s/%s%s", namespace, prefix, basePath, suffix), resultRes.toString());
    }

    @ParameterizedTest
    @CsvSource({"namespace, path, target_namespace",
                "animania_refresh, assets/textures, minecraft",
                "random-mod, random_png, new"})
    public void changeNamespace(String namespace, String path, String targetNamespace)
    {
        ResourceLocation res = ResourceHelper.GetModResource(namespace, path);
        ResourceLocation resultRes = ResourceHelper.ChangeNamespace(res, targetNamespace);

        assertEquals(String.format("%s:%s", targetNamespace, path), resultRes.toString());
    }
}
