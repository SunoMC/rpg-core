## Important
- new:
  - `net.sunomc.rpg.game.prolog.*` -> add prolog as message
  - `net.sunomc.rpg.game....` -> path zeiger / nav mit path finder for dungeons etc

- structure:
  - `net.sunomc.rpg.core.handler.CommandHandler` -> better command system ew custom logik for player and perms 

- code:
  - `net.sunomc.rpg.core.common.SunoPlayer` -> add lang to player

- docs:
  - `net.sunomc.rpg.core.handler.SqlHandler` -> docs für alles
  - `net.sunomc.rpg.core.handler.PacketHandler` -> docs für alles
  - `net.sunomc.rpg.core.events.*` -> docs für events and usage
  - `net.sunomc.rpg.core.common.SunoPlayer:42,382` -> docs erweitern

- messages:
  - `net.sunomc.rpg.core.commands.MsgCommand:29` übersetzung

- errors:
  - [08:52:42 INFO]: [RPG] Disabling SunoRpgCore v1.0.1
    [08:52:42 INFO]: [RPG] Loading server plugin SunoRpgCore v1.0.1
    [08:52:42 INFO]: [RPG] Enabling SunoRpgCore v1.0.1
    [08:52:43 INFO]: [RPG] Database connection established
    [08:52:43 INFO]: [PlugMan] SunoRpgCore has been reloaded.
    [08:52:46 INFO]: UUID of player LeyCM is 766fa3dd-7a1a-35f0-95f5-24f3ae20626b
    [08:52:46 INFO]: LeyCM joined the game
    [08:52:46 INFO]: LeyCM[/127.0.0.1:53946] logged in with entity id 1063 at ([world]119.30000001192093, 69.0, -38.57506391022114)
    [08:53:51 INFO]: LeyCM lost connection: Disconnected
    [08:53:51 ERROR]: Could not pass event PlayerQuitEvent to SunoRpgCore v1.0.1
    com.google.gson.JsonIOException: Failed making field 'java.time.LocalDateTime#date' accessible; either increase its visibility or write a custom TypeAdapter for its declaring type.
    at com.google.gson.internal.reflect.ReflectionHelper.makeAccessible(ReflectionHelper.java:38) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getBoundFields(ReflectiveTypeAdapterFactory.java:286) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.create(ReflectiveTypeAdapterFactory.java:130) ~[gson-2.10.1.jar:?]
    at com.google.gson.Gson.getAdapter(Gson.java:556) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper.write(TypeAdapterRuntimeTypeWrapper.java:55) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:207) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:144) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper.write(TypeAdapterRuntimeTypeWrapper.java:70) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:207) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:144) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper.write(TypeAdapterRuntimeTypeWrapper.java:70) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:207) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:144) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper.write(TypeAdapterRuntimeTypeWrapper.java:70) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:207) ~[gson-2.10.1.jar:?]
    at com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter.write(MapTypeAdapterFactory.java:144) ~[gson-2.10.1.jar:?]
    at com.google.gson.Gson.toJson(Gson.java:842) ~[gson-2.10.1.jar:?]
    at com.google.gson.Gson.toJson(Gson.java:812) ~[gson-2.10.1.jar:?]
    at com.google.gson.Gson.toJson(Gson.java:759) ~[gson-2.10.1.jar:?]
    at com.google.gson.Gson.toJson(Gson.java:736) ~[gson-2.10.1.jar:?]
    at SunoRPGCore-1.0.1-1747119157559.jar/net.sunomc.rpg.core.data.Data.to(Data.java:122) ~[SunoRPGCore-1.0.1-1747119157559.jar:?]
    at SunoRPGCore-1.0.1-1747119157559.jar/net.sunomc.rpg.core.common.SunoPlayer.save(SunoPlayer.java:121) ~[SunoRPGCore-1.0.1-1747119157559.jar:?]
    at SunoRPGCore-1.0.1-1747119157559.jar/net.sunomc.rpg.SunoMC.removePlayer(SunoMC.java:55) ~[SunoRPGCore-1.0.1-1747119157559.jar:?]
    at SunoRPGCore-1.0.1-1747119157559.jar/net.sunomc.rpg.core.listener.PlayerListener.onPlayerQuit(PlayerListener.java:41) ~[SunoRPGCore-1.0.1-1747119157559.jar:?]
    at com.destroystokyo.paper.event.executor.asm.generated.GeneratedEventExecutor49.execute(Unknown Source) ~[?:?]
    at org.bukkit.plugin.EventExecutor$2.execute(EventExecutor.java:77) ~[paper-api-1.21.3-R0.1-SNAPSHOT.jar:?]
    at co.aikar.timings.TimedEventExecutor.execute(TimedEventExecutor.java:80) ~[paper-api-1.21.3-R0.1-SNAPSHOT.jar:?]
    at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:70) ~[paper-api-1.21.3-R0.1-SNAPSHOT.jar:?]
    at io.papermc.paper.plugin.manager.PaperEventManager.callEvent(PaperEventManager.java:54) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at io.papermc.paper.plugin.manager.PaperPluginManagerImpl.callEvent(PaperPluginManagerImpl.java:131) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:628) ~[paper-api-1.21.3-R0.1-SNAPSHOT.jar:?]
    at net.minecraft.server.players.PlayerList.remove(PlayerList.java:554) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.players.PlayerList.remove(PlayerList.java:539) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.network.ServerGamePacketListenerImpl.removePlayerFromWorld(ServerGamePacketListenerImpl.java:2130) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.network.ServerGamePacketListenerImpl.onDisconnect(ServerGamePacketListenerImpl.java:2110) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.network.ServerGamePacketListenerImpl.onDisconnect(ServerGamePacketListenerImpl.java:2097) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.network.Connection.handleDisconnection(Connection.java:910) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.network.ServerConnectionListener.tick(ServerConnectionListener.java:268) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.MinecraftServer.tickConnection(MinecraftServer.java:1917) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.dedicated.DedicatedServer.tickConnection(DedicatedServer.java:453) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.MinecraftServer.tickChildren(MinecraftServer.java:1890) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.MinecraftServer.tickServer(MinecraftServer.java:1664) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.MinecraftServer.runServer(MinecraftServer.java:1329) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:340) ~[paper-1.21.3.jar:1.21.3-74-47f2071]
    at java.base/java.lang.Thread.run(Thread.java:1583) ~[?:?]
    Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make field private final java.time.LocalDate java.time.LocalDateTime.date accessible: module java.base does not "opens java.time" to unnamed module @5260ca9
    at java.base/java.lang.reflect.AccessibleObject.throwInaccessibleObjectException(AccessibleObject.java:391) ~[?:?]
    at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:367) ~[?:?]
    at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:315) ~[?:?]
    at java.base/java.lang.reflect.Field.checkCanSetAccessible(Field.java:183) ~[?:?]
    at java.base/java.lang.reflect.Field.setAccessible(Field.java:177) ~[?:?]
    at com.google.gson.internal.reflect.ReflectionHelper.makeAccessible(ReflectionHelper.java:35) ~[gson-2.10.1.jar:?]
    ... 44 more
    [08:53:51 INFO]: LeyCM left the game