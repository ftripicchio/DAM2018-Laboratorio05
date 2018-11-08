package ar.edu.utn.frsf.isi.dam.laboratorio05;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.MyDatabase;
import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.Reclamo;
import ar.edu.utn.frsf.isi.dam.laboratorio05.modelo.ReclamoDao;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaReclamosFragment extends Fragment {

    private static final int EVENTO_UPDATE_LISTA = 100;
    private ReclamoArrayAdapter adapter;
    private List<Reclamo> listaReclamos;
    private ListView lvReclamos;
    private ReclamoDao reclamoDao;
    public ListaReclamosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_reclamos, container, false);
        lvReclamos = (ListView) v.findViewById(R.id.listaReclamos);
        listaReclamos = new ArrayList<>();
        adapter = new ReclamoArrayAdapter(getActivity(),listaReclamos);
        adapter.setOnReclamoListener(eventosAdapterManager);
        lvReclamos.setAdapter(adapter);

        reclamoDao = MyDatabase.getInstance(this.getActivity()).getReclamoDao();
        cargarReclamosAsyn();
        return v;
    }

    ReclamoArrayAdapter.OnReclamoListener eventosAdapterManager = new ReclamoArrayAdapter.OnReclamoListener() {
        @Override
        public void editarReclamo(int id) {
            NuevoReclamoFragment f = new NuevoReclamoFragment ();
            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("idReclamo",id);
            f.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenido, f)
                    .commit();
        }

        @Override
        public void borrarReclamo(final int id) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Reclamo r= reclamoDao.getById(id);
                    reclamoDao.delete(r);
                    listaReclamos.clear();
                    listaReclamos.addAll(reclamoDao.getAll());
                    Message completeMessage = handler.obtainMessage(EVENTO_UPDATE_LISTA);
                    completeMessage.sendToTarget();
                }
            };
            Thread t1 = new Thread(r);
            t1.start();
        }

        @Override
        public void mostrarMapa(int id) {
            Fragment f = new MapaFragment();
            Bundle args = new Bundle();
            args.putInt("tipo_mapa", 3);
            args.putInt("idReclamo", id);
            f.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenido, f)
                    .commit();
        }
    };

    private void cargarReclamosAsyn(){
        Runnable hiloCargarReclamos = new Runnable() {
            @Override
            public void run() {
                listaReclamos.clear();
                listaReclamos.addAll(reclamoDao.getAll());
                Message completeMessage = handler.obtainMessage(EVENTO_UPDATE_LISTA);
                completeMessage.sendToTarget();
            }
        };
        Thread t1 = new Thread(hiloCargarReclamos);
        t1.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_UPDATE_LISTA:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
