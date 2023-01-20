import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DDMFileDownloaderMultithreading implements Runnable {

    private static int tamanhoDaImagemEsperadaWidth;
    private static int tamanhoDaImagemEsperadaHeight;
    private static boolean recortarImagem;

    //Recorte
    private static int xInicialDaNovaImagem;

    //Recorte parametros
    private static int yInicialDaNovaImagem;
    private static int xPlusWidth;
    private static int yPlusHeight;
    private static DDMFileDownloader parentManager;
    private static volatile HashMap<Integer, DDMLinkImageDataFile> entryList;
    private final DDMLinkImageDataFile ProcessData = new DDMLinkImageDataFile();


    public DDMFileDownloaderMultithreading(int realTimeId, DDMLinkImageDataFile newFile, boolean inputrecortarImagem, int inputtamanhoDaImagemEsperadaWidth, int inputtamanhoDaImagemEsperadaHeight, int inputxInicialDaNovaImagem, int inputyInicialDaNovaImagem, int inputxPlusWidth, int inputyPlusHeight, HashMap<Integer, DDMLinkImageDataFile> inputEntryList) {
        ProcessData.Copy(newFile);
        recortarImagem = inputrecortarImagem;
        ProcessData.fileInternalId = 0 + realTimeId;
        tamanhoDaImagemEsperadaWidth = inputtamanhoDaImagemEsperadaWidth;
        tamanhoDaImagemEsperadaHeight = inputtamanhoDaImagemEsperadaHeight;
        xInicialDaNovaImagem = inputxInicialDaNovaImagem;
        yInicialDaNovaImagem = inputyInicialDaNovaImagem;
        xPlusWidth = inputxPlusWidth;
        yPlusHeight = inputyPlusHeight;
        entryList = inputEntryList;
    }

    public static void imageManager(DDMLinkImageDataFile ProcessData) throws IOException {
        if (ProcessData.dia == "9") {
            ProcessData.errorCode = 9;
            return;
        }
        URL downloadURL = new URL(ProcessData.urlInput);
        Image download = null;
        try {
            download = ImageIO.read(downloadURL);
            TimeUnit.SECONDS.sleep(1);
        } catch (IIOException e) {
            ProcessData.errorCode = 1;
            entryList.put(ProcessData.fileInternalId, ProcessData);
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (download == null) {
            ProcessData.errorCode = 2;
            entryList.put(ProcessData.fileInternalId, ProcessData);
            return;
        }
        if (download.getHeight(null) == tamanhoDaImagemEsperadaHeight) {
            if (download.getWidth(null) == tamanhoDaImagemEsperadaWidth) {
                BufferedImage imagemCarregada = new BufferedImage(tamanhoDaImagemEsperadaWidth, tamanhoDaImagemEsperadaHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D buff = imagemCarregada.createGraphics();
                buff.drawImage(download, 0, 0, null);
                ProcessData.imagem = imagemCarregada;
                if (recortarImagem) {
                    BufferedImage preparado = imagemCarregada.getSubimage(xInicialDaNovaImagem, yInicialDaNovaImagem, xPlusWidth, yPlusHeight);
                    BufferedImage imagemComCrop = new BufferedImage(preparado.getWidth(), preparado.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics g = imagemComCrop.createGraphics();
                    g.drawImage(preparado, 0, 0, null);
                    Files.createDirectories(Paths.get(ProcessData.diretorio + "/data/" + ProcessData.ano + "/" + ProcessData.mes + "/" + ProcessData.dia));
                    File outputfile = new File(ProcessData.diretorio + "/data/" + ProcessData.ano + "/" + ProcessData.mes + "/" + ProcessData.dia + "/" + ProcessData.ano + "." + ProcessData.mes + "." + ProcessData.dia + "." + ProcessData.horario + ".jpg");
                    ImageIO.write(imagemComCrop, "jpg", outputfile);
                    ProcessData.imagemCrop = imagemComCrop;
                } else {
                    Files.createDirectories(Paths.get(ProcessData.diretorio + "/data/" + ProcessData.ano + "/" + ProcessData.mes + "/" + ProcessData.dia));
                    File outputfile = new File(ProcessData.diretorio + "/data/" + ProcessData.ano + "/" + ProcessData.mes + "/" + ProcessData.dia + "/" + ProcessData.ano + "." + ProcessData.mes + "." + ProcessData.dia + "." + ProcessData.horario + ".jpg");
                    ImageIO.write(imagemCarregada, "jpg", outputfile);
                }
                entryList.put(ProcessData.fileInternalId, ProcessData);
                return;
            } else {
                ProcessData.errorCode = 4;
                entryList.put(ProcessData.fileInternalId, ProcessData);
            }
        } else {
            ProcessData.errorCode = 3;
            entryList.put(ProcessData.fileInternalId, ProcessData);
        }
        ProcessData.errorCode = 99;
        entryList.put(ProcessData.fileInternalId, ProcessData);
        return;
    }

    @Override
    public void run() {
        try {
            imageManager(ProcessData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
