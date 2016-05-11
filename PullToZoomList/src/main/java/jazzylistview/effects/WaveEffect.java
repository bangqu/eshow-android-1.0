package jazzylistview.effects;

import android.view.View;
import android.view.ViewPropertyAnimator;

import jazzylistview.JazzyEffect;

public class WaveEffect implements JazzyEffect {

    @Override
    public void initView(View item, int position, int scrollDirection) {
        item.setTranslationX(item.getWidth());
    }

    public void initView(View item,int xPoint){
        item.setTranslationX(xPoint);
    }
    @Override
    public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimator animator) {
        animator.translationX(0);
    }
}
