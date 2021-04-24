package me.lucasvalenca.funnyclasses.tasks;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.classes.Protetor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class coolDownPickThrow extends BukkitRunnable {
    FunnyClasses plugin;
    Protetor protetor;

    public coolDownPickThrow(FunnyClasses plugin, Protetor protetor) {
        this.plugin = plugin;
        this.protetor = protetor;
    }

    @Override
    public void run() {
        this.protetor.eraseCoolDownPickThrow();
    }
}

