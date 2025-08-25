package dev.opalsopl.animania_refresh.helper;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.io.InputStream;

public class ImageHelper {
    @OnlyIn(Dist.CLIENT)
    public static int getAverageColor(ResourceLocation location, Vector2i corner1, Vector2i corner2, boolean ignoreAlpha)
    {
        Minecraft mc = Minecraft.getInstance();

        Resource res  = mc.getResourceManager().getResource(location).orElseThrow();

        try (InputStream stream = res.open()) {

            NativeImage img = NativeImage.read(stream);

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

            for (int y = minY; y < maxY; y++)
            {
                for (int x = minX; x < maxX; x++)
                {
                    int color = img.getPixelRGBA(x, y);

                    int a = (color >> 24) & 0xFF;

                    if (ignoreAlpha && a == 0) continue;

                    rSum += (int) Math.sqrt((color >> 16) & 0xFF);
                    gSum += (int) Math.sqrt((color >> 8) & 0xFF);
                    bSum += (int) Math.sqrt(color & 0xFF);

                    count++;
                }
            }

            int r = (int) Math.round(Math.pow((double) rSum / count, 2) * 255);
            int g = (int) Math.round(Math.pow((double) gSum / count, 2) * 255);
            int b = (int) Math.round(Math.pow((double) bSum / count, 2) * 255);

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
}
