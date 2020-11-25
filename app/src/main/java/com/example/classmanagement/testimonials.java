package com.example.classmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyimageslider.EasyImageSlider;

public class testimonials extends Fragment {
    int a=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  Bundle savedInstanceState) {
View v= inflater.inflate(R.layout.activity_testimonials,container,false);
        EasyImageSlider slider = v.findViewById(R.id.slider);

                   slider.put(R.drawable.testimonial1);
            slider.put(R.drawable.testimonial2);
            slider.put(R.drawable.testimonial3);
            slider.with(getActivity());

return v;

    }
}