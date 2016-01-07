package com.kyleduo.problem.viewstatesaving.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by kyle on 15/9/8.
 */
public class SoftInputUtils {

	public static void hideSoftInput(Context context, View view) {
		if (context == null || view == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void hideSoftInput(Context context) {
		if (context instanceof Activity) {
			hideSoftInput(context, ((Activity) context).getCurrentFocus());
		}
	}

	public static void showSoftInput(Context context, View view) {
		if (context == null || view == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}

}
