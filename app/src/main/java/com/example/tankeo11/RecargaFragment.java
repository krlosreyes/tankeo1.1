package com.example.tankeo11;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tankeo11.Entidades.Recargas;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.epayco.android.models.Authentication;

import android.util.Log;

import com.example.epaycocheckout.check.CheckoutDialog;
import com.example.epaycocheckout.classes.EpaycoRest;
import com.example.epaycocheckout.entities.Checkout;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONException;
import org.json.JSONObject;

import co.epayco.android.util.EpaycoCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecargaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecargaFragment extends Fragment {


    RecyclerView recyclerViewRecargas;
    ArrayList<Recargas> recargas;
   


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView nomUser, valorTankeo, saldoRecarga, correoText2,cedulaNum,celularNum;
    private EditText txtOtroVal;
    private MaterialButton btnRecarga, btn20, btn50, btn100;





    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecargaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecargaFragment newInstance(String param1, String param2) {
        RecargaFragment fragment = new RecargaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            txtOtroVal = (EditText)  txtOtroVal.findViewById(R.id.txtOtroVal);
            mAuth = FirebaseAuth.getInstance();
            mDatabase= FirebaseDatabase.getInstance().getReference();
            nomUser = (TextView) nomUser.findViewById(R.id.nomUser);
            btnRecarga = (MaterialButton) btnRecarga.findViewById(R.id.btnRecarga);
            btn20 = (MaterialButton) btn20.findViewById(R.id.btn20);
            btn50 = (MaterialButton) btn50.findViewById(R.id.btn50);
            btn100 = (MaterialButton) btn100.findViewById(R.id.btn100);
            saldoRecarga = (TextView) saldoRecarga.findViewById(R.id.saldoRecarga);
            correoText2 = (TextView) correoText2.findViewById(R.id.correoText2);
            cedulaNum= (TextView) cedulaNum.findViewById(R.id.cedulaNum);
            celularNum= (TextView) celularNum.findViewById(R.id.celularNum);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recarga, container, false);

        txtOtroVal=view.findViewById(R.id.txtOtroVal);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nomUser = view.findViewById(R.id.nomUser);
        valorTankeo = view.findViewById(R.id.valorTankeo);
        btnRecarga = view.findViewById(R.id.btnRecarga);
        btn20 = view.findViewById(R.id.btn20);
        btn50 = view.findViewById(R.id.btn50);
        btn100 = view.findViewById(R.id.btn100);
        saldoRecarga = view.findViewById(R.id.saldoRecarga);
        correoText2 =view.findViewById(R.id.correoText2);
        cedulaNum = view.findViewById(R.id.cedulaNum);
        celularNum = view.findViewById(R.id.celularNum);
        getUserInfo();
        getRecargaInfo();


        btnRecarga.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Authentication auth = new Authentication();
                auth.setPrivateKey(getString(R.string.epayco_private_key));
                auth.setApiKey(getString(R.string.epayco_public_key));
                auth.setLang("ES");
                auth.setTest(true);
                getUserInfo();
                mDatabase = FirebaseDatabase.getInstance().getReference();

                // En lugar de usar la clase original de Epayco, se usa una descendiente para poder manejar pagos con tarjetas de credito sin asociar clientes y algunas otras API's que la original no posee.
                final EpaycoRest epayco = new EpaycoRest(auth);
                final Checkout checkout = new Checkout();


                // REQUERIDOS


                checkout.setInvoice("OR - 1234");
                checkout.setTitle("Tankeo Otro Valor");
                checkout.setDescription("Tankeo con valor determinado por el cliente");
                checkout.setValue(txtOtroVal.getText().toString());
                checkout.setTax("0");
                checkout.setTaxBase("0");
                checkout.setCurrency(getString(R.string.currency));
                checkout.setCountry(getString(R.string.country));

                // OPCIONALES

                // El formulario por defecto utiliza appcompat y el formulario 2 utiliza material design para las vistas
                checkout.setForm(2);
                // Los siguientes datos permiten rellenar automaticamente los datos en los formularios
                checkout.setDocType("");
                checkout.setDocNumber("");
                checkout.setName("");
                checkout.setLastName("");
                checkout.setEmail("");
                checkout.setPhoneCode("+57");
                checkout.setPhoneNumber("");
                checkout.setUrlConfirmation("https://tankeo.com.co/");
                checkout.setUrlResponse("https://tankeo.com.co/");
                // Se debe colocar una fecha para la expiración del pago por efectivo (Por default se dá un día)
//               checkout.setCashDateEnd("YYYY-MM-DD");
                // añade la opción de pago en un click (Para permitir el pago en un solo click deben estar vacio o con los datos correspondientes)
                checkout.setCustomerId("");
                checkout.setTokenCard("");


                // checkoutDialog recibe como parametro (Context, Epayco, Checkout, EpaycoCallback)
                CheckoutDialog checkoutDialog = new CheckoutDialog(getContext(), epayco, checkout, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        String fecha = jsonObject.getString("fecha");
                        String valor = jsonObject.getString("valor");
                        //El tipo de transacción viene dado por el parametro "transaction_type" dentro del json
                        String estado = jsonObject.getString("estado");
                        Log.e("Prueba", jsonObject.toString());
                        if(estado.equals("Rechazada")||estado.equals("Fallida")||estado.equals("Pendiente")){
                            Toast.makeText(getContext(), "Transaccion no exitosa", Toast.LENGTH_SHORT).show();
                            txtOtroVal.setText("");
                        }else {
                                Map<String, Object> recargaMap = new HashMap<>();
                                recargaMap.put("fecha", fecha);
                                recargaMap.put("valor", valor);
                                recargaMap.put("estado", estado);
                                mDatabase.child("Users").child(mAuth.getUid()).child("Recargas").push().setValue(recargaMap);
                                txtOtroVal.setText("");

                        }

                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Ocurrio un error con los servidores de epayco", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

                // start checkout


                View view = checkoutDialog.selectMethod();

            }

        });

        btn20.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Authentication auth = new Authentication();
                auth.setPrivateKey(getString(R.string.epayco_private_key));
                auth.setApiKey(getString(R.string.epayco_public_key));
                auth.setLang("ES");
                auth.setTest(true);

                // En lugar de usar la clase original de Epayco, se usa una descendiente para poder manejar pagos con tarjetas de credito sin asociar clientes y algunas otras API's que la original no posee.
                final EpaycoRest epayco = new EpaycoRest(auth);
                final Checkout checkout = new Checkout();


                // REQUERIDOS

                checkout.setInvoice("OR - 1234");
                checkout.setTitle("Tankeo $20.000");
                checkout.setDescription("Tankeo por $20.000");
                checkout.setValue("20000");
                checkout.setTax("0");
                checkout.setTaxBase("0");
                checkout.setCurrency(getString(R.string.currency));
                checkout.setCountry(getString(R.string.country));

                // OPCIONALES

                // El formulario por defecto utiliza appcompat y el formulario 2 utiliza material design para las vistas
                checkout.setForm(2);
                // Los siguientes datos permiten rellenar automaticamente los datos en los formularios
                checkout.setDocType("");
                checkout.setDocNumber("");
                checkout.setName("");
                checkout.setLastName("");
                checkout.setEmail("");
                checkout.setPhoneCode("+57");
                checkout.setPhoneNumber("");
//                checkout.setUrlConfirmation("");
//                checkout.setUrlResponse("");
                // Se debe colocar una fecha para la expiración del pago por efectivo (Por default se dá un día)
//               checkout.setCashDateEnd("YYYY-MM-DD");
                // añade la opción de pago en un click (Para permitir el pago en un solo click deben estar vacio o con los datos correspondientes)
                checkout.setCustomerId("");
                checkout.setTokenCard("");


                // checkoutDialog recibe como parametro (Context, Epayco, Checkout, EpaycoCallback)
                CheckoutDialog checkoutDialog = new CheckoutDialog(getContext(), epayco, checkout, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        //El tipo de transacción viene dado por el parametro "transaction_type" dentro del json


                        Toast.makeText(getContext(), "La transacción se realizo con éxito", Toast.LENGTH_SHORT).show();
                        Log.e("Prueba", jsonObject.toString());
                        //presenter.onSave(fillEditText(checkout.getValue()));

                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Ocurrio un error con los servidores de epayco", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

                // start checkout


                View view = checkoutDialog.selectMethod();
            }

        });

        btn50.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Authentication auth = new Authentication();
                auth.setPrivateKey(getString(R.string.epayco_private_key));
                auth.setApiKey(getString(R.string.epayco_public_key));
                auth.setLang("ES");
                auth.setTest(true);

                // En lugar de usar la clase original de Epayco, se usa una descendiente para poder manejar pagos con tarjetas de credito sin asociar clientes y algunas otras API's que la original no posee.
                final EpaycoRest epayco = new EpaycoRest(auth);
                final Checkout checkout = new Checkout();


                // REQUERIDOS

                checkout.setInvoice("OR - 1234");
                checkout.setTitle("Tankeo");
                checkout.setDescription("Tankeo $50.000");
                checkout.setValue("50000");
                checkout.setTax("0");
                checkout.setTaxBase("0");
                checkout.setCurrency(getString(R.string.currency));
                checkout.setCountry(getString(R.string.country));

                // OPCIONALES

                // El formulario por defecto utiliza appcompat y el formulario 2 utiliza material design para las vistas
                checkout.setForm(2);
                // Los siguientes datos permiten rellenar automaticamente los datos en los formularios
                checkout.setDocType("");
                checkout.setDocNumber("");
                checkout.setName("");
                checkout.setLastName("");
                checkout.setEmail("");
                checkout.setPhoneCode("+57");
                checkout.setPhoneNumber("");
//                checkout.setUrlConfirmation("");
//                checkout.setUrlResponse("");
                // Se debe colocar una fecha para la expiración del pago por efectivo (Por default se dá un día)
//               checkout.setCashDateEnd("YYYY-MM-DD");
                // añade la opción de pago en un click (Para permitir el pago en un solo click deben estar vacio o con los datos correspondientes)
                checkout.setCustomerId("");
                checkout.setTokenCard("");


                // checkoutDialog recibe como parametro (Context, Epayco, Checkout, EpaycoCallback)
                CheckoutDialog checkoutDialog = new CheckoutDialog(getContext(), epayco, checkout, new EpaycoCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        //El tipo de transacción viene dado por el parametro "transaction_type" dentro del json

                        Toast.makeText(getContext(), "La transacción se realizo con éxito", Toast.LENGTH_SHORT).show();
                        Log.e("Prueba", jsonObject.toString());

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Ocurrio un error con los servidores de epayco", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

                // start checkout


                View view = checkoutDialog.selectMethod();
            }

        });


//***********************************************************************************************************************************
        ImageCarousel carousel = view.findViewById(R.id.carousel);

        carousel.registerLifecycle(getLifecycle());

        List<CarouselItem> list = new ArrayList<>();

        list.add(
                new CarouselItem(
                        R.drawable.efecty2
                )

        );
        list.add(
                new CarouselItem(
                        R.drawable.pse
                )

        );
        list.add(
                new CarouselItem(
                        R.drawable.epayco1
                )

        );
        list.add(
                new CarouselItem(
                        R.drawable.visamaster
                )

        );
        list.add(
                new CarouselItem(
                        R.drawable.logobaloto
                )

        );

// ...

        carousel.setData(list);
//*****************************************************************************************************************************************************


        return view;
    }

    private void getRecargaInfo() {
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int suma=0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Recargas Valor = snapshot1.getValue(Recargas.class);
                        int valor = Integer.parseInt(Valor.getValor());
                        if(valor!=0){
                            suma=suma+valor;
                        }
                        Log.e("", ""+suma);

                    }
                    saldoRecarga.setText(suma);

                    //saldoRecarga.setText(suma);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }


    public void getUserInfo(){
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                        String nombre = dataSnapshot.child("nombre").getValue().toString();
                        String apellido = dataSnapshot.child("apellido").getValue().toString();
                        nomUser.setText(nombre + " " + apellido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });



    }
}
