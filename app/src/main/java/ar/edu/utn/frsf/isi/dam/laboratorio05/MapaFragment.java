package ar.edu.utn.frsf.isi.dam.laboratorio05;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.MyDatabase;
import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.Reclamo;
import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.ReclamoDao;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {
    private GoogleMap miMapa;
    private int tipoMapa;
    private LatLng coordenadas;
    private OnMapaListener listener;

    private List<Reclamo> listaReclamos;
    private ReclamoDao reclamoDao;

    public MapaFragment() {
    }

    public void setListener(OnMapaListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container,
                savedInstanceState);
        tipoMapa = 0;
        Bundle argumentos = getArguments();
        if (argumentos != null) {
            tipoMapa = argumentos.getInt("tipo_mapa", 0);
        }

        getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        miMapa = map;
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        9999);
                return;
        }
        miMapa.setMyLocationEnabled(true);

        if(tipoMapa == 1){
            miMapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    listener.coordenadasSeleccionadas(latLng);
                }
            });
        } else if (tipoMapa ==2 ){
            cargarReclamosAsyn();
        }
    }

    private void cargarReclamosAsyn(){
        reclamoDao = MyDatabase.getInstance(this.getActivity()).getReclamoDao();
        listaReclamos = new ArrayList<>();
        Runnable hiloCargarReclamos = new Runnable() {
            @Override
            public void run() {
                listaReclamos.addAll(reclamoDao.getAll());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Reclamo r : listaReclamos){
                            Marker marker = miMapa.addMarker(new MarkerOptions()
                                    .position(new LatLng(r.getLatitud(), r.getLongitud()))
                                    .title(r.getId() + "-" + r.getTipo().toString())
                                    .snippet(r.getReclamo()));
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,10);
                        miMapa.moveCamera(cu);
                    }
                });
            }
        };
        Thread t1 = new Thread(hiloCargarReclamos);
        t1.start();
    }

    public interface OnMapaListener {
        public void coordenadasSeleccionadas(LatLng c);
    }
}

