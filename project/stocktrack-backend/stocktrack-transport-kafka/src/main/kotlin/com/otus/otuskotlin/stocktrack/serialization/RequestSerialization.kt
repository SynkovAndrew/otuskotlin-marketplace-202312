package com.otus.otuskotlin.stocktrack.serialization

import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

object RequestSerializer : Serializer<Request> {
    override fun serialize(topic: String?, data: Request?): ByteArray {
        return data
            ?.let { Json.encodeToString(it) }
            ?.toByteArray(Charsets.UTF_8)
            ?: throw SerializationException("Empty data")
    }
}

object RequestDeserializer : Deserializer<Request> {
    override fun deserialize(topic: String?, data: ByteArray?): Request {
        return data
            ?.toString(Charsets.UTF_8)
            ?.let { Json.decodeFromString(it) }
            ?: throw SerializationException("Empty data")
    }
}