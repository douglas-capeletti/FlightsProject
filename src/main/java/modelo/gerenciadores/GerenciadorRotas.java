package modelo.gerenciadores;

import modelo.Util;
import modelo.objetos.Rota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorRotas {

    private Map<String, Rota> rotas;
    private Util util = new Util();

    public GerenciadorRotas() {
        this.rotas = new HashMap<>();
        // Carrega a o HashMap de objetos
        for (String[] linha :  util.carregaDados(Util.Arquivos.ROTAS)){
            //rotas.put(linha[0],); // TODO
        }
    }

    public void adicionar(Rota rota) {
        rotas.put(rota.getOrigem().getCodigo(), rota);
    }

    public ArrayList<Rota> listarTodas() {
        return new ArrayList<>(rotas.values());
    }

    public ArrayList<Rota> buscarOrigem(String codigo) {
        ArrayList<Rota> result = new ArrayList<>();
        for(Rota r: rotas.values()) if(r.getOrigem().getCodigo().equals(codigo)) result.add(r);
        return result;
    }
}
