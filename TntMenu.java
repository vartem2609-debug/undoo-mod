package com.example.undoo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TntMenu extends AbstractContainerMenu {
    private final ItemStack tntStack;
    public TntMenu(int id, Inventory inv, FriendlyByteBuf buf) { this(id, inv, buf.readItem()); }
    public TntMenu(int id, Inventory inv, ItemStack stack) {
        super(UndooMod.TNT_MENU.get(), id);
        this.tntStack = stack;
        Container container = new SimpleContainer(1);
        this.addSlot(new Slot(container, 0, 80, 20) { @Override public boolean mayPlace(ItemStack s) { return false; } @Override public ItemStack getItem() { return tntStack; } });
        for (int i = 0; i < 3; ++i) { for (int j = 0; j < 9; ++j) { this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18)); } }
        for (int k = 0; k < 9; ++k) { this.addSlot(new Slot(inv, k, 8 + k * 18, 142)); }
    }
    public ItemStack getTntStack() { return tntStack; }
    @Override public ItemStack quickMoveStack(Player p, int i) { return ItemStack.EMPTY; }
    @Override public boolean stillValid(Player p) { return true; }
}
