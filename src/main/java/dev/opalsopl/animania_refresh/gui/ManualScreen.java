package dev.opalsopl.animania_refresh.gui;

import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class ManualScreen extends Screen {
    public static final ResourceLocation BOOK_LOCATION = ResourceHelper.getModResourceLocation("textures/gui/book.png");

    private boolean playTurnSound = true; //replace with dynamic method

    ManualPageButton forwardButton;
    ManualPageButton backButton;
    ManualPageButton forwardArticleButton;
    ManualPageButton backArticleButton;

    public ManualScreen(Component title) {
        super(title);
    }

    protected void init() {
        this.createPageControlButtons();
    }

    protected void createPageControlButtons() {
        int corner = (this.width - 192) / 2;
        forwardButton = addRenderableWidget(new ManualPageButton(corner + 176, 159,
                true,false, (btn) -> pageForward(), playTurnSound));
        backButton = addRenderableWidget(new ManualPageButton(corner - 15, 159,
                false, false, (btn) -> pageBack(), playTurnSound));
        forwardArticleButton = addRenderableWidget(new ManualPageButton(corner + 176, 170,
                true, true, (btn) -> articleForward(), playTurnSound));
        backArticleButton = addRenderableWidget(new ManualPageButton(corner - 15, 170,
                false, true, (btn) -> articleBack(), playTurnSound));

        this.updateButtonVisibility();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        int corner = (this.width - 192) / 2;
        graphics.blit(BOOK_LOCATION, corner, 2, 0, 0, 192, 192);

        Component string = Component.literal("Lorem ipsum dolor sit amet consectetur adipiscing elit. Pretium tellus duis convallis tempus leo eu aenean. Iaculis massa nisl malesuada lacinia integer nunc posuere. Conubia nostra inceptos himenaeos orci varius natoque penatibus. Nulla molestie mattis scelerisque maximus ege");

        var textLines = this.font.split(string, 114);

        int maxHeight = Math.min(128 / 9, textLines.size());

        for(int l = 0; l < maxHeight; ++l) {
            FormattedCharSequence formattedcharsequence = textLines.get(l);
            graphics.drawString(this.font, formattedcharsequence, corner + 36, 32 + l * 9, 0, false);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void pageBack() {
    }

    private void pageForward() {
    }

    private void articleBack() {
    }

    private void articleForward() {
    }

    private void updateButtonVisibility() {
    }
}
