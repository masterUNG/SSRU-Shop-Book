package appewtc.masterung.ssrushopbook;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private String nameBookString, priceBookString,
            urlPDFString, moneyString, imageString;
    private String[] loginStrings;
    private String urlEdit = "http://swiftcodingthai.com/ssru/edit_money_master.php";
    private GoogleMap mMap;
    private double centerLat = 13.774880;
    private double centerLng = 100.508189;
    private boolean statusClick = true;
    private ImageView imageView;
    private TextView nameTextView;
    private TextView moneyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);

        //Bind Widget
        imageView = (ImageView) findViewById(R.id.imageView3);
        nameTextView = (TextView) findViewById(R.id.textView12);
        moneyTextView = (TextView) findViewById(R.id.textView13);

        //Get Value From Intent
        nameBookString = getIntent().getStringExtra("NameBook");
        priceBookString = getIntent().getStringExtra("PriceBook");
        urlPDFString = getIntent().getStringExtra("urlEbook");
        loginStrings = getIntent().getStringArrayExtra("Login");
        moneyString = getIntent().getStringExtra("Money");
        imageString = getIntent().getStringExtra("ImageBook");

        //updateAccount
        updateAccount();

        //Show View
        Picasso.with(this).load(imageString).resize(120, 150).into(imageView);
        nameTextView.setText(nameBookString + " ราคา " + priceBookString + " THB.");
        moneyTextView.setText("เงินที่เหลืออยู่ = " + moneyString);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }   // Main Method

    public void clickOrder(View view) {

        if (statusClick) {
            //True ยังไม่ได้กำหนดที่ส่งของ
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "ยังไม่เลือกที่ส่งของ",
                    "กรุณากำหนดที่ส่งของ");

        } else {
            //False กำหนดที่ส่งของแล้ว
            upOrderToServer();
        }

    }

    private void upOrderToServer() {

    }


    private void updateAccount() {

        int intCurrentMoney = Integer.parseInt(moneyString);
        int intPriceBook = Integer.parseInt(priceBookString);
        int intMyMoney = intCurrentMoney - intPriceBook;

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("User", loginStrings[3])
                .add("Money", Integer.toString(intMyMoney))
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(urlEdit).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });

    }   // updateAccount


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(centerLat, centerLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (statusClick) {
                    statusClick = false;
                }


                mMap.clear();

                mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("ส่งของที่นี่")
                .snippet("(" +
                        Double.toString(latLng.latitude) +
                        ", " +
                        Double.toString(latLng.longitude) +
                        ")"));


            }   // onMapClick
        });


    }   // onMap

}   // Main Class
