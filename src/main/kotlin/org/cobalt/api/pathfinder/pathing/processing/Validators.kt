package org.cobalt.api.pathfinder.pathing.processing

import org.cobalt.api.pathfinder.pathing.processing.context.EvaluationContext
import org.cobalt.api.pathfinder.pathing.processing.context.SearchContext

object Validators {

  object Validators {

    fun allOf(vararg validators: ValidationProcessor): ValidationProcessor =
      AllOfValidator(*validators)

    fun allOf(validators: List<ValidationProcessor>): ValidationProcessor =
      AllOfValidator(validators)

    fun anyOf(vararg validators: ValidationProcessor): ValidationProcessor =
      AnyOfValidator(*validators)

    fun anyOf(validators: List<ValidationProcessor>): ValidationProcessor =
      AnyOfValidator(validators)

    fun noneOf(vararg validators: ValidationProcessor): ValidationProcessor =
      NoneOfValidator(*validators)

    fun noneOf(validators: List<ValidationProcessor>): ValidationProcessor =
      NoneOfValidator(validators)

    fun not(validator: ValidationProcessor): ValidationProcessor = NotValidator(validator)

    fun alwaysTrue(): ValidationProcessor = AlwaysTrueValidator

    fun alwaysFalse(): ValidationProcessor = AlwaysFalseValidator

    private fun copyAndFilterNulls(
      vararg validators: ValidationProcessor?,
    ): List<ValidationProcessor> = validators.filterNotNull()

    private fun copyAndFilterNulls(
      validators: List<ValidationProcessor?>?,
    ): List<ValidationProcessor> = validators?.filterNotNull() ?: emptyList()

    private abstract class AbstractCompositeValidator(validators: List<ValidationProcessor?>) :
      ValidationProcessor {
      protected val children: List<ValidationProcessor> = validators.filterNotNull()

      override fun initializeSearch(context: SearchContext) =
        children.forEach { it.initializeSearch(context) }

      override fun finalizeSearch(context: SearchContext) =
        children.forEach { it.finalizeSearch(context) }
    }

    private class AllOfValidator : AbstractCompositeValidator {
      constructor(vararg validators: ValidationProcessor?) : super(validators.toList())
      constructor(validators: List<ValidationProcessor?>?) : super(validators ?: emptyList())

      override fun isValid(context: EvaluationContext): Boolean =
        children.all { it.isValid(context) }
    }

    private class AnyOfValidator : AbstractCompositeValidator {
      constructor(vararg validators: ValidationProcessor?) : super(validators.toList())
      constructor(validators: List<ValidationProcessor?>?) : super(validators ?: emptyList())

      override fun isValid(context: EvaluationContext): Boolean {
        if (children.isEmpty()) return false
        return children.any { it.isValid(context) }
      }
    }

    private class NoneOfValidator : AbstractCompositeValidator {
      constructor(vararg validators: ValidationProcessor?) : super(validators.toList())
      constructor(validators: List<ValidationProcessor?>?) : super(validators ?: emptyList())

      override fun isValid(context: EvaluationContext): Boolean =
        children.none { it.isValid(context) }
    }

    private class NotValidator(private val child: ValidationProcessor) : ValidationProcessor {
      override fun initializeSearch(context: SearchContext) = child.initializeSearch(context)

      override fun isValid(context: EvaluationContext): Boolean = !child.isValid(context)

      override fun finalizeSearch(context: SearchContext) = child.finalizeSearch(context)
    }

    private object AlwaysTrueValidator : ValidationProcessor {
      override fun isValid(context: EvaluationContext): Boolean = true
    }

    private object AlwaysFalseValidator : ValidationProcessor {
      override fun isValid(context: EvaluationContext): Boolean = false
    }
  }
}
