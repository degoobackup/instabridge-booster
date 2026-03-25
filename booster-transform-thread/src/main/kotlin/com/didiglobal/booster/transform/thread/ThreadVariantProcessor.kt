package com.didiglobal.booster.transform.thread

import com.android.build.api.variant.DynamicFeatureVariant
import com.android.build.api.variant.LibraryVariant
import com.android.build.api.variant.Variant
import com.didiglobal.booster.task.spi.VariantProcessor
import com.google.auto.service.AutoService
import org.gradle.api.Project

@AutoService(VariantProcessor::class)
class ThreadVariantProcessor(private val project: Project) : VariantProcessor {

    override fun process(variant: Variant) {
        if (variant is LibraryVariant || variant is DynamicFeatureVariant) {
            return
        }
        project.dependencies.add("${variant.name}Implementation", "com.github.degoobackup.instabridge-booster:booster-android-instrument-thread:${Build.VERSION}")
    }

}
