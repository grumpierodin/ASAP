package com.grumpierodin.asap.utils;

import java.util.UUID;

public class UUIDUtils {
    UUID createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    String getUUIDString(UUID uuid) {
        return uuid.toString();
    }

    long getUUIDTimestamp(UUID uuid) {
        return uuid.timestamp();
    }
}
