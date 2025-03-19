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
        "googleplay=http://www.google.com/schemas/play-podcasts/1.0;" +
        "content=http://purl.org/rss/1.0/modules/content/;" +
        "itunes=http://www.itunes.com/dtds/podcast-1.0.dtd;" +
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
    @XmlElement val transcript: Transcript? = null,
    @XmlElement val guid: Guid? = null,
    @XmlElement val source: Source? = null,
    @XmlCData // TODO improve later because spaces and stuff, it's hard I dunno
    @XmlSerialName(value = "encoded", namespace = "http://purl.org/rss/1.0/modules/content/", prefix = "content")
    @XmlElement val encodedDescription: String? = null,
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
data class Transcript(
    val url: String? = null,
    val type: String? = null,
    @Serializable(LocaleSerializer::class) val language: Locale? = null,
    val rel: String? = null,
)
