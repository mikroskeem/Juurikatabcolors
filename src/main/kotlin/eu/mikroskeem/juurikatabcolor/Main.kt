package eu.mikroskeem.juurikatabcolor

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.tehkode.permissions.PermissionUser
import ru.tehkode.permissions.bukkit.PermissionsEx

/**
 * @author Mark Vainomaa
 */
class Main : JavaPlugin(), Listener {
    override fun onEnable() {
        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()
        server.pluginManager.registerEvents(this, this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name == "jtc") {
            sender.sendMessage("Configuration reloaded")
            reloadConfig()
            server.onlinePlayers.forEach { it.playerListName = getTablistName(it) }
            return true
        }
        return false
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerJoinEvent) {
        event.player.playerListName = getTablistName(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: ru.tehkode.permissions.events.PermissionEntityEvent) {
        if(event.entity is PermissionUser) {
            val player : Player? = server.onlinePlayers.filter { it.name == event.entity.name }.first()
            if(player != null) {
                player.playerListName = getTablistName(player)
            }
        }
    }

    fun getTablistName(player: Player) : String {
        val pexPlayer = PermissionsEx.getUser(player)
        val format = config.getString("format", "%name")
        return ChatColor.translateAlternateColorCodes('&', format
                .replace("%name", player.name)
                .replace("%displayname", player.displayName)
                .replace("%prefix", pexPlayer.prefix)
                .replace("%suffix", pexPlayer.suffix))
    }
}