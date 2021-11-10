package com.example.tankeo11;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class principalActivity extends AppCompatActivity {

    FragmentTransaction transaction;
    Fragment fragment_perfil, fragment_recarga, fragment_tankeo,fragment_compartir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        fragment_perfil=new PerfilFragment();
        fragment_recarga=new RecargaFragment();
        fragment_tankeo=new TankeoFragment();
        fragment_compartir=new CompartirFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView,fragment_tankeo).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.nav_tankeo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TankeoFragment()).commit();
                        return true;
                    case R.id.nav_recarga:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new RecargaFragment()).commit();
                        return true;
                    case R.id.nav_compartir:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CompartirFragment()).commit();
                        return true;
                }
                return false;
            }
        });



    }

    public void onClick(View view){
        transaction=getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.btnPerfil:
                transaction.replace(R.id.fragmentContainerView,fragment_perfil).commit();
                break;

            case R.id.btnHelp:
                transaction.replace(R.id.fragmentContainerView,fragment_compartir).commit();
                break;
        }
    }

}


