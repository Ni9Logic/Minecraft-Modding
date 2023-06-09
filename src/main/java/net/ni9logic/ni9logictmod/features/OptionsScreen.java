package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;

public class OptionsScreen extends Screen {
    public static KeyBinding KEY_CHAT_GAMES;
    private final HashMap<ButtonWidget, String> buttonTooltips = new HashMap<>();
    public static boolean chatGames = false;

    public OptionsScreen() {
        super(Text.empty());
    }

    @Override
    protected void init() {

        int ReactionGame_X = this.width / 2 - 65, ReactionGame_Y = this.height / 2;

        ButtonWidget chatGamesbtn = ButtonWidget.builder(
                        Text.of(""), // Initial empty button text
                        (button) -> {
                            chatGames = !chatGames;
                            button.setMessage(getChatGameBtnStatus(chatGames)); // Set button text based on the current state
                        })
                .dimensions(ReactionGame_X, ReactionGame_Y - 60, 130, 20)
                .build();

        chatGamesbtn.setMessage(getChatGameBtnStatus(chatGames)); // Set initial button text

        this.buttonTooltips.put(this.addDrawableChild(chatGamesbtn), "Start auto solving chat games");

        int Feed_X = this.width / 2 - 65, Feed_Y = this.height / 2;

        ButtonWidget FeedBtn = ButtonWidget.builder(
                        Text.of(""),
                        (button) -> {
                            FeedOwnHunger.isAutoFeed = !FeedOwnHunger.isAutoFeed;
                            button.setMessage(getAutoFeed(FeedOwnHunger.isAutoFeed));
                        })
                .dimensions(Feed_X, Feed_Y - 40, 130, 20)
                .build();

        FeedBtn.setMessage(getAutoFeed(FeedOwnHunger.isAutoFeed));


        int Fix_X = this.width / 2 - 65, Fix_Y = this.height / 2;

        ButtonWidget FixBtn = ButtonWidget.builder(
                        Text.of(""),
                        (button) -> {
                            FixHandItem.isFixHandItem = !FixHandItem.isFixHandItem;
                            button.setMessage(getAutoFix(FixHandItem.isFixHandItem));
                        })
                .dimensions(Fix_X, Fix_Y - 20, 130, 20)
                .build();

        FixBtn.setMessage(getAutoFix(FixHandItem.isFixHandItem));

    }

    private Text getChatGameBtnStatus(boolean isActive) {
        Style activateStyle = Style.EMPTY
                .withColor(Formatting.DARK_GREEN)
                .withItalic(true);
        Style deactivateStyle = Style.EMPTY
                .withColor(Formatting.DARK_RED)
                .withItalic(true);

        MutableText statusText = Text.of(isActive ? "[ACTIVATED]" : "[DEACTIVATED]")
                .copy()
                .setStyle(isActive ? activateStyle : deactivateStyle);

        return Text.of("ChatGames ").copy().append(statusText);
    }

    private Text getAutoFeed(boolean isActive) {
        Style activateStyle = Style.EMPTY
                .withColor(Formatting.DARK_GREEN)
                .withItalic(true);
        Style deactivateStyle = Style.EMPTY
                .withColor(Formatting.DARK_RED)
                .withItalic(true);

        MutableText statusText = Text.of(isActive ? "[ACTIVATED]" : "[DEACTIVATED]")
                .copy()
                .setStyle(isActive ? activateStyle : deactivateStyle);

        return Text.of("FEED ").copy().append(statusText);
    }

    private Text getAutoFix(boolean isActive) {
        Style activateStyle = Style.EMPTY
                .withColor(Formatting.DARK_GREEN)
                .withItalic(true);
        Style deactivateStyle = Style.EMPTY
                .withColor(Formatting.DARK_RED)
                .withItalic(true);

        MutableText statusText = Text.of(isActive ? "[ACTIVATED]" : "[DEACTIVATED]")
                .copy()
                .setStyle(isActive ? activateStyle : deactivateStyle);

        return Text.of("FIX ").copy().append(statusText);
    }

    private void renderHelpingTip(MatrixStack stack, Text text) {
        int x = this.width / 2, y = this.height / 2;

        this.renderOrderedTooltip(stack,
                MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(text.getString()), 270),
                x - 40,
                y + 70);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Style titleStyle = Style.EMPTY
                .withColor(Formatting.DARK_RED) // Set the color to dark red
                .withBold(true) // Apply bold formatting
                .withItalic(true); // Apply italic formatting

        MutableText owner_name = Text.of("Ni9Logic").copy().setStyle(titleStyle);
        MutableText title = Text.of("Chat games mod by ").copy().append(owner_name);


        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        int titleWidth = this.textRenderer.getWidth(title);
        int x = (this.width - titleWidth) / 2;

        this.textRenderer.drawWithShadow(
                matrices,
                title,
                x,
                this.height / 2f - 100,
                0xFFFFFF);


        for (ButtonWidget button : buttonTooltips.keySet()) {
            if (button.active) {
                this.renderHelpingTip(matrices, Text.translatable(this.buttonTooltips.get(button)));
            }
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == KEY_CHAT_GAMES.getDefaultKey().getCode()) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
