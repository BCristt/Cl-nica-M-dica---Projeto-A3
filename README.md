# Sistema de Gestão para Clínica Médica

## Descrição
Este projeto é um sistema simples de gerenciamento de uma clínica médica, desenvolvido na linguagem Java. O sistema permite o cadastro de médicos, pacientes, agendamento de consultas e listagem de consultas do dia.

## Funcionalidades

-Cadastro de médicos
-Cadastro de pacientes
-Agendamento de consultas
-Listagem de consultas do dia

## Como executar

1. Abra o projeto na sua IDE de preferência (VSCode, Eclipse ou outra).
2. Execute o arquivo principal:

SistemaClinica.java

3. O sistema rodará em console (linha de comando) e apresentará um menu interativo.

## Estrutura do projeto

Médico.java – Classe que representa o médico, com atributos como nome, CRM e especialidade.
Paciente.java – Classe que representa o paciente, contendo nome, CPF e data de nascimento.
Consulta.java – Classe que representa uma consulta, com informações do paciente, médico, data e hora.
SistemaClinica.java – Classe principal que contém o menu e toda a lógica do sistema

## Regras de Negócio

- Um paciente não pode ter mais de uma consulta com o mesmo médico no mesmo dia
- Um médico pode realizar até 10 consultas por dia
- Consultas não podem ser agendadas para datas passadas

## Objetivo 

Este projeto foi desenvolvido como exercício da disciplina de Programação de Soluções Computacionais, com o objetivo de aplicar os conceitos de programação orientada a objetos, criação de classes, métodos e interação com o usuário através do console.

## Desenvolvedores
Alunos:
- Anabela Cristina de Almeida Cordeiro - RA: 62510984
- Luana Caroline Madrona Cordeiro - RA: 62512711
- Nikolas Araujo Tavares - RA: 62512762
- Alessandra Firmiano Silva - RA: 62510096

Faculdade UNA 
Disciplina: Programação de Soluções Computacionais
Professora: Charlene Cassia
