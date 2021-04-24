package me.lucasvalenca.funnyclasses.tasks;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.classes.Errante;
import me.lucasvalenca.funnyclasses.classes.Protetor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class coolDownBombardear extends BukkitRunnable {
    FunnyClasses plugin;
    Errante errante;

    public coolDownBombardear(FunnyClasses plugin, Errante errante) {
        this.plugin = plugin;
        this.errante = errante;
    }

    @Override
    public void run() {
        this.errante.eraseCoolDownBombardear();
    }
}
