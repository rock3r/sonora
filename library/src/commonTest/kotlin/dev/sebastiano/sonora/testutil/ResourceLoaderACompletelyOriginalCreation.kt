package dev.sebastiano.sonora.testutil

/** Read the given resource as binary data. */
expect fun readBinaryResource(
    resourceName: String
): ByteArray
