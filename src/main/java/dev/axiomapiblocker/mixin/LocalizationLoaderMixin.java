package dev.axiomapiblocker.mixin;

import com.moulberry.axiom.i18n.LocalizationLoader;
import dev.axiomapiblocker.ChatLogger;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalizationLoader.class)
public class LocalizationLoaderMixin {
    private static final String AXIOM_API_HOST = "axiom.moulberry.com";

    @Redirect(
        method = {"fetchUpdateCount", "lambda$languageChanged$0"},
        at = @At(value = "INVOKE", target = "Ljava/net/URL;openConnection()Ljava/net/URLConnection;")
    )
    private static URLConnection axiomOutageBlockLocalization(URL url) throws IOException {
        if (AXIOM_API_HOST.equalsIgnoreCase(url.getHost())) {
            int blocked = ChatLogger.registerBlock();
            ChatLogger.warn("AXIOM попытался загрузить переводы с API и был заблокирован: " + url + " (всего заблокировано: " + blocked + ")");
            throw new IOException("Axiom API blocked by axiomapiblocker: " + url);
        }
        return url.openConnection();
    }
}