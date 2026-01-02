package com.marrineer.elyInv.models

import com.marrineer.elyInv.models.data.UsageData

data class ConfigData(
    val prefix: String,
    val storage: String,
    val usageData: UsageData
)
