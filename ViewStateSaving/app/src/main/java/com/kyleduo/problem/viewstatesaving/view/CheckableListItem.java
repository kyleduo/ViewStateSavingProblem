package com.kyleduo.problem.viewstatesaving.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.kyleduo.problem.viewstatesaving.R;

import butterknife.OnClick;

/**
 * Created by kyle on 15/9/8.
 */
public class CheckableListItem extends BaseListItem implements Checkable {

	private boolean mChecked;
	private OnCheckedChangeListener mOnCheckedChangedListener;

	public CheckableListItem(Context context) {
		super(context);
	}

	public CheckableListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CheckableListItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void init(AttributeSet attrs) {
		super.init(attrs);
		refreshCheckedStatus();
	}

	/**
	 * @param checked
	 * @param trigger false不触发OnCheckedChangeListener
	 */
	public void setChecked(boolean checked, boolean trigger) {
		mChecked = checked;
		refreshCheckedStatus();
		if (null != mOnCheckedChangedListener && trigger) {
			mOnCheckedChangedListener.onCheckedChangeListener(this, mChecked);
		}
	}

	protected void refreshCheckedStatus() {
		setRightIcon(getResources().getDrawable(mChecked ? R.drawable.round_checkbox_checked : R.drawable.round_checkbox_unchecked));
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		setChecked(checked, true);
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	@OnClick
	public void onClick(View v) {
		toggle();
	}

	public void setOnCheckedChangedListener(OnCheckedChangeListener onCheckedChangedListener) {
		mOnCheckedChangedListener = onCheckedChangedListener;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		SavedState ss = new SavedState(superState);

		ss.checked = isChecked();
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;

		super.onRestoreInstanceState(ss.getSuperState());
		setChecked(ss.checked);
		requestLayout();
	}

	public interface OnCheckedChangeListener {
		void onCheckedChangeListener(CheckableListItem item, boolean checked);
	}

	public static class SavedState implements Parcelable {
		public static final Creator<SavedState> CREATOR
				= new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
		boolean checked;

		private Parcelable mSuperState;

		SavedState(Parcelable superState) {
			mSuperState = superState != BaseSavedState.EMPTY_STATE ? superState : null;
		}

		private SavedState(Parcel in) {
			Parcelable superState = in.readParcelable(CheckableListItem.class.getClassLoader());
			mSuperState = superState != BaseSavedState.EMPTY_STATE ? superState : null;
			checked = (Boolean) in.readValue(null);
		}
		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeParcelable(mSuperState, flags);
			out.writeValue(checked);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public String toString() {
			return "CheckableListItem.SavedState{"
					+ Integer.toHexString(System.identityHashCode(this))
					+ " checked=" + checked + "}";
		}

		public Parcelable getSuperState() {
			return mSuperState;
		}
	}

}
