package dev.sebastiano.sonora.validation

/**
 * Enum containing all possible validation message IDs.
 * Each ID is unique per kind of validation message found by the validator.
 */
enum class ErrorMessages {
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
    ITEM_DOUBLE_ENCLOSURE,

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
    ITUNES_IMAGE_REQUIRED,
    ITUNES_CATEGORIES_REQUIRED,
    INVALID_ITUNES_CATEGORY,

    // Spotify tags
    SPOTIFY_LIMIT_MISSING_COUNT,
    SPOTIFY_LIMIT_INVALID_COUNT,

    // Atom issues
    ATOM_LINK_REQUIRED,
    ATOM_LINK_MISSING_SELF_LINK,
    ATOM_LINK_MULTIPLE_SELF_LINKS,

    // Other tags
    TTL_MUST_BE_POSITIVE,

    // Other stuff I don't know whatever
    EITHER_MEDIA_TITLE_OR_TITLE,
    MISSING_LANGUAGE,
    MALFORMED_LOCALE,
    INVALID_LOCALE_COMPONENTS,
}
