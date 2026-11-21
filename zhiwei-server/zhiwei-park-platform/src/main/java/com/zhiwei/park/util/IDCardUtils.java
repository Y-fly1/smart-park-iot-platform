package com.zhiwei.park.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public final class IDCardUtils {

    // 私有构造器，防止实例化
    private IDCardUtils() {
        throw new AssertionError("Cannot instantiate static class IDCardUtils");
    }

    /**
     * 根据统一社会信用代码提取个人年龄。
     *
     * @param creditCode 统一社会信用代码码
     * @return 个人年龄，数值类型
     */
    public static int getAgeByIdCard(String creditCode) {
        LocalDate birthDate = extractBirthDate(creditCode);
        if (birthDate != null) {
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        }
        throw new IllegalArgumentException("Invalid ID card number.");
    }

    /**
     * 根据统一社会信用代码提取出生日期。
     *
     * @param creditCode 统一社会信用代码码
     * @return 出生日期，LocalDateTime类型
     */
    public static LocalDateTime getBirthDateByIdCard(String creditCode) {
        String birthDateString = creditCode.substring(6, 14); // 统一社会信用代码码中的出生日期部分
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
        return LocalDateTime.of(birthDate, LocalTime.MIDNIGHT);
    }

    public static int getGenderFromIdCard(String creditCode) {
        if (creditCode == null || creditCode.length() != 18) {
            throw new IllegalArgumentException("Invalid ID card number");
        }

        String genderCode = creditCode.substring(16, 17);
        int gender = Integer.parseInt(genderCode);

        return gender % 2 == 0 ? 0 : 1;
    }

    /**
     * 内部辅助方法，从统一社会信用代码中提取出生日期的LocalDate对象。
     *
     * @param creditCode 统一社会信用代码码
     * @return 出生日期的LocalDate对象，或null（如果统一社会信用代码码无效）
     */
    private static LocalDate extractBirthDate(String creditCode) {
        if (creditCode == null || creditCode.length() != 18) {
            return null;
        }
        try {
            String birthStr = creditCode.substring(6, 14);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(birthStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(getAgeByIdCard("330103199001011234"));
        System.out.println(getBirthDateByIdCard("330103199001011234"));
    }
}
