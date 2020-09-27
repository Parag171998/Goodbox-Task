package com.example.goodboxtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

	private Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*mainMotor =  new ViewModelProvider(this).get(MainMotor.class);
		uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/Download/goodbox-1.0.apk"));

		startActivityForResult( new Intent(Intent.ACTION_OPEN_DOCUMENT)
				.setType("application/vnd.android.package-archive")
				.addCategory(Intent.CATEGORY_OPENABLE), 100
		);
*/
		/*Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext().getPackageName() + ".provider", new File(Environment.getExternalStorageDirectory().toString() + "/Download/goodbox-1.0.apk"));
		intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
		startActivity(intent);*/

		/*File toInstall = new File(Environment.getExternalStorageDirectory().toString() + "/Download/goodbox-1.0.apk");
		Intent intent;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", toInstall);
			intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
			intent.setData(apkUri);
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		} else {
			Uri apkUri = Uri.fromFile(toInstall);
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		startActivity(intent);*/

		/*StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();

		File directory = getExternalFilesDir(null);
		File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download/");
		Uri fileUri = Uri.fromFile(file);
		Intent intent = new Intent(Intent.CATEGORY_TYPED_OPENABLE);
		startActivity(Intent.createChooser(intent, "Open folder"));

		Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
		startActivity(intent);
		if (Build.VERSION.SDK_INT >= 24) {
			fileUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(),
					file);
		}
		Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
		intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
		intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivity(intent);
		finish();

		*/

		new AppUpdaterUtils (this)
				.setUpdateFrom(UpdateFrom.GITHUB)
				.setGitHubUserAndRepo("Parag171998", "Goodbox-Task")
				.withListener(new AppUpdaterUtils.UpdateListener() {
					@Override
					public void onSuccess(Update update, Boolean isUpdateAvailable) {
						try {
							PackageInfo packageInfo = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
							if(Float.parseFloat(packageInfo.versionName) < Float.parseFloat(update.getLatestVersion())){
								Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "New update available " + update.getLatestVersion(), Snackbar.LENGTH_SHORT);
								snackbar.show();
								isStoragePermissionGranted();

							}
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailed(AppUpdaterError error) {

					}
				})
				.start();

		}

	private void showDialog(){
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("New update available")
				.setMessage("Do you want to update?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						UpdateApp atualizaApp = new UpdateApp();
						atualizaApp.setContext(getApplicationContext());
						atualizaApp.execute("https://github.com/Parag171998/Goodbox-Task/releases/download/1.1/app-debug.apk");
					}
				}).setNegativeButton("No", null)
				.show();
	}

	private  boolean isStoragePermissionGranted() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED) {
				showDialog();
				return true;
			} else {

				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
				return false;
			}
		}
		else { //permission is automatically granted on sdk<23 upon installation
			return true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
			showDialog();
		}
	}

	public class UpdateApp extends AsyncTask<String,Void,Void> {
		private Context context;
		ProgressDialog progressDialog;

		public void setContext(Context contextf){
			context = contextf;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				int count;

				URL url = new URL(arg0[0]);
				URLConnection connection = url.openConnection();
				connection.connect();

				// this will be useful so that you can show a tipical 0-100%
				// progress bar
				int lenghtOfFile = connection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/Download/app-debug.apk");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					progressDialog.setMessage("Downloading... " + (int) ((total * 100) / lenghtOfFile) + "%");
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("UpdateAPP", "Update error! " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progressDialog.dismiss();
			/*Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/Download/goodbox-1.0.apk")), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
			context.startActivity(intent);*/
		}
	}
}