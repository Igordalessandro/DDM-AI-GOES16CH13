public class DDMRegion {
    public int x1; //big
    public int x2; //small
    public int y1; //big
    public int y2; //small

    public void checkRectangle(int xBeingWorked, int yBeingWorked) {
        if (xBeingWorked > x1) {
            x1 = xBeingWorked;
        }
        if (xBeingWorked < x2) {
            x2 = xBeingWorked;
        }
        if (yBeingWorked > y1) {
            y1 = yBeingWorked;
        }
        if (yBeingWorked < y2) {
            y2 = yBeingWorked;
        }
    }

    public void regionInit(int x, int y) {
        x1 = x;
        x2 = x;
        y1 = y;
        y2 = y;
    }
}
