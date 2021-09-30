package Entity;

import TileMap.TileMap;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD extends MapObject{
	private Player player;
	private BufferedImage image;
	private BufferedImage imageskulls;
	private BufferedImage imagefill;
	private BufferedImage imagedanger;

	private Font font;

	public HUD(Player p, TileMap tm) {
		super(tm);
		player = p;
		try {
			imagedanger = ImageIO.read(
					getClass().getResourceAsStream(
							"/HUD/danger.png"
					)
			);
			font = new Font("Arial", Font.PLAIN, 10);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void update(){

	}

	public void draw(Graphics2D g) {
		setMapPosition();

		if(player.getHealth()<20) {
			g.drawImage(imagedanger, 0, 0, null);
		}
	}
}













