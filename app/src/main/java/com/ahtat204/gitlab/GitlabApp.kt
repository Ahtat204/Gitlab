package com.ahtat204.gitlab

import android.app.Application
import android.content.Context
import com.ahtat204.gitlab.domain.authentication.constants.Tokens
import dagger.hilt.android.HiltAndroidApp
import io.opentelemetry.android.OpenTelemetryRum
import io.opentelemetry.android.agent.OpenTelemetryRumInitializer
import io.opentelemetry.android.config.OtelRumConfig
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter
import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes


@HiltAndroidApp
class GitlabApp : Application() {
    private val endpoint = "http://192.168.1.199:4318"
    val HTTP_METHOD: AttributeKey<String?> = AttributeKey.stringKey("http.request.method")
    val HTTP_STATUS: AttributeKey<Long?> = AttributeKey.longKey("http.response.status_code")
    override fun onCreate() {
        super.onCreate()
        Tokens.initialize(this)
        //val attributes2 = Resource.builder().put()
        initializeOpenTelemetry(Tokens.context)
        val metricsExporter =
            OtlpHttpMetricExporter.builder().setEndpoint("$endpoint/v1/metrics").build()
        val spanExporter = OtlpHttpSpanExporter.builder()
            .setEndpoint("$endpoint/v1/traces")
            .build()

        val logExporter = OtlpHttpLogRecordExporter.builder()
            .setEndpoint("$endpoint/v1/logs")
            .build()

        val config = OtelRumConfig()//.setGlobalAttributes { }

        OpenTelemetryRum.builder(this, config).addMetricExporterCustomizer { metricsExporter }
            .addSpanExporterCustomizer { spanExporter }
            .addLogRecordExporterCustomizer { logExporter }
            .build()
    }

    private fun initializeOpenTelemetry(context: Context): OpenTelemetryRum =
        OpenTelemetryRumInitializer.initialize(
            context = context,
            configuration = {
                httpExport {
                    baseUrl = endpoint
                    
                }
                session {
                    backgroundInactivityTimeout = 15.minutes
                    maxLifetime = 4.days
                }

            }

        )
}

