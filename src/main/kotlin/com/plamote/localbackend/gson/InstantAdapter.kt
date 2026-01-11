package com.plamote.localbackend.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.Instant


class InstantAdapter : TypeAdapter<Instant?>() {
  override fun write(p0: JsonWriter?, p1: Instant?) {
    if (p0 != null) {
      p0.value(p1.toString())
    } // ISO 8601 string
  }

  @Throws(IOException::class)
  override fun read(`in`: JsonReader): Instant {
    return Instant.parse(`in`.nextString()) // Parses ISO 8601 string
  }
}


