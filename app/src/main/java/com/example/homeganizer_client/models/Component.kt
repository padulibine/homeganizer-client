package com.example.homeganizer_client.models

data class Component(
    val name: String? = null,
    var description: String? = null,
    var path: String? = null,
    val objects: Array<Component>? = null,
    var tag: String? = null
)
