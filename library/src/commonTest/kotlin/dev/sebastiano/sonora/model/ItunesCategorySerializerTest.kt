package dev.sebastiano.sonora.model

import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ItunesCategorySerializerTest {

    val xml = XML {
        defaultPolicy {
            ignoreUnknownChildren()
            isStrictBoolean = false
            isStrictAttributeNames = false
            isStrictOtherAttributes = false
        }
        indent = 4
    }

    @Test
    fun `should deserialize nested itunes categories`() {
        @Language("XML") val xmlInput =
            """
            |<rss version="2.0" xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd">
            |    <channel>
            |        <itunes:category text="Education">
            |            <itunes:category text="Self-Improvement" />
            |        </itunes:category>
            |    </channel>
            |</rss>
            """
                .trimMargin()

        val rssFeed: RssFeed = xml.decodeFromString(xmlInput)

        val categories = rssFeed.channel.itunesCategories
        assertNotNull(categories)
        assertEquals(1, categories.size)
        assertEquals("Education", categories[0].text)
        assertNotNull(categories[0].subcategory)
        assertEquals("Self-Improvement", categories[0].subcategory!!.text)
        val validated = ValidItunesCategory.asValidItunesCategory(categories.first())
        assertEquals(ValidItunesCategory.SELF_IMPROVEMENT, validated)
    }
}
