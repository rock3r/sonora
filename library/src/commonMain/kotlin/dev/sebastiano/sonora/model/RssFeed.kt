package dev.sebastiano.sonora.model

import dev.sebastiano.sonora.serialization.ItunesCategorySerializer
import dev.sebastiano.sonora.serialization.LocaleSerializer
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XmlCData
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlNamespaceDeclSpec
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.util.Locale

@OptIn(ExperimentalXmlUtilApi::class)
@XmlSerialName(value = "rss")
@XmlNamespaceDeclSpec(
    "atom=http://www.w3.org/2005/Atom;" +
        "googleplay=http://www.google.com/schemas/play-podcasts/1.0;" +
        "content=http://purl.org/rss/1.0/modules/content/;" +
        "itunes=http://www.itunes.com/dtds/podcast-1.0.dtd"
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
    @XmlElement val guid: String? = null,
    @XmlElement val enclosure: Enclosure? = null,
    @Serializable(LocaleSerializer::class)
    @XmlElement val language: Locale? = null,
    @XmlElement val copyright: String? = null,
    @XmlElement val image: ChannelImage? = null,
    @XmlElement
    @XmlSerialName(value = "explicit", namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
    val itunesExplicit: Boolean? = null,
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
    val itunesImage: String? = null,
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

@XmlSerialName(value = "item")
@Serializable
data class Item(
    @XmlElement val title: String? = null,
    @XmlElement val description: String? = null,
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

@XmlSerialName(value = "link", namespace = "http://www.w3.org/2005/Atom", prefix = "atom")
@Serializable
data class AtomLink(
    val href: String,
    val rel: String,
    val type: String? = null,
)
