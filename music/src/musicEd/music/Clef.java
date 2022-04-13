package musicEd.music;
import java.awt.Graphics;

public class Clef {
    public Glyph glyph = null;
    public static Clef G = new Clef(Glyph.CLEF_G);
    public static Clef F = new Clef(Glyph.CLEF_F);
    public static Clef C = new Clef(Glyph.CLEF_C);

    public Clef(Glyph glyph) {this.glyph = glyph;}
    public void showAt (Graphics g, int x, int yTop, int H) {
        glyph.showAt(g, H, x, yTop + 4*H);
    }


    
}
