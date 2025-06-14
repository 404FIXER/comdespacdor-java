package br.com.senac.bo;

import br.com.senac.exception.SenacException;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class CompactadorBO implements ICompactadorBO {

    private File arquivoZipAtual;
    private static final int TAMANHO_BUFFER = 2048; // 2 Kb

    public File getArquivoZipAtual() {
        return arquivoZipAtual;
    }

    private void setArquivoZipAtual(File arquivoZipAtual) {
        this.arquivoZipAtual = arquivoZipAtual;
    }

    @Override
    public List criarZip(File destino, File[] arquivos)
            throws SenacException, IOException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        setArquivoZipAtual( null );
        try {
            //adiciona a extens o .zip no arquivo, caso n o exista
            if( !destino.getName().toLowerCase().endsWith(".zip") ) {
                destino = new File( destino.getAbsolutePath()+".zip" );
            }
            fos = new FileOutputStream( destino );
            bos = new BufferedOutputStream( fos, TAMANHO_BUFFER );
            List listaEntradasZip = criarZip( bos, arquivos );
            setArquivoZipAtual( destino );
            return listaEntradasZip;
        }
        finally {
            if( bos != null ) {
                try {
                    bos.close();
                } catch( Exception e ) {}
            }
            if( fos != null ) {
                try {
                    fos.close();
                } catch( Exception e ) {}
            }
        }
    }

    private List criarZip(OutputStream os, File[] arquivos ) throws ZipException, IOException {
        if( arquivos == null || arquivos.length < 1 ) {
            throw new ZipException("Adicione ao menos um arquivo ou diret rio");
        }
        List listaEntradasZip = new ArrayList();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream( os );
            for( int i=0; i<arquivos.length; i++ ) {
                String caminhoInicial = arquivos[i].getParent();
                List novasEntradas = adicionarArquivoNoZip( zos, arquivos[i], caminhoInicial );
                if( novasEntradas != null ) {
                    listaEntradasZip.addAll( novasEntradas );
                }
            }
        }
        finally {
            if( zos != null ) {
                try {
                    zos.close();
                } catch( Exception e ) {}
            }
        }
        return listaEntradasZip;
    }

    private List adicionarArquivoNoZip( ZipOutputStream zos, File arquivo, String caminhoInicial ) throws IOException {
        List listaEntradasZip = new ArrayList();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte buffer[] = new byte[TAMANHO_BUFFER];
        try {
            //diret rios n o s o adicionados
            if( arquivo.isDirectory() ) {
                //recursivamente adiciona os arquivos dos diret rios abaixo
                File[] arquivos = arquivo.listFiles();
                for( int i=0; i<arquivos.length; i++ ) {
                    List novasEntradas = adicionarArquivoNoZip( zos, arquivos[i], caminhoInicial );
                    if( novasEntradas != null ) {
                        listaEntradasZip.addAll( novasEntradas );
                    }
                }
                return listaEntradasZip;
            }
            String caminhoEntradaZip = null;
            int idx = arquivo.getAbsolutePath().indexOf(caminhoInicial);
            if( idx >= 0 ) {
                //calcula os diret rios a partir do diret rio inicial
                //isso serve para n o colocar uma entrada com o caminho completo
                caminhoEntradaZip = arquivo.getAbsolutePath().substring( idx+caminhoInicial.length()+1 );
            }
            ZipEntry entrada = new ZipEntry( caminhoEntradaZip );
            zos.putNextEntry( entrada );
            zos.setMethod( ZipOutputStream.DEFLATED );
            fis = new FileInputStream( arquivo );
            bis = new BufferedInputStream( fis, TAMANHO_BUFFER );
            int bytesLidos = 0;
            while((bytesLidos = bis.read(buffer, 0, TAMANHO_BUFFER)) != -1) {
                zos.write( buffer, 0, bytesLidos );
            }
            listaEntradasZip.add( entrada );
        }
        finally {
            if( bis != null ) {
                try {
                    bis.close();
                } catch( Exception e ) {}
            }
            if( fis != null ) {
                try {
                    fis.close();
                } catch( Exception e ) {}
            }
        }
        return listaEntradasZip;
    }

    public void fecharZip() {
        setArquivoZipAtual( null );
    }

    @Override
    public void extrairZip(File arquivoZip, File diretorio)
            throws SenacException, IOException {
        ZipFile zip = null;
        File arquivo = null;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[TAMANHO_BUFFER];
        try {
            //cria diret rio informado, caso n o exista
            if( !diretorio.exists() ) {
                diretorio.mkdirs();
            }
            if( !diretorio.exists() || !diretorio.isDirectory() ) {
                throw new IOException("Informe um diret rio v lido");
            }
            zip = new ZipFile( arquivoZip );
            Enumeration<?> e = zip.entries();
            while( e.hasMoreElements() ) {
                ZipEntry entrada = (ZipEntry) e.nextElement();
                arquivo = new File( diretorio, entrada.getName() );
                //se for diret rio inexistente, cria a estrutura
                //e pula pra pr xima entrada
                if( entrada.isDirectory() && !arquivo.exists() ) {
                    arquivo.mkdirs();
                    continue;
                }
                //se a estrutura de diret rios n o existe, cria
                if( !arquivo.getParentFile().exists() ) {
                    arquivo.getParentFile().mkdirs();
                }
                try {
                    //l  o arquivo do zip e grava em disco
                    is = zip.getInputStream( entrada );
                    os = new FileOutputStream( arquivo );
                    int bytesLidos = 0;
                    if( is == null ) {
                        throw new ZipException("Erro ao ler a entrada do zip: "+entrada.getName());
                    }
                    while( (bytesLidos = is.read( buffer )) > 0 ) {
                        os.write( buffer, 0, bytesLidos );
                    }
                } finally {
                    if( is != null ) {
                        try {
                            is.close();
                        } catch( Exception ex ) {}
                    }
                    if( os != null ) {
                        try {
                            os.close();
                        } catch( Exception ex ) {}
                    }
                }
            }
        } finally {
            if( zip != null ) {
                try {
                    zip.close();
                } catch( Exception e ) {}
            }
        }
    }
}
