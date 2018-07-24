package com.jaoafa.jaoReputation.Lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.jaoafa.jaoReputation.JaoReputation;

public class ReputationManager {
	private OfflinePlayer player = null;
	private int ID = -1; // jaoReputation ID
	public ReputationManager(OfflinePlayer player){
		this.player = player;
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jaoReputation WHERE uuid = ?");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				ID = res.getInt("id");
			}else{
				int id = Create();
				if(id == -1){
					throw new IllegalStateException();
				}
				ID = id;
			}
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
	}
	private int Create(){
		if(player == null){
			return -1;
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jaoReputation (player, uuid, reputation) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setInt(3, 0);
			statement.executeUpdate();
			ResultSet res = statement.getGeneratedKeys();
			if(res != null && res.next()){
				return res.getInt(1);
			}else{
				throw new IllegalStateException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return -1;
	}
	public boolean Good(Player performer){
		if(ID == -1){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaoReputation SET reputation = reputation + 1 WHERE id = ?");
			statement.setInt(1, ID);
			int i = statement.executeUpdate();
			if(i != 1){
				return false;
			}
			int rep = getReputation();
			Logger(performer, 1, rep);
			for(Player p : Bukkit.getOnlinePlayers()){
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
					continue;
				}
				p.sendMessage("[jaoReputation] " + ChatColor.GREEN + player.getName() + "を" + performer.getName() + "が" + ChatColor.AQUA + "GOOD" + ChatColor.GREEN + "しました！");
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return false;
	}
	public boolean Good(Player performer, String reason){
		if(ID == -1){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaoReputation SET reputation = reputation + 1 WHERE id = ?");
			statement.setInt(1, ID);
			int i = statement.executeUpdate();
			if(i != 1){
				return false;
			}
			int rep = getReputation();
			Logger(performer, 1, rep, reason);
			for(Player p : Bukkit.getOnlinePlayers()){
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
					continue;
				}
				p.sendMessage("[jaoReputation] " + ChatColor.GREEN + player.getName() + "を" + performer.getName() + "が「" + reason + "」という理由で" + ChatColor.AQUA + "GOOD" + ChatColor.GREEN + "しました！");
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return false;
	}
	public boolean Good(String performer, String performer_uuid, int good, String reason){
		if(ID == -1){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaoReputation SET reputation = reputation + ? WHERE id = ?");
			statement.setInt(1, good);
			statement.setInt(2, ID);
			int i = statement.executeUpdate();
			if(i != 1){
				return false;
			}
			int rep = getReputation();
			Logger(performer, performer_uuid, -1, rep, reason);
			for(Player p : Bukkit.getOnlinePlayers()){
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
					continue;
				}
				p.sendMessage("[jaoReputation] " + ChatColor.GREEN + player.getName() + "を" + performer + "が「" + reason + "」という理由で" + ChatColor.AQUA + "GOOD×" + good + ChatColor.GREEN + "しました！");
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return false;
	}
	public boolean Bad(Player performer){
		if(ID == -1){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaoReputation SET reputation = reputation - 1 WHERE id = ?");
			statement.setInt(1, ID);
			int i = statement.executeUpdate();
			if(i != 1){
				return false;
			}
			int rep = getReputation();
			Logger(performer, -1, rep);
			for(Player p : Bukkit.getOnlinePlayers()){
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
					continue;
				}
				p.sendMessage("[jaoReputation] " + ChatColor.GREEN + player.getName() + "を" + performer.getName() + "が" + ChatColor.RED + "BAD" + ChatColor.GREEN + "しました…");
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return false;
	}

	public boolean Bad(Player performer, String reason){
		if(ID == -1){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaoReputation SET reputation = reputation - 1 WHERE id = ?");
			statement.setInt(1, ID);
			int i = statement.executeUpdate();
			if(i != 1){
				return false;
			}
			int rep = getReputation();
			Logger(performer, -1, rep, reason);
			for(Player p : Bukkit.getOnlinePlayers()){
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
					continue;
				}
				p.sendMessage("[jaoReputation] " + ChatColor.GREEN + player.getName() + "を" + performer.getName() + "が「" + reason + "」という理由で" + ChatColor.RED + "BAD" + ChatColor.GREEN + "しました…");
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return false;
	}
	public boolean Bad(String performer, String performer_uuid, int bad, String reason){
		if(ID == -1){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaoReputation SET reputation = reputation - ? WHERE id = ?");
			statement.setInt(1, bad);
			statement.setInt(2, ID);
			int i = statement.executeUpdate();
			if(i != 1){
				return false;
			}
			int rep = getReputation();
			Logger(performer, performer_uuid, -1, rep, reason);
			for(Player p : Bukkit.getOnlinePlayers()){
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
					continue;
				}
				p.sendMessage("[jaoReputation] " + ChatColor.GREEN + player.getName() + "を" + performer + "が「" + reason + "」という理由で" + ChatColor.RED + "BAD×" + bad + ChatColor.GREEN + "しました…");
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return false;
	}
	public int getReputation(){
		if(ID == -1){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jaoReputation WHERE id = ?");
			statement.setInt(1, ID);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getInt("reputation");
			}
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return -1;
	}
	public static int getTodayGoodBadCount(OfflinePlayer player){
		try {
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT COUNT(*) FROM jaoReputation_History WHERE performer_uuid = ? AND date LIKE ?");
			statement.setString(1, player.getUniqueId().toString());
			statement.setString(2, date.format(new Date()) + "%");
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getInt(1);
			}else{
				return 0;
			}
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return 0;
	}
	public boolean isEvaluable(){
		if(player == null){
			throw new IllegalStateException();
		}
		String group = PermissionsManager.getPermissionMainGroup(player.getName());
		if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
			return true;
		}
		return false;
	}
	void Logger(Player performer, int reputation, int nowreputation){
		if(ID == -1){
			throw new IllegalStateException();
		}
		if(player == null){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jaoReputation_History (player, uuid, performer, performer_uuid, reputation, nowreputation) VALUES (?, ?, ?, ?, ?, ?);");
			statement.setString(1, player.getName()); // Player
			statement.setString(2, player.getUniqueId().toString()); // UUID
			statement.setString(3, performer.getName()); // 実行者Name
			statement.setString(4, performer.getUniqueId().toString()); // 実行者UUID
			statement.setInt(5, reputation); // 変更
			statement.setInt(6, nowreputation); // 変更後
			statement.executeUpdate();
			Discord.send("293856671799967744", "__**[jaoReputation]**__ " + player.getName() + " : " + reputation + " - (" + performer.getName() + ") -> " + nowreputation);
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return;
	}
	void Logger(Player performer, int reputation, int nowreputation, String reason){
		if(ID == -1){
			throw new IllegalStateException();
		}
		if(player == null){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jaoReputation_History (player, uuid, performer, performer_uuid, reputation, nowreputation, reason) VALUES (?, ?, ?, ?, ?, ?, ?);");
			statement.setString(1, player.getName()); // Player
			statement.setString(2, player.getUniqueId().toString()); // UUID
			statement.setString(3, performer.getName()); // 実行者Name
			statement.setString(4, performer.getUniqueId().toString()); // 実行者UUID
			statement.setInt(5, reputation); // 変更
			statement.setInt(6, nowreputation); // 変更後
			statement.setString(7, reason);
			statement.executeUpdate();
			Discord.send("293856671799967744", "__**[jaoReputation]**__ " + player.getName() + " : " + reputation + " (" + performer.getName() + ") -> " + nowreputation + "\nReason:```" + reason + "```");
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return;
	}
	void Logger(String performer, String performer_uuid, int reputation, int nowreputation, String reason){
		if(ID == -1){
			throw new IllegalStateException();
		}
		if(player == null){
			throw new IllegalStateException();
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jaoReputation_History (player, uuid, performer, performer_uuid, reputation, nowreputation, reason) VALUES (?, ?, ?, ?, ?, ?, ?);");
			statement.setString(1, player.getName()); // Player
			statement.setString(2, player.getUniqueId().toString()); // UUID
			statement.setString(3, performer); // 実行者Name
			statement.setString(4, performer_uuid); // 実行者UUID
			statement.setInt(5, reputation); // 変更
			statement.setInt(6, nowreputation); // 変更後
			statement.setString(7, reason);
			statement.executeUpdate();
			Discord.send("293856671799967744", "__**[jaoReputation]**__ " + player.getName() + " : " + reputation + " (" + performer + ") -> " + nowreputation + "\nReason:```" + reason + "```");
		} catch (ClassNotFoundException | SQLException e) {
			JaoReputation.BugReporter(e);
		}
		return;
	}
}
