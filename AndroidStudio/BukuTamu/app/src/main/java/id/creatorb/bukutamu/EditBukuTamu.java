package id.creatorb.bukutamu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBukuTamu extends Activity {

	// properti dari edit_bukutamu
	EditText txtName;
	EditText txtEmail;
	EditText txtDesc;
	EditText txtCreatedAt;
	Button btnSave;
	Button btnDelete;

	String pid;

	// Progress Dialog
	private ProgressDialog pDialog;

	// buat class json parser
	JSONParser jsonParser = new JSONParser();

	// url untuk halaman single dari bukutamu
	private static final String url_pendaftaran_details = "http://192.168.1.22/lab-tutor/pendaftaran/get_pendaftaran_details.php";

	// url untuk pembaharuan buku tamu
	private static final String url_update_pendaftaran = "http://192.168.1.22/lab-tutor/pendaftaran/update_pendaftaran.php";

	// url untuk menghapus buku tamu
	private static final String url_delete_pendaftaran = "http://192.168.1.22/lab-tutor/pendaftaran/delete_pendaftaran.php";

	// node node json
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PENDAFTARAN = "pendaftaran";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_DESCRIPTION = "description";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_bukutamu);

		// beberapa tombol untuk menyimpan dan menghapus
		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);

		// get semua daftar buku tamu dari intent
		Intent i = getIntent();

		// get daftar buku tamu dari intent by pendaftaran id (pid)
		pid = i.getStringExtra(TAG_PID);

		// get komplit pendaftaran dari thread di background
		new GetPendaftaranDetails().execute();

		// event untuk tombol simpan
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
			// mulai pekerjaan di background untuk pembaharuan data
				new SavePendaftaranDetails().execute();
			}
		});

		// event jika tombol delete di klik
		btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// di background menjalankan penghapusan daftar yg dipilih
				new DeleteProduct().execute();
			}
		});

	}

	/**
	 * jalankan get semua product di background
	 * */
	class GetPendaftaranDetails extends AsyncTask<String, String, String> {

		/**
		 * jika memulai get activity maka jalankan progress dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBukuTamu.this);
			pDialog.setMessage("Mengambil Data...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * mulai jalankan get semua daftar dan jalankan di background
		 * */
		protected String doInBackground(String... params) {

			// pembaharuan ui dari thread yg dijalankan
			runOnUiThread(new Runnable() {
				public void run() {
					// Cek tag success
					int success;
					try {
						// buat paramater
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("pid", pid));

						// get detail dari daftar menggunakan http request
						JSONObject json = jsonParser.makeHttpRequest(
								url_pendaftaran_details, "GET", params);

						// cek log kita dg json response
						Log.d("Single Product Details", json.toString());

						// tag success json
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// sukses mendapat detail daftar
							JSONArray productObj = json
									.getJSONArray(TAG_PENDAFTARAN); // JSON
																	// Array

							// get objek daftar pertama dari json array
							JSONObject pendaftaran = productObj
									.getJSONObject(0);

							// temukan daftar menggunakan pid
							txtName = (EditText) findViewById(R.id.inputName);
							txtEmail = (EditText) findViewById(R.id.inputEmail);
							txtDesc = (EditText) findViewById(R.id.inputDesc);

							// tampilkan di edit text
							txtName.setText(pendaftaran.getString(TAG_NAME));
							txtEmail.setText(pendaftaran.getString(TAG_EMAIL));
							txtDesc.setText(pendaftaran
									.getString(TAG_DESCRIPTION));

						} else {
							// pid tidak ditemukan
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}

		/**
		 * jika pekerjaan di background selesai maka hentikan progress yg
		 * berjalan
		 * **/
		protected void onPostExecute(String file_url) {
			// hentikan progress dialog untuk get
			pDialog.dismiss();
		}
	}

	/**
	 * async task untuk menyimpan pendaftaran
	 * */
	class SavePendaftaranDetails extends AsyncTask<String, String, String> {

		/**
		 * jika proses di background akan berjalan maka tampilkan progress
		 * dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBukuTamu.this);
			pDialog.setMessage("Menyimpan data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * menyimpan data
		 * */
		protected String doInBackground(String... args) {

			// get pembaharuan data dari edit text yg di input user
			String name = txtName.getText().toString();
			String email = txtEmail.getText().toString();
			String description = txtDesc.getText().toString();

			// buat parameter
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_PID, pid));
			params.add(new BasicNameValuePair(TAG_NAME, name));
			params.add(new BasicNameValuePair(TAG_EMAIL, email));
			params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

			// kirim data pembaharuan melalui http request
			JSONObject json = jsonParser.makeHttpRequest(
					url_update_pendaftaran, "POST", params);

			// cek tag success json
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// sukses di perbaharui
					Intent i = getIntent();
					// jika sukses maka kirimkan kode 100
					setResult(100, i);
					finish();
				} else {
					// jika tidak maka gagal
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * jika pekerjaan di background selesai maka hentikan progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// hentikan progress dialog untuk menympan data
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task untuk menghapus data
	 * */
	class DeleteProduct extends AsyncTask<String, String, String> {

		/**
		 * sebelum pekerjaan di mulai di background maka jalankan progress
		 * dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditBukuTamu.this);
			pDialog.setMessage("Penghapusan data...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * hapus data
		 * */
		protected String doInBackground(String... args) {

			// cek tag success
			int success;
			try {
				// buat paramater
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("pid", pid));

				// get details data menggunakan http request
				JSONObject json = jsonParser.makeHttpRequest(
						url_delete_pendaftaran, "POST", params);

				// cek log json response
				Log.d("Hapus Data", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// data pendaftaran berhasil di hapus
					// kirimkan kode 100
					Intent i = getIntent();
					// jika pendaftaran / entri menuju buku tamu berhasil
					setResult(100, i);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * jika proses di belakang layar selesai maka hentikan progress dialog
		 * yg berjalan
		 * **/
		protected void onPostExecute(String file_url) {
			// hentikan progress dialog untuk penghapusan
			pDialog.dismiss();

		}

	}

}