package com.betalpha.data

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MockkTest {
    val service = mockk<TestableService>()

    @Test
    fun givenServiceMock_whenCallingMockedMethod_thenCorrectlyVerified() {
        // given
        val service = mockk<TestableService>()
        every { service.getDataFromDb("Expected Param") } returns "Expected Output"

        // when
        val result = service.getDataFromDb("Expected Param")

        // then
        verify { service.getDataFromDb("Expected Param") }
        println("test-test-test-test-test-test-test-test-")
        assertEquals("Expected Output", result)
    }
}

/**
 *  注入测试  InjectMockKs
 */
class AnnotationMockKUnitTest {

    @MockK
    lateinit var service1: TestableService

    @MockK
    lateinit var service2: TestableService

    @InjectMockKs
    var objectUnderTest = InjectTestService()

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun test_inject() {
        every { service1.getDataFromDb(any()) } returns "inject mock".intern()
        val invokeService1 = objectUnderTest.invokeService1()
        assertEquals(invokeService1, "inject mock".intern())
    }
    /**
     * spy
     */
    @Test
    fun givenServiceSpy_whenMockingOnlyOneMethod_thenOtherMethodsShouldBehaveAsOriginalObject() {
        // given
        val service = spyk<TestableService>() // spy 哪些方法被mock了，就代理哪些方法，哪些方法没有被mock就调用原来的方法
        every { service.getDataFromDb(any()) } returns "Mocked Output"

        // when checking mocked method
        val firstResult = service.getDataFromDb("Any Param")

        // then
        assertEquals("Mocked Output", firstResult)

        // when checking not mocked method
        val secondResult = service.doSomethingElse("Any Param")

        // then
        assertEquals("I don't want to!", secondResult)
    }

    @Test
    fun givenRelaxedMock_whenCallingNotMockedMethod_thenReturnDefaultValue() {
        // given ,
        // relaxed  参数 表示 如果
        val service = mockk<TestableService>(relaxed = true)

        // when
        val result = service.getDataFromDb("Any Param")

        // then
        assertEquals("", result)
    }
}

/**
 * spy
 */
class SpyKunitTest{
    @SpyK
    lateinit var service: TestableService

    @Test
    fun test_spy(){
        every { service.getDataFromDb(any()) } returns "Mocked Output"

        // when checking mocked method
        val firstResult = service.getDataFromDb("Any Param")

        // then
        assertEquals("Mocked Output", firstResult)

        // when checking not mocked method
        val secondResult = service.doSomethingElse("Any Param")

        // then
        assertEquals("I don't want to!", secondResult)
    }
}






class InjectTestService {
    lateinit var service1: TestableService
    lateinit var service2: TestableService

    fun invokeService1(): String {
        return service1.getDataFromDb("Test Param")
    }
}

class TestableService {
    fun getDataFromDb(testParameter: String): String {
        return ""
    }

    fun doSomethingElse(testParameter: String): String {
        return "I don't want to!"
    }
}

