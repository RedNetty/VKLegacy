package me.bpweber.practiceserver.DonationMechanics;

import me.bpweber.practiceserver.player.GamePlayer.GameConfig;
import me.bpweber.practiceserver.player.GamePlayer.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by jaxon on 3/26/2017.
 */
public class ZCashHandler implements Listener{

    public HashMap<UUID, Integer> zcashamount = new HashMap<>();

    public void onEnable() {
        loadZCash();
    }

    public void onDisable() {

    }


    public void loadZCash() {
        for(String s : GameConfig.get().getKeys(false)) {
            UUID id = UUID.fromString(s);
            int amount = GameConfig.get().getInt(s + ".Main.ZCash");
            zcashamount.put(id, amount);
        }
    }
    public void saveZCash() {
        for(UUID s : zcashamount.keySet()) {
            int amount = zcashamount.get(s);
            GameConfig.get().set(s + ".Main.ZCash", amount);
        }
    }
    public int getZcash(Player p) {
        if(zcashamount.containsKey(p.getUniqueId())) {
            return zcashamount.get(p.getUniqueId());
        }else{
            return 0;
        }
    }


    public void setZcash(Player p, int amount) {
        if (zcashamount.containsKey(p.getUniqueId())) {
            zcashamount.remove(p.getUniqueId());
            zcashamount.put(p.getUniqueId(), amount);
        }
    }
    public void subtractZcash(Player p, int amount) {
        if(zcashamount.containsKey(p.getUniqueId())) {
            zcashamount.remove(p.getUniqueId());
            zcashamount.put(p.getUniqueId(), zcashamount.get(p.getUniqueId()) - amount);

        }
    }

}
