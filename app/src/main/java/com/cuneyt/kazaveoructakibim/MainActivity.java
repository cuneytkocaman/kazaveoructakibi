package com.cuneyt.kazaveoructakibim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.cuneyt.kazaveoructakibim.busines.SqliteHelper;
import com.cuneyt.kazaveoructakibim.dataAccess.SqliteInfo;
import com.cuneyt.kazaveoructakibim.entities.concretes.AksamModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.IkindiModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.OgleModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.SabahModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.YatsiModel;
import com.cuneyt.kazaveoructakibim.entities.abstracts.VakitModelService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout constMid, constCalcCalendar;
    private TextView textSabah, textOgle, textIkindi, textAksam, textYatsi, textDateSabah, textDateOgle, textDateIkindi, textDateAksam, textDateYatsi;
    private ImageButton btSabahUp, btSabahDown, btOgleUp, btOgleDown, btIkindiUp, btIkindiDown, btAksamUp, btAksamDown, btYatsiUp, btYatsiDown, btCalendar, btInfo, btOruc, btCompass;

    private Animation animationInput;

    private SqliteHelper sqliteHelper = new SqliteHelper(MainActivity.this);
    private SqliteInfo sqliteInfo = new SqliteInfo();

    private VakitModelService modelSabah = new SabahModel();
    private VakitModelService modelOgle = new OgleModel();
    private VakitModelService modelIkindi = new IkindiModel();
    private VakitModelService modelAksam = new AksamModel();
    private VakitModelService modelYatsi = new YatsiModel();

    private AdView adsBanner;

    private int currentValue;

    public void visualObject() {
        constCalcCalendar = findViewById(R.id.constCalcCalendar);
        constMid = findViewById(R.id.constMid);
        textSabah = findViewById(R.id.textSabah);
        textOgle = findViewById(R.id.textOgle);
        textIkindi = findViewById(R.id.textIkindi);
        textAksam = findViewById(R.id.textAksam);
        textYatsi = findViewById(R.id.textYatsi);
        textDateSabah = findViewById(R.id.textDateSabah);
        textDateOgle = findViewById(R.id.textDateOgle);
        textDateIkindi = findViewById(R.id.textDateIkindi);
        textDateAksam = findViewById(R.id.textDateAksam);
        textDateYatsi = findViewById(R.id.textDateYatsi);
        btInfo = findViewById(R.id.btInfo);
        btCalendar = findViewById(R.id.btCalendar);
        btOruc = findViewById(R.id.btOruc);
        btSabahUp = findViewById(R.id.btSabahUp);
        btSabahDown = findViewById(R.id.btSabahDown);
        btOgleUp = findViewById(R.id.btOgleUp);
        btOgleDown = findViewById(R.id.btOgleDown);
        btIkindiUp = findViewById(R.id.btIkindiUp);
        btIkindiDown = findViewById(R.id.btIkindiDown);
        btAksamUp = findViewById(R.id.btAksamUp);
        btAksamDown = findViewById(R.id.btAksamDown);
        btYatsiUp = findViewById(R.id.btYatsiUp);
        btYatsiDown = findViewById(R.id.btYatsiDown);
        adsBanner = findViewById(R.id.adsBannerCalendar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            visualObject();
            animation();

            textClickOperation();
            buttonClickOperation();
            advertising();

            onRestart(); // Navigation bar back butona basınca yenilenmeyen sayfa onRestart metodu ile yenilendi.

            btCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
            });

            btInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    startActivity(intent);
                }
            });

            btOruc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, OrucActivity.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e){
            Log.e("HATA", "Cihaz: " + deviceName() + " | Tarih: " + currentlyDate() + " | " + String.valueOf(e));
        }

    }

    protected void onRestart() {
        super.onRestart();
        viewData(modelSabah, SqliteInfo.SELECT_SABAH, textDateSabah, textSabah);
        viewData(modelOgle, SqliteInfo.SELECT_OGLE, textDateOgle, textOgle);
        viewData(modelIkindi, SqliteInfo.SELECT_IKINDI, textDateIkindi, textIkindi);
        viewData(modelAksam, SqliteInfo.SELECT_AKSAM, textDateAksam, textAksam);
        viewData(modelYatsi, SqliteInfo.SELECT_YATSI, textDateYatsi, textYatsi);
    }

    public void toastMessage() {
        Toast.makeText(getApplicationContext(), "Kaza sayısı giriniz.", Toast.LENGTH_SHORT).show();
    }

    public void animation() {
        animationInput = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_input_text);
        constMid.setAnimation(animationInput);
    }

    public void textClickOperation() {
        textSabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUpdateData(modelSabah, textSabah, textDateSabah, "Sabah", sqliteHelper.readData(SqliteInfo.SELECT_SABAH), SqliteInfo.TABLE_NAME_SABAH);

            }
        });

        textOgle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUpdateData(modelOgle, textOgle, textDateOgle, "Öğle", sqliteHelper.readData(SqliteInfo.SELECT_OGLE), SqliteInfo.TABLE_NAME_OGLE);
            }
        });

        textIkindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUpdateData(modelIkindi, textIkindi, textDateIkindi, "İkindi", sqliteHelper.readData(SqliteInfo.SELECT_IKINDI), SqliteInfo.TABLE_NAME_IKINDI);
            }
        });

        textAksam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUpdateData(modelAksam, textAksam, textDateAksam, "Akşam", sqliteHelper.readData(SqliteInfo.SELECT_AKSAM), SqliteInfo.TABLE_NAME_AKSAM);
            }
        });

        textYatsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUpdateData(modelYatsi, textYatsi, textDateYatsi, "Yatsı", sqliteHelper.readData(SqliteInfo.SELECT_YATSI), SqliteInfo.TABLE_NAME_YATSI);
            }
        });
    }

    public void buttonClickOperation() {
        btSabahUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textSabah.getText().equals("Sabah")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelSabah, textSabah, textDateSabah, SqliteInfo.TABLE_NAME_SABAH);
                }
            }
        });

        btSabahDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textSabah.getText().equals("Sabah")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelSabah, textSabah, textDateSabah, SqliteInfo.TABLE_NAME_SABAH);
                }
            }
        });

        btOgleUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textOgle.getText().equals("Öğle")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelOgle, textOgle, textDateOgle, SqliteInfo.TABLE_NAME_OGLE);
                }
            }
        });

        btOgleDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textOgle.getText().equals("Öğle")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelOgle, textOgle, textDateOgle, SqliteInfo.TABLE_NAME_OGLE);
                }
            }
        });

        btIkindiUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textIkindi.getText().equals("İkindi")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelIkindi, textIkindi, textDateIkindi, SqliteInfo.TABLE_NAME_IKINDI);
                }
            }
        });

        btIkindiDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textIkindi.getText().equals("İkindi")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelIkindi, textIkindi, textDateIkindi, SqliteInfo.TABLE_NAME_IKINDI);
                }
            }
        });

        btAksamUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textAksam.getText().equals("Akşam")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelAksam, textAksam, textDateAksam, SqliteInfo.TABLE_NAME_AKSAM);
                }
            }
        });

        btAksamDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textAksam.getText().equals("Akşam")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelAksam, textAksam, textDateAksam, SqliteInfo.TABLE_NAME_AKSAM);
                }
            }
        });

        btYatsiUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textYatsi.getText().equals("Yatsı")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelYatsi, textYatsi, textDateYatsi, SqliteInfo.TABLE_NAME_YATSI);
                }
            }
        });

        btYatsiDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textYatsi.getText().equals("Yatsı")) {
                    toastMessage();
                } else {
                    upDownData(currentValue, modelYatsi, textYatsi, textDateYatsi, SqliteInfo.TABLE_NAME_YATSI);
                }
            }
        });
    }

    // GEÇERLİ TARİH
    public String currentlyDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        String dateToday = dateFormat.format(calendar.getTime()) + " ";
        return dateToday;
    }

    // VERI EKLEME GUNCELLEME --------------------------------------------------------------------------------------------------------------------------------------------
    public void insertUpdateData(VakitModelService model, TextView text1, TextView text2, String textName, Cursor cursor, String tableName) {
        // model: İstenen vaktin modelin çağırmak için parametre olarak atandı
        // cursor: DB'de veri var mı yok mu kontrol etmek için parametre olarak atandı.
        // tableName: Hangi tablo üzerinde işlem yapılması gerektiği belli olsun diye parametre olarak atandı.

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_design, null); // AlertDialog tasarımı view'e aktarıldı.

        EditText editAlertValue = alertView.findViewById(R.id.editAlertValue); // Alert Dialog görsel objeleri.
        TextView btAlertAdd = alertView.findViewById(R.id.textAlertAdd);
        TextView btAlertUpdate = alertView.findViewById(R.id.textAlertUpdate);
        TextView btAlertClose = alertView.findViewById(R.id.textAlertClose);

        builder.setView(alertView);
        AlertDialog alertDialog = builder.create();

        // Veri ekleyen veya güncelleyen buton işlemi.
        alertView.findViewById(R.id.textAlertAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editAlertValue.getText())) {
                    Toast.makeText(getApplicationContext(), "Kaza sayısı giriniz.", Toast.LENGTH_SHORT).show();

                } else {
                    String incomingId = model.modelId("1");
                    String incomingDate = model.modelDate(currentlyDate()); // Geçerli tarih model içindeki date değişkenine gönderildi
                    String incomingVakit = model.modelVakit(editAlertValue.getText().toString()); // AlertDialog'daki veri model içindeki sabah değişkenine gönderildi

                    text1.setText(incomingVakit); // textView' e veri aktarıldı.
                    text2.setText(incomingDate); // textView' e veri aktarıldı.

                    if (cursor.getCount() == 0) { // Eğer veri yoksa ekleme yap, veri varsa güncelleme yap. getCount ile DB'deki veri kontrol edildi.
                        sqliteHelper.addData(incomingId, incomingDate, incomingVakit, tableName);
                    } else {
                        sqliteHelper.updateData(incomingId, incomingDate, incomingVakit, tableName);
                    }

                    alertDialog.dismiss();
                }
            }
        });

        // Alert Dialog'daki editText'e girilen veri ile textView'deki eski veriyi toplayıp güncelleyen buton işlemi.
        alertView.findViewById(R.id.textAlertUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editAlertValue.getText())) {
                    Toast.makeText(getApplicationContext(), "Kaza sayısı giriniz.", Toast.LENGTH_SHORT).show();

                } else if (text1.getText().equals(textName)) {

                    String incomingId = model.modelId("1");
                    String incomingDate = model.modelDate(currentlyDate()); // Geçerli tarih model içindeki date değişkenine gönderildi
                    String incomingVakit = model.modelVakit(editAlertValue.getText().toString()); // AlertDialog'daki veri model içindeki sabah değişkenine gönderildi

                    int oldVakitValue = 0; // TextView'deki eski değer int değişkene aktarıldı.
                    int incomingVakitValue = Integer.parseInt(incomingVakit.toString()); // Yeni girilen değer int değişkene aktarıldı.

                    incomingVakitValue += oldVakitValue; // Eski ve yeni değer toplandı.

                    text1.setText(String.valueOf(incomingVakitValue)); // textView' e veri aktarıldı.
                    text2.setText(incomingDate); // textView' e veri aktarıldı.

                    sqliteHelper.addData(incomingId, incomingDate, String.valueOf(incomingVakitValue), tableName); // TextView' de sayısal veri olmadığı için ekleme yapıldı.

                    alertDialog.dismiss();

                } else {

                    String incomingId = model.modelId("1");
                    String incomingDate = model.modelDate(currentlyDate()); // Geçerli tarih model içindeki date değişkenine gönderildi
                    String incomingVakit = model.modelVakit(editAlertValue.getText().toString()); // AlertDialog'daki veri model içindeki sabah değişkenine gönderildi

                    int oldVakitValue = Integer.parseInt(text1.getText().toString()); // TextView'deki eski değer int değişkene aktarıldı.
                    int incomingVakitValue = Integer.parseInt(incomingVakit.toString()); // Yeni girilen değer int değişkene aktarıldı.

                    incomingVakitValue += oldVakitValue; // Eski ve yeni değer toplandı.

                    text1.setText(String.valueOf(incomingVakitValue)); // textView' e veri aktarıldı.
                    text2.setText(incomingDate); // textView' e veri aktarıldı.

                    sqliteHelper.updateData(incomingId, incomingDate, String.valueOf(incomingVakitValue), tableName); // Güncelleme yapıldı.

                    alertDialog.dismiss();
                }

            }
        });

        alertView.findViewById(R.id.textAlertClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    // VERI GORUNTULEME --------------------------------------------------------------------------------------------------------------------------------------------------
    public void viewData(VakitModelService model, String query, TextView text1, TextView text2) {
        // model: İstenen vaktin modelin çağırmak için parametre olarak atandı
        // query: İstenen vakitteki SELECT sorgusunu çalıştırmak için parametre olarak atandı.
        // text1, text2: İstenen textView' e veri aktarabilmek için parametre olarak atandı.

        Cursor cursor = sqliteHelper.readData(query);

        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                String dbId = model.modelId(cursor.getString(0));
                String dbDate = model.modelDate(cursor.getString(1));
                String dbVakit = model.modelDate(cursor.getString(2));

                text1.setText(dbDate);
                text2.setText(dbVakit);
            }
        }
    }

    // ARTTIR VE AZALT BUTONU İŞLEMLERİ ----------------------------------------------------------------------------------------------------------------------------------
    public void upDownData(int value, VakitModelService model, TextView text1, TextView text2, String tableName) {

        value = Integer.parseInt(text1.getText().toString());

        if (btSabahUp.isPressed() || btOgleUp.isPressed() || btIkindiUp.isPressed() || btAksamUp.isPressed() || btYatsiUp.isPressed()) {
            // Arttır butonalarına basıldığında veri +1 arttırıldı
            value++;

        } else if (btSabahDown.isPressed() || btOgleDown.isPressed() || btIkindiDown.isPressed() || btAksamDown.isPressed() || btYatsiDown.isPressed()) {
            // Azalt butonlarına basıldığında veri -1 azaltıldı.
            if (value == 0) {

            } else {
                value--;
            }

        }

        String incomingId = model.modelId("1");
        String newDate = model.modelDate(currentlyDate()); // Geçerli tarih model içindeki date değişkenine gönderildi
        String newVakit = model.modelVakit(String.valueOf(value)); // AlertDialog'daki veri model içindeki sabah değişkenine gönderildi

        text1.setText(newVakit);
        text2.setText(newDate);

        sqliteHelper.updateData(incomingId, newDate, newVakit, tableName);
    }

    // REKLAM
    public void advertising() {
        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adsBanner.loadAd(adRequest);
    }

    // KULLANICI CİHAZI MARKA VE MODELİ
    public String deviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}