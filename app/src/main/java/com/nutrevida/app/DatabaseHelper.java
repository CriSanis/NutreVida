package com.nutrevida.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nutrevida.app.modelos.Alimento;
import com.nutrevida.app.modelos.ConsumoDiario;
import com.nutrevida.app.modelos.RegistroAgua;
import com.nutrevida.app.modelos.RegistroIMC;
import com.nutrevida.app.modelos.Ejercicio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Información de la base de datos
    private static final String DATABASE_NAME = "nutrevida.db";
    private static final int DATABASE_VERSION = 5;

    // Columnas comunes
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FECHA = "fecha";

    // Tabla IMC
    private static final String TABLE_IMC = "imc";
    private static final String COLUMN_PESO = "peso";
    private static final String COLUMN_ALTURA = "altura";
    private static final String COLUMN_IMC = "imc";
    private static final String COLUMN_RESULTADO = "resultado";
    private static final String CREATE_TABLE_IMC = "CREATE TABLE " + TABLE_IMC + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PESO + " REAL NOT NULL,"
            + COLUMN_ALTURA + " REAL NOT NULL,"
            + COLUMN_IMC + " REAL NOT NULL,"
            + COLUMN_RESULTADO + " TEXT NOT NULL,"
            + COLUMN_FECHA + " TEXT NOT NULL"
            + ")";

    // Tabla Alimentos
    private static final String TABLE_ALIMENTOS = "alimentos";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_CALORIAS = "calorias";
    private static final String COLUMN_PROTEINAS = "proteinas";
    private static final String COLUMN_CARBOHIDRATOS = "carbohidratos";
    private static final String COLUMN_GRASAS = "grasas";
    private static final String CREATE_TABLE_ALIMENTOS = "CREATE TABLE " + TABLE_ALIMENTOS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOMBRE + " TEXT NOT NULL,"
            + COLUMN_CALORIAS + " REAL NOT NULL,"
            + COLUMN_PROTEINAS + " REAL NOT NULL,"
            + COLUMN_CARBOHIDRATOS + " REAL NOT NULL,"
            + COLUMN_GRASAS + " REAL NOT NULL"
            + ")";

    // Tabla Consumo Diario
    private static final String TABLE_CONSUMO_DIARIO = "consumo_diario";
    private static final String COLUMN_ALIMENTO_ID = "alimento_id";
    private static final String COLUMN_CANTIDAD = "cantidad";
    private static final String CREATE_TABLE_CONSUMO_DIARIO = "CREATE TABLE " + TABLE_CONSUMO_DIARIO + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ALIMENTO_ID + " INTEGER NOT NULL,"
            + COLUMN_CANTIDAD + " REAL NOT NULL,"
            + COLUMN_FECHA + " TEXT NOT NULL,"
            + "FOREIGN KEY(" + COLUMN_ALIMENTO_ID + ") REFERENCES " + TABLE_ALIMENTOS + "(" + COLUMN_ID + ")"
            + ")";

    // Tabla Registro Agua
    private static final String TABLE_AGUA = "registro_agua";
    private static final String COLUMN_HORA = "hora";
    private static final String CREATE_TABLE_AGUA = "CREATE TABLE " + TABLE_AGUA + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CANTIDAD + " REAL NOT NULL,"
            + COLUMN_FECHA + " TEXT NOT NULL,"
            + COLUMN_HORA + " TEXT NOT NULL"
            + ")";
    // Tabla de rutinas
    private static final String TABLE_RUTINAS = "rutinas";
    private static final String COLUMN_RUTINA_ID = "rutina_id";
    private static final String COLUMN_RUTINA_FECHA = "rutina_fecha";
    private static final String COLUMN_TIPO = "tipo";
    private static final String COLUMN_DESCRIPCION = "descripcion";

    private static final String CREATE_TABLE_RUTINAS = "CREATE TABLE " + TABLE_RUTINAS + " (" +
            COLUMN_RUTINA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_RUTINA_FECHA + " TEXT, " +
            COLUMN_TIPO + " TEXT, " +
            COLUMN_DESCRIPCION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_IMC);
        db.execSQL(CREATE_TABLE_ALIMENTOS);
        db.execSQL(CREATE_TABLE_CONSUMO_DIARIO);
        db.execSQL(CREATE_TABLE_AGUA);
        db.execSQL(CREATE_TABLE_EJERCICIOS);
        db.execSQL(CREATE_TABLE_RUTINAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            db.execSQL(CREATE_TABLE_RUTINAS);
        } else {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIMENTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMO_DIARIO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGUA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EJERCICIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUTINAS);
            onCreate(db);
        }
    }

    public void addRutina(String fecha, String tipo, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RUTINA_FECHA, fecha);
        values.put(COLUMN_TIPO, tipo);
        values.put(COLUMN_DESCRIPCION, descripcion);
        db.insert(TABLE_RUTINAS, null, values);
        db.close();
    }

    public List<Rutina> getRutinasByDate(String fecha) {
        List<Rutina> rutinas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_RUTINAS,
                    new String[]{COLUMN_RUTINA_ID, COLUMN_RUTINA_FECHA, COLUMN_TIPO, COLUMN_DESCRIPCION},
                    COLUMN_RUTINA_FECHA + " = ?",
                    new String[]{fecha},
                    null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Rutina rutina = new Rutina(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RUTINA_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RUTINA_FECHA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                    );
                    rutinas.add(rutina);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Log para depuración
            android.util.Log.e("DatabaseHelper", "Error en getRutinasByDate: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return rutinas;
    }

    // Tabla Ejercicios
    private static final String TABLE_EJERCICIOS = "ejercicios";
    private static final String COLUMN_DURACION = "duracion";
    private static final String COLUMN_CALORIAS_QUEMADAS = "calorias_quemadas";
    private static final String CREATE_TABLE_EJERCICIOS = "CREATE TABLE " + TABLE_EJERCICIOS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOMBRE + " TEXT NOT NULL,"
            + COLUMN_DURACION + " INTEGER NOT NULL,"
            + COLUMN_CALORIAS_QUEMADAS + " REAL NOT NULL,"
            + COLUMN_FECHA + " TEXT NOT NULL"
            + ")";

    // Métodos para IMC
    public long insertarRegistroIMC(double peso, double altura, double imc, String resultado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PESO, peso);
        values.put(COLUMN_ALTURA, altura);
        values.put(COLUMN_IMC, imc);
        values.put(COLUMN_RESULTADO, resultado);
        values.put(COLUMN_FECHA, getCurrentDate());

        long id = db.insert(TABLE_IMC, null, values);
        db.close();
        return id;
    }

    public List<RegistroIMC> obtenerTodosLosRegistros() {
        List<RegistroIMC> registros = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_IMC + " ORDER BY " + COLUMN_FECHA + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegistroIMC registro = new RegistroIMC();
                registro.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                registro.setPeso(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PESO)));
                registro.setAltura(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ALTURA)));
                registro.setImc(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_IMC)));
                registro.setResultado(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESULTADO)));
                registro.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)));
                registros.add(registro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return registros;
    }

    public boolean eliminarRegistro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_IMC, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas > 0;
    }

    public boolean eliminarTodosRegistros() {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_IMC, null, null);
        db.close();
        return filasAfectadas > 0;
    }

    // Métodos para Alimentos
    public long insertarAlimento(Alimento alimento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, alimento.getNombre());
        values.put(COLUMN_CALORIAS, alimento.getCalorias());
        values.put(COLUMN_PROTEINAS, alimento.getProteinas());
        values.put(COLUMN_CARBOHIDRATOS, alimento.getCarbohidratos());
        values.put(COLUMN_GRASAS, alimento.getGrasas());

        long id = db.insert(TABLE_ALIMENTOS, null, values);
        db.close();
        return id;
    }

    public List<Alimento> obtenerTodosLosAlimentos() {
        List<Alimento> alimentos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ALIMENTOS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Alimento alimento = new Alimento();
                alimento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                alimento.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
                alimento.setCalorias(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIAS)));
                alimento.setProteinas(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PROTEINAS)));
                alimento.setCarbohidratos(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CARBOHIDRATOS)));
                alimento.setGrasas(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GRASAS)));
                alimentos.add(alimento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return alimentos;
    }

    // Métodos para Consumo Diario
    public long insertarConsumoDiario(ConsumoDiario consumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALIMENTO_ID, consumo.getAlimentoId());
        values.put(COLUMN_CANTIDAD, consumo.getCantidad());
        values.put(COLUMN_FECHA, consumo.getFecha());

        long id = db.insert(TABLE_CONSUMO_DIARIO, null, values);
        db.close();
        return id;
    }

    public List<ConsumoDiario> obtenerConsumoPorFecha(String fecha) {
        List<ConsumoDiario> consumos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONSUMO_DIARIO + " WHERE " + COLUMN_FECHA + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{fecha});

        if (cursor.moveToFirst()) {
            do {
                ConsumoDiario consumo = new ConsumoDiario();
                consumo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                consumo.setAlimentoId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALIMENTO_ID)));
                consumo.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)));
                consumo.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)));
                consumos.add(consumo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return consumos;
    }

    public boolean eliminarConsumoDiario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_CONSUMO_DIARIO, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas > 0;
    }

    public boolean eliminarConsumosPorFecha(String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_CONSUMO_DIARIO, COLUMN_FECHA + " = ?", new String[]{fecha});
        db.close();
        return filasAfectadas > 0;
    }

    public double obtenerCaloriasTotalesPorFecha(String fecha) {
        double totalCalorias = 0;
        String query = "SELECT cd." + COLUMN_CANTIDAD + ", a." + COLUMN_CALORIAS
                + " FROM " + TABLE_CONSUMO_DIARIO + " cd"
                + " INNER JOIN " + TABLE_ALIMENTOS + " a ON cd." + COLUMN_ALIMENTO_ID + " = a." + COLUMN_ID
                + " WHERE cd." + COLUMN_FECHA + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{fecha});

        if (cursor.moveToFirst()) {
            do {
                double cantidad = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD));
                double caloriasPor100g = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIAS));
                totalCalorias += (cantidad / 100.0) * caloriasPor100g;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return totalCalorias;
    }

    // Métodos para Registro de Agua
    public long insertarRegistroAgua(double cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANTIDAD, cantidad);
        values.put(COLUMN_FECHA, getCurrentDate());
        values.put(COLUMN_HORA, getCurrentTime());

        long id = db.insert(TABLE_AGUA, null, values);
        db.close();
        return id;
    }

    public List<RegistroAgua> obtenerRegistrosAguaPorFecha(String fecha) {
        List<RegistroAgua> registros = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_AGUA + " WHERE " + COLUMN_FECHA + " = ? ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{fecha});

        if (cursor.moveToFirst()) {
            do {
                RegistroAgua registro = new RegistroAgua();
                registro.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                registro.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)));
                registro.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)));
                registro.setHora(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)));
                registros.add(registro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return registros;
    }

    public boolean eliminarRegistroAgua(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_AGUA, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas > 0;
    }

    public boolean eliminarRegistrosAguaPorFecha(String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_AGUA, COLUMN_FECHA + " = ?", new String[]{fecha});
        db.close();
        return filasAfectadas > 0;
    }

    public double obtenerTotalAguaPorFecha(String fecha) {
        String query = "SELECT SUM(" + COLUMN_CANTIDAD + ") FROM " + TABLE_AGUA + " WHERE " + COLUMN_FECHA + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{fecha});

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    // Métodos utilitarios
    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
    // Métodos para Ejercicios
    public long insertarEjercicio(String nombre, int duracion, double caloriasQuemadas, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_DURACION, duracion);
        values.put(COLUMN_CALORIAS_QUEMADAS, caloriasQuemadas);
        values.put(COLUMN_FECHA, fecha);

        long id = db.insert(TABLE_EJERCICIOS, null, values);
        db.close();
        return id;
    }

    public List<Ejercicio> obtenerEjerciciosPorFecha(String fecha) {
        List<Ejercicio> ejercicios = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EJERCICIOS + " WHERE " + COLUMN_FECHA + " = ? ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{fecha});

        if (cursor.moveToFirst()) {
            do {
                Ejercicio ejercicio = new Ejercicio();
                ejercicio.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                ejercicio.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
                ejercicio.setDuracion(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURACION)));
                ejercicio.setCaloriasQuemadas(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIAS_QUEMADAS)));
                ejercicio.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)));
                ejercicios.add(ejercicio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ejercicios;
    }

    public boolean eliminarEjercicio(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_EJERCICIOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas > 0;
    }

    public boolean eliminarEjerciciosPorFecha(String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_EJERCICIOS, COLUMN_FECHA + " = ?", new String[]{fecha});
        db.close();
        return filasAfectadas > 0;
    }

    public double obtenerTotalCaloriasQuemadasPorFecha(String fecha) {
        String query = "SELECT SUM(" + COLUMN_CALORIAS_QUEMADAS + ") FROM " + TABLE_EJERCICIOS + " WHERE " + COLUMN_FECHA + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{fecha});

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }
    public RegistroIMC obtenerUltimoIMC(String fecha) {
        RegistroIMC registro = null;
        String query = "SELECT * FROM " + TABLE_IMC + " WHERE " + COLUMN_FECHA + " <= ? ORDER BY " + COLUMN_FECHA + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{fecha});
        if (cursor.moveToFirst()) {
            registro = new RegistroIMC();
            registro.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            registro.setPeso(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PESO)));
            registro.setAltura(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ALTURA)));
            registro.setImc(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_IMC)));
            registro.setResultado(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESULTADO)));
            registro.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)));
        }
        cursor.close();
        db.close();
        return registro;
    }

    public double obtenerTotalCaloriasConsumidas(String fecha) {
        return obtenerCaloriasTotalesPorFecha(fecha);
    }

    public double obtenerTotalAgua(String fecha) {
        double total = 0;
        String query = "SELECT SUM(" + COLUMN_CANTIDAD + ") FROM " + TABLE_AGUA + " WHERE " + COLUMN_FECHA + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{fecha});
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }
}