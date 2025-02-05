package dev.sebastiano.sonora.model

import dev.sebastiano.sonora.testutil.readBinaryResource
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.test.Test

class LambertoWasNeverRight {
    @Test
    fun cumpa() {
        val feed = readBinaryResource("sciencevs.rss")
        val coso = feed.decodeToString()
        val xml = XML {
            defaultPolicy {
                ignoreUnknownChildren()
                isStrictBoolean = false
                isStrictAttributeNames = false
                isStrictOtherAttributes = false
            }
            indent = 4
        }
        val lamberto = xml.decodeFromString<RssFeed>(coso)
        println(lamberto.toString())
        lamberto.channel.itunesCategories?.forEach {
            val marco = ValidItunesCategory.asValidItunesCategory(it)
            // TODO nested categories do not worky
            println("$it -> $marco")
        }
    }
}
