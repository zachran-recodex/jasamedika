package com.task.absensi.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class EpochSeconds {
    private EpochSeconds() {
    }

    public static LocalDate toLocalDate(long epochSeconds, ZoneId zoneId) {
        return Instant.ofEpochSecond(epochSeconds).atZone(zoneId).toLocalDate();
    }

    public static long fromLocalDate(LocalDate localDate, ZoneId zoneId) {
        return localDate.atStartOfDay(zoneId).toEpochSecond();
    }
}
