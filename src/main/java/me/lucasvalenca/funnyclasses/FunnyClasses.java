package me.lucasvalenca.funnyclasses;

import me.lucasvalenca.funnyclasses.classes.Arquiteto;
import me.lucasvalenca.funnyclasses.classes.Errante;
import me.lucasvalenca.funnyclasses.classes.Protetor;
import me.lucasvalenca.funnyclasses.classes.Sonhador;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FunnyClasses extends JavaPlugin implements Listener {

    Map<Player, String> classOfPlayer = new HashMap<Player, String>();
    Map<Player, Protetor> protetorOfPlayer = new HashMap<Player, Protetor>();
    Map<Player, Sonhador> sonhadorOfPlayer = new HashMap<Player, Sonhador>();
    Map<Player, Arquiteto> arquitetoOfPlayer = new HashMap<Player, Arquiteto>();
    Map<Player, Errante> erranteOfPlayer = new HashMap<Player, Errante>();

    boolean lightmode = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("escolher") && (sender instanceof Player) && args.length == 1) {
            Player player = (Player) sender;
            String className = args[0];
            if (className.equalsIgnoreCase("Protetor")) {
                Protetor protetor = new Protetor(player, this);
                classOfPlayer.put(player, "Protetor");
                protetorOfPlayer.put(player, protetor);

                player.sendMessage("Você agora é um " + ChatColor.BLUE + "Protetor");
            } else if (className.equalsIgnoreCase("Sonhador")) {
                Sonhador sonhador = new Sonhador(player, this);
                classOfPlayer.put(player, "Sonhador");
                sonhadorOfPlayer.put(player, sonhador);

                player.sendMessage("Você agora é um " + ChatColor.LIGHT_PURPLE + "Sonhador");
            } else if (className.equalsIgnoreCase("Arquiteto")) {
                Arquiteto arquiteto = new Arquiteto(player, this);
                classOfPlayer.put(player, "Arquiteto");
                arquitetoOfPlayer.put(player, arquiteto);

                player.sendMessage("Você agora é um " + ChatColor.RED + "Arquiteto");
            } else if (className.equalsIgnoreCase("Errante")) {
                Errante errante = new Errante(player, this);
                classOfPlayer.put(player, "Errante");
                erranteOfPlayer.put(player, errante);

                player.sendMessage("Você agora é um " + ChatColor.GREEN + "Errante");
            }
        }
        else if (command.getName().equalsIgnoreCase("lightmode") && sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length != 1) {
                player.sendMessage("Falta de argumentos. Use '/lightmode on' para ligar o lightmode ou" +
                        " '/lightmode off' para desligar o lightmode");
            } else {
                if (args[0].equalsIgnoreCase("on")) {
                    if (lightmode) {
                        player.sendMessage("Light mode ja está ligado");
                    } else {
                        player.sendMessage("Light mode foi ligado!!");
                        lightmode = true;
                    }
                } else if (args[0].equalsIgnoreCase("off")) {
                    if (lightmode) {
                        player.sendMessage("Light mode foi desligado!!");
                        lightmode = false;
                    } else {
                        player.sendMessage("Light mode já está desligado");
                    }
                }
            }
        }
        return true;
    }

    // Para protetores
    @EventHandler
    public boolean OnHitEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String result = classOfPlayer.get(player);
            if (classOfPlayer.get(player) != null && classOfPlayer.get(player).equals("Protetor") &&
                    player.getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE) {
                Entity entidadeMachucada = event.getEntity();
                event.setCancelled(true);
                protetorOfPlayer.get(player).HealWeaponOrRoot(entidadeMachucada);
            }
        }
        return true;
    }

    // Para protetores
    @EventHandler
    public boolean onClickProtetor(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (classOfPlayer.containsKey(player) && player.getInventory().getItemInMainHand().getType() == Material.STICK
                && classOfPlayer.get(player).equals("Protetor")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    protetorOfPlayer.get(player).usarMagia();
                } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    protetorOfPlayer.get(player).mudarMagia();
                }
            }
        }
        else if (classOfPlayer.containsKey(player) && player.getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD
            && classOfPlayer.get(player).equalsIgnoreCase("Protetor")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    protetorOfPlayer.get(player).puloPesado();
                }
            }
        } else if (classOfPlayer.containsKey(player) && player.getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE
                && classOfPlayer.get(player).equalsIgnoreCase("Protetor")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    protetorOfPlayer.get(player).sobreVisao();
                }
            }
        }
        return true;
    }

    @EventHandler
    public boolean onMoveProtetor(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (classOfPlayer.containsKey(player) && classOfPlayer.get(player).equalsIgnoreCase("Protetor")
            && protetorOfPlayer.get(player).getIsPulando()) {
            Location loc = player.getLocation();
            Location block_to_check = new Location(player.getWorld(), loc.getX(), loc.getY() - 2, loc.getZ());

            if (block_to_check.getBlock().getType() != Material.AIR &&
                    player.getVelocity().getY() <= 0) {
                protetorOfPlayer.get(player).fim_pulo();
            }
        }
        return true;
    }

    @EventHandler
    public boolean onInteractPlayers(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (classOfPlayer.containsKey(player) && classOfPlayer.get(player).equals("Protetor")) {
            if (player.isSneaking()) {
                protetorOfPlayer.get(player).jogarPlayer();
            }
            else {
                protetorOfPlayer.get(player).pegarPlayer(event.getRightClicked());
            }
        }

        return true;
    }

    @EventHandler
    public boolean entityDeath(EntityDeathEvent event) {
        for (Map.Entry<Player, Protetor> entry : protetorOfPlayer.entrySet()) {
            Entity holded = entry.getValue().getHolded();
            Entity dead_entity = event.getEntity();
            if (holded == dead_entity) {
                entry.getValue().holdedMorto();
            }
        }
        return true;
    }


    //--------------------------------------------------------------------------------------------------------------

    // Para sonhadores
    @EventHandler
    public boolean onClickSonhador(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (classOfPlayer.containsKey(player) &&
                player.getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD
                && classOfPlayer.get(player).equals("Sonhador")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    sonhadorOfPlayer.get(player).superPulo();
                } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    sonhadorOfPlayer.get(player).teleportar();
                }
            }
        } else if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE
        && classOfPlayer.get(player).equals("Sonhador")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    sonhadorOfPlayer.get(player).puxao();
                }
            }
        }
        return true;
    }

    // Para Sonhadores
    @EventHandler
    public boolean onHitSonhador(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            if (classOfPlayer.containsKey(damager) && classOfPlayer.get(damager).equalsIgnoreCase("Sonhador")
                && sonhadorOfPlayer.get(damager).getCoolDownTeleportar()) {
                sonhadorOfPlayer.get(damager).TNTCombo(event.getEntity());
            }
        }
        return false;
    }

    // Para Sonhadores
    @EventHandler
    public boolean onShift(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (classOfPlayer.containsKey(player) && classOfPlayer.get(player).equals("Sonhador")) {
            ItemStack[] contents = player.getInventory().getContents();
            for (int i = 0; i <= 8; i++) {
                if (contents[i] != null) {
                    if (contents[i].getType() == Material.WOODEN_AXE) {
                        if (player.isSneaking()) {
                            sonhadorOfPlayer.get(player).dash(lightmode);
                        }
                    }
                }
            }
        }
        return true;
    }

    // Para Arquitetos
    @EventHandler
    public boolean onClickArquiteto(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (classOfPlayer.containsKey(player) && player.getInventory().getItemInMainHand().getType() == Material.STICK
                && classOfPlayer.get(player).equals("Arquiteto")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!arquitetoOfPlayer.get(player).isBuildingBridge()) {
                        arquitetoOfPlayer.get(player).superPonte();
                    } else {
                        arquitetoOfPlayer.get(player).setPauseIsBuildingBridge();
                    }
                }
            }
        } else if (classOfPlayer.containsKey(player) &&
                player.getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE &&
                classOfPlayer.get(player).equals("Arquiteto")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    arquitetoOfPlayer.get(player).deslocadorDeMateriaPreparar();
                } else if (event.getAction() == Action.LEFT_CLICK_AIR ||
                        event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    arquitetoOfPlayer.get(player).deslocadorDeMateriaExecutar();
                }
            }
        } else if (classOfPlayer.containsKey(player) &&
                player.getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD &&
                classOfPlayer.get(player).equals("Arquiteto")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    arquitetoOfPlayer.get(player).ganchoDeslocador();
                }
            }
        } else if (classOfPlayer.containsKey(player) &&
                player.getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE &&
                classOfPlayer.get(player).equals("Arquiteto")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    arquitetoOfPlayer.get(player).machineGun();
                }
            }
        }
        return true;
    }

    // Para Arquitetos
    @EventHandler
    public boolean arquitetoHurtEntity(EntityDamageByEntityEvent event) {
        Player player;
        if (!(event.getDamager() instanceof Player)) {
            return false;
        }
        player = (Player) event.getDamager();

        if (classOfPlayer.containsKey(player) &&
                player.getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE &&
                classOfPlayer.get(player).equals("Arquiteto")) {
            arquitetoOfPlayer.get(player).corteCircular();
        }
        return true;
    }

    // Para Arquitetos
    @EventHandler
    public boolean onMoveArquiteto(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (classOfPlayer.containsKey(player) && classOfPlayer.get(player).equals("Arquiteto")) {
            if (arquitetoOfPlayer.get(player).isBuildingBridge() &&
            arquitetoOfPlayer.get(player).isBuildingBridgePause()) {
                Location location = player.getLocation();

                double x = location.getX();
                double y = location.getY() - 1;
                double z = location.getZ();
                Location block_location = new Location(player.getWorld(), x, y, z);
                Block block = block_location.getBlock();
                block.setType(Material.COBBLESTONE);
                //player.sendMessage("funcionou");
            }
            if (arquitetoOfPlayer.get(player).getAtirando()) {
                arquitetoOfPlayer.get(player).atirar();
            }
        }
        return true;
    }


    // Para errantes
    @EventHandler
    public boolean VehicleMove(VehicleMoveEvent event) {
        List<Entity> lista = event.getVehicle().getPassengers();
        Player player = null;
        for (Entity passageiro : lista) {
            if (passageiro instanceof Player) {
                player = (Player) passageiro;
            }
        }

        if (player == null || !classOfPlayer.containsKey(player) ||
                !classOfPlayer.get(player).equalsIgnoreCase("Errante") ||
                !(event.getVehicle() instanceof Boat)) return false;

        erranteOfPlayer.get(player).carro(event.getVehicle());

        //player.sendMessage("Deu certo");
        return true;
    }

    // Para errantes (coppi)
    @EventHandler
    public boolean onClickErrante(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (classOfPlayer.containsKey(player) && player.getInventory().getItemInMainHand().getType() == Material.STICK
                && classOfPlayer.get(player).equals("Errante")) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    erranteOfPlayer.get(player).set_direcao_vertical(1);
                    erranteOfPlayer.get(player).subir_carro();
                } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    erranteOfPlayer.get(player).set_direcao_vertical(-1);
                    erranteOfPlayer.get(player).subir_carro();
                }
            }
        } else if (classOfPlayer.containsKey(player) && classOfPlayer.get(player).equals("Errante") &&
                player.getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE) {
            if (event.getHand() != null && event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    erranteOfPlayer.get(player).trovejar();
                }
            }
        } else if (classOfPlayer.containsKey(player) && classOfPlayer.get(player).equals("Errante") &&
                player.getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE) {
            erranteOfPlayer.get(player).destruir();
        }
        return true;
    }



    // testing
    /*
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Vector vetor = event.getPlayer().getVelocity();
        event.getPlayer().sendMessage("x = " + vetor.getX() + " || y = " + vetor.getY() + " || z = " + vetor.getZ());
    }
    @EventHandler
    public boolean onShiftTest(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Chegou no evento");
        ItemStack[] stacks = player.getInventory().getContents();
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] != null) {
                player.sendMessage(i + " = " + stacks[i].getType());
            }
        }
        return true;
    }
    @EventHandler
    public boolean onWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        player.sendMessage("Velocidade de movimento default é = " + attribute);
        return true;
    }
    */
}
