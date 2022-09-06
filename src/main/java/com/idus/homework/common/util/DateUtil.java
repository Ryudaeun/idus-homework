package com.idus.homework.common.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    /**
     *  데이터 저장 시 UTC
     *  데이터 조회 시 System Default Zone
     */

    private static final ZoneId SAVE_TIME_ZONE = ZoneId.of("UTC");
    private static final ZoneId READ_TIME_ZONE = ZoneId.systemDefault();
    private static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getStringWithZone(ZonedDateTime datetime) {
        if (datetime == null) return "";
        return datetime.format(DEFAULT_FORMATTER.withZone(READ_TIME_ZONE));
    }

    public static ZonedDateTime getNow() {
        return ZonedDateTime.now(SAVE_TIME_ZONE);
    }

    public static ZonedDateTime parseDateTime(String string) {
        if (string.isBlank()) return null;
        return ZonedDateTime.parse(string, DEFAULT_FORMATTER.withZone(SAVE_TIME_ZONE));
    }
}
