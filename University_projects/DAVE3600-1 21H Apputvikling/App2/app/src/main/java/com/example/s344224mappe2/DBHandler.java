package com.example.s344224mappe2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    static final String TABLE_RESTAURANTER = "Restauranter";
    static final String RESTAURANTER_KEY_ID = "_id";
    static final String RESTAURANTER_KEY_NAME = "Navn";
    static final String RESTAURANTER_KEY_ADRESSE = "Adresse";
    static final String RESTAURANTER_KEY_TELEFON = "Telefon";
    static final String RESTAURANTER_KEY_TYPE = "Type";

    static final String TABLE_VENNER = "Venner";
    static final String VENNER_KEY_ID = "_id";
    static final String VENNER_KEY_FIRSTNAME = "Fornavn";
    static final String VENNER_KEY_LASTNAME = "Etternavn";
    static final String VENNER_KEY_TELEFON = "Telefon";

    static final String TABLE_BESTILLINGER = "Bestillinger";
    static final String BESTILLINGER_KEY_ID = "_id";
    static final String BESTILLINGER_KEY_RESTAURANT_ID = "Restaurant_id";
    static final String BESTILLINGER_KEY_DATO = "Dato";
    static final String BESTILLINGER_KEY_TID = "Tidspunkt";

    static final String TABLE_BESTILLINGER_VENNER = "Bestillinger_Venner";
    static final String BESTILLINGER_VENNER_KEY_BESTILLING_ID = "Bestilling_id";
    static final String BESTILLINGER_VENNER_KEY_VENN_ID = "Venn_id";

    static String DATABASE_NAME = "Restaurant_bestillinger";
    static int DATABASE_VERSION = 4;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_RESTAURANTER_TABELL = "CREATE TABLE " + TABLE_RESTAURANTER + "(" + RESTAURANTER_KEY_ID + " INTEGER PRIMARY KEY," + RESTAURANTER_KEY_NAME + " TEXT," + RESTAURANTER_KEY_ADRESSE + " TEXT," + RESTAURANTER_KEY_TELEFON + " TEXT," + RESTAURANTER_KEY_TYPE + " TEXT" + ")";
        String LAG_VENNER_TABELL = "CREATE TABLE " + TABLE_VENNER + "(" + VENNER_KEY_ID + " INTEGER PRIMARY KEY," + VENNER_KEY_FIRSTNAME + " TEXT," + VENNER_KEY_LASTNAME + " TEXT," + VENNER_KEY_TELEFON + " TEXT" + ")";
        String LAG_BESTILLINGER_TABELL = "CREATE TABLE "
                + TABLE_BESTILLINGER + " ("
                + BESTILLINGER_KEY_ID + " INTEGER PRIMARY KEY, "
                + BESTILLINGER_KEY_RESTAURANT_ID + " INTEGER, "
                + BESTILLINGER_KEY_DATO + " TEXT, "
                + BESTILLINGER_KEY_TID + " TEXT, "
                + "FOREIGN KEY ("+BESTILLINGER_KEY_RESTAURANT_ID+") REFERENCES "+TABLE_RESTAURANTER+"("+RESTAURANTER_KEY_ID+") ON DELETE CASCADE)";
        String LAG_BESTILLINGER_VENNER_TABELL = "CREATE TABLE "
                + TABLE_BESTILLINGER_VENNER + " ("
                + BESTILLINGER_VENNER_KEY_BESTILLING_ID + " INTEGER, "
                + BESTILLINGER_VENNER_KEY_VENN_ID + " INTEGER, "
                + "FOREIGN KEY ("+BESTILLINGER_VENNER_KEY_BESTILLING_ID+") REFERENCES "+TABLE_BESTILLINGER+"("+BESTILLINGER_KEY_ID+") ON DELETE CASCADE, "
                + "FOREIGN KEY ("+BESTILLINGER_VENNER_KEY_VENN_ID+") REFERENCES "+TABLE_VENNER+"("+VENNER_KEY_ID+") ON DELETE CASCADE, "
                + "PRIMARY KEY ("+BESTILLINGER_VENNER_KEY_BESTILLING_ID+", "+BESTILLINGER_VENNER_KEY_VENN_ID+"))";
        Log.d("SQL", LAG_RESTAURANTER_TABELL);
        Log.d("SQL", LAG_VENNER_TABELL);
        Log.d("SQL", LAG_BESTILLINGER_TABELL);
        Log.d("SQL", LAG_BESTILLINGER_VENNER_TABELL);
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(LAG_RESTAURANTER_TABELL);
        db.execSQL(LAG_VENNER_TABELL);
        db.execSQL(LAG_BESTILLINGER_TABELL);
        db.execSQL(LAG_BESTILLINGER_VENNER_TABELL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BESTILLINGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BESTILLINGER_VENNER);
        onCreate(db);
    }

    //Bestillinger tabell
    public void leggTilBestilling(Bestilling bestilling) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        values.put(BESTILLINGER_KEY_RESTAURANT_ID, bestilling.getRestaurant().get_id());
        values.put(BESTILLINGER_KEY_DATO, dateFormat.format(bestilling.getDato()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        values.put(BESTILLINGER_KEY_TID, timeFormat.format(bestilling.getTidspunkt()));
        long id = db.insert(TABLE_BESTILLINGER, null, values);
        if (id != -1) leggTilVennerForBestilling(id, bestilling.getVenner());
        db.close();
    }

    public void leggTilVennerForBestilling(long id, List<Venn> venner) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        for (Venn venn : venner) {
            ContentValues values = new ContentValues();
            values.put(BESTILLINGER_VENNER_KEY_BESTILLING_ID, id);
            values.put(BESTILLINGER_VENNER_KEY_VENN_ID, venn.get_id());
            db.insert(TABLE_BESTILLINGER_VENNER, null, values);
        }
        db.close();
    }

    public List<Bestilling> getAlleBestillinger() {
        List<Bestilling> bestillinger = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        String selectQuery= "SELECT "
                + TABLE_BESTILLINGER+"."+BESTILLINGER_KEY_ID+" AS b_id, "
                + BESTILLINGER_KEY_DATO+", "
                + BESTILLINGER_KEY_TID+", "
                + TABLE_BESTILLINGER+"."+BESTILLINGER_KEY_RESTAURANT_ID+" AS r_id, "
                + RESTAURANTER_KEY_NAME+", "
                + RESTAURANTER_KEY_ADRESSE+", "
                + RESTAURANTER_KEY_TELEFON+", "
                + RESTAURANTER_KEY_TYPE
                + " FROM "+TABLE_BESTILLINGER
                + " INNER JOIN "+ TABLE_RESTAURANTER +" ON "+TABLE_BESTILLINGER+"."+BESTILLINGER_KEY_RESTAURANT_ID+" = "+TABLE_RESTAURANTER+"."+RESTAURANTER_KEY_ID;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Bestilling bestilling = new Bestilling();
                bestilling.set_id(cursor.getLong(0));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date date = null;
                Date time = null;
                try {
                    date = dateFormat.parse(cursor.getString(1));
                    time = timeFormat.parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bestilling.setDato(date);
                bestilling.setTidspunkt(time);
                bestilling.setRestaurant(new Restaurant(
                        cursor.getLong(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)
                ));
                List<Venn> venner = getAlleVennerIBestilling(cursor.getLong(0));
                bestilling.setVenner(venner);
                bestillinger.add(bestilling);
            }
            while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return bestillinger;
    }

    public List<Venn> getAlleVennerIBestilling(long bestilling_id) {
        List<Venn> venner = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        String selectQuery= "SELECT "
                + BESTILLINGER_VENNER_KEY_BESTILLING_ID+", "
                + BESTILLINGER_VENNER_KEY_VENN_ID+", "
                + VENNER_KEY_FIRSTNAME+", "
                + VENNER_KEY_LASTNAME+", "
                + VENNER_KEY_TELEFON
                + " FROM "+TABLE_BESTILLINGER_VENNER
                + " INNER JOIN "+ TABLE_VENNER +" ON "+BESTILLINGER_VENNER_KEY_VENN_ID+" = "+VENNER_KEY_ID
                + " WHERE "+ BESTILLINGER_VENNER_KEY_BESTILLING_ID+" = "+bestilling_id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Venn venn = new Venn();
                venn.set_id(cursor.getLong(1));
                venn.setFirstName(cursor.getString(2));
                venn.setLastName(cursor.getString(3));
                venn.setTelefon(cursor.getString(4));
                venner.add(venn);
            }
            while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return venner;
    }

    //Venner tabell
    public void leggTilVenn(Venn venn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues values = new ContentValues();
        values.put(VENNER_KEY_FIRSTNAME, venn.getFirstName());
        values.put(VENNER_KEY_LASTNAME, venn.getLastName());
        values.put(VENNER_KEY_TELEFON, venn.getTelefon());
        db.insert(TABLE_VENNER, null, values);
        db.close();
    }

    public List<Venn> getAlleVenner() {
        List<Venn> list = new ArrayList<>();
        String selectQuery= "SELECT * FROM " + TABLE_VENNER;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Venn venn = new Venn();
                venn.set_id(cursor.getLong(0));
                venn.setFirstName(cursor.getString(1));
                venn.setLastName(cursor.getString(2));
                venn.setTelefon(cursor.getString(3));
                list.add(venn);
            }
            while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return list;
    }

    public Venn getEnVenn(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        Cursor cursor = db.query(TABLE_VENNER, new String[] {
                VENNER_KEY_ID, VENNER_KEY_FIRSTNAME, VENNER_KEY_LASTNAME, VENNER_KEY_TELEFON
        }, VENNER_KEY_ID + "=?", new String[] {
                String.valueOf(id)
        }, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();
        Venn venn = new Venn(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        db.close();
        return venn;
    }

    public void slettVenn(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        db.delete(TABLE_VENNER, VENNER_KEY_ID + " =? ", new String[] {
                Long.toString(inn_id)
        });
        db.close();
    }

    public int oppdaterVenn(Venn venn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues values = new ContentValues();
        values.put(VENNER_KEY_FIRSTNAME, venn.getFirstName());
        values.put(VENNER_KEY_LASTNAME, venn.getLastName());
        values.put(VENNER_KEY_TELEFON, venn.getTelefon());
        int endret = db.update(TABLE_VENNER, values, VENNER_KEY_ID + "= ?", new String[] {
                String.valueOf(venn.get_id())
        });
        db.close();
        return endret;
    }

    //Restauranter tabell
    public boolean leggTilRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues values = new ContentValues();
        values.put(RESTAURANTER_KEY_NAME, restaurant.getNavn());
        values.put(RESTAURANTER_KEY_ADRESSE, restaurant.getAdresse());
        values.put(RESTAURANTER_KEY_TELEFON, restaurant.getTelefon());
        values.put(RESTAURANTER_KEY_TYPE, restaurant.getType());
        Long result = db.insert(TABLE_RESTAURANTER, null, values);
        db.close();
        if (result == -1) return false;
        else return true;
    }

    public List<Restaurant> getAlleRestauranter() {
        List<Restaurant> list = new ArrayList<>();
        String selectQuery= "SELECT * FROM " + TABLE_RESTAURANTER;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                restaurant.set_id(cursor.getLong(0));
                restaurant.setNavn(cursor.getString(1));
                restaurant.setAdresse(cursor.getString(2));
                restaurant.setTelefon(cursor.getString(3));
                restaurant.setType(cursor.getString(4));
                list.add(restaurant);
            }
            while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return list;
    }

    public Restaurant getEnRestaurant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        Cursor cursor = db.query(TABLE_RESTAURANTER, new String[] {
                RESTAURANTER_KEY_ID, RESTAURANTER_KEY_NAME, RESTAURANTER_KEY_ADRESSE, RESTAURANTER_KEY_TELEFON, RESTAURANTER_KEY_TYPE
        }, RESTAURANTER_KEY_ID + "=?", new String[] {
                String.valueOf(id)
        }, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();
        Restaurant restaurant = new Restaurant(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        cursor.close();
        db.close();
        return restaurant;
    }

    public boolean slettRestaurant(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        long result = db.delete(TABLE_RESTAURANTER, RESTAURANTER_KEY_ID + " =? ", new String[] {
                Long.toString(inn_id)
        });
        db.close();
        if (result == -1) return false;
        else return true;
    }

    public int oppdaterRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues values = new ContentValues();
        values.put(RESTAURANTER_KEY_NAME, restaurant.getNavn());
        values.put(RESTAURANTER_KEY_ADRESSE, restaurant.getAdresse());
        values.put(RESTAURANTER_KEY_TELEFON, restaurant.getTelefon());
        values.put(RESTAURANTER_KEY_TYPE, restaurant.getType());
        int endret = db.update(TABLE_RESTAURANTER, values, RESTAURANTER_KEY_ID + "= ?", new String[] {
                String.valueOf(restaurant.get_id())
        });
        db.close();
        return endret;
    }

    public int oppdaterBestilling(Bestilling bestilling) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues values = new ContentValues();
        values.put(BESTILLINGER_KEY_RESTAURANT_ID, bestilling.getRestaurant().get_id());
        values.put(BESTILLINGER_KEY_DATO, new SimpleDateFormat("dd-MM-yyyy").format(bestilling.getDato()));
        values.put(BESTILLINGER_KEY_TID, new SimpleDateFormat("HH:mm").format(bestilling.getTidspunkt()));
        int endret = db.update(TABLE_BESTILLINGER, values, BESTILLINGER_KEY_ID + "= ?", new String[] {
                String.valueOf(bestilling.get_id())
        });
        slettVennerIBestilling(bestilling.get_id());
        leggTilVennerForBestilling(bestilling.get_id(), bestilling.getVenner());
        db.close();
        return endret;
    }

    public boolean slettVennerIBestilling(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        long result = db.delete(TABLE_BESTILLINGER_VENNER, BESTILLINGER_VENNER_KEY_BESTILLING_ID + " =? ", new String[] {
                Long.toString(inn_id)
        });
        db.close();
        if (result == -1) return false;
        else return true;
    }

    public boolean slettBestilling(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        long result = db.delete(TABLE_BESTILLINGER, BESTILLINGER_KEY_ID + " =? ", new String[] {
                Long.toString(inn_id)
        });
        db.close();
        if (result == -1) return false;
        else return true;
    }
}
