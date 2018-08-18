package com.jaoafa.jaoReputation.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.jaoReputation.JaoReputation;
import com.jaoafa.jaoReputation.Lib.PermissionsManager;
import com.jaoafa.jaoReputation.Lib.ReputationManager;

public class Cmd_Good implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Good(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			JaoReputation.SendMessage(sender, cmd, "このコマンドはプレイヤーからのみ実行できます。");
			return true;
		}
		Player performer = (Player) sender;
		String group = PermissionsManager.getPermissionMainGroup(performer);
		if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
			JaoReputation.SendMessage(sender, cmd, "このコマンドは管理部・モデレーター・常連のみ使用可能です。");
			return true;
		}
		if(args.length == 1){
			// 理由なし
			String playername = args[0];
			OfflinePlayer player = Bukkit.getOfflinePlayer(playername);
			if(player == null){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");
				return true;
			}
			JaoReputation.SendMessage(sender, cmd, ChatColor.GRAY + "Good, Badを付ける際は、できる限り理由(Reason)を付けてください。/good <Reason>");

			int today = ReputationManager.getTodayGoodBadCount(performer);
			if(today >= 10){
				JaoReputation.SendMessage(sender, cmd, "本日の評価回数を超過しました。また明日評価をお願いします！");
				return true;
			}

			ReputationManager rm = new ReputationManager(player);
			if(!rm.isEvaluable()){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + player.getName() + "」は評価できません。");
				return true;
			}
			if(rm.Good(performer)){
				JaoReputation.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」をGOODとして評価しました。");
			}else{
				JaoReputation.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」をGOODとして評価できませんでした。");
			}
			return true;
		}else if(args.length >= 2){
			// 理由あり
			String playername = args[0];
			OfflinePlayer player = Bukkit.getOfflinePlayer(playername);
			if(player == null){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");
				return true;
			}
			String reason = "";
			int c = 1;
			while(args.length > c){
				reason += args[c];
				if(args.length != (c+1)){
					reason += " ";
				}
				c++;
			}

			int today = ReputationManager.getTodayGoodBadCount(performer);
			if(today >= 10){
				JaoReputation.SendMessage(sender, cmd, "本日の評価回数を超過しました。また明日評価をお願いします！");
				return true;
			}

			ReputationManager rm = new ReputationManager(player);
			if(!rm.isEvaluable()){
				JaoReputation.SendMessage(sender, cmd, "指定されたプレイヤー「" + player.getName() + "」は評価できません。");
				return true;
			}
			if(rm.Good(performer, reason)){
				JaoReputation.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」をGOODとして評価しました。");
			}else{
				JaoReputation.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」をGOODとして評価できませんでした。");
			}
			return true;
		}
		return false;
	}
}
