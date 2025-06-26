package com.nutrevida.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.nutrevida.app.modelos.RegistroIMC; // Explicit import
import java.util.List;

public class HistorialActivity extends AppCompatActivity implements HistorialAdapter.OnRegistroEliminadoListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewHistorial;
    private HistorialAdapter adapter;
    private TextView tvTotalRegistros, tvUltimoIMC;
    private MaterialCardView layoutSinRegistros;
    private ImageButton btnVolver, btnLimpiarHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar vistas
        inicializarVistas();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Configurar botones
        configurarBotones();

        // Cargar datos
        cargarHistorial();
    }

    private void inicializarVistas() {
        recyclerViewHistorial = findViewById(R.id.recyclerViewHistorial);
        tvTotalRegistros = findViewById(R.id.tvTotalRegistros);
        tvUltimoIMC = findViewById(R.id.tvUltimoIMC);
        layoutSinRegistros = findViewById(R.id.layoutSinRegistros);
        btnVolver = findViewById(R.id.btnVolver);
        btnLimpiarHistorial = findViewById(R.id.btnLimpiarHistorial);
    }

    private void configurarRecyclerView() {
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistorialAdapter(this, databaseHelper.obtenerTodosLosRegistros());
        adapter.setOnRegistroEliminadoListener(this);
        recyclerViewHistorial.setAdapter(adapter);
    }

    private void configurarBotones() {
        btnVolver.setOnClickListener(v -> finish());

        btnLimpiarHistorial.setOnClickListener(v -> mostrarDialogoLimpiarHistorial());
    }

    private void cargarHistorial() {
        List<RegistroIMC> registros = databaseHelper.obtenerTodosLosRegistros();

        if (registros.isEmpty()) {
            // Mostrar mensaje de sin registros
            layoutSinRegistros.setVisibility(View.VISIBLE);
            recyclerViewHistorial.setVisibility(View.GONE);
            tvTotalRegistros.setText("0");
            tvUltimoIMC.setText("--");
        } else {
            // Mostrar lista de registros
            layoutSinRegistros.setVisibility(View.GONE);
            recyclerViewHistorial.setVisibility(View.VISIBLE);

            // Actualizar estadísticas
            tvTotalRegistros.setText(String.valueOf(registros.size()));
            tvUltimoIMC.setText(String.format("%.1f", registros.get(0).getImc()));

            // Actualizar adapter
            adapter.actualizarRegistros(registros);
        }
    }

    @Override
    public void onRegistroEliminado(RegistroIMC registro) {
        boolean eliminado = databaseHelper.eliminarRegistro(registro.getId());

        if (eliminado) {
            Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
            cargarHistorial(); // Recargar la lista
        } else {
            Toast.makeText(this, "Error al eliminar el registro", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogoLimpiarHistorial() {
        new AlertDialog.Builder(this)
                .setTitle("Limpiar historial")
                .setMessage("¿Estás seguro de que quieres eliminar TODO el historial de IMC? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar todo", (dialog, which) -> limpiarHistorialCompleto())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void limpiarHistorialCompleto() {
        boolean eliminado = databaseHelper.eliminarTodosRegistros();

        if (eliminado) {
            Toast.makeText(this, "Historial eliminado completamente", Toast.LENGTH_SHORT).show();
            cargarHistorial(); // Recargar para mostrar vista vacía
        } else {
            Toast.makeText(this, "Error al eliminar el historial", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando se regrese a esta pantalla
        cargarHistorial();
    }
}