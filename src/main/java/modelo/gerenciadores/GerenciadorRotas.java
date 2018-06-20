package modelo.gerenciadores;

import modelo.objetos.Aeroporto;
import modelo.objetos.Rota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            if(r.getOrigem().getCodigo().equals(codOrigem)){
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

    public ArrayList<Rota> buscaPorCia(String codCia){
        ArrayList<Rota> result = new ArrayList<>();
        for(Rota rota: rotas.values()){
            if(rota.getCia().getCodigo().equalsIgnoreCase(codCia))
                result.add(rota);
        }
        return result;
    }

    public int getTrafego(Aeroporto aeroporto){
        List<Aeroporto> aeroportos = new ArrayList<>();
        aeroportos.add(aeroporto);
        return getTrafego(aeroportos);
    }

    public int getTrafego(List<Aeroporto> aeroportos){
        int trafego = 0;
        for(Aeroporto aero: aeroportos){
            for(Rota r: rotas.values()){
                if(r.getOrigem().getCodigo().equals(aero.getCodigo())) trafego++;
                if(r.getDestino().getCodigo().equals(aero.getCodigo())) trafego++;
            }
        }
        return trafego;
    }

    public int getTrafegoTotal(){
        return rotas.size();
    }

}
