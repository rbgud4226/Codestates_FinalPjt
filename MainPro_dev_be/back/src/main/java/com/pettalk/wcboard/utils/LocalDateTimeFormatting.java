package com.pettalk.wcboard.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatting {
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 원하는 형식의 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // LocalDateTime을 문자열로 변환
        String formattedDateTime = now.format(formatter);

        return formattedDateTime;
    }
}
