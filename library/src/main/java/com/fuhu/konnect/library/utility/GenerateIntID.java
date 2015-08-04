package com.fuhu.konnect.library.utility;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jacktseng on 2015/6/8.
 *
 * This class is goals to solve create view's id when API lower 17.
 * The code is copy from Android source code.
 */
public class GenerateIntID {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in View.setId(int id).
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

}
