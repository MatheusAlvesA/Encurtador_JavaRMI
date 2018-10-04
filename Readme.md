# Encurtador de URLs Java
Este Encurtador de URLs foi desenvolvido em Java através da IDE Eclipse, como um projeto da disciplina de Programação Distribuída do curso de bacharelado em Tecnologia da Informação do IMD.

A proposta consiste em uma aplicação java que roda separadamente em múltiplas máquinas, sendo uma cliente e uma servidor.
Através da tecnologia [java RMI](https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html) O cliente se conecta ao servidor para realizar a inserção e busca de URLs encurtadas pelo servidor.

Versão **1.0**

# A Estratégia

O servidor tem o objetivo de receber requisições, através da interface RMI, e retornar como resposta o dado solicitado ou a confirmação de sucesso no processamento da requisição.

Ao cliente cabe o papel de receber do usuário final as URLs a serem encurtadas, submetendo ao servidor para armazenamento, e repassando para o cliente a versão encurtada daquela URL.
O Cliente também pode receber do usuário uma requisição para "tradução" de uma URL previamente encurtada de volta na URL original.

Existem cinco classes e uma interface dentro do pacote `Encurtador`, pacote que efetivamente representa esse projeto. O pacote `org.json` guarda o código necessário para interpretar do banco de dados.
O sistema se divide em 3 camadas: O banco de dados, com o acesso e gravação dos dados em disco; A camada que efetivamente processa o encurtamento das urls composta pelo Encurtador e pelo Servidor e, por último, a classe cliente que representa a camada de interação com o usuário final.
As classes e suas funções estão descritas abaixo.


| Classe | Função |
| ------ | ------ |
| Banco.java | Esta classe tem a responsabilidade de abstrair o acesso ao disco do resto do sistema. Atravez do pacote JSON importado, lê e grava chaves e valores no disco. |
| BancoException.java | Esta classe representa toda e qualquer exceção que as funções do banco de dados podem lançar, ela guarda a exceção que causou o erro e uma mensagem descritiva. |
| Encurtador.java | Esta é, possivelmente, a classe mais importante do projeto. Esta classe é responsável por efetivamente processar as URLs longas e transformá-las em suas versões curtas. Usando o banco de dados para guardar as URLs encurtadas como chave e as longas como valor. |
| ServerFachada.java | Ao contrário das anteriores está é uma interface, não uma classe. Esta interface define quais métodos o servidor que a implementa deve prover, para dessa forma, ser compatível com a classe cliente. |
| Server.java | Esta classe implemente ServerFachada.java e tem a responsabilidade de prover a interface entre o cliente rodando na máquina do usuário e o código encurtador que está em execução no servidor remoto. |
| Cliente.java | O cliente funciona de forma quase totalmente avulsa ao resto do sistema, ele é responsável por solicitar ao usuário as URLs a serem encurtadas ou desencurtadas. |

# A camada de dados

Esta camada abstrai toda a persistência de dados em disco.

## Banco

O banco de dados é uma classe simples que guarda em disco tuplas, sendo chave e valor necessariamente `Strings`. Os dados são guardados no formato json, no arquivo `banco.json`.
Todos os problemas com leitura e gravação em arquivo são tratados e transformados em instancias da Classe(Exceção) `BancoException`, essa exceção é do tipo checada e deve ser tratada nas camadas superiores.

**Inserir** dados no banco é feito através da função `put()`, essa função lança exceção caso ocorra algum problema na gravação dos dados no banco.
```java
public void put(String chave, String valor) throws BancoException;
```

**Buscar** dados no banco é feito através da função `get()`, essa função lança exceção caso não seja encontrada a chave no banco; caso o dado armazenado não seja uma string ou caso a chave passada seja `null`.
```java
public String get(String chave) throws BancoException;
```

**Buscar** dados no banco também é possível através da função `buscarValor()` que retorna a chave equivalente aquele valor. Essa função não lança exceções, mas retorna `null` caso não encontre o valor no banco.
```java
public String buscarValor(String valor);
```

**Remover** dados do banco é possível através da função `remover()`, essa função lança exceção caso ocorra algum problema na gravação dos dados no banco.
```java
public void remove(String chave) throws BancoException;
```

**Checar a existência** de determinadas chaves no banco é possível através da função `temChave()`, ela suprime possíveis exceções e retorna `true` caso encontre e `false` caso contrário.
```java
public boolean temChave(String chave);
```

**O número** de entradas no banco pode ser obtida através da função `size()`.
```java
public int size();
```

# Camada de processamento

Esta camada efetivamente encurta as URLs passadas e grava as mesmas em disco através da camada de dados.

## Encurtador

**O Construtor** dessa classe recebe o domínio onde o encurtador está hospedado para usar como parte constante das URLs encurtadas, sendo o sulfixo o código variável de 5 characteres alfanuméricos.
```java
public Encurtador(String prefixo) throws BancoException;
```

**Encurtar** uma URL é feito através da função `encurtar()` que recebe a URL longa e retorna sua versão encurtada. Essa função lança `MalformedURLException` caso a URL passada seja inválida e lança `BancoException` caso ocorra uma falha no banco.
```java
public String encurtar(String URLoriginal) throws BancoException, MalformedURLException;
```

**(Des)encurtar** uma URL é feito através da função `desEncurtar()` que recebe a URL previamente encurtada e retorna sua versão original. Caso a URL encurtada não exista no banco `null` será retornado. Essa função lança `BancoException` caso ocorra uma falha no banco.
```java
public String desEncurtar(String URLEncurtada) throws BancoException;
```

**Remover** uma URL previamente encurtada pode ser feito através da função `remover()` que recebe a URL e retorna `true` caso sucesso e `false` caso a URL não seja encontrada no banco. Essa função lança uma `BancoException` caso ocorra um problema com o banco de dados.
```java
public boolean remover(String URLEncurtada) throws BancoException;
```

**O número** de URLs encurtadas pode ser obtido através da função. `totalEncurtado()`.
```java
public int totalEncurtado();
```

## Servidor

Implementando o ServidorFachada essa classe é responsável por receber requisições remotas do cliente e repassar para o encurtador. Usando a tecnologia RMI a comunicação é quase totalmente abstraída.

**A função Main()** desta classe efetivamente cria uma instancia do servidor e inicializa o RMI para que receba conexões do cliente.
```java
Server servidor = new Server("short.com.br");
LocateRegistry.createRegistry(1099);
Naming.rebind("rmi://localhost/encurtador", servidor);
```

**Desligar** o servidor é possível através da função `desligar()` que pode ser executada remotamente.
```java
public void desligar() {
	System.out.println("> Servidor encerrado via cliente <");
	System.exit(0);
}
```

As demais funções desta classe simplesmente recebem as requisições de encurtar, desencurtar, tamanho e remoção de URLs, repassando tudo para a classe principal: Encurtar.

Exceções capturadas pelas funções dessa classe são logadas em um arquivo de log `log.txt` e um aviso de exceção é emitido no console.
```java
private void logarErro(Exception ex)
```

# Camada de interação com o usuário

Esta camada funciona na máquina do usuário, sendo a única das três com essa característica.

## Cliente

Esta é a única classe desta camada, sendo responsável por interagir com o usuário e repassar as solicitações para o servidor.

**A função Main()** desta classe, ao contrário do servidor, não instancia a própria classe, pois todos os métodos são estáticos.
Esta função busca pelo servidor através do `Naming.lookup()` e inicia uma conexão com o mesmo recebendo uma instancia da classe que implementa `ServerFachada`.
```java
ServerFachada servidor = null;
try {
	servidor = (ServerFachada) Naming.lookup("rmi://localhost/encurtador");
} catch (MalformedURLException | RemoteException | NotBoundException e) {
	System.out.println("O Servidor recusou a conexão ("+e.getMessage()+")");
	System.exit(0);
}
```

**Automaticamente** o cliente pode inserir no servidor 10 URLs previamente codadas na função `popularBanco()`. Esta função é útil para demonstrar rapidamente o funcionamento do sistema encurtando as URLs.
```java
public static void popularBanco(ServerFachada servidor);
```

**Exemplo de saída da função**

>facebook.com.br -> short.com.br/S7jkQ

>twitter.com.br -> short.com.br/fGYX6

>sigaa.ufrn.br -> short.com.br/weiIw

>ru.ufrn.br -> short.com.br/jK7Qi

>steampowered.com -> short.com.br/YgWjw

>stackoverflow.com -> short.com.br/prOmp

>https://www.instagram.com -> short.com.br/ssDFg

>https://www.twitch.tv/ -> short.com.br/dre8G

>www.twitch.tv -> short.com.br/5qjc5

>orkut.com.br -> short.com.br/F0fhT

>No total foram encurtadas `10` URLs

As demais funções desta classe solicitam ao usuário as URLs para encurtar e desencurtar e executam as demandas via servidor. Toda e qualquer exceção é capturada na própria função e é informada ao usuário. Mais detalhes a respeito das falhas poderão ser obtidos no log de erros do Servidor.

## Autoria

 - [Matheus Alves de Andrade](https://github.com/MatheusAlvesA)
