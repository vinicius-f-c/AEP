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
