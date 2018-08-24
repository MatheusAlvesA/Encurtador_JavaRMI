# Encurtador de URLs Java
Este Encurtador de URLs foi desenvolvido em Java através da IDE Eclipse, como um projeto da disciplina de Programação Distribuída do curso de bacharelado em Tecnologia da Informação do IMD.

A proposta consiste em uma aplicação java que roda separadamente em múltiplas máquinas, sendo no mínimo uma operando como servidor.
Através da tecnologia [java RMI](https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html) O cliente se conecta ao servidor para realizar a inserção e busca de URLs encurtadas pelo servidor.
Versão **0.1**

# A Estratégia

O servidor tem o objetivo de receber requisições, através da interface RMI, e retornar como resposta o dado solicitado ou a confirmação de sucesso no processamento da requisição.

Ao cliente cabe o papel de receber do usuário final as URLs a serem encurtadas, submetendo ao servidor para armazenamento, e repassando para o cliente a versão encurtada daquela URL.
O Cliente também pode receber do usuário uma requisição para "tradução" de uma URL previamente encurtada de volta na URL original.


>Projeto, e documentação ainda em desenvolvimento...

## Autoria

 - [Matheus Alves - Encurtador](https://github.com/MatheusAlvesA)
 - [Sean Leary - Interpretador JSON](https://github.com/stleary/JSON-java)
 