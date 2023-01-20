import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class DMMImageCompare {
    private static String diretorio;
    private static int size;
    private static DDMInternalConfigFile config;
    private static int count;
    private static BufferedImage imageCompareDataFolder;
    private static int erroWidthAceitavel = 0;
    private static int erroHeightAceitavel = 0;
    private static int local;
    private static float notaMaxima;

    public DMMImageCompare(DDMInternalConfigFile loadedConfig) {
        diretorio = loadedConfig.diretorio;
        config = loadedConfig;
    }

    boolean loaded = false;

    public void LinkDataFolder() {
        config.dmmgui.ConsoleText("Mapeando fotos nas pastas (..)/data/anos/mes/dias.");
        config.FolderMapSubs.clear();
        size = 0;
        File dataFolder = new File(diretorio + "data");
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
                        public boolean accept(File current, String name) { return new File(current, name).isDirectory();}
                    });
                    for (String sub : directoriesDia) {
                        File subFolder = new File(diretorio + "/data/" + ano + "/" + mes + "/" + dia+ "/" +sub);
                        String[] directoriesSub = subFolder.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File current, String name) { return !(new File(current, name).isDirectory());}
                        });
                    for (String arquivo : directoriesSub) {
                        //File arquivoFinal = new File(diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/" + sub + "/" + arquivo);
                        if (!config.FolderMapSubs.containsKey(ano)) {
                            HashMap<String, String> namePath = new HashMap<>();
                            namePath.put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/"+ sub + "/");
                            HashMap<String, HashMap<String, String>> subName = new HashMap<>();
                            subName.put(sub, namePath);
                            HashMap<String, HashMap<String, HashMap<String, String>>> diaSub = new HashMap<>();
                            diaSub.put(dia, subName);
                            HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> MesDia = new HashMap<>();
                            MesDia.put(mes, diaSub);
                            config.FolderMapSubs.put(ano, MesDia);
                            size++;
                        } else {
                            if (!config.FolderMapSubs.get(ano).containsKey(mes)) {
                                HashMap<String, String> namePath = new HashMap<>();
                                namePath.put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/"+ sub + "/");
                                HashMap<String, HashMap<String, String>> subName = new HashMap<>();
                                subName.put(sub, namePath);
                                HashMap<String, HashMap<String, HashMap<String, String>>> diaSub = new HashMap<>();
                                diaSub.put(dia, subName);
                                config.FolderMapSubs.get(ano).put(mes, diaSub);
                                size++;
                            } else {
                                if (!config.FolderMapSubs.get(ano).get(mes).containsKey(dia)) {
                                    HashMap<String, String> namePath = new HashMap<>();
                                    namePath.put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/"+ sub + "/");
                                    HashMap<String, HashMap<String, String>> subName = new HashMap<>();
                                    subName.put(sub, namePath);
                                    config.FolderMapSubs.get(ano).get(mes).put(dia, subName);
                                    size++;
                                } else {
                                    if(!config.FolderMapSubs.get(ano).get(mes).get(dia).containsKey(sub)){
                                        HashMap<String, String> namePath = new HashMap<>();
                                        namePath.put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/"+ sub + "/");
                                        config.FolderMapSubs.get(ano).get(mes).get(dia).put(sub, namePath);
                                        } else {
                                        config.FolderMapSubs.get(ano).get(mes).get(dia).get(sub).put(arquivo, diretorio + "/data/" + ano + "/" + mes + "/" + dia + "/"+ sub + "/");
                                        }
                                    }
                                    size++;
                                }
                            }
                        }
                    }
                }
            }
        }
        config.dmmgui.ConsoleText("Foram marcadas para comparacao: " + size + " imagens!");
        config.dmmgui.ConsoleText("Pasta data Completamente mapeada e marcadas com sucesso!");
        StartCompare();
    }

    public void StartCompare(){
        if(config.FolderMapSubs.size() <= 0){
            config.dmmgui.ConsoleText("nao foram achadas pastas 'sub' suficentes para comparacao.");
            config.dmmgui.ConsoleText("operacao da AI de comparação cancelada.");
            return;
        }
        count = 0;
        config.FolderMapSubs.forEach((ano, mesHashmap) -> {
            mesHashmap.forEach((mes, diaHashmap) -> {
                diaHashmap.forEach((dia, subHashmap) -> {
                    subHashmap.forEach((sub, arquivoHashmap) -> {
                        arquivoHashmap.forEach((nomeDoArquivo, pathDoArquivo) -> {
                            count++;
                            config.dmmgui.ConsoleText("Fila global de imagens: " + count + " De: " + (size + 1));
                            try {
                                imageCompareDataFolder = ImageIO.read(new File(pathDoArquivo+nomeDoArquivo));
                                try {
                                    erroHeightAceitavel = Math.round(((float)(imageCompareDataFolder.getHeight())/100)*config.porcentagemDasComparacoesDIferencaDeTamanhoAceitavel);
                                    erroWidthAceitavel = Math.round(((float)(imageCompareDataFolder.getWidth())/100)*config.porcentagemDasComparacoesDIferencaDeTamanhoAceitavel);
                                    config.dmmgui.setDisplay1ImageTypeOfBufferedImage(imageCompareDataFolder);
                                    DDMCompare(ano,mes,pathDoArquivo,nomeDoArquivo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    });
                });
            });
        });
    }

    public void DDMCompare(String inputAno,String InputMes,String path,String Nome) throws IOException {
        local = 0;
        JsonArray NotaArray = new JsonArray();
        config.FolderMapSubs.forEach((ano, mesHashmap) -> {
            mesHashmap.forEach((mes, diaHashmap) -> {
                //eliminar por tempo start
                if(!( config.naoFazerComparacoesNoProprioMes == true && inputAno == ano && InputMes == mes)){
                    //eliminar por tempo end
                    diaHashmap.forEach((dia, subHashmap) -> {
                        subHashmap.forEach((sub, arquivoHashmap) -> {
                            arquivoHashmap.forEach((nomeDoArquivo, pathDoArquivo) -> {
                                //carregar imagem;
                                BufferedImage Compare = null;
                                try {
                                    Compare = ImageIO.read(new File(pathDoArquivo+nomeDoArquivo));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //eliminar por tamanho start
                                if(Compare.getHeight() < imageCompareDataFolder.getHeight()+erroHeightAceitavel && Compare.getHeight() > imageCompareDataFolder.getHeight()-erroHeightAceitavel){
                                    if(Compare.getWidth() < imageCompareDataFolder.getWidth()+erroWidthAceitavel && Compare.getWidth() > imageCompareDataFolder.getWidth()-erroWidthAceitavel){
                                        //eliminar por tamanho end
                                        Image ImageFit = Compare.getScaledInstance(imageCompareDataFolder.getWidth(),imageCompareDataFolder.getHeight(), Image.SCALE_FAST);
                                        BufferedImage BufferedImageFit = new BufferedImage(ImageFit.getWidth(null), ImageFit.getHeight(null), BufferedImage.TYPE_INT_RGB);
                                        BufferedImageFit.getGraphics().drawImage(ImageFit, 0, 0, null);
                                        config.dmmgui.setDisplay2ImageTypeOfBufferedImage(BufferedImageFit);
                                        float nota = 0;
                                        //Compare image start
                                        long diff = 0;
                                        for (int j = 0; j < imageCompareDataFolder.getHeight(); j++) {
                                            for (int i = 0; i < imageCompareDataFolder.getWidth(); i++) {
                                                //Getting the RGB values of a pixel
                                                int pixel1 = imageCompareDataFolder.getRGB(i, j);
                                                Color color1 = new Color(pixel1, true);
                                                int r1 = color1.getRed();
                                                int g1 = color1.getGreen();
                                                int b1 = color1.getBlue();
                                                int pixel2 = BufferedImageFit.getRGB(i, j);
                                                Color color2 = new Color(pixel2, true);
                                                int r2 = color2.getRed();
                                                int g2 = color2.getGreen();
                                                int b2= color2.getBlue();
                                                long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                                                diff = diff+data;
                                            }
                                        }
                                        double avg = diff/(imageCompareDataFolder.getWidth()*imageCompareDataFolder.getHeight()*3);
                                        double percentage = (avg/255)*100;
                                        nota = (float) percentage;
                                        if(nota > notaMaxima){
                                            notaMaxima = nota;
                                        }
                                        config.dmmgui.setDisplay1text("Nota de similaridade: "+nota+" Nota maxima encontrada: "+notaMaxima);
                                        config.dmmgui.setDisplay1ImageTypeOfBufferedImage(BufferedImageFit);
                                        if(nota > config.porcentagemDasComparacoesMinimaDeSimilaridade){
                                            NotaArray.add(nomeDoArquivo+"_Coeficiente_de_Similaridade: "+nota);
                                        }
                                        config.dmmgui.setDisplay2text("Fila Local: " + local + " De: " + (size + 1));
                                        local++;
                                    } else {
                                        config.dmmgui.setDisplay2text("Fila Local: " + local + " De: " + (size + 1));
                                        local++;
                                    }
                                } else {
                                    config.dmmgui.setDisplay2text("Fila Local: " + local + " De: " + (size + 1));
                                    local++;
                                }
                            });
                        });
                    });
                } else {
                    config.dmmgui.setDisplay2text("Fila Local: " + local + " De: " + (size + 1));
                    local++;
                }
            });
        });
        if(NotaArray.size() > 0){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter((path+"Mapa_"+Nome+".json"));
            gson.toJson(NotaArray,writer);
            writer.close();
        }
    }
}
