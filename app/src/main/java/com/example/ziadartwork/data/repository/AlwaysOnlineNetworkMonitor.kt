package com.example.ziadartwork.data.repository

import com.example.ziadartwork.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AlwaysOnlineNetworkMonitor: NetworkMonitor {
    override val isOnline: Flow<Boolean> =
        flowOf(true)

}