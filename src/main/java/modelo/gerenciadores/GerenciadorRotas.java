package modelo.gerenciadores;

import modelo.objetos.Rota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorRotas {

    private Map<String, Rota> rotas;

    public GerenciadorRotas() {
        this.rotas = new HashMap<>();
    }

    public void adicionar(Rota rota) {
        rotas.put(rota.getOrigem().getCodigo(), rota);
    }

    public ArrayList<Rota> listarTodos() {
        return new ArrayList<>(rotas.values());
    }

    public ArrayList<String> listarTodosCodigos(){
        ArrayList<String> codigos = new ArrayList<>();
        for(Rota r: rotas.values()){
            codigos.add(r.getAeronave().getCodigo());
        }
        return codigos;
    }

    public ArrayList<Rota> listaDestinos(String codOrigem){
        ArrayList<Rota> destinos = new ArrayList<>();
        for(Rota r: rotas.values()) {
            if(r.getAeronave().getCodigo().equals(codOrigem)){
                destinos.add(r);
            }
        }
        return destinos;
    }

    public ArrayList<Rota> buscarOrigem(String codigo) {
        ArrayList<Rota> result = new ArrayList<>();
        for(Rota r: rotas.values()) if(r.getOrigem().getCodigo().equals(codigo)) result.add(r);
        return result;
    }
}
