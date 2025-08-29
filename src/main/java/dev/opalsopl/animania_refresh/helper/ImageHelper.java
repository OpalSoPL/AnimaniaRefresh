package dev.opalsopl.animania_refresh.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImageHelper {
    @OnlyIn(Dist.CLIENT)
    public static int getAverageColor(ResourceLocation location, Vector2i corner1, Vector2i corner2, boolean ignoreAlpha)
    {
        Resource res;
        try {
            res = ResourceHelper.getResource(location);

            return getAverageColor(res, corner1, corner2, ignoreAlpha);
        } catch (Exception e) { //catch exception to stop game from crashing
            return 0xF800F8;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static int getAverageColor(Resource resource, Vector2i corner1, Vector2i corner2, boolean ignoreAlpha)
    {
        try (InputStream stream = resource.open();
             NativeImage img = NativeImage.read(stream))
        {

            int minX = Math.min(corner1.x, corner2.x);
            int maxX = Math.max(corner1.x, corner2.x);
            int minY = Math.min(corner1.y, corner2.y);
            int maxY = Math.max(corner1.y, corner2.y);

            minX = Math.max(0, minX);
            maxX = Math.min(img.getWidth() - 1, maxX);
            minY = Math.max(0, minY);
            maxY = Math.min(img.getHeight() - 1, maxY);

            int rSum = 0;
            int gSum = 0;
            int bSum = 0;
            int count = 0;

            for (int y = minY; y < maxY; y++) {
                for (int x = minX; x < maxX; x++) {
                    int color = img.getPixelRGBA(x, y);

                    int a = (color >> 24) & 0xFF;

                    if (ignoreAlpha && a == 0) continue;

                    rSum += (int) Math.sqrt(color & 0xFF);
                    gSum += (int) Math.sqrt((color >> 8) & 0xFF);
                    bSum += (int) Math.sqrt((color >> 16) & 0xFF);

                    count++;
                }
            }

            int r = (int) Math.pow((double) rSum / count, 2);
            int g = (int) Math.pow((double) gSum / count, 2);
            int b = (int) Math.pow((double) bSum / count, 2);

            return (r << 16) | (g << 8) | b;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0xFFFFFF;
        }
    }

    public static Vector3i intToVectorColor (int color)
    {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        return new Vector3i(r, g, b);
    }

    @OnlyIn(Dist.CLIENT)
    public static JsonObject getTextureMetadata (ResourceLocation location)
    {
        Resource res;

        try {
            res = ResourceHelper.getResource(location);

            return getTextureMetadata(res);
        } catch (Exception e) { //catch exception to stop game from crashing
            return null;
        }

    }

    @OnlyIn(Dist.CLIENT)
    public static JsonObject getTextureMetadata (Resource resource) throws IOException {
        try (InputStream stream = resource.open()){
            return  JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
        }
    }
}
