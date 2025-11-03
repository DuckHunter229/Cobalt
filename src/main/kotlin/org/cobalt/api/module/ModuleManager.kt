package org.cobalt.api.module

internal object ModuleManager {

  private val modules = mutableListOf<Module>()

  fun addModule(module: Module) {
    modules.add(module)
  }

  fun getModules(): List<Module> {
    return modules
  }

}
