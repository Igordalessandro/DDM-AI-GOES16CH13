import com.google.gson.JsonArray;

public class DDMConfig {

    //Temporal
    public JsonArray anosList = new JsonArray();
    public JsonArray anosbissextos = new JsonArray();
    public int mesInicial = 5;
    public int mesFinal = 5;

    //Local onde por os arquivos:
    public String diretorio = "";

    //modo demo (calibração)
    public boolean demoModoApresentacao = true;
    //reduz a resolução da imagem para 150x150 para processamento rapido.
    public boolean demoModoRapido = true;
    public int demoModoRapidoReScaleX = 300;
    public int demoModoRapidoReScaleY = 300;
    public boolean calibrationOutPutToSubFolder = false;
    public boolean DividirModoRapido = false;
    public int DividirModoRapidoReScaleX = 300;
    public int DividirModoRapidoReScaleY = 300;
    //Manipulação de imagem
    public boolean recortarImagem = true;
    //x = baixo, esquerda.
    public int xInicialDaNovaImagem = 763;
    //y = baixo, esquerda.
    public int yInicialDaNovaImagem = 679;
    //X final - X inicial.
    public int xPlusWidth = 1280;
    //Y final - Y inicial.
    public int yPlusHeight = 1548;
    public boolean naoFazerComparacoesNoProprioMes = true;
    public int porcentagemDasComparacoesDIferencaDeTamanhoAceitavel = 5;
    public int porcentagemDasComparacoesMinimaDeSimilaridade = 90;

    //Técnico
    //Interval é o tempo minimo entre download das imagens (usar somente no caso de imagens sendo baixadas faltando pedaços)
    public int interval = 100;
    //DIF é quão diferente (%) o pixel tem que ser de branco ou preto para ser considerado.
    public int DIF = 37; //default is 57
    //areaMinimaEmPixels
    public int areaMinimaEmPixels = 0; //default is 57 for low resolution, 1300 for 1280x1548
    // zero = automático!
    public int demoBarSize = 0;

}
