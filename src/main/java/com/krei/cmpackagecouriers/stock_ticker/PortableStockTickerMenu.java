package com.krei.cmpackagecouriers.stock_ticker;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import static com.krei.cmpackagecouriers.stock_ticker.LogisticallyLinkedItem.isTuned;

// Shamelessly copied from Create: Mobile Packages
public class PortableStockTickerMenu extends AbstractContainerMenu {
    public PortableStockTicker portableStockTicker;
    public Object screenReference;
    public Player player;

    public PortableStockTickerMenu(int id, Inventory playerInventory) {
        super(PortableStockTickerReg.PORTABLE_STOCK_TICKER_MENU.get(), id);
        ItemStack stack = PortableStockTicker.find(playerInventory);
        if (stack != null && stack.getItem() instanceof PortableStockTicker pst) {
            if (!isTuned(stack)) {
                playerInventory.player.displayClientMessage(Component.translatable("item.cmpackagecouriers.portable_stock_ticker.not_linked"), true);
            }
            this.portableStockTicker = pst;
        }
        this.player = playerInventory.player;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
