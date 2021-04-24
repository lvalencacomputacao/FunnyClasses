package me.lucasvalenca.funnyclasses.classes;

import me.lucasvalenca.funnyclasses.FunnyClasses;
import me.lucasvalenca.funnyclasses.ajuda.Ajuda;
import me.lucasvalenca.funnyclasses.tasks.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import me.lucasvalenca.funnyclasses.ajuda.Ajuda;

import java.util.ArrayList;
import java.util.List;

/*
Aqui você encontra as informações sobre a classe protetor. Os primeiros métodos são os métodos de habilidades,
enquanto que os últimos controlam o cooldown de cada uma dessas habilidades.
 */

public class Protetor {
    int nivel;
    Player player;
    FunnyClasses plugin;
    boolean isOnCoolDownHealWeapon;
    boolean isOnCoolDownRoot;
    boolean isOnCoolDownMagicShield;
    boolean isOnCoolDownSlimeSave;
    boolean isOnCoolDownPickThrow;
    boolean isOnCoolDownSpeedSpell;
    boolean isOnCoolDownPulo;
    boolean isSegurando;
    boolean isPulando;
    Entity player_segurado;
    Ajuda ajuda;

    int magiaAtual = 0;

    List<Block> blocosMagicShield;
    String[] Magias;

    public Protetor(Player player, FunnyClasses plugin) {
        this.player = player;
        this.plugin = plugin;
        this.nivel = 0;
        this.isOnCoolDownHealWeapon = false;
        this.isOnCoolDownRoot = false;
        this.isOnCoolDownMagicShield = false;
        this.isOnCoolDownSlimeSave = false;
        this.isOnCoolDownPickThrow = false;
        this.isOnCoolDownSpeedSpell = false;
        this.isOnCoolDownPulo = false;
        this.isSegurando = false;
        this.isPulando = false;
        this.blocosMagicShield = new ArrayList<Block>();
        this.Magias = new String[]{"MagicShield", "SlimeSave", "SpeedSpell"};
        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16);
        this.player.setHealthScale(16);
        this.player_segurado = null;
        this.ajuda = new Ajuda();
    }

    // Habilidade
    public void HealWeapon(Entity entidadeMachucada) {
        //this.player.sendMessage("teste");
        //this.player.sendMessage(entidadeMachucada.toString());
        if (entidadeMachucada instanceof Player) {
            if (!this.isOnCoolDownHealWeapon) {
                Player playerParaCurar = (Player)entidadeMachucada;
                playerParaCurar.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6 * 20, 1));
                playerParaCurar.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6 * 20, 1));
                playerParaCurar.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40 * 20, 1));
                player.sendMessage(playerParaCurar.getDisplayName() + " foi" + ChatColor.GOLD +  " PROTEGIDO!");
                this.isOnCoolDownHealWeapon = true;
                BukkitTask taskHealWealpon = new coolDownHealWeapon(this.plugin, this).runTaskLater(this.plugin, 10 * 20);
                this.player.getWorld().playSound(this.player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 2);
            } else {
                this.player.sendMessage("A habilidade está em cooldown");
            }
        } else if (entidadeMachucada instanceof LivingEntity) {
            if (!this.isOnCoolDownRoot) {
                for (Entity entity : this.player.getNearbyEntities(10, 10, 10)) {
                    if (!(entity instanceof Player) && (entity instanceof LivingEntity)) {
                        LivingEntity entidadeParaPrender = (LivingEntity) entity;
                        entidadeParaPrender.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 999));
                        entidadeParaPrender.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5 * 20, 1));
                    }
                }
                player.sendMessage("As criaturas foram" + ChatColor.GREEN +  " enraizadas!");
                //entidadeParaPrender.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 999));
                this.isOnCoolDownRoot = true;
                BukkitTask taskRoot = new coolDownRoot(this.plugin, this).runTaskLater(this.plugin, 15 * 20);
                this.player.getWorld().playSound(this.player.getLocation(), Sound.ITEM_TRIDENT_HIT_GROUND, 1, 2);
            } else {
                this.player.sendMessage("A habilidade está em cooldown");
            }
        }
    }

    public void MagicShield() {
        if (!isOnCoolDownMagicShield) {
            player.sendMessage("Você usou" + ChatColor.LIGHT_PURPLE + " Magic Shield!");
            Location locationOfPlayer = player.getLocation();
            double x = locationOfPlayer.getX();
            double y = locationOfPlayer.getY();
            double z = locationOfPlayer.getZ();
            World world = locationOfPlayer.getWorld();

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    Location block1_location = new Location(world, x + 3, y + i, z + j - 2);
                    Block block = block1_location.getBlock();
                    block.setType(Material.DIRT);
                    blocosMagicShield.add(block);
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    Location block1_location = new Location(world, x - 3, y + i, z + j - 2);
                    Block block = block1_location.getBlock();
                    block.setType(Material.DIRT);
                    blocosMagicShield.add(block);
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    Location block1_location = new Location(world, x + j - 2, y + i, z + 3);
                    Block block = block1_location.getBlock();
                    block.setType(Material.DIRT);
                    blocosMagicShield.add(block);
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    Location block1_location = new Location(world, x + j - 2, y + i, z - 3);
                    Block block = block1_location.getBlock();
                    block.setType(Material.DIRT);
                    blocosMagicShield.add(block);
                }
            }
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    Location block1_location = new Location(world, x + i - 2, y + 3, z + j - 2);
                    Block block = block1_location.getBlock();
                    if (i == 2 && j == 2) block.setType(Material.GLOWSTONE);
                    else block.setType(Material.DIRT);
                    blocosMagicShield.add(block);
                }
            }
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    Location block1_location = new Location(world, x + i - 2, y - 1, z + j - 2);
                    Block block = block1_location.getBlock();
                    if (i == 2 && j == 2) block.setType(Material.GLOWSTONE);
                    else block.setType(Material.DIRT);
                    blocosMagicShield.add(block);
                }
            }
            System.out.println(blocosMagicShield.size());
            isOnCoolDownMagicShield = true;
            //BukkitTask taskRemoveMagicShield = new askToRemoveMagicShield(this.plugin, this).runTaskLater(this.plugin, 5 * 20);
            BukkitTask taskMagicShield = new coolDownMagicShield(this.plugin, this).runTaskLater(this.plugin, 30 * 20);
            this.player.getWorld().playSound(this.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
        } else {
            this.player.sendMessage("A habilidade está em cooldown");
        }
    }

    public void SlimeSave() {
        //player.sendMessage("Slime save!");
        if (!isOnCoolDownSlimeSave) {
            System.out.println("Slime save executado");
            Block block = player.getTargetBlock(null, 100);
            Location CenterOfSlimePad = block.getLocation();
            World world = CenterOfSlimePad.getWorld();
            double x = CenterOfSlimePad.getX();
            double y = CenterOfSlimePad.getY();
            double z = CenterOfSlimePad.getZ();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    Location locationOfBlock = new Location(world, x + i - 2, y, z + j - 2);
                    Block blockToChange = locationOfBlock.getBlock();
                    blockToChange.setType(Material.SLIME_BLOCK);
                }
            }
            for (int k = 0; k < 5; k++) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        Location locationOfBlock = new Location(world, x + i - 2, y + k + 1, z + j - 2);
                        Block blockToChange = locationOfBlock.getBlock();
                        blockToChange.setType(Material.AIR);
                    }
                }
            }
            this.isOnCoolDownSlimeSave = true;
            BukkitTask taskSlimeSave = new coolDownSlimeSave(this.plugin, this).runTaskLater(this.plugin, 30 * 20);
            this.player.getWorld().playSound(this.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
        } else {
            player.sendMessage("A habilidade está em cooldown");
        }
    }

    // Cooldown
    public void eraseCoolDownRoot() {
        player.sendMessage("O coolDown de " + ChatColor.GREEN + "root" + " terminou");
        this.isOnCoolDownRoot = false;
    }
    public void eraseCoolDownHealWeapon() {
        player.sendMessage("O coolDown de " + ChatColor.GREEN + "Heal Weapon" + " terminou");
        this.isOnCoolDownHealWeapon = false;
    }
    public void eraseCoolDownMagicShield() {
        player.sendMessage("O coolDown de " + ChatColor.LIGHT_PURPLE + "Magic Shield" + " terminou");
        this.isOnCoolDownMagicShield = false;
    }
    public void eraseCoolDownSlimeSave() {
        player.sendMessage("O coolDown de " + ChatColor.GREEN + "Slime Save" + "terminou");
        this.isOnCoolDownSlimeSave = false;
    }

    // Remove blocos de magia (WIP)
    public void removeMagicShield() {
        //System.out.println("Funcionou!!");
        for (Block block : blocosMagicShield) {
            block.setType(Material.AIR);
            this.blocosMagicShield.remove(block);
        }
    }

    // Muda a magia atual
    public void mudarMagia() {
        System.out.println("Funcionou!");
        this.magiaAtual = (this.magiaAtual + 1) % Magias.length;
        player.sendMessage("A sua magia atual é " + ChatColor.GREEN + this.Magias[this.magiaAtual]);
    }

    // Usar magia
    public void usarMagia() {
        System.out.println("Chegou até usar magia");
        if (this.Magias[this.magiaAtual].equals("MagicShield")) {
            this.MagicShield();
        } else if (this.Magias[this.magiaAtual].equals("SlimeSave")) {
            this.SlimeSave();
        } else if (this.Magias[this.magiaAtual].equals("SpeedSpell")) {
            this.SpeedSpell();
        }
    }

    public void SpeedSpell() {
        if (this.isOnCoolDownSpeedSpell) {
            player.sendMessage("A habilidade está em cooldown");
            return;
        }
        speedBoost(this.player, 9);

        List<Entity> entidades_proximas = this.player.getNearbyEntities(10, 10, 10);
        for (Entity entity : entidades_proximas) {
            if (entity instanceof Player) {
                speedBoost((Player) entity, 9);
            }
        }
        this.isOnCoolDownSpeedSpell = true;
        BukkitTask taskSlimeSave = new coolDownSpeedSpell(this.plugin, this).runTaskLater(this.plugin, 30 * 20);
        this.player.getWorld().playSound(this.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
    }

    public void speedBoost(Player jogador, int nivel) {
        jogador.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 8, 0));
        jogador.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 8, nivel));
    }

    public void pegarPlayer(Entity jogador_segurado) {
        if (this.isSegurando) return;
        if (this.isOnCoolDownPickThrow) {
            player.sendMessage("Habilidade está em cooldown");
            return;
        }
        this.player.addPassenger(jogador_segurado);
        //this.player.eject();
        this.isSegurando = true;
        this.player_segurado = jogador_segurado;
        this.isOnCoolDownPickThrow = true;
    }

    public void jogarPlayer() {
        if (!this.isSegurando) return;

        Location looking_location  = player.getTargetBlock(null, 1000).getLocation();
        Location current_location = player.getLocation();

        Vector posFinal = new Vector(looking_location.getX(), looking_location.getY(), looking_location.getZ());
        Vector posInicial = new Vector(current_location.getX(), current_location.getY(), current_location.getZ());

        Vector direction = posFinal.subtract(posInicial).normalize();

        this.player.eject();
        BukkitTask task = new coolDownPickThrow(this.plugin, this).runTaskLater(this.plugin, 20 * 20);

        this.player_segurado.setVelocity(direction.multiply(1.5));
        if (this.player_segurado instanceof Player) {
            receberEscudo((Player) this.player_segurado, 1);
        }
        this.player_segurado = null;

        this.isSegurando = false;

    }

    public void eraseCoolDownPickThrow() {
        this.isOnCoolDownPickThrow = false;
        this.player.sendMessage("O cooldown de segurar players acabou!");
    }

    public void puloPesado() {
        if (this.isOnCoolDownPulo) {
            player.sendMessage("A habilidade está em cooldown");
            return;
        }
        this.player.setVelocity(player.getLocation().getDirection().multiply(1.75));
        receberEscudo(this.player, 3);
        this.isPulando = true;
        this.isOnCoolDownPulo = true;
        BukkitTask task = new coolDownPulo(this.plugin, this).runTaskLater(this.plugin, 20 * 20);
    }

    public boolean getIsPulando() {
        return this.isPulando;
    }

    public void fim_pulo() {
        this.player.getWorld().playSound(this.player.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1, 2);
        this.isPulando = false;
        //removerEscudo(this.player);
        double raio = this.player.getFallDistance() / 1.5;
        double intensidade = this.player.getFallDistance() / 7;
        double fallDistance = this.player.getFallDistance();
        List<Entity> entidades_proximas = this.player.getNearbyEntities(2 * fallDistance, Math.max(10, 2 * fallDistance),
                2 * fallDistance);
        for (Entity entidade : entidades_proximas) {
            if (entidade instanceof Player) {
                receberEscudo((Player) entidade, 4);
            }
            double distancia = Ajuda.getDistancia(this.player, entidade);

            double fator_multiplicativo = Math.max((2 * fallDistance - distancia * 1.25) / (2 * fallDistance), 0);
            Vector velocidade_atual = entidade.getVelocity();
            velocidade_atual.setY(intensidade * fator_multiplicativo);
            entidade.setVelocity(velocidade_atual);
        }
    }

    public void receberEscudo(Player jogador_para_proteger, int nivel) {
        if (nivel < 1) return;
        jogador_para_proteger.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 4, 0));
        jogador_para_proteger.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 4, nivel));
    }
    public void removerEscudo(Player jogador_para_retirar) {
        jogador_para_retirar.removePotionEffect(PotionEffectType.ABSORPTION);
        jogador_para_retirar.removePotionEffect(PotionEffectType.GLOWING);
    }

    public void eraseCoolDownSpeedSpell() {
        this.isOnCoolDownSpeedSpell = false;
        this.player.sendMessage("O cooldown de " + ChatColor.BLUE + "SpeedSpell acabou");
    }

    public void eraseCoolDownPulo() {
        this.isOnCoolDownPulo = false;
        this.player.sendMessage("O cooldown de " + ChatColor.LIGHT_PURPLE + "Pulo Pesado acabou");
    }
}
