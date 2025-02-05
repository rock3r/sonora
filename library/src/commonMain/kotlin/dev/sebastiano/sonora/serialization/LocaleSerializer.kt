package dev.sebastiano.sonora.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Locale

class LocaleSerializer : KSerializer<Locale> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.util.Locale", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Locale) {
        encoder.encodeString(value.toLanguageTag())
    }

    override fun deserialize(decoder: Decoder): Locale {
        return try {
            Locale.forLanguageTag(decoder.decodeString())
        } catch (e: Exception) {
            throw SerializationException("Invalid locale", e)
        }
    }
}
