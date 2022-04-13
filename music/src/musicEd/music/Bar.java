package musicEd.music;

import musicEd.graphicsLib.G;
import musicEd.reaction.Gesture;
import musicEd.reaction.Mass;
import musicEd.reaction.Reaction;

import java.awt.Graphics;
import java.awt.Color;

public class Bar extends Mass{
    public Sys sys;
    public int x;
    public int barType = 0;
    public static final int LEFT = 8;
    public static final int RIGHT = 4;
    
    /*
      barType = 
      0 - normal thin line
      1 - double thin line
      2 - thin : fat i.e.  fine line
      4-7 - fat: thin dots i.e. repeat to the right
      8-11 - dots thin: fat i.e. repeat to the left
      12 - dot thin: fat: thin dots i.e. repeat both sides 
    */


    public Bar(Sys sys, int x) {
        super("BACK");
        this.sys = sys;
        this.x = x;

        addReaction(new Reaction("S-S") {
            public int bid(Gesture gest) {
                int x = gest.vs.xM();
                if (Math.abs(x - Bar.this.x) > 2 * UC.marginSnap) {return UC.noBid;}
                int y1 = gest.vs.yL();
                int y2 = gest.vs.yH();
                int sysTop = Bar.this.sys.yTop(), sysBar = Bar.this.sys.yBot();
                if(y1 < sysTop - 10 || y2 > sysBar + 10) {return UC.noBid;}
                G.LoHi m = Page.PAGE.xMargin;
                if (x < m.lo || x > m.hi + UC.marginSnap) {return UC.noBid;}
                int d = Math.abs(x - Bar.this.x);
                return d > UC.marginSnap ?  UC.noBid: d;
            }

            public void act(Gesture gest) {Bar.this.cycleType();}
            
        });

        addReaction(new Reaction("DOT") { // this means DOT the bar line
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y = gest.vs.yM();
                if(y < Bar.this.sys.yTop() || y > Bar.this.sys.yBot()) {return UC.noBid;}
                int d = Math.abs(x - Bar.this.x);
                if (d > 3 * Page.PAGE.sysFmt.maxH) {return UC.noBid;}
                return d;
            }

            public void act(Gesture gest) {
                if (gest.vs.xM() < Bar.this.x) {Bar.this.toggleLeft();}
                else {Bar.this.toggleRight();}


            }
            
        });
    }

    public void show(Graphics g) {
        g.setColor(Color.BLACK);
        int sysTop = sys.yTop(), y1 = 0, y2 = 0; // y1, y2 mark top and bottom
        boolean justSawBreak = true; // this signials that we are athe the top of a new component
        for (Staff staff: sys.staffs) {
            int staffTop = staff.yTop();
            if (justSawBreak) { y1 = staff.yTop();} // remember staff component 
            y2 = staff.yBot(); // assume this ends component
            if (!staff.fmt.barContinues) { // this is how we know that we are at the end
                drawVerticalLines(g, y1, y2);
            }
            justSawBreak = !staff.fmt.barContinues;
            if (barType > 3) {
                drawDots(g, x, staffTop);
            }
        }
        // if (barType == 1) {
        //     g.setColor(Color.BLUE);
        // }
        // int y = sys.yTop(), N = sys.staffs.size();
        // for (int i = 0; i < N; i++) {
        //     Staff staff = sys.staffs.get(i);
        //     g.drawLine(x, staff.yTop(), x, staff.yBot());
        // }

    }

    public void drawVerticalLines(Graphics g, int y1, int y2) {
        int H = sys.page.sysFmt.maxH;
        if (barType == 0) {
            thinBar(g, x, y1, y2);
        }
        if (barType == 1) {
            thinBar(g, x, y1, y2);
            thinBar(g, x - H, y1, y2);
        }
        if (barType == 2) {
            fatBar(g, x - H, y1, y2, H);
            thinBar(g, x - 2*H, y1, y2);
        }
        if (barType >= 4) {
            fatBar(g, x-H, y1, y2, H);
            if ((barType & LEFT) != 0) {
                thinBar(g, x - 2*H, y1, y2);
                wings(g, x-2*H, y1, y2, -H, H);
            }
            if ((barType & RIGHT) != 0) {
                thinBar(g, x + H, y1, y2);
                wings(g, x + H, y1, y2, H, H);
            }
        }
    }

    public static void thinBar(Graphics g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
    }

    public static void fatBar(Graphics g, int x, int y1, int y2, int dx) {
        g.fillRect(x, y1, dx, y2 - y1);
    }
    
    public static void wings(Graphics g, int x, int y1, int y2, int dx, int dy) {
        g.drawLine(x, y1, x + dx, y1 - dy);
        g.drawLine(x, y2, x + dx, y2 + dy);
    }

    public void drawDots(Graphics g, int x, int top) {
        // dots from top of the single staff
        // assume nLine = 5
        int H = sys.page.sysFmt.maxH;
        if ((barType & LEFT) != 0) {
            g.fillOval(x - 3 * H, top + 11 * H / 4, H/2, H/2);
            g.fillOval(x - 3 * H, top + 19 * H / 4, H/2, H/2);
        }

        if ((barType & RIGHT) != 0) {
            g.fillOval(x + 3 * H/2, top + 11 * H / 4, H/2, H/2);
            g.fillOval(x + 3 * H/2, top + 19 * H / 4, H/2, H/2);
        }
 
    }

    public void cycleType() { barType ++; if (barType > 2) {barType = 0;}}

    public void toggleLeft() {barType = barType^LEFT;}
    public void toggleRight() {barType = barType^RIGHT;}
    
}
