package me.kayaba.guilds.util;

import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;

public class EnchantmentGlow extends Enchantment {

    public EnchantmentGlow() {
        super(150);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }
}
