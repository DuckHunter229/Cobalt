package org.cobalt.internal.ui.screen

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.cobalt.Cobalt.mc
import org.cobalt.api.event.EventBus
import org.cobalt.api.event.annotation.SubscribeEvent
import org.cobalt.api.event.impl.render.NvgEvent
import org.cobalt.api.util.TickScheduler
import org.cobalt.api.util.ui.NVGRenderer
import org.cobalt.internal.util.Config

internal object ConfigScreen : Screen(Text.empty()) {

  init {
    EventBus.register(this)
  }

  @SubscribeEvent
  fun onRender(event: NvgEvent) {
    if (mc.currentScreen != this)
      return

    val width = mc.window.width.toFloat()
    val height = mc.window.height.toFloat()

    NVGRenderer.beginFrame(width, height)

    NVGRenderer.endFrame()
  }

  override fun close() {
    Config.saveModulesConfig()
    super.close()
  }

  fun openUI() {
    TickScheduler.schedule(1) {
      mc.setScreen(this)
    }
  }

}
