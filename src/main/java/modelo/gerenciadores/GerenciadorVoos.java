package modelo.gerenciadores;

import modelo.objetos.Voo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorVoos {

    private Map<LocalDateTime, Voo> voos;

    public GerenciadorVoos() {
        this.voos = new HashMap<>();
    }

    public void adicionar(Voo v) {
        voos.put(v.getDatahora(), v);
    }

    public ArrayList<Voo> listarTodos() {
        return new ArrayList<>(voos.values());
    }

    public ArrayList<Voo> buscarPorData(LocalDate data) {
       ArrayList<Voo> result = new ArrayList<>();
       for(Voo v: voos.values()) if(v.getDatahora().toLocalDate().equals(data)) result.add(v);
       return result;
    }
}
