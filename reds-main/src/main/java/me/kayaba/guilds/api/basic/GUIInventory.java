package me.kayaba.guilds.api.basic;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.*;

public interface GUIInventory {

    void onClick(InventoryClickEvent event);


    Inventory getInventory();


    void open(GPlayer nPlayer);


    void onOpen();


    void generateContent();


    GPlayer getViewer();


    void setViewer(GPlayer nPlayer);


    void registerExecutor(Executor executor);


    Set<Executor> getExecutors();


    void close();

    interface Executor {

        ItemStack getItem();


        void execute();
    }
}
