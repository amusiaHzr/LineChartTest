package com.amusia.linecharttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SimpleLineChart slc;
    SimpleHistogram sh;
    PreservationLineChart plc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float[] data = {90f,78.8f,78.8f,76.8f,65.8f,53.8f,48.8f,31.8f};
        slc = findViewById(R.id.slc);
        slc.setMode(SimpleLineChart.MODE_ONE);
        slc.setData(data,new String[]{"2021","2022","2023","2024","2021","2022","2023","2024"} );

        sh = findViewById(R.id.sh);
        sh.setData(data,new String[]{"湖南","广东","上海","重庆","浙江","江西","湖北","沈阳"} );

        plc = findViewById(R.id.plc);
        plc.setData(data,new String[]{"2021","2022","2023","2024","2021","2022","2023","2024"});
         }
}
