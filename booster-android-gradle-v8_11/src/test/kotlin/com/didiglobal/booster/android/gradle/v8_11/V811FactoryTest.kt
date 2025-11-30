package com.didiglobal.booster.android.gradle.v8_11

import com.android.repository.Revision
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for V811Factory to verify AGP 8.11.0 compatibility
 */
class V811FactoryTest {

    private val factory = V811Factory()

    @Test
    fun `test factory revision is 8_11_0`() {
        val revision = factory.revision
        assertEquals(8, revision.major, "Major version should be 8")
        assertEquals(11, revision.minor, "Minor version should be 11")
        assertEquals(0, revision.micro, "Micro version should be 0")
    }

    @Test
    fun `test factory creates V811 interface`() {
        val agpInterface = factory.newAGPInterface()
        assertNotNull(agpInterface, "AGPInterface should not be null")
        assertTrue(agpInterface === V811, "Factory should return V811 singleton")
    }

    @Test
    fun `test revision matches expected AGP version`() {
        val expectedRevision = Revision(8, 11, 0)
        assertEquals(expectedRevision, factory.revision, "Factory revision should match expected AGP 8.11.0")
    }
}
