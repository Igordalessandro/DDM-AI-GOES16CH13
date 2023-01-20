import java.util.Iterator;


public class DDMLinkGenerator{

    static DDMInternalConfigFile DDMInternalConfigFile;

    public DDMLinkGenerator(DDMInternalConfigFile loadedConfig) {
        DDMInternalConfigFile = loadedConfig;
    }
    public DDMInternalConfigFile GenerateLinks(){
        boolean first = true;

        String mes;
        String dia;
        String hora;

        //Arquivo de log
        //  ############## anos - primeiro
        for (Iterator<String> anos = DDMInternalConfigFile.anosList.iterator(); anos.hasNext(); ) {
            String ano = anos.next();
            String finalURL = "";
            if (first) {
                for (int i = DDMInternalConfigFile.mesInicial - 1; i < 13; i++) {
                    mes = DDMLib.gerarMes(i);
                    int dias = DDMLib.quantidadeDias(i, false) + 1;
                    for (int j = 0; j < dias; j++) {
                        dia = DDMLib.gerarDia(j);
                        for (int horario = 0; horario < 24; horario++) {
                            hora = DDMLib.gerarHorario(horario);
                            finalURL = ("http://satelite.cptec.inpe.br/repositoriogoes/goes16/goes16_web/ams_ret_ch13_baixa/" + ano + "/" + mes + "/S11635388_" + ano + mes + dia + hora + "00.jpg");
                            SendRequest(finalURL, ano, mes, dia, hora);
                        }
                    }
                    first = false;
                }
            } else {
                if (anos.hasNext() && !DDMInternalConfigFile.anosBissextos.contains(ano)) {
                    for (int i = 0; i < 13; i++) {
                        mes = DDMLib.gerarMes(i);
                        int dias = DDMLib.quantidadeDias(i, false) + 1;
                        for (int j = 0; j < dias; j++) {
                            dia = DDMLib.gerarDia(j);
                            for (int horario = 0; horario < 24; horario++) {
                                hora = DDMLib.gerarHorario(horario);
                                finalURL = ("http://satelite.cptec.inpe.br/repositoriogoes/goes16/goes16_web/ams_ret_ch13_baixa/" + ano + "/" + mes + "/S11635388_" + ano + mes + dia + hora + "00.jpg");
                                SendRequest(finalURL, ano, mes, dia, hora);
                            }
                        }
                    }
                } else {
                    //  ############## anos bissextos - normal
                    if (anos.hasNext() && DDMInternalConfigFile.anosBissextos.contains(ano)) {
                        for (int i = 0; i < 13; i++) {
                            mes = DDMLib.gerarMes(i);
                            int dias = DDMLib.quantidadeDias(i, true) + 1;
                            for (int j = 0; j < dias; j++) {
                                dia = DDMLib.gerarDia(j);
                                for (int horario = 0; horario < 24; horario++) {
                                    hora = DDMLib.gerarHorario(horario);
                                    finalURL = ("http://satelite.cptec.inpe.br/repositoriogoes/goes16/goes16_web/ams_ret_ch13_baixa/" + ano + "/" + mes + "/S11635388_" + ano + mes + dia + hora + "00.jpg");
                                    SendRequest(finalURL, ano, mes, dia, hora);
                                }
                            }
                        }
                    } else {
                        //  ############## anos bissextos - FINAL
                        if (!anos.hasNext() && DDMInternalConfigFile.anosBissextos.contains(ano)) {
                            for (int i = 0; i < DDMInternalConfigFile.mesInicial; i++) {
                                mes = DDMLib.gerarMes(i);
                                int dias = DDMLib.quantidadeDias(i, true) + 1;
                                for (int j = 0; j < dias; j++) {
                                    dia = DDMLib.gerarDia(j);
                                    for (int horario = 0; horario < 24; horario++) {
                                        hora = DDMLib.gerarHorario(horario);
                                        finalURL = ("http://satelite.cptec.inpe.br/repositoriogoes/goes16/goes16_web/ams_ret_ch13_baixa/" + ano + "/" + mes + "/S11635388_" + ano + mes + dia + hora + "00.jpg");
                                        SendRequest(finalURL, ano, mes, dia, hora);
                                    }
                                }
                            }
                        } else {
                            //  ############## anos normal - FINAL
                            for (int i = 0; i < DDMInternalConfigFile.mesFinal; i++) {
                                mes = DDMLib.gerarMes(i);
                                int dias = DDMLib.quantidadeDias(i, false) + 1;
                                for (int j = 0; j < dias; j++) {
                                    dia = DDMLib.gerarDia(j);
                                    for (int horario = 0; horario < 24; horario++) {
                                        hora = DDMLib.gerarHorario(horario);
                                        finalURL = ("http://satelite.cptec.inpe.br/repositoriogoes/goes16/goes16_web/ams_ret_ch13_baixa/" + ano + "/" + mes + "/S11635388_" + ano + mes + dia + hora + "00.jpg");
                                        SendRequest(finalURL, ano, mes, dia, hora);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //  ############## anos - normal
            first = false;
        }
        DDMInternalConfigFile.dmmgui.ToggleButtons(true);
        DDMInternalConfigFile.dmmgui.ConsoleText("Listando entradas processadas: " + DDMInternalConfigFile.CoreRequests.size());
        return DDMInternalConfigFile;
    }



    public static void SendRequest(String finalURL, String ano, String mes, String dia, String hora){
        DDMLinkImageDataFile newDDMLinkImageDataFile = new DDMLinkImageDataFile();
        newDDMLinkImageDataFile.urlInput = finalURL;
        newDDMLinkImageDataFile.ano = ano;
        newDDMLinkImageDataFile.mes = mes;
        newDDMLinkImageDataFile.dia = dia;
        newDDMLinkImageDataFile.horario = hora;
        newDDMLinkImageDataFile.diretorio = DDMInternalConfigFile.diretorio;
        DDMInternalConfigFile.CoreRequests.add(newDDMLinkImageDataFile);
    }
}
