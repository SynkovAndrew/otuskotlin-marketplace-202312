package com.otus.otuskotlin.stocktrack

import kotlin.reflect.KClass

interface ApplicationSettings {
    val processors: Map<KClass<*>, ResponseProcessor<*, *, *>>
    val coreSettings: CoreSettings
}
