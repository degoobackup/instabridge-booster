package com.didiglobal.booster.transform.asm

import com.didiglobal.booster.transform.KlassPool
import org.objectweb.asm.ClassWriter

/**
 * A [ClassWriter] that resolves common superclasses using the [KlassPool]'s
 * classloader, which includes the full runtime classpath and boot classpath
 * (android.jar). This enables [COMPUTE_FRAMES] to work correctly in Android
 * build environments where framework classes are not on the default classloader.
 *
 * @author artemchep
 */
class BoosterClassWriter(
    flags: Int,
    private val klassPool: KlassPool,
) : ClassWriter(flags) {
    override fun getCommonSuperClass(
        type1: String,
        type2: String,
    ): String {
        val class1 = resolveClass(type1) ?: return "java/lang/Object"
        val class2 = resolveClass(type2) ?: return "java/lang/Object"
        return when {
            class1.isAssignableFrom(class2) -> {
                type1
            }

            class2.isAssignableFrom(class1) -> {
                type2
            }

            class1.isInterface || class2.isInterface -> {
                "java/lang/Object"
            }

            else -> {
                var c = class1
                while (!c.isAssignableFrom(class2)) {
                    c = c.superclass ?: return "java/lang/Object"
                }
                c.name.replace('.', '/')
            }
        }
    }

    private fun resolveClass(type: String): Class<*>? =
        try {
            Class.forName(type.replace('/', '.'), false, klassPool.classLoader)
        } catch (_: Throwable) {
            null
        }
}
