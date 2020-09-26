package com.example.goodboxtask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new AppUpdater(this)
				.setTitleOnUpdateAvailable("Update available!")
				.showAppUpdated(true)
				.setDisplay(Display.SNACKBAR)
				.setUpdateFrom(UpdateFrom.GITHUB)
				.setGitHubUserAndRepo("Parag171998", "Goodbox-Task")
				.start();
		Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Checking for updates...", Snackbar.LENGTH_SHORT);
		snackbar.show();
	}
}