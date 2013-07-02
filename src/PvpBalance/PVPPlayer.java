package PvpBalance;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import Event.PBEntityRegainHealthEvent;


public class PVPPlayer
{
	private Player player;
	private int health;
	private int healthLastTick;
	private int maxHealth;
	private int cooldown;
	private int hitCoolDown;
	private int combatCoolDown;
	private int hunger;
	private int armorEventLastTick;
	private boolean inCombat;
	private boolean isDead;
	private boolean canRegen;
	private boolean god;
	private boolean pvpstats;
	private boolean colorUp;
	
	public PVPPlayer(Player player)
	{
		this.player = player;
		this.health = 500;
		this.canRegen = true;
		this.healthLastTick = 500;
		this.maxHealth = 500;
		this.cooldown = 0;
		this.isDead = false;
		this.hitCoolDown = 0;
		this.hunger = player.getFoodLevel();
		this.inCombat = false;
		this.combatCoolDown = 0;
		this.armorEventLastTick = 0;
		colorUp = false;
	}

	public Player getPlayer()
	{
		return this.player;
	}
	
	public int getCombatCoolDown()
	{
		return combatCoolDown;
	}
	
	public int getCooldown()
	{
		return cooldown;
	}
	
	public boolean isDead()
	{
		return isDead;
	}
	
	public boolean isColorUp()
	{
		return colorUp;
	}
	
	public int getHealthLastTick()
	{
		return this.healthLastTick;
	}
	
	public int getMaxHealth()
	{
		return this.maxHealth;
	}
	
	public int gethealth()
	{
		return this.health;
	}
	
	public int getHunger()
	{
		return this.hunger;
	}
	
	public boolean isGod()
	{
		return god;
	}
	
	public int getHitCooldown()
	{
		return this.hitCoolDown;
	}
	
	public int getArmorEventLastTick()
	{
		return this.armorEventLastTick;
	}
	
	public void setGod(boolean god)
	{
		this.god = god;
	}
	
	public void setArmorEventLastTick(int armorEventLastTick)
	{
		this.armorEventLastTick = armorEventLastTick;
	}
	
	public void setCombatCoolDown(int combatCoolDown)
	{
		this.combatCoolDown = combatCoolDown;
	}
	
	public void setHunger(int hunger)
	{
		if(this.getHunger() - hunger < 1)
		{
			this.hunger = 1;
		}
		else
		{
			this.hunger = hunger;
		}
		
	}
	
	public void setHitCoolDown(int hitCoolDown)
	{
		this.hitCoolDown = hitCoolDown;
	}
	
	public void setCooldown(int cooldown)
	{
		this.cooldown = cooldown;
	}
	
	public void setIsDead(boolean isDead)
	{
		this.isDead = isDead;
	}
	
	public void setColorUp(boolean value)
	{
		colorUp = value;
	}
	
	public void setMaxHealth(int maxHealth)
	{
		if(this.health == this.maxHealth)
		{
			this.maxHealth = maxHealth;
			this.sethealth(this.maxHealth);
			if(this.armorEventLastTick == 1)
			{
				player.sendMessage(ChatColor.GREEN + "[HEALTH]:" + ChatColor.YELLOW + " change in armor your new health is: " + ChatColor.GREEN + this.maxHealth);
			}
		}
		else
		{
			this.maxHealth = maxHealth;
			if(this.armorEventLastTick == 1)
			{
				player.sendMessage(ChatColor.GREEN + "[HEALTH]:" + ChatColor.YELLOW + " change in armor your new health is: " + ChatColor.GREEN + this.maxHealth);
				player.sendMessage(ChatColor.GREEN + "[HEALTH]:" + ChatColor.RED + "Due to recent combat you will gain life to your new max");
			}

		}
	}
	
	public void sethealth(int health)
	{
		if(pvpstats)
		{
			if(this.health > health)
			{
				int decreasedBy = this.health - health;
				if(decreasedBy > 10)
				{
					player.sendMessage(ChatColor.YELLOW + "[HEALTH]: " + ChatColor.RED + "- " + decreasedBy);
				}
			}
			if(this.health < health)
			{
				int increasedBy = health - this.health;
				if(increasedBy > 10)
				{
					player.sendMessage(ChatColor.YELLOW + "[HEALTH]: " + ChatColor.GREEN + "+ " + increasedBy);
				}
			}
		}
		if(this.player.getGameMode() == GameMode.SURVIVAL)
		{
			if(health > this.maxHealth)
			{
				health = this.maxHealth;
			}
			this.health = health;
			if(this.health <= 0)
			{
				this.health = 0;
			}
			this.setProperHealth();
		}
		String message = ("SIDEBAR,Health," + ChatColor.BLUE + "Health:" + ChatColor.RESET + "," + health);
		Bukkit.getMessenger().dispatchIncomingMessage(player, "Scoreboard", message.getBytes());
	}
	
	public void Damage(int dmg)
	{
		if(player.getGameMode().equals(GameMode.SURVIVAL) && !this.god)
		{
			health = health - dmg;
			if(this.health <= 0 && !this.isDead)
			{
				health = 0;
				this.player.setHealth(0);
				this.isDead = true;
			}
			if(healthLastTick > health)
			{
				cooldown = (int) (new Date().getTime()/1000);
			}
		}
		else
		{
			this.health = this.maxHealth;
			player.setFoodLevel(20);
		}
		String message = ("SIDEBAR,Health," + ChatColor.BLUE + "Health:" + ChatColor.RESET + "," + health);
		Bukkit.getMessenger().dispatchIncomingMessage(player, "Scoreboard", message.getBytes());
	}
	
	public void tick()
	{
		Date date = new Date();
		if(player.getGameMode() == GameMode.CREATIVE)
		{
			this.health = this.maxHealth;
			this.canRegen = true;
			this.healthLastTick = this.maxHealth;
			this.cooldown = 0;
			this.isDead = false;
			this.hitCoolDown = 0;
			this.inCombat = false;
			this.combatCoolDown = 0;
			this.armorEventLastTick = 0;
		}
		if(this.armorEventLastTick > 0)
		{
			this.armorEventLastTick--;
			Damage.calcArmor(player);
		}
		if(player.getHealth() <= 0)
		{
			this.health = 0;
		}
		if(this.health <= 0 && this.isDead != true)
		{
			this.player.setHealth(0);
			this.isDead = true;
		}
		setProperHealth();
		if((date.getTime()/1000) - cooldown < 20)
		{
			this.canRegen = false;
		}
		if(player.getFoodLevel() == 0)
		{
			this.canRegen = false;
		}
		this.hunger = this.player.getFoodLevel();
		if(this.hunger < 2 && this.health > 100)
		{
			this.canRegen = false;
			this.sethealth(this.gethealth() - 10);
		}
		if((date.getTime() / 1000) - combatCoolDown >= 40)
		{
			if(inCombat)
			{
				inCombat = false;
//				player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You are no longer in combat and may log off safely");
			}
		}
		else
		{
			if(!inCombat)
			{
				inCombat = true;
//				player.sendMessage(ChatColor.RED + "WARNING: you have entered combat if you log out within the next "
//						+ ChatColor.YELLOW + "= 20 Seconds =" + ChatColor.RED + " you will be automaticly killed and your loot will drop");
			}
		}
		if(PvpBalance.plugin.isDebug())
		{
			Bukkit.broadcastMessage("Cooldown: " + cooldown + " this.canRegen : " + this.canRegen + " MaxHealth: " + maxHealth + " Health: " + health );
			Bukkit.broadcastMessage("HUNGER LEVEL" + hunger);
			Bukkit.broadcastMessage(""+player.getExhaustion());
			Bukkit.broadcastMessage("HITCoolDown:" + hitCoolDown);
			
		}
		if(health < maxHealth && this.canRegen)
		{
			if(inCombat)
			{
				int heal = 5;
				PBEntityRegainHealthEvent pberh = new PBEntityRegainHealthEvent(player, heal, RegainReason.CUSTOM);
				Bukkit.getPluginManager().callEvent(pberh);
				if(pberh.isCancelled())
					return;
				this.sethealth(health + heal);
			}
			else
			{
				int heal = 10;
				PBEntityRegainHealthEvent pberh = new PBEntityRegainHealthEvent(player, heal, RegainReason.CUSTOM);
				Bukkit.getPluginManager().callEvent(pberh);
				if(pberh.isCancelled())
					return;
				this.sethealth(health + heal);
			}
		}
		if(health > maxHealth)
		{
			health = maxHealth;
		}
		healthLastTick = health;
	}
	
	public void setProperHealth()
	{
		if(this.isDead == false)
		{
			int realHealth = health/(maxHealth/20);
			if(realHealth <= 1)
			{
				realHealth = 1;
			}
			if(realHealth > 20)
			{
				realHealth = 20;
			}
			player.setHealth(realHealth);
		}
	}
	
	public boolean isPvpstats()
	{
		return pvpstats;
	}
	
	public void setPvpstats(boolean value)
	{
		pvpstats = value;
	}
	
	public boolean isInCombat()
	{
		return inCombat;
	}
	
	public void setInCombat(boolean value)
	{
		inCombat = value;
	}
}
