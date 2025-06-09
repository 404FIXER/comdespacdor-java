# comdespacdor-java
Aplicação Java Simples para compactar e descompactar arquivos com interface gráfica em Swing.

# SenacZIP

Aplicação desktop em Java para compactar e descompactar arquivos usando Swing.

## 📂 Estrutura do Projeto

- `src/br/com/senac/bo/CompactadorBO.java` – lógica de compressão e descompressão  
- `src/br/com/senac/exception/SenacException.java` – tratamento de erros  
- `src/br/com/senac/ui/SenacView.java` – interface gráfica (Swing)  
- `lib/` – dependências (Groovy, se usadas)  
- `.idea/` – configuração do IntelliJ IDEA  

## ⚙️ Como Compilar e Executar

1. **Pelo terminal** (JDK instalado):
   ```bash
   # Compila
   javac -d out/production/SenacZIP src/br/com/senac/**/*.java
   # Empacota em um JAR executável
   jar cvfe SenacZIP.jar br.com.senac.ui.SenacView -C out/production/SenacZIP .
   # Executa
   java -jar SenacZIP.jar

