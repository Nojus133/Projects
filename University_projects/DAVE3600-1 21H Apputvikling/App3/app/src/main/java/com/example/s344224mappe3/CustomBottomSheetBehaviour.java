package com.example.s344224mappe3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class CustomBottomSheetBehaviour<V extends View> extends BottomSheetBehavior {
    public CustomBottomSheetBehaviour() {
        super();
    }

    public CustomBottomSheetBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent event) {
        return false;
        /*
        NestedScrollView nestedScrollView = null;
        for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
            if (((ViewGroup) child).getChildAt(i) instanceof NestedScrollView) {
                nestedScrollView = (NestedScrollView) ((ViewGroup) child).getChildAt(i);
            }
        }

        if (nestedScrollView != null) {
            float x = event.getX();
            float y = event.getY();
            int[] position = new int[2];
            nestedScrollView.getLocationOnScreen(position);
            float scrollViewX = position[0];
            float scrollViewY = position[1];

            float boundLeft = scrollViewX;
            float boundRight = scrollViewX + nestedScrollView.getWidth();
            float boundTop = scrollViewY;
            float boundBottom = scrollViewY + nestedScrollView.getHeight();

            if ((x > boundLeft && x < boundRight && y > boundTop && y < boundBottom) || event.getAction() == MotionEvent.ACTION_CANCEL) {
                return false;
            }
        }
        */
        //return super.onInterceptTouchEvent(parent, child, event);
    }
}
