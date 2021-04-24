package me.lucasvalenca.funnyclasses.tasks;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.classes.Arquiteto;
import me.lucasvalenca.funnyclasses.classes.Protetor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class coolDownMachineGun extends BukkitRunnable {
    FunnyClasses plugin;
    Arquiteto arquiteto;

    public coolDownMachineGun(FunnyClasses plugin, Arquiteto arquiteto) {
        this.plugin = plugin;
        this.arquiteto = arquiteto;
    }

    @Override
    public void run() {
        this.arquiteto.eraseCoolDownMachineGun();
    }
}
