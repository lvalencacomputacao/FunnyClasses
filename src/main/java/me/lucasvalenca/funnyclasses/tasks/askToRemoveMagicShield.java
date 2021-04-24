package me.lucasvalenca.funnyclasses.tasks;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.classes.Protetor;
import org.bukkit.scheduler.BukkitRunnable;

public class askToRemoveMagicShield extends BukkitRunnable {
    FunnyClasses plugin;
    Protetor protetor;

    public askToRemoveMagicShield(FunnyClasses plugin, Protetor protetor) {
        this.plugin = plugin;
        this.protetor = protetor;
    }

    @Override
    public void run() {
        this.protetor.removeMagicShield();
    }
}
