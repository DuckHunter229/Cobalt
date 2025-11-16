package org.cobalt.api.module

import org.cobalt.api.module.setting.Setting

abstract class Module(
  val name: String,
  val category: Category,
) {

  var isEnabled: Boolean = false
    set(value) {
      field = value

      if (value)
        onEnable()
      else
        onDisable()
    }

  private val settingsList = mutableListOf<Setting<*>>()

  abstract fun onEnable()
  abstract fun onDisable()

  fun addSetting(vararg settings: Setting<*>) {
    settingsList.addAll(listOf(*settings))
  }

  fun getSettings(): List<Setting<*>> {
    return settingsList
  }

}

data class Category(
  val name: String,
  val svg: String,
)
