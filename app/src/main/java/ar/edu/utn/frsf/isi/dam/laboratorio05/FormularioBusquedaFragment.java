package ar.edu.utn.frsf.isi.dam.laboratorio05;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.Reclamo;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormularioBusquedaFragment extends Fragment {

    private ArrayAdapter<Reclamo.TipoReclamo> tipoReclamoAdapter;
    private Spinner tipoReclamo;
    private Button botonBuscar;

    public FormularioBusquedaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_formulario_busqueda, container, false);

        tipoReclamo=  v.findViewById(R.id.reclamo_tipo_buscar);
        botonBuscar = v.findViewById(R.id.btnBuscar);

        tipoReclamoAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,Reclamo.TipoReclamo.values());
        tipoReclamoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoReclamo.setAdapter(tipoReclamoAdapter);

        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrarReclamos(tipoReclamo.getSelectedItem().toString());
            }
        });

        return v;
    }

    private void filtrarReclamos(String tipo_reclamo){
        Fragment f = new MapaFragment();
        Bundle args = new Bundle();
        args.putInt("tipo_mapa", 5);
        args.putString("tipo_reclamo", tipo_reclamo);
        f.setArguments(args);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenido, f)
                .commit();
    }
}
