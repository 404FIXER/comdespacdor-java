package br.com.senac.bo;

import br.com.senac.exception.SenacException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Interface responsável por definir os métodos de negócio do
 * nosso compactador SENACZIP.
 *
 * @author Escreva seu nome.
 * @since 13/05/2025
 */
public interface ICompactadorBO {

    /**
     * Compacta um ou mais arquivos em um arquivo ZIP.
     * @param destino
     * @param arquivos
     * @return
     * @throws SenacException
     */
    public abstract List criarZip(File destino, File[] arquivos)
            throws SenacException, IOException;

    /**
     * Descompacta um arquivo ZIP em um diretório específico.
     * @param arquivo
     * @param diretorio
     * @throws SenacException
     */
    public abstract void extrairZip(File arquivo, File diretorio)
            throws SenacException, IOException;

}
