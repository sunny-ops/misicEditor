# musicEditor

Overview

This is a gesture based music notation system that allow users to draw music elements like staffs, systems, node heads and stems. The first thing to do is the gesture training process. I implemented a distance metric to recognize gestures by dragging the mouse into shapes like (East - West, West - East, North - South, NE - SW). Then used AI-pattern recognition to interpret strokes in a scaling-isonorphical manner, and then draw music notations. 

Run

◦ Open main.java in the music/src/musicEd folder in IDE

◦ Run the main.java (wih WindowPANEL = new ShapeTrainer()) to train some shapes and save in a file.

◦ Run the main.java (wih WindowPANEL = new MusicApp()) and drag mouse to draw music notations.
