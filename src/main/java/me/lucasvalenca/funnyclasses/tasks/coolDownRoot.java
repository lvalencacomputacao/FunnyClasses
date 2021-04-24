package me.lucasvalenca.funnyclasses.tasks;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.classes.Protetor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class coolDownRoot extends BukkitRunnable {
    FunnyClasses plugin;
    Protetor protetor;

    public coolDownRoot(FunnyClasses plugin, Protetor protetor) {
        this.plugin = plugin;
        this.protetor = protetor;
    }

    @Override
    public void run() {
        System.out.println("The task has been run");
        this.protetor.eraseCoolDownRoot();
    }
}
