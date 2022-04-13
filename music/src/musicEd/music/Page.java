package musicEd.music;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import musicEd.graphicsLib.G;
import musicEd.reaction.Gesture;
import musicEd.reaction.Mass;
import musicEd.reaction.Reaction;

public class Page extends Mass {
    public static Page PAGE; // singleton element, only have one alive

    public G.LoHi xMargin, yMargin;
    public int sysGap;
    public Sys.Fmt sysFmt = new Sys.Fmt();
    public ArrayList<Sys> sysList = new ArrayList<>();
    private static Reaction R1;

    public Page(int y) {
        super("BACK");
        PAGE = this;
        int MM = 50;
        xMargin = new G.LoHi(MM, UC.windowWidth - MM);
        yMargin = new G.LoHi(y, UC.windowHeight - y);
        addNewStaffFmtToSysFmt(y);
        addNewSys();
        
        addReaction(R1 = new Reaction("E-E") { // add new staff
            public int bid(Gesture gest) {
               return (gest.vs.yM() < PAGE.allSysBot()) ? UC.noBid: 0; 
            }

            @Override
            public void act(Gesture gest) {
                addNewStaffFmtToSysFmt(gest.vs.yM());
                
            }
            
        });

        addReaction(new Reaction("W-W") { //Add new system
            public int bid(Gesture gest) {
               return (gest.vs.yM() < PAGE.allSysBot()) ? UC.noBid: 0; 
            }

            @Override
            public void act(Gesture gest) {
                if (PAGE.sysList.size() == 1)  {
                    PAGE.sysGap = gest.vs.yM() - PAGE.allSysBot();
                    R1.disable(); // disable further additions to sysFmt
                }
                addNewSys();
                
            }
        });

    
    }
    public void addNewStaffFmtToSysFmt(int y) {
        int yOff = y - yMargin.lo;
        int iStaff = sysFmt.size(); // index of new staff
        sysFmt.addNew(yOff);
        for(Sys sys : sysList) {
            sys.addNewStaff(iStaff);
        }
    }

    public void addNewSys() {
        Sys sys = new Sys(this, sysList.size());
        sysList.add(sys);
        for (int i = 0; i < sysFmt.size(); i++) {
            sys.addNewStaff(i);
        }
    }
    public int sysTop(int iSys) {
        return yMargin.lo + iSys*(sysFmt.height() + sysGap);
    }

    public int allSysBot() {
        int n = sysList.size();
        return yMargin.lo + n * sysFmt.height() + (n-1) * sysGap;
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(0, yMargin.lo, 30, yMargin.lo);
        for(int i = 0; i < sysList.size(); i++) {
            sysFmt.showAt(g, sysTop(i), this);
        }
    }
}
