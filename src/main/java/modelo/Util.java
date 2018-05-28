package modelo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    public enum Arquivos {
        CIA_AEREA("airlines.dat"),
        AEROPORTOS("airport.dat"),
        PAISES("countries.dat"),
        AERONAVES("equipments.dat"),
        ROTAS("routes.dat");

        private String caminho;

        public String getCaminho(){
            return System.getProperty("user.dir") + "/src/main/resources/dados/" + caminho;
        }

        Arquivos(String caminho) {
            this.caminho = caminho;
        }
    }

    // Ajustar lancamento e excecao - TODO
    public List<String[]> carregaDados(Arquivos origem) {
        try (Stream<String> stream = Files.lines(Paths.get(origem.getCaminho()))){
            List<String[]> linhas = stream.map((linha)-> linha.split(";")).collect(Collectors.toList());
            linhas.remove(0);//tira a primeira linha com o cabecalho
            return linhas;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
