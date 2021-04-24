package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.ajuda.Ajuda;
import me.lucasvalenca.funnyclasses.tasks.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import java.lang.Math;
import java.util.List;

public class Sonhador {
    int nivel;
    Player player;
    FunnyClasses plugin;
    AttributeInstance moveSpeed;
    boolean isOnCoolDownSuperPulo;
    boolean isOnCoolDownTeleportar;
    boolean isOnCoolDownDash;
    boolean isOnCoolDownTNTCombo;
    boolean isOnCoolDownPuxao;

    boolean firstCombo;
    boolean secondCombo;
    boolean thirdCombo;
    boolean comboCompleto;

    public Sonhador(Player player, FunnyClasses plugin) {
        this.player = player;
        this.plugin = plugin;
        this.nivel = 0;
        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(12);
        this.player.setHealthScale(12);
        this.moveSpeed = this.player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        this.moveSpeed.setBaseValue(moveSpeed.getBaseValue() * 1.15);
        this.isOnCoolDownSuperPulo = false;
        this.isOnCoolDownTeleportar = false;
        this.isOnCoolDownDash = false;
        this.isOnCoolDownTNTCombo = false;
        this.isOnCoolDownPuxao = false;

        this.firstCombo = false;
        this.secondCombo = false;
        this.thirdCombo = false;
        this.comboCompleto = false;
    }

    public void superPulo() {
        if (isOnCoolDownSuperPulo) {
            player.sendMessage("A habilidade está em cooldown");
        } else {
            //this.player.getWorld().playSound(this.player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 2);
            Vector velocidade = this.player.getVelocity();
            this.player.setVelocity(new Vector(velocidade.getX(), 3.0, velocidade.getZ()));
            isOnCoolDownSuperPulo = true;
            BukkitTask taskSuperPulo = new coolDownSuperPulo(this.plugin, this).runTaskLater(this.plugin, 20 * 20);
        }
    }

    public void teleportar() {
        if (isOnCoolDownTeleportar) {
            if (isOnCoolDownTNTCombo) {
                //this.player.sendMessage("A Habilidade (e combo) estão em cooldown");
                return;
            }
            return;
        } else {
            Block block = player.getTargetBlock(null, 150);
            Location locationToTeleport = block.getLocation();
            locationToTeleport.setY(locationToTeleport.getY() + 1);
            float currentPitch = player.getLocation().getPitch();
            float currentYaw = player.getLocation().getYaw();
            locationToTeleport.setPitch(currentPitch);
            locationToTeleport.setYaw(currentYaw);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2 * 20, 5));
            player.teleport(locationToTeleport);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8 * 20, 0));

            World world = player.getWorld();

            double x = locationToTeleport.getX();
            double y = locationToTeleport.getY();
            double z = locationToTeleport.getZ();

            int radius = 5;
            for (int i = 0; i < 3; i++) {
                world.strikeLightning(new Location(world, x + i - 1, y, z + radius));
                world.strikeLightning(new Location(world, x + i - 1, y, z - radius));
            }
            for (int i = 0; i < 3; i++) {
                world.strikeLightning(new Location(world, x + radius, y, z + i - 1));
                world.strikeLightning(new Location(world, x - radius, y, z + i - 1));
            }
            this.isOnCoolDownTeleportar = true;
            BukkitTask taskTeleportar = new coolDownTeleportar(this.plugin, this).runTaskLater(this.plugin, 20 * 20);
            this.firstCombo = true;
            BukkitTask task_first = new firstComboOff(this.plugin, this).runTaskLater(this.plugin, 2 * 20);
        }
    }

    public void TNTCombo(Entity damaged) {
        if (this.isOnCoolDownTNTCombo) {
            return;
        }
        if (!this.firstCombo && !this.secondCombo && !this.thirdCombo) {
            return;
        }
        if (this.player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
            this.player.removePotionEffect(PotionEffectType.ABSORPTION);
        }
        boolean resetarCoolDown, resetarCoolDown2;
        resetarCoolDown = this.firstCombo;
        resetarCoolDown2 = this.secondCombo;

        if (this.thirdCombo) {
            player.sendMessage(ChatColor.RED + "TERCEIRO!");
            this.player.removePotionEffect(PotionEffectType.SPEED);
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8 * 20, 4));
            this.player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 8 * 20, 3));
            this.thirdCombo = false;
            this.comboCompleto = true;
            player.sendMessage("<Combo Completo>");
            BukkitTask task_completo = new comboCompletoOff(this.plugin, this).runTaskLater(this.plugin, 2 * 20);
        }

        if (resetarCoolDown2) {
            player.sendMessage("Segundo!");
            this.player.removePotionEffect(PotionEffectType.SPEED);
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8 * 20, 2));
            this.player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 8 * 20, 1));
            this.secondCombo = false;
            this.thirdCombo = true;
            BukkitTask task_third = new thirdComboOff(this.plugin, this).runTaskLater(this.plugin, 2 * 20);

        }
        if (resetarCoolDown) {
            this.player.removePotionEffect(PotionEffectType.SPEED);
            this.firstCombo = false;
            this.secondCombo = true;
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8 * 20, 1));
            BukkitTask task_second = new secondComboOff(this.plugin, this).runTaskLater(this.plugin, 2 * 20);
            player.sendMessage("Primeiro...");
        }
        //player.sendMessage(damaged.getName());
        float player_yaw = this.player.getLocation().getYaw();
        Location location_damaged = damaged.getLocation();
        double x = location_damaged.getX();
        double y = location_damaged.getY() + 6;
        double z = location_damaged.getZ();
        Location player_location = new Location(this.player.getWorld(), x, y, z);
        player_location.setPitch(90);
        player_location.setYaw(player_yaw);
        this.player.teleport(player_location);
        //TNTPrimed tnt = this.player.getWorld().spawn(damaged.getLocation(), TNTPrimed.class);
        //tnt.setFuseTicks(5);
        this.player.getWorld().createExplosion(damaged.getLocation(), 2, false, false);
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2 * 20, 2));
        //this.isOnCoolDownTNTCombo = !(resetarCoolDown || resetarCoolDown2);
        //BukkitTask task = new coolDownTNTCombo(this.plugin, this).runTaskLater(this.plugin, 20 * 20);

    }

    public void dash(Boolean lightmode) {
        if (this.isOnCoolDownDash) {
            player.sendMessage("A habilidade está em cooldown");
        } else {
            Location locationFinal = player.getTargetBlock(null, 150).getLocation();
            Vector posFinal = new Vector(locationFinal.getX(), locationFinal.getY(), locationFinal.getZ());
            Location locationInicial = player.getLocation();
            Vector posInicial = new Vector(locationInicial.getX(), locationInicial.getY(), locationInicial.getZ());
            Vector deslocamento = posFinal.subtract(posInicial);
            Vector direcao = deslocamento.normalize();
            direcao.setY(0);
            int distancia = 5;

            double finalX = posInicial.getX() + distancia * direcao.getX();
            double finalY = posInicial.getY();
            double finalZ = posInicial.getZ() + distancia * direcao.getZ();
            float pitch = locationInicial.getPitch();
            float yaw = locationInicial.getYaw();
            World world = player.getWorld();
            Location locationToTeleport = new Location(world, finalX, finalY, finalZ, yaw, pitch);
            //player.setVelocity(new Vector(direcao.getX() * 1.2, 0.1, direcao.getZ() * 1.2));
            player.teleport(locationToTeleport);
            //player.setVelocity(player.getVelocity().setY(0));
            this.player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1 * 20, 0));
            this.player.setFallDistance(0);


            if (!lightmode) {
                double x_p1 = 1;
                double y_p1 = 1;

                //player.sendMessage(direcao.getX() + " " +  direcao.getY() + " " + direcao.getZ());
                double z_p1 = (-direcao.getX() + -direcao.getY()) / direcao.getZ();
                Vector holder = direcao.clone();
                Vector direcao_perpendicular1 = new Vector(x_p1, y_p1, z_p1).normalize();
                Vector direcao_perpendicular2 = (holder.crossProduct(direcao_perpendicular1)).normalize();
            /*
            player.sendMessage(direcao.getX() + " " +  direcao.getY() + " " + direcao.getZ());
            player.sendMessage(direcao_perpendicular1.getX() + " " +  direcao_perpendicular1.getY() + " " +
                    direcao_perpendicular1.getZ());
            player.sendMessage(direcao_perpendicular2.getX() + " " +  direcao_perpendicular2.getY() + " " +
                    direcao_perpendicular2.getZ());
            player.sendMessage(String.valueOf(direcao.dot(direcao_perpendicular1)));
            player.sendMessage(String.valueOf(direcao.dot(direcao_perpendicular2)));
            player.sendMessage(String.valueOf(direcao_perpendicular1.dot(direcao_perpendicular2)));
            */
                double particle_x, particle_y, particle_z;
                double radius = 0.10;
                for (int i = 0; i < distancia; i++) {
                    particle_x = posInicial.getX() + i * direcao.getX();
                    particle_y = posInicial.getY() + 1;
                    particle_z = posInicial.getZ() + i * direcao.getZ();
                    Vector vetor_inicial = new Vector(particle_x, particle_y, particle_z);

                    //world.spawnParticle(Particle.ASH, particle_x, particle_y, particle_z, 15);
                    int iteracoes = 60;
                    for (int j = 0; j < iteracoes; j++) {
                        double t = j * 2*Math.PI / iteracoes;

                    /*
                    double x_t = 0;
                    double y_t = radius * Math.sin(t);
                    double z_t = radius * Math.cos(t);
                    */

                        double m1 = radius * Math.sin(t);
                        double m2 = radius * Math.cos(t);

                        Vector v1 = direcao_perpendicular1.clone();
                        Vector v2 = direcao_perpendicular2.clone();

                        v1.multiply(m1);
                        v2.multiply(m2);

                        v1.add(v2);

                        //Vector vetor_t = new Vector(x_t, y_t, z_t);
                        //Vector vetor_final = vetor_inicial.add(vetor_t);
                        Vector vetor_final = vetor_inicial.add(v1);
                        Location localizacao_particula = new Location(world, vetor_final.getX(), vetor_final.getY(), vetor_final.getZ());
                        world.spawnParticle(Particle.ASH, localizacao_particula, 10);
                    }
                }
            }


            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 1));

            this.isOnCoolDownDash = true;
            BukkitTask taskDash = new coolDownDash(this.plugin, this).runTaskLater(this.plugin, 3 * 20);
        }
    }

    public void puxao() {
        if (this.isOnCoolDownPuxao) {
            player.sendMessage("Essa habilidade está em cooldwon");
            return;
        }
        List<Entity> entidades_proximas = this.player.getNearbyEntities(25, 25, 25);
        for (Entity entidade : entidades_proximas) {
            Location eye = this.player.getEyeLocation();
            Location entity_eye = Ajuda.get_eye_location(entidade);
            Vector toEntity = entity_eye.toVector().subtract(eye.toVector());
            double dot = toEntity.normalize().dot(eye.getDirection());

            //player.sendMessage(String.valueOf(dot));
            if (dot >= 0.994D) {
                Location player_location = this.player.getLocation();
                Vector direction = this.player.getEyeLocation().getDirection();
                double x = player_location.getX() + direction.getX() * 2;
                double y = player_location.getY();
                double z = player_location.getZ() + direction.getZ() * 2;
                Location to_teleport = new Location(this.player.getWorld(), x, y, z);
                entidade.teleport(to_teleport);
                this.player.getWorld().playSound(player_location, Sound.ITEM_TRIDENT_HIT_GROUND, 1, 1);
            }
        }
        this.isOnCoolDownPuxao = true;
        BukkitTask task = new coolDownPuxao(this.plugin, this).runTaskLater(this.plugin, 15 * 20);
    }

    public boolean getCoolDownTeleportar() {
        return this.isOnCoolDownTeleportar;
    }

    public void eraseCoolDownSuperPulo() {
        isOnCoolDownSuperPulo = false;
        player.sendMessage("O coolDown de " + ChatColor.BLUE + "Super Pulo" + " terminou");
    }
    public void eraseCoolDownTeleportar() {
        this.isOnCoolDownTeleportar = false;
        this.isOnCoolDownTNTCombo = false;
        this.firstCombo = false;
        this.secondCombo = false;
        player.sendMessage("O coolDown de " + ChatColor.YELLOW + "Teleportar" + " terminou");
        player.sendMessage("O coolDown de " + ChatColor.RED + "TNT Combo" + " terminou");
    }
    public void eraseCoolDownDash() {
        isOnCoolDownDash = false;
        player.sendMessage("O coolDown de " + ChatColor.RED + "Dash" + " terminou");
    }
    public void eraseCoolDownTNTCombo() {
        this.isOnCoolDownTNTCombo = false;
        player.sendMessage("O coolDown de " + ChatColor.RED + "TNT Combo" + " terminou");
    }
    public void eraseCoolDownPuxao() {
        this.isOnCoolDownPuxao = false;
        player.sendMessage("O coolDown de " + ChatColor.AQUA + "Puxão" + " terminou");
    }
    public void eraseFirstCombo() {
        if (!this.secondCombo) player.sendMessage("<Combo Off>");
        this.firstCombo = false;
    }
    public void eraseSecondCombo() {
        if (!this.thirdCombo) player.sendMessage("<Combo Off>");
        this.secondCombo = false;
    }
    public void eraseThirdCombo() {
        if (!this.comboCompleto) player.sendMessage("<Combo Off>");
        this.thirdCombo = false;
    }
    public void eraseComboCompleto() {
        this.comboCompleto = false;
    }
}
