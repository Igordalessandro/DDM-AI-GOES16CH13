import java.util.Arrays;
import java.util.List;

public class DDMLib {
    public static List<String> mesList = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"); // Nunca editar
    public static List<String> diaListFormat = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09"); // Nunca editar
    public static List<String> horarioFormat = Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09");// Nunca editar

    public static String gerarMes(int mes) {
        String diaString = "";
        if (mes < 10) {
            diaString = mesList.get(mes);
        } else {
            diaString = Integer.toString(mes);
        }
        return diaString;
    }

    public static String gerarDia(int dia) {
        String diaString = "";
        if (dia < 9) {
            diaString = diaListFormat.get(dia);
            return diaString;
        }
        diaString = Integer.toString(dia);
        return diaString;
    }

    public static String gerarHorario(int hora) {
        String horaString = "";
        if (hora < 10) {
            horaString = horarioFormat.get(hora);
        } else {
            horaString = Integer.toString(hora);
        }
        return horaString;
    }

    public static int quantidadeDias(int mes, boolean bissexto) {
        switch (mes) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (bissexto) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return 0;
    }
}
