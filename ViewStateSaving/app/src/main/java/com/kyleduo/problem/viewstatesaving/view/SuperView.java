package com.kyleduo.problem.viewstatesaving.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kyle on 16/1/13.
 */
public class SuperView extends View {

	private int number;

	public SuperView(Context context) {
		super(context);
	}

	public SuperView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SuperView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.number = this.number;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		this.number = ss.number;
	}

	public static class SavedState extends BaseSavedState {

		public static final Creator<SavedState> CREATOR
				= new Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
		private int number;

		public SavedState(Parcel source) {
			super(source);
			number = source.readInt();
		}

		SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(number);
		}
	}
}
