package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.tasks.askToRemoveIsLightningStrike;
import me.lucasvalenca.funnyclasses.tasks.askToRemoveIsSubindo;
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

    public Errante(Player player, FunnyClasses plugin) {
        this.player = player;
        this.plugin = plugin;

        this.nivel = 0;
        this.isSubindo = false;
        this.isStrikingLightning = false;
        this.direcao_vertical = 1;
        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        this.player.setHealthScale(10);
    }

    public void carro(Vehicle carro) {

        Location looking_location  = player.getTargetBlock(null, 1000).getLocation();
        Location current_location = player.getLocation();

        Vector posFinal = new Vector(looking_location.getX(), 0, looking_location.getZ());
        Vector posInicial = new Vector(current_location.getX(), 0, current_location.getZ());

        Vector direction = posFinal.subtract(posInicial).normalize();

        double incremento_se_subindo = this.isSubindo ? 0.5 * this.direcao_vertical : 0;
        Vector vector = new Vector(direction.getX(), incremento_se_subindo, direction.getZ());
        carro.setVelocity(vector);

        World world = player.getWorld();
        if (isStrikingLightning) {
            world.strikeLightning(looking_location);
        }

        //carro.setVelocity(carro.getVelocity().multiply(4));
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
        isStrikingLightning = true;
        BukkitTask task = new askToRemoveIsLightningStrike(this.plugin, this).runTaskLater(this.plugin, 6);
    }

    public void takeOffIsSubindo() {
        this.isSubindo = false;
        player.sendMessage("Deu certo");
    }

    public void set_direcao_vertical(int direcao) {
        this.direcao_vertical = direcao;
    }

    public void takeOffIsLightningStrike() {
        isStrikingLightning = false;
        player.sendMessage("Deu certo");
    }

    public void destruir() {
        Location looking_location  = player.getTargetBlock(null, 1000).getLocation();
        Location current_location = player.getLocation();

        Vector posFinal = new Vector(looking_location.getX(), looking_location.getY(), looking_location.getZ());
        Vector posInicial = new Vector(current_location.getX(), current_location.getY(), current_location.getZ());

        Vector direction = posFinal.subtract(posInicial).normalize();
        TNTPrimed tnt = player.getWorld().spawn(current_location, TNTPrimed.class);
        tnt.setFuseTicks(30);
        tnt.setVelocity(direction.multiply(1));
    }
}
