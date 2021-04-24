package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.tasks.askToRemoveIsLightningStrike;
import me.lucasvalenca.funnyclasses.tasks.askToRemoveIsSubindo;
import me.lucasvalenca.funnyclasses.tasks.coolDownBombardear;
import me.lucasvalenca.funnyclasses.tasks.coolDownTrovejar;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Errante {
    int nivel;
    Player player;
    FunnyClasses plugin;
    boolean isSubindo;
    int direcao_vertical;
    boolean isStrikingLightning;

    boolean isOnCoolDownTrovejar;
    boolean isOnCoolDownDestruir;

    public Errante(Player player, FunnyClasses plugin) {
        this.player = player;
        this.plugin = plugin;

        this.isOnCoolDownTrovejar = false;
        this.isOnCoolDownDestruir = false;

        this.nivel = 0;
        this.isSubindo = false;
        this.isStrikingLightning = false;
        this.direcao_vertical = 1;
        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        this.player.setHealthScale(10);
    }

    public void carro(Vehicle carro) {

        Vector direction = this.player.getEyeLocation().getDirection();
        Location looking_location  = player.getTargetBlock(null, 100).getLocation();

        double incremento_se_subindo = this.isSubindo ? 0.5 * this.direcao_vertical : 0;
        Vector vector = new Vector(direction.getX(), incremento_se_subindo, direction.getZ());
        carro.setVelocity(vector);

        World world = player.getWorld();
        if (isStrikingLightning) {
            world.strikeLightning(looking_location);
        }
    }

    public void subir_carro() {
        if (!player.isInsideVehicle() && !(player.getVehicle() instanceof Boat)) {
            player.sendMessage("IH, você precisa estar em um barco pra usar esta habilidade!");
            return;
        }

        this.isSubindo = true;
        BukkitTask task = new askToRemoveIsSubindo(this.plugin, this).runTaskLater(this.plugin, 20);
    }

    public void trovejar() {
        if (this.isOnCoolDownTrovejar) {
            this.player.sendMessage("A habilidade está em cooldown");
            return;
        }
        this.isStrikingLightning = true;
        BukkitTask task = new askToRemoveIsLightningStrike(this.plugin, this).runTaskLater(this.plugin, 6);
        this.isOnCoolDownTrovejar = true;
        BukkitTask cd = new coolDownTrovejar(this.plugin, this).runTaskLater(this.plugin, 12 * 20);

    }

    public void takeOffIsSubindo() {
        this.isSubindo = false;
    }

    public void set_direcao_vertical(int direcao) {
        this.direcao_vertical = direcao;
    }

    public void takeOffIsLightningStrike() {
        isStrikingLightning = false;
    }

    public void destruir() {
        if (this.isOnCoolDownDestruir) {
            this.player.sendMessage("A habilidade está em cooldown");
            return;
        }
        Vector direction;
        Location current_location = this.player.getLocation();
        if (this.player.isInsideVehicle() && (this.player.getVehicle() instanceof Boat)) {
            direction = new Vector(0, -2, 0);
        } else {
            direction = this.player.getEyeLocation().getDirection();
        }

        TNTPrimed tnt = this.player.getWorld().spawn(current_location, TNTPrimed.class);
        tnt.setFuseTicks(30);
        tnt.setVelocity(direction.multiply(1));

        this.isOnCoolDownDestruir = true;
        BukkitTask cd = new coolDownBombardear(this.plugin, this).runTaskLater(this.plugin, 6 * 20);
    }

    // CoolDown
    public void eraseCoolDownTrovejar() {
        this.isOnCoolDownTrovejar = false;
        this.player.sendMessage("O coolDown de " + ChatColor.BLUE + "Trovejar" + " terminou");
    }
    public void eraseCoolDownBombardear() {
        this.isOnCoolDownDestruir = false;
        this.player.sendMessage("O coolDown de " + ChatColor.RED + "Bombardear" + " terminou");
    }
}
