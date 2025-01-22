package dev.sebastiano.sonora.model

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
@XmlNamespaceDeclSpec("atom=http://www.w3.org/2005/Atom;" +
    "googleplay=http://www.google.com/schemas/play-podcasts/1.0;" +
    "content=http://purl.org/rss/1.0/modules/content/;" +
    "itunes=http://www.itunes.com/dtds/podcast-1.0.dtd")
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
    @Serializable(LocaleSerializer::class)
    @XmlElement val language: Locale? = null,
    @XmlCData // TODO improve later because spaces and stuff, it's hard I dunno
    @XmlSerialName(value = "encoded", namespace = "http://purl.org/rss/1.0/modules/content/", prefix = "content")
    @XmlElement val encodedDescription: String? = null,
    @XmlElement val items: List<Item>? = null,
)

@XmlSerialName(value = "item")
@Serializable
data class Item(
    @XmlElement val title: String? = null,
    @XmlElement val description: String? = null,
)

@XmlSerialName(value = "link", namespace = "http://www.w3.org/2005/Atom", prefix = "atom")
@Serializable
data class AtomLink(
    val href: String,
    val rel: String,
    val type: String? = null,
)
