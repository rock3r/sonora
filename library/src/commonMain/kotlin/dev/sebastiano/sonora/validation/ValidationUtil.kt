package dev.sebastiano.sonora.validation

import org.jsoup.Jsoup

/**
 * Validates that a string is not blank if it is not null.
 *
 * @param value The string value to validate.
 * @param path The path to the field being validated.
 * @param fieldName The name of the field being validated.
 * @param messages The set of validation messages to add to.
 */
internal fun validateNotBlank(
    value: String?,
    path: String,
    fieldName: String,
    messages: MutableSet<ValidationMessage>,
    severity: ValidationSeverity = ValidationSeverity.WARNING,
) {
    if (value != null && value.isBlank()) {
        messages.add(ValidationMessage.create("$fieldName is blank", path, severity))
    }
}

internal fun validateBoolean(value: String?, path: String, fieldName: String): ValidationMessage? {
    if (value == null) return null
    return when (value.lowercase()) {
        "true",
        "false" -> null
        else ->
            ValidationMessage(
                ErrorMessages.APPLE_BOOLEANS,
                "$fieldName must be 'true' or 'false'",
                path,
                ValidationSeverity.ERROR,
            )
    }
}

internal fun validateWeirdAppleYesThing(
    value: String?,
    path: String,
    fieldName: String,
): ValidationMessage? =
    when (value) {
        null -> null
        "Yes" -> null
        "yes" ->
            ValidationMessage(
                ErrorMessages.APPLE_YES_CASING,
                "$fieldName's value is 'yes', but it should be 'Yes'",
                path,
                ValidationSeverity.WARNING,
            )

        else ->
            ValidationMessage(
                ErrorMessages.APPLE_YES_IS_THE_ONLY_ALLOWED_VALUE,
                "$fieldName's value must be 'Yes' or be removed",
                path,
                ValidationSeverity.ERROR,
            )
    }

/**
 * Compares the plain text content of description and encodedDescription. Emits a warning if they
 * don't match.
 *
 * @param description The plain text description.
 * @param encodedDescription The HTML encoded description.
 * @param path The path to the field being validated.
 * @param messages The set of validation messages to add to.
 */
internal fun validateDescriptionMatch(
    description: String?,
    encodedDescription: String?,
    path: String,
    messages: MutableSet<ValidationMessage>,
) {
    if (description != null && encodedDescription != null) {
        val plainTextFromEncoded = extractPlainTextFromHtml(encodedDescription)
        if (description != plainTextFromEncoded) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.DESCRIPTION_MISMATCH,
                    "Description and encoded description (HTML) don't match when comparing plain text",
                    path,
                    ValidationSeverity.WARNING,
                )
            )
        }
    }
}

/**
 * Extracts plain text from HTML content using JSoup.
 *
 * @param html The HTML content to extract plain text from.
 * @return The plain text extracted from the HTML content.
 */
private fun extractPlainTextFromHtml(html: String?): String? {
    if (html == null) return null
    return Jsoup.parse(html).text()
}
