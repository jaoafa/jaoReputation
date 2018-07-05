package com.jaoafa.jaoReputation.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.jaoReputation.JaoReputation;
import com.jaoafa.jaoReputation.Lib.PermissionsManager;
import com.jaoafa.jaoReputation.Lib.ReputationManager;

public class Cmd_Rep implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Rep(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 1) return false;
		if(sender instanceof Player){
			Player performer = (Player) sender;
			String group = PermissionsManager.getPermissionMainGroup(performer);
			if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
				JaoReputation.SendMessage(sender, cmd, "このコマンドは管理部・モデレーター・常連のみ使用可能です。");
				return true;
			}
			String playername = args[0];
			Player player = Bukkit.getPlayerExact(playername);
			if(player == null){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(playername);
				if(any_chance_player != null){
					JaoReputation.SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}
			ReputationManager rm = new ReputationManager(player);
			if(!rm.isEvaluable()){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + player.getName() + "」の評価値は確認できません。");
				return true;
			}
			int rep = rm.getReputation();
			JaoReputation.SendMessage(sender, cmd, player.getName() + " jao Reputation: " + rep);
			return true;
		}else{
			String playername = args[0];
			Player player = Bukkit.getPlayerExact(playername);
			if(player == null){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(playername);
				if(any_chance_player != null){
					JaoReputation.SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}
			ReputationManager rm = new ReputationManager(player);
			if(!rm.isEvaluable()){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + player.getName() + "」の評価値は確認できません。");
				return true;
			}
			int rep = rm.getReputation();
			JaoReputation.SendMessage(sender, cmd, player.getName() + " jao Reputation: " + rep);
			return true;
		}
	}
}
