# EcoTech - Sistema de Gestão de Resíduos Eletrônicos

**Autor:** Pedro Miguel Sabater Medeiros (Vinicius)
**Curso:** Análise e Desenvolvimento de Sistemas (ADS) - UniCesumar

## Sobre o Projeto

O EcoTech é um sistema computacional que conecta usuários interessados em descartar peças eletrônicas a centros especializados em reciclagem, garantindo a rastreabilidade completa do lixo eletrônico. O projeto está alinhado ao Objetivo de Desenvolvimento Sustentável (ODS) 12 da ONU - Consumo e Produção Responsáveis, atuando na redução da geração de resíduos por meio da prevenção, redução, reciclagem e reuso.

## Lista de Requisitos (Escopo)

1. O sistema deve permitir o cadastro de doadores, com nome completo, e-mail, telefone celular e endereço completo para coleta.
2. O sistema deve permitir o registro de itens eletrônicos para descarte, contendo informações detalhadas como tipo de equipamento, marca, modelo e estado de conservação.
3. O sistema deve permitir a atualização do status de rastreamento do equipamento para refletir seu estágio no processo de reciclagem.
4. O sistema deve permitir a consulta detalhada do histórico de descartes realizados por um doador específico.
5. O sistema deve permitir a exclusão de registros de itens eletrônicos que tenham sido cadastrados de forma incorreta pelo doador, desde que ainda não tenham sido coletados.

## Cronograma de Execução

| Data Prevista | Atividade | Responsável |
|---|---|---|
| 15/09/2026 | Levantamento de Requisitos e definição do ODS | Vinicius |
| 22/09/2026 | Criação do Diagrama de Classes e Diagrama de Banco (DER) | Vinicius |
| 25/09/2026 | Estruturação do repositório no GitHub e elaboração do README | Vinicius |
| 15/10/2026 | Implementação das Classes Base, Herança e Polimorfismo | Vinicius |
| 30/10/2026 | Desenvolvimento do CRUD e integração ativa com Banco de Dados MySQL | Vinicius |

## Estrutura de Diretórios

- `/src`: Contém todo o código-fonte da aplicação orientada a objetos.
- `/docs`: Armazena a documentação do projeto, incluindo o PDF/DOCX desta entrega e os diagramas.
- `/database`: Contém o script SQL (DDL) para a criação do banco de dados MySQL e suas respectivas tabelas.

## Tecnologias

- **Linguagem:** Java 
- **Banco de Dados:** MySQL
- **Padrão Arquitetural:** Camadas (MVC / DAO)

## Pré-requisitos

| Item | Versão |
|---|---|
| JDK | 17 ou superior (`java -version` e `javac -version`) |
| MySQL Server | 8.0 ou superior |
| Maven (opcional) | 3.8 ou superior, para baixar o driver automaticamente |

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk mysql-server maven
sudo systemctl start mysql
```

### Windows

Instale o [JDK 17](https://adoptium.net/), o [MySQL Installer](https://dev.mysql.com/downloads/installer/) e, se for usar a opção com Maven, o [Apache Maven](https://maven.apache.org/download.cgi). Confirme que `java`, `javac` (e `mvn`) estão no `PATH`.

## Como executar

### 1. Criar o banco de dados

O script `database/script_banco.sql` cria o banco `reciclagem_eletronicos` e as tabelas `doador` e `item_eletronico`. **Atenção:** ele recria as tabelas (`DROP TABLE IF EXISTS`), então apaga dados existentes.

Linux / macOS:

```bash
mysql -u root -p < database/script_banco.sql
```

Windows (CMD ou PowerShell):

```bat
mysql -u root -p -e "source database/script_banco.sql"
```

Também é possível abrir o arquivo no MySQL Workbench ou DBeaver e executá-lo inteiro.

### 2. Configurar a conexão (se necessário)

Por padrão a aplicação conecta em `localhost:3306` com usuário `root` e senha vazia. Se o seu MySQL for diferente, defina as variáveis de ambiente **antes** de executar, sem alterar o código:

| Variável | Padrão |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/reciclagem_eletronicos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo&characterEncoding=UTF-8` |
| `DB_USER` | `root` |
| `DB_PASS` | *(vazio)* |

Linux / macOS:

```bash
export DB_USER=root
export DB_PASS=minha_senha
```

Windows (CMD):

```bat
set DB_USER=root
set DB_PASS=minha_senha
```

Windows (PowerShell):

```powershell
$env:DB_USER = "root"
$env:DB_PASS = "minha_senha"
```

### 3. Compilar e executar

#### Opção A: Maven (baixa o driver sozinho)

Linux / macOS:

```bash
mvn clean compile dependency:copy-dependencies -DoutputDirectory=target/lib
java -cp "target/classes:target/lib/*" br.edu.ads.reciclagem.Main
```

Windows:

```bat
mvn clean compile dependency:copy-dependencies -DoutputDirectory=target/lib
java -cp "target/classes;target/lib/*" br.edu.ads.reciclagem.Main
```

#### Opção B: sem Maven (apenas JDK)

Baixe o **MySQL Connector/J** (opção *Platform Independent*) em <https://dev.mysql.com/downloads/connector/j/>, extraia e copie o `.jar` para uma pasta `lib/` na raiz do projeto.

Linux / macOS:

```bash
mkdir -p out
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -cp "lib/*" -d out @sources.txt
java -cp "out:lib/*" br.edu.ads.reciclagem.Main
```

Windows (CMD):

```bat
mkdir out
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -cp "lib/*" -d out @sources.txt
java -cp "out;lib/*" br.edu.ads.reciclagem.Main
```

> O separador do classpath é `:` no Linux/macOS e `;` no Windows.

#### Opção C: IntelliJ IDEA

Abra a pasta como projeto Maven, aguarde o download do driver e execute a classe `br.edu.ads.reciclagem.Main`.

## Menu da aplicação

```
===== EcoTech - Gestão de Resíduos Eletrônicos =====
--- Doadores ---
 1 - Cadastrar doador
 2 - Listar doadores
 3 - Buscar doador por ID
 4 - Atualizar doador
 5 - Excluir doador
--- Itens eletrônicos ---
 6 - Registrar item para descarte
 7 - Listar todos os itens
 8 - Buscar item por ID
 9 - Corrigir dados de um item
10 - Atualizar status de rastreamento
11 - Consultar histórico de descartes de um doador
12 - Excluir item cadastrado incorretamente
 0 - Sair
```

### CRUD implementado

| Entidade | Create | Read | Update | Delete |
|---|---|---|---|---|
| Doador | 1 | 2 e 3 | 4 | 5 |
| Item eletrônico | 6 | 7, 8 e 11 | 9 (dados) e 10 (status) | 12 |

Nas opções de atualização (4 e 9), pressione **Enter** para manter o valor atual de um campo.

Fluxo sugerido para testar: cadastre um doador (1), registre um item para ele (6), corrija um dado (9), avance o status (10), consulte o histórico (11) e, com um item ainda em "Aguardando coleta", teste a exclusão (12).

## Regras de negócio e tratamento de erros

| Situação | Comportamento |
|---|---|
| Texto digitado onde se espera número | A pergunta é repetida até a entrada ser válida |
| Número fora do menu (inclusive valores muito grandes) | "Opção inválida"; nada é executado |
| Nome, e-mail, telefone ou endereço inválidos, ou maiores que a coluna do banco | `IllegalArgumentException` com mensagem clara; nada é gravado |
| E-mail já cadastrado (cadastro ou atualização) | Bloqueado pela regra de negócio e pela restrição `UNIQUE` do banco |
| Doador ou item inexistente | `RegraNegocioException`; o programa continua |
| Atualizar status para um estágio anterior ou igual ao atual | Bloqueado: o status só pode avançar |
| Corrigir dados ou excluir item já coletado | Bloqueado: só é permitido com status "Aguardando coleta" |
| Excluir doador que possui itens | Bloqueado pela chave estrangeira (`ON DELETE RESTRICT`), preservando o histórico |
| Entrada do teclado encerrada (Ctrl+D / Ctrl+Z) | O programa encerra normalmente, sem erro |
| MySQL desligado ou credenciais erradas | Mensagem orientando a conferir o servidor, o script e as variáveis |

Todas as consultas usam `PreparedStatement`, o que protege contra SQL Injection.

## Problemas comuns

- **`Não foi possível conectar ao MySQL`:** confirme que o servidor está ativo (`sudo systemctl start mysql` no Linux), que o script do banco foi executado e que `DB_USER`/`DB_PASS` estão corretos.
- **`No suitable driver` ou `ClassNotFoundException`:** o `.jar` do MySQL Connector/J não está no classpath (`lib/*` ou `target/lib/*`).
- **`Access denied for user`:** usuário ou senha incorretos.
- **`Unknown database 'reciclagem_eletronicos'`:** execute o passo 1.
- **Acentos quebrados no terminal Windows:** rode `chcp 65001` antes e adicione `-Dfile.encoding=UTF-8` ao comando `java`.
