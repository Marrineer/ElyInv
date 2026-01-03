package com.marrineer.elyInv.models

data class StorageReport(
    val type: String,
    val entries: Int,
    val durationMs: Long
)
