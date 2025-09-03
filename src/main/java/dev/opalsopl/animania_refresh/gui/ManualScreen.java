package dev.opalsopl.animania_refresh.gui;

import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class ManualScreen extends Screen {
    public static final ResourceLocation BOOK_LOCATION = ResourceHelper.getModResourceLocation("textures/gui/book.png");

    public ManualScreen(Component title) {
        super(title);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        this.renderBackground(graphics);
        int i = (this.width - 192) / 2;
        graphics.blit(BOOK_LOCATION, i, 2, 0, 0, 192, 192);

        //BookViewScreen`
        Component string = Component.literal("Lorem ipsum dolor sit amet consectetur adipiscing elit. Pretium tellus duis convallis tempus leo eu aenean. Iaculis massa nisl malesuada lacinia integer nunc posuere. Conubia nostra inceptos himenaeos orci varius natoque penatibus. Nulla molestie mattis scelerisque maximus ege");

        var textLines = this.font.split(string, 114);

        int maxHeight = Math.min(128 / 9, textLines.size());

        for(int l = 0; l < maxHeight; ++l) {
            FormattedCharSequence formattedcharsequence = textLines.get(l);
            graphics.drawString(this.font, formattedcharsequence, i + 36, 32 + l * 9, 0, false);
        }

    }
}
