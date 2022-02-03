package com.cuneyt.kazaveoructakibim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cuneyt.kazaveoructakibim.busines.SqliteHelper;
import com.cuneyt.kazaveoructakibim.dataAccess.SqliteInfo;
import com.cuneyt.kazaveoructakibim.entities.abstracts.VakitModelService;
import com.cuneyt.kazaveoructakibim.entities.concretes.AksamModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.IkindiModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.OgleModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.SabahModel;
import com.cuneyt.kazaveoructakibim.entities.concretes.YatsiModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CalendarActivity extends AppCompatActivity {

    private Toolbar toolbarCalendar;
    private TextView textDate1, textDate2, textResult, textGun;
    private ImageButton btCalc, btClear, btSabah, btOgle, btIkindi, btAksam, btYatsi, btDateAdd1, btDateAdd2;
    private Button btAddUpdateAll;
    private ConstraintLayout constCalcClear, constCaclBt;

    private VakitModelService modelSabah = new SabahModel();
    private VakitModelService modelOgle = new OgleModel();
    private VakitModelService modelIkindi = new IkindiModel();
    private VakitModelService modelAksam = new AksamModel();
    private VakitModelService modelYatsi = new YatsiModel();

    private AdView adsBannerCalendar;
    private SqliteHelper sqliteHelper = new SqliteHelper(this);

    public void visualObject() {
        constCaclBt = findViewById(R.id.constCaclBt);
        constCalcClear = findViewById(R.id.constCalcClear);
        toolbarCalendar = findViewById(R.id.toolbarCalendar);
        textDate1 = findViewById(R.id.textDate1);
        textDate2 = findViewById(R.id.textDate2);
        textResult = findViewById(R.id.textResult);
        textGun = findViewById(R.id.textGun);
        btCalc = findViewById(R.id.btCalc);
        btClear = findViewById(R.id.btClear);
        btSabah = findViewById(R.id.btSabah);
        btOgle = findViewById(R.id.btOgle);
        btIkindi = findViewById(R.id.btIkindi);
        btAksam = findViewById(R.id.btAksam);
        btYatsi = findViewById(R.id.btYatsi);
        btDateAdd1 = findViewById(R.id.btDateAdd1);
        btDateAdd2 = findViewById(R.id.btDateAdd2);
        adsBannerCalendar = findViewById(R.id.adsBannerCalendar);
        btAddUpdateAll = findViewById(R.id.btAddUpdateAll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        visualObject();
        advertising();

        // Toolbar
        toolbarCalendar.setTitle("İki Tarih Arasını Hesapla");
        toolbarCalendar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbarCalendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Toolbar geri butonu.

        btDateAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarChoise(textDate1);
            }
        });

        btDateAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarChoise(textDate2);
            }
        });

        calcButtonOperation();
        addButtonOperation();
    }

    public void toastMessage(){
        Toast.makeText(getApplicationContext(), "Tarih hesaplaması yapınız.", Toast.LENGTH_SHORT).show();
    }

    // HESAPLA VE TEMİZLE BUTONLARI
    public void calcButtonOperation() {

        btCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textDate1.getText().equals("tarih") && textDate2.getText().equals("tarih")){
                    Toast.makeText(getApplicationContext(), "Tarihleri seçiniz.", Toast.LENGTH_SHORT).show();

                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");// Tarih formatı

                    String date1 = textDate1.getText().toString();
                    String date2 = textDate2.getText().toString();

                    try {

                        Date text1 = dateFormat.parse(date1);
                        Date text2 = dateFormat.parse(date2);

                        long difference = text1.getTime() - text2.getTime(); // Seçilen iki tarih arasındaki fark hesaplandı
                        long diff;

                        if (difference < 0) {
                            diff = -1 * TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS); // fark sıfırdan küçük çıkarsa -1 ile çarpılıp + değer bulundu.

                            textResult.setText(String.valueOf(diff));

                            textGun.setVisibility(View.VISIBLE);
                            textResult.setVisibility(View.VISIBLE);

                        } else {
                            diff = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

                            textResult.setText(String.valueOf(diff));

                            textGun.setVisibility(View.VISIBLE);
                            textResult.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDate1.setText("");
                textDate2.setText("");
                textResult.setText("sonuc");
                textDate1.setVisibility(View.INVISIBLE);
                textDate2.setVisibility(View.INVISIBLE);
                textResult.setVisibility(View.INVISIBLE);
                textGun.setVisibility(View.INVISIBLE);
            }
        });
    }

    // VAKİTLERE EKLEME YAPAN BUTONLAR
    public void addButtonOperation() {
        btSabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textResult.getText().equals("sonuc")) {
                    toastMessage();
                } else {
                    insertUpdateAlertDialog(modelSabah, SqliteInfo.SELECT_SABAH, sqliteHelper.readData(SqliteInfo.SELECT_SABAH), SqliteInfo.TABLE_NAME_SABAH, "Sabah");
                }
            }
        });

        btOgle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textResult.getText().equals("sonuc")) {
                    toastMessage();
                } else {
                    insertUpdateAlertDialog(modelOgle, SqliteInfo.SELECT_OGLE, sqliteHelper.readData(SqliteInfo.SELECT_OGLE), SqliteInfo.TABLE_NAME_OGLE, "Öğle");
                }
            }
        });

        btIkindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textResult.getText().equals("sonuc")) {
                    toastMessage();
                } else {
                    insertUpdateAlertDialog(modelIkindi, SqliteInfo.SELECT_IKINDI, sqliteHelper.readData(SqliteInfo.SELECT_IKINDI), SqliteInfo.TABLE_NAME_IKINDI, "İkindi");
                }
            }
        });

        btAksam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textResult.getText().equals("sonuc")) {
                    toastMessage();
                } else {
                    insertUpdateAlertDialog(modelAksam, SqliteInfo.SELECT_AKSAM, sqliteHelper.readData(SqliteInfo.SELECT_AKSAM), SqliteInfo.TABLE_NAME_AKSAM, "Akşam");
                }
            }
        });

        btYatsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textResult.getText().equals("sonuc")) {
                    toastMessage();
                } else {
                    insertUpdateAlertDialog(modelYatsi, SqliteInfo.SELECT_YATSI, sqliteHelper.readData(SqliteInfo.SELECT_YATSI), SqliteInfo.TABLE_NAME_YATSI, "Yatsı");
                }
            }
        });

        btAddUpdateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textResult.getText().equals("sonuc")){
                    toastMessage();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                    View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_calendar_design, null);

                    TextView textAlertCalAdd = alertView.findViewById(R.id.textAlertCalAdd);
                    TextView textAlertCalUpdate = alertView.findViewById(R.id.textAlertCalUpdate);
                    TextView textAlertCalClose = alertView.findViewById(R.id.textAlertCalClose);

                    TextView textAlertTitle = alertView.findViewById(R.id.textAlertTitle);
                    textAlertTitle.setText("Tüm Vakitlere Ekle");

                    builder.setView(alertView);
                    AlertDialog alertDialog = builder.create();

                    alertView.findViewById(R.id.textAlertCalAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            allAddData(modelSabah, SqliteInfo.SELECT_SABAH, sqliteHelper.readData(SqliteInfo.SELECT_SABAH), SqliteInfo.TABLE_NAME_SABAH, "Sabah");
                            allAddData(modelOgle, SqliteInfo.SELECT_OGLE, sqliteHelper.readData(SqliteInfo.SELECT_OGLE), SqliteInfo.TABLE_NAME_OGLE, "Öğle");
                            allAddData(modelIkindi, SqliteInfo.SELECT_IKINDI, sqliteHelper.readData(SqliteInfo.SELECT_IKINDI), SqliteInfo.TABLE_NAME_IKINDI, "İkindi");
                            allAddData(modelAksam, SqliteInfo.SELECT_AKSAM, sqliteHelper.readData(SqliteInfo.SELECT_AKSAM), SqliteInfo.TABLE_NAME_AKSAM, "Akşam");
                            allAddData(modelYatsi, SqliteInfo.SELECT_YATSI, sqliteHelper.readData(SqliteInfo.SELECT_YATSI), SqliteInfo.TABLE_NAME_YATSI, "Yatsı");

                            alertDialog.dismiss();
                        }
                    });

                    alertView.findViewById(R.id.textAlertCalUpdate).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            allUpdateData(modelSabah, SqliteInfo.SELECT_SABAH, sqliteHelper.readData(SqliteInfo.SELECT_SABAH), SqliteInfo.TABLE_NAME_SABAH, "Sabah");
                            allUpdateData(modelOgle, SqliteInfo.SELECT_OGLE, sqliteHelper.readData(SqliteInfo.SELECT_OGLE), SqliteInfo.TABLE_NAME_OGLE, "Öğle");
                            allUpdateData(modelIkindi, SqliteInfo.SELECT_IKINDI, sqliteHelper.readData(SqliteInfo.SELECT_IKINDI), SqliteInfo.TABLE_NAME_IKINDI, "İkindi");
                            allUpdateData(modelAksam, SqliteInfo.SELECT_AKSAM, sqliteHelper.readData(SqliteInfo.SELECT_AKSAM), SqliteInfo.TABLE_NAME_AKSAM, "Akşam");
                            allUpdateData(modelYatsi, SqliteInfo.SELECT_YATSI, sqliteHelper.readData(SqliteInfo.SELECT_YATSI), SqliteInfo.TABLE_NAME_YATSI, "Yatsı");

                            alertDialog.dismiss();
                        }
                    });

                    alertView.findViewById(R.id.textAlertCalClose).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();

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

    // EKLEME VE GÜNCELLEME YAPAN ALERT DİALOG İŞLEMLERİ
    public void insertUpdateAlertDialog(VakitModelService model, String selectQuery, Cursor cursor, String tableName, String vakitName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
        View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_calendar_design, null);

        TextView textAlertCalendarAdd = alertView.findViewById(R.id.textAlertCalAdd);
        TextView textAlertCalendarUpdate = alertView.findViewById(R.id.textAlertCalUpdate);
        TextView textAlertCalendarCancel = alertView.findViewById(R.id.textAlertCalClose);

        TextView textAlertTitle = alertView.findViewById(R.id.textAlertTitle);
        textAlertTitle.setText(vakitName + " Namazına Ekle");

        builder.setView(alertView);
        AlertDialog alertDialog = builder.create();

        alertView.findViewById(R.id.textAlertCalAdd).setOnClickListener(new View.OnClickListener() { // Yeni veri ekleyen buton
            @Override
            public void onClick(View v) {

                allAddData(model, selectQuery, cursor, tableName, vakitName); // Ekleme yapan metod

                alertDialog.dismiss();
            }
        });

        alertView.findViewById(R.id.textAlertCalUpdate).setOnClickListener(new View.OnClickListener() { // Geçerli verinin üzerine ekleme yapan buton
            @Override
            public void onClick(View v) {

                allUpdateData(model, selectQuery, cursor, tableName, vakitName); // Güncelleme yapan metod

                alertDialog.dismiss();

            }
        });

        alertView.findViewById(R.id.textAlertCalClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    //VERİ EKLEME
    public void allAddData(VakitModelService model, String selectQuery, Cursor cursor, String tableName, String vakitName){

        String incomingId = model.modelId("1");
        String incomingDate = model.modelDate(currentlyDate());
        String incomingVakit = model.modelVakit(textResult.getText().toString());

        if (cursor.getCount() == 0) { // DB'de veri varmı kontrol edildi.
            sqliteHelper.addData(incomingId, incomingDate, incomingVakit, tableName); // Veri yoksa ekleme yapıldı.
        } else {
            sqliteHelper.updateData(incomingId, incomingDate, incomingVakit, tableName); // Veri varsa güncelleme yapıldı.
        }
    }

    // VERİ GÜNCELLEME
    public void allUpdateData(VakitModelService model, String selectQuery, Cursor cursor, String tableName, String vakitName){

        if (textResult.getText().equals("sonuc")) {
            Toast.makeText(getApplicationContext(), "Tarih hesaplaması yapınız.", Toast.LENGTH_SHORT).show();

        } else {

            String incomingId = model.modelId("1");
            String incomingDate = model.modelDate(currentlyDate());

            Cursor cursor1 = sqliteHelper.readData(selectQuery); // Hangi vaktin butonuna tıklandıysa o vaktin Select sorgusu çalıştırıldı.

            int oldVakitValue;
            int incomingVakitValue = Integer.parseInt(textResult.getText().toString()); // İki takvim arasındaki fark değişkene aktarıldı.

            if (cursor1.getCount() == 0) { // Tabloda veri yoksa ekleme işlemi yap.
                oldVakitValue = 0; // Tabloda veri olmadığı için eski değere sıfır atandı.
                oldVakitValue += incomingVakitValue; // Sıfır atanan eski değer ile iki tarih arasındaki fark toplandı.

                sqliteHelper.addData(incomingId, incomingDate, String.valueOf(oldVakitValue), tableName);

            } else {

                while (cursor1.moveToNext()) { // Bütün tablolar gezildi içinde veri olanlar ve olmayanlara göre veri güncelleme işlemi yapıldı.
                    String dbId = model.modelId(cursor1.getString(0));
                    String dbDate = model.modelDate(cursor1.getString(1));
                    String dbVakit = model.modelDate(cursor1.getString(2));

                    oldVakitValue = Integer.parseInt(dbVakit); // Sorgusu çalıştırılan vaktin kaza değeri değişkene aktarıldı.;
                    oldVakitValue += incomingVakitValue; // Eski değer ile yeni değer toplandı.

                    sqliteHelper.updateData(dbId, dbDate, String.valueOf(oldVakitValue), tableName);
                }
            }
        }
    }

    // TAKVİM
    public void calendarChoise(TextView textView) {

        Calendar calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        int day = calendar1.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker;

        datePicker = new DatePickerDialog(CalendarActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView.setText(dayOfMonth + "." + (month + 1) + "." + year);
                textView.setVisibility(View.VISIBLE);
            }
        }, year, month, day);

        datePicker.setTitle("Tarih Seçiniz");
        datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "Tamam", datePicker);
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "İptal", datePicker);
        datePicker.show();
    }

    // REKLAM
    public void advertising() {
        MobileAds.initialize(CalendarActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adsBannerCalendar.loadAd(adRequest);
    }
}