package com.sdl.lt.gateway.commons.db.domain.listener;

import java.util.HashMap;
import java.util.Map;

public class BatchQuery {
    // key - type, value - ids list
    private static final ThreadLocal<Map> THREAD_LOCAL = new ThreadLocal<Map>();

    public static void beginBatchQuery() {
        Map dependencyBatch = new HashMap();
        THREAD_LOCAL.set(dependencyBatch);
    }

    public static void endBatchQuery() {
        THREAD_LOCAL.remove();
    }

    public static boolean isBatchQuery() {
        return THREAD_LOCAL.get() != null;
    }
}
