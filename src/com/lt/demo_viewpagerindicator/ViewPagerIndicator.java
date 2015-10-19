package com.lt.demo_viewpagerindicator;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout {

	private Paint mPaint;
	private Path mPath;
	private int mTriangleWidth;
	private int mTriangleHeight;
	private static final float RATIO_TRIANGLE = 1.0f / 6;
	private final int DIMENSION_TRIANGLE_WIDTH = (int) (getScreenWidth() / 3 * RATIO_TRIANGLE);
	private int mInitTranslationX;
	private float mTranslationX;
	private static final int COUNT_DEFAULT_TAB = 4;
	private int mTabVisibleCount = COUNT_DEFAULT_TAB;
	private List<String> mTabTitles;
	private ViewPager mViewPager;
	private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
	private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFFFFFFFF;

	public ViewPagerIndicator(Context context) {
		this(context, null);
	}

	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.viewpagerindicator);
		mTabVisibleCount = a.getInt(R.styleable.viewpagerindicator_item_count,
				COUNT_DEFAULT_TAB);
		if (mTabVisibleCount < 0) {
			mTabVisibleCount = COUNT_DEFAULT_TAB;
		}
		a.recycle();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#FF0000"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
		canvas.drawPath(mPath, mPaint);
		canvas.restore();
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth = (int) (w / mTabVisibleCount * RATIO_TRIANGLE);
		mTriangleWidth = Math.min(DIMENSION_TRIANGLE_WIDTH, mTriangleWidth);
		mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth
				/ 2;
		initTriangle();
	}
	
	

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		int count=getChildCount();
		if(count==0){
			return;
		}
		for(int i=0;i<count;i++){
			View view=getChildAt(i);
			LinearLayout.LayoutParams lp=(LayoutParams)view .getLayoutParams();
			lp.weight=0;
			lp.width=getScreenWidth()/mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		setItemClickEvent();
	}

	public void setVisibleTabCount(int count) {
		this.mTabVisibleCount = count;
	}

	public void setTabItemTitles(List<String> datas) {
		if (datas != null && datas.size() > 0) {
			this.removeAllViews();
			this.mTabTitles = datas;
			for (String title : mTabTitles) {
				addView(generateTextView(title));
			}
			setItemClickEvent();
		}
	}

	public void setViewPager(ViewPager viewPager, int pos) {
		this.mViewPager = viewPager;
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				resetTextViewColor();
				highLightTextView(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOfsetPixels) {
				scroll(position, positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

		});
		viewPager.setCurrentItem(pos);
		highLightTextView(pos);
	}

	/**
	 * 根据位置和偏移量设置指示器的位置以及title的位置
	 * @param position
	 * @param offset
	 */
	private void scroll(int position, float offset) {
		mTranslationX = getWidth() / mTabVisibleCount * (position + offset);
		int tabWidth = getScreenWidth() / mTabVisibleCount;
		if (offset > 0 && position >= (mTabVisibleCount - 2)
				&& getChildCount() > mTabVisibleCount
				&& position < (getChildCount() - 2)) {
			if (mTabVisibleCount != 1) {
				this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth
						+ (int) (tabWidth * offset), 0);
			}else{
				this.scrollTo(position*tabWidth+(int)(tabWidth*offset), 0);
			}
		}
		invalidate();
	}

	protected void highLightTextView(int position) {
		View view = getChildAt(position);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
		}
	}

	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}

	private void setItemClickEvent() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

	//代码生成textview
	private View generateTextView(String title) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setText(title);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setLayoutParams(lp);
		return tv;
	}

	//初始化指示器图形
	private void initTriangle() {
		mPath = new Path();
		mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
		mPath.moveTo(-mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth*2, 0);
		mPath.lineTo(mTriangleWidth *2, -mTriangleHeight);
		mPath.lineTo(-mTriangleWidth,-mTriangleHeight);
		mPath.close();
	}

}
