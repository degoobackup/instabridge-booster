package com.didiglobal.booster.transform.asm

import com.didiglobal.booster.transform.AbstractKlassPool
import com.didiglobal.booster.transform.ArtifactManager
import com.didiglobal.booster.transform.Collector
import com.didiglobal.booster.transform.KlassPool
import com.didiglobal.booster.transform.TransformContext
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.VarInsnNode
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/**
 * Tests that prove COMPUTE_FRAMES is necessary for correct bytecode after
 * transformations that alter control flow.
 *
 * The test generates a Java 8 class with a method containing branching control
 * flow, then applies a transformer that inserts a new branch. This invalidates
 * the original StackMapTable frames. With COMPUTE_MAXS the frames are stale and
 * the JVM rejects the class with VerifyError. With COMPUTE_FRAMES (via
 * BoosterClassWriter) the frames are recomputed and the class loads correctly.
 */
class ComputeFramesTest {
    /**
     * A minimal [TransformContext] for tests that don't need the full Android
     * build environment.
     */
    private class TestTransformContext : TransformContext {
        override val name: String = "test"
        override val projectDir: File = File(System.getProperty("java.io.tmpdir"))
        override val buildDir: File = File(projectDir, "build")
        override val temporaryDir: File = File(buildDir, "tmp")
        override val reportsDir: File = File(buildDir, "reports")
        override val bootClasspath: Collection<File> = emptyList()
        override val compileClasspath: Collection<File> = emptyList()
        override val runtimeClasspath: Collection<File> = emptyList()
        override val artifacts: ArtifactManager = object : ArtifactManager {}
        override val dependencies: Collection<String> = emptyList()
        override val klassPool: KlassPool = object : AbstractKlassPool(emptyList()) {}
        override val applicationId: String = "com.test"
        override val originalApplicationId: String = "com.test"
        override val isDebuggable: Boolean = true
        override val isDataBindingEnabled: Boolean = false

        override fun hasProperty(name: String): Boolean = false

        override fun <R> registerCollector(collector: Collector<R>) {}

        override fun <R> unregisterCollector(collector: Collector<R>) {}
    }

    /**
     * Generates bytecode for a class equivalent to:
     *
     * ```java
     * public class Sample {
     *     public static Object choose(boolean flag) {
     *         Object result;
     *         if (flag) {
     *             result = "hello";
     *         } else {
     *             result = Integer.valueOf(42);
     *         }
     *         return result;
     *     }
     * }
     * ```
     *
     * This method has an if/else branch where String and Integer merge into
     * Object at the join point, producing a StackMapTable frame.
     */
    private fun generateSampleBytecode(className: String = "Sample"): ByteArray {
        val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null)

        // Default constructor
        val init = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
        init.visitCode()
        init.visitVarInsn(Opcodes.ALOAD, 0)
        init.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        init.visitInsn(Opcodes.RETURN)
        init.visitMaxs(0, 0)
        init.visitEnd()

        // public static Object choose(boolean flag)
        val mv =
            cw.visitMethod(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                "choose",
                "(Z)Ljava/lang/Object;",
                null,
                null,
            )
        mv.visitCode()
        val elseLabel = Label()
        val endLabel = Label()
        // if (flag)
        mv.visitVarInsn(Opcodes.ILOAD, 0)
        mv.visitJumpInsn(Opcodes.IFEQ, elseLabel)
        // result = "hello"
        mv.visitLdcInsn("hello")
        mv.visitVarInsn(Opcodes.ASTORE, 1)
        mv.visitJumpInsn(Opcodes.GOTO, endLabel)
        // else: result = Integer.valueOf(42)
        mv.visitLabel(elseLabel)
        mv.visitIntInsn(Opcodes.BIPUSH, 42)
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
        mv.visitVarInsn(Opcodes.ASTORE, 1)
        // end: return result
        mv.visitLabel(endLabel)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitInsn(Opcodes.ARETURN)
        mv.visitMaxs(0, 0)
        mv.visitEnd()

        cw.visitEnd()
        return cw.toByteArray()
    }

    /**
     * Generates bytecode for a class where two different array types merge into
     * an Object-typed local at the join point, forcing ASM to resolve array
     * descriptors while recomputing frames.
     */
    private fun generateArraySampleBytecode(className: String = "ArraySample"): ByteArray {
        val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null)

        val init = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
        init.visitCode()
        init.visitVarInsn(Opcodes.ALOAD, 0)
        init.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        init.visitInsn(Opcodes.RETURN)
        init.visitMaxs(0, 0)
        init.visitEnd()

        val mv =
            cw.visitMethod(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                "chooseArray",
                "(Z)Ljava/lang/Object;",
                null,
                null,
            )
        mv.visitCode()
        val elseLabel = Label()
        val endLabel = Label()
        mv.visitVarInsn(Opcodes.ILOAD, 0)
        mv.visitJumpInsn(Opcodes.IFEQ, elseLabel)
        mv.visitInsn(Opcodes.ICONST_1)
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String")
        mv.visitVarInsn(Opcodes.ASTORE, 1)
        mv.visitJumpInsn(Opcodes.GOTO, endLabel)
        mv.visitLabel(elseLabel)
        mv.visitInsn(Opcodes.ICONST_1)
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Integer")
        mv.visitVarInsn(Opcodes.ASTORE, 1)
        mv.visitLabel(endLabel)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitInsn(Opcodes.ARETURN)
        mv.visitMaxs(0, 0)
        mv.visitEnd()

        cw.visitEnd()
        return cw.toByteArray()
    }

    /**
     * Builds a method with explicit frame entries that merge a loadable JDK type and
     * a deliberately missing application type. This lets us exercise
     * [BoosterClassWriter.getCommonSuperClass] without needing the missing type to be
     * present on the test runtime classpath.
     */
    private fun generateMissingTypeMergeBytecode(className: String = "MissingTypeSample"): ByteArray {
        val missingType = "com/example/MissingType"
        val cw = ClassWriter(0)
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null)

        val init = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
        init.visitCode()
        init.visitVarInsn(Opcodes.ALOAD, 0)
        init.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        init.visitInsn(Opcodes.RETURN)
        init.visitMaxs(1, 1)
        init.visitEnd()

        val mv = cw.visitMethod(Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC, "chooseMissing", "(Z)Ljava/lang/Object;", null, null)
        mv.visitCode()
        val elseLabel = Label()
        val endLabel = Label()

        mv.visitVarInsn(Opcodes.ILOAD, 0)
        mv.visitJumpInsn(Opcodes.IFEQ, elseLabel)
        mv.visitLdcInsn("hello")
        mv.visitVarInsn(Opcodes.ASTORE, 1)
        mv.visitJumpInsn(Opcodes.GOTO, endLabel)

        mv.visitLabel(elseLabel)
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
        mv.visitInsn(Opcodes.ACONST_NULL)
        mv.visitTypeInsn(Opcodes.CHECKCAST, missingType)
        mv.visitVarInsn(Opcodes.ASTORE, 1)

        mv.visitLabel(endLabel)
        mv.visitFrame(Opcodes.F_APPEND, 1, arrayOf("java/lang/Object"), 0, null)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitInsn(Opcodes.ARETURN)
        mv.visitMaxs(1, 2)
        mv.visitEnd()

        cw.visitEnd()
        return cw.toByteArray()
    }

    /**
     * A transformer that injects a new branch at the beginning of the "choose"
     * method, changing the control flow graph and invalidating the original
     * StackMapTable.
     *
     * The injected code is equivalent to prepending:
     * ```java
     * if (flag) {
     *     System.lineSeparator(); // side-effect free call, result discarded
     * }
     * ```
     *
     * This adds a new branch target that the original StackMapTable does not
     * account for, making the frames invalid.
     */
    private fun createBranchInjectingTransformer(): ClassTransformer {
        return object : ClassTransformer {
            override fun transform(
                context: TransformContext,
                klass: ClassNode,
            ): ClassNode {
                val method = klass.methods.find { it.name == "choose" } ?: return klass
                val insns = InsnList()

                val skipLabel = LabelNode()
                // if (!flag) goto skip
                insns.add(VarInsnNode(Opcodes.ILOAD, 0))
                insns.add(JumpInsnNode(Opcodes.IFEQ, skipLabel))
                // System.lineSeparator() — a harmless static call
                insns.add(
                    MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "java/lang/System",
                        "lineSeparator",
                        "()Ljava/lang/String;",
                        false,
                    ),
                )
                insns.add(InsnNode(Opcodes.POP))
                insns.add(skipLabel)

                method.instructions.insert(insns)
                return klass
            }
        }
    }

    /**
     * Transforms bytecode using a plain ClassWriter(COMPUTE_MAXS) — the old
     * approach. Returns the resulting bytecode.
     */
    private fun transformWithComputeMaxs(
        original: ByteArray,
        transformer: ClassTransformer,
    ): ByteArray {
        val klass = ClassNode().also { ClassReader(original).accept(it, 0) }
        val context = TestTransformContext()
        transformer.transform(context, klass)
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        klass.accept(writer)
        return writer.toByteArray()
    }

    /**
     * Transforms bytecode using BoosterClassWriter(COMPUTE_FRAMES) — the new
     * approach. Returns the resulting bytecode.
     */
    private fun transformWithComputeFrames(
        original: ByteArray,
        transformer: ClassTransformer,
        classLoader: ClassLoader = this::class.java.classLoader,
    ): ByteArray {
        val klass = ClassNode().also { ClassReader(original).accept(it, 0) }
        val context = TestTransformContext()
        transformer.transform(context, klass)
        val klassPool =
            object : AbstractKlassPool(emptyList()) {
                override val classLoader: ClassLoader = classLoader
            }
        val writer = BoosterClassWriter(ClassWriter.COMPUTE_FRAMES, klassPool)
        klass.accept(writer)
        return writer.toByteArray()
    }

    private fun addExtraStackFrameMerge(methodName: String): ClassTransformer {
        return object : ClassTransformer {
            override fun transform(
                context: TransformContext,
                klass: ClassNode,
            ): ClassNode {
                val method = klass.methods.find { it.name == methodName } ?: return klass
                method.instructions.insert(InsnNode(Opcodes.NOP))
                return klass
            }
        }
    }

    /**
     * Attempts to define and resolve a class from bytecode using the JVM's
     * bytecode verifier. Throws [VerifyError] if the StackMapTable is invalid.
     */
    private fun verifyBytecode(
        name: String,
        bytecode: ByteArray,
    ): Class<*> {
        val loader =
            object : ClassLoader(this::class.java.classLoader) {
                fun definePublicClass(
                    name: String,
                    b: ByteArray,
                ): Class<*> = defineClass(name, b, 0, b.size)
            }
        loader.definePublicClass(name, bytecode)
        // Force full verification by resolving the class
        return Class.forName(name, true, loader)
    }

    @Test
    fun `COMPUTE_MAXS produces invalid frames after control flow transformation`() {
        val original = generateSampleBytecode()
        val transformer = createBranchInjectingTransformer()
        val transformed = transformWithComputeMaxs(original, transformer)

        assertFailsWith<VerifyError>("Expected VerifyError with COMPUTE_MAXS after control flow change") {
            verifyBytecode("Sample", transformed)
        }
    }

    @Test
    fun `COMPUTE_FRAMES produces valid frames after control flow transformation`() {
        val original = generateSampleBytecode("SampleFixed")
        val transformer = createBranchInjectingTransformer()
        val transformed = transformWithComputeFrames(original, transformer)

        val clazz = verifyBytecode("SampleFixed", transformed)
        assertNotNull(clazz)

        // Also verify the method is callable and returns correct results
        val method = clazz.getMethod("choose", Boolean::class.java)
        val result = method.invoke(null, true)
        assertNotNull(result)
    }

    @Test
    fun `COMPUTE_FRAMES handles array merges when resolving common superclasses`() {
        val original = generateArraySampleBytecode("ArraySampleFixed")
        val transformer = createBranchInjectingTransformer()

        val transformed = transformWithComputeFrames(original, transformer)

        val clazz = verifyBytecode("ArraySampleFixed", transformed)
        val method = clazz.getMethod("chooseArray", Boolean::class.java)
        assertNotNull(method.invoke(null, true))
        assertNotNull(method.invoke(null, false))
    }

    @Test
    fun `COMPUTE_FRAMES falls back to Object when merged type is unresolved`() {
        val original = generateMissingTypeMergeBytecode()
        val transformer = addExtraStackFrameMerge("chooseMissing")
        val isolatedClassLoader =
            object : ClassLoader(null) {
                override fun loadClass(
                    name: String,
                    resolve: Boolean,
                ): Class<*> {
                    if (name == "java.lang.Object" || name == "java.lang.String") {
                        return ComputeFramesTest::class.java.classLoader.loadClass(name)
                    }
                    throw ClassNotFoundException(name)
                }
            }

        val transformed = transformWithComputeFrames(original, transformer, isolatedClassLoader)
        assertNotNull(transformed)

        val method =
            ClassNode()
                .also { ClassReader(transformed).accept(it, 0) }
                .methods
                .find { it.name == "chooseMissing" }
        assertNotNull(method)
    }
}
