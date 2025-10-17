package com.jessevandenberghe.milkyway

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform