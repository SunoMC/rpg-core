package net.sunomc.rpg.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BodyYawUtil {
    private static MethodHandle getHandle;
    private static Field bodyYawField;

    static {
        try {
            // CraftPlayer-Klasse
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");

            // getHandle()
            Method getHandleMethod = craftPlayerClass.getMethod("getHandle");
            getHandle = MethodHandles.lookup().unreflect(getHandleMethod);

            // bodyYaw-Feld from EntityPlayer
            Object dummyCraftPlayer = craftPlayerClass.cast(Bukkit.getOnlinePlayers().stream().findFirst().orElse(null));
            Object handle = getHandle.invoke(dummyCraftPlayer);
            Class<?> entityPlayerClass = handle.getClass();

            Field field = entityPlayerClass.getDeclaredField("bodyYaw");
            field.setAccessible(true);
            bodyYawField = field;

        } catch (Throwable t) {
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
        }
    }

    public static float getBodyYaw(Player player) {
        try {
            Object handle = getHandle.invoke(player);
            return bodyYawField.getFloat(handle);
        } catch (Throwable t) {
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
            return 0;
        }
    }
}
