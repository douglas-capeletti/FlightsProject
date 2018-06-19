package gui;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import modelo.Setup;
import modelo.gerenciadores.*;
import modelo.objetos.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    private final SwingNode mapkit = new SwingNode();
    private GerenciadorMapa gerMapa;
    private Controller.EventosMouse mouse;
    private ContextMenu contextMenu = new ContextMenu();

    private GerenciadorAeronaves gerAvioes = new GerenciadorAeronaves();
    private GerenciadorAeroportos gerAero = new GerenciadorAeroportos();
    private GerenciadorCias gerCias = new GerenciadorCias();
    private GerenciadorPaises gerPaises = new GerenciadorPaises();
    private GerenciadorRotas gerRotas = new GerenciadorRotas();

    @FXML BorderPane PainelPrincipal;
    @FXML Button BTNFlipMap;

    // Pesquisa Cia Aerea
    @FXML ComboBox CBCiaAerea;
    @FXML ComboBox CBRotaCiaAerea;

    // Pesquisa Aeroporto;
    @FXML ComboBox CBPais;
    @FXML Label LBLTrafego;

    // Pesquisa Rota
    @FXML ComboBox CBOrigem;
    @FXML ComboBox CBDestino;
    @FXML ComboBox CBConexoes;
    @FXML ComboBox CBTempoVoo;

    // Altera o modo de exibicao do Mapa
    @FXML private void FlipMap(){
        gerMapa.flipTipoMapa();
        BTNFlipMap.setText(gerMapa.getTipoMapa().toString());
    }

    @FXML private void PesquisaCiaAerea(){
        gerMapa.clear();
        if(CBRotaCiaAerea.getValue() != null) {
            pintorDeTracos((Rota) CBRotaCiaAerea.getValue());
            gerMapa.setPosicaoVisual(((Rota) CBRotaCiaAerea.getValue()).getOrigem().getLocal());

        } else if (CBCiaAerea.getValue() != null){
            pintorDeTracos(gerRotas.buscaPorCia(((CiaAerea) CBCiaAerea.getValue()).getCodigo()));
        }
    }

    @FXML private void PesquisaAeroporto(){
        if(CBPais.getValue() != null){
            List<Aeroporto> aeroportos = gerAero.buscarPorPais((Pais) CBPais.getValue());
            pintorDePontos(aeroportos);
            LBLTrafego.setText(gerRotas.getTrafego(aeroportos) + " Voos");
            if(aeroportos.size() > 0) {
                gerMapa.setPosicaoVisual(aeroportos.get(0).getLocal());
            }
        }else {
            pintorDePontos(buscaTodosAeroportos());
        }
    }

    @FXML private void PesquisaRota(){

    }

    private void contextMenu(){
        MenuItem todosAero = new MenuItem("Buscar Todos Aeroportos");
        todosAero.setOnAction(event -> pintorDePontos(buscaTodosAeroportos()));

        Menu pesquisarAeroporto = new Menu("Buscar Aeroporto...");
        MenuItem distancia1 = new MenuItem("10KM de distância");
        distancia1.setOnAction(event -> pintorDePontos(buscarPorDistancia(10)));

        MenuItem distancia2 = new MenuItem("50KM de distância");
        distancia2.setOnAction(event -> pintorDePontos(buscarPorDistancia(50)));

        MenuItem distancia3 = new MenuItem("100KM de distância");
        distancia2.setOnAction(event -> pintorDePontos(buscarPorDistancia(100)));

        MenuItem distanciaX = new MenuItem("Mais próximo");
        distanciaX.setOnAction(event -> pintorDePontos(buscarPorDistancia(-1)));

        MenuItem fechar = new MenuItem("(Fechar Menu)");
        pesquisarAeroporto.getItems().addAll(distancia1, distancia2, distancia3, distanciaX);
        contextMenu.getItems().addAll(todosAero, pesquisarAeroporto, fechar);
    }

    private List<Aeroporto> buscaTodosAeroportos(){
        LBLTrafego.setText(gerRotas.getTrafegoTotal() + " Voos");
        return new ArrayList<>(gerAero.listarTodos());
    }

    private Aeroporto buscarPorDistancia(int distancia){
        Geo posicaoAtual = new Geo(gerMapa.getPosicao().getLatitude(), gerMapa.getPosicao().getLongitude());
        Aeroporto aeroporto = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        for(Aeroporto aero: gerAero.listarTodos()){
            if(posicaoAtual.distancia(aero.getLocal()) < menorDistancia){
                menorDistancia = posicaoAtual.distancia(aero.getLocal());
                aeroporto = aero;
            }
        }
        return (menorDistancia < distancia || distancia == -1) && aeroporto != null ? aeroporto : null;
    }

    private void pintorDePontos(Aeroporto aeroporto){
        List<Aeroporto> pontos = new ArrayList<>();
        if(aeroporto != null)
            pontos.add(aeroporto);
        pintorDePontos(pontos);
    }

    private void pintorDePontos(List<Aeroporto> aeroportos){
        List<MyWaypoint> pontos = new ArrayList<>();
        for (Aeroporto aero: aeroportos)
            pontos.add(aero.waypoint(gerRotas.getTrafego(aero)));
        gerMapa.setPontos(pontos);
        gerMapa.getMapKit().repaint();
    }

    private void pintorDeTracos(Rota rota){
        List<Aeroporto> pontos = new ArrayList<>();
        if(rota != null) {
            pontos.add(rota.getOrigem());
            pontos.add(rota.getDestino());
        }
        pintorDeTracos(pontos);
    }
    private void pintorDeTracos(ArrayList<Rota> rotas){
        List<Aeroporto> pontos = new ArrayList<>();
        for (Rota rota: rotas) {
            if(rota != null) {
                pontos.add(rota.getOrigem());
                pontos.add(rota.getDestino());
            }
            pintorDeTracos(pontos);
        }
    }

    private void pintorDeTracos(List<Aeroporto> aeroportos){
        Tracado tracado = new Tracado();
        for (Aeroporto aero: aeroportos){
            tracado.addPonto(aero.getLocal());
        }
        tracado.setWidth(5);
        tracado.setCor(new Color(60, 40, 40, 200));
        gerMapa.addTracado(tracado);
        pintorDePontos(aeroportos);
    }


    private class EventosMouse extends MouseAdapter {
        private int lastButton = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            JXMapViewer mapa = gerMapa.getMapKit().getMainMap();
            GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
            lastButton = e.getButton();
            gerMapa.setPosicao(loc);
            // Botão direito: seleciona localização
            if (lastButton == MouseEvent.BUTTON3) {
                mapkit.setOnContextMenuRequested(event -> contextMenu.show(mapkit, event.getScreenX(), event.getScreenY()));
            }
        }
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(gerMapa.getMapKit()));
    }

    private void inicializacaoGerenciadores(){
        new Setup(gerAvioes, gerAero, gerCias, gerPaises, gerRotas);
        gerMapa = new GerenciadorMapa(gerAero.buscarPorCodigo("POA").getLocal(), GerenciadorMapa.FonteImagens.VirtualEarth);
        BTNFlipMap.setText(gerMapa.getTipoMapa().toString());
    }

    private void inicializacaoPesquisaCiaAerea(){
        CBCiaAerea.setItems(gerCias.listarTodos().stream().collect(Collectors.toCollection(FXCollections::observableArrayList)).sorted());
        CBCiaAerea.valueProperty().addListener((ChangeListener<CiaAerea>) (lista, anterior, atual) ->
                CBRotaCiaAerea.setItems(gerRotas.buscaPorCia(atual.getCodigo()).stream().collect(Collectors.toCollection(FXCollections::observableArrayList)).sorted()));
    }

    private void inicializacaoPesquisaAeroporto(){
        CBPais.setItems(gerPaises.listarTodos().stream().collect(Collectors.toCollection(FXCollections::observableArrayList)).sorted());
    }

    private void inicializacaoPesquisaRotas(){
        CBOrigem.setItems(gerRotas.listarTodos().stream().map(Rota::getOrigem).collect(Collectors.toCollection(FXCollections::observableArrayList)).sorted());
        CBDestino.setItems(gerRotas.listarTodos().stream().map(Rota::getDestino).collect(Collectors.toCollection(FXCollections::observableArrayList)).sorted());
        CBConexoes.setItems(FXCollections.observableArrayList(0, 1, 2, 5));
        CBTempoVoo.setItems(FXCollections.observableArrayList(4.00, 8.00, 12.00, 24.00));
    }



    @FXML void initialize(){
        contextMenu();
        inicializacaoGerenciadores();
        inicializacaoPesquisaCiaAerea();
        inicializacaoPesquisaAeroporto();
        inicializacaoPesquisaRotas();
        mouse = new Controller.EventosMouse();
        gerMapa.getMapKit().getMainMap().addMouseListener(mouse);
        gerMapa.getMapKit().getMainMap().addMouseMotionListener(mouse);
        createSwingContent(mapkit);
        PainelPrincipal.setCenter(mapkit);
    }

}
