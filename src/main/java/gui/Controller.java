package gui;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import modelo.Setup;
import modelo.gerenciadores.*;
import modelo.objetos.Aeroporto;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
        Consulta();
    }

    @FXML private void PesquisaAeroporto(){
        System.out.println("Limpar");
        gerMapa.clear();
        gerMapa.getMapKit().repaint();
    }

    @FXML private void PesquisaRota(){

    }

    private void contextMenu(){
        MenuItem todosAero = new MenuItem("Buscar Todos Aeroportos");
        todosAero.setOnAction(event -> buscaTodosAeroportos());

        Menu pesquisarAeroporto = new Menu("Buscar Aeroporto...");
        MenuItem distancia1 = new MenuItem("5KM de distância");
        distancia1.setOnAction(event -> buscarPorDistancia(5));

        MenuItem distancia2 = new MenuItem("12KM de distância");
        distancia2.setOnAction(event -> buscarPorDistancia(12));

        MenuItem distanciaX = new MenuItem("Qualquer distância");
        distanciaX.setOnAction(event -> buscarPorDistancia(-1));

        pesquisarAeroporto.getItems().addAll(distancia1, distancia2, distanciaX);
        contextMenu.getItems().addAll(todosAero, pesquisarAeroporto);
    }

    private void Consulta() {

        // Lista para armazenar o resultado da consulta
        List<MyWaypoint> lstPoints = new ArrayList<>();

        Aeroporto poa = gerAero.buscarPorCodigo("POA");
        Aeroporto gru = gerAero.buscarPorCodigo("GRU");
        Aeroporto lis = gerAero.buscarPorCodigo("LIS");
        Aeroporto mia = gerAero.buscarPorCodigo("MIA");

        gerMapa.clear();
        Tracado tr = new Tracado();
        tr.setLabel("Teste");
        tr.setWidth(5);
        tr.setCor(new Color(0,0,0,60));
        tr.addPonto(poa.getLocal());
        tr.addPonto(mia.getLocal());
        tr.addPonto(gru.getLocal());
        tr.addPonto(lis.getLocal());

        gerMapa.addTracado(tr);

        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints

        lstPoints.add(new MyWaypoint(Color.RED, poa.getCodigo(), poa.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, gru.getCodigo(), gru.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, lis.getCodigo(), lis.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, mia.getCodigo(), mia.getLocal(), 5));

        // Para obter um ponto clicado no mapa, usar como segue:
        // GeoPosition pos = gerMapa.getPosicao();

        // Informa o resultado para o gerMapa
        gerMapa.setPontos(lstPoints);

        // Quando for o caso de limpar os traçados...
        // gerMapa.clear();

        gerMapa.getMapKit().repaint();
    }

    private MyWaypoint listarPontosDinamicos(Aeroporto aero){
        return new MyWaypoint(Color.RED, aero.getCodigo(), aero.getLocal(), 7);
    }

    private void buscaTodosAeroportos(){
        List<MyWaypoint> pontos = new ArrayList<>();
        for(Aeroporto aero: gerAero.listarTodos()){
            int trafego = gerRotas.buscaTrafego(aero.getCodigo());
            pontos.add(new MyWaypoint(Color.RED, aero.getCodigo(), aero.getLocal(), trafego));
        }
        gerMapa.setPontos(pontos);
        gerMapa.getMapKit().repaint();
    }

    private void buscarPorDistancia(int distancia){
        for(Aeroporto aero: gerAero.listarTodos()){

            // TODO

        }


        List<MyWaypoint> pontos = new ArrayList<>();
        pontos.add(new MyWaypoint(Color.RED, " ", gerMapa.getPosicao(), 7));
        gerMapa.setPontos(pontos);
        gerMapa.getMapKit().repaint();
    }

    private class EventosMouse extends MouseAdapter {
        private int lastButton = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            JXMapViewer mapa = gerMapa.getMapKit().getMainMap();
            GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
            lastButton = e.getButton();
            // Botão direito: seleciona localização
            if (lastButton == MouseEvent.BUTTON3) {
                gerMapa.setPosicao(loc);
                gerMapa.getMapKit().repaint();
                mapkit.setOnContextMenuRequested(event -> contextMenu.show(mapkit, event.getScreenX(), event.getScreenY()));
            }
        }
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(gerMapa.getMapKit()));
    }

    void inicializacaoGerenciadores(){
        mouse = new Controller.EventosMouse();
        new Setup(gerAvioes, gerAero, gerCias, gerPaises, gerRotas);
        //Posicão inicial do Mapa, listeners do mouse e texto de FlipMap
        gerMapa = new GerenciadorMapa(gerAero.buscarPorCodigo("POA").getLocal(), GerenciadorMapa.FonteImagens.VirtualEarth);
        gerMapa.getMapKit().getMainMap().addMouseListener(mouse);
        gerMapa.getMapKit().getMainMap().addMouseMotionListener(mouse);
        BTNFlipMap.setText(gerMapa.getTipoMapa().toString());
    }

    @FXML void initialize(){
        contextMenu();
        inicializacaoGerenciadores();
        createSwingContent(mapkit);
        PainelPrincipal.setCenter(mapkit); //inicializacao do mapa
    }

}
