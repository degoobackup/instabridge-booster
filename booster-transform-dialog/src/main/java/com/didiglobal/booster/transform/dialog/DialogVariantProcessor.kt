package com.didiglobal.booster.transform.dialog

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.LibraryVariant
import com.didiglobal.booster.gradle.isDynamicFeature
import com.didiglobal.booster.gradle.project
import com.didiglobal.booster.task.spi.VariantProcessor
import com.google.auto.service.AutoService

@AutoService(VariantProcessor::class)
class DialogVariantProcessor : VariantProcessor {

    override fun process(variant: BaseVariant) {
        if (variant !is LibraryVariant && !variant.isDynamicFeature) {
            variant.project.dependencies.add("implementation", "com.github.degoobackup.instabridge-booster:booster-android-instrument-dialog:${Build.VERSION}")
        }
    }

}
