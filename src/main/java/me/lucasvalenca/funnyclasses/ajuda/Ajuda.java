package me.lucasvalenca.funnyclasses.ajuda;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.lang.Math;

public class Ajuda {
    public static double getDistancia(Player player, Entity entidade) {
        double deslocamento_x = player.getLocation().getX() - entidade.getLocation().getX();
        double deslocamento_y = player.getLocation().getY() - entidade.getLocation().getY();
        double deslocamento_z = player.getLocation().getZ() - entidade.getLocation().getZ();
        return Math.sqrt(Math.pow(deslocamento_x, 2) + Math.pow(deslocamento_y, 2) + Math.pow(deslocamento_z, 2));
    }

    public static boolean isOnGround(Entity entidade) {
        Location loc = entidade.getLocation();
        Location block_to_check = new Location(entidade.getWorld(), loc.getX(), loc.getY() - 2, loc.getZ());
        return block_to_check.getBlock().getType() != Material.AIR &&
                entidade.getVelocity().getY() <= 0;
    }

    public static Location get_eye_location(Entity entidade) {
        Location leg_location = entidade.getLocation();
        double x = leg_location.getX();
        double y = leg_location.getY() + 1;
        double z = leg_location.getZ();
        Location eye_location = new Location(entidade.getWorld(), x, y, z);
        return eye_location;
    }
}
