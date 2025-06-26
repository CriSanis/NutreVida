package com.nutrevida.app;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
    private TextView tvResponse, tvLoadingText;
    private ImageButton btnBack;
    private CardView cardResponse;
    private LinearLayout loadingLayout;
    private ScrollView responseScrollView;
    private ProgressBar progressBar;
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
        tvLoadingText = findViewById(R.id.tvLoadingText);
        btnBack = findViewById(R.id.btnBack);
        cardResponse = findViewById(R.id.cardResponse);
        loadingLayout = findViewById(R.id.loadingLayout);
        responseScrollView = findViewById(R.id.responseScrollView);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void handleSubmit() {
        String prompt = etPrompt.getText().toString().trim();
        if (prompt.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu consulta", Toast.LENGTH_SHORT).show();
            etPrompt.requestFocus();
            return;
        }

        // Deshabilitar botón y mostrar animación de carga
        btnSubmit.setEnabled(false);
        btnSubmit.setText("🤔 Procesando...");
        
        // Mostrar card de respuesta con animación
        cardResponse.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.VISIBLE);
        responseScrollView.setVisibility(View.GONE);
        
        // Aplicar animaciones
        Animation cardAnimation = AnimationUtils.loadAnimation(this, R.anim.text_loading_animation);
        cardResponse.startAnimation(cardAnimation);
        
        Animation loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.ai_loading_animation);
        progressBar.startAnimation(loadingAnimation);

        // Textos de carga animados más rápidos
        startLoadingTextAnimation();

        // Llamada a la IA con timeout
        aiHelper.getHealthRecommendation(prompt, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                runOnUiThread(() -> {
                    // Ocultar animación de carga
                    loadingLayout.setVisibility(View.GONE);
                    responseScrollView.setVisibility(View.VISIBLE);
                    
                    // Procesar respuesta
                    String responseText = result.getText() != null ? result.getText() : "Lo siento, no pude generar una respuesta en este momento.";
                    
                    // Mostrar respuesta directamente (sin procesar Markdown para mayor velocidad)
                    tvResponse.setText(responseText);
                    
                    // Aplicar animación de entrada a la respuesta
                    Animation responseAnimation = AnimationUtils.loadAnimation(AIHealthActivity.this, R.anim.text_loading_animation);
                    responseScrollView.startAnimation(responseAnimation);
                    
                    // Restaurar botón
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("🌟 Consultar IA");
                    
                    // Scroll automático a la respuesta
                    cardResponse.post(() -> {
                        cardResponse.requestFocus();
                    });
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    // Ocultar animación de carga
                    loadingLayout.setVisibility(View.GONE);
                    responseScrollView.setVisibility(View.VISIBLE);
                    
                    // Mostrar mensaje de error amigable
                    String errorMessage = "😔 Lo siento, tuve un problema al procesar tu consulta.\n\n" +
                                        "Por favor, verifica tu conexión a internet e intenta nuevamente. " +
                                        "Si el problema persiste, puedes reformular tu pregunta.";
                    
                    tvResponse.setText(errorMessage);
                    
                    // Aplicar animación de entrada
                    Animation responseAnimation = AnimationUtils.loadAnimation(AIHealthActivity.this, R.anim.text_loading_animation);
                    responseScrollView.startAnimation(responseAnimation);
                    
                    // Restaurar botón
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("🌟 Consultar IA");
                    
                    Toast.makeText(AIHealthActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void startLoadingTextAnimation() {
        final String[] loadingTexts = {
            "🤔 Pensando...",
            "🔍 Analizando...", 
            "💡 Generando..."
        };
        
        final int[] currentIndex = {0};
        
        android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (loadingLayout.getVisibility() == View.VISIBLE) {
                    tvLoadingText.setText(loadingTexts[currentIndex[0]]);
                    currentIndex[0] = (currentIndex[0] + 1) % loadingTexts.length;
                    handler.postDelayed(this, 1000); // Más rápido: 1 segundo
                }
            }
        };
        handler.post(runnable);
    }
}