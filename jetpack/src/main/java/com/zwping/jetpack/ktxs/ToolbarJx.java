package com.zwping.jetpack.ktxs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * zwping @ 4/21/21
 */
public class ToolbarJx {

    /**
     * 设置Toolbar居中的标题
     * 新增一个TextView, 相对于Toolbar完全居中
     *
     * @param toolbar
     * @param title
     * @return AppCompatTextView
     */
    public static AppCompatTextView setTitleOfCenter(Toolbar toolbar, CharSequence title) {
        if (null == toolbar) { return null; }
        toolbar.setTitle("");
        AppCompatTextView tv = new AppCompatTextView(toolbar.getContext());
        tv.setLayoutParams(new ActionBar.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextAppearance(toolbar.getContext(), android.R.style.TextAppearance_Material_Widget_Toolbar_Title);
        tv.setText(title);
        toolbar.addView(tv);
        return tv;
    }

    /**
     * 设置Toolbar标题居中
     * 不改变原有结构, 相对于本身TextView居中
     *
     * @param toolbar 标题 (注意其执行顺序, Toolbar内部动态addView)
     * @param title
     */
    public static void setTitleCenter(Toolbar toolbar, CharSequence title) {
        if (null == toolbar) { return; }
        toolbar.setTitle(title);
        toolbar.setContentInsetStartWithNavigation(0);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View v = toolbar.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setGravity(Gravity.CENTER);
                v.getLayoutParams().width = MATCH_PARENT;
            }
        }
    }

    /**
     * 快捷增加menu
     * @param toolbar
     * @param itemId menuId 0x01
     * @param iconRes
     * @param title
     * @param actionEnum 展现方式
     *                  总是显示在界面上 MenuItem.SHOW_AS_ACTION_ALWAYS
     *                  不显示在界面上 MenuItem.SHOW_AS_ACTION_NEVER
     *                  如果有位置才显示, 不然就出现在右边的三个点中 MenuItem.SHOW_AS_ACTION_IF_ROOM
     */
    public static void addMenu(Toolbar toolbar, int itemId, @DrawableRes int iconRes, CharSequence title, int actionEnum) {
        if (null == toolbar) { return; }
        addMenu(toolbar.getMenu(), itemId, iconRes, title, actionEnum);
    }
    public static void addMenu(Toolbar toolbar, int itemId, @DrawableRes int iconRes, CharSequence title) {
        addMenu(toolbar, itemId, iconRes, title, MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    public static void addMenu(Menu menu, int itemId, @DrawableRes int iconRes, CharSequence title, int actionEnum) {
        if (null == menu) { return; }
        menu.add(Menu.NONE, itemId, menu.size(), title).setIcon(iconRes).setShowAsAction(actionEnum);
    }

    public static void addMenu(Menu menu, int itemId, @DrawableRes int iconRes, CharSequence title) {
        addMenu(menu, itemId, iconRes, title, MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    /***
     * 快捷增加带角标的menu
     * @param toolbar
     * @param itemId
     * @param iconRes
     * @param title
     * @param lis
     * @return {@link ActionProvider2}
     */
    public static ActionProvider2 addMenuBadge(Toolbar toolbar, int itemId, @DrawableRes int iconRes, CharSequence title, OnActionProvider2ItemClickLis lis) {
        if (null == toolbar) { return null; }
        return addMenuBadge(toolbar.getMenu(), toolbar.getContext(), itemId, iconRes, title, lis);
    }

    public static ActionProvider2 addMenuBadge(Menu menu, Context ctx, int itemId, @DrawableRes int iconRes, CharSequence title, OnActionProvider2ItemClickLis lis) {
        if (null == menu) { return null; }
        MenuItem item = menu.add(Menu.NONE, itemId, menu.size(), title).setIcon(iconRes);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        ActionProvider2 ap2 = new ActionProvider2(ctx, item);
        ap2.setOnMenuItemClickListener(lis);
        MenuItemCompat.setActionProvider(item, ap2);
        return ap2;
    }

    /**
     * 获取带角标的ActionProvider2
     * @param toolbar
     * @param itemId
     * @return
     */
    public static ActionProvider2 getActionProvider2(Toolbar toolbar, int itemId) {
        if (null == toolbar) { return null; }
        return (ActionProvider2) MenuItemCompat.getActionProvider(toolbar.getMenu().findItem(itemId));
    }


    /* ====================== */

    /*** 圆形背景TextView ***/
    public static class BadgeTextView extends AppCompatTextView{

        public BadgeTextView(@NonNull Context context) { super(context); }

        public int color = Color.RED;
        private Paint paint = new Paint();

        @Override
        protected void onDraw(Canvas canvas) {
            paint.setColor(color);
            paint.setAntiAlias(true);
            int w2 = getWidth() / 2;
            canvas.drawCircle(w2, w2, w2, paint);
            super.onDraw(canvas);
        }
    }

    /*** 自定义携带角标的ActionProvider ***/
    public static class ActionProvider2 extends ActionProvider {

        public ActionProvider2(Context context, MenuItem item) { super(context); this.item = item; }

        public MenuItem item;
        public AppCompatImageView iconView;
        public BadgeTextView badgeView;
        public int num;
        private FrameLayout ly;

        @Override
        public View onCreateActionView() {
            ly = new FrameLayout(getContext());
            Resources resources = getContext().getResources();
            int dh = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.abc_action_bar_default_height_material) -
                    (int) (10 * resources.getDisplayMetrics().density);  // 盲减, 约等于默认menuItem宽度
            ly.setLayoutParams(new ViewGroup.LayoutParams(dh, MATCH_PARENT));
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.actionBarItemBackground, typedValue, true);
            ly.setBackgroundResource(typedValue.resourceId);

            iconView = new AppCompatImageView(getContext());    // 增加图标
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dh/2, dh/2);
            lp.gravity = Gravity.CENTER;
            iconView.setLayoutParams(lp);
            iconView.setScaleType(ImageView.ScaleType.CENTER);
            iconView.setImageDrawable(item.getIcon());
            ly.addView(iconView);

            badgeView = new BadgeTextView(getContext());    // 增加badge
            int w = (int) (14 * getContext().getResources().getDisplayMetrics().density);
            lp = new FrameLayout.LayoutParams(w, w);
            lp.gravity = Gravity.CENTER;
            lp.leftMargin = dh/4; lp.bottomMargin = dh/4;
            badgeView.setLayoutParams(lp);
            badgeView.setGravity(Gravity.CENTER);
            badgeView.setTextColor(Color.WHITE);
            badgeView.setTextSize(9F);
            badgeView.setVisibility(View.INVISIBLE);
            ly.addView(badgeView);
            return ly;
        }

        /**
         * 设置角标数值
         * @param num
         * @return
         */
        public ActionProvider2 setBadgeNum(int num) {
            this.num = num;
            badgeView.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
            badgeView.setText(num > 99 ? "99" : num < 0 ? "1" : "" + num);
            return this;
        }

        /**
         * 设置角标是否显示
         * @param visible
         * @return
         */
        public ActionProvider2 setBadgeVisible(boolean visible) {
            badgeView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            return this;
        }


        /**
         * 设置menuItem点击事件
         * @param lis
         * @return
         */
        public ActionProvider2 setOnMenuItemClickListener(OnActionProvider2ItemClickLis lis) {
            ly.setOnClickListener(v -> lis.click(this));
            return this;
        }

    }

    public interface OnActionProvider2ItemClickLis{
        void click(ActionProvider2 ap2);
    }

}
