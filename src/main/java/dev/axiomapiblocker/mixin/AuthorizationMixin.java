package dev.axiomapiblocker.mixin;

import dev.axiomapiblocker.ChatLogger;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = {"com.moulberry.axiom.utils.Authorization", "com.moulberry.axi\u03BFm.utils.Authorization"})
public class AuthorizationMixin {
    private static final String AXIOM_API_HOST = "axiom.moulberry.com";

    @Redirect(
        method = {"lambda$getMeta$0", "lambda$checkCommercial$1", "lambda$checkServer$2"},
        at = @At(value = "INVOKE", target = "Ljava/net/URL;openConnection()Ljava/net/URLConnection;")
    )
    private static URLConnection axiomOutageBlockApi(URL url) throws IOException {
        if (AXIOM_API_HOST.equalsIgnoreCase(url.getHost())) {
            int blocked = ChatLogger.registerBlock();
            ChatLogger.warn("AXIOM попытался подключиться к API и был заблокирован: " + url + " (всего заблокировано: " + blocked + ")");
            throw new IOException("Axiom API blocked by axiomapiblocker: " + url);
        }
        return url.openConnection();
    }
}