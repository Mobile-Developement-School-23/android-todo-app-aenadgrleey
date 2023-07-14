package com.aenadgrleey.core.domain

/*
Enum class to indicate state of the network
 */
enum class NetworkStatus {
    SYNCED,
    SYNCING,
    NO_INTERNET,
    SERVER_ERROR,
    SERVER_INTERNAL_ERROR
}