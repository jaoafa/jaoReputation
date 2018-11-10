package com.jaoafa.jaoReputation.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.jaoReputation.JaoReputation;
import com.jaoafa.jaoReputation.Lib.Discord;
import com.jaoafa.jaoReputation.Lib.MySQL;
import com.jaoafa.jaoReputation.Lib.ReputationManager;

public class Event_LoginLogoutDiffRepNotice implements Listener {
	JavaPlugin plugin;
	public Event_LoginLogoutDiffRepNotice(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Map<UUID, Integer> LoginReps = new HashMap<>();

	@EventHandler
	public void OnEvent_LoginRepCheck(PlayerJoinEvent event){
		Player player = event.getPlayer();
		ReputationManager rm = new ReputationManager(player);
		if(!rm.isEvaluable()){
			// not QD
			return;
		}
		int rep = rm.getReputation();
		LoginReps.put(player.getUniqueId(), rep);
	}
	@EventHandler
	public void OnEvent_LoginRepCheck(PlayerQuitEvent event){
		Player player = event.getPlayer();
		ReputationManager rm = new ReputationManager(player);
		if(!rm.isEvaluable()){
			// not QD
			return;
		}
		if(!LoginReps.containsKey(player.getUniqueId())){
			return;
		}
		int rep = rm.getReputation();
		int loginrep = LoginReps.get(player.getUniqueId());
		if(rep == loginrep){
			return;
		}
		int sa = rep - loginrep;
		String saText;
		if(sa > 0){
			saText = "+" + sa;
		}else if(sa == 0){
			return;
		}else{
			saText = "" + sa;
		}
		List<String> reasons = new ArrayList<>();
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jaoReputation_History WHERE uuid = ? ORDER BY id DESC LIMIT 5");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			while(res.next()){
				String symbol = "";
				if(res.getInt("reputation") > 0) symbol = "+";
				reasons.add(res.getString("date") + "「" + res.getString("reason") + "」: " + symbol + res.getInt("reputation"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		Discord.send("223582668132974594", ":scroll:__**[jaoReputation]**__ プレイヤー「" + player.getName() + "」のjaoReputation値に変更がありましたので通知します。\n"
				+ "```" + loginrep + " → " + rep + " (" + saText + ")```\n直近の理由: ```" + implode(reasons, "\n") + "```");
	}
	static <T> String implode(List<T> list, String glue) {
	    StringBuilder sb = new StringBuilder();
	    for (T e : list) {
	        sb.append(glue).append(e);
	    }
	    return sb.substring(glue.length());
	}
}
