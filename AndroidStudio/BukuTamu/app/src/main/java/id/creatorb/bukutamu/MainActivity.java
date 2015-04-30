package id.creatorb.bukutamu;

 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 
public class MainActivity extends Activity{
 
    Button btnLihatData;
    Button btnBuatData;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        // tombol lihat data dan buat data
        btnLihatData = (Button) findViewById(R.id.btnLihatData);
        btnBuatData = (Button) findViewById(R.id.btnBuatData);
 
        // event dari lihat data
        btnLihatData.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // jalankan activity dari SemuaBukuTamu
                Intent i = new Intent(getApplicationContext(), SemuaBukuTamu.class);
                startActivity(i);
 
            }
        });
 
        // jalankan activity untuk melihat BukuTamu
        btnBuatData.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Jalankan class TambahBukuTamu untuk menambah entri
                Intent i = new Intent(getApplicationContext(), TambahBukuTamu.class);
                startActivity(i);
 
            }
        });
    }
}