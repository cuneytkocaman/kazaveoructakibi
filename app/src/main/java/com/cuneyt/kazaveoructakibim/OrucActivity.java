package com.cuneyt.kazaveoructakibim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cuneyt.kazaveoructakibim.busines.SqliteHelper;
import com.cuneyt.kazaveoructakibim.dataAccess.SqliteInfo;
import com.cuneyt.kazaveoructakibim.entities.abstracts.VakitModelService;
import com.cuneyt.kazaveoructakibim.entities.concretes.OrucModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrucActivity extends AppCompatActivity {

    private Toolbar toolbarOruc;
    private TextView textKazaOruc, textKazaOrucDate, textOrucGun;
    private ImageButton btOrucUp, btOrucDown;
    private AdView adsBannerOruc;

    private VakitModelService orucModel = new OrucModel();

    private SqliteHelper sqliteHelper = new SqliteHelper(this);

    private String tableNameOruc = SqliteInfo.TABLE_NAME_ORUC;

    public void visualObject() {
        toolbarOruc = findViewById(R.id.toolbarOruc);
        textKazaOruc = findViewById(R.id.textKazaOruc);
        textKazaOrucDate = findViewById(R.id.textKazaOrucDate);
        textOrucGun = findViewById(R.id.textOrucGun);
        btOrucUp = findViewById(R.id.btOrucUp);
        btOrucDown = findViewById(R.id.btOrucDown);
        adsBannerOruc = findViewById(R.id.adsBannerOruc);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oruc);

        visualObject();

        // Toolbar
        toolbarOruc.setTitle("Kaza Oru??lar??n??z");
        toolbarOruc.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbarOruc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Toolbar geri butonu.

        advertising();

        textKazaOruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrucActivity.this);
                View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_design, null);

                TextView textAlertAdd = alertView.findViewById(R.id.textAlertAdd);
                TextView textAlertUpdate = alertView.findViewById(R.id.textAlertUpdate);
                TextView textAlertClose = alertView.findViewById(R.id.textAlertClose);
                EditText editAlertValue = alertView.findViewById(R.id.editAlertValue);

                builder.setView(alertView);
                AlertDialog alertDialog = builder.create();

                Cursor cursor = sqliteHelper.readData(SqliteInfo.SELECT_ORUC);

                alertView.findViewById(R.id.textAlertAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String orucId = orucModel.modelId("1"); // Sadece tek veri eklenece??inden ID 1 olarak girildi.
                        String orucDate = orucModel.modelDate(currentlyDate());
                        String orucSayi = orucModel.modelVakit(editAlertValue.getText().toString());

                        if (cursor.getCount() == 0) {
                            sqliteHelper.addData(orucId, orucDate, orucSayi, tableNameOruc); // Tablo bo??sa ekleme i??lemi yap??ld??.
                        } else {
                            sqliteHelper.updateData(orucId, orucDate, orucSayi, tableNameOruc); // Tablo doluysa g??ncelleme i??lemi yap??ld??.
                        }

                        textKazaOruc.setText(String.valueOf(orucSayi)); // Veri TextView'e aktar??ld??.
                        textKazaOrucDate.setText(orucDate);
                        textOrucGun.setVisibility(View.VISIBLE);
                        alertDialog.dismiss();
                    }
                });

                alertView.findViewById(R.id.textAlertUpdate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String orucId = orucModel.modelId("1");
                        String orucDate = orucModel.modelDate(currentlyDate());
                        String orucSayi = orucModel.modelVakit(editAlertValue.getText().toString());

                        int oldOrucSayi, newOrucSayi; // G??ncelleme yap??laca???? i??in eski ve yeni de??erler i??in de??i??ken olu??turuldu

                        if (cursor.getCount() == 0) {
                            sqliteHelper.addData(orucId, orucDate, orucSayi, tableNameOruc);
                            textKazaOruc.setText(String.valueOf(orucSayi));

                        } else {

                            if (TextUtils.isEmpty(editAlertValue.getText())) {
                                Toast.makeText(getApplicationContext(), "De??er giriniz.", Toast.LENGTH_SHORT).show();

                            } else if (textKazaOruc.getText().equals("T??klay??n")) { // TextView ??zerinde say??sal de??er yaz??yor mu kontrol edildi. (Ayn?? zamanda DB'de veri yok demek)
                                oldOrucSayi = 0; // TextView'de say??sal de??er olmad?????? i??in ola??an de??ere 0 atand??.
                                newOrucSayi = Integer.parseInt(editAlertValue.getText().toString()); // EditText'e girilen veri yeni de??ere aktar??ld??.

                                newOrucSayi += oldOrucSayi; // Yeni de??er ile eski de??er topland??.

                                textKazaOruc.setText(String.valueOf(newOrucSayi)); // Son de??er TextView'e aktar??ld??.
                                textKazaOrucDate.setText(orucDate);
                                textOrucGun.setVisibility(View.VISIBLE);
                                sqliteHelper.addData(orucId, orucDate, String.valueOf(newOrucSayi), tableNameOruc);

                            } else {
                                oldOrucSayi = Integer.parseInt(textKazaOruc.getText().toString());
                                newOrucSayi = Integer.parseInt(orucSayi);

                                newOrucSayi += oldOrucSayi;

                                sqliteHelper.updateData(orucId, orucDate, String.valueOf(newOrucSayi), tableNameOruc);
                                textKazaOrucDate.setText(orucDate);
                                textOrucGun.setVisibility(View.VISIBLE);
                                textKazaOruc.setText(String.valueOf(newOrucSayi));
                            }
                        }
                        alertDialog.dismiss();
                    }
                });

                alertView.findViewById(R.id.textAlertClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        btOrucUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDownData();
            }
        });

        btOrucDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDownData();
            }
        });

        viewData(orucModel);
    }

    // ARTTIR VE AZALT BUTONU ????LEMLER?? ----------------------------------------------------------------------------------------------------------------------------------
    public void upDownData() {

        if (textKazaOruc.getText().equals("T??klay??n")){
            Toast.makeText(getApplicationContext(),"De??er giriniz.", Toast.LENGTH_LONG).show();

        } else {
            int orucValue = Integer.parseInt(textKazaOruc.getText().toString());

            if (btOrucUp.isPressed()) {
                // Artt??r butonalar??na bas??ld??????nda veri +1 artt??r??ld??
                orucValue++;

            } else if (btOrucDown.isPressed()) {
                // Azalt butonlar??na bas??ld??????nda veri -1 azalt??ld??.
                if (orucValue == 0) {

                } else {
                    orucValue--;
                }
            }

            String orucId = orucModel.modelId("1");
            String newDate = orucModel.modelDate(currentlyDate()); // Ge??erli tarih model i??indeki date de??i??kenine g??nderildi
            String newOrucValue = orucModel.modelVakit(String.valueOf(orucValue)); // AlertDialog'daki veri model i??indeki sabah de??i??kenine g??nderildi

            textKazaOruc.setText(newOrucValue);
            textKazaOrucDate.setText(newDate);

            sqliteHelper.updateData(orucId, newDate, newOrucValue, tableNameOruc);
        }
    }

    // VER?? L??STELEME
    public void viewData(VakitModelService model) {
        // model: ??stenen vaktin modelin ??a????rmak i??in parametre olarak atand??

        Cursor cursor = sqliteHelper.readData(SqliteInfo.SELECT_ORUC);

        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                String dbOrucId = model.modelId(cursor.getString(0));
                String dbOrucDate = model.modelDate(cursor.getString(1));
                String dbOrucSayi = model.modelDate(cursor.getString(2));

                textKazaOruc.setText(dbOrucSayi);
                textOrucGun.setVisibility(View.VISIBLE);
                textKazaOrucDate.setText(dbOrucDate);
            }
        }
    }

    // GE??ERL?? TAR??H
    public String currentlyDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String dateToday = dateFormat.format(calendar.getTime()) + " ";
        return dateToday;
    }

    // REKLAM
    public void advertising() {
        MobileAds.initialize(OrucActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adsBannerOruc.loadAd(adRequest);
    }
}

