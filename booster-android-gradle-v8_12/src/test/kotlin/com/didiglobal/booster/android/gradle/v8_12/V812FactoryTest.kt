package com.didiglobal.booster.android.gradle.v8_12

import com.android.repository.Revision
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for V812Factory to verify AGP 8.12.0 compatibility
 */
class V812FactoryTest {

    private val factory = V812Factory()

    @Test
    fun `test factory revision is 8_12_0`() {
        val revision = factory.revision
        assertEquals(8, revision.major, "Major version should be 8")
        assertEquals(12, revision.minor, "Minor version should be 12")
        assertEquals(0, revision.micro, "Micro version should be 0")
    }

    @Test
    fun `test factory creates V812 interface`() {
        val agpInterface = factory.newAGPInterface()
        assertNotNull(agpInterface, "AGPInterface should not be null")
        assertTrue(agpInterface === V812, "Factory should return V812 singleton")
    }

    @Test
    fun `test revision matches expected AGP version`() {
        val expectedRevision = Revision(8, 12, 0)
        assertEquals(expectedRevision, factory.revision, "Factory revision should match expected AGP 8.12.0")
    }
}
