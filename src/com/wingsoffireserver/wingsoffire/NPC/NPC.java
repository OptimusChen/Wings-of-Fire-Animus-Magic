package com.wingsoffireserver.wingsoffire.NPC;

import com.wingsoffireserver.wingsoffire.Main;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {

    public static ArrayList<EntityPlayer> NPC = new ArrayList<>();

    public static void spawnNPC(Location location){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.YELLOW + "" + ChatColor.BOLD + "Click");
        EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        npc.getDataWatcher().set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);

        gameProfile.getProperties().removeAll("textures");

        PropertyMap map = gameProfile.getProperties();

        map.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNzI5OTA4MDc0MCwKICAicHJvZmlsZUlkIiA6ICJiN2ZkYmU2N2NkMDA0NjgzYjlmYTllM2UxNzczODI1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EwNmU2NzNjMzk0MzA5N2RjYzg4ZTE3MDNhYmIxZWZlZTcyOTZiYzBjYzFhOTU0N2QyMDBkN2YzOGM5YzU5NDAiCiAgICB9CiAgfQp9", "eoH/M+M49Hew7JhBiEGJDXxmFJNqmU8yUIFgtJBcZZdyfVpjZnnR59YGPcyJZg/FBlvKBnV02red1O2OS7JGteKTow7cmmCDlyAyTyCxjqstyRKl9ZsPbVSbxboghTWAVrl4dLZTRrFe0/rHcMVXngkdU0z1talRVBg2ylr0faO7j8vx76MF+Lclq8W0ZCmLDagGZ5VADeXu3hmMkAXIFmzZKglJAH4i7ncxOP2Z1kwtYu+pD13XJBjDI1UDCSnm758fEFMNiUmg7JZ8RrVeeiyOFwdYE/U32g2scplC5eq7WbV6vVpXCArI4V4ZSUPELccO/aVgu8eD1SGnuLvYygq1TmDe+7nwDMTJXvihy2k9MDpbuS7SmpHWNFUfJPLeLX11uGdGM5/M+1QPxvvQ3wwPVVzJ2fCDUm8NnuQ4EpGB1Rn2uNkSsK1oQ5pMj4SIp9dHAs26QtlXr631QBLkru5QVVjfg7mGdPCFBRFWRWn3o9JOIdsWtHjr1Zi+K9QOwnjLZm3pQZLlVhAOW4vzsaMdnlqqTPFYycaJC9+HqU7ghlDlvU7VfiEmcYRP6MrKBzqEfeqILASiHadCLq6icCMFGUd63j2ohyCm6ZSqMYrvvcedhH0PhcV9Sn90+3x1F6IlSToCDz3aFlKPE9+APhW8HChfd+d7hSa3WT851/k="));

        NPC.add(npc);
        addNpcPacket(npc);

        int var = 1;
        if (Main.getInstance().getConfig().contains("data"))
            var = Main.getInstance().getConfig().getConfigurationSection("data").getKeys(false).size() + 1;

        Main.getInstance().getConfig().set("data." + var + ".x", location.getX());
        Main.getInstance().getConfig().set("data." + var + ".y", location.getY());
        Main.getInstance().getConfig().set("data." + var + ".z", location.getZ());
        Main.getInstance().getConfig().set("data." + var + ".yaw", location.getYaw());
        Main.getInstance().getConfig().set("data." + var + ".p", location.getPitch());
        Main.getInstance().getConfig().set("data." + var + ".world", location.getWorld().getName());
        Main.getInstance().saveConfig();
    }

    public static void loadNpc(Location location, GameProfile profile){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = profile;
        EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        npc.getDataWatcher().set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);

        NPC.add(npc);
        addNpcPacket(npc);
    }

    /*
    private static String[] getSkin(Player player, String name){
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

            String texture = property.get("value").getAsString();

        } catch (Exception e){

        }
    }
     */

    public static void addNpcPacket(EntityPlayer npc){
        for (Player player : Bukkit.getOnlinePlayers()){
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
            connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true));
            new BukkitRunnable() {
                @Override
                public void run() {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
            }.runTaskLater(Main.getInstance(), 10);
        }
    }

    public static void removeNpc(Player player, EntityPlayer npc){
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
    }

    public static void addJoinPacket(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (EntityPlayer npc : NPC){
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                    connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                    connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
                    connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                        }
                    }.runTaskLater(Main.getInstance(), 10);
                }
            }
        }.runTaskLater(Main.getInstance(), 1);
    }

    public static List<EntityPlayer> getNpcs(){
        return NPC;
    }

}
