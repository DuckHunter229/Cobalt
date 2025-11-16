package org.cobalt.api.addon

abstract class Addon {

  abstract fun onLoad()
  abstract fun onUnload()

}
