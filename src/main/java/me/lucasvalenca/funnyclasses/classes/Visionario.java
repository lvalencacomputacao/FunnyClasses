package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class Visionario {
    int nivel;
    Player player;
    FunnyClasses plugin;
    World world;

    boolean isOnCoolDownAtirar;
    boolean isOnCoolDownExplodir;
    boolean isOnCoolDownTiroDeslocador;
    boolean isOnCoolDownTeleport;

    AbstractArrow flecha_deslocadora;
    Location tp_loc;
    float tp_yaw;
    float tp_pitch;

    public Visionario(Player player, FunnyClasses plugin) {
        this.player = player;
        this.world = player.getWorld();
        this.plugin = plugin;
        this.nivel = 0;
        this.flecha_deslocadora = null;
        this.tp_loc = null;
        this.tp_yaw = 0;
        this.tp_pitch = 0;

        this.isOnCoolDownAtirar = false;
        this.isOnCoolDownExplodir = false;
        this.isOnCoolDownTiroDeslocador = false;
        this.isOnCoolDownTeleport = false;

        this.player.setHealthScale(10);
        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
    }
    public void atirar() {
        if (this.isOnCoolDownAtirar) {
            return;
        }
        this.isOnCoolDownAtirar = true;
        Vector direction = this.player.getEyeLocation().getDirection();
        this.player.launchProjectile(AbstractArrow.class, direction.multiply(4)).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Visionario.this.isOnCoolDownAtirar = false;
            }
        }, 20L);
    }

    public void explodir() {
        if (this.isOnCoolDownExplodir) {
            this.player.sendMessage("A habilidade está em cooldown");
            return;
        }
        this.isOnCoolDownExplodir = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Visionario.this.isOnCoolDownExplodir = false;
                Visionario.this.player.sendMessage("O coolDown de " + ChatColor.RED + "Flechas Explosivas" + " terminou");
            }
        }, 45*20L);

        List<Entity> entidades = this.player.getNearbyEntities(15,15, 15);
        for (Entity entidade : entidades) {
            if (entidade instanceof Arrow) {
                this.world.createExplosion(entidade.getLocation(),2);
                entidade.remove();
            }
        }
    }

    public void tiro_deslocador() {
        if (this.isOnCoolDownTiroDeslocador) {
            this.player.sendMessage("A habilidade está em cooldown");
            return;
        }
        this.isOnCoolDownTiroDeslocador = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Visionario.this.isOnCoolDownTiroDeslocador = false;
                Visionario.this.player.sendMessage("O coolDown de " + ChatColor.LIGHT_PURPLE + "Flecha deslocadora" + " terminou");
            }
        }, 15*20L);
        Vector direction = this.player.getEyeLocation().getDirection();
        this.flecha_deslocadora = this.player.launchProjectile(AbstractArrow.class, direction.multiply(4));
        this.flecha_deslocadora.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Vector direction = (Visionario.this.flecha_deslocadora.getLocation().toVector().subtract(Visionario.this.player.getLocation().toVector())).normalize();
                Visionario.this.player.setVelocity(direction.multiply(5));
                Visionario.this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 4 * 20, 0));
            }
        }, 2*20L);
    }

    public void teleport() {
        if (this.isOnCoolDownTeleport) {
            this.player.sendMessage("A habilidade está em cooldown");
            return;
        }
        this.isOnCoolDownTeleport = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Visionario.this.isOnCoolDownTeleport = false;
                Visionario.this.player.sendMessage("O coolDown de " + ChatColor.YELLOW + "Teleporte" + " terminou");
            }
        }, 15*20L);
        this.tp_loc = this.player.getLocation();
        this.tp_yaw = this.tp_loc.getYaw();
        this.tp_pitch = this.tp_loc.getPitch();

        this.player.sendMessage("Localização gravada!");

        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
                Visionario.this.player.teleport(tp_loc);
                Visionario.this.player.getLocation().setYaw(Visionario.this.tp_yaw);
                Visionario.this.player.getLocation().setPitch(Visionario.this.tp_pitch);
                Visionario.this.player.sendMessage("Retornando!");
            }
        }, 3*20L);
    }
}
