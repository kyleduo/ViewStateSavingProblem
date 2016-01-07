package com.kyleduo.problem.viewstatesaving.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kyleduo.problem.viewstatesaving.R;
import com.kyleduo.problem.viewstatesaving.utils.SoftInputUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 基本的列表item
 * Created by kyle on 15/9/8.
 */
public class BaseListItem extends RelativeLayout {

	@Bind(R.id.base_list_item_text_tv)
	TextView mTitleTv;
	TextView mDescTv;
	@Bind(R.id.base_list_item_desc_wrapper)
	LinearLayout mDescWrapper;
	@Bind(R.id.base_list_item_icon_iv)
	ImageView mIconIv;
	@Bind(R.id.base_list_item_arrow_iv)
	ImageView mArrowIv;
	@Bind(R.id.base_list_item_right_icon_iv)
	ImageView mRightIconIv;

	private boolean mDescEditable = false;
	private boolean mShowBottomLine;
	private boolean mShowTopLine;
	private float mBottomLineWidth;
	private int mBottomLineColor;
	private int mBottomLinePaddingStart, mBottomLinePaddingEnd;
	private Paint mPaint;
	private CharSequence mHint;
	private OnRightIconClickListener mOnRightIconClickListener;

	public BaseListItem(Context context) {
		super(context);
		init(null);
	}

	public BaseListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public BaseListItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	protected void init(AttributeSet attrs) {
		mBottomLineWidth = getResources().getDimension(R.dimen.divider_width);
		mBottomLineColor = getResources().getColor(R.color.theme_list_divider_color);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		LayoutInflater.from(getContext()).inflate(R.layout.layout_base_list_item_view, this, true);
		ButterKnife.bind(this);

		Resources resources = getContext().getResources();
		TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.BaseListItem);


		int paddingHorizontal = (int) resources.getDimension(R.dimen.base_space_large_1_5);
		int paddingVertical = (int) resources.getDimension(R.dimen.base_space_small);
		int minHeight = (int) resources.getDimension(R.dimen.min_list_item_height);
		boolean showArrow = true;
		String titleText = "";
		int descTextColor = resources.getColor(R.color.text_color_body);
		int titleTextColor = descTextColor;
		Drawable icon = null;
		String descText = "";
		Drawable rightIcon = null;
		String descHint = "";
		float descTextSize = resources.getDimension(R.dimen.text_size_body);
		float titleTextSize = resources.getDimension(R.dimen.text_size_body);
		int paddingStart = 0, paddingEnd = 0;
		boolean useForStatic = false;
		boolean useMinHeight = true;

		if (null != ta) {
			showArrow = ta.getBoolean(R.styleable.BaseListItem_show_arrow, true);
			titleText = ta.getString(R.styleable.BaseListItem_title_text);
			titleTextColor = ta.getColor(R.styleable.BaseListItem_title_text_color, titleTextColor);

			descText = ta.getString(R.styleable.BaseListItem_desc_text);
			descTextColor = ta.getColor(R.styleable.BaseListItem_desc_text_color, descTextColor);

			paddingHorizontal = (int) ta.getDimension(R.styleable.BaseListItem_padding_horizontal, paddingHorizontal);
			paddingVertical = (int) ta.getDimension(R.styleable.BaseListItem_padding_vertical, paddingVertical);
			paddingStart = (int) ta.getDimension(R.styleable.BaseListItem_padding_start, 0);
			paddingEnd = (int) ta.getDimension(R.styleable.BaseListItem_padding_end, 0);

			icon = ta.getDrawable(R.styleable.BaseListItem_list_icon);
			rightIcon = ta.getDrawable(R.styleable.BaseListItem_right_icon);
			mDescEditable = ta.getBoolean(R.styleable.BaseListItem_desc_editable, false);
			descHint = ta.getString(R.styleable.BaseListItem_desc_hint);
			descTextSize = ta.getDimension(R.styleable.BaseListItem_desc_text_size, descTextSize);
			titleTextSize = ta.getDimension(R.styleable.BaseListItem_title_text_size, descTextSize);

			mBottomLineColor = ta.getColor(R.styleable.BaseListItem_bottom_line_color, mBottomLineColor);
			mBottomLineWidth = ta.getDimension(R.styleable.BaseListItem_bottom_line_width, mBottomLineWidth);
			mBottomLinePaddingStart = (int) ta.getDimension(R.styleable.BaseListItem_bottom_line_padding_start, 0);
			mBottomLinePaddingEnd = (int) ta.getDimension(R.styleable.BaseListItem_bottom_line_padding_end, 0);
			mShowBottomLine = ta.getBoolean(R.styleable.BaseListItem_show_bottom_line, false);
			mShowTopLine = ta.getBoolean(R.styleable.BaseListItem_show_top_line, false);

			useForStatic = ta.getBoolean(R.styleable.BaseListItem_use_for_static, false);

			useMinHeight = ta.getBoolean(R.styleable.BaseListItem_use_min_height, useMinHeight);

			ta.recycle();
		}

		int anInt = InputType.TYPE_CLASS_TEXT;
		TypedArray ta2 = attrs == null ? null : getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.inputType});
		if (null != ta2) {
			anInt = ta2.getInt(0, InputType.TYPE_CLASS_TEXT);
		}


		if (null == titleText) {
			titleText = "";
		}
		if (null == descText) {
			descText = "";
		}
		if (null == descHint) {
			descHint = "";
		}
		if (paddingStart == 0) {
			paddingStart = paddingHorizontal;
		}
		if (paddingEnd == 0) {
			paddingEnd = paddingHorizontal;
		}

		setPadding(paddingStart, paddingVertical, paddingEnd, paddingVertical);
		if (useMinHeight) {
			setMinimumHeight(minHeight);
		}

		if (TextUtils.isEmpty(titleText)) {
			mTitleTv.setVisibility(View.GONE);
		} else {
			mTitleTv.setText(titleText);
			mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
			if (!mTitleTv.isShown()) {
				mTitleTv.setVisibility(View.VISIBLE);
			}
		}
		mTitleTv.setTextColor(titleTextColor);

		if (createRightView(mDescWrapper) == null) {
			mHint = descHint;
			if (mDescEditable) {
				LayoutInflater.from(getContext()).inflate(R.layout.list_item_right_edittext, mDescWrapper, true);
			} else {
				LayoutInflater.from(getContext()).inflate(R.layout.list_item_right_textview, mDescWrapper, true);
			}
			mDescTv = (TextView) findViewById(R.id.base_list_item_desc_tv);
			mDescTv.setSaveEnabled(false);
			mDescTv.setInputType(anInt);

			if (mDescEditable) {
				mDescTv.setHint(mHint);
			}
			setUseForStatic(useForStatic);
			mDescTv.setText(descText);
			mDescTv.setTextColor(descTextColor);
			mDescTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, descTextSize);
		} else {
			ButterKnife.bind(this);
		}

		setUseForStatic(useForStatic);

		if (rightIcon != null) {
			setRightIcon(rightIcon);
		} else {
			mRightIconIv.setVisibility(View.GONE);
		}
		mArrowIv.setVisibility(showArrow ? View.VISIBLE : View.GONE);
		if (icon != null) {
			mIconIv.setImageDrawable(icon);
		} else {
			mIconIv.setVisibility(View.GONE);
		}

		mPaint.setStrokeWidth(mBottomLineWidth);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mBottomLineColor);

	}

	public void setOnRightIconClickListener(OnRightIconClickListener onClickListener) {
		mOnRightIconClickListener = onClickListener;
	}

	@OnClick(R.id.base_list_item_right_icon_iv)
	public void onRightIconClick() {
		if (mOnRightIconClickListener != null) {
			mOnRightIconClickListener.onRightIconClick(this);
		}
	}

	public void setTitleColor(int color) {
		mTitleTv.setTextColor(color);
	}

	public void setTitleColorRes(int res) {
		setTitleColor(getResources().getColor(res));
	}

	public BaseListItem setShowArrow(boolean showArrow) {
		mArrowIv.setVisibility(showArrow ? View.VISIBLE : View.GONE);
		return this;
	}

	public void setShowBottomLine(boolean showBottomLine) {
		mShowBottomLine = showBottomLine;
		invalidate();
	}

	public void setBottomLinePadding(int start, int end, boolean useDp) {
		if (!mShowBottomLine) {
			mShowTopLine = true;
		}
		if (useDp) {
			float density = getResources().getDisplayMetrics().density;
			start *= density;
			end *= density;
		}
		mBottomLinePaddingStart = start;
		mBottomLinePaddingEnd = end;
		invalidate();
	}

	public BaseListItem setText(CharSequence text) {
		mTitleTv.setText(text);
		if (!mTitleTv.isShown()) {
			mTitleTv.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public BaseListItem setDesc(CharSequence text) {
		mDescTv.setText(text);
		return this;
	}

	public BaseListItem setHint(CharSequence hint) {
		mHint = hint;
		mDescTv.setHint(hint);
		return this;
	}

	public BaseListItem setEditable(boolean editable) {
		if (mDescEditable != editable) {
			mDescEditable = editable;
			setUseForStatic(false);
			mDescWrapper.removeView(mDescTv);
			if (mDescEditable) {
				LayoutInflater.from(getContext()).inflate(R.layout.list_item_right_edittext, mDescWrapper, true);
			} else {
				LayoutInflater.from(getContext()).inflate(R.layout.list_item_right_textview, mDescWrapper, true);
			}
			mDescTv = (TextView) findViewById(R.id.base_list_item_desc_tv);
			mDescTv.setSaveEnabled(false);
			if (mDescEditable) {
				mDescTv.setEnabled(true);
				mDescTv.setHint(mHint);
			}
		}
		return this;
	}

	public BaseListItem setDescColor(int color) {
		mDescTv.setTextColor(color);
		return this;
	}

	public void setDescColorRes(int res) {
		setDescColor(getResources().getColor(res));
	}

	public void setRightIcon(int resId) {
		Drawable drawable = getResources().getDrawable(resId);
		setRightIcon(drawable);
	}

	public void setRightIcon(Drawable rightIcon) {
		if (rightIcon == null) {
			mRightIconIv.setVisibility(View.GONE);
			return;
		}
		mRightIconIv.setVisibility(VISIBLE);
		mRightIconIv.setImageDrawable(rightIcon);
		LayoutParams rlp = (LayoutParams) mDescWrapper.getLayoutParams();
		rlp.addRule(LEFT_OF, R.id.base_list_item_right_icon_iv);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			rlp.addRule(START_OF, R.id.base_list_item_right_icon_iv);
		}
		mDescWrapper.setLayoutParams(rlp);
		if (mArrowIv.isShown()) {
			mArrowIv.setVisibility(View.GONE);
		}
	}


	public void setPaddingVertical(int paddingVertical) {
		setPadding(getPaddingLeft(), paddingVertical, getPaddingRight(), paddingVertical);
	}

	public void setPaddingHorizontal(int paddingHorizontal) {
		setPadding(paddingHorizontal, getPaddingTop(), paddingHorizontal, getPaddingBottom());
	}

	public void setPaddingStart(int paddingStart) {
		setPadding(paddingStart, getPaddingTop(), getPaddingRight(), getPaddingBottom());
	}

	public void setRightIconLayoutParams(int width, int height) {
		LayoutParams lp = (LayoutParams) mRightIconIv.getLayoutParams();
		lp.width = width;
		lp.height = height;
		mRightIconIv.setLayoutParams(lp);
	}

	public TextView getTitleTv() {
		return mTitleTv;
	}

	public TextView getDescTv() {
		return mDescTv;
	}

	public ImageView getIconIv() {
		return mIconIv;
	}

	public void setIcon(Drawable icon) {
		if (icon == null) {
			mIconIv.setVisibility(View.GONE);
			return;
		}
		mIconIv.setImageDrawable(icon);
		if (!mIconIv.isShown()) {
			mIconIv.setVisibility(View.VISIBLE);
		}
	}

	protected View createRightView(ViewGroup wrapper) {
		return null;
	}

	public View getRightView() {
		return mDescWrapper.getChildAt(0);
	}

	public void setRightView(View view) {
		if (view == null) {
			return;
		}
		if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		mDescWrapper.removeAllViews();
		mDescWrapper.addView(view);

		ButterKnife.bind(this);
	}

	public void setRightViewResId(int resId) {
		View view = LayoutInflater.from(getContext()).inflate(resId, mDescWrapper, false);
		setRightView(view);
	}

	public void setShowTopLine(boolean showTopLine) {
		mShowTopLine = showTopLine;
		invalidate();
	}

	public boolean isUseForStatic() {
		return !isClickable();
	}

	public void setUseForStatic(boolean useForStatic) {
		boolean clickable = !useForStatic;
		if (mDescEditable) {
			clickable = false;
		}
		setClickable(clickable);
		if (clickable) {
			setBackgroundResource(R.drawable.base_list_item_background);
		} else {
			setBackgroundColor(Color.WHITE);
		}
	}

	@OnClick(R.id.base_list_item_desc_wrapper)
	public void onEditWrapperClick() {
		if (mDescTv == null) {
			return;
		}
		if (mDescTv.isEnabled()) {
			mDescTv.requestFocus();
			SoftInputUtils.showSoftInput(getContext(), mDescTv);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mShowBottomLine) {
			//noinspection SuspiciousNameCombination
			canvas.drawLine(mBottomLinePaddingStart, getMeasuredHeight() - mBottomLineWidth / 2.f, getMeasuredWidth() - mBottomLinePaddingEnd, getMeasuredHeight() - mBottomLineWidth / 2.f, mPaint);
		}
		if (mShowTopLine) {
			//noinspection SuspiciousNameCombination
			canvas.drawLine(0, mBottomLineWidth / 2.f, getMeasuredWidth(), mBottomLineWidth / 2.f, mPaint);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) || (!mDescEditable && isClickable());
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.desc = mDescTv.getText().toString();
		ss.childrenStates = new SparseArray();
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).saveHierarchyState(ss.childrenStates);
		}
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		mDescTv.setText(ss.desc);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).restoreHierarchyState(ss.childrenStates);
		}
	}

	@Override
	protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
		dispatchFreezeSelfOnly(container);
	}

	@Override
	protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
		dispatchThawSelfOnly(container);
	}

	public interface OnRightIconClickListener {
		void onRightIconClick(BaseListItem item);
	}

	public static class SavedState extends BaseSavedState {
		public static final ClassLoaderCreator<SavedState> CREATOR
				= new ClassLoaderCreator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel source, ClassLoader loader) {
				return new SavedState(source, loader);
			}

			public SavedState createFromParcel(Parcel in) {
				return createFromParcel(in, null);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

		String desc;
		SparseArray childrenStates;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in, ClassLoader classLoader) {
			super(in);
			desc = in.readString();
			childrenStates = in.readSparseArray(classLoader);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeString(desc);
			out.writeSparseArray(childrenStates);
		}

		@Override
		public String toString() {
			return "BaseListItem.SavedState{"
					+ Integer.toHexString(System.identityHashCode(this))
					+ " desc=" + desc + "}";
		}
	}
}
