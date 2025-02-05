package dev.sebastiano.sonora.serialization

import dev.sebastiano.sonora.model.ItunesCategory
import dev.sebastiano.sonora.model.ItunesSubcategory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class ItunesCategorySerializer : KSerializer<ItunesCategory> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ItunesCategory") {
        element(
            "text",
            descriptor = serialDescriptor<String>().nullable,
            isOptional = true
        )
        element("subcategory", serialDescriptor<ItunesSubcategory?>().nullable, isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: ItunesCategory) {
        val compositeOutput = encoder.beginStructure(descriptor)
        value.text?.let { compositeOutput.encodeStringElement(descriptor, 0, it) }
        value.subcategory?.let {
            compositeOutput.encodeSerializableElement(
                descriptor,
                1,
                ItunesSubcategory.serializer(),
                it
            )
        }
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): ItunesCategory {
        val compositeInput = decoder.beginStructure(descriptor)
        var text: String? = null
        var subcategory: ItunesSubcategory? = null

        loop@ while (true) {
            when (val index = compositeInput.decodeElementIndex(descriptor)) {
                0 -> text = compositeInput.decodeStringElement(descriptor, index)
                1 -> subcategory =
                    compositeInput.decodeSerializableElement(descriptor, index, ItunesSubcategory.serializer())

                CompositeDecoder.DECODE_DONE -> break@loop
                else -> throw SerializationException("Unexpected index: $index")
            }
        }

        compositeInput.endStructure(descriptor)
        return ItunesCategory(text, subcategory)
    }
}
