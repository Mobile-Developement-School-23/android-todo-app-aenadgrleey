package com.aenadgrleey.data.remote

/*
Enum class to indicate state of the network
 */
enum class NetworkStatus {
    SYNCED,
    SYNCING,
    NO_INTERNET,
    PENDING,
    SERVER_ERROR,
    SERVER_INTERNAL_ERROR
}