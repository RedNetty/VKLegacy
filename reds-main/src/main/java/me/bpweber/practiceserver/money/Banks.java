package me.bpweber.practiceserver.money;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.money.Economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Banks implements Listener {
	public static HashMap<Player, String> banksee = new HashMap<Player, String>();
	List<String> withdraw = new ArrayList<String>();

	public void onEnable() {
		PracticeServer.log.info("[Banks] has been enabled.");
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
		File file = new File(PracticeServer.plugin.getDataFolder(), "banks");
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void onDisable() {
		PracticeServer.log.info("[Banks] has been disabled.");
		File file = new File(PracticeServer.plugin.getDataFolder(), "banks");
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
			e.setCancelled(true);
			Inventory inv = Banks.getBank(p);
			if (inv == null) {
				inv = Bukkit.createInventory((InventoryHolder) null, (int) 63, (String) "Bank Chest (1/1)");
			}
			p.openInventory(inv);
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (e.getInventory().getName().equals("Bank Chest (1/1)")) {
			this.saveBank(e.getInventory(), p);
			new BukkitRunnable() {

				public void run() {
					Banks.this.saveBank(e.getInventory(), p);
					if (Banks.banksee.containsKey(p)) {
						Banks.banksee.remove(p);
					}
				}
			}.runTaskLater(PracticeServer.plugin, 1);
		}
	}

	@EventHandler
	public void onClickSave(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().equals("Bank Chest (1/1)")) {
			this.saveBank(e.getInventory(), p);
			new BukkitRunnable() {

				public void run() {
					Banks.this.saveBank(e.getInventory(), p);
				}
			}.runTaskLater(PracticeServer.plugin, 1);
		}
	}

	public static ItemStack makeGems(final int amount) {
		final ItemStack i = new ItemStack(Material.EMERALD, amount);
		final List<String> new_lore = new ArrayList<String>(
				Arrays.asList(String.valueOf(ChatColor.GRAY.toString()) + "The currency of Andalucia"));
		final ItemMeta im = i.getItemMeta();
		im.setLore((List<String>) new_lore);
		im.setDisplayName(String.valueOf(ChatColor.WHITE.toString()) + "Gem");
		i.setItemMeta(im);
		i.setAmount(amount);
		return i;
	}

	public void saveBank(Inventory inv, Player p) {
		String name = p.getName();
		if (banksee.containsKey(p)) {
			name = banksee.get(p);
		}
		File file = new File(PracticeServer.plugin.getDataFolder() + "/banks", String.valueOf(name) + ".yml");
		YamlConfiguration config = new YamlConfiguration();
		int i = 0;
		while (i < inv.getSize()) {
			if (inv.getItem(i) != null) {
				config.set("" + i, inv.getItem(i));
			}
			++i;
		}
		try {
			config.save(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Inventory getBank(Player p) {
		File file;
		String name = p.getName();
		if (banksee.containsKey(p)) {
			name = banksee.get(p);
		}
		if (!(file = new File(PracticeServer.plugin.getDataFolder() + "/banks", String.valueOf(name) + ".yml")).exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 63, (String) "Bank Chest (1/1)");
		int i = 0;
		while (i < inv.getSize()) {
			if (config.contains("" + i)) {
				inv.setItem(i, config.getItemStack("" + i));
			}
			++i;
		}
		return inv;
	}

	@EventHandler
	public void onInvOpen(InventoryOpenEvent e) {
		if (e.getInventory().getName().equals("Bank Chest (1/1)")) {
			Inventory inv = e.getInventory();
			ItemStack glass = new ItemStack(Material.THIN_GLASS);
			ItemMeta meta = glass.getItemMeta();
			meta.setDisplayName(" ");
			glass.setItemMeta(meta);
			int i = 54;
			while (i < 63) {
				inv.setItem(i, glass);
				++i;
			}
			ItemStack gem = new ItemStack(Material.EMERALD);
			ItemMeta im = gem.getItemMeta();
			im.setDisplayName(ChatColor.GREEN.toString() + (int) Economy.getBalance(e.getPlayer().getUniqueId())
					+ (Object) ChatColor.GREEN + ChatColor.BOLD.toString() + " GEM(s)");
			im.setLore(Arrays.asList(ChatColor.GRAY + "Right Click to create " + ChatColor.GREEN + "A GEM NOTE"));
			gem.setItemMeta(im);
			inv.setItem(58, gem);
		}
	}

	int getGems(ItemStack is) {
		List<String> lore;
		if (is != null && is.getType() != Material.AIR && is.getType() == Material.PAPER && is.getItemMeta().hasLore()
				&& (lore = is.getItemMeta().getLore()).size() > 0 && ((String) lore.get(0)).contains("Value")) {
			try {
				String line = ChatColor.stripColor((String) ((String) lore.get(0)));
				return Integer.parseInt(line.split(": ")[1].split(" Gems")[0]);
			} catch (Exception e) {
				return 0;
			}
		}
		return 0;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPromptAmount(final AsyncPlayerChatEvent e) {
		final Player p = e.getPlayer();
		if (this.withdraw.contains(p.getName())) {
			e.setCancelled(true);
			final String message = e.getMessage();
			if (e.getMessage().equalsIgnoreCase("cancel") && this.withdraw.contains(p.getName())) {
				for (int i = 0; i < this.withdraw.size(); ++i) {
					this.withdraw.remove(p.getName());
				}
				p.sendMessage(ChatColor.RED + "Withdrawl operation - " + ChatColor.BOLD + "CANCELLED");
				return;
			}
			int amt = 0;
			try {
				amt = Integer.parseInt(message);
				if (amt > Economy.getBalance(p.getUniqueId())) {
					p.sendMessage(ChatColor.GRAY + "You cannot withdraw more GEMS than you have stored.");
				} else if (amt <= 0) {
					p.sendMessage(ChatColor.RED + "You must enter a POSIVIVE amount.");
				} else {
					for (int j = 0; j < this.withdraw.size(); ++j) {
						this.withdraw.remove(p.getName());
					}
					Economy.withdrawPlayer(p.getUniqueId(), amt);
					if (p.getInventory().firstEmpty() != -1) {
						p.getInventory().setItem(p.getInventory().firstEmpty(), Money.createBankNote(amt));
					}
					p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD)
							.append("New Balance: ").append(ChatColor.GREEN)
							.append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)").toString());
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 1.2f);
				}
			} catch (NumberFormatException ex) {
				p.sendMessage(ChatColor.RED
						+ "Please enter a NUMBER, the amount you'd like to WITHDRAW from your bank account. Or type 'cancel' to void the withdrawl.");
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvClick(final InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PAPER
				&& e.getCurrentItem().getItemMeta().hasLore() && e.getCursor().getType() == Material.PAPER
				&& e.getCursor().getItemMeta().hasLore()) {
			e.setCancelled(true);
			final int first = this.getGems(e.getCurrentItem());
			final int second = this.getGems(e.getCursor());
			final ItemStack gem = new ItemStack(Material.PAPER);
			final ItemMeta im = gem.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Bank Note");
			im.setLore((List<String>) Arrays.asList(
					new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append("Value: ")
							.append(ChatColor.WHITE).append(first + second).append(" Gems").toString(),
					ChatColor.GRAY + "Exchange at any bank for GEM(s)"));
			gem.setItemMeta(im);
			e.setCurrentItem(gem);
			e.setCursor((ItemStack) null);
			p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 1.2f);
		}
		if (e.getInventory().getName().equals("Bank Chest (1/1)")) {
			if (e.getClick() == ClickType.RIGHT && e.getSlot() == 58) {
				e.setCancelled(true);
				p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD)
						.append("Current Balance: ").append(ChatColor.GREEN)
						.append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)").toString());
				for (int i = 0; i < this.withdraw.size(); ++i) {
					this.withdraw.remove(p.getName());
				}
				this.withdraw.add(p.getName());
				p.sendMessage(ChatColor.GRAY
						+ "Please enter the amount you'd like To CONVERT into a gem note. Alternatively, type "
						+ ChatColor.RED + "'cancel'" + ChatColor.GRAY + " to void this operation.");
				new BukkitRunnable() {
					public void run() {
						p.closeInventory();
					}
				}.runTaskLater(PracticeServer.plugin, 1L);
			}
			for (int i = 54; i < 63; ++i) {
				if (e.getSlot() == i) {
					e.setCancelled(true);
				}
			}
			if (e.getSlotType() == InventoryType.SlotType.OUTSIDE) {
				return;
			}
			if (e.isShiftClick() && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.EMERALD) {
				if (e.getRawSlot() < 63) {
					return;
				}
				final int amt = e.getCurrentItem().getAmount();
				e.setCancelled(true);
				Economy.depositPlayer(p.getUniqueId(), amt);
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				e.getInventory().setItem(58, this.gemBalance((int) Economy.getBalance(p.getUniqueId())));
				p.updateInventory();
				p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("+")
						.append(ChatColor.GREEN).append(amt).append(ChatColor.GREEN).append(ChatColor.BOLD).append("G")
						.append(ChatColor.GREEN).append(", ").append(ChatColor.BOLD).append("New Balance: ")
						.append(ChatColor.GREEN).append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)")
						.toString());
				e.setCurrentItem((ItemStack) null);
			}
			if (e.isShiftClick() && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PAPER) {
				if (e.getRawSlot() < 63) {
					return;
				}
				final int amt = this.getGems(e.getCurrentItem());
				e.setCancelled(true);
				Economy.depositPlayer(p.getUniqueId(), amt);
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				e.getInventory().setItem(58, this.gemBalance((int) Economy.getBalance(p.getUniqueId())));
				p.updateInventory();
				p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("+")
						.append(ChatColor.GREEN).append(amt).append(ChatColor.GREEN).append(ChatColor.BOLD).append("G")
						.append(ChatColor.GREEN).append(", ").append(ChatColor.BOLD).append("New Balance: ")
						.append(ChatColor.GREEN).append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)")
						.toString());
				e.setCurrentItem((ItemStack) null);
			}
			if (e.isShiftClick() && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.INK_SACK
					&& e.getCurrentItem().getDurability() == 0) {
				if (e.getRawSlot() < 63) {
					return;
				}
				final int amt = GemPouches.getCurrentValue(e.getCurrentItem());
				if (amt < 1) {
					if (e.getInventory().firstEmpty() != -1) {
						e.getInventory().setItem(e.getInventory().firstEmpty(), e.getCurrentItem());
						e.setCurrentItem((ItemStack) null);
					}
					return;
				}
				e.setCancelled(true);
				Economy.depositPlayer(p.getUniqueId(), amt);
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				e.getInventory().setItem(58, this.gemBalance((int) Economy.getBalance(p.getUniqueId())));
				p.updateInventory();
				p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("+")
						.append(ChatColor.GREEN).append(amt).append(ChatColor.GREEN).append(ChatColor.BOLD).append("G")
						.append(ChatColor.GREEN).append(", ").append(ChatColor.BOLD).append("New Balance: ")
						.append(ChatColor.GREEN).append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)")
						.toString());
				GemPouches.setPouchBal(e.getCurrentItem(), 0);
			}
			if (!e.isShiftClick() && e.getCursor() != null && e.getCursor().getType() == Material.EMERALD) {
				if (e.getRawSlot() > 53) {
					return;
				}
				final int amt = e.getCursor().getAmount();
				e.setCancelled(true);
				Economy.depositPlayer(p.getUniqueId(), amt);
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				e.getInventory().setItem(58, this.gemBalance((int) Economy.getBalance(p.getUniqueId())));
				p.updateInventory();
				p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("+")
						.append(ChatColor.GREEN).append(amt).append(ChatColor.GREEN).append(ChatColor.BOLD).append("G")
						.append(ChatColor.GREEN).append(", ").append(ChatColor.BOLD).append("New Balance: ")
						.append(ChatColor.GREEN).append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)")
						.toString());
				e.setCursor((ItemStack) null);
			}
			if (!e.isShiftClick() && e.getCursor() != null && e.getCursor().getType() == Material.PAPER) {
				if (e.getRawSlot() > 53) {
					return;
				}
				final int amt = this.getGems(e.getCursor());
				e.setCancelled(true);
				Economy.depositPlayer(p.getUniqueId(), amt);
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				e.getInventory().setItem(58, this.gemBalance((int) Economy.getBalance(p.getUniqueId())));
				p.updateInventory();
				p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("+")
						.append(ChatColor.GREEN).append(amt).append(ChatColor.GREEN).append(ChatColor.BOLD).append("G")
						.append(ChatColor.GREEN).append(", ").append(ChatColor.BOLD).append("New Balance: ")
						.append(ChatColor.GREEN).append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)")
						.toString());
				e.setCursor((ItemStack) null);
			}
			if (!e.isShiftClick() && e.getCursor() != null && e.getCursor().getType() == Material.INK_SACK
					&& e.getCursor().getDurability() == 0) {
				if (e.getRawSlot() > 53) {
					return;
				}
				final int amt = GemPouches.getCurrentValue(e.getCursor());
				if (amt < 1) {
					return;
				}
				e.setCancelled(true);
				Economy.depositPlayer(p.getUniqueId(), amt);
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
				e.getInventory().setItem(58, this.gemBalance((int) Economy.getBalance(p.getUniqueId())));
				p.updateInventory();
				p.sendMessage(new StringBuilder().append(ChatColor.GREEN).append(ChatColor.BOLD).append("+")
						.append(ChatColor.GREEN).append(amt).append(ChatColor.GREEN).append(ChatColor.BOLD).append("G")
						.append(ChatColor.GREEN).append(", ").append(ChatColor.BOLD).append("New Balance: ")
						.append(ChatColor.GREEN).append((int) Economy.getBalance(p.getUniqueId())).append(" GEM(s)")
						.toString());
				GemPouches.setPouchBal(e.getCursor(), 0);
			}
		}
	}

	ItemStack gemBalance(final int amt) {
		final ItemStack is = new ItemStack(Material.EMERALD);
		final ItemMeta im = is.getItemMeta();
		im.setDisplayName(new StringBuilder().append(ChatColor.GREEN).append(amt).append(ChatColor.GREEN)
				.append(ChatColor.BOLD).append(" GEM(s)").toString());
		im.setLore((List<String>) Arrays
				.asList(ChatColor.GRAY + "Right Click to create " + ChatColor.GREEN + "A GEM NOTE"));
		is.setItemMeta(im);
		return is;
	}

}