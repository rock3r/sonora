package dev.sebastiano.sonora.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import org.junit.jupiter.api.Test

class LambertoWasRight {
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
        println(xml.encodeToString(lamberto))
    }
}
