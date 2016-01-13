package com.kyleduo.problem.viewstatesaving;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kyleduo.problem.viewstatesaving.view.BaseListItem;
import com.kyleduo.problem.viewstatesaving.view.CheckableListItem;
import com.kyleduo.problem.viewstatesaving.view.SubSubView;
import com.kyleduo.problem.viewstatesaving.view.SubView;
import com.kyleduo.problem.viewstatesaving.view.SuperView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

	@Bind(R.id.base_item)
	protected BaseListItem mBaseListItem;
	@Bind(R.id.spv)
	protected SuperView mSuperView;
	@Bind(R.id.sbv)
	protected SubView mSubView;
	@Bind(R.id.sbbv)
	protected SubSubView mSubSubView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			int alwaysFinish = Settings.Global.getInt(getApplication().getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
			if (alwaysFinish == 0) {
				new AlertDialog.Builder(this)
						.setTitle("Hint")
						.setMessage("Please turn on the \"Always finish activities\" option to see the problem.")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
								startActivity(intent);
								dialog.dismiss();
							}
						})
						.setNegativeButton("Later", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.create()
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "spv: " + mSuperView.getNumber() + " | sbv: " + mSubView.getNumber() + " sbv: " + mSubView.isChecked() + "\n sbbv: " + mSubSubView.getNumber() + " sbbv: " + mSubSubView.isChecked() + " sbbv: " + mSubSubView.getName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		savedInstanceState.setClassLoader(CheckableListItem.class.getClassLoader());
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.setClassLoader(CheckableListItem.class.getClassLoader());
		super.onSaveInstanceState(outState);
	}

	@OnClick(R.id.base_item)
	public void onBaseClick() {
		mSuperView.setNumber(10);
		mSubView.setNumber(20);
		mSubView.setIsChecked(true);
		mSubSubView.setNumber(30);
		mSubSubView.setIsChecked(true);
		mSubSubView.setName("b");
		startActivity(new Intent(this, SubActivity.class));
	}
}
