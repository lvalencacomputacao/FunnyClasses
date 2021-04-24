package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.tasks.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class Arquiteto {
    int nivel;
    Player player;
    FunnyClasses plugin;
    boolean isOnCoolDownSuperPonte;
    boolean isOnCoolDownGanchoDeslocador;
    boolean isOnCoolDownReplicadorDeMateria;
    boolean isOnCoolDownCorteCircular;
    boolean isOnCoolDownMachineGun;

    boolean isBuildingBridgePause_;

    int deslocadorDeMateriaCounter;
    Block block_inicio;
    Block block_fim;
    Block[][][] blocos;
    int deslocamento_x;
    int deslocamento_y;
    int deslocamento_z;

    boolean isBuildingBridge_;
    boolean atirando;

    public Arquiteto(Player player, FunnyClasses plugin) {
        this.player = player;
        this.plugin = plugin;
        this.nivel = 0;
        this.isOnCoolDownSuperPonte = false;
        this.isOnCoolDownGanchoDeslocador = false;
        this.isOnCoolDownReplicadorDeMateria = false;
        this.isOnCoolDownCorteCircular = false;
        this.isOnCoolDownMachineGun = false;

        this.isBuildingBridgePause_ = false;
        this.atirando = false;

        this.isBuildingBridge_ = false;
        this.deslocadorDeMateriaCounter = 0;

        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(12);
        this.player.setHealthScale(12);
    }

    public boolean isBuildingBridge() {
        return isBuildingBridge_;
    }

    public void superPonte() {
        if (!isOnCoolDownSuperPonte) {
            player.setVelocity(player.getVelocity().setY(0));
            player.setFallDistance(0);
            this.isBuildingBridge_ = true;
            this.isBuildingBridgePause_ = true;
            this.isOnCoolDownSuperPonte = true;
            //player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2));

            BukkitTask taskSuperPonte = new coolDownSuperPonte(this.plugin, this).runTaskLater(this.plugin, 20 * 20);
            BukkitTask taskSetOffBuildingBridge = new askToRemoveIsBuildingBridge(this.plugin, this).runTaskLater(this.plugin, 10 * 20);
        } else {
            player.sendMessage("A habilidade está em cooldown");
        }
    }

    public void setOffIsBuildingBridge() {
        this.isBuildingBridge_ = false;
    }

    public void setPauseIsBuildingBridge() {
        if (!isBuildingBridgePause_) {
            player.setVelocity(player.getVelocity().setY(0));
            this.player.setFallDistance(0);
            isBuildingBridgePause_ = true;
        } else {
            isBuildingBridgePause_ = false;
        }
    }
    public boolean isBuildingBridgePause() {
        return isBuildingBridgePause_;
    }

    // Melhorar
    public void deslocadorDeMateriaPreparar() {
        switch (deslocadorDeMateriaCounter) {
            case 0:
                this.block_inicio = player.getTargetBlock(null, 5);
                if (this.block_inicio.getType() == Material.AIR) {
                    player.sendMessage("Longe demais, tente se aproximar mais do bloco");
                    return;
                }
                this.deslocadorDeMateriaCounter++;
                player.sendMessage("Primeiro bloco selecionado!");
                player.sendMessage("Bloco selecionado: " + this.block_inicio.getType().name());
                break;

            case 1:
                this.block_fim = player.getTargetBlock(null, 4);
                if (this.block_inicio.getType() == Material.AIR) {
                    player.sendMessage("Longe demais, tente se aproximar mais do bloco");
                    return;
                }
                this.deslocadorDeMateriaCounter++;

                int x_inicial = Math.min(block_inicio.getX(), block_fim.getX());
                int y_inicial = Math.min(block_inicio.getY(), block_fim.getY());
                int z_inicial = Math.min(block_inicio.getZ(), block_fim.getZ());

                this.deslocamento_x = Math.abs(block_inicio.getX() - block_fim.getX()) + 1;
                this.deslocamento_y = Math.abs(block_inicio.getY() - block_fim.getY()) + 1;
                this.deslocamento_z = Math.abs(block_inicio.getZ() - block_fim.getZ()) + 1;

                player.sendMessage("Segundo bloco selecionado!");
                player.sendMessage("Bloco selecionado: " + this.block_fim.getType().name());
                this.blocos = new Block[this.deslocamento_x][this.deslocamento_y][this.deslocamento_z];
                Location localizacao_inicial = new Location(player.getWorld(), x_inicial, y_inicial, z_inicial);

                for (int i = 0; i < this.deslocamento_x; i++) {
                    for (int j = 0; j < this.deslocamento_y; j++) {
                        for (int k = 0; k < this.deslocamento_z; k++) {
                            double x_atual = localizacao_inicial.getX() + i;
                            double y_atual = localizacao_inicial.getY() + j;
                            double z_atual = localizacao_inicial.getZ() + k;
                            Location localizacao_atual = new Location(player.getWorld(), x_atual, y_atual, z_atual);

                            this.blocos[i][j][k] = localizacao_atual.getBlock();
                        }
                    }
                }
                break;

            case 2:
                player.sendMessage("Cancelou");
                this.deslocadorDeMateriaCounter = 0;
        }
    }

    public void deslocadorDeMateriaExecutar() {
        if (deslocadorDeMateriaCounter != 2) {
            player.sendMessage("Declare os pontos primeiro!");
            return;
        }
        if (this.isOnCoolDownReplicadorDeMateria) {
            player.sendMessage("A Habilidade está em cooldown");
            return;
        }

        Block block = player.getTargetBlock(null, 15);
        int direcao_x;
        int direcao_z;
        int direcao_y;

        if (player.getLocation().getX() < block.getX()) direcao_x = 1;
        else direcao_x = -1;
        if (!player.isSneaking()) direcao_y = 1;
        else direcao_y = -1;
        if (player.getLocation().getZ() < block.getZ()) direcao_z = 1;
        else direcao_z = -1;
        Location localizacao_inicial = new Location(player.getWorld(), block.getX(), block.getY() + 1,
                block.getZ());
        Vector direcao = this.player.getEyeLocation().getDirection();
        for (int i = 0; i < this.deslocamento_x; i++) {
            for (int j = 0; j < this.deslocamento_y; j++) {
                for (int k = 0; k < this.deslocamento_z; k++) {
                    double x_atual, y_atual, z_atual;

                    x_atual = localizacao_inicial.getX() + i * direcao_x;

                    y_atual = localizacao_inicial.getY() + j * direcao_y;

                    z_atual = localizacao_inicial.getZ() + k * direcao_z;

                    Location localizacao_atual = new Location(player.getWorld(), x_atual, y_atual, z_atual);

                    localizacao_atual.getBlock().setType(blocos[i][j][k].getType());
                    blocos[i][j][k].setType(Material.AIR);
                    blocos[i][j][k] = localizacao_atual.getBlock();
                }
            }
        }
        BukkitTask task = new coolDownReplicadorDeMateria(this.plugin, this).runTaskLater(this.plugin, 15 * 20);
        this.isOnCoolDownReplicadorDeMateria = true;
    }

    public void ganchoDeslocador() {
        if (this.isOnCoolDownGanchoDeslocador) {
            player.sendMessage("Habilidade em cooldown");
            return;
        }
        Block block = player.getTargetBlock(null, 100);
        if (block.getType() == Material.AIR) {
            player.sendMessage("Você não conseguiu se prender a nada");
            return;
        }
        double x_destino = block.getX();
        double y_destino = block.getY();
        double z_destino = block.getZ();

        double x_atual = player.getLocation().getX();
        double y_atual = player.getLocation().getY();
        double z_atual = player.getLocation().getZ();

        Vector deslocamento = new Vector(x_destino - x_atual, y_destino - y_atual, z_destino - z_atual);

        player.setVelocity(deslocamento.multiply(0.2));
        //player.setVelocity(player.getLocation().getDirection().multiply(1.5));
        this.isOnCoolDownGanchoDeslocador = true;
        BukkitTask taskGanchoDeslocador = new coolDownGanchoDeslocador(this.plugin, this).runTaskLater(this.plugin, 12 * 20);
    }

    public void corteCircular() {
        if (this.isOnCoolDownCorteCircular) {
            player.sendMessage("A habilidade está em cooldown");
            return;
        }
        Location player_location = this.player.getLocation();
        List<Entity> entidades_proximas = this.player.getNearbyEntities(5, 5, 5);
        for (Entity entidade : entidades_proximas) {
            Vector direction = (entidade.getLocation().toVector().subtract(player_location.toVector())).normalize();
            entidade.setVelocity(direction.multiply(2.5));
            if (entidade instanceof Monster) {
                Monster mob = (Monster) entidade;
                mob.damage(7);
            }
        }
        BukkitTask task = new coolDownCorteCircular(this.plugin, this).runTaskLater(this.plugin, 20 * 20);
        this.isOnCoolDownCorteCircular = true;
    }

    public void machineGun() {
        if (this.isOnCoolDownMachineGun) {
            player.sendMessage("A habilidade está em cooldown");
            return;
        }
        this.atirando = true;
        this.isOnCoolDownMachineGun = true;
        BukkitTask task = new atirando(this.plugin, this).runTaskLater(this.plugin, 4 * 20);
        BukkitTask task_coolDown = new coolDownMachineGun(this.plugin, this).runTaskLater(this.plugin, 30 * 20);
    }

    public void atirar() {
        Vector direction = this.player.getEyeLocation().getDirection();

        this.player.launchProjectile(AbstractArrow.class, direction.multiply(2)).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
    }

    // CoolDown e outros timers
    public void eraseCoolDownSuperPonte() {
        this.isOnCoolDownSuperPonte = false;
        player.sendMessage("O coolDown de " + ChatColor.BLUE + "Super Ponte" + " terminou");
    }
    public void eraseCoolDownGanchoDeslocador() {
        this.isOnCoolDownGanchoDeslocador = false;
        player.sendMessage("O coolDown de " + ChatColor.RED + "Gancho Deslocador" + " terminou");
    }
    public void eraseCoolDownCorteCircular() {
        this.isOnCoolDownCorteCircular = false;
        player.sendMessage("O coolDown de " + ChatColor.RED + "Corte Circular" + " terminou");
    }
    public void eraseCoolDownReplicadorDeMateria() {
        this.isOnCoolDownReplicadorDeMateria = false;
        player.sendMessage("O coolDown de " + ChatColor.BLUE + "Replicador De Matéria" + " terminou");
    }
    public void eraseCoolDownMachineGun() {
        this.isOnCoolDownMachineGun = false;
        player.sendMessage("O coolDown de " + ChatColor.GRAY + "Machine Gun" + " terminou");
    }
    public void eraseAtirando() {
        player.sendMessage("Parou de atirar");
        this.atirando = false;
    }

    // Getters
    public boolean getAtirando() {
        return this.atirando;
    }
}
