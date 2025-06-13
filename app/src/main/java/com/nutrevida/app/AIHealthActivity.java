package com.nutrevida.app;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class AIHealthActivity extends AppCompatActivity {

    private AIIntegrationHelper aiHelper;
    private EditText etPrompt;
    private Button btnSubmit;
    private TextView tvResponse;
    private ImageButton btnBack;
    private CardView cardResponse;
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_health);

        aiHelper = new AIIntegrationHelper(this);
        initializeViews();
    }

    private void initializeViews() {
        etPrompt = findViewById(R.id.etPrompt);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvResponse = findViewById(R.id.tvResponse);
        btnBack = findViewById(R.id.btnBack);
        cardResponse = findViewById(R.id.cardResponse);

        btnBack.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void handleSubmit() {
        String prompt = etPrompt.getText().toString().trim();
        if (prompt.isEmpty()) {
            Toast.makeText(this, "Ingresa una consulta", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar CardView con mensaje de carga
        tvResponse.setText("⌛ Cargando respuesta...");
        cardResponse.setVisibility(View.VISIBLE);

        aiHelper.getHealthRecommendation(prompt, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String responseText = result.getText() != null ? result.getText() : "Sin respuesta";
                // Convertir Markdown a HTML
                Node document = parser.parse(responseText);
                String html = renderer.render(document);
                // Mostrar HTML en TextView
                tvResponse.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
                cardResponse.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable t) {
                tvResponse.setText("Error: " + t.getMessage());
                cardResponse.setVisibility(View.VISIBLE);
                Toast.makeText(AIHealthActivity.this, "Error al conectar con la IA", Toast.LENGTH_SHORT).show();
            }
        });
    }
}