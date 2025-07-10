package OP.offlinePlayers.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;
    
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }
    
    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }
    
    public ItemBuilder setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    public ItemBuilder setLore(String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    public ItemBuilder addLoreLine(String line) {
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add(line);
        meta.setLore(lore);
        return this;
    }
    
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }
    
    public ItemBuilder addItemFlag(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }
    
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }
    
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}