package com.ssafy.soltravel.v1.util;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class LogUtil {

    public static void info(String message, Object... args) {
        log.info(format(message, args));
    }

    public static void debug(String message, Object... args) {
        log.debug(format(message, args));
    }

    public static void warn(String message, Object... args) {
        log.warn(format(message, args));
    }

    public static void error(String message, Object... args) {
        log.error(format(message, args));
    }

    private static String format(String message, Object... args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement caller = null;
        for (int i = 1; i < stackTrace.length; i++) {
            if (!stackTrace[i].getClassName().equals(LogUtil.class.getName())) {
                caller = stackTrace[i];
                break;
            }
        }

        if (caller != null) {
            String className = caller.getClassName();
            String methodName = caller.getMethodName();

            return String.format("[LOG] %s %s - %s : %s", getSimpleClassName(className), methodName, message,
                Arrays.toString(args));
        } else {
            return String.format("[LOG] %s - %s : %s", "UnknownClass", "UnknownMethod", message, Arrays.toString(args));
        }
    }

    private static String getSimpleClassName(String className) {
        int lastDotIndex = className.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < className.length() - 1) {
            return className.substring(lastDotIndex + 1);
        }
        return className;
    }
}
