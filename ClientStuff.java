package com.example.undoo;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class ClientStuff {
    @EventBusSubscriber(modid = UndooMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBusEvents {
        public static final KeyMapping OPEN_MENU = new KeyMapping("key.undoo.open_menu", GLFW.GLFW_KEY_I, "category.undoo");
        @SubscribeEvent public static void regKeys(RegisterKeyMappingsEvent e) { e.register(OPEN_MENU); }
        @SubscribeEvent public static void regScreens(RegisterMenuScreensEvent e) { e.register(UndooMod.TNT_MENU.get(), TntScreen::new); }
    }

    @EventBusSubscriber(modid = UndooMod.MODID, value = Dist.CLIENT)
    public static class GameBusEvents {
        @SubscribeEvent public static void onKey(ClientTickEvent.Post e) {
            while (ModBusEvents.OPEN_MENU.consumeClick()) {
                var p = Minecraft.getInstance().player;
                if (p != null && p.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BigTntItem) PacketDistributor.sendToServer(new OpenTntMenuPacket());
            }
        }
    }

    public static class TntScreen extends AbstractContainerScreen<TntMenu> {
        private float power;
        public TntScreen(TntMenu m, Inventory i, Component t) { super(m, i, t); this.power = BigTntItem.getPower(m.getTntStack()); }
        @Override protected void init() {
            super.init();
            int cx = width / 2, cy = height / 2;
            addRenderableWidget(new AbstractSliderButton(cx - 80, cy - 40, 160, 20, Component.literal("Сила: " + (int) power), (power - 1) / 499.0) {
                @Override protected void updateMessage() { setMessage(Component.literal("Сила: " + (int) (this.value * 499 + 1))); }
                @Override protected void applyValue() { power = (float) (this.value * 499 + 1); BigTntItem.setPower(menu.getTntStack(), power); }
            });
            addRenderableWidget(Button.builder(Component.literal("💥 ВЗОРВАТЬ 💥"), b -> { BigTntItem.setPower(menu.getTntStack(), power); if (minecraft != null && minecraft.player != null) minecraft.player.closeContainer(); }).bounds(cx - 80, cy - 10, 160, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Закрыть"), b -> { if (minecraft != null && minecraft.player != null) minecraft.player.closeContainer(); }).bounds(cx - 40, cy + 30, 80, 20).build());
        }
        @Override protected void renderBg(GuiGraphics g, float p, int mx, int my) {
            int x = (width - 176) / 2, y = (height - 166) / 2;
            g.fill(x, y, x + 176, y + 166, 0xFF1A0033); g.fill(x + 1, y + 1, x + 175, y + 165, 0xFF330066);
            g.fill(x, y, x + 176, y + 2, 0xFFFF00FF); g.fill(x, y + 164, x + 176, y + 166, 0xFFFF00FF);
            g.fill(x, y, x + 2, y + 166, 0xFFFF00FF); g.fill(x + 174, y, x + 176, y + 166, 0xFFFF00FF);
        }
        @Override public void render(GuiGraphics g, int mx, int my, float pt) { renderBackground(g, mx, my, pt); super.render(g, mx, my, pt); renderTooltip(g, mx, my); }
    }
        }
