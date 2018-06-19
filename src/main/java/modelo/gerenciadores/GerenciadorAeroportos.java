package modelo.gerenciadores;

import modelo.objetos.Aeroporto;
import modelo.objetos.Pais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorAeroportos {

    private Map<String, Aeroporto> aeroportos;

    public GerenciadorAeroportos() {
        this.aeroportos = new HashMap<>();
    }

    public void adicionar(Aeroporto aero) {
        aeroportos.put(aero.getCodigo(), aero);
    }

    public ArrayList<Aeroporto> listarTodos() {
        return new ArrayList<>(aeroportos.values());
    }

    public ArrayList<Aeroporto> listarTodosOutros(Aeroporto aeroporto){
        Map<String, Aeroporto> aeros = new HashMap<>(aeroportos);
        aeros.remove(aeroporto.getCodigo());
        return new ArrayList<>(aeros.values());
    }

    public Aeroporto buscarPorCodigo(String codigo) {
        return aeroportos.get(codigo);
    }

    public List<Aeroporto> buscarPorPais(Pais pais){
        List<Aeroporto> aeros = new ArrayList<>();
        for(Aeroporto aero: aeroportos.values()){
            if(aero.getPais().getCodigo().equals(pais.getCodigo()))
                aeros.add(aero);
        }
        return aeros;
    }

}
