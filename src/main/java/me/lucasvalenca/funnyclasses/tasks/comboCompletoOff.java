package me.lucasvalenca.funnyclasses.tasks;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.classes.Sonhador;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class comboCompletoOff extends BukkitRunnable {
    FunnyClasses plugin;
    Sonhador sonhador;

    public comboCompletoOff(FunnyClasses plugin, Sonhador sonhador) {
        this.plugin = plugin;
        this.sonhador = sonhador;
    }

    @Override
    public void run() {
        this.sonhador.eraseComboCompleto();
    }
}
