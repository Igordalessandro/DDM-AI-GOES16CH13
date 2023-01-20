import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class DDMInternalConfigFile {

    //=========================||
    //  NÃO FORMATADO PARA JSON||
    //    PARA USO INTERNO     ||
    //       -> APENAS! <-     ||
    //=========================||

    public static LinkedList<String> anosList = new LinkedList<>();
    public static LinkedList<String> anosBissextos = new LinkedList<String>();
    public static int mesInicial = 5;
    public static int mesFinal = 5;
    public static String diretorio = "";
    public static boolean recortarImagem = true;
    public static int xInicialDaNovaImagem = 763;
    public static int yInicialDaNovaImagem = 679;
    public static int xPlusWidth = 1280;
    public static int yPlusHeight = 1548;
    public static int interval = 100;
    public static int DIF = 37; //default is 57
    public static int areaMinimaEmPixels = 0;
    public static int demoBarSize = 0;
    public static boolean demoModoApresentacao = true;
    public static boolean demoModoRapido = true;
    public static int demoModoRapidoReScaleX = 300;
    public static int demoModoRapidoReScaleY = 300;
    public static boolean DividirModoRapido = false;
    public static int DividirModoRapidoReScaleX = 300;
    public static int DividirModoRapidoReScaleY = 300;
    public static DDMGUI dmmgui;
    public static DDMLib ddmlib;
    public static boolean calibrationOutPutToSub = false;
    public static boolean naoFazerComparacoesNoProprioMes = true;
    public static int porcentagemDasComparacoesDIferencaDeTamanhoAceitavel = 5;
    public static int porcentagemDasComparacoesMinimaDeSimilaridade = 90;

    public static volatile ArrayList<DDMLinkImageDataFile> CoreRequests = new ArrayList<>();
    public static HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> FolderMap = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>();
    public static HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> FolderMapSubs = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>>();

    public DDMInternalConfigFile(DDMGUI DMMGUIInput, DDMLib DMMLibInput) {
        dmmgui = DMMGUIInput;
        ddmlib = DMMLibInput;
    }
}
