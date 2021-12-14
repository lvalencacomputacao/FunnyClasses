package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Melancia {
    int nivel;
    FunnyClasses plugin;
    Player player;

    public Melancia(Player player, FunnyClasses plugin) {
        this.player = player;
        this.plugin = plugin;
        this.nivel = 0;

        this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 999));
        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        this.player.setHealthScale(40);
    }
}
