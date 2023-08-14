package com.rko;

import android.content.Context;
import android.view.View;

public class MyView extends View {
    int mIndex = 0;

    public MyView(Context context, int index) {
        super(context);
        mIndex = index;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (mIndex == 10000) {
                Thread.sleep(20000);
            }
        } finally {
            super.finalize();
        }
    }
}
