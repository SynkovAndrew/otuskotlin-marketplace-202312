package com.otus.otuskotlin.stocktrack.configuration

import com.otus.otuskotlin.stocktrack.ApplicationSettings
import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.LoggerProvider
import com.otus.otuskotlin.stocktrack.SearchStocksResponseProcessor
import com.otus.otuskotlin.stocktrack.SingleStockResponseProcessor
import com.otus.otuskotlin.stocktrack.logbackLoggerWrapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringApplicationConfiguration {

    @Bean
    fun singleStockResponseProcessor(coreSettings: CoreSettings): SingleStockResponseProcessor {
        return SingleStockResponseProcessor(coreSettings = coreSettings)
    }

    @Bean
    fun searchStocksResponseProcessor(coreSettings: CoreSettings): SearchStocksResponseProcessor {
        return SearchStocksResponseProcessor(coreSettings = coreSettings)
    }

    @Bean
    fun loggerProvider(): LoggerProvider {
        return LoggerProvider { logbackLoggerWrapper(it) }
    }

    @Bean
    fun coreSettings(loggerProvider: LoggerProvider): CoreSettings {
        return CoreSettings(loggerProvider = loggerProvider)
    }

    @Bean
    fun applicationSettings(
        coreSettings: CoreSettings,
        singleStockResponseProcessor: SingleStockResponseProcessor,
        searchStocksResponseProcessor: SearchStocksResponseProcessor
    ): ApplicationSettings {
        return SpringApplicationSettings(
            coreSettings = coreSettings,
            singleStockResponseProcessor = singleStockResponseProcessor,
            searchStocksResponseProcessor = searchStocksResponseProcessor
        )
    }
}