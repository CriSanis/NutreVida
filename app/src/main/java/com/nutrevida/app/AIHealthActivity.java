package com.nutrevida.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AIHealthActivity extends AppCompatActivity {
    private EditText etUserInput;
    private Button btnMental, btnFisica, btnNutricion;
    private TextView tvResponse;
    private AIIntegrationHelper aiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_health);

        aiHelper = new AIIntegrationHelper(this);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etUserInput = findViewById(R.id.etUserInput);
        btnMental = findViewById(R.id.btnMental);
        btnFisica = findViewById(R.id.btnFisica);
        btnNutricion = findViewById(R.id.btnNutricion);
        tvResponse = findViewById(R.id.tvResponse);
    }

    private void setupListeners() {
        btnMental.setOnClickListener(v -> getRecommendation("mental"));
        btnFisica.setOnClickListener(v -> getRecommendation("fisica"));
        btnNutricion.setOnClickListener(v -> getRecommendation("nutricion"));
    }

    private void getRecommendation(String category) {
        String userInput = etUserInput.getText().toString().trim();
        if (userInput.isEmpty()) {
            aiHelper.showToast("Por favor, ingresa una pregunta o describe tu necesidad.");
            return;
        }

        tvResponse.setText("Cargando...");
        aiHelper.getHealthRecommendation(userInput, category, new AIIntegrationHelper.AIResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> tvResponse.setText(response));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    tvResponse.setText("");
                    aiHelper.showToast(error);
                });
            }
        });
    }
}