package modelo;

import modelo.gerenciadores.*;
import modelo.objetos.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Setup {
	private GerenciadorAeronaves avioes;
	private GerenciadorAeroportos aeroportos;
	private GerenciadorCias empresas;
	private GerenciadorRotas rotas;
	private GerenciadorPaises paises;

	public Setup(GerenciadorAeronaves avioes, GerenciadorAeroportos aeroportos, GerenciadorCias empresas, GerenciadorPaises paises, GerenciadorRotas rotas) {
		this.avioes = avioes;
		this.aeroportos = aeroportos;
		this.empresas = empresas;
		this.paises = paises;
		this.rotas = rotas;
		carregaAeronaves();
		carregaAeroportos();
		carregaCias();
		carregaPaises();
		carregaRotas();
	}

	private List<String[]> carregaDados(Arquivos origem) throws NullPointerException{
		List<String[]> linhas = null;
		try (Stream<String> stream = Files.lines(Paths.get(origem.getCaminho()))){
			 linhas = stream.map((linha)-> linha.split(";")).collect(Collectors.toList());
			linhas.remove(0);//tira a primeira linha com o cabecalho
		} catch (Exception e){
			e.printStackTrace();
		}
		return linhas;
	}

	private void carregaAeronaves(){
		for (String[] linha :  carregaDados(Arquivos.AERONAVES))
			avioes.adicionar(new Aeronave(linha[0], linha[1], Integer.parseInt(linha[2])));
	}

	private void carregaAeroportos(){
		for (String[] linha :  carregaDados(Arquivos.AEROPORTOS))
			aeroportos.adicionar(new Aeroporto(linha[0], linha[3], new Geo(Double.parseDouble(linha[1]), Double.parseDouble(linha[2]))));
	}

	private void carregaCias(){
		for (String[] linha :  carregaDados(Arquivos.CIA_AEREA))
			empresas.adicionar(new CiaAerea(linha[0], linha[1]));
	}

	private void carregaPaises(){
		for (String[] linha :  carregaDados(Arquivos.PAISES))
			paises.adicionar(new Pais(linha[0], linha[1]));
	}

	private void carregaRotas(){
		for (String[] linha :  carregaDados(Arquivos.ROTAS))
			rotas.adicionar(new Rota(empresas.buscarPorCod(linha[0]), aeroportos.buscarPorCodigo(linha[1]), aeroportos.buscarPorCodigo(linha[2]), avioes.buscarPorCodigo(linha[5])));
	}

	public enum Arquivos {
		CIA_AEREA("airlines.dat"),
		AEROPORTOS("airports.dat"),
		PAISES("countries.dat"),
		AERONAVES("equipment.dat"),
		ROTAS("routes.dat");

		private String caminho;

		public String getCaminho(){
			return System.getProperty("user.dir") + "/src/main/resources/dados/" + caminho;
		}

		Arquivos(String caminho) {
			this.caminho = caminho;
		}
	}

}
