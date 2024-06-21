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
        project.dependencies.add("implementation", "com.github.degoobackup.instabridge-booster:booster-android-instrument-shared-preferences:${Build.VERSION}")
    }
}
