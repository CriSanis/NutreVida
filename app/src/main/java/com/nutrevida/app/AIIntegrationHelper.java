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
        String localizedPrompt = prompt + "\n\nProporciona información relevante para Bolivia, considerando alimentos, ejercicios y prácticas de salud mental locales. Responde en español, de manera clara, concisa y amigable, adecuada para usuarios de NutreVida.";
        Content content = new Content.Builder()
                .addText(localizedPrompt)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, callback, context.getMainExecutor());
    }
}