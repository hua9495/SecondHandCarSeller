package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alex on 10/19/2018.
 */

public class CarAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] carNames;
    private final Integer[] carImages;

    public CarAdapter(@NonNull Context context, Integer[] IMAGES, String[] NAMES) {
        super(context, R.layout.adapter_car_layout);

        this.carImages=IMAGES;
        this.carNames=NAMES;
        this.context=context;
    }

    @Override
    public int getCount() {
        return carNames.length;
    }

    public String[] getCarNames() {
        return carNames;
    }

    public Integer[] getCarImages() {
        return carImages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.adapter_car_layout,null,true);
        TextView carResult=(TextView)v.findViewById(R.id.textViewCar);
        ImageView imCarResult=(ImageView)v.findViewById(R.id.imageViewCar);

        imCarResult.setImageResource(carImages[position]);
        carResult.setText(carNames[position]);

        return v;
    }

}

