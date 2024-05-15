package com.otus.otuskotlin.stocktrack.serialization

import com.otus.otuskotlin.stocktrack.api.v1.models.Response
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

object ResponseSerializer : Serializer<Response> {
    override fun serialize(topic: String?, data: Response?): ByteArray {
        return data
            ?.let { Json.encodeToString(it) }
            ?.toByteArray(Charsets.UTF_8)
            ?: throw SerializationException("Empty data")
    }
}

object ResponseDeserializer : Deserializer<Response> {
    override fun deserialize(topic: String?, data: ByteArray?): Response {
        return data
            ?.toString(Charsets.UTF_8)
            ?.let { Json.decodeFromString(it) }
            ?: throw SerializationException("Empty data")
    }
}