package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alex on 10/19/2018.
 */

public class CarAdapter extends ArrayAdapter<Car> {
    Context context;
    int resource;
    List<Car> carList;


    public CarAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Car> carList) {
        super(context, resource, carList);
        this.context = context;
        this.resource = resource;
        this.carList = carList;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(resource, null);


        TextView carResult = (TextView) v.findViewById(R.id.textViewCar);
        ImageView imCarResult = (ImageView) v.findViewById(R.id.imageViewCar);

        Car car = carList.get(position);
        final String carName = car.getCarNames();
        carResult.setText(carName);
        imCarResult.setImageDrawable(context.getResources().getDrawable(car.getCarImages()));


        imCarResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(context, EditCarActivity.class);
                login.putExtra("Item", carName);

                context.startActivity(login);
            }
        });


        return v;
    }


}

