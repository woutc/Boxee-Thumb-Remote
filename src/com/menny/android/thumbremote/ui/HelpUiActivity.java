package com.menny.android.thumbremote.ui;

import com.menny.android.thumbremote.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HelpUiActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}
	
	public void onCloseClicked(View view) {
		finish();
	}
}
