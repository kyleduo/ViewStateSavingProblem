package com.kyleduo.problem.viewstatesaving.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

/**
 * Created by kyle on 16/1/13.
 */
public class SubSubView extends SubView {

	private String name = "a";

	public SubSubView(Context context) {
		super(context);
	}

	public SubSubView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SubSubView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.name = this.name;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		this.name = ss.name;
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
		
		private String name;
		private Parcelable mSuperState;

		public SavedState(Parcel source) {
			Parcelable superState = source.readParcelable(SubSubView.class.getClassLoader());
			this.mSuperState = superState != null ? superState : BaseSavedState.EMPTY_STATE;
			name = source.readString();
		}

		SavedState(Parcelable superState) {
			this.mSuperState = superState != null ? superState : BaseSavedState.EMPTY_STATE;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeParcelable(mSuperState, flags);

			out.writeString(name);
		}

		public Parcelable getSuperState() {
			return mSuperState;
		}
	}

}
