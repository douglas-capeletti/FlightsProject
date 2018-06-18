package gui;

import geografico.Geodesic;
import geografico.GeodesicData;
import geografico.GeodesicLine;
import geografico.GeodesicMask;
import modelo.objetos.Geo;
import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * Classe para gerenciar um mapa
 *
 * @author Marcelo Cohen
 * Edited version (Douglas Paz)
 */
public class GerenciadorMapa {

	final JXMapKit jXMapKit;

	private WaypointPainter<MyWaypoint> pontosPainter;
	private GeoPosition posicao;
	private ArrayList<Tracado> linhas;

	private int maxZoomText;
	private double wxmin, wxmax;
	private double deltaldp;
	private Font font;
	private boolean useGeodesic; // true = desenha linhas geodésicas de um ponto a outro, false = desenha linhas simples
	private FonteImagens tipoMapa;
	public enum FonteImagens {
		OpenStreetMap, VirtualEarth
	}

	public GerenciadorMapa(GeoPosition centro, FonteImagens fonte) {
		jXMapKit = new JXMapKit();
		setTipoMapa(fonte);


		//Alteracoes no zoom padrao
		maxZoomText = 10;
		jXMapKit.setZoom(10);

		// Define local inicial do mapa
		setPosicaoVisual(centro);

		// Opcoes extras sobre posicao e fonte
		wxmin = jXMapKit.getMainMap().getTileFactory().getInfo().getLongitudeDegreeWidthInPixels(17);
		wxmax = jXMapKit.getMainMap().getTileFactory().getInfo().getLongitudeDegreeWidthInPixels(1);
		deltaldp = wxmax - wxmin;
		font = new Font("Sans", Font.PLAIN, 20);
		useGeodesic = true;

		// Criando um objeto para "pintar" os pontos
		pontosPainter = new WaypointPainter<MyWaypoint>();

		// Criando um objeto para desenhar os pontos
		pontosPainter.setRenderer((g, viewer, wp) -> {

			// Desenha cada waypoint como um pequeno círculo
			Point2D point = viewer.getTileFactory().geoToPixel(wp.getPosition(), viewer.getZoom());
			float x = (float) point.getX();
			float y = (float) point.getY();

			// Define a cor do waypoint
			g.setColor(wp.getColor());
			double s = wp.getSize();
			double zoom = jXMapKit.getZoomSlider().getValue();

			double zoomfat = viewer.getTileFactory().getInfo().getLongitudeDegreeWidthInPixels((int) zoom);
			double fat = (zoomfat - wxmin) / deltaldp * 2;
			// System.out.println("Fat: "+fat+" zoom: "+zoom);
			// s = (1+s)+100*fat;
			s = 1 + s * 1000 * fat;
			// System.out.println("zoom:"+zoom+" - "+s);
			int offset = (int) s;
			g.fill(new Ellipse2D.Double(x - s, y - s, s + s, s + s));
			g.setFont(font);
			if (jXMapKit.getZoomSlider().getValue() < maxZoomText) {
				// if(wp.getLabel().equals("POA"))
				// System.out.println("POA: "+x+" "+y);
				g.setColor(Color.BLACK);
				g.drawString(wp.getLabel(), x + offset + 1, y + offset + 1);
				g.setColor(wp.getColor());
				g.drawString(wp.getLabel(), x + offset, y + offset);
			}

		});

		// Criando um objeto para desenhar o traçado das linhas
		Painter<JXMapViewer> linhasPainter = (g, map, w, h) -> {
			for (Tracado tr : linhas) {
				ArrayList<Geo> pontos = tr.getPontos();
				Color cor = tr.getCor();
				int x[] = new int[pontos.size()];
				int y[] = new int[pontos.size()];

				Point2D p0 = map.convertGeoPositionToPoint(pontos.get(0));
				Point2D p1 = map.convertGeoPositionToPoint(pontos.get(1));

				int xmid = (int)(p0.getX() + p1.getX()) / 2;
				int ymid = (int)(p0.getY() + p1.getY()) / 2;
				g.setColor(Color.RED);

				for (int i = 0; i < pontos.size(); i++) {
					Point2D point = map.convertGeoPositionToPoint(pontos.get(i));
					x[i] = (int) point.getX();
					y[i] = (int) point.getY();
				}
				// int xPoints[] = { 0, 20, 40, 100, 120 };
				// int yPoints[] = { 0, 20, 40, 100, 120 };
				g.setColor(cor);
				g.setStroke(new BasicStroke(tr.getWidth()));
				g.drawPolyline(x, y, x.length);
			}
		};

		// Criando um objeto para desenhar os elementos de interface
		// (ponto selecionado, etc)
		Painter<JXMapViewer> guiPainter = (g, map, w, h) -> {
			if (posicao == null)
				return;
			Point2D point = map.convertGeoPositionToPoint(posicao);
			int x = (int) point.getX();
			int y = (int) point.getY();
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(2));
			g.draw(new Rectangle2D.Float(x - 6, y - 6, 12, 12));
		};

		// Um CompoundPainter permite combinar vários painters ao mesmo tempo...
		CompoundPainter cp = new CompoundPainter();
		cp.setPainters(pontosPainter, linhasPainter, guiPainter);

		jXMapKit.getMainMap().setOverlayPainter(cp);

		posicao = null;
		linhas = new ArrayList<Tracado>();
	}

	/*
	 * Informa o nível máximo de zoom para exibir os labels dos pontos
	 *
	 * @param nível máximo
	 */
	public void setMaxZoomText(int max) {
		maxZoomText = max;
	}

	/*
	 * Informa a localização de um ponto
	 *
	 * @param ponto central
	 */
	public void setPosicao(GeoPosition sel) {
		this.posicao = sel;
	}

	/*
	 * Informa se desejamos desenhar as linhas
	 * com arcos (true) ou linhas comuns (false)
	 */
	public void setArcos(boolean g) {
		this.useGeodesic = true;
	}

	/*
	 * Retorna a localização de um ponto
	 *
	 * @returns ponto selecionado
	 */
	public GeoPosition getPosicao() {
		return posicao;
	}

	/*
	 * Informa os pontos a serem desenhados (precisa ser chamado a cada vez que
	 * mudar)
	 * 
	 * @param lista lista de pontos (objetos MyWaypoint)
	 */
	public void setPontos(List<MyWaypoint> lista) {
		pontosPainter.setWaypoints(new HashSet<MyWaypoint>(lista));
	}

	public void setPosicaoVisual(GeoPosition posicaoAtual) {
		jXMapKit.setAddressLocation(posicaoAtual);
		jXMapKit.setAddressLocationShown(false);
		this.posicao = posicaoAtual;
	}

	public FonteImagens getTipoMapa() {
		return tipoMapa;
	}

	public void setTipoMapa(FonteImagens tipoMapa) {
		TileFactoryInfo info = null;
		if (tipoMapa == FonteImagens.OpenStreetMap) info = new OSMTileFactoryInfo();
		else info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
		jXMapKit.setTileFactory(new DefaultTileFactory(info));
		this.tipoMapa = tipoMapa;
	}

	public void flipTipoMapa(){
		setTipoMapa(getTipoMapa() == FonteImagens.VirtualEarth ? FonteImagens.OpenStreetMap : FonteImagens.VirtualEarth);
		setPosicaoVisual(posicao);
	}

	/*
	 * Adiciona um novo traçado ao mapa (o traçado tem um conjunto de pontos e
	 * uma cor)
	 */
	public void addTracado(Tracado tr) {
		if(!useGeodesic)
		{
			linhas.add(tr);
			return;
		}			
		Geodesic geod = Geodesic.WGS84;
		Tracado novo = new Tracado();
		novo.setWidth(tr.getWidth());
		novo.setCor(tr.getCor());
		novo.setLabel(tr.getLabel());
		ArrayList<Geo> pontos = tr.getPontos();
		for (int pos = 0; pos < pontos.size() - 1; pos++) {
			double lat1 = pontos.get(pos).getLatitude();
			double lat2 = pontos.get(pos + 1).getLatitude();
			double lon1 = pontos.get(pos).getLongitude();
			double lon2 = pontos.get(pos + 1).getLongitude();

			GeodesicLine line = geod.InverseLine(lat1, lon1, lat2, lon2,GeodesicMask.DISTANCE_IN | GeodesicMask.LATITUDE | GeodesicMask.LONGITUDE);
			double ds0 = 200e3; // Nominal distance between points = 200 km
			int num = (int) (Math.ceil(line.Distance() / ds0));
			double da = line.Arc() / num;
			for (int i = 0; i <= num; ++i) {
				GeodesicData g = line.ArcPosition(i * da, GeodesicMask.LATITUDE | GeodesicMask.LONGITUDE);
				if(Math.abs(g.lon1-g.lon2)>180) continue;
				novo.addPonto(new Geo(g.lat2, g.lon2));
			}
		}		
		linhas.add(novo);
	}

	public void clear() {
		linhas.clear();
	}

	public int totalTracados() {
		return linhas.size();
	}

	/*
	 * Retorna a referência ao objeto JXMapKit, para ajuste de parâmetros (se
	 * for o caso)
	 * 
	 * @returns referência para objeto JXMapKit em uso
	 */
	public JXMapKit getMapKit() {
		return jXMapKit;
	}

}
