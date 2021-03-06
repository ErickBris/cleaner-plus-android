package com.ferdi.cleaner.widget.stikkyheader.core.animator;


import com.ferdi.cleaner.widget.stikkyheader.core.HeaderAnimator;

public class BaseStickyHeaderAnimator extends HeaderAnimator {

    private float mTransactionRatio;

    @Override
    protected void onAnimatorAttached() {
        //nothing to do
    }

    @Override
    protected void onAnimatorReady() {
        //nothing to do
    }

    @Override
    public void onScroll(int scrolledY) {

        getHeader().setTranslationY(Math.max(scrolledY, getMaxTransaction()));

        mTransactionRatio = calculateTransactionRatio(scrolledY);
    }

    public float getTranslationRatio() {
        return mTransactionRatio;
    }

    private float calculateTransactionRatio(int scrolledY) {
        return (float) scrolledY / (float) getMaxTransaction();
    }

}
