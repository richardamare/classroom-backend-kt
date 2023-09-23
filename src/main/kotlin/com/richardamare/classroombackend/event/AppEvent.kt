package com.richardamare.classroombackend.event

sealed class AppEvent(val eventName: String) {

    data class TenantCreated(
        var id: String
    ) : AppEvent("tenant.created")

    data class EmailCredentials(
        var email: String,
        var password: String
    ) : AppEvent("email.credentials")

    data class EmailWelcome(
        var email: String,
        var name: String
    ) : AppEvent("email.welcome")
}