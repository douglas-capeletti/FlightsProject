package modelo.gerenciadores;

import modelo.objetos.Aeroporto;

import java.util.ArrayList;
import java.util.HashMap;
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

    public Aeroporto buscarPorCodigo(String codigo) {
        return aeroportos.get(codigo);
    }

}
