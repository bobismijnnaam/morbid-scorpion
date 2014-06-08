package nl.plusminos.gdx.morbidscorpion.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

public class FontManager implements Disposable {

	FreeTypeFontGenerator fg;
	ObjectMap<Integer, BitmapFont> fonts = new ObjectMap<Integer, BitmapFont>();

	public FontManager(String targetFont) {
		fg = new FreeTypeFontGenerator(Gdx.files.internal(targetFont));
	}
	
	public BitmapFont getFont(int size) {
		if (fonts.containsKey(size)) {
			return fonts.get(size);
		} else {
			FreeTypeFontParameter fp = new FreeTypeFontParameter();
			fp.size = size;
			BitmapFont generatedFont = fg.generateFont(fp);
			fonts.put(size, generatedFont);
			return generatedFont;
		}
	}

	@Override
	public void dispose() {
		for (Values<BitmapFont> allFonts = fonts.values(); allFonts.hasNext;) {
			BitmapFont font = allFonts.next();
			font.dispose();
		}
		
		fg.dispose();
	}

}
