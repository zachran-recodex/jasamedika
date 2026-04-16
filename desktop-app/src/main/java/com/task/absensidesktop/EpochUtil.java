package com.task.absensidesktop;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class EpochUtil {
    private EpochUtil() {
    }

    public static long localDateToEpochSeconds(LocalDate localDate, ZoneId zoneId) {
        return localDate.atStartOfDay(zoneId).toEpochSecond();
    }

    public static LocalDate epochSecondsToLocalDate(long epochSeconds, ZoneId zoneId) {
        return Instant.ofEpochSecond(epochSeconds).atZone(zoneId).toLocalDate();
    }
}

