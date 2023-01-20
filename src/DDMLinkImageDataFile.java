import java.awt.image.BufferedImage;

public class DDMLinkImageDataFile {
    public int fileInternalId;
    public String urlInput;
    public String ano;
    public String mes;
    public String dia;
    public String horario;
    public String diretorio;
    public BufferedImage imagem;
    public BufferedImage imagemCrop;
    public int errorCode = 0;

    public void Copy(DDMLinkImageDataFile newData){
        fileInternalId = newData.fileInternalId;
        urlInput = newData.urlInput;
        ano = newData.ano;
        mes = newData.mes;
        dia = newData.dia;
        horario = newData.horario;
        diretorio = newData.diretorio;
        imagem = newData.imagem;
        imagemCrop = newData.imagemCrop;
        errorCode = newData.errorCode;
    }
}

