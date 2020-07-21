package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

class NavigationView extends View {
    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
