package com.zwping.plibx;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * zwping @ 4/12/21
 */
public class ResourceJx {

    /**
     *  xml shape java代码实现方式
     *
     * @param dw    支持渐变色的Drawable
     * @param shape 线        {@link GradientDrawable#LINE}
     *              圆        {@link GradientDrawable#OVAL}
     *              矩形      {@link GradientDrawable#RECTANGLE}
     *              虚线矩形   {@link GradientDrawable#LINEAR_GRADIENT}
     * @param dsl
     * @return  Drawable
     */
    public static Drawable createGradientDrawable(GradientDrawable dw,
                                                  int shape,
                                                  CommCallback<GradientDrawable> dsl) {
        dw.setShape(shape);
        dsl.callback(dw);
        return dw;
    }


    public static Drawable createGradientDrawable(CommCallback<GradientDrawable> dsl) {
        return createGradientDrawable(new GradientDrawable(), GradientDrawable.RECTANGLE, dsl);
    }


    /**
     * xml selector item drawable java代码实现, 代码创建view不同状态的Drawable
     * @param dw 不同状态的Drawable {@link States}
     * @param ctx
     * @param defaultRes    @DrawableRes or @Drawable
     * @param dsl
     * @param <T>   @DrawableRes or @Drawable
     * @return
     */
    @Deprecated
    public static <T> StateListDrawable createStateListDrawable(StateListDrawable dw,
                                                                @Nullable Context ctx,
                                                                T defaultRes,
                                                                CommCallback<States<T>> dsl) {
        if (null == ctx) return dw;
        States<T> states = new States<T>();
        dsl.callback(states);
        Iterator<Map.Entry<Integer, T>> it = states.map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, T> entry = it.next();
            T v = entry.getValue();
            dw.addState(new int[]{entry.getKey()}, v instanceof Drawable ? (Drawable) v : ContextCompat.getDrawable(ctx, (Integer) v));
        }
        dw.addState(new int[]{}, defaultRes instanceof Drawable ? (Drawable) defaultRes : ContextCompat.getDrawable(ctx, (Integer) defaultRes));
        return dw;
    }


    /*** xml selector item color java代码实现 ***/
    public static class ColorStateList2{
        private List<int[]> ids = new ArrayList<>();
        private List<Integer> colors = new ArrayList<>();

        /**
         *
         * @param defaultColor
         * @param dsl
         */
        public ColorStateList2(@ColorInt int defaultColor,
                               CommCallback<States<Integer>> dsl) {

            States<Integer> states = new States<Integer>();
            dsl.callback(states);
            Iterator<Map.Entry<Integer, Integer>> it = states.map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Integer> entry = it.next();
                ids.add(new int[]{entry.getKey()});
                colors.add(entry.getValue());
            }
            ids.add(new int[]{});
            colors.add(defaultColor);
        }

        public ColorStateList create() {
            int[] c = new int[colors.size()];
            for (int i = 0; i < colors.size(); i++) {
                c[i] = colors.get(i);
            }
            return new ColorStateList(ids.toArray(new int[][]{}), c);
        }
    }


    /*** view状态的实现 ***/
    public static class States<T> {
        HashMap<Integer, T> map = new HashMap<Integer, T>();

        void pressed(@NonNull T value) {
            map.put(android.R.attr.state_pressed, value);
        }
        void focused(@NonNull T value) {
            map.put(android.R.attr.state_focused, value);
        }
        void selected(@NonNull T value) {
            map.put(android.R.attr.state_selected, value);
        }
        void checkable(@NonNull T value) {
            map.put(android.R.attr.state_checkable, value);
        }
        void checked(@NonNull T value) {
            map.put(android.R.attr.state_checked, value);
        }
        void unEnabled(@NonNull T value) {
            map.put(-android.R.attr.state_enabled, value);
        }
        void window_focused(@NonNull T value) { map.put(android.R.attr.state_window_focused, value); }
    }

    public interface CommCallback<T> { void callback(@Nullable T it); }

    private static Float density;

    public static Float getDensity(){
        if (density == null) {
            density = Resources.getSystem().getDisplayMetrics().density;
        }
        return density;
    }

    public static int dp2px(Float value) {
        return (int) (value * getDensity() + 0.5F);
    }

    public static Float px2dp(int value) {
        return value / getDensity();
    }

    public static int toInt2(@Nullable String value) {
        if (value == null || value == "") return 0;
        return Integer.parseInt(value);
    }

    public static int getColor2(Context ctx, @ColorRes int id) {
        return ContextCompat.getColor(ctx, id);
    }

}

