package com.example.undoo;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

class NetherPortalItem extends Item {
    public NetherPortalItem(Properties p) { super(p); }
    @Override public InteractionResultHolder<ItemStack> use(Level l, Player p, InteractionHand h) {
        if (p instanceof ServerPlayer sp) {
            var n = sp.getServer().getLevel(Level.NETHER);
            if (n != null) sp.teleportTo(n, (int) sp.getX() / 8, 100, (int) sp.getZ() / 8, sp.getYRot(), sp.getXRot());
        }
        return InteractionResultHolder.success(p.getItemInHand(h));
    }
}

class EndPortalItem extends Item {
    public EndPortalItem(Properties p) { super(p); }
    @Override public InteractionResultHolder<ItemStack> use(Level l, Player p, InteractionHand h) {
        if (p instanceof ServerPlayer sp) {
            var e = sp.getServer().getLevel(Level.END);
            if (e != null) sp.teleportTo(e, 1000, 100, 0, sp.getYRot(), sp.getXRot());
        }
        return InteractionResultHolder.success(p.getItemInHand(h));
    }
}

class MegaEnchanterItem extends Item {
    public MegaEnchanterItem(Properties p) { super(p); }
    @Override public InteractionResultHolder<ItemStack> use(Level l, Player p, InteractionHand h) {
        if (!l.isClientSide) {
            ItemStack t = p.getOffhandItem();
            if (!t.isEmpty()) {
                var reg = l.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
                var mut = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                reg.stream().forEach(e -> mut.set(e, 255));
                t.set(DataComponents.ENCHANTMENTS, mut.toImmutable());
            }
        }
        return InteractionResultHolder.success(p.getItemInHand(h));
    }
}
