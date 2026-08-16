package dev.axiomapiblocker;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class ChatLogger {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final AtomicInteger BLOCKED = new AtomicInteger();

    private ChatLogger() {
    }

    public static int blockedCount() {
        return BLOCKED.get();
    }

    public static int registerBlock() {
        return BLOCKED.incrementAndGet();
    }

    public static void log(String message) {
        send("LOG", message);
    }

    public static void warn(String message) {
        send("WARN", message);
    }

    public static void error(String message) {
        send("ERROR", message);
    }

    private static void send(String level, String message) {
        class_310 client = class_310.method_1551();
        client.execute(() -> {
            class_746 player = client.field_1724;
            if (player != null) {
                player.method_7353(class_2561.method_43470("[" + TIME.format(LocalTime.now()) + "] " + level + ": " + message), false);
            }
        });
    }
}