package Skills;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import PvpBalance.Effects;
import PvpBalance.ParticleEffect;

public class SuperJump {
	public static void Jump(Player player,double d){
        float hForce = 20 / 10.0F;
        float vForce = 15 / 10.0F;
        Vector direction = player.getLocation().getDirection();
        Vector forward = direction.multiply(15);
        Vector v = player.getLocation().toVector().subtract(player.getLocation().add(0,17,0).toVector());
        v.add(forward);
        v.setY(5);
        v.normalize();
        v.multiply(hForce*d);
        v.setY(vForce*d);
        player.setVelocity(v);
		List<Player> list = SkillHandler.getPlayers(20, player);
		if(!list.isEmpty())
		{
			for(Player lplayer:list){
				lplayer.playSound(lplayer.getLocation(), Sound.HORSE_JUMP, 3.0F, 0.533F);
			}
		}
		Effects.effectSuperJump(player);
		Effects.effectSuperJump(player);
		Effects.effectSuperJump(player);
	}
}
