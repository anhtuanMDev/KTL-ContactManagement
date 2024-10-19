package com.example.contact_manager.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    var name: String,
    var email: String,
    var phone: String
)
