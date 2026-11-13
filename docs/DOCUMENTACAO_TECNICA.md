# Documentação Técnica - EcoTech (2ª Entrega)

Linguagem: Java 17 (JDBC). Banco: MySQL 8. Arquitetura em camadas (MVC / DAO).

## 1. Banco de dados

Script: `database/script_banco.sql`. Para criar, execute o arquivo inteiro no MySQL Workbench, DBeaver ou pelo terminal (`mysql -u root -p < database/script_banco.sql`). Banco criado: `reciclagem_eletronicos`.

### Tabela `doador`

| Coluna | Tipo | Observação |
|---|---|---|
| id | BIGINT UNSIGNED, PK, AUTO_INCREMENT | |
| nome_completo | VARCHAR(150) | obrigatório |
| email | VARCHAR(150) | obrigatório e único |
| telefone | VARCHAR(20) | celular com DDD |
| cep, logradouro, numero, complemento, bairro, cidade, uf | endereço completo para coleta | complemento é opcional |
| data_cadastro | DATETIME | preenchido automaticamente |

### Tabela `item_eletronico`

| Coluna | Tipo | Observação |
|---|---|---|
| id | BIGINT UNSIGNED, PK, AUTO_INCREMENT | |
| doador_id | BIGINT UNSIGNED, FK para `doador.id` | `ON DELETE RESTRICT` preserva o histórico |
| tipo_equipamento, marca, modelo | VARCHAR | obrigatórios |
| estado_conservacao | ENUM | OTIMO, BOM, REGULAR, RUIM, SUCATA |
| status_rastreamento | ENUM | AGUARDANDO_COLETA (padrão), COLETADO, EM_TRIAGEM, EM_RECICLAGEM, RECICLADO |
| data_cadastro, data_atualizacao | DATETIME | automáticos |

## 2. Conexão com o banco

Classe `br.edu.ads.reciclagem.infra.ConexaoBancoDados`. Abre uma conexão JDBC real a cada chamada de `obterConexao()`; não há listas em memória no projeto. Configuração por variáveis de ambiente, com padrões para uso local:

| Variável | Padrão |
|---|---|
| DB_URL | jdbc:mysql://localhost:3306/reciclagem_eletronicos |
| DB_USER | root |
| DB_PASS | (vazio) |

`testarConexao()` retorna `true` quando o banco responde. O `Main` usa esse teste antes de abrir o menu.

## 3. Estrutura de POO

| Classe / interface | Papel |
|---|---|
| `Validavel` (interface) | contrato com o método `validar()` |
| `Validacao` (utilitária) | regras de obrigatoriedade e tamanho máximo das colunas |
| `EntidadeBase` (abstrata) | superclasse com `id` e `dataCadastro`; declara `descricaoResumida()` |
| `Pessoa` (abstrata) | estende `EntidadeBase`; nome, e-mail e telefone |
| `Doador` | estende `Pessoa`; acrescenta `Endereco` |
| `ItemEletronico` | estende `EntidadeBase`; tipo, marca, modelo, estado e status |
| `Repositorio<T>` (interface) | contrato de CRUD implementado pelos DAOs |

Herança: `EntidadeBase` → `Pessoa` → `Doador` e `EntidadeBase` → `ItemEletronico`.
Polimorfismo: `validar()` e `descricaoResumida()` são sobrescritos em cada subclasse.

## 4. Camadas

| Pacote | Conteúdo |
|---|---|
| `infra` | conexão JDBC |
| `modelo` | entidades e enums (`EstadoConservacao`, `StatusRastreamento`) |
| `repositorio` | `DoadorDAO`, `ItemEletronicoDAO`, `PersistenciaException` |
| `servico` | regras de negócio (`DoadorService`, `ItemEletronicoService`) |
| `controlador` | `EcoTechController` |
| `visao` | `ConsoleView` (menu de console) e `EntradaEncerradaException` |

## 5. CRUD e requisitos

### CRUD por entidade

| Entidade | Create | Read | Update | Delete |
|---|---|---|---|---|
| Doador | 1 - `DoadorService.cadastrar` | 2, 3 - `listar`, `buscarPorId` | 4 - `atualizar` | 5 - `excluir` (bloqueada se houver itens) |
| Item eletrônico | 6 - `registrar` | 7, 8, 11 - `listar`, `buscarPorId`, `consultarHistorico` | 9 - `atualizarDados`; 10 - `atualizarStatus` | 12 - `excluirNaoColetado` |

### Requisitos do escopo

| # | Requisito | Opção do menu | Onde |
|---|---|---|---|
| 1 | Cadastro de doadores | 1 | `DoadorService.cadastrar` / `DoadorDAO.salvar` |
| 2 | Registro de itens para descarte | 6 | `ItemEletronicoService.registrar` / `ItemEletronicoDAO.salvar` |
| 3 | Atualização do status de rastreamento | 10 | `ItemEletronicoService.atualizarStatus` (o status só avança) |
| 4 | Histórico de descartes de um doador | 11 | `ItemEletronicoDAO.listarPorDoador` |
| 5 | Exclusão de item ainda não coletado | 12 | `ItemEletronicoDAO.excluirSeNaoColetado` (só `AGUARDANDO_COLETA`) |

A correção de dados do item (opção 9) usa `atualizarDadosSeNaoColetado`, com a mesma condição de status no próprio `UPDATE`, o que evita alterar um item que acabou de ser coletado.

## 5.1 Tratamento de exceções

| Exceção | Origem | Tratamento |
|---|---|---|
| `IllegalArgumentException` | validações do modelo | exibida ao usuário; nada é gravado |
| `RegraNegocioException` | camada de serviço | exibida ao usuário |
| `PersistenciaException` | DAOs (erros de SQL, e-mail duplicado, FK) | exibida ao usuário |
| `EntradaEncerradaException` | `ConsoleView` (fim da entrada) | encerra o programa normalmente |
| `RuntimeException` (inesperada) | qualquer camada | mensagem genérica; o menu continua |

## 6. Como executar

1. Execute `database/script_banco.sql` no MySQL.
2. Abra a pasta no IntelliJ como projeto Maven (o `pom.xml` baixa o driver `mysql-connector-j`).
3. Se o MySQL tiver senha, defina a variável `DB_PASS`.
4. Execute a classe `br.edu.ads.reciclagem.Main`.

## 7. Pendências para a entrega

- Inserir nesta pasta o PDF/DOCX da entrega e os diagramas (classes e DER), conforme o README.
