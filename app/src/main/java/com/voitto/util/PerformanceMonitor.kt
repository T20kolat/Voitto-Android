package com.voitto.util

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

object PerformanceMonitor {
    
    private const val TAG = "PerformanceMonitor"
    
    fun <T> measureTime(operation: String, block: () -> T): T {
        var result: T
        val time = measureTimeMillis {
            result = block()
        }
        Log.d(TAG, "$operation took ${time}ms")
        return result
    }
    
    suspend fun <T> measureSuspendTime(operation: String, block: suspend () -> T): T {
        var result: T
        val time = measureTimeMillis {
            result = block()
        }
        Log.d(TAG, "$operation took ${time}ms")
        return result
    }
    
    fun <T> Flow<T>.logPerformance(operation: String): Flow<T> = flow {
        val time = measureTimeMillis {
            collect { emit(it) }
        }
        Log.d(TAG, "$operation flow completed in ${time}ms")
    }
    
    fun logMemoryUsage(operation: String) {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        val maxMemory = runtime.maxMemory()
        val memoryPercent = (usedMemory * 100 / maxMemory).toInt()
        
        Log.d(TAG, "$operation - Memory: ${usedMemory / 1024 / 1024}MB / ${maxMemory / 1024 / 1024}MB ($memoryPercent%)")
    }
}
