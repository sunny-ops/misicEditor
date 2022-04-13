package musicEd.music;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import musicEd.reaction.Reaction;
import musicEd.reaction.Gesture;

public class Stem extends Duration implements Comparable<Stem>{
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;
    public Beam beam = null;


    public Stem(Staff staff, boolean isUp) {
        this.staff = staff;
        this.isUp = isUp;

        addReaction(new Reaction("E-E"){
            public int bid(Gesture gest) {
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH();
                int xs = Stem.this.heads.get(0).time.x;
                if (x1 >= xs || x2 < xs) {return UC.noBid;}
                int y1 = Stem.this.yLo(), y2 = Stem.this.yHi();
                if(y < y1 || y > y2) {return UC.noBid;}
                return Math.abs(y - (y1 + y2) / 2); 
            }
            public void act(Gesture gest) {
                Stem.this.incFlag();
            }
        });

        addReaction(new Reaction("W-W"){
            public int bid(Gesture gest) {
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH();
                int xs = Stem.this.heads.get(0).time.x;
                if (x1 >= xs || x2 < xs) {return UC.noBid;}
                int y1 = Stem.this.yLo(), y2 = Stem.this.yHi();
                if(y < y1 || y > y2) {return UC.noBid;}
                return Math.abs(y - (y1 + y2) / 2); 
            }
            public void act(Gesture gest) {
                Stem.this.decFlag();
            }
        });

    }

    public void show(Graphics g) {
        if (nFlag >= - 1 && heads.size() > 0) {
            int x = X(), h = staff.H(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
            if(nFlag > 0) { //upstem has downflags
                (isUp? Glyph.DNFLAGS: Glyph.UPFLAGS)[nFlag - 1].showAt(g, h, x, yB);
            }
        }
    }

    public Head firstHead() {
        return heads.get(isUp ? heads.size() - 1: 0);
    }
    
    public Head lastHead() {
        return heads.get(isUp ? 0: heads.size() - 1);
    }
    public int yFirstHead() {
        Head h = firstHead();
        return h.staff.yLine(h.line);
    }

    public int yBeamEnd() {
        Head h = lastHead();
        int line = h.line;
        line += isUp ? -7: 7; //Default length of 7
        int flagInc = nFlag > 2 ? 2 * (nFlag - 2) : 0;
        line += isUp ? -flagInc: flagInc;
        if((isUp && line > 4) || (!isUp && line < 4)) {
            line = 4;
        }
        return h.staff.yLine(line);
    }

    public int yLo() {return isUp? yBeamEnd(): yFirstHead();}

    public int yHi() {return isUp? yFirstHead(): yBeamEnd();}


    public int X() {
        Head h = firstHead();
        return h.time.x + (isUp? h.W(): 0);
        // return firstHead().time.x;
    }

    public void deleteStem() {
        staff.sys.stems.remove(this);
        deleteMass();
    }

    public void setWrongSides() {
        heads.sort();
        int i, last, next;
        if(isUp) {
            i = heads.size() - 1;
            last = 0;
            next = -1;
        }else {
            i = 0;
            last = heads.size() - 1;
            next = 1;
        }
        Head pH = heads.get(i); pH.wrongSide = false;
        while (i != last) {
            i += next;
            Head nH = heads.get(i); 
            nH.wrongSide = pH.staff == nH.staff && Math.abs(nH.line - pH.line) <= 1 && !pH.wrongSide;
            pH = nH;
        }
    }

    public int compareTo(Stem s) {return X() - s.X();}
    

    //----------------------List--------------------------
    public static class List extends ArrayList<Stem>{
        public void sort(){
            Collections.sort(this);
        }
    }

    
}
