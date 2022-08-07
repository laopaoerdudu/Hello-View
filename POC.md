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

我们都知道如果给一个控件注册了 touch 事件，每次点击它的时候都会触发一系列的 `ACTION_DOWN`，`ACTION_MOVE`，`ACTION_UP` 等事件。
这里需要注意，如果你在执行 `ACTION_DOWN` 的时候返回了 false，后面一系列其它的 action 就不会再得到执行了。简单的说，
就是当 `dispatchTouchEvent` 在进行事件分发的时候，只有前一个 action 返回 true，才会触发后一个 action。




[requestLayout 竟然涉及到这么多知识点](https://mp.weixin.qq.com/s/moQq-Ga9pSSFP5amYqYkkw)

[requestLayout 这么问？谁能会？](https://mp.weixin.qq.com/s/BeYCM6AUsJ9qzhb5_XAweQ)