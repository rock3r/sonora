package dev.sebastiano.sonora.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlNamespaceDeclSpec
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@OptIn(ExperimentalXmlUtilApi::class)
@XmlSerialName(value = "rss")
@XmlNamespaceDeclSpec("atom=http://www.w3.org/2005/Atom;" +
    "googleplay=http://www.google.com/schemas/play-podcasts/1.0")
@Serializable
data class RssFeed(
    val version: String,
    @XmlElement val channel: Channel,
)

@XmlSerialName(value = "channel")
@Serializable
data class Channel(
    @XmlElement val title: String,
    @XmlElement val atomLink: AtomLink,
    @XmlElement val items: List<Item>,
)

@XmlSerialName(value = "item")
@Serializable
data class Item(
    @XmlElement val title: String,
)

@XmlSerialName(value = "link", namespace = "http://www.w3.org/2005/Atom", prefix = "atom")
@Serializable
data class AtomLink(
    val href: String,
    val rel: String,
    val type: String? = null,
)
