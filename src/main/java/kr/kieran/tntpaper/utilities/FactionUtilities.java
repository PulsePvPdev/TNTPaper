package kr.kieran.tntpaper.utilities;

import com.massivecraft.factions.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionUtilities {

    public boolean isInFactionClaim(Player player, Location location) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = getFaction(location);
        return faction != null && faction.isNormal() && ((fPlayer.getFaction() != null && fPlayer.getFaction().equals(faction)) || faction.playerHasOwnershipRights(fPlayer, new FLocation(location)));
    }

    public boolean isInForeignClaim(Player player, Location location) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = getFaction(location);
        return faction != null && (fPlayer.getFaction() != null && !fPlayer.getFaction().equals(faction) && !faction.playerHasOwnershipRights(fPlayer, new FLocation(location)));
    }

    private Faction getFaction(Location location) {
        return Board.getInstance().getFactionAt(new FLocation(location));
    }

}
