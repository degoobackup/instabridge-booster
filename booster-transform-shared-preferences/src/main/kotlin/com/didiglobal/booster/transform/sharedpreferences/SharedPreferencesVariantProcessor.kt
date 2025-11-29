package com.didiglobal.booster.transform.sharedpreferences

import com.android.build.api.variant.DynamicFeatureVariantBuilder
import com.android.build.api.variant.LibraryVariantBuilder
import com.android.build.api.variant.VariantBuilder
import com.didiglobal.booster.task.spi.VariantProcessor
import com.didiglobal.booster.transform.shared.preferences.Build
import com.didiglobal.booster.transform.shared.preferences.Build.GROUP
import com.didiglobal.booster.transform.shared.preferences.Build.VERSION
import com.google.auto.service.AutoService
import org.gradle.api.Project

/**
 * @author neighbWang
 */
@AutoService(VariantProcessor::class)
class SharedPreferencesVariantProcessor(private val project: Project) : VariantProcessor {

    override fun beforeProcess(variantBuilder: VariantBuilder) {
        if (variantBuilder is LibraryVariantBuilder || variantBuilder is DynamicFeatureVariantBuilder) {
            return
        }
        // 🔍 Log all configurations that end with "Implementation"
        project.logger.lifecycle("Booster: ===== Implementation configurations =====")
        project.configurations
            .filter { it.name.endsWith("Implementation") }
            .sortedBy { it.name }
            .forEach { config ->
                project.logger.lifecycle("Booster:   - ${config.name}")
            }
        project.logger.lifecycle("Booster: ======================================")

        project.dependencies.add("${variantBuilder.name}Implementation", "com.github.degoobackup.instabridge-booster:booster-android-instrument-shared-preferences:${VERSION}")
    }
}
