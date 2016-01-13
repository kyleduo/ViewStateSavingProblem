package com.kyleduo.problem.viewstatesaving.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.kyleduo.problem.viewstatesaving.R;

/**
 * Created by kyle on 16/1/13.
 */
public class CustomGroup extends LinearLayout {
	public CustomGroup(Context context) {
		super(context);
		init();
	}

	public CustomGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomGroup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_group, this, true);
		setOrientation(HORIZONTAL);
	}


	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
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

	public static class SavedState extends BaseSavedState {
		public static final Parcelable.ClassLoaderCreator<SavedState> CREATOR
				= new Parcelable.ClassLoaderCreator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel source, ClassLoader loader) {
				return new SavedState(source, loader);
			}

			@Override
			public SavedState createFromParcel(Parcel source) {
				return null;
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

		SparseArray childrenStates;

		public SavedState(Parcel source, ClassLoader loader) {
			super(source);
			childrenStates = source.readSparseArray(loader);
		}

		public SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeSparseArray(childrenStates);
		}
	}

}
