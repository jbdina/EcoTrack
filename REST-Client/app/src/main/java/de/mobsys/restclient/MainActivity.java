package de.mobsys.restclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    TextView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = findViewById(R.id.label);
    }

    public void doIt(View view) {
        new HTTPGetReqTask().execute();
    }

    private class HTTPGetReqTask extends AsyncTask<Void, Void, String> {

        String line, lline;

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://192.168.43.24:8080/greeting");
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();
                if (code !=  200) {
                    throw new Exception("Invalid response from server: " + code);
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));

                while ((line = rd.readLine()) != null) {
                    Log.i("data", line);
                    if (line != null) lline = line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return lline;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Update the UI with the result
            if (result != null) {
                l.setText(result); // Assuming l is your TextView
            } else {
                // Handle case where result is null or empty
                // Display an error message or handle the situation accordingly
            }
        }
    }

    public void doSent(View view) {
        // Get the input from TextInputEditText
        TextInputEditText inputEditText = findViewById(R.id.textInputEditText);
        String input = inputEditText.getText().toString();

        // Execute POST request with input data
        new HTTPPostReqTask().execute(input);
    }

    private class HTTPPostReqTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String inputData = params[0]; // Retrieve the input data from the params array

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://192.168.43.24/saveNumber");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                // Use the input data in your request
                String postData = "{\"number\": \"" + inputData + "\"}";

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(postData);
                wr.flush();
                wr.close();

                int code = urlConnection.getResponseCode();
                if (code != 200) {
                    throw new Exception("Invalid response from server: " + code);
                }

                // Read response from the server
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        protected void onPostExecute(String result) {
            // Display a popup indicating successful saving
            showSuccessPopup(result);
        }
    }

    private void showSuccessPopup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
            // Retrieve the saved entry from the server
            new HTTPGetReqTask().execute();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    }

