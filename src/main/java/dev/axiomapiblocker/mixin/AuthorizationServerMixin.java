package dev.axiomapiblocker.mixin;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"com.moulberry.axiom.utils.Authorization", "com.moulberry.axi\u03BFm.utils.Authorization"})
public class AuthorizationServerMixin {
    private static final Class<?> serverAuthorizationClass = findServerAuthorizationClass();

    private static Class<?> findServerAuthorizationClass() {
        try {
            return Class.forName("com.moulberry.axi\u03BFm.utils.Authorization$ServerAuthorization");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Object yesValue() {
        try {
            Method valueOf = serverAuthorizationClass.getMethod("valueOf", String.class);
            return valueOf.invoke(null, "YES");
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    @Inject(method = "checkServer", at = @At("HEAD"), cancellable = true)
    private static void axiomForceServerAllowed(String server, String host, UUID uuid, CallbackInfoReturnable<CompletableFuture<?>> cir) {
        Object yes = yesValue();
        if (yes != null) {
            cir.setReturnValue(CompletableFuture.completedFuture(yes));
        }
    }
}
