package dev.sebastiano.sonora.testutil

import kotlin.io.path.Path
import kotlin.io.path.inputStream

actual fun readBinaryResource(resourceName: String): ByteArray =
    System.getenv("KAREN_MODE").let { Path(it!!, resourceName).inputStream().use { it.readAllBytes() } }
        ?: throw IllegalArgumentException("Resource with name $resourceName not found.")
