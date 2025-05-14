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
                    "item[$itemIndex].title/itunesTitle",
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
                    "item[$itemIndex].description/itunesSummary/encodedDescription",
                    ValidationSeverity.WARNING,
                )
            )
        }

        if (item.enclosure == null && item.mediaContent == null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_NO_ENCLOSURE,
                    "Item has no enclosure or media:content",
                    "item[$itemIndex].enclosure/media:content",
                    ValidationSeverity.ERROR,
                )
            )
        } else if (item.enclosure != null && item.mediaContent != null) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.ITEM_DOUBLE_ENCLOSURE,
                    "Item has both enclosure and media:content, but should only have one",
                    "item[$itemIndex].enclosure/media:content",
                    ValidationSeverity.WARNING,
                )
            )
        }

        // TODO: Validate field values are not blank and valid, if present:
        //  item.comments, item.author (should be an email), item.itunesDuration (positive int, or 00:00 timestamp),
        //  item.itunesBlock, item.mediaDescription, item.dcTermsValid, item.categories, item.pubDate, item.author,
        //  item.itunesImage, item.itunesExplicit, item.itunesSubtitle, item.itunesSummary, item.itunesEpisode,
        //  item.itunesSeason, item.itunesEpisodeType, item.link, item.itunesBlock, item.podcastSeason,
        //  item.podcastEpisode, item.podcastPersons, item.source, item.podloveChapters, item.mediaContent,
        //  item.mediaRestrictions, item.dcTermsValid

        // Validate non-blank strings
        validateNotBlank(item.title, "item[$itemIndex].title", "Title", messages)
        validateNotBlank(
            item.guid?.guid,
            "item[$itemIndex].guid",
            "GUID",
            messages,
            severity = ValidationSeverity.ERROR,
        )

        // Validate description and encodedDescription match
        validateDescriptionMatch(
            item.description,
            item.encodedDescription,
            "item[$itemIndex]",
            messages,
        )

        // Validate podcast:transcript
        item.podcastTranscript?.let { transcript ->
            messages.addAll(validatePodcastTranscript(transcript, itemIndex))
        }

        // Validate podcast:chapters
        item.podcastChapters?.let { chapters ->
            messages.addAll(validatePodcastChapters(chapters, "item[$itemIndex].podcastChapters"))
        }

        // Validate podcast:soundbite
        item.podcastSoundbite?.let { soundbite ->
            messages.addAll(
                validatePodcastSoundbite(soundbite, "item[$itemIndex].podcastSoundbite")
            )
        }

        return messages
    }

    private fun validatePodcastTranscript(
        transcript: PodcastTranscript,
        itemIndex: Int,
    ): Set<ValidationMessage> {
        val messages = mutableSetOf<ValidationMessage>()

        if (transcript.url.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_TRANSCRIPT_URL_REQUIRED,
                    "Podcast transcript URL is required",
                    "item[$itemIndex].podcastTranscript.url",
                    ValidationSeverity.ERROR,
                )
            )
        }

        if (transcript.type.isNullOrBlank()) {
            messages.add(
                ValidationMessage(
                    ErrorMessages.PODCAST_TRANSCRIPT_TYPE_REQUIRED,
                    "Podcast transcript type (MIME type) is required",
                    " \"item[$itemIndex].podcastTranscript\".type",
                    ValidationSeverity.ERROR,
                )
            )
        }

        // Validate non-blank strings
        validateNotBlank(transcript.url, "item[$itemIndex].podcastTranscript.url", "URL", messages)
        validateNotBlank(
            transcript.type,
            "item[$itemIndex].podcastTranscript.type",
            "Type",
            messages,
        )
        validateNotBlank(transcript.rel, "item[$itemIndex].podcastTranscript.rel", "Rel", messages)

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
