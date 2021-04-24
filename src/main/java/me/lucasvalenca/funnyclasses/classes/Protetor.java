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


public class Protetor {
    int nivel;
    Player player;
    FunnyClasses plugin;
    World world;

    boolean isOnCoolDownHealWeapon;
    boolean isOnCoolDownRoot;
    boolean isOnCoolDownMagicShield;
    boolean isOnCoolDownSlimeSave;
    boolean isOnCoolDownPickThrow;
    boolean isOnCoolDownSpeedSpell;
    boolean isOnCoolDownPulo;
    boolean isOnCoolDownSobreVisao;

    boolean isSegurando;
    boolean isPulando;
    Entity player_segurado;
    int magiaAtual = 0;
    List<Block> blocosMagicShield;
    String[] Magias;

    Ajuda ajuda;

    public Protetor(Player player, FunnyClasses plugin) {
        this.player = player;
        this.world = player.getWorld();
        this.plugin = plugin;
        this.nivel = 0;

        this.isOnCoolDownHealWeapon = false;
        this.isOnCoolDownRoot = false;
        this.isOnCoolDownMagicShield = false;
        this.isOnCoolDownSlimeSave = false;
        this.isOnCoolDownPickThrow = false;
        this.isOnCoolDownSpeedSpell = false;
        this.isOnCoolDownPulo = false;
        this.isOnCoolDownSobreVisao = false;

        this.isSegurando = false;
        this.player_segurado = null;
        this.isPulando = false;
        this.blocosMagicShield = new ArrayList<Block>();
        this.Magias = new String[]{"MagicShield", "SlimeSave", "SpeedSpell"};

        this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16);
        this.player.setHealthScale(16);

        this.ajuda = new Ajuda();
    }

    // Habilidades
    public void HealWeaponOrRoot(Entity entidadeMachucada) {
        if (entidadeMachucada instanceof Player) {
            if (this.isOnCoolDownHealWeapon) {
                this.player.sendMessage("A habilidade está em cooldown");
                return;
            }
            Player playerParaCurar = (Player)entidadeMachucada;
            receiveProtection(playerParaCurar, 2);
            player.sendMessage(playerParaCurar.getDisplayName() + " foi" + ChatColor.GOLD +  " PROTEGIDO!");

            this.isOnCoolDownHealWeapon = true;
            BukkitTask cd = new coolDownHealWeapon(this.plugin, this).runTaskLater(this.plugin, 15 * 20);
            this.world.playSound(this.player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 2);

        }
        else if (entidadeMachucada instanceof LivingEntity) {
            if (this.isOnCoolDownRoot) {
                this.player.sendMessage("A habilidade está em cooldown");
                return;
            }
            for (Entity entity : this.player.getNearbyEntities(10, 10, 10)) {
                if (!(entity instanceof Player) && (entity instanceof LivingEntity)) {
                    LivingEntity entidadeParaPrender = (LivingEntity) entity;
                    receiveRoot(entidadeParaPrender);
                }
            }
            player.sendMessage("As criaturas foram" + ChatColor.GREEN +  " enraizadas!");
            this.isOnCoolDownRoot = true;
            BukkitTask cd = new coolDownRoot(this.plugin, this).runTaskLater(this.plugin, 10 * 20);
            this.world.playSound(this.player.getLocation(), Sound.ITEM_TRIDENT_HIT_GROUND, 1, 2);
        }
    }

    public void MagicShield() {
        if (this.isOnCoolDownMagicShield) {
            this.player.sendMessage("A habilidade está em cooldown");
            return;
        }
        player.sendMessage("Você usou" + ChatColor.LIGHT_PURPLE + " Magic Shield!");
        Location locationOfPlayer = player.getLocation();
        double x = locationOfPlayer.getX();
        double y = locationOfPlayer.getY();
        double z = locationOfPlayer.getZ();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                Location block1_location = new Location(this.world, x + 3, y + i, z + j - 2);
                Block block = block1_location.getBlock();
                block.setType(Material.DIRT);
                blocosMagicShield.add(block);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                Location block1_location = new Location(this.world, x - 3, y + i, z + j - 2);
                Block block = block1_location.getBlock();
                block.setType(Material.DIRT);
                blocosMagicShield.add(block);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                Location block1_location = new Location(this.world, x + j - 2, y + i, z + 3);
                Block block = block1_location.getBlock();
                block.setType(Material.DIRT);
                blocosMagicShield.add(block);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                Location block1_location = new Location(this.world, x + j - 2, y + i, z - 3);
                Block block = block1_location.getBlock();
                block.setType(Material.DIRT);
                blocosMagicShield.add(block);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Location block1_location = new Location(this.world, x + i - 2, y + 3, z + j - 2);
                Block block = block1_location.getBlock();
                if (i == 2 && j == 2) block.setType(Material.GLOWSTONE);
                else block.setType(Material.DIRT);
                blocosMagicShield.add(block);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Location block1_location = new Location(this.world, x + i - 2, y - 1, z + j - 2);
                Block block = block1_location.getBlock();
                if (i == 2 && j == 2) block.setType(Material.GLOWSTONE);
                else block.setType(Material.DIRT);
                blocosMagicShield.add(block);
            }
        }
        isOnCoolDownMagicShield = true;
        BukkitTask rm = new askToRemoveMagicShield(this.plugin, this).runTaskLater(this.plugin, 4 * 20);
        BukkitTask cd = new coolDownMagicShield(this.plugin, this).runTaskLater(this.plugin, 30 * 20);
        this.world.playSound(this.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
    }

    public void SlimeSave() {
        if (this.isOnCoolDownSlimeSave) {
            player.sendMessage("A habilidade está em cooldown");
            return;
        }
        Block block = player.getTargetBlock(null, 100);
        Location CenterOfSlimePad = block.getLocation();
        double x = CenterOfSlimePad.getX();
        double y = CenterOfSlimePad.getY();
        double z = CenterOfSlimePad.getZ();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Location locationOfBlock = new Location(this.world, x + i - 2, y, z + j - 2);
                Block blockToChange = locationOfBlock.getBlock();
                blockToChange.setType(Material.SLIME_BLOCK);
            }
        }
        for (int k = 0; k < 5; k++) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    Location locationOfBlock = new Location(this.world, x + i - 2, y + k + 1, z + j - 2);
                    Block blockToChange = locationOfBlock.getBlock();
                    blockToChange.setType(Material.AIR);
                }
            }
        }
        this.isOnCoolDownSlimeSave = true;
        BukkitTask taskSlimeSave = new coolDownSlimeSave(this.plugin, this).runTaskLater(this.plugin, 30 * 20);
        this.world.playSound(this.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
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
        this.world.playSound(this.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
    }

    // Muda a magia atual
    public void mudarMagia() {
        this.magiaAtual = (this.magiaAtual + 1) % Magias.length;
        player.sendMessage("A sua magia atual é " + ChatColor.GREEN + this.Magias[this.magiaAtual]);
    }

    // Usar magia
    public void usarMagia() {
        switch (this.Magias[this.magiaAtual]) {
            case "MagicShield":
                this.MagicShield();
                break;
            case "SlimeSave":
                this.SlimeSave();
                break;
            case "SpeedSpell":
                this.SpeedSpell();
                break;
        }
    }

    public void pegarPlayer(Entity jogador_segurado) {
        if (this.isSegurando) return;
        if (this.isOnCoolDownPickThrow) {
            player.sendMessage("Habilidade está em cooldown");
            return;
        }
        this.player.addPassenger(jogador_segurado);
        this.isSegurando = true;
        this.player_segurado = jogador_segurado;
        this.isOnCoolDownPickThrow = true;
    }

    public void jogarPlayer() {
        if (!this.isSegurando) return;

        Vector direction = this.player.getEyeLocation().getDirection();

        this.player.eject();
        BukkitTask cd = new coolDownPickThrow(this.plugin, this).runTaskLater(this.plugin, 15 * 20);

        this.player_segurado.setVelocity(direction.multiply(1.5));
        if (this.player_segurado instanceof Player) {
            receberEscudo((Player) this.player_segurado, 2);
        }
        this.player_segurado = null;
        this.isSegurando = false;
    }

    public void holdedMorto() {
        this.player.sendMessage("IH");
        this.player_segurado = null;
        this.isSegurando = false;
        BukkitTask cd = new coolDownPickThrow(this.plugin, this).runTaskLater(this.plugin, 15 * 20);
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
        BukkitTask cd = new coolDownPulo(this.plugin, this).runTaskLater(this.plugin, 20 * 20);
    }

    public void fim_pulo() {
        this.world.playSound(this.player.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1, 2);
        this.isPulando = false;
        double raio = this.player.getFallDistance() / 1.5;
        double intensidade = this.player.getFallDistance() / 7;
        double fallDistance = this.player.getFallDistance();
        List<Entity> entidades_proximas = this.player.getNearbyEntities(2 * fallDistance,
                Math.max(10, 2 * fallDistance), 2 * fallDistance);
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

    public void sobreVisao() {
        if (this.isOnCoolDownSobreVisao) {
            this.player.sendMessage("A habilidade está em cooldown");
            return;
        }
        List<Entity> entidades_proximas = this.player.getNearbyEntities(25, 20, 25);
        for (Entity entity : entidades_proximas) {
            if (entity instanceof LivingEntity) {
                LivingEntity to_find = (LivingEntity) entity;
                to_find.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 8 * 20, 0));
            }
        }
        if (entidades_proximas.size() == 0) {
            this.player.sendMessage("Parece que não há monstros por perto");
        } else {
            this.player.sendMessage("As paredes revelaram seus mistérios");
        }
        this.isOnCoolDownSobreVisao = true;
        BukkitTask cd = new coolDownSobreVisao(this.plugin, this).runTaskLater(this.plugin, 60 * 20);
    }

    // Cooldown
    public void eraseCoolDownRoot() {
        this.player.sendMessage("O coolDown de " + ChatColor.GREEN + "root" + " terminou");
        this.isOnCoolDownRoot = false;
    }
    public void eraseCoolDownHealWeapon() {
        this.player.sendMessage("O coolDown de " + ChatColor.GREEN + "Heal Weapon" + " terminou");
        this.isOnCoolDownHealWeapon = false;
    }
    public void eraseCoolDownMagicShield() {
        this.player.sendMessage("O coolDown de " + ChatColor.LIGHT_PURPLE + "Magic Shield" + " terminou");
        this.isOnCoolDownMagicShield = false;
    }
    public void eraseCoolDownSlimeSave() {
        this.player.sendMessage("O coolDown de " + ChatColor.GREEN + "Slime Save" + "terminou");
        this.isOnCoolDownSlimeSave = false;
    }
    public void eraseCoolDownPickThrow() {
        this.isOnCoolDownPickThrow = false;
        this.player.sendMessage("O cooldown de " + ChatColor.LIGHT_PURPLE + "Pick Throw acabou!");
    }
    public void eraseCoolDownSpeedSpell() {
        this.isOnCoolDownSpeedSpell = false;
        this.player.sendMessage("O cooldown de " + ChatColor.BLUE + "SpeedSpell acabou");
    }
    public void eraseCoolDownPulo() {
        this.isOnCoolDownPulo = false;
        this.player.sendMessage("O cooldown de " + ChatColor.LIGHT_PURPLE + "Pulo Pesado acabou");
    }
    public void eraseCoolDownSobreVisao() {
        this.isOnCoolDownSobreVisao = false;
        this.player.sendMessage("O cooldown de " + ChatColor.WHITE + "Sobre Visão acabou");
    }

    // Remove blocos de magia (Bonus: Recebe proteção)
    public void removeMagicShield() {
        for (Block block : blocosMagicShield) {
            block.setType(Material.AIR);
        }
        blocosMagicShield.clear();
        receiveProtection(this.player, 1);
    }

    // Boosts
    public void speedBoost(LivingEntity entity, int nivel) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 8, 0));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 8, nivel));
    }
    public void receberEscudo(LivingEntity entity, int nivel) {
        if (nivel < 1) return;
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 8, 0));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 8, nivel));
    }
    public void receiveProtection(LivingEntity entity, int nivel) {
        if (nivel < 1) return;
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6 * 20, nivel - 1));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6 * 20, nivel - 1));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 6 * 20, nivel - 1));
    }
    public void receiveRoot(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 999));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5 * 20, 1));
    }

    // Getters
    public boolean getIsPulando() {
        return this.isPulando;
    }
    public Entity getHolded() {
        return this.player_segurado;
    }
}
