## POC Test for View

触摸任何一个控件，就一定会调用该控件的 `dispatchTouchEvent(...)`方法。

```
public boolean dispatchTouchEvent(MotionEvent event) {
    if (mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&
            mOnTouchListener.onTouch(this, event)) {
        return true;
    }
    // `onTouch(...)` 返回 fale，就一定进入 `onTouchEvent(...)`
    return onTouchEvent(event);
}

public boolean onTouchEvent(MotionEvent event) {
    final int viewFlags = mViewFlags;
    if ((viewFlags & ENABLED_MASK) == DISABLED) {
        // A disabled view that is clickable still consumes the touch
        // events, it just doesn't respond to them.
        return (((viewFlags & CLICKABLE) == CLICKABLE ||
                (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE));
    }
    if (mTouchDelegate != null) {
        if (mTouchDelegate.onTouchEvent(event)) {
            return true;
        }
    }
    if (((viewFlags & CLICKABLE) == CLICKABLE ||
            (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                boolean prepressed = (mPrivateFlags & PREPRESSED) != 0;
                if ((mPrivateFlags & PRESSED) != 0 || prepressed) {
                    // take focus if we don't have it already and we should in
                    // touch mode.
                    boolean focusTaken = false;
                    if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
                        focusTaken = requestFocus();
                    }
                    if (!mHasPerformedLongPress) {
                        // This is a tap, so remove the longpress check
                        removeLongPressCallback();
                        // Only perform take click actions if we were in the pressed state
                        if (!focusTaken) {
                            // Use a Runnable and post this rather than calling
                            // performClick directly. This lets other visual state
                            // of the view update before click actions start.
                            if (mPerformClick == null) {
                                mPerformClick = new PerformClick();
                            }
                            if (!post(mPerformClick)) {
                                performClick();
                            }
                        }
                    }
                    if (mUnsetPressedState == null) {
                        mUnsetPressedState = new UnsetPressedState();
                    }
                    if (prepressed) {
                        mPrivateFlags |= PRESSED;
                        refreshDrawableState();
                        postDelayed(mUnsetPressedState,
                                ViewConfiguration.getPressedStateDuration());
                    } else if (!post(mUnsetPressedState)) {
                        // If the post failed, unpress right now
                        mUnsetPressedState.run();
                    }
                    removeTapCallback();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (mPendingCheckForTap == null) {
                    mPendingCheckForTap = new CheckForTap();
                }
                mPrivateFlags |= PREPRESSED;
                mHasPerformedLongPress = false;
                postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
                break;
            case MotionEvent.ACTION_CANCEL:
                mPrivateFlags &= ~PRESSED;
                refreshDrawableState();
                removeTapCallback();
                break;
            case MotionEvent.ACTION_MOVE:
                final int x = (int) event.getX();
                final int y = (int) event.getY();
                // Be lenient about moving outside of buttons
                int slop = mTouchSlop;
                if ((x < 0 - slop) || (x >= getWidth() + slop) ||
                        (y < 0 - slop) || (y >= getHeight() + slop)) {
                    // Outside button
                    removeTapCallback();
                    if ((mPrivateFlags & PRESSED) != 0) {
                        // Remove any future long press/tap checks
                        removeLongPressCallback();
                        // Need to switch from pressed to not pressed
                        mPrivateFlags &= ~PRESSED;
                        refreshDrawableState();
                    }
                }
                break;
        }
        return true;
    }
    return false;
}

public boolean performClick() {
    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
    if (mOnClickListener != null) {
        playSoundEffect(SoundEffectConstants.CLICK);
        mOnClickListener.onClick(this);
        return true;
    }
    return false;
}
```
当你点击了某个控件，首先会去调用该控件所在布局的 `dispatchTouchEvent(...)` 方法，
如果我们点击了 MyLayout 中的按钮，会先去调用 MyLayout 的 `dispatchTouchEvent` 方法，可是你会发现 MyLayout 中并没有这个方法。
那就再到它的父类 LinearLayout 中找一找，发现也没有这个方法。那只好继续再去 LinearLayout 的父类 ViewGroup 中去找，
你终于在 ViewGroup 中找到了这个方法，按钮的 `dispatchTouchEvent` 方法就是在这里调用的。

```
# ViewGroup.java
public boolean dispatchTouchEvent(MotionEvent ev) {
    final int action = ev.getAction();
    final float xf = ev.getX();
    final float yf = ev.getY();
    final float scrolledXFloat = xf + mScrollX;
    final float scrolledYFloat = yf + mScrollY;
    final Rect frame = mTempRect;
    boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
    if (action == MotionEvent.ACTION_DOWN) {
        if (mMotionTarget != null) {
            mMotionTarget = null;
        }
        if (disallowIntercept || !onInterceptTouchEvent(ev)) {
            ev.setAction(MotionEvent.ACTION_DOWN);
            final int scrolledXInt = (int) scrolledXFloat;
            final int scrolledYInt = (int) scrolledYFloat;
            final View[] children = mChildren;
            final int count = mChildrenCount;
            for (int i = count - 1; i >= 0; i--) {
                final View child = children[i];
                if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE
                        || child.getAnimation() != null) {
                    child.getHitRect(frame);
                    if (frame.contains(scrolledXInt, scrolledYInt)) {
                        final float xc = scrolledXFloat - child.mLeft;
                        final float yc = scrolledYFloat - child.mTop;
                        ev.setLocation(xc, yc);
                        child.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
                        if (child.dispatchTouchEvent(ev))  {
                            mMotionTarget = child;
                            return true;
                        }
                    }
                }
            }
        }
    }
    boolean isUpOrCancel = (action == MotionEvent.ACTION_UP) ||
            (action == MotionEvent.ACTION_CANCEL);
    if (isUpOrCancel) {
        mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
    }
    final View target = mMotionTarget;
    if (target == null) {
        ev.setLocation(xf, yf);
        if ((mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
        }
        return super.dispatchTouchEvent(ev);
    }
    if (!disallowIntercept && onInterceptTouchEvent(ev)) {
        final float xc = scrolledXFloat - (float) target.mLeft;
        final float yc = scrolledYFloat - (float) target.mTop;
        mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
        ev.setAction(MotionEvent.ACTION_CANCEL);
        ev.setLocation(xc, yc);
        if (!target.dispatchTouchEvent(ev)) {
        }
        mMotionTarget = null;
        return true;
    }
    if (isUpOrCancel) {
        mMotionTarget = null;
    }
    final float xc = scrolledXFloat - (float) target.mLeft;
    final float yc = scrolledYFloat - (float) target.mTop;
    ev.setLocation(xc, yc);
    if ((target.mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {
        ev.setAction(MotionEvent.ACTION_CANCEL);
        target.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
        mMotionTarget = null;
    }
    return target.dispatchTouchEvent(ev);
}
```



[requestLayout 竟然涉及到这么多知识点](https://mp.weixin.qq.com/s/moQq-Ga9pSSFP5amYqYkkw)

[requestLayout 这么问？谁能会？](https://mp.weixin.qq.com/s/BeYCM6AUsJ9qzhb5_XAweQ)