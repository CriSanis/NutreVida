package com.nutrevida.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nutrevida.app.modelos.Alimento;
import com.nutrevida.app.modelos.ConsumoDiario;

import java.util.ArrayList;
import java.util.List;

public class AgregarAlimentoActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private AutoCompleteTextView autoCompleteAlimentos;
    private EditText etCantidad, etNombre, etCalorias, etProteinas, etCarbohidratos, etGrasas;
    private Button btnAgregarConsumo, btnAgregarNuevoAlimento;
    private ImageButton btnVolver;
    private List<Alimento> alimentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alimento);

        databaseHelper = new DatabaseHelper(this);
        inicializarVistas();
        cargarAlimentos();
        configurarBotones();
    }

    private void inicializarVistas() {
        autoCompleteAlimentos = findViewById(R.id.autoCompleteAlimentos);
        etCantidad = findViewById(R.id.etCantidad);
        etNombre = findViewById(R.id.etNombre);
        etCalorias = findViewById(R.id.etCalorias);
        etProteinas = findViewById(R.id.etProteinas);
        etCarbohidratos = findViewById(R.id.etCarbohidratos);
        etGrasas = findViewById(R.id.etGrasas);
        btnAgregarConsumo = findViewById(R.id.btnAgregarConsumo);
        btnAgregarNuevoAlimento = findViewById(R.id.btnAgregarNuevoAlimento);
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void cargarAlimentos() {
        alimentos = databaseHelper.obtenerTodosLosAlimentos();
        List<String> nombresAlimentos = new ArrayList<>();
        for (Alimento alimento : alimentos) {
            nombresAlimentos.add(alimento.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nombresAlimentos);
        autoCompleteAlimentos.setAdapter(adapter);
        autoCompleteAlimentos.setThreshold(1);
    }

    private void configurarBotones() {
        btnVolver.setOnClickListener(v -> finish());

        btnAgregarConsumo.setOnClickListener(v -> agregarConsumo());

        btnAgregarNuevoAlimento.setOnClickListener(v -> agregarNuevoAlimento());
    }

    private void agregarConsumo() {
        String nombreAlimento = autoCompleteAlimentos.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();

        if (nombreAlimento.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadStr);
            if (cantidad <= 0) {
                Toast.makeText(this, getString(R.string.error_cantidad_invalida), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.error_cantidad_invalida), Toast.LENGTH_SHORT).show();
            return;
        }

        Alimento alimentoSeleccionado = null;
        for (Alimento alimento : alimentos) {
            if (alimento.getNombre().equalsIgnoreCase(nombreAlimento)) {
                alimentoSeleccionado = alimento;
                break;
            }
        }

        if (alimentoSeleccionado == null) {
            Toast.makeText(this, getString(R.string.alimento_no_encontrado), Toast.LENGTH_SHORT).show();
            return;
        }

        ConsumoDiario consumo = new ConsumoDiario();
        consumo.setAlimentoId(alimentoSeleccionado.getId());
        consumo.setCantidad(cantidad);
        consumo.setFecha(databaseHelper.getCurrentDate());

        long id = databaseHelper.insertarConsumoDiario(consumo);
        if (id > 0) {
            Toast.makeText(this, getString(R.string.consumo_agregado), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.error_agregar_consumo), Toast.LENGTH_SHORT).show();
        }
    }

    private void agregarNuevoAlimento() {
        String nombre = etNombre.getText().toString().trim();
        String caloriasStr = etCalorias.getText().toString().trim();
        String proteinasStr = etProteinas.getText().toString().trim();
        String carbohidratosStr = etCarbohidratos.getText().toString().trim();
        String grasasStr = etGrasas.getText().toString().trim();

        if (nombre.isEmpty() || caloriasStr.isEmpty() || proteinasStr.isEmpty() || carbohidratosStr.isEmpty() || grasasStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show();
            return;
        }

        double calorias, proteinas, carbohidratos, grasas;
        try {
            calorias = Double.parseDouble(caloriasStr);
            proteinas = Double.parseDouble(proteinasStr);
            carbohidratos = Double.parseDouble(carbohidratosStr);
            grasas = Double.parseDouble(grasasStr);

            if (calorias < 0 || proteinas < 0 || carbohidratos < 0 || grasas < 0) {
                Toast.makeText(this, getString(R.string.error_valores_negativos), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.error_valores_invalidos), Toast.LENGTH_SHORT).show();
            return;
        }

        Alimento alimento = new Alimento();
        alimento.setNombre(nombre);
        alimento.setCalorias(calorias);
        alimento.setProteinas(proteinas);
        alimento.setCarbohidratos(carbohidratos);
        alimento.setGrasas(grasas);

        long id = databaseHelper.insertarAlimento(alimento);
        if (id > 0) {
            Toast.makeText(this, getString(R.string.alimento_agregado), Toast.LENGTH_SHORT).show();
            cargarAlimentos();
            etNombre.setText("");
            etCalorias.setText("");
            etProteinas.setText("");
            etCarbohidratos.setText("");
            etGrasas.setText("");
        } else {
            Toast.makeText(this, getString(R.string.error_agregar_alimento), Toast.LENGTH_SHORT).show();
        }
    }
}