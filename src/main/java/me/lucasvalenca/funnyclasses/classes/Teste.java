package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.ajuda.Ajuda;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Teste {
    int nivel;
    Player player;
    FunnyClasses plugin;
    World world;

    Ajuda ajuda;

    public Teste(Player player, FunnyClasses plugin) {
        this.player = player;
        this.plugin = plugin;

        this.ajuda = new Ajuda();
    }

    public void testa_1() {
        ajuda.get_nearby_and_apply(this.player,10, 5, 10, "speed", 10, 10);
    }
}
