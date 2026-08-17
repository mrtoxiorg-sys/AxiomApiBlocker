package dev.axiomapiblocker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"com.moulberry.axiom.utils.Authorization", "com.moulberry.axi\u03BFm.utils.Authorization"})
public class AuthorizationLicenseMixin {
    @Inject(method = "hasCommercialLicense", at = @At("HEAD"), cancellable = true)
    private static void axiomForceCommercialLicense(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
