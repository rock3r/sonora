package dev.sebastiano.sonora.validation

import dev.sebastiano.sonora.model.Channel
import dev.sebastiano.sonora.model.Item
import dev.sebastiano.sonora.model.PodcastChapters
import dev.sebastiano.sonora.model.PodcastFunding
import dev.sebastiano.sonora.model.PodcastLocked
import dev.sebastiano.sonora.model.PodcastSoundbite
import dev.sebastiano.sonora.model.PodcastTranscript
import dev.sebastiano.sonora.model.RssFeed
import dev.sebastiano.sonora.model.Taxonomy
import dev.sebastiano.sonora.model.ValidItunesCategory
import org.jsoup.Jsoup
import java.util.Locale

/**
 * Validator for [RssFeed] that checks if an instance contains valid
 * values. Returns either a set of validation messages, or the validated
 * feed with validation messages.
 */
@Suppress("TooManyFunctions") object RssFeedValidator {

    /**
     * Validates the given [RssFeed] instance.
     *
     * @param feed The feed to validate.
     * @return A [RssFeedValidationResult] containing either the validated feed
     *    with warnings, or a set of validation messages.
     */
    fun validate(feed: RssFeed): RssFeedValidationResult {
        val messages = mutableSetOf<ValidationMessage>()

        // Validate RssFeed
        messages.addAll(validateFeedVersion(feed))

        // Validate Channel
        messages.addAll(validateChannel(feed.channel))

        // Return the validation result
        val hasErrors = messages.any { it.severity == ValidationSeverity.ERROR }
        return if (!hasErrors) {
            RssFeedValidationResult.Success(feed, messages)
        } else {
            RssFeedValidationResult.Failure(messages)
        }
    }

    private fun validateFeedVersion(
        feed: RssFeed,
    ): MutableSet<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()
        if (feed.version.isBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.RSS_VERSION_REQUIRED,
                    "RSS version is required",
                    "feed.version",
                    ValidationSeverity.ERROR
                )
            )
        } else if (feed.version != "2.0") {
            messages.add(
                ValidationMessage(
                    ErrorMessages.RSS_VERSION_WRONG,
                    "RSS version is supposed to be '2.0', but it's '${feed.version}'",
                    "feed.version",
                    ValidationSeverity.WARNING
                )
            )
        }
        return messages
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

    /**
     * Compares the plain text content of description and encodedDescription.
     * Emits a warning if they don't match.
     *
     * @param description The plain text description.
     * @param encodedDescription The HTML encoded description.
     * @param path The path to the field being validated.
     * @param messages The set of validation messages to add to.
     */
    private fun validateDescriptionMatch(
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
                        ValidationSeverity.WARNING
                    )
                )
            }
        }
    }

    /**
     * Validates that a string is not blank if it is not null.
     *
     * @param value The string value to validate.
     * @param path The path to the field being validated.
     * @param fieldName The name of the field being validated.
     * @param messages The set of validation messages to add to.
     */
    private fun validateNotBlank(
        value: String?,
        path: String,
        fieldName: String,
        messages: MutableSet<ValidationMessage>,
        severity: ValidationSeverity = ValidationSeverity.WARNING,
    ) {
        if (value != null && value.isBlank()) {
            messages.add(
                ValidationMessage.create(
                    "$fieldName is blank", path, severity
                )
            )
        }
    }

    private val isoCountries = Locale.getISOCountries().map { it.lowercase() }

    private fun validateChannel(channel: Channel): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (channel.atomLink.isEmpty()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ATOM_LINK_REQUIRED,
                    "At least the 'self' atom:link is required",
                    "channel.atomLink",
                    ValidationSeverity.ERROR
                )
            )
        } else {
            val selfLinks = channel.atomLink.count { it.rel == "self" && it.type == "application/rss+xml" }
            if (selfLinks == 0) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.ATOM_LINK_MISSING_SELF_LINK,
                        "Must have exactly one atom:link with rel=\"self\" and type=\"application/rss+xml\"",
                        "channel.atomLink",
                        ValidationSeverity.ERROR
                    )
                )
            } else if (selfLinks > 1) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.ATOM_LINK_MULTIPLE_SELF_LINKS,
                        "Must have only one atom:link with rel=\"self\" and type=\"application/rss+xml\"",
                        "channel.atomLink",
                        ValidationSeverity.ERROR
                    )
                )
            }

            channel.atomLink.forEachIndexed { index, link ->
                if (link.href.isBlank()) {
                    messages.add(
                        ValidationMessage(
                            ErrorMessages.FIELD_IS_BLANK,
                            "atom:link href cannot be blank",
                            "channel.atomLink[$index].href",
                            ValidationSeverity.ERROR
                        )
                    )
                }
            }
        }

        channel.ttl?.let { ttl ->
            if (ttl <= 0) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.TTL_MUST_BE_POSITIVE,
                        "TTL must be greater than 0",
                        "channel.ttl",
                        ValidationSeverity.ERROR
                    )
                )
            }
        }

        channel.categories?.let { categories ->
            categories.forEachIndexed { index, category ->
                if (category.name.isNullOrBlank()) {
                    messages.add(
                        ValidationMessage(
                            ErrorMessages.FIELD_IS_BLANK,
                            "Channel category cannot be blank",
                            "channel.categories[$index]",
                            ValidationSeverity.WARNING
                        )
                    )
                }
            }

            categories.mapIndexed { index, category -> category to index }
                .groupBy({ it.first }, { it.second })
                .filter { it.value.size > 1 }
                .onEach { (category, indices) ->
                    messages.add(
                        ValidationMessage(
                            ErrorMessages.FIELD_IS_BLANK,
                            "Channel category '$category' is duplicated",
                            "channel.categories[${indices.joinToString(",")}]",
                            ValidationSeverity.WARNING
                        )
                    )
                }
        }

        if (channel.itunesOwner == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.FIELD_IS_BLANK,
                    "iTunes owner is required",
                    "channel.itunesOwner",
                    ValidationSeverity.ERROR
                )
            )
        } else {
            if (channel.itunesOwner.name.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "iTunes owner name cannot be blank",
                        "channel.itunesOwner.name",
                        ValidationSeverity.ERROR
                    )
                )
            }
            if (channel.itunesOwner.email.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "iTunes owner email cannot be blank",
                        "channel.itunesOwner.email",
                        ValidationSeverity.ERROR
                    )
                )
            }
        }

        if (channel.itunesImage == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITUNES_IMAGE_REQUIRED,
                    "iTunes image is required",
                    "channel.itunesImage",
                    ValidationSeverity.ERROR
                )
            )
        } else {
            if (channel.itunesImage.href.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "iTunes image href cannot be blank",
                        "channel.itunesImage.href",
                        ValidationSeverity.ERROR
                    )
                )
            }
        }

        if (channel.itunesCategories.isNullOrEmpty()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITUNES_CATEGORIES_REQUIRED,
                    "At least one iTunes category is required",
                    "channel.itunesCategories",
                    ValidationSeverity.ERROR
                )
            )
        } else {
            channel.itunesCategories.forEachIndexed { index, category ->
                if (ValidItunesCategory.asValidItunesCategory(category) == null) {
                    messages.add(
                        ValidationMessage(
                            ErrorMessages.INVALID_ITUNES_CATEGORY,
                            "Invalid iTunes category: $category",
                            "channel.itunesCategories[$index]",
                            ValidationSeverity.ERROR
                        )
                    )
                }
            }

            if (channel.itunesCategories.size > 1) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Multiple iTunes categories found. Only the first one will be used.",
                        "channel.itunesCategories",
                        ValidationSeverity.INFO
                    )
                )
            }
        }

        channel.mediaRestrictions?.let { restrictions ->
            if (restrictions.type != "country") {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Media restriction type must be 'country'",
                        "channel.mediaRestrictions.type",
                        ValidationSeverity.WARNING
                    )
                )
            }
            if (restrictions.country != "allow") {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Media restriction country must be 'allow'",
                        "channel.mediaRestrictions.relationship",
                        ValidationSeverity.WARNING
                    )
                )
            }
            restrictions.countries?.split(" ")
                ?.map { it.lowercase() }
                ?.forEach { country ->
                    if (country !in isoCountries) {
                        messages.add(
                            ValidationMessage(
                                ErrorMessages.FIELD_IS_BLANK,
                                "Invalid ISO 3166 country code: $country",
                                "channel.mediaRestrictions.countries",
                                ValidationSeverity.WARNING
                            )
                        )
                    }
                }
        }

        channel.spotifyLimit?.let { limit ->
            if (limit.recentCount == null) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.SPOTIFY_LIMIT_MISSING_COUNT,
                        "Spotify limit must be greater than 0",
                        "channel.spotifyLimit",
                        ValidationSeverity.WARNING
                    )
                )
            } else if (limit.recentCount <= 0) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.SPOTIFY_LIMIT_INVALID_COUNT,
                        "Spotify limit must be greater than 0",
                        "channel.spotifyLimit",
                        ValidationSeverity.WARNING
                    )
                )
            }
        }

        channel.spotifyCountryOfOrigin?.let { origin ->
            if (origin.countries.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Spotify country of origin countries cannot be blank",
                        "channel.spotifyCountryOfOrigin.countries",
                        ValidationSeverity.WARNING
                    )
                )
            } else {
                origin.countries.split(" ").forEach { country ->
                    if (country !in isoCountries) {
                        messages.add(
                            ValidationMessage(
                                ErrorMessages.FIELD_IS_BLANK,
                                "Invalid ISO 3166 country code: $country",
                                "channel.spotifyCountryOfOrigin.countries",
                                ValidationSeverity.WARNING
                            )
                        )
                    }
                }
            }
        }

        channel.podcastPodroll?.let { podroll ->
            if (podroll.remoteItems.isEmpty()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Podroll cannot be empty",
                        "channel.podroll.remoteItems",
                        ValidationSeverity.WARNING
                    )
                )
            } else {
                podroll.remoteItems.forEachIndexed { index, item ->
                    validateNotBlank(item.feedGuid, "channel.podroll[$index].feedGuid", "Podroll feedGuid", messages)
                    validateNotBlank(
                        item.feedUrl, "channel.podroll[$index].feedUrl", "Podroll feedUrl", messages,
                        ValidationSeverity.INFO
                    )
                    validateNotBlank(
                        item.itemGuid, "channel.podroll[$index].itemGuid", "Podroll itemGuid", messages,
                        ValidationSeverity.INFO
                    )
                    validateNotBlank(
                        item.medium, "channel.podroll[$index].medium", "Podroll medium", messages,
                        ValidationSeverity.INFO
                    )
                    validateNotBlank(
                        item.title,
                        "channel.podroll[$index].title",
                        "Podroll title",
                        messages,
                        ValidationSeverity.INFO
                    )

                }
            }
        }

        channel.podcastPersons.forEachIndexed { index, person ->
            validateNotBlank(person.name, "channel.podcastPersons[$index].name", "Person name", messages)
            if (person.name != null && person.name.length > 128) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Person name exceeds recommended maximum length of 128 characters",
                        "channel.podcastPersons[$index].name",
                        ValidationSeverity.WARNING
                    )
                )
            }

            if (person.role.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Role not specified, assuming 'host'",
                        "channel.podcastPersons[$index].role",
                        ValidationSeverity.INFO
                    )
                )
            } else {
                if (person.role.lowercase() !in Taxonomy.roles) {
                    messages.add(
                        ValidationMessage(
                            ErrorMessages.FIELD_IS_BLANK,
                            "Invalid person role value '${person.role}'. Must be one of taxonomy items.",
                            "channel.podcastPersons[$index].role",
                            ValidationSeverity.WARNING
                        )
                    )
                }
            }

            if (person.group.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.FIELD_IS_BLANK,
                        "Group not specified, assuming 'cast'",
                        "channel.podcastPersons[$index].group",
                        ValidationSeverity.INFO
                    )
                )
            } else {
                if (person.group.lowercase() !in Taxonomy.groups) {
                    messages.add(
                        ValidationMessage(
                            ErrorMessages.FIELD_IS_BLANK,
                            "Invalid person group value '${person.group}'. Must be one of: taxonomy items.",
                            "channel.podcastPersons[$index].group",
                            ValidationSeverity.WARNING
                        )
                    )
                }
            }
        }

        channel.podcastLocation?.let { location ->
            validateNotBlank(location.name, "channel.podcastLocation.name", "Location name", messages)
            validateNotBlank(location.geo, "channel.podcastLocation.geo", "Location geo", messages)
        }

        if (channel.title.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.CHANNEL_TITLE_REQUIRED,
                    "Channel title is required",
                    "channel.title",
                    ValidationSeverity.ERROR
                )
            )
        }

        if (channel.description.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.CHANNEL_DESCRIPTION_REQUIRED,
                    "Channel description is required",
                    "channel.description",
                    ValidationSeverity.ERROR
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(channel.pubDate, "channel.pubDate", "pubDate", messages)
        validateNotBlank(channel.lastBuildDate, "channel.lastBuildDate", "lastBuildDate", messages)
        validateNotBlank(channel.copyright, "channel.copyright", "Copyright", messages)
        validateNotBlank(channel.webMaster, "channel.webMaster", "Webmaster", messages)
        validateNotBlank(channel.generator, "channel.generator", "Generator", messages)
        messages.addMaybe(validateBoolean(channel.itunesExplicit, "channel.itunesExplicit", "iTunes explicit"))
        messages.addMaybe(validateWeirdAppleYesThing(channel.itunesBlock, "channel.itunesBlock", "iTunes block"))
        messages.addMaybe(
            validateWeirdAppleYesThing(
                channel.itunesComplete, "channel.itunesComplete", "iTunes complete"
            )
        )
        messages.addMaybe(validateItunesType(channel.itunesType))
        validateNotBlank(channel.itunesSubtitle, "channel.itunesSubtitle", "iTunes subtitle", messages)
        validateNotBlank(channel.itunesAuthor, "channel.itunesAuthor", "iTunes author", messages)
        validateNotBlank(channel.itunesSummary, "channel.itunesSummary", "iTunes summary", messages)
        validateNotBlank(channel.newFeedUrl, "channel.newFeedUrl", "New feed URL", messages)
        validateNotBlank(channel.encodedDescription, "channel.encodedDescription", "Encoded description", messages)
        validateNotBlank(channel.mediaTitle, "channel.mediaTitle", "Media title", messages)

        if (channel.mediaTitle != null && channel.title != null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.EITHER_MEDIA_TITLE_OR_TITLE,
                    "Either media:title or title should be used, not both",
                    "channel.mediaTitle/title",
                    ValidationSeverity.ERROR
                )
            )
        }

        // Validate description and encodedDescription match
        validateDescriptionMatch(channel.description, channel.encodedDescription, "channel", messages)

        if (channel.language == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.MISSING_LANGUAGE,
                    "Channel language is missing",
                    "channel.language",
                    ValidationSeverity.ERROR,
                )
            )
        } else {
            val match = "^[a-z]{2}(_[a-z]{2})$".toRegex(RegexOption.IGNORE_CASE)
                .matchEntire(channel.language.stripExtensions().toString())
            when {
                match == null -> messages.add(
                    ValidationMessage(
                        ErrorMessages.MALFORMED_LOCALE,
                        "Malformed language format: ${channel.language}",
                        "channel.language",
                        ValidationSeverity.ERROR
                    )
                )

                Locale.getISOLanguages().any { it.equals(match.groupValues[0], true) } ||
                    isoCountries.any { it.equals(match.groupValues[1], true) } ->
                    ValidationMessage(
                        ErrorMessages.INVALID_LOCALE_COMPONENTS,
                        "The language contains non-ISO-639 components: ${channel.language}",
                        "channel.language",
                        ValidationSeverity.ERROR
                    )
            }
        }

        // Validate podcast:locked
        channel.podcastLocked?.let { locked ->
            messages.addAll(validatePodcastLocked(locked))
        }

        // Validate podcast:funding
        channel.podcastFunding?.let { funding ->
            messages.addAll(validatePodcastFunding(funding))
        }

        // Validate items
        channel.items?.forEach { item ->
            messages.addAll(validateItem(item))
        } ?: messages.add(
            ValidationMessage(
                ErrorMessages.CHANNEL_NO_ITEMS, "Channel has no items", "channel.items", ValidationSeverity.WARNING
            )
        )

        return messages
    }

    private fun validateBoolean(value: String?, path: String, fieldName: String): ValidationMessage? {
        if (value == null) return null
        return when (value.lowercase()) {
            "true", "false" -> null
            else -> ValidationMessage(
                ErrorMessages.APPLE_BOOLEANS, "$fieldName must be 'true' or 'false'", path, ValidationSeverity.ERROR
            )
        }
    }

    private fun validateWeirdAppleYesThing(value: String?, path: String, fieldName: String): ValidationMessage? {
        if (value == null) return null
        return when (value) {
            "Yes" -> null
            "yes" -> ValidationMessage(
                ErrorMessages.APPLE_YES_CASING,
                "$fieldName's value is 'yes', but it should be 'Yes'",
                path,
                ValidationSeverity.WARNING
            )

            else -> ValidationMessage(
                ErrorMessages.APPLE_YES_IS_THE_ONLY_ALLOWED_VALUE,
                "$fieldName's value must be 'Yes' or be removed",
                path,
                ValidationSeverity.ERROR
            )
        }
    }

    private fun validateItunesType(value: String?): ValidationMessage? {
        if (value == null) return null
        return when (value.lowercase()) {
            "episodic", "serial" -> null
            else -> ValidationMessage(
                ErrorMessages.APPLE_BOOLEANS,
                "itunes:type must be 'episodic' or 'serial'",
                "channel.itunesType",
                ValidationSeverity.ERROR
            )
        }
    }

    private fun validateItem(item: Item): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (item.title.isNullOrBlank() && item.itunesTitle.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_TITLE_REQUIRED,
                    "Item title or iTunes title is required",
                    "item.title/itunesTitle",
                    ValidationSeverity.ERROR
                )
            )
        }

        if (item.description.isNullOrBlank() && item.itunesSummary.isNullOrBlank() && item.encodedDescription.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_NO_DESCRIPTION,
                    "Item has no description",
                    "item.description/itunesSummary/encodedDescription",
                    ValidationSeverity.WARNING
                )
            )
        }

        if (item.enclosure == null && item.mediaContent == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_NO_ENCLOSURE,
                    "Item has no enclosure or media:content",
                    "item.enclosure/mediaContent",
                    ValidationSeverity.WARNING
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(item.title, "item.title", "Title", messages)
        validateNotBlank(item.description, "item.description", "Description", messages)
        validateNotBlank(item.link, "item.link", "Link", messages)
        validateNotBlank(item.comments, "item.comments", "Comments", messages)
        validateNotBlank(item.pubDate, "item.pubDate", "Publication date", messages)
        validateNotBlank(item.author, "item.author", "Author", messages)
        validateNotBlank(item.itunesAuthor, "item.itunesAuthor", "iTunes author", messages)
        validateNotBlank(item.itunesDuration, "item.itunesDuration", "iTunes duration", messages)
        validateNotBlank(item.itunesTitle, "item.itunesTitle", "iTunes title", messages)
        validateNotBlank(item.itunesSubtitle, "item.itunesSubtitle", "iTunes subtitle", messages)
        validateNotBlank(item.itunesSummary, "item.itunesSummary", "iTunes summary", messages)
        validateNotBlank(item.itunesBlock, "item.itunesBlock", "iTunes block", messages)
        validateNotBlank(item.encodedDescription, "item.encodedDescription", "Encoded description", messages)
        validateNotBlank(item.mediaDescription, "item.mediaDescription", "Media description", messages)
        validateNotBlank(item.dcTermsValid, "item.dcTermsValid", "DC terms valid", messages)

        // Validate description and encodedDescription match
        validateDescriptionMatch(item.description, item.encodedDescription, "item", messages)

        // Validate podcast:transcript
        item.podcastTranscript?.let { transcript ->
            messages.addAll(validatePodcastTranscript(transcript, "item.podcastTranscript"))
        }

        // Validate podcast:chapters
        item.podcastChapters?.let { chapters ->
            messages.addAll(validatePodcastChapters(chapters, "item.podcastChapters"))
        }

        // Validate podcast:soundbite
        item.podcastSoundbite?.let { soundbite ->
            messages.addAll(validatePodcastSoundbite(soundbite, "item.podcastSoundbite"))
        }

        return messages
    }

    private fun validatePodcastTranscript(
        transcript: PodcastTranscript,
        path: String,
    ): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (transcript.url.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_TRANSCRIPT_URL_REQUIRED,
                    "Podcast transcript URL is required",
                    "$path.url",
                    ValidationSeverity.ERROR
                )
            )
        }

        if (transcript.type.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_TRANSCRIPT_TYPE_REQUIRED,
                    "Podcast transcript type (MIME type) is required",
                    "$path.type",
                    ValidationSeverity.ERROR
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(transcript.url, "$path.url", "URL", messages)
        validateNotBlank(transcript.type, "$path.type", "Type", messages)
        validateNotBlank(transcript.rel, "$path.rel", "Rel", messages)

        return messages
    }

    private fun validatePodcastChapters(
        chapters: PodcastChapters,
        path: String,
    ): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (chapters.url.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_CHAPTERS_URL_REQUIRED,
                    "Podcast chapters URL is required",
                    "$path.url",
                    ValidationSeverity.ERROR
                )
            )
        }

        if (chapters.type.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_CHAPTERS_TYPE_REQUIRED,
                    "Podcast chapters type (MIME type) is required",
                    "$path.type",
                    ValidationSeverity.ERROR
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(chapters.url, "$path.url", "URL", messages)
        validateNotBlank(chapters.type, "$path.type", "Type", messages)

        return messages
    }

    private fun validatePodcastLocked(
        locked: PodcastLocked,
    ): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        when {
            locked.locked.isNullOrBlank() -> {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.PODCAST_LOCKED_VALUE_REQUIRED,
                        "Podcast locked value is required (yes or no)",
                        "channel.locked",
                        ValidationSeverity.ERROR
                    )
                )
            }

            locked.locked != "yes" && locked.locked != "no" -> {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.PODCAST_LOCKED_VALUE_INVALID,
                        "Podcast locked value must be 'yes' or 'no'",
                        "channel.locked",
                        ValidationSeverity.ERROR
                    )
                )
            }
        }

        validateNotBlank(locked.owner, "channel.locked#owner", "Podcast locked owner", messages)

        return messages
    }

    private fun validatePodcastFunding(
        fundings: List<PodcastFunding>,
    ): Set<ValidationMessage> {
        if (fundings.isEmpty()) return emptySet()
        val messages = mutableSetOf<ValidationMessage>()

        for ((i, funding) in fundings.withIndex()) {
            if (funding.fundingSource.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.PODCAST_FUNDING_SOURCE_REQUIRED,
                        "Podcast funding source value is required",
                        "channel.podcastFunding[$i].fundingSource",
                        ValidationSeverity.ERROR
                    )
                )
            } else if (funding.fundingSource.length > 128) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.PODCAST_FUNDING_SOURCE_TOO_LONG,
                        "Podcast funding source exceeds recommended maximum length of 128 characters",
                        "channel.podcastFunding[$i].fundingSource",
                        ValidationSeverity.WARNING
                    )
                )
            }

            if (funding.url.isNullOrBlank()) {
                messages.add(
                    ValidationMessage(
                        ErrorMessages.PODCAST_FUNDING_URL_REQUIRED,
                        "Podcast funding URL is required",
                        "channel.podcastFunding[$i].url",
                        ValidationSeverity.ERROR
                    )
                )
            }
        }
        return messages
    }

    private fun validatePodcastSoundbite(
        soundbite: PodcastSoundbite,
        path: String,
    ): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (soundbite.startTime == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_SOUNDBITE_START_TIME_REQUIRED,
                    "Podcast soundbite start time is required",
                    "$path.startTime",
                    ValidationSeverity.ERROR
                )
            )
        }

        if (soundbite.duration == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_SOUNDBITE_DURATION_REQUIRED,
                    "Podcast soundbite duration is required",
                    "$path.duration",
                    ValidationSeverity.ERROR
                )
            )
        }

        if (soundbite.fundingSource != null && soundbite.fundingSource.length > 128) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_SOUNDBITE_SOURCE_TOO_LONG,
                    "Podcast soundbite funding source exceeds recommended maximum length of 128 characters",
                    "$path.fundingSource",
                    ValidationSeverity.WARNING
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(soundbite.fundingSource, "$path.fundingSource", "Funding source", messages)

        return messages
    }

    private fun MutableCollection<ValidationMessage>.addMaybe(message: ValidationMessage?): MutableCollection<ValidationMessage> {
        if (message != null) add(message)
        return this
    }
}
