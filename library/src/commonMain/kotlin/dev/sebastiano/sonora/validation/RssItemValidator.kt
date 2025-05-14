package dev.sebastiano.sonora.validation

import dev.sebastiano.sonora.model.Item
import dev.sebastiano.sonora.model.PodcastChapters
import dev.sebastiano.sonora.model.PodcastSoundbite
import dev.sebastiano.sonora.model.PodcastTranscript

internal object RssItemValidator {
    fun validateItem(item: Item, itemIndex: Int): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (item.title.isNullOrBlank() && item.itunesTitle.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_TITLE_REQUIRED,
                    "Item title or iTunes title is required",
                    "item.title/itunesTitle",
                    ValidationSeverity.ERROR,
                )
            )
        }

        if (
            item.description.isNullOrBlank() &&
                item.itunesSummary.isNullOrBlank() &&
                item.encodedDescription.isNullOrBlank()
        ) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_NO_DESCRIPTION,
                    "Item has no description",
                    "item.description/itunesSummary/encodedDescription",
                    ValidationSeverity.WARNING,
                )
            )
        }

        if (item.enclosure == null && item.mediaContent == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_NO_ENCLOSURE,
                    "Item has no enclosure or media:content",
                    "item.enclosure/mediaContent",
                    ValidationSeverity.WARNING,
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
        validateNotBlank(
            item.encodedDescription,
            "item.encodedDescription",
            "Encoded description",
            messages,
        )
        validateNotBlank(
            item.mediaDescription,
            "item.mediaDescription",
            "Media description",
            messages,
        )
        validateNotBlank(item.dcTermsValid, "item.dcTermsValid", "DC terms valid", messages)

        // Validate description and encodedDescription match
        validateDescriptionMatch(item.description, item.encodedDescription, "item", messages)

        // Validate podcast:transcript
        item.podcastTranscript?.let { transcript ->
            messages.addAll(validatePodcastTranscript(transcript))
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
    ): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (transcript.url.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_TRANSCRIPT_URL_REQUIRED,
                    "Podcast transcript URL is required",
                    "item.podcastTranscript.url",
                    ValidationSeverity.ERROR,
                )
            )
        }

        if (transcript.type.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_TRANSCRIPT_TYPE_REQUIRED,
                    "Podcast transcript type (MIME type) is required",
                    " \"item.podcastTranscript\".type",
                    ValidationSeverity.ERROR,
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(transcript.url, "item.podcastTranscript.url", "URL", messages)
        validateNotBlank(transcript.type, "item.podcastTranscript.type", "Type", messages)
        validateNotBlank(transcript.rel, "item.podcastTranscript.rel", "Rel", messages)

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
                    ValidationSeverity.ERROR,
                )
            )
        }

        if (chapters.type.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_CHAPTERS_TYPE_REQUIRED,
                    "Podcast chapters type (MIME type) is required",
                    "$path.type",
                    ValidationSeverity.ERROR,
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(chapters.url, "$path.url", "URL", messages)
        validateNotBlank(chapters.type, "$path.type", "Type", messages)

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
                    ValidationSeverity.ERROR,
                )
            )
        }

        if (soundbite.duration == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_SOUNDBITE_DURATION_REQUIRED,
                    "Podcast soundbite duration is required",
                    "$path.duration",
                    ValidationSeverity.ERROR,
                )
            )
        }

        if (soundbite.fundingSource != null && soundbite.fundingSource.length > 128) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_SOUNDBITE_SOURCE_TOO_LONG,
                    "Podcast soundbite funding source exceeds recommended maximum length of 128 characters",
                    "$path.fundingSource",
                    ValidationSeverity.WARNING,
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(soundbite.fundingSource, "$path.fundingSource", "Funding source", messages)

        return messages
    }
}
