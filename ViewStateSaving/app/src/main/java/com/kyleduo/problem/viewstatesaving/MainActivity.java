package com.kyleduo.problem.viewstatesaving;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.kyleduo.problem.viewstatesaving.view.BaseListItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

	@Bind(R.id.base_item)
	protected BaseListItem mBaseListItem;

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
						}).setNegativeButton("Later", new DialogInterface.OnClickListener() {
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

	@OnClick(R.id.base_item)
	public void onBaseClick() {
		startActivity(new Intent(this, SubActivity.class));
	}
}
