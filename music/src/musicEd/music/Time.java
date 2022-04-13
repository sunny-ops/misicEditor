package musicEd.music;

import java.util.ArrayList;

public class Time {
    public int x;
    public Head.List heads = new Head.List();

    private Time(int x, List tl) {
        this.x = x;
        tl.add(this);
    }

    public void unStemHeads(int y1, int y2) {
        for (Head h: heads) {
            int y = h.Y();
            if (y > y1 && y < y2) {
                h.unStem();
            }
        }
    }

    public void stemHeads(Staff staff, boolean up, int y1, int y2) {
        Stem s = new Stem(staff, up);
        for (Head h: heads) {
            int y = h.Y();
            if(y > y1 && y < y2) {h.joinStem(s);}
        }
        if (s.heads.size() == 0) {
            System.out.println("Empty headlist after stemming!");
        } else {
            s.setWrongSides();
            s.staff.sys.stems.add(s);
        }
    }
    //---------------List----------------
    public static class List extends ArrayList<Time>{
        public Sys sys;

        public List(Sys sys){this.sys = sys;}
        public Time getTime(int x) {
            Time res = null;
            int dist = UC.snapTime;
            for (Time t: this) {
                int d = Math.abs(t.x - x);
                if(d < dist) {
                    dist = d;
                    res = t; // only find the time < snapTime
                }
            }
            return (res != null) ? res : new Time(x, this);
        }
    }
}
