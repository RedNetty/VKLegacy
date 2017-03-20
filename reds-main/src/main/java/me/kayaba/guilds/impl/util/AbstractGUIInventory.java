package me.kayaba.guilds.impl.util;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.*;

public abstract class AbstractGUIInventory implements GUIInventory {
    protected final Inventory inventory;
    private GPlayer viewer;
    protected final PracticeServer plugin = PracticeServer.getInstance();
    private final Set<GUIInventory.Executor> executors = new HashSet<>();


    public AbstractGUIInventory(int size, MessageWrapper title) {
        inventory = ChestGUIUtils.createInventory(size, title);
    }

    @Override
    public final GPlayer getViewer() {
        return viewer;
    }

    @Override
    public final void setViewer(GPlayer nPlayer) {
        this.viewer = nPlayer;
    }

    @Override
    public void registerExecutor(GUIInventory.Executor executor) {
        Validate.notNull(executor.getItem());

        if (executors.contains(executor)) {
            return;
        }

        executors.add(executor);
    }

    @Override
    public Set<GUIInventory.Executor> getExecutors() {
        return executors;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItemStack = event.getCurrentItem();

        for (GUIInventory.Executor executor : new HashSet<>(getExecutors())) {
            if (executor.getItem().equals(clickedItemStack)) {
                executor.execute();
            }
        }
    }

    @Override
    public final Inventory getInventory() {
        return inventory;
    }

    @Override
    public final void open(GPlayer nPlayer) {
        setViewer(nPlayer);
        ChestGUIUtils.openGUIInventory(nPlayer, this);
    }

    @Override
    public void onOpen() {

    }

    @Override
    public final void close() {
        getViewer().getPlayer().closeInventory();
    }


    protected void add(GUIInventory.Executor executor) {
        if (!getExecutors().contains(executor)) {
            throw new IllegalArgumentException("Trying to add not registered executor to the inventory");
        }

        getInventory().addItem(executor.getItem());
    }


    protected void registerAndAdd(GUIInventory.Executor executor) {
        registerExecutor(executor);
        add(executor);
    }


    protected void reopen() {
        close();
        open(getViewer());
    }


    protected void regenerate() {
        inventory.clear();
        getExecutors().clear();
        generateContent();
        ChestGUIUtils.addBackItem(this);
    }

    public abstract class Executor implements GUIInventory.Executor {
        private ItemStack itemStack;


        public Executor(ItemStack itemStack) {
            this.itemStack = itemStack;
        }


        public Executor(MessageWrapper messageWrapper) {
            this(messageWrapper.getItemStack());
        }

        @Override
        public ItemStack getItem() {
            return itemStack;
        }
    }

    public class EmptyExecutor extends Executor {


        public EmptyExecutor(ItemStack itemStack) {
            super(itemStack);
        }


        public EmptyExecutor(MessageWrapper messageWrapper) {
            super(messageWrapper);
        }

        @Override
        public final void execute() {

        }
    }

    public class CommandExecutor extends Executor {
        private final String command;
        private final boolean close;


        public CommandExecutor(ItemStack itemStack, String command, boolean close) {
            super(itemStack);
            this.command = command;
            this.close = close;
        }


        public CommandExecutor(MessageWrapper messageWrapper, String command, boolean close) {
            this(messageWrapper.getItemStack(), command, close);
        }

        @Override
        public void execute() {
            Bukkit.dispatchCommand(getViewer().getPlayer(), command);

            if (close) {
                close();
            }
        }
    }
}
