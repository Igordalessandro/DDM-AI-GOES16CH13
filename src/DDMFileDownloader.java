import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DDMFileDownloader {

    private static final int tamanhoDaImagemEsperadaWidth = 2192;
    private static final int tamanhoDaImagemEsperadaHeight = 2328;
    private static final HashMap<Integer, DDMLinkImageDataFile> entryList = new HashMap<Integer, DDMLinkImageDataFile>();
    public static boolean recortarImagem = true;
    public static int xInicialDaNovaImagem = 763;
    public static int yInicialDaNovaImagem = 679;
    public static int xPlusWidth = 1280;
    public static int yPlusHeight = 1548;
    private static String date = "";
    private static String diretorio = "H:/GOES16/data";

    //Local onde por os arquivos
    private static boolean finish = false;
    private volatile ArrayList<DDMLinkImageDataFile> CoreRequests;
    private volatile DDMGUI ddmgui;
    private volatile boolean coreOperationsDone;
    private volatile int milisecInterval;

    public DDMFileDownloader(DDMInternalConfigFile inputConfig) {
        reloadConfig(inputConfig);
    }

    public void reloadConfig(DDMInternalConfigFile inputConfig) {
        diretorio = DDMInternalConfigFile.diretorio + "/data";
        milisecInterval = DDMInternalConfigFile.interval;
        ddmgui = DDMInternalConfigFile.dmmgui;
        CoreRequests = DDMInternalConfigFile.CoreRequests;
        recortarImagem = DDMInternalConfigFile.recortarImagem;
        xInicialDaNovaImagem = DDMInternalConfigFile.xInicialDaNovaImagem;
        yInicialDaNovaImagem = DDMInternalConfigFile.yInicialDaNovaImagem;
        xPlusWidth = DDMInternalConfigFile.xPlusWidth;
        yPlusHeight = DDMInternalConfigFile.yPlusHeight;
        ddmgui = DDMInternalConfigFile.dmmgui;
    }

    public void startDownloads() throws IOException, InterruptedException {
        DDMFileDownloaderMultithreadingStart();
    }

    public void DDMFileDownloaderMultithreadingStart() throws IOException {

        date = (new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));

        //Information id.

        int interfaceId = 1;
        int realTimeId = 1;
        int maxthreads = Runtime.getRuntime().availableProcessors();

        //Startup threads.

        ArrayList<Thread> listOfAvailableThreads = new ArrayList<>();

        //Startup map.

        while (!finish) {
            listOfAvailableThreads.removeIf(candidate -> !candidate.isAlive());
            if (listOfAvailableThreads.size() < maxthreads) {
                if (CoreRequests.size() >= realTimeId) {
                    DDMFileDownloaderMultithreading threadCode = new DDMFileDownloaderMultithreading(realTimeId, CoreRequests.get(realTimeId - 1), recortarImagem, tamanhoDaImagemEsperadaWidth, tamanhoDaImagemEsperadaHeight, xInicialDaNovaImagem, yInicialDaNovaImagem, xPlusWidth, yPlusHeight, entryList);
                    Thread newThread = new Thread(threadCode);
                    listOfAvailableThreads.add(newThread);
                    newThread.start();
                    realTimeId++;
                }
            }

            //skip

            if ((realTimeId - interfaceId) > 30) {
                interfaceId++;
            }

            if (entryList.containsKey(interfaceId)) {
                boolean check = false;
                try {
                    entryList.get(interfaceId);
                } catch (NullPointerException e) {
                    ddmgui.ConsoleText("Something unknow went terrible wrong with the ID: " + interfaceId);
                    interfaceId++;
                    check = true;
                }
                if (!check) {
                    try {
                        if (entryList.get(interfaceId).errorCode == 0) {
                            //no error
                            try {
                                ddmgui.setDisplay1ImageTypeOfBufferedImage(entryList.get(interfaceId).imagem);
                            } catch (NullPointerException e) {
                                ddmgui.ConsoleText("Something unknow went terrible wrong with: " + entryList.get(interfaceId).urlInput);
                                interfaceId++;
                            }
                            if (recortarImagem) {
                                try {
                                    ddmgui.setDisplay2ImageTypeOfBufferedImage(entryList.get(interfaceId).imagemCrop);
                                } catch (NullPointerException e) {
                                    try {
                                        ddmgui.setDisplay2ImageTypeOfBufferedImage(entryList.get(interfaceId).imagemCrop);
                                    } catch (NullPointerException ignored) {

                                    }
                                }
                            }
                            try {
                                ddmgui.ConsoleText("Baixando e processando imagem numero: " + interfaceId + " de " + CoreRequests.size() + " Data: " + entryList.get(interfaceId).ano + "/" + entryList.get(interfaceId).mes + "/" + entryList.get(interfaceId).dia + ":" + entryList.get(interfaceId).horario);
                            } catch (NullPointerException e) {
                                try {
                                    ddmgui.ConsoleText("Baixando e processando imagem numero: " + interfaceId + " de " + CoreRequests.size() + " Data: " + entryList.get(interfaceId).ano + "/" + entryList.get(interfaceId).mes + "/" + entryList.get(interfaceId).dia + ":" + entryList.get(interfaceId).horario);
                                } catch (NullPointerException ignored) {

                                }
                            }
                            try {
                                entryList.get(interfaceId).imagem = null;
                                entryList.get(interfaceId).imagemCrop = null;
                            } catch (NullPointerException ignored) {

                            }
                            interfaceId++;
                        } else {
                            int errorCode = 0;
                            try {
                                errorCode = entryList.get(interfaceId).errorCode;
                            } catch (NullPointerException e) {
                                ddmgui.ConsoleText("Problema de acesso a memoria do computador: " + entryList.get(interfaceId).ano + "/" + entryList.get(interfaceId).mes + "/" + entryList.get(interfaceId).dia + " horário: " + entryList.get(interfaceId).horario);
                                interfaceId++;
                            }
                            if (errorCode == 9) {
                                //internal bullshit
                                interfaceId++;
                            } else {
                                if ((errorCode == 1)) {
                                    ddmgui.ConsoleText("Problema 404: " + entryList.get(interfaceId).ano + "/" + entryList.get(interfaceId).mes + "/" + entryList.get(interfaceId).dia + " horário: " + entryList.get(interfaceId).horario);
                                    interfaceId++;
                                } else {
                                    if (errorCode == 2) {
                                        ddmgui.ConsoleText("Problema NULL(?): " + entryList.get(interfaceId).ano + "/" + entryList.get(interfaceId).mes + "/" + entryList.get(interfaceId).dia + " horário: " + entryList.get(interfaceId).horario);
                                        interfaceId++;
                                    } else {
                                        if (errorCode == 3) {
                                            // Wrong image Height
                                            try {
                                                ddmgui.ConsoleText("Height errado! expectativa: " + tamanhoDaImagemEsperadaHeight + " Recebido: " + entryList.get(interfaceId).imagem.getHeight(null));
                                                interfaceId++;
                                            } catch (NullPointerException e) {
                                                FileWriter logWriter = new FileWriter(entryList.get(interfaceId).diretorio + "/logs/" + "DaleDataMinerLog" + date + ".txt", true);
                                                ddmgui.ConsoleText("Height errado! Height unknow!");
                                                logWriter.write("ERROR: " + entryList.get(interfaceId).ano + "/" + entryList.get(interfaceId).mes + "/" + entryList.get(interfaceId).dia + " horário: " + entryList.get(interfaceId).horario + " TYPE: Height errado! Unknow height!");
                                                logWriter.close();
                                                interfaceId++;
                                            }
                                        } else {
                                            if (errorCode == 4) {
                                                try {
                                                    ddmgui.ConsoleText("Width errado! expectativa: " + tamanhoDaImagemEsperadaWidth + " Recebido: " + entryList.get(interfaceId).imagem.getWidth(null));
                                                    interfaceId++;
                                                } catch (NullPointerException e) {
                                                    ddmgui.ConsoleText("Width errado! Width unknow!");
                                                    interfaceId++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            if (coreOperationsDone) {
                if (interfaceId >= CoreRequests.size()) {
                    finish = true;
                    ddmgui.resetDisplay();
                    ddmgui.ConsoleText("Operação de download e processamento de imagens RAW finalizada.");
                    ddmgui.ConsoleText("Numero de imagens validadas: " + interfaceId + " Numero de imagens ignoradas: " + (realTimeId - interfaceId));
                    ddmgui.resetDisplay();
                    ddmgui.ToggleButtons(true);
                }
            }
        }
        ddmgui.ToggleButtons(true);
        coreOperationsDone = false;
        finish = false;
    }
}