package com.richardamare.classroombackend.core.event

import org.springframework.context.ApplicationEvent

object Event {
    class TenantCreated(
        var id: String
    ) : ApplicationEvent(Object())
}
