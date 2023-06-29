package com.example.ziadartwork

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ziadartwork.domain.repository.CartRepository
import com.example.ziadartwork.data.implementations.CartRepositoryImpl
import com.example.ziadartwork.domain.repository.CartDataStore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


private const val TEST_DATASTORE_NAME: String = "test_datastore"

class CartRepoTest() {

    private val testContext: Context =
        InstrumentationRegistry.getInstrumentation().targetContext
    private val testCoroutineDispatcher: TestCoroutineDispatcher =
        TestCoroutineDispatcher()
    private val testCoroutineScope =
        TestCoroutineScope(testCoroutineDispatcher + Job())
    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile =
            { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
        )

    private val cartDataStore: CartDataStore = TestCartDataStore()
    private val repository: CartRepository = CartRepositoryImpl(cartDataStore)

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun repository_testWriteSortByDeadline() = testCoroutineScope.runBlockingTest {
        repository.addToCart("33333")
        val cartContent = repository.getCartContent().first()
        assertEquals(1, cartContent["33333"])
    }

    @Test
    fun repository_testFetchInitialPreferences() = runBlocking {
        val cartContent = repository.getCartContent().first()
        assertTrue(cartContent.isEmpty())
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        testCoroutineScope.runBlockingTest {
            testDataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }

}
