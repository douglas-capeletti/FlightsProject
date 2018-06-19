package modelo;

import gui.Util;
import modelo.gerenciadores.*;
import modelo.objetos.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Setup {
    private Util util = new Util();
	private GerenciadorAeronaves avioes;
	private GerenciadorAeroportos aeroportos;
	private GerenciadorCias empresas;
	private GerenciadorRotas rotas;
	private GerenciadorPaises paises;

	public Setup(GerenciadorAeronaves avioes, GerenciadorAeroportos aeroportos, GerenciadorCias empresas, GerenciadorPaises paises, GerenciadorRotas rotas) {
		this.paises = paises;
		this.avioes = avioes;
		this.aeroportos = aeroportos;
		this.empresas = empresas;
		this.paises = paises;
		this.rotas = rotas;
		carregaPaises();
		carregaAeronaves();
		carregaAeroportos();
		carregaCias();
		carregaPaises();
		carregaRotas();
	}

	private List<String[]> carregaDados(Arquivos origem) throws NullPointerException{
		List<String[]> linhas = null;
		try {
			try (Stream<String> stream = Files.lines(Paths.get(origem.getCaminho()))) {
				try {
					linhas = stream.map((linha) -> linha.split(";")).collect(Collectors.toList());
					linhas.remove(0);//tira a primeira linha com o cabecalho
				} finally {
					stream.close();
				}
			}
		} catch (IOException e) {
			util.showWarning(Util.Warning.ERRO_DADOS_INICIALIZACAO);
		}
		return linhas;
	}

	private void carregaAeronaves(){
		carregaDados(Arquivos.AERONAVES).forEach(linha -> {
		    if(avioes.buscarPorCodigo(linha[0]) == null)
                avioes.adicionar(new Aeronave(linha[0], linha[1], Integer.parseInt(linha[2])));
        });
	}

	private void carregaAeroportos(){
		carregaDados(Arquivos.AEROPORTOS).forEach(linha -> {
		    if(aeroportos.buscarPorCodigo(linha[0]) == null)
                aeroportos.adicionar(new Aeroporto(linha[0], linha[3], new Geo(Double.parseDouble(linha[1]), Double.parseDouble(linha[2])), paises.buscarPorCod(linha[4])));
        });
	}

	private void carregaCias(){
		carregaDados(Arquivos.CIA_AEREA).forEach(linha -> {
		    if(empresas.buscarPorCod(linha[0]) == null)
                empresas.adicionar(new CiaAerea(linha[0], linha[1]));
        });
	}

	private void carregaPaises(){
	    carregaDados(Arquivos.PAISES).forEach(linha -> {
	        if(paises.buscarPorCod(linha[0]) == null)
                paises.adicionar(new Pais(linha[0], linha[1]));
        });
	}

	private void carregaRotas(){
		carregaDados(Arquivos.ROTAS).forEach(linha -> rotas.adicionar(new Rota(empresas.buscarPorCod(linha[0]), aeroportos.buscarPorCodigo(linha[1]), aeroportos.buscarPorCodigo(linha[2]), avioes.buscarPorCodigo(linha[5]))));
	}

	public enum Arquivos {
		CIA_AEREA("airlines.dat"),
		AEROPORTOS("airports.dat"),
		PAISES("countries.dat"),
		AERONAVES("equipment.dat"),
		ROTAS("routes.dat");

		private String caminho;

		public String getCaminho(){
			return System.getProperty("user.dir") + "/dados/" + caminho;
		}

		Arquivos(String caminho) {
			this.caminho = caminho;
		}
	}

}
