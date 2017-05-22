package com.sdsmdg.harjot.rotatingtext.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Harjot on 22-May-17.
 */

public class Utils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
