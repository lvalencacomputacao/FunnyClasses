package me.lucasvalenca.funnyclasses.tasks;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.classes.Arquiteto;
import me.lucasvalenca.funnyclasses.classes.Protetor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class coolDownGanchoDeslocador extends BukkitRunnable {
    FunnyClasses plugin;
    Arquiteto arquiteto;

    public coolDownGanchoDeslocador(FunnyClasses plugin, Arquiteto arquiteto) {
        this.plugin = plugin;
        this.arquiteto = arquiteto;
    }

    @Override
    public void run() {
        this.arquiteto.eraseCoolDownGanchoDeslocador();
    }
}
