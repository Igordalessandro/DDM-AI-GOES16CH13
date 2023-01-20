import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Thread.sleep;

public class DDMImageDivider {

    private static final HashMap<Integer, HashMap<Integer, Boolean>> imageMap = new HashMap<Integer, HashMap<Integer, Boolean>>();
    private static final HashMap<Integer, HashMap<Integer, Boolean>> filteredImageMap = new HashMap<Integer, HashMap<Integer, Boolean>>();
    private static final HashMap<Integer, HashMap<Integer, Boolean>> neighborhood = new HashMap<Integer, HashMap<Integer, Boolean>>();
    private static final HashMap<Integer, HashMap<Integer, HashMap<Integer, Boolean>>> regionMap = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Boolean>>>();
    private static final HashMap<Integer, DDMRegion> regionDDMRegionMap = new HashMap<Integer, DDMRegion>();
    private static String diretorio = "H:/GOES16/data";
    private static BufferedImage testImg;
    private static BufferedImage testImgPure;
    private static BufferedImage testResult;
    private static int regionsCounter;
    private static DDMGUI ddmgui;
    private static int DIF = 37; //default is 37
    private static int areaMinimaEmPixels = 0; //default is 57 for low resolution, 1300 for 1280x1548
    private static BufferedImage backup;
    private static boolean demo = true;
    private static int demoBarSize = 0;
    private static boolean demoModoRapido = true;
    private static DDMInternalConfigFile config;
    private static boolean calibration;
    private static boolean calibrationOutPutToSub;
    private static boolean DividirModoRapido = false;
    private static int DividirModoRapidoReScaleX = 300;
    private static int DividirModoRapidoReScaleY = 300;
    private static HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> FolderMapClone;
    private static BufferedImage imageDivideDataFolder;
    private static String nomeDivideDataFolder;
    private static String pathDivideDataFolder;
    private static int size;
    private static int count;

    public DDMImageDivider(DDMInternalConfigFile newConfig) {
        DividerReload(newConfig);
    }

    public void DivideDataFolder() {
        count = 0;
        demoModoRapido = false;
        calibration = false;
        FolderMapClone = config.FolderMap;
        FolderMapClone.forEach((ano, mesHashmap) -> {
            mesHashmap.forEach((mes, diaHashmap) -> {
                diaHashmap.forEach((dia, arquivoHashmap) -> {
                    arquivoHashmap.forEach((nomeDoArquivo, pathDoArquivo) -> {
                        count++;
                        ddmgui.setDisplay2text("Imagem: " + count + " De: " + (size + 1));
                        nomeDivideDataFolder = nomeDoArquivo;
                        pathDivideDataFolder = pathDoArquivo;
                        try {
                            imageDivideDataFolder = ImageIO.read(new File(pathDoArquivo+nomeDoArquivo));
                            try {
                                starter(false);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            });
        });
        ddmgui.ConsoleText("Operação de separar imagens termniada.");
    }

    public void DividerReload(DDMInternalConfigFile newConfig) {
        config = newConfig;
        ddmgui = DDMInternalConfigFile.dmmgui;
        diretorio = DDMInternalConfigFile.diretorio;
        DIF = DDMInternalConfigFile.DIF;
        demoBarSize = DDMInternalConfigFile.demoBarSize;
        areaMinimaEmPixels = DDMInternalConfigFile.areaMinimaEmPixels;
        demo = DDMInternalConfigFile.demoModoApresentacao;
        demoModoRapido = DDMInternalConfigFile.demoModoRapido;
        calibrationOutPutToSub = DDMInternalConfigFile.calibrationOutPutToSub;
        DividirModoRapido = DDMInternalConfigFile.DividirModoRapido;
        DividirModoRapidoReScaleX = DDMInternalConfigFile.DividirModoRapidoReScaleX;
        DividirModoRapidoReScaleY = DDMInternalConfigFile.DividirModoRapidoReScaleY;
    }

    public DDMInternalConfigFile LinkDataFolder() {
        config.dmmgui.ConsoleText("Mapeando fotos nas pastas (..)data/anos/mes/dias.");
        config.FolderMap.clear();
        size = 0;
        File dataFolder = new File(diretorio + "/data");
        String[] directoriesDATA = dataFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for (String ano : directoriesDATA) {
            File anoFolder = new File(diretorio + "/data/" + ano);
            String[] directoriesAno = anoFolder.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });
            for (String mes : directoriesAno) {
                File mesFolder = new File(diretorio + "/data/" + ano + "/" + mes);
                String[] directoriesMes = mesFolder.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {
                        return new File(current, name).isDirectory();
                    }
                });
                for (String dia : directoriesMes) {
                    File diaFolder = new File(diretorio + "/data/" + ano + "/" + mes + "/" + dia);
                    String[] directoriesDia = diaFolder.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File current, String name) {
                            return !(new File(current, name).isDirectory());
                        }
                    });
                    for (String arquivo : directoriesDia) {

                        if (!DDMInternalConfigFile.FolderMap.containsKey(ano)) {
                            HashMap<String, String> namePath = new HashMap<>();
                            namePath.put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/");
                            HashMap<String, HashMap<String, String>> diaName = new HashMap<>();
                            diaName.put(dia, namePath);
                            HashMap<String, HashMap<String, HashMap<String, String>>> MesDia = new HashMap<>();
                            MesDia.put(mes, diaName);
                            DDMInternalConfigFile.FolderMap.put(ano, MesDia);
                            size++;
                        } else {
                            if (!DDMInternalConfigFile.FolderMap.get(ano).containsKey(mes)) {
                                HashMap<String, String> namePath = new HashMap<>();
                                namePath.put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/");
                                HashMap<String, HashMap<String, String>> diaName = new HashMap<>();
                                diaName.put(dia, namePath);
                                DDMInternalConfigFile.FolderMap.get(ano).put(mes, diaName);
                                size++;
                            } else {
                                if (!DDMInternalConfigFile.FolderMap.get(ano).get(mes).containsKey(dia)) {
                                    HashMap<String, String> namePath = new HashMap<>();
                                    namePath.put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/");
                                    DDMInternalConfigFile.FolderMap.get(ano).get(mes).put(dia, namePath);
                                    size++;
                                } else {
                                    DDMInternalConfigFile.FolderMap.get(ano).get(mes).get(dia).put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/");
                                    size++;
                                }
                            }
                        }
                    }
                }
            }
        }
        DDMInternalConfigFile.dmmgui.ConsoleText("Foram mapeadas: " + size + " imagens!");
        DDMInternalConfigFile.dmmgui.ConsoleText("Pasta data Completamente mapeada com sucesso!");
        return config;
    }

    public void starter(boolean inputCalibration) throws IOException, InterruptedException {
        calibration = inputCalibration;

        //cleaning
        regionDDMRegionMap.clear();
        filteredImageMap.clear();
        neighborhood.clear();
        regionMap.clear();
        imageMap.clear();

        regionsCounter = 0;

        //Resize para operações rapidas!
        if (demoModoRapido && calibration) {
            Image testImgLoader = ImageIO.read(new File(diretorio + "/ImagemDeCalibracao/demo.jpg"));
            Image testImgResize = testImgLoader.getScaledInstance(DDMInternalConfigFile.demoModoRapidoReScaleX, DDMInternalConfigFile.demoModoRapidoReScaleY, Image.SCALE_FAST);
            testImg = new BufferedImage(testImgResize.getWidth(null), testImgResize.getHeight(null), BufferedImage.TYPE_INT_RGB);
            testImg.getGraphics().drawImage(testImgResize, 0, 0, null);
            Image testImgLoaderPure = ImageIO.read(new File(diretorio + "/ImagemDeCalibracao/demo.jpg"));
            Image testImgResizePure = testImgLoaderPure.getScaledInstance(DDMInternalConfigFile.demoModoRapidoReScaleX, DDMInternalConfigFile.demoModoRapidoReScaleY, Image.SCALE_FAST);
            testImgPure = new BufferedImage(testImgResizePure.getWidth(null), testImgResizePure.getHeight(null), BufferedImage.TYPE_INT_RGB);
            testImgPure.getGraphics().drawImage(testImgResizePure, 0, 0, null);
        } else {
            if (calibration) {
                testImg = ImageIO.read(new File(diretorio + "/ImagemDeCalibracao/demo.jpg"));
                testImgPure = ImageIO.read(new File(diretorio + "/ImagemDeCalibracao/demo.jpg"));
            } else {
                if (DividirModoRapido) {
                    Image testImgLoader = imageDivideDataFolder;
                    Image testImgResize = testImgLoader.getScaledInstance(DDMInternalConfigFile.DividirModoRapidoReScaleX, DDMInternalConfigFile.DividirModoRapidoReScaleY, Image.SCALE_FAST);
                    testImg = new BufferedImage(testImgResize.getWidth(null), testImgResize.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    testImg.getGraphics().drawImage(testImgResize, 0, 0, null);
                    Image testImgLoaderPure = imageDivideDataFolder;
                    Image testImgResizePure = testImgLoaderPure.getScaledInstance(DDMInternalConfigFile.DividirModoRapidoReScaleX, DDMInternalConfigFile.DividirModoRapidoReScaleY, Image.SCALE_FAST);
                    testImgPure = new BufferedImage(testImgResizePure.getWidth(null), testImgResizePure.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    testImgPure.getGraphics().drawImage(testImgResizePure, 0, 0, null);
                } else {
                    testImg = imageDivideDataFolder;
                    testImgPure = imageDivideDataFolder;
                }
            }

        }
        if (areaMinimaEmPixels == 0) {
            areaMinimaEmPixels = (int) Math.round(0.0006450112083914901 * (DDMInternalConfigFile.demoModoRapidoReScaleX * DDMInternalConfigFile.demoModoRapidoReScaleY) + 21.94899124476592);
        }

        ddmgui.setDisplay1ImageTypeOfBufferedImage(testImg);
        int threads = Runtime.getRuntime().availableProcessors();
        ArrayList<Thread> listOfAvailableThreads = new ArrayList<>();
        int Height = testImg.getHeight();
        int Width = testImg.getWidth();
        if (demoBarSize == 0) {
            demoBarSize = (int) Math.round(0.000003204203915537185 * Height * Width + 1.6510621935980003);
        }
        if (calibration) {
            ddmgui.ConsoleText("Nivel de detalhamento em area de pixels: " + areaMinimaEmPixels + " Coeficiente DIF: " + DIF);
            ddmgui.ConsoleText("Resolução da imagem sendo processada: (Width)X:" + Width + " (Height)Y: " + Height);
            ddmgui.ConsoleText("Atenção! Quanto maior a resolução mais demorado e detalhado fica.");
        } else {
            ddmgui.ConsoleText("Nivel de detalhamento em area de pixels: " + areaMinimaEmPixels + " Coeficiente DIF: " + DIF);
            ddmgui.ConsoleText("Resolução da imagem sendo processada: (Width)X:" + Width + " (Height)Y: " + Height);
            ddmgui.ConsoleText("Dividindo " + nomeDivideDataFolder + " (trabalhando fora de ordem no modo_rapido!)");
        }
        while (true) {
            for (int y = 0; y < Height; y++) {
                for (int x = 0; x < Width; x++) {
                    while (listOfAvailableThreads.size() >= threads) {
                        listOfAvailableThreads.removeIf(candidate -> !candidate.isAlive());
                    }
                    if (listOfAvailableThreads.size() < threads) {
                        DDMImageDividerMultiThreaded threadCode = new DDMImageDividerMultiThreaded(imageMap, x, y, DIF, testImg, threads);
                        Thread newThread = new Thread(threadCode);
                        listOfAvailableThreads.add(newThread);
                        newThread.start();
                    }
                    ddmgui.setDisplay1text("processando imagem: X: " + x + " Y: " + y + " CPU Threads: " + listOfAvailableThreads.size());
                }
            }
            for (int y = 0; y < Height; y++) {
                for (int x = 0; x < Width; x++) {
                    ddmgui.setDisplay1text("pintando: X: " + x + " Y: " + y + "");
                    try {
                        if (imageMap.get(y).get(x) == true) {
                            testImg.setRGB(x, y, 0x000000ff);
                            try {
                                filteredImageMap.get(y).put(x, false);
                            } catch (NullPointerException z) {
                                filteredImageMap.put(y, new HashMap<Integer, Boolean>());
                                filteredImageMap.get(y).put(x, false);
                            }
                        } else {

                        }
                    } catch (NullPointerException e) {
                        int clr = testImg.getRGB(x, y);
                        int red = (clr & 0x00ff0000) >> 16;
                        int green = (clr & 0x0000ff00) >> 8;
                        int blue = clr & 0x000000ff;
                        boolean acceptable;
                        acceptable = (Math.abs(red - green) > DIF) || (Math.abs(red - blue) > DIF) || (Math.abs(green - blue) > DIF);
                        imageMap.get(y).put(x, acceptable);
                        try {
                            if (imageMap.get(y).get(x) == true) {
                                testImg.setRGB(x, y, 0x000000ff);
                                try {
                                    filteredImageMap.get(y).put(x, false);
                                } catch (NullPointerException z) {
                                    filteredImageMap.put(y, new HashMap<Integer, Boolean>());
                                    filteredImageMap.get(y).put(x, false);
                                }
                            } else {

                            }
                        } catch (NullPointerException f) {

                        }
                    }
                }
            }
            ColorModel cm = testImg.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = testImg.copyData(null);
            backup = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
            ddmgui.setDisplay1ImageTypeOfBufferedImage(testImg);
            ddmgui.setDisplay1text("done heatmap");
            boolean squares = false;
            while (!squares) {
                regionsCounter = 0;
                for (int y = 0; y < Height; y++) {
                    for (int x = 0; x < Width; x++) {
                        if (filteredImageMap.containsKey(y)) {
                            if (filteredImageMap.get(y).containsKey(x)) {
                                if (filteredImageMap.get(y).get(x) == false) {
                                    ddmgui.setDisplay1text("Marcando Região Local: X: " + x + " Y: " + y + "");
                                    //unclaimed territory start!
                                    //color
                                    Random rand = new Random();
                                    float r = (float) (rand.nextFloat() * (0.8));
                                    float g = (float) (rand.nextFloat() * (0.8));
                                    float b = (float) (rand.nextFloat() * (0.8));
                                    Color newTerritoryColor = new Color(r, g, b);
                                    //setting up new region
                                    // true = occupied
                                    filteredImageMap.get(y).put(x, true);
                                    HashMap<Integer, Boolean> xInput = new HashMap<Integer, Boolean>();
                                    xInput.put(x, true);
                                    HashMap<Integer, HashMap<Integer, Boolean>> yInput = new HashMap<Integer, HashMap<Integer, Boolean>>();
                                    yInput.put(y, xInput);
                                    regionMap.put(regionsCounter, yInput);
                                    //region done
                                    //Map neighborhood
                                    //x-y+||x y+||x+y+
                                    //x-y ||x y ||x+y
                                    //x-y-||x y-||x+y-
                                    findNeighborhood(y, x, newTerritoryColor);
                                    ddmgui.setDisplay1ImageTypeOfBufferedImage(testImg);
                                    //up region
                                    regionsCounter++;
                                }
                            }
                        }
                    }
                }
                ColorModel model = testImg.getColorModel();
                boolean alphaMulti = model.isAlphaPremultiplied();
                WritableRaster wRaster = testImg.copyData(null);
                testResult = new BufferedImage(model, wRaster, alphaMulti, null);
                ddmgui.setDisplay1ImageTypeOfBufferedImage(testImg);
                dealWithSubSectors();
                squares = true;
            }
            if (calibration) {
                ddmgui.addButton0();
                ddmgui.addButton99();
                while (demo) {
                    ddmgui.updateUIButton99();
                    ddmgui.updateUIButton0();
                    if (DDMGUI.butZero == 1) {
                        return;
                    }
                    while (DDMGUI.but99 == 1) {
                        sleep(1000);
                        if (DDMGUI.butZero == 1) {
                            return;
                        }
                    }
                    ddmgui.updateUIButton99();
                    ddmgui.updateUIButton0();
                    ddmgui.setDisplay1ImageTypeOfBufferedImage(testImgPure);
                    ddmgui.setDisplay1text("COMPARE MODE 0");
                    sleep(1000);
                    if (DDMGUI.butZero == 1) {
                        return;
                    }
                    while (DDMGUI.but99 == 1) {
                        sleep(1000);
                        if (DDMGUI.butZero == 1) {
                            return;
                        }
                    }
                    ddmgui.updateUIButton99();
                    ddmgui.updateUIButton0();
                    ddmgui.setDisplay1ImageTypeOfBufferedImage(backup);
                    ddmgui.setDisplay1text("COMPARE MODE 1");
                    sleep(1000);
                    if (DDMGUI.butZero == 1) {
                        return;
                    }
                    while (DDMGUI.but99 == 1) {
                        sleep(1000);
                        if (DDMGUI.butZero == 1) {
                            return;
                        }
                    }
                    ddmgui.updateUIButton99();
                    ddmgui.updateUIButton0();
                    ddmgui.setDisplay1ImageTypeOfBufferedImage(testImg);
                    ddmgui.setDisplay1text("COMPARE MODE 2");
                    sleep(1000);
                    if (DDMGUI.butZero == 1) {
                        return;
                    }
                    while (DDMGUI.but99 == 1) {
                        sleep(1000);
                        if (DDMGUI.butZero == 1) {
                            return;
                        }
                    }
                    ddmgui.updateUIButton99();
                    ddmgui.updateUIButton0();
                    ddmgui.setDisplay1ImageTypeOfBufferedImage(testResult);
                    ddmgui.setDisplay1text("COMPARE MODE 3 (Border PX Size: " + demoBarSize + " )");
                    sleep(1000);
                }
            }
            break;
        }
    }

    public void dealWithSubSectors() throws IOException {
        for (int i = 0; i < regionMap.size(); i++) {
            int xMenor = regionDDMRegionMap.get(i).x2;
            int xMaior = regionDDMRegionMap.get(i).x1;
            int yMenor = regionDDMRegionMap.get(i).y2;
            int yMaior = regionDDMRegionMap.get(i).y1;
            int width = xMaior - xMenor;
            int height = yMaior - yMenor;
            ddmgui.setDisplay1text("Criando subsetor: " + i + " x: " + xMenor + " y: " + yMenor + " X: " + xMaior + " Y: " + yMaior);
            if ((xMaior - xMenor) * (yMaior - yMenor) > areaMinimaEmPixels) {
                BufferedImage preparado = testImgPure.getSubimage(xMenor, yMenor, width, height);
                BufferedImage imagemComCrop = new BufferedImage(preparado.getWidth(), preparado.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics g = imagemComCrop.createGraphics();
                g.drawImage(preparado, 0, 0, null);
                if (calibration) {
                    try {
                        Path path = Paths.get(diretorio + "ImagemDeCalibracao/sub");
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Path path = Paths.get(pathDivideDataFolder + "sub");
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (calibration && calibrationOutPutToSub) {
                    File outputFile = new File(diretorio + "ImagemDeCalibracao/sub/" + i + ".jpg");
                    ImageIO.write(imagemComCrop, "jpg", outputFile);
                }
                if (!calibration) {
                    File outputFile = new File(pathDivideDataFolder + "sub/" + nomeDivideDataFolder.substring(0,nomeDivideDataFolder.length()-4) + "_SubSector_" + i + ".jpg");
                    ImageIO.write(imagemComCrop, "jpg", outputFile);
                }
                if (calibration) {
                    Graphics2D g2d = testResult.createGraphics();
                    g2d.setColor(Color.red);
                    for (int j = 0; j < demoBarSize; j++) {
                        //top
                        g2d.drawLine(xMenor, yMaior - j, xMaior, yMaior - j);
                        //bottom
                        g2d.drawLine(xMenor, yMenor + j, xMaior, yMenor + j);
                        //left
                        g2d.drawLine(xMenor + j, yMaior, xMenor + j, yMenor);
                        //right
                        g2d.drawLine(xMaior - j, yMaior, xMaior - j, yMenor);
                        //done
                    }
                    g2d.dispose();
                } else {
                    Graphics2D g2d = testResult.createGraphics();
                    g2d.setColor(Color.red);
                    for (int j = 0; j < demoBarSize; j++) {
                        //top
                        g2d.drawLine(xMenor, yMaior - j, xMaior, yMaior - j);
                        //bottom
                        g2d.drawLine(xMenor, yMenor + j, xMaior, yMenor + j);
                        //left
                        g2d.drawLine(xMenor + j, yMaior, xMenor + j, yMenor);
                        //right
                        g2d.drawLine(xMaior - j, yMaior, xMaior - j, yMenor);
                        //done
                    }
                    g2d.dispose();
                    ddmgui.setDisplay2ImageTypeOfBufferedImage(testResult);
                }
            }
        }
    }

    public void findNeighborhood(int y, int x, Color regionColor) {
        int xBeingWorked = x;
        int yBeingWorked = y;
        //init
        HashMap<Integer, Boolean> xNeighborInit = new HashMap<Integer, Boolean>();
        xNeighborInit.clear();
        xNeighborInit.put(xBeingWorked, true);
        neighborhood.put(yBeingWorked, xNeighborInit);
        regionDDMRegionMap.put(regionsCounter, new DDMRegion());
        regionDDMRegionMap.get(regionsCounter).regionInit(x, y);
        //init done
        //coords init
        int[] coords = {-1, 0, -1, 1, -1, -1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};
        //coords init done
        while (neighborhood.size() != 0) {
            for (int i = 0; i < 15; i = i + 2) {
                int localY = yBeingWorked + coords[i + 1];
                int localX = xBeingWorked + coords[i];
                if (filteredImageMap.containsKey(localY)) {
                    if (filteredImageMap.get(localY).containsKey(localX)) {
                        if (filteredImageMap.get(localY).get(localX) == false) {
                            if (neighborhood.containsKey(localY)) {
                                if (neighborhood.get(localY).containsKey(localX) == false) {
                                    neighborhood.get(localY).put(localX, true);
                                    filteredImageMap.get(localY).put(localX, true);
                                    HashMap<Integer, Boolean> xInput = new HashMap<Integer, Boolean>();
                                    xInput.put(localX, true);
                                    regionMap.get(regionsCounter).put(localY, xInput);
                                }
                            } else {
                                filteredImageMap.get(localY).put(localX, true);
                                HashMap<Integer, Boolean> xInput = new HashMap<Integer, Boolean>();
                                xInput.put(localX, true);
                                neighborhood.put(localY, xInput);
                                regionMap.get(regionsCounter).put(localY, xInput);
                            }
                        }
                    }
                }
            }
            regionDDMRegionMap.get(regionsCounter).checkRectangle(xBeingWorked, yBeingWorked);
            testImg.setRGB(xBeingWorked, yBeingWorked, regionColor.getRGB());
            neighborhood.get(yBeingWorked).remove(xBeingWorked);
            if (neighborhood.get(yBeingWorked).size() == 0) {
                neighborhood.remove(yBeingWorked);
                if (neighborhood.size() != 0) {
                    yBeingWorked = (int) neighborhood.keySet().toArray()[0];
                }
            }
            if (neighborhood.size() == 0) {
                break;
            }
            xBeingWorked = (int) neighborhood.get(yBeingWorked).keySet().toArray()[0];
        }
    }
}
