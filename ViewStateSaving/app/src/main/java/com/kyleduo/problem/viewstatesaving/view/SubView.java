package com.kyleduo.problem.viewstatesaving.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

/**
 * Created by kyle on 16/1/13.
 */
public class SubView extends SuperView {

	private boolean isChecked;

	public SubView(Context context) {
		super(context);
	}

	public SubView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SubView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.checked = this.isChecked;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		this.isChecked = ss.checked;
	}

	public static class SavedState implements Parcelable {
		public static final SavedState EMPTY_STATE = new SavedState() {
		};

		public static final Creator<SavedState> CREATOR
				= new Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
		// This keeps the parent(RecyclerView)'s state
		private boolean checked;
		private Parcelable mSuperState;

		public SavedState(Parcel source) {
			Parcelable superState = source.readParcelable(SuperView.class.getClassLoader());
			this.mSuperState = superState != null ? superState : EMPTY_STATE;
			checked = (boolean) source.readValue(this.getClass().getClassLoader());
		}

		SavedState() {
			mSuperState = null;
		}

		SavedState(Parcelable superState) {
			this.mSuperState = superState != EMPTY_STATE ? superState : null;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeParcelable(mSuperState, flags);
			out.writeValue(checked);
		}

		public Parcelable getSuperState() {
			return mSuperState;
		}
	}

}
