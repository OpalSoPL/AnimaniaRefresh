package dev.opalsopl.animania_refresh.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.PageButton;

public class ManualPageButton extends PageButton {

    private final boolean isForward;
    private final boolean isArticle;

    public ManualPageButton(int x, int y, boolean isForward, boolean isArticle, OnPress pOnPress, boolean pPlayTurnSound) {
        super(x, y, isForward, pOnPress, pPlayTurnSound);
        this.isForward = isForward;
        this.isArticle = isArticle;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int uvX = 0;
        int uvY = 192;
        if (isArticle)
        {
            uvY += 26;
        }

        if (isHoveredOrFocused()) {
            uvX += 23;
        }

        if (!isForward) {
            uvY += 13;
        }

        pGuiGraphics.blit(ManualScreen.BOOK_LOCATION, this.getX(), this.getY(), uvX, uvY, 23, 13);
    }
}
