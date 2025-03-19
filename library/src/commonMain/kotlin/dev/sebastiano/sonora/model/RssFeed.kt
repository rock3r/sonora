package dev.sebastiano.sonora.model

import dev.sebastiano.sonora.serialization.ItunesCategorySerializer
import dev.sebastiano.sonora.serialization.LocaleSerializer
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XmlCData
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlNamespaceDeclSpec
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue
import java.util.Locale

@OptIn(ExperimentalXmlUtilApi::class)
@XmlSerialName(value = "rss")
@XmlNamespaceDeclSpec(
    "atom=http://www.w3.org/2005/Atom;" +
        "content=http://purl.org/rss/1.0/modules/content/;" +
        "itunes=http://www.itunes.com/dtds/podcast-1.0.dtd;" +
        "media=http://search.yahoo.com/mrss/;" +
        "dcterms=http://purl.org/dc/terms;" +
        "spotify=https://www.spotify.com/ns/rss;" +
        "psc=http://podlove.org/simple-chapters;" +
        "podcast=https://podcastindex.org/namespace/1.0"
)
@Serializable
data class RssFeed(
    val version: String,
    @XmlElement val channel: Channel,
)

@XmlSerialName(value = "channel")
@Serializable
data class Channel(
    @XmlElement val title: String? = null,
    @XmlElement val atomLink: AtomLink? = null,
    @XmlElement val description: String? = null,
    @XmlElement val pubDate: String? = null,
    @XmlElement val lastBuildDate: String? = null,
    @Serializable(LocaleSerializer::class)
    @XmlElement val language: Locale? = null,
    @XmlElement val copyright: String? = null,
    @XmlElement val webMaster: String? = null,
    @XmlElement val generator: String? = null,
    @XmlElement val image: ChannelImage? = null,
    @XmlElement val ttl: Int? = null,
    @XmlElement val categories: List<Category>? = null,
    @XmlElement
    @XmlSerialName(value = "explicit", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesExplicit: String? = null,
    @XmlElement
    @XmlSerialName(value = "block", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesBlock: String? = null,
    @XmlElement
    @XmlSerialName(value = "complete", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesComplete: String? = null,
    @XmlElement
    @XmlSerialName(value = "type", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesType: String? = null,
    @XmlElement
    @XmlSerialName(value = "subtitle", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesSubtitle: String? = null,
    @XmlElement
    @XmlSerialName(value = "author", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesAuthor: String? = null,
    @XmlElement
    @XmlSerialName(value = "summary", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesSummary: String? = null,
    @XmlElement
    @XmlSerialName(value = "owner", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesOwner: ItunesOwner? = null,
    @XmlSerialName(value = "image", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesImage: ItunesImage? = null,
    @XmlElement
    @XmlSerialName(value = "category", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesCategories: List<ItunesCategory>? = null,
    @XmlElement
    @XmlSerialName(value = "new-feed-url", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val newFeedUrl: String? = null,
    @XmlCData // TODO improve later because spaces and stuff, it's hard I dunno
    @XmlSerialName(value = "encoded", namespace = "http://purl.org/rss/1.0/modules/content/", prefix = "content")
    @XmlElement val encodedDescription: String? = null,
    @XmlElement val items: List<Item>? = null,
    val mediaRestrictions: MediaRestrictions? = null,
    val spotifyLimit: SpotifyLimit? = null,
    val spotifyCountryOfOrigin: SpotifyCountryOfOrigin? = null,
    @XmlSerialName(value = "title", namespace = "http://search.yahoo.com/mrss/", prefix = "media")
    @XmlElement val mediaTitle: String? = null, // Mutually exclusive with the normal title
    @XmlElement val podcastPodroll: PodcastPodroll? = null,
    @XmlElement val podcastLocked: PodcastLocked? = null,
    @XmlElement val podcastFunding: PodcastFunding? = null,
    val podcastPersons: List<PodcastPerson>,
    val podcastLocation: PodcastLocation,
)

@XmlSerialName(value = "category", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
@Serializable(ItunesCategorySerializer::class)
data class ItunesCategory(
    val text: String? = null,
    @XmlElement val subcategory: ItunesSubcategory? = null,
)

@XmlSerialName(value = "category", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
@Serializable
data class ItunesSubcategory(
    val text: String? = null,
)

@XmlSerialName(value = "owner", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
@Serializable
data class ItunesOwner(
    @XmlElement val name: String? = null,
    @XmlElement val email: String? = null,
)

@XmlSerialName(value = "image", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
@Serializable
data class ItunesImage(
    val href: String? = null,
)

@XmlSerialName(value = "item")
@Serializable
data class Item(
    @XmlElement val title: String? = null,
    @XmlElement val description: String? = null,
    @XmlElement val link: String? = null,
    @XmlElement val categories: List<Category>? = null,
    @XmlElement val comments: String? = null,
    @XmlElement val pubDate: String? = null,
    @XmlElement val author: String? = null,
    @XmlElement val enclosure: Enclosure? = null,
    @XmlElement
    @XmlSerialName(value = "author", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesAuthor: String? = null,
    @XmlElement
    @XmlSerialName(value = "duration", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesDuration: String? = null,
    @XmlElement
    @XmlSerialName(value = "image", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesImage: ItunesImage? = null,
    @XmlElement
    @XmlSerialName(value = "explicit", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesExplicit: Boolean? = null,
    @XmlElement
    @XmlSerialName(value = "title", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesTitle: String? = null,
    @XmlElement
    @XmlSerialName(value = "subtitle", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesSubtitle: String? = null,
    @XmlElement
    @XmlSerialName(value = "summary", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesSummary: String? = null,
    @XmlElement
    @XmlSerialName(value = "season", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesSeason: Int? = null,
    @XmlElement
    @XmlSerialName(value = "episode", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesEpisode: Int? = null,
    @XmlElement
    @XmlSerialName(value = "episodeType", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesEpisodeType: String? = null,
    @XmlElement
    @XmlSerialName(value = "block", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesBlock: String? = null,
    @XmlElement val podcastTranscript: PodcastTranscript? = null,
    @XmlElement val podcastChapters: PodcastChapters? = null,
    @XmlElement val podcastSoundbite: PodcastSoundbite? = null,
    @XmlElement val podcastSeason: PodcastSeason? = null,
    @XmlElement val podcastEpisode: PodcastEpisode? = null,
    val podcastPersons: List<PodcastPerson>,
    @XmlElement val guid: Guid? = null,
    @XmlElement val source: Source? = null,
    @XmlCData // TODO improve later because spaces and stuff, it's hard I dunno
    @XmlSerialName(value = "encoded", namespace = "http://purl.org/rss/1.0/modules/content/", prefix = "content")
    @XmlElement val encodedDescription: String? = null,
    @XmlElement val podloveChapters: PodloveChapters? = null,
    @XmlSerialName(value = "description", namespace = "http://search.yahoo.com/mrss/", prefix = "media")
    @XmlElement val mediaDescription: String? = null, // Mutually exclusive with the normal description
    @XmlSerialName(value = "content", namespace = "http://search.yahoo.com/mrss/", prefix = "media")
    @XmlElement val mediaContent: MediaContent? = null, // Mutually exclusive with the enclosure tag
    val mediaRestrictions: MediaRestrictions? = null,
    @XmlSerialName(value = "valid", namespace = "http://purl.org/dc/terms", prefix = "dcterms")
    @XmlElement val dcTermsValid: String? = null,
)

@XmlSerialName(value = "image")
@Serializable
data class ChannelImage(
    @XmlElement val url: String? = null,
    @XmlElement val title: String? = null,
    @XmlElement val link: String? = null,
)

@XmlSerialName(value = "enclosure")
@Serializable
data class Enclosure(
    val url: String? = null,
    val type: String? = null,
    val length: String? = null,
)

@XmlSerialName(value = "source")
@Serializable
data class Source(
    @XmlValue val name: String? = null,
    val url: String? = null,
)

@XmlSerialName(value = "guid")
@Serializable
data class Guid(
    @XmlValue val guid: String? = null,
    val isPermaLink: Boolean? = null,
)

@XmlSerialName(value = "category")
@Serializable
data class Category(
    @XmlValue val name: String? = null,
    val domain: String? = null,
)

@XmlSerialName(value = "link", namespace = "http://www.w3.org/2005/Atom", prefix = "atom")
@Serializable
data class AtomLink(
    val href: String,
    val rel: String,
    val type: String? = null,
)

@XmlSerialName(value = "transcript", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastTranscript(
    val url: String? = null, // mandatory
    val type: String? = null, // mandatory, MIME type
    @Serializable(LocaleSerializer::class) val language: Locale? = null,
    val rel: String? = null,
)

@XmlSerialName(value = "chapters", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastChapters(
    val url: String? = null, // mandatory
    val type: String? = null, // mandatory, MIME type
)

@XmlSerialName(value = "podroll", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastPodroll(
    val remoteItems: List<PodcastRemoteItem>
)

@XmlSerialName(value = "remoteItem", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastRemoteItem(
    val feedGuid: String? = null, // mandatory
    val feedUrl: String? = null,
    val itemGuid: String? = null,
    val medium: String? = null, // (see podcast:medium) If not 'podcast', should match that feed's 'medium' attribute
    val title: String? = null,
)

@XmlSerialName(value = "locked", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastLocked(
    @XmlValue val locked: String? = null, // mandatory; yes or no
    val owner: String? = null, // email address
)

@XmlSerialName(value = "funding", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastFunding(
    @XmlValue val fundingSource: String? = null, // mandatory, free-form, max 128 chars (soft limit)
    val url: String? = null, // mandatory
)

@XmlSerialName(value = "soundbite", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastSoundbite(
    @XmlValue val fundingSource: String? = null, // free-form, max 128 chars (soft limit)
    val startTime: Float? = null, // mandatory, seconds
    val duration: Float? = null, // mandatory, seconds
)

@XmlSerialName(value = "person", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastPerson(
    @XmlValue val name: String? = null, // free-form, non blank, max 128 chars (soft limit)
    val role: String? = null, // should be role from taxonomy; assumed 'host' if missing
    val group: String? = null, // should be group from taxonomy; assumed 'cast' if missing
    val img: String? = null, // image url
    val href: String? = null, // url
)

@XmlSerialName(value = "location", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastLocation(
    @XmlValue val name: String? = null, // free-form, non blank, max 128 chars (soft limit)
    val geo: String? = null, // recommended; uses geo notation (geo:30.2672,97.7431)
    val osm: String? = null, // recommended; uses OSM notation (R113314)
)

@XmlSerialName(value = "season", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastSeason(
    @XmlValue val number: Int? = null, // mandatory
    val name: String? = null, // free-form, non blank, max 128 chars (soft limit)
)

@XmlSerialName(value = "episode", namespace = "https://podcastindex.org/namespace/1.0", prefix = "podcast")
@Serializable
data class PodcastEpisode(
    @XmlValue val number: Int? = null, // mandatory
    val display: String? = null, // free-form, non blank, max 32 chars (soft limit)
)

@XmlSerialName(value = "chapters", namespace = "http://podlove.org/simple-chapters", prefix = "psc")
@Serializable
data class PodloveChapters(
    @XmlElement val chapters: List<PodloveChapter>,
    val version: String? = null,
)

@XmlSerialName(value = "chapter", namespace = "http://podlove.org/simple-chapters", prefix = "psc")
@Serializable
data class PodloveChapter(
    val start: String? = null, // mandatory ([05:][12:]03[.5])
    val title: String? = null, // mandatory lol
    val href: String? = null,
    val image: String? = null,
)

@XmlSerialName(value = "restriction", namespace = "http://search.yahoo.com/mrss/", prefix = "media")
@Serializable
data class MediaRestrictions(
    @XmlValue val countries: String? = null, // space-separated ISO country codes
    val type: String? = null, // 'country' is the only valid value
    val country: String? = null, // 'allow' is the only valid value
)

@XmlSerialName(value = "limit", namespace = "https://www.spotify.com/ns/rss", prefix = "spotify")
@Serializable
data class SpotifyLimit(
    val recentCount: Int? = null,
)

@XmlSerialName(value = "countryOfOrigin", namespace = "https://www.spotify.com/ns/rss", prefix = "spotify")
@Serializable
data class SpotifyCountryOfOrigin(
    @XmlValue val countries: String? = null, // space separated list of ISO 3166 country codes ranked in order
    // of priority from most relevant to least relevant
)

@XmlSerialName(value = "content", namespace = "http://search.yahoo.com/mrss/", prefix = "media")
@Serializable
data class MediaContent(
    val type: String? = null, // MIME type
    val url: String? = null,
)
