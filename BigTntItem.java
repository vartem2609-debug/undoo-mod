package com.example.undoo;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import java.util.List;

public class BigTntItem extends Item {
    public BigTntItem(Properties p) { super(p); }
    public static float getPower(ItemStack s) { return s.getOrDefault(ModDataComponents.EXPLOSION_POWER.get(), 4f); }
    public static void setPower(ItemStack s, float p) { s.set(ModDataComponents.EXPLOSION_POWER.get(), Math.max(1f, Math.min(500f, p))); }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) level.explode(null, player.getX(), player.getY(), player.getZ(), getPower(stack), Level.ExplosionInteraction.TNT);
            return InteractionResultHolder.success(stack);
        }
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            sp.openMenu(new SimpleMenuProvider((id, inv, p) -> new TntMenu(id, inv, stack), Component.literal("Сила взрыва").withStyle(ChatFormatting.LIGHT_PURPLE)), buf -> buf.writeItem(stack));
        }
        return InteractionResultHolder.success(stack);
    }
    @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
        t.add(Component.literal("Сила: " + (int) getPower(s)).withStyle(ChatFormatting.LIGHT_PURPLE));
        t.add(Component.literal("ПКМ — меню | Shift+ПКМ — взрыв").withStyle(ChatFormatting.GRAY));
    }
}
