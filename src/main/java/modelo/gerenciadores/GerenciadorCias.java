package modelo.gerenciadores;

import modelo.objetos.CiaAerea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorCias {
    private Map<String, CiaAerea> empresas;

    public GerenciadorCias() {
        this.empresas = new HashMap<>();
    }

    public ArrayList<CiaAerea> listarTodos() {
        return new ArrayList<>(empresas.values());
    }

    public void adicionar(CiaAerea cia1) {
        empresas.put(cia1.getCodigo(),cia1);
    }

    public CiaAerea buscarPorCod(String cod) {
        return empresas.get(cod);
    }

    public CiaAerea buscarPorNome(String nome) {
        for(CiaAerea cia: empresas.values()) if(cia.getNome().equals(nome)) return cia;
        return null;
    }
}
