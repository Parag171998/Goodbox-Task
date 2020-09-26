package com.example.goodboxtask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AppUpdaterUtils  appUpdaterUtils  = new AppUpdaterUtils(this)
				.setUpdateFrom(UpdateFrom.GITHUB)
				.setGitHubUserAndRepo("Parag171998", "Goodbox-Task")
				.withListener(new AppUpdaterUtils.UpdateListener() {
					@Override
					public void onSuccess(Update update, Boolean isUpdateAvailable) {
						Toast.makeText(MainActivity.this, Boolean.toString(isUpdateAvailable), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailed(AppUpdaterError error) {
						Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
					}
				});
		appUpdaterUtils .start();

	}
}