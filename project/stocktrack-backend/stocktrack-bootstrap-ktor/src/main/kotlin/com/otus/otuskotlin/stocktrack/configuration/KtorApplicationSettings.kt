package com.otus.otuskotlin.stocktrack.configuration

import com.otus.otuskotlin.stocktrack.ApplicationSettings
import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.ResponseProcessor
import com.otus.otuskotlin.stocktrack.context.Context
import kotlin.reflect.KClass

data class KtorApplicationSettings(
    override val coreSettings: CoreSettings,
    override val processors: Map<KClass<*>, ResponseProcessor<*>>,
): ApplicationSettings