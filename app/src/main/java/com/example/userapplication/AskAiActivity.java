package com.example.userapplication;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapplication.common.Message;
import com.example.userapplication.common.MessageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AskAiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvWelcome;
    ImageButton sendButton;
    EditText etMsg;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ask_ai);

        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.AskAIActivityRecyclerView);
        tvWelcome = findViewById(R.id.tvWelcomeText);
        sendButton = findViewById(R.id.sendButton);
        etMsg = findViewById(R.id.messageEditText);

        // setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question = etMsg.getText().toString().trim();
                addToChat(question,Message.SENT_BY_ME);
                etMsg.setText("");
                callAPI(question);
                tvWelcome.setVisibility(View.GONE);

            }
        });

    }

    void addToChat(String message,String sentBy){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

            }
        });

    }

    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response,Message.SENT_BY_BOT);
    }




    void callAPI(String question) {
        // okhttp setup

        messageList.add(new Message("Typing...", Message.SENT_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-4o-mini");

//            jsonBody.put("prompt", question);
//            jsonBody.put("max_tokens", 4000);
//            jsonBody.put("temperature", 0);

            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role","user");
            obj.put("content",question);
            messageArr.put(obj);

            jsonBody.put("messages",messageArr);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("\n" +
                        "https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());

                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");

                        addResponse(result.trim());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {

                    addResponse("Failed to load response due to " + Objects.requireNonNull(response.body()).string());
                }

            }
        });
    }

}