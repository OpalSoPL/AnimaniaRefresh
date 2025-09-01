package dev.opalsopl.animania_refresh.gui;

import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class ManualScreen extends Screen {
    public static final ResourceLocation BOOK_LOCATION = ResourceHelper.getModResourceLocation("textures/gui/book.png");
    private MutableComponent pageMsg;


    public ManualScreen(Component title) {
        super(title);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        this.renderBackground(graphics);
        int i = (this.width - 192) / 2;
        graphics.blit(BOOK_LOCATION, i, 2, 0, 0, 192, 192);

    }
}
