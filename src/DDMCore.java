import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class DDMCore {


    //Variaveis de configuração start;
    //Local onde por os arquivos;
    //Recorte parametros;

    private static final DDMGUI ddmgui = new DDMGUI();
    private static final DDMLib ddmlib = new DDMLib();
    private static DDMInternalConfigFile loadedConfig = new DDMInternalConfigFile(ddmgui,ddmlib);
    private static final ArrayList<DDMLinkImageDataFile> CoreRequests = new ArrayList<>();
    private static final ArrayList<Integer> CoreCallsRequests = new ArrayList<Integer>();
    private static final DDMImageDivider DDMImageDivider1 = new DDMImageDivider(loadedConfig);
    //Abrindo Image handler/Downloader Multicore instanciado
    private static DDMFileDownloader dmmFileDownloader;
    private static final boolean DDMFileDownloaderIsStarted = false;
    private static final String date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    private static String programPath;
    private static DDMSaveLoad saveLoad;
    //Done

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        // Variaveis do sistema
        programPath = DDMCore.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        programPath = (new File(programPath)).getParentFile().getPath();
        if(System.getProperty("os.name").startsWith("Windows")){
            char c = programPath.charAt(0);
            if(!(c >= 'A' && c <= 'z')){
               while (!(c >= 'A' && c <= 'z')){
                   programPath = programPath.substring(1,programPath.length());
                   c = programPath.charAt(0);
               }
            }
        }
        saveLoad = new DDMSaveLoad(programPath);
        //Iniciando interface
        ddmgui.start(CoreCallsRequests);
        ddmgui.ToggleButtons(false);
        //Carrendo cofigurações
        loadedConfig = DDMSaveLoad.loadConfigs(loadedConfig,false);
        //respondendo input
        ddmgui.ToggleButtons(true);
        requests();
    }

    public static void requests() throws IOException, InterruptedException {
        do {
            if(CoreCallsRequests.contains(0)){
                CoreCallsRequests.remove((Integer) 0);
                DDMGUI.butZero = 1;
                sleep(1001);
                ddmgui.removeButton0();
                ddmgui.removeButton99();
                ddmgui.reset();
                ddmgui.resetDisplay();
                ddmgui.ToggleButtons(true);
                DDMGUI.butZero = 0;
                DDMGUI.but99 = 0;
            }
            if(CoreCallsRequests.contains(1)){
                CoreCallsRequests.remove((Integer) 1);
                ddmgui.resetDisplay();
                generateLinksAndDownload();
            }
            if(CoreCallsRequests.contains(2)){
                CoreCallsRequests.remove((Integer) 2);
                ddmgui.resetDisplay();
                if(DDMInternalConfigFile.CoreRequests.size() > 0){
                    try {
                        dmmFileDownloader.startDownloads();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    ddmgui.resetDisplay();
                    ddmgui.ConsoleText("É necessário gerar links de download primeiro. ");
                    ddmgui.ToggleButtons(true);
                }
            }
            if(CoreCallsRequests.contains(3)){
                CoreCallsRequests.remove((Integer) 3);
                DDMImageDivider1.DividerReload(loadedConfig);
                DDMImageDivider1.starter(true);
            }
            if(CoreCallsRequests.contains(4)){
                CoreCallsRequests.remove((Integer) 4);
                loadedConfig = DDMSaveLoad.loadConfigs(loadedConfig,false);
            }
            if(CoreCallsRequests.contains(5)){
                CoreCallsRequests.remove((Integer) 5);
                System.exit(0);
                return;
            }
            if(CoreCallsRequests.contains(6)){
                CoreCallsRequests.remove((Integer) 6);
                DDMImageDivider1.DividerReload(loadedConfig);
                loadedConfig = DDMImageDivider1.LinkDataFolder();
                ddmgui.ToggleButtons(true);
            }
            if(CoreCallsRequests.contains(7)){
                CoreCallsRequests.remove((Integer) 7);
                if(DDMInternalConfigFile.FolderMap.size() <= 0){
                    ddmgui.ConsoleText("É necessário Mapear a pasta data primeiro. ");
                    ddmgui.ToggleButtons(true);
                } else {
                    DDMImageDivider1.DividerReload(loadedConfig);
                    DDMImageDivider1.DivideDataFolder();
                    ddmgui.resetDisplay();
                    ddmgui.ToggleButtons(true);
                }
                // sete
                ddmgui.ToggleButtons(true);
            }
            if(CoreCallsRequests.contains(8)){
                CoreCallsRequests.remove((Integer) 8);
                DMMImageCompare compare = new DMMImageCompare(loadedConfig);
                compare.LinkDataFolder();
                ddmgui.resetDisplay();
                ddmgui.ToggleButtons(true);
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    public static void generateLinksAndDownload() {
        DDMInternalConfigFile.CoreRequests.clear();
        DDMLinkGenerator linkGen = new DDMLinkGenerator(loadedConfig);
        loadedConfig = linkGen.GenerateLinks();
        if(!DDMFileDownloaderIsStarted){
            dmmFileDownloader = new DDMFileDownloader(loadedConfig);
        }
    }
}
