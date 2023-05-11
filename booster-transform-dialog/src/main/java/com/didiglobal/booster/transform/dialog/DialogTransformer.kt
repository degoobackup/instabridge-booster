package com.didiglobal.booster.transform.dialog

import com.didiglobal.booster.kotlinx.asIterable
import com.didiglobal.booster.kotlinx.touch
import com.didiglobal.booster.transform.ArtifactManager
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.TransformException
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.didiglobal.booster.transform.asm.className
import com.didiglobal.booster.transform.asm.find
import com.didiglobal.booster.transform.asm.findAll
import com.didiglobal.booster.transform.asm.isInstanceOf
import com.didiglobal.booster.transform.util.ComponentHandler
import com.google.auto.service.AutoService
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TypeInsnNode
import java.io.PrintWriter
import javax.xml.parsers.SAXParserFactory

/**
 * Represents a class transformer for dialogs
 *
 * @author Daniel Olsson
 */
@AutoService(ClassTransformer::class)
class DialogTransformer : ClassTransformer {

    private lateinit var logger: PrintWriter
    private val applications = mutableSetOf<String>()

    override val name: String = Build.ARTIFACT

    override fun onPreTransform(context: TransformContext) {
        val parser = SAXParserFactory.newInstance().newSAXParser()
        context.artifacts.get(ArtifactManager.MERGED_MANIFESTS).forEach { manifest ->
            val handler = ComponentHandler()
            parser.parse(manifest, handler)
            applications.addAll(handler.applications)
        }
        this.logger = getReport(context, "report.txt").touch().printWriter()
    }

    override fun onPostTransform(context: TransformContext) {
        this.logger.close()
    }

    override fun transform(context: TransformContext, klass: ClassNode): ClassNode {
        if (klass.name.startsWith(BOOSTER_INSTRUMENT)) {
            // ignore booster instrumented classes
            return klass
        }

        klass.methods?.forEach { method ->
            method.instructions?.iterator()?.asIterable()?.forEach {
                when (it.opcode) {
                    Opcodes.NEW -> (it as TypeInsnNode).transform(context, klass, method)
                }
            }
        }
        return klass
    }

    private fun TypeInsnNode.transform(context: TransformContext, klass: ClassNode, method: MethodNode) {
        when (this.desc) {
            ALERT_DIALOG -> this.transformNew(context, klass, method, SHADOW_ALERT_DIALOG)
            APP_COMPAT_DIALOG -> this.transformNew(context, klass, method, SHADOW_APP_COMPAT_DIALOG)
        }
    }

    private fun TypeInsnNode.transformNew(@Suppress("UNUSED_PARAMETER") context: TransformContext, klass: ClassNode, method: MethodNode, type: String, optimizable: Boolean = false) {
        this.find {
            (it.opcode == Opcodes.INVOKESPECIAL) &&
                    (it is MethodInsnNode) &&
                    (this.desc == it.owner && "<init>" == it.name)
        }?.isInstanceOf { init: MethodInsnNode ->
            // replace original type with shadowed type
            // e.g. android/os/HandlerThread => com/didiglobal/booster/instrument/ShadowHandlerThread
            this.desc = type

            // replace the constructor of original type with the constructor of shadowed type
            // e.g. android/os/HandlerThread(Ljava/lang/String;) => com/didiglobal/booster/instrument/ShadowHandlerThread(Ljava/lang/String;Ljava/lang/String;)
            val rp = init.desc.lastIndexOf(')')
            init.apply {
                owner = type
                //desc = "${desc.substring(0, rp)}Ljava/lang/String;${if (optimizable) "Z" else ""}${desc.substring(rp)}"
            }
            //method.instructions.insertBefore(init, LdcInsnNode(makeThreadName(klass.className)))
        } ?: throw TransformException("`invokespecial $desc` not found: ${klass.name}.${method.name}${method.desc}")
    }

}

internal const val MARK = "\u200B"

internal const val BOOSTER_INSTRUMENT = "com/didiglobal/booster/instrument/"
internal const val SHADOW = "${BOOSTER_INSTRUMENT}Shadow"
internal const val SHADOW_DIALOG = "${SHADOW}Dialog"
internal const val SHADOW_ALERT_DIALOG = "${SHADOW}AlertDialog"
internal const val SHADOW_APP_COMPAT_DIALOG = "${SHADOW}AppCompatDialog"

internal const val DIALOG = "android/app/Dialog"
internal const val ALERT_DIALOG = "androidx/appcompat/app/AlertDialog"
internal const val APP_COMPAT_DIALOG = "androidx/appcompat/app/AppCompatDialog"
