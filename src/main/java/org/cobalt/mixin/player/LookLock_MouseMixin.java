package org.cobalt.mixin.player;

import net.minecraft.client.Mouse;
import org.cobalt.api.util.MouseUtils;
import org.cobalt.api.util.player.MovementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mouse.class)
public abstract class LookLock_MouseMixin {

  @Shadow
  private boolean cursorLocked;

  @Shadow
  public abstract void unlockCursor();

  @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
  private void onUpdateMouse(CallbackInfo ci) {
    if (MovementManager.isLookLocked) {
      ci.cancel();
    }
  }

  // might as well fit in ungrab mouse here as well
  @Inject(method = "isCursorLocked", at = @At("HEAD"), cancellable = true)
  private void onIsCursorLocked(CallbackInfoReturnable<Boolean> cir) {
    if (MouseUtils.isMouseUngrabbed()) {
      if (this.cursorLocked) {
        this.unlockCursor();
      }

      cir.setReturnValue(false);
    }
  }

  @Inject(method = "lockCursor", at = @At("HEAD"), cancellable = true)
  private void onLockCursor(CallbackInfo ci) {
    if (MouseUtils.isMouseUngrabbed()) {
      ci.cancel();
    }
  }

}
