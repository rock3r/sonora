package dev.sebastiano.sonora.validation

import dev.sebastiano.sonora.model.RssFeed

/** Represents the result of validating an [RssFeed]. */
sealed interface RssFeedValidationResult {

    /**
     * Represents a successful validation result with optional validation messages.
     *
     * @property feed The validated feed.
     * @property messages Set of validation messages that don't prevent the
     *    feed from being valid (all warnings).
     */
    data class Success(
        val feed: RssFeed,
        val messages: Set<ValidationMessage> = emptySet(),
    ) : RssFeedValidationResult {
        // Helper property to get only warnings for backward compatibility
        val warnings: Set<ValidationMessage>
            get() = messages.filter { it.severity == ValidationSeverity.WARNING }.toSet()
    }

    /**
     * Represents a failed validation result with validation messages.
     *
     * @property messages Set of validation messages that include both errors and warnings.
     */
    data class Failure(
        val messages: Set<ValidationMessage>,
    ) : RssFeedValidationResult {
        // Helper properties to get errors and warnings separately for backward compatibility
        val errors: Set<ValidationMessage>
            get() = messages.filter { it.severity == ValidationSeverity.ERROR }.toSet()

        val warnings: Set<ValidationMessage>
            get() = messages.filter { it.severity == ValidationSeverity.WARNING }.toSet()
    }
}

/**
 * Severity level for validation messages.
 */
enum class ValidationSeverity {
    ERROR,
    WARNING,
    INFO
}

/**
 * Represents a validation message with a severity level.
 *
 * @property id The unique identifier for this type of validation message.
 * @property message The validation message.
 * @property path The path to the field that caused the validation message.
 * @property severity The severity level of the validation message.
 */
data class ValidationMessage(
    val id: ErrorMessages,
    val message: String,
    val path: String,
    val severity: ValidationSeverity,
) {
    companion object {
        /**
         * Factory function for creating a ValidationMessage with a default ID.
         * Uses [ErrorMessages.FIELD_IS_BLANK] as a default ID.
         *
         * @param message The validation message.
         * @param path The path to the field that caused the validation message.
         * @param severity The severity level of the validation message.
         * @return A new ValidationMessage instance with the default ID.
         */
        @Deprecated("Use explicit IDs, you lazy fuck")
        fun create(
            message: String,
            path: String,
            severity: ValidationSeverity
        ): ValidationMessage = ValidationMessage(ErrorMessages.FIELD_IS_BLANK, message, path, severity)
    }
}
