package dev.sebastiano.sonora.validation

import dev.sebastiano.sonora.model.RssFeed
import dev.sebastiano.sonora.model.Channel
import dev.sebastiano.sonora.model.Item
import dev.sebastiano.sonora.model.PodcastTranscript
import dev.sebastiano.sonora.model.PodcastChapters
import dev.sebastiano.sonora.model.PodcastLocked
import dev.sebastiano.sonora.model.PodcastFunding
import dev.sebastiano.sonora.model.PodcastSoundbite
import dev.sebastiano.sonora.model.PodcastPerson
import dev.sebastiano.sonora.model.PodcastLocation
import dev.sebastiano.sonora.testutil.readBinaryResource
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class RssFeedValidatorTest {

    private val xml = XML {
        defaultPolicy {
            ignoreUnknownChildren()
            isStrictBoolean = false
            isStrictAttributeNames = false
            isStrictOtherAttributes = false
        }
        indent = 4
    }

    @Test
    fun `validate valid feed returns success`() {
        // Given a valid feed
        val feedXml = readBinaryResource("sciencevs.rss").decodeToString()
        val feed = xml.decodeFromString<RssFeed>(feedXml)

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a success
        assertTrue(result is RssFeedValidationResult.Success)
        assertEquals(feed, result.feed)
    }

    @Test
    fun `validate feed with missing title returns failure`() {
        // Given a feed with missing title
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = null,
                description = "Description",
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item Description",
                        podcastPersons = emptyList()
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a failure with an error about the missing title
        assertTrue(result is RssFeedValidationResult.Failure)
        val errors = result.errors
        assertTrue(errors.any { it.path == "channel.title" && it.message.contains("title is required") && it.severity == ValidationSeverity.ERROR })
    }

    @Test
    fun `validate feed with missing description returns failure`() {
        // Given a feed with missing description
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = "Title",
                description = null,
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item Description",
                        podcastPersons = emptyList()
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a failure with an error about the missing description
        assertTrue(result is RssFeedValidationResult.Failure)
        val errors = result.errors
        assertTrue(errors.any { it.path == "channel.description" && it.message.contains("description is required") && it.severity == ValidationSeverity.ERROR })
    }

    @Test
    fun `validate feed with invalid podcast locked value returns failure`() {
        // Given a feed with invalid podcast locked value
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = "Title",
                description = "Description",
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                podcastLocked = PodcastLocked(
                    locked = "invalid",
                    owner = "owner@example.com"
                ),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item Description",
                        podcastPersons = emptyList()
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a failure with an error about the invalid locked value
        assertTrue(result is RssFeedValidationResult.Failure)
        val errors = result.errors
        assertTrue(errors.any { it.path == "channel.podcastLocked.locked" && it.message.contains("must be 'yes' or 'no'") && it.severity == ValidationSeverity.ERROR })
    }

    @Test
    fun `validate feed with missing podcast transcript url returns failure`() {
        // Given a feed with missing podcast transcript url
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = "Title",
                description = "Description",
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item Description",
                        podcastPersons = emptyList(),
                        podcastTranscript = PodcastTranscript(
                            url = null,
                            type = "text/plain"
                        )
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a failure with an error about the missing transcript url
        assertTrue(result is RssFeedValidationResult.Failure)
        val errors = result.errors
        assertTrue(errors.any { it.path == "item.podcastTranscript.url" && it.message.contains("URL is required") && it.severity == ValidationSeverity.ERROR })
    }

    @Test
    fun `validate feed with missing podcast transcript type returns failure`() {
        // Given a feed with missing podcast transcript type
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = "Title",
                description = "Description",
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item Description",
                        podcastPersons = emptyList(),
                        podcastTranscript = PodcastTranscript(
                            url = "https://example.com/transcript.txt",
                            type = null
                        )
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a failure with an error about the missing transcript type
        assertTrue(result is RssFeedValidationResult.Failure)
        val errors = result.errors
        assertTrue(errors.any { it.path == "item.podcastTranscript.type" && it.message == "Podcast transcript type (MIME type) is required" && it.severity == ValidationSeverity.ERROR })
    }

    @Test
    fun `validate feed with blank string fields returns warnings`() {
        // Given a feed with blank string fields
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = "Title",
                description = "Description",
                copyright = "",  // Blank string
                webMaster = "  ",  // Blank string with whitespace
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item Description",
                        link = "",  // Blank string
                        comments = "   ",  // Blank string with whitespace
                        podcastPersons = emptyList()
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a success with warnings about blank string fields
        assertTrue(result is RssFeedValidationResult.Success)
        val warnings = result.messages
        assertTrue(warnings.any { it.path == "channel.copyright" && it.message.contains("Copyright is blank") && it.severity == ValidationSeverity.WARNING })
        assertTrue(warnings.any { it.path == "channel.webMaster" && it.message.contains("Webmaster is blank") && it.severity == ValidationSeverity.WARNING })
        assertTrue(warnings.any { it.path == "item.link" && it.message.contains("Link is blank") && it.severity == ValidationSeverity.WARNING })
        assertTrue(warnings.any { it.path == "item.comments" && it.message.contains("Comments is blank") && it.severity == ValidationSeverity.WARNING })
    }

    @Test
    fun `validate feed with matching description and encodedDescription returns success`() {
        // Given a feed with matching description and encodedDescription
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = "Title",
                description = "This is a description",
                encodedDescription = "This is a description",
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item description",
                        encodedDescription = "Item description",
                        podcastPersons = emptyList()
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a success with no warnings about description mismatch
        assertTrue(result is RssFeedValidationResult.Success)
        val warnings = result.messages
        assertFalse(warnings.any { it.path == "channel" && it.message.contains("Description and encoded description") })
        assertFalse(warnings.any { it.path == "item" && it.message.contains("Description and encoded description") })
    }

    @Test
    fun `validate feed with non-matching description and encodedDescription returns warning`() {
        // Given a feed with non-matching description and encodedDescription
        val feed = RssFeed(
            version = "2.0",
            channel = Channel(
                title = "Title",
                description = "This is a description",
                encodedDescription = "<p>This is a <strong>different</strong> description</p>",
                podcastPersons = emptyList(),
                podcastLocation = PodcastLocation(name = null),
                items = listOf(
                    Item(
                        title = "Item Title",
                        description = "Item description",
                        encodedDescription = "<p>Item <em>different</em> description</p>",
                        podcastPersons = emptyList()
                    )
                )
            )
        )

        // When validating the feed
        val result = RssFeedValidator.validate(feed)

        // Then the result is a success with warnings about description mismatch
        assertTrue(result is RssFeedValidationResult.Success)
        val warnings = result.messages
        assertTrue(warnings.any { it.path == "channel" && it.message.contains("Description and encoded description") && it.severity == ValidationSeverity.WARNING })
        assertTrue(warnings.any { it.path == "item" && it.message.contains("Description and encoded description") && it.severity == ValidationSeverity.WARNING })
    }
}
