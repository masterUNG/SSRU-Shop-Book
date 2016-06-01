package appewtc.masterung.ssrushopbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ProductListView extends AppCompatActivity {

    //Explicit
    private TextView nameTextView, surnameTextView, moneyTextView;
    private ListView listView;
    private String[] loginStrings;
    private String urlJSON = "http://swiftcodingthai.com/ssru/get_product.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_view);

        //Bind Widget
        nameTextView = (TextView) findViewById(R.id.textView7);
        surnameTextView = (TextView) findViewById(R.id.textView8);
        moneyTextView = (TextView) findViewById(R.id.textView9);
        listView = (ListView) findViewById(R.id.listView);

        //Receive Value From Intent
        loginStrings = getIntent().getStringArrayExtra("Login");

        //Show View
        nameTextView.setText(loginStrings[1]);
        surnameTextView.setText(loginStrings[2]);
        moneyTextView.setText(loginStrings[5] + " THB.");

        SynchronizeProduct synchronizeProduct = new SynchronizeProduct(this, urlJSON);
        synchronizeProduct.execute();

    }   // Main Method

    private class SynchronizeProduct extends AsyncTask<Void, Void, String> {

        private Context context;
        private String urlString;
        private ProgressDialog progressDialog;

        public SynchronizeProduct(Context context,
                                  String urlString) {
            this.context = context;
            this.urlString = urlString;
        }   // Constructor

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(context, "Load Product",
                    "Load Product Process ...");

        }   // onPre

        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlString).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("1JuneV1", "doIn e ==> " + e.toString());
                return null;
            }

        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.length() != 0) {
                progressDialog.dismiss();
                Log.d("1JuneV1", "s ==> " + s);
            }



        }   // onPost

    }   // Syn Class




}   // Main Class
