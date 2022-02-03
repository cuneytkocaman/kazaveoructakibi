package com.cuneyt.kazaveoructakibim.dataAccess;

public class SqliteInfo {

    public static final String DB_NAME = "KazaOrucTakibi";
    public static final String TABLE_NAME_SABAH = "SabahNamazi";
    public static final String TABLE_NAME_OGLE = "OgleNamazi";
    public static final String TABLE_NAME_IKINDI = "IkindiNamazi";
    public static final String TABLE_NAME_AKSAM = "AksamNamazi";
    public static final String TABLE_NAME_YATSI = "YatsiNamazi";
    public static final String TABLE_NAME_ORUC = "Oruc";
    public static final int VERSION = 1;
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String VAKIT = "vakit";

    public static final String CREATE_SABAH = "CREATE TABLE " + TABLE_NAME_SABAH + "("
            + ID + " INTEGER PRIMARY KEY,"
            + DATE + " TEXT,"
            + VAKIT + " TEXT"
            + ")";

    public static final String CREATE_OGLE = "CREATE TABLE " + TABLE_NAME_OGLE + "("
            + ID + " INTEGER PRIMARY KEY,"
            + DATE + " TEXT,"
            + VAKIT + " TEXT"
            + ")";

    public static final String CREATE_IKINDI = "CREATE TABLE " + TABLE_NAME_IKINDI + "("
            + ID + " INTEGER PRIMARY KEY,"
            + DATE + " TEXT,"
            + VAKIT + " TEXT"
            + ")";

    public static final String CREATE_AKSAM = "CREATE TABLE " + TABLE_NAME_AKSAM + "("
            + ID + " INTEGER PRIMARY KEY,"
            + DATE + " TEXT,"
            + VAKIT + " TEXT"
            + ")";

    public static final String CREATE_YATSI = "CREATE TABLE " + TABLE_NAME_YATSI + "("
            + ID + " INTEGER PRIMARY KEY,"
            + DATE + " TEXT,"
            + VAKIT + " TEXT"
            + ")";

    public static final String CREATE_ORUC = "CREATE TABLE " + TABLE_NAME_ORUC + "("
            + ID + " INTEGER PRIMARY KEY,"
            + DATE + " TEXT,"
            + VAKIT + " TEXT"
            + ")";

    public static final String DROP_SABAH = "DROP TABLE IF EXISTS " + TABLE_NAME_SABAH;
    public static final String DROP_OGLE = "DROP TABLE IF EXISTS " + TABLE_NAME_OGLE;
    public static final String DROP_IKINDI = "DROP TABLE IF EXISTS " + TABLE_NAME_IKINDI;
    public static final String DROP_AKSAM = "DROP TABLE IF EXISTS " + TABLE_NAME_AKSAM;
    public static final String DROP_YATSI = "DROP TABLE IF EXISTS " + TABLE_NAME_YATSI;
    public static final String DROP_ORUC = "DROP TABLE IF EXISTS " + TABLE_NAME_ORUC;

    public static final String SELECT_SABAH = "SELECT * FROM " + TABLE_NAME_SABAH;
    public static final String SELECT_OGLE = "SELECT * FROM " + TABLE_NAME_OGLE;
    public static final String SELECT_IKINDI = "SELECT * FROM " + TABLE_NAME_IKINDI;
    public static final String SELECT_AKSAM = "SELECT * FROM " + TABLE_NAME_AKSAM;
    public static final String SELECT_YATSI = "SELECT * FROM " + TABLE_NAME_YATSI;
    public static final String SELECT_ORUC = "SELECT * FROM " + TABLE_NAME_ORUC;
}
