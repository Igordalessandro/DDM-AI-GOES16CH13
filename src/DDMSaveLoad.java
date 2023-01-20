import com.google.gson.*;

import java.io.*;

public class DDMSaveLoad {

    private static String programPath;
    private static DDMInternalConfigFile loadedConfig;

    public DDMSaveLoad(String inputProgramPath) {
        programPath = inputProgramPath;
    }


    public static DDMInternalConfigFile loadConfigs(DDMInternalConfigFile inputLoadedConfig, boolean consoleNoControl) throws IOException {
        loadedConfig = inputLoadedConfig;
        boolean ignoreMe = false;
        try{
            DDMInternalConfigFile.dmmgui.ToggleButtons(false);
            File jsonInput = new File(programPath,"config.json");
            FileReader reader = new FileReader(jsonInput);
            JsonObject configFile = (JsonObject) JsonParser.parseReader(reader);
            reader.close();
            if(configFile.get("diretorio").getAsString().chars().count() == 0 || configFile.get("diretorio").getAsString() == null){
                DDMInternalConfigFile.diretorio = programPath;
            }
            JsonArray anosArray = new JsonArray();
            anosArray = configFile.get("anosList").getAsJsonArray();
            if (anosArray != null && anosArray.size() > 0) {
                DDMInternalConfigFile.anosList.clear();
                for(int i=0; i< anosArray.size(); i++){
                    DDMInternalConfigFile.anosList.add(anosArray.get(i).getAsString());
                }
            } else {
                DDMInternalConfigFile.anosList.add("2017");
                DDMInternalConfigFile.anosList.add("2018");
                DDMInternalConfigFile.anosList.add("2019");
                DDMInternalConfigFile.anosList.add("2020");
                DDMInternalConfigFile.anosList.add("2021");
                DDMInternalConfigFile.anosBissextos.add("2020");
                throw new RuntimeException("failed to load years!");
            }
            JsonArray anosBissextoArray = new JsonArray();
            anosBissextoArray = configFile.get("anosbissextos").getAsJsonArray();
            if (anosBissextoArray != null || anosBissextoArray.size() > 0) {
                DDMInternalConfigFile.anosBissextos.clear();
                for(int i=0; i< anosBissextoArray.size(); i++){
                    DDMInternalConfigFile.anosBissextos.add(anosBissextoArray.get(i).getAsString());
                }
            }

            DDMInternalConfigFile.mesInicial = configFile.get("mesInicial").getAsInt();
            DDMInternalConfigFile.mesFinal = configFile.get("mesFinal").getAsInt();
            DDMInternalConfigFile.recortarImagem = configFile.get("recortarImagem").getAsBoolean();
            DDMInternalConfigFile.xInicialDaNovaImagem = configFile.get("xInicialDaNovaImagem").getAsInt();
            DDMInternalConfigFile.yInicialDaNovaImagem = configFile.get("yInicialDaNovaImagem").getAsInt();
            DDMInternalConfigFile.xPlusWidth = configFile.get("xPlusWidth").getAsInt();
            DDMInternalConfigFile.yPlusHeight = configFile.get("yPlusHeight").getAsInt();
            DDMInternalConfigFile.interval = configFile.get("interval").getAsInt();
            DDMInternalConfigFile.DIF = configFile.get("DIF").getAsInt();
            DDMInternalConfigFile.areaMinimaEmPixels = configFile.get("areaMinimaEmPixels").getAsInt();
            DDMInternalConfigFile.demoBarSize = configFile.get("demoBarSize").getAsInt();
            DDMInternalConfigFile.demoModoApresentacao = configFile.get("demoModoApresentacao").getAsBoolean();
            DDMInternalConfigFile.demoModoRapido = configFile.get("demoModoRapido").getAsBoolean();
            DDMInternalConfigFile.diretorio = configFile.get("diretorio").getAsString();
            DDMInternalConfigFile.demoModoRapidoReScaleX = configFile.get("demoModoRapidoReScaleX").getAsInt();
            DDMInternalConfigFile.demoModoRapidoReScaleY = configFile.get("demoModoRapidoReScaleY").getAsInt();
            DDMInternalConfigFile.calibrationOutPutToSub = configFile.get("calibrationOutPutToSubFolder").getAsBoolean();
            DDMInternalConfigFile.DividirModoRapido = configFile.get("DividirModoRapido").getAsBoolean();
            DDMInternalConfigFile.DividirModoRapidoReScaleX = configFile.get("DividirModoRapidoReScaleX").getAsInt();
            DDMInternalConfigFile.DividirModoRapidoReScaleY = configFile.get("DividirModoRapidoReScaleY").getAsInt();
            DDMInternalConfigFile.naoFazerComparacoesNoProprioMes = configFile.get("naoFazerComparacoesNoProprioMes").getAsBoolean();
            DDMInternalConfigFile.porcentagemDasComparacoesDIferencaDeTamanhoAceitavel = configFile.get("porcentagemDasComparacoesDIferencaDeTamanhoAceitavel").getAsInt();
            DDMInternalConfigFile.porcentagemDasComparacoesMinimaDeSimilaridade = configFile.get("porcentagemDasComparacoesMinimaDeSimilaridade").getAsInt();

        } catch (Exception e){
            JsonArray anosArray2 = new JsonArray();
            for (String s: DDMInternalConfigFile.anosList) {
                anosArray2.add(s);
            }
            JsonArray anosBissextoArray2 = new JsonArray();
            for (String s: DDMInternalConfigFile.anosBissextos) {
                anosBissextoArray2.add(s);
            }
            DDMConfig newDDMConfigFile = new DDMConfig();
            newDDMConfigFile.anosbissextos = anosBissextoArray2;
            newDDMConfigFile.anosList = anosArray2;
            newDDMConfigFile.diretorio = DDMInternalConfigFile.diretorio;
            newDDMConfigFile.demoModoRapido = true;
            newDDMConfigFile.demoModoApresentacao = true;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter((programPath+"/config.json"));
            gson.toJson(newDDMConfigFile,writer);
            writer.close();
            if(consoleNoControl == false){
                DDMInternalConfigFile.dmmgui.ConsoleText("Criando novo config.json");
            }
            loadedConfig = loadConfigs(loadedConfig,true);
            ignoreMe = true;
        }
        if(!ignoreMe){
            DDMInternalConfigFile.dmmgui.ConsoleText("config.json carregado!");
            DDMInternalConfigFile.dmmgui.ToggleButtons(true);
            return loadedConfig;
        }
        DDMInternalConfigFile.dmmgui.ToggleButtons(true);
        return loadedConfig;
    }
}
