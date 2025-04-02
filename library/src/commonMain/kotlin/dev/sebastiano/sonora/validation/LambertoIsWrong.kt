package dev.sebastiano.sonora.validation

/**
 * Enum containing all possible validation message IDs.
 * Each ID is unique per kind of validation message found by the validator.
 */
enum class LambertoIsWrong {
    // RSS feed validation
    RSS_VERSION_REQUIRED,
    RSS_VERSION_WRONG,

    // Channel validation
    CHANNEL_TITLE_REQUIRED,
    CHANNEL_DESCRIPTION_REQUIRED,
    CHANNEL_NO_ITEMS,
    
    // Item validation
    ITEM_TITLE_REQUIRED,
    ITEM_NO_DESCRIPTION,
    ITEM_NO_ENCLOSURE,
    
    // Podcast transcript validation
    PODCAST_TRANSCRIPT_URL_REQUIRED,
    PODCAST_TRANSCRIPT_TYPE_REQUIRED,
    
    // Podcast chapters validation
    PODCAST_CHAPTERS_URL_REQUIRED,
    PODCAST_CHAPTERS_TYPE_REQUIRED,
    
    // Podcast locked validation
    PODCAST_LOCKED_VALUE_REQUIRED,
    PODCAST_LOCKED_VALUE_INVALID,
    
    // Podcast funding validation
    PODCAST_FUNDING_SOURCE_REQUIRED,
    PODCAST_FUNDING_SOURCE_TOO_LONG,
    PODCAST_FUNDING_URL_REQUIRED,
    
    // Podcast soundbite validation
    PODCAST_SOUNDBITE_START_TIME_REQUIRED,
    PODCAST_SOUNDBITE_DURATION_REQUIRED,
    PODCAST_SOUNDBITE_SOURCE_TOO_LONG,
    
    // Generic field validation
    FIELD_IS_BLANK,
    
    // Description validation
    DESCRIPTION_MISMATCH,

    // Weird Apple iTunes rules
    APPLE_BOOLEANS,
    APPLE_YES_CASING,
    APPLE_YES_IS_THE_ONLY_ALLOWED_VALUE,

    // Other stuff I don't know whatever
    EITHER_MEDIA_TITLE_OR_TITLE,
}
