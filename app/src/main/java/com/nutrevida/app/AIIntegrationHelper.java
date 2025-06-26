package com.nutrevida.app;

import android.content.Context;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class AIIntegrationHelper {
    private final GenerativeModelFutures model;
    private final Context context;

    public AIIntegrationHelper(Context context) {
        this.context = context;
        model = GenerativeModelFutures.from(
                new GenerativeModel("gemini-1.5-flash", BuildConfig.GEMINI_API_KEY)
        );
    }

    public void getHealthRecommendation(
            String prompt,
            FutureCallback<GenerateContentResponse> callback
    ) {
        // Prompt optimizado para respuestas más rápidas
        String optimizedPrompt = 
            "Eres un asistente de salud especializado en Bolivia. " +
            "Responde de manera clara, práctica y específica para Bolivia. " +
            "\n\n" +
            "INSTRUCCIONES:" +
            "\n• Considera la altitud de Bolivia (La Paz: 3,650m, Cochabamba: 2,558m, Santa Cruz: 416m)" +
            "\n• Incluye alimentos locales: quinua, chuño, charque, llajwa, etc." +
            "\n• Usa un tono amigable y motivador" +
            "\n• Estructura con emojis y formato claro" +
            "\n• Máximo 3 párrafos" +
            "\n• Sé específico y práctico" +
            "\n\n" +
            "CONSULTA: " + prompt +
            "\n\n" +
            "Responde de manera útil y aplicable inmediatamente.";

        Content content = new Content.Builder()
                .addText(optimizedPrompt)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, callback, context.getMainExecutor());
    }
}