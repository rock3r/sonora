package dev.sebastiano.sonora.validation

import dev.sebastiano.sonora.model.RssFeed

/**
 * Validator for [RssFeed] that checks if an instance contains valid values. Returns either a set of
 * validation messages, or the validated feed with validation messages.
 */
@Suppress("TooManyFunctions")
object RssFeedValidator {

    /**
     * Validates the given [RssFeed] instance.
     *
     * @param feed The feed to validate.
     * @return A [RssFeedValidationResult] containing either the validated feed with warnings, or a
     *   set of validation messages.
     */
    fun validate(feed: RssFeed): RssFeedValidationResult {
        val messages = mutableSetOf<ValidationMessage>()

        // Validate RssFeed
        messages.addAll(validateFeedVersion(feed))

        // Validate Channel
        messages.addAll(RssChannelValidator.validateChannel(feed.channel))

        // Validate items
        feed.channel.items?.forEachIndexed { index, item ->
            messages.addAll(RssItemValidator.validateItem(item, index))
        }
            ?: messages.add(
                ValidationMessage(
                    ErrorMessages.CHANNEL_NO_ITEMS,
                    "Channel has no items",
                    "channel.items",
                    ValidationSeverity.WARNING,
                )
            )

        // Return the validation result
        val hasErrors = messages.any { it.severity == ValidationSeverity.ERROR }
        return if (!hasErrors) {
            RssFeedValidationResult.Success(feed, messages)
        } else {
            RssFeedValidationResult.Failure(messages)
        }
    }

    private fun validateFeedVersion(feed: RssFeed): MutableSet<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()
        if (feed.version.isBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.RSS_VERSION_REQUIRED,
                    "RSS version is required",
                    "feed.version",
                    ValidationSeverity.ERROR,
                )
            )
        } else if (feed.version != "2.0") {
            messages.add(
                ValidationMessage(
                    ErrorMessages.RSS_VERSION_WRONG,
                    "RSS version is supposed to be '2.0', but it's '${feed.version}'",
                    "feed.version",
                    ValidationSeverity.WARNING,
                )
            )
        }
        return messages
    }
}
