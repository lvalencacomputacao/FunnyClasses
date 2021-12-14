package me.lucasvalenca.funnyclasses.ajuda;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.lang.Math;
import java.util.Map;

public class Ajuda {

    Map<String, PotionEffectType> spellName_to_spellObject = new HashMap<String, PotionEffectType>() {{
       put("regeneration", PotionEffectType.REGENERATION);
       put("absorption", PotionEffectType.ABSORPTION);
       put("speed", PotionEffectType.SPEED);
    }};

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

    public void get_nearby_and_apply(Player player, int x, int y, int z, String spell, int duration, int level) {
        List<Entity> nearby_entities = player.getNearbyEntities(x, y, z);

        PotionEffectType spell_object = spellName_to_spellObject.get(spell);
        level = Math.max(1, level);
        PotionEffect potion = new PotionEffect(spell_object, duration, level-1);
        for (Entity entity : nearby_entities) {
            LivingEntity living_entity = (LivingEntity) entity;
            living_entity.addPotionEffect(potion);
            player.sendMessage(living_entity.getName());
        }
    }
}
