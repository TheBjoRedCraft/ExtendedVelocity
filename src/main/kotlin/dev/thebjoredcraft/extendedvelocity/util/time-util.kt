package dev.thebjoredcraft.extendedvelocity.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

val timeFormatter: DateTimeFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy, HH:mm:ss", Locale.GERMANY)
    .withZone(ZoneId.of("Europe/Berlin"))

fun formatTime(millis: Long): String {
    return timeFormatter.format(Instant.ofEpochMilli(millis))
}