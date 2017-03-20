package me.bpweber.practiceserver.mobs;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

import java.util.*;

/**
 * Created by Giovanni on 11-2-2017.
 */
public class NameFixer implements Listener {

    private final List<EntityType> checkedEntities = Arrays.asList(EntityType.ZOMBIE, EntityType.PIG_ZOMBIE,
            EntityType.SKELETON, EntityType.SLIME,
            EntityType.MAGMA_CUBE, EntityType.IRON_GOLEM,
            EntityType.SILVERFISH, EntityType.GHAST);

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (this.checkedEntities.contains(event.getEntityType())) {
            event.getEntity().setCustomNameVisible(true);
        }
    }
}
