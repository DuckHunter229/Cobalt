package org.cobalt.api.event.impl.client

import org.cobalt.api.event.Event

abstract class TickEvent: Event() {
  class Start(): TickEvent()
  class End(): TickEvent()
}
