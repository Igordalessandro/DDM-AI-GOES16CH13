import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DDMImageDividerMultiThreaded implements Runnable {

    private final int x;
    private final int y;
    private final int DIF;
    private final int threads;
    private static BufferedImage testImg;
    private static volatile HashMap<Integer,HashMap<Integer,Boolean>> imageMap;
    private Boolean acceptable;


    public DDMImageDividerMultiThreaded(HashMap<Integer,HashMap<Integer,Boolean>> inputimageMap, int inputx, int inputy, int inputDIF, BufferedImage inputtestImg, int inputthreads) {
        imageMap = inputimageMap;
        x = inputx;
        y = inputy;
        DIF = inputDIF;
        testImg = inputtestImg;
        threads = inputthreads;
    }
    public void DMMImageDividerMultiThreaded() {
        int clr = testImg.getRGB(x, y);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        acceptable = (Math.abs(red - green) > DIF) || (Math.abs(red - blue) > DIF) || (Math.abs(green - blue) > DIF);
        if(x<threads){
            if(x==0){
                HashMap<Integer,Boolean> done = new HashMap<>();
                done.put(x,acceptable);
                imageMap.put(y,done);
                return;
            }
            boolean done = false;
            while (!done){
                try {
                    imageMap.get(y).put(x,acceptable);
                    done = true;
                } catch (NullPointerException e){
                    done = false;
                }
            }
        } else {
            try {
                imageMap.get(y).put(x,acceptable);
            } catch (NullPointerException f){

            }
            return;
        }
    }
    @Override
    public void run() {
        DMMImageDividerMultiThreaded();
    }
}
