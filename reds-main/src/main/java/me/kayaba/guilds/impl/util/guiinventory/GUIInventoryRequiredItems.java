package me.kayaba.guilds.impl.util.guiinventory;

import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class GUIInventoryRequiredItems extends AbstractGUIInventory {
    private final List<ItemStack> requiredItems = new ArrayList<>();


    public GUIInventoryRequiredItems(List<ItemStack> itemStackList) {
        super(ChestGUIUtils.getChestSize(itemStackList.size()), Message.INVENTORY_REQUIREDITEMS_NAME);
        requiredItems.addAll(itemStackList);
    }

    @Override
    public void generateContent() {
        for (ItemStack item : requiredItems) {
            int amountInventory = InventoryUtils.getTotalAmountOfItemStackInInventory(getViewer().getPlayer().getInventory(), item);
            int amountEnderChest = InventoryUtils.getTotalAmountOfItemStackInInventory(getViewer().getPlayer().getEnderChest(), item);
            int needMore = item.getAmount() - amountEnderChest - amountInventory;

            if (needMore < 0) {
                needMore = 0;
            }

            ItemMeta itemStackMeta = item.hasItemMeta()
                    ? item.getItemMeta()
                    : Bukkit.getItemFactory().getItemMeta(item.getType());

            List<String> lore = new ArrayList<>();

            if (itemStackMeta.hasLore()) {
                lore.addAll(itemStackMeta.getLore());
            }

            lore.addAll(Message.INVENTORY_REQUIREDITEMS_LORE
                    .clone()
                    .setVar(VarKey.AMOUNT_AVAILABLE, amountInventory)
                    .setVar(VarKey.AMOUNT_AVAILABLE2, amountEnderChest)
                    .setVar(VarKey.AMOUNT_AVAILABLE3, amountInventory + amountEnderChest)
                    .setVar(VarKey.AMOUNT, item.getAmount())
                    .setVar(VarKey.NEEDMORE, needMore)
                    .getList());

            itemStackMeta.setLore(lore);
            item.setItemMeta(itemStackMeta);

            registerAndAdd(new EmptyExecutor(item));
        }
    }
}
