package dev.sebastiano.sonora.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import org.junit.jupiter.api.Test

class LambertoWasNeverRight {
    @Test
    fun cumpa() {
        val feed = RssFeed::class.java.classLoader.getResourceAsStream("sciencevs.rss")
        val coso = feed!!.readBytes().decodeToString()
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
//        println(xml.encodeToString(lamberto))
    }
}
