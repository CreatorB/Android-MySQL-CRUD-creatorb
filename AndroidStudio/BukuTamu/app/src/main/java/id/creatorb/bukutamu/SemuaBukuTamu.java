package id.creatorb.bukutamu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class SemuaBukuTamu extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // buat json object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> bukutamuList;
 
    // url untuk get semua buku tamu
    private static String url_semua_bukutamu = "http://192.168.1.22/lab-tutor/pendaftaran/get_all_pendaftaran.php";
 
    // JSON Node
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PENDAFTARAN = "pendaftaran";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
 
    // pendaftaran JSONArray
    JSONArray pendaftaran = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semua_bukutamu);
 
        // Hashmap untuk ListView
        bukutamuList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadSemuaBukuTamu().execute();
 
        // Get listview
        ListView lv = getListView();
 
        // select single bukutamu
        // Jalankan tampilan edit buku tamu
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // dapatkan nilai dari list pendaftaran
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
 
                // Memulai aktifitas baru
                Intent in = new Intent(getApplicationContext(),
                        EditBukuTamu.class);
                // kirimkan pid ke activity selanjutnya
                in.putExtra(TAG_PID, pid);
 
                // memulai activity baru dengan mnghrap bbrapa kembalian response
                
                startActivityForResult(in, 100);
            }
        });
 
    }
 
    // response dari edit bukutamu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // jika kode hasil sama dengan 100 
        if (resultCode == 100) {
            // maka diterima
            // ketika user ngedit atau menghapus data
            // reload screen
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
 
    /**
     * Background Async Task untuk menampilkan semua daftar bukutamu menggunakan http request
     * */
    class LoadSemuaBukuTamu extends AsyncTask<String, String, String> {
 
        /**
         * sebelum melakukan thread di background maka jalankan progres dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SemuaBukuTamu.this);
            pDialog.setMessage("Mohon tunggu, Loading Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * dapetkan semua produk dari get url di background
         * */
        protected String doInBackground(String... args) {
            // Buat Parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // ambil json dari url
            JSONObject json = jParser.makeHttpRequest(url_semua_bukutamu, "GET", params);
 
            // cek logcat untuk response dari json
            Log.d("Semua Buku Tamu: ", json.toString());
 
            try {
                // cek jika tag success
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // data ditemukan
                    // ambil array dari bukutamu
                    pendaftaran = json.getJSONArray(TAG_PENDAFTARAN);
 
                    // tampilkan perulangan semua produk
                    for (int i = 0; i < pendaftaran.length(); i++) {
                        JSONObject c = pendaftaran.getJSONObject(i);
 
                        // simpan pada variabel
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
 
                        // buat new hashmap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
 
                        // masukan HashList ke ArrayList
                        bukutamuList.add(map);
                    }
                } else {
                    // jika tidak ada data
                    // maka jalankan tambahkan buku tamu
                    Intent i = new Intent(getApplicationContext(),
                            TambahBukuTamu.class);
                    // tutup semua proses sebelumnya
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * jika pekerjaan di belakang layar selesai maka hentikan progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // hentikan progress ketika semua data didapat
            pDialog.dismiss();
            // perbarui screen
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * perbarui json ke arraylist
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            SemuaBukuTamu.this, bukutamuList,
                            R.layout.list_pendaftaran, new String[] { TAG_PID,
                                    TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // perbarui list pendaftaran
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}