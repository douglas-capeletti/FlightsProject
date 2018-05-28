package modelo.gerenciadores;

import modelo.objetos.Pais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorPaises {
    private Map<String, Pais> paises;

    public GerenciadorPaises() {
        this.paises = new HashMap<>();
    }

    public ArrayList<Pais> listarTodos() {
        return new ArrayList<>(paises.values());
    }

    public void adicionar(Pais p) {
        paises.put(p.getCodigo(), p);
    }

    public Pais buscarPorCod(String cod) {
        return paises.get(cod);
    }

}
