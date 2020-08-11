package fr.trxyy.alternative.alternative_api.utils;

import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class FontLoader {
	private static Font font;

	private static float size;

	public void loadFont(String s) {
		Font.loadFont(this.getClass().getResourceAsStream("/resources/" + s), 14.0);
	}

	public void setFont(String fontName, float size) {
		FontLoader.size = size;
		Font.font(fontName,  FontLoader.size);
	}

	public static Font loadFont(String fullFont, String fontName, float size) {
		FontLoader.size = size;
		Font.loadFont(FontLoader.class.getResourceAsStream("/resources/" + fullFont), 14.0);
		font = Font.font(fontName,  FontLoader.size);
		return font;
	}
	
	public static Font loadFontItalic(String fullFont, String fontName,float size) {
		FontLoader.size = size;
		Font.loadFont(FontLoader.class.getResourceAsStream("/resources/" + fullFont), 14.0);
		font = Font.font(fontName, FontPosture.ITALIC,  FontLoader.size);
		return font;
	}
	
	public static java.awt.Font loadFontAWT(String fullFont, String fontName, float size) {
		FontLoader.size = size;
		java.awt.Font font = null;
		InputStream is = FontLoader.class.getResourceAsStream("/resources/" + fullFont);
			try {
				font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is).deriveFont(java.awt.Font.PLAIN, 15f);
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
		assert font != null;
		font.deriveFont(FontLoader.size);
		return font;
	}

	public static void setSize(float size)
	{
		FontLoader.size = size;
	}

}