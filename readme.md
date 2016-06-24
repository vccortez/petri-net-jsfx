# README #


### Comet IDE

* Projeto de editor de texto, com as funcionalidades de editar varios arquivos simultaneamente, abrir arquivo do disco, salvar arquivo do disco e executar arquivo.
* Projeto versao 1.0

### Como instalar

* Instalar a ultima versão Java JDK 8 (que inclua o JavaFX 8).
* Instalar Eclipse 4.3 ou posterior com o plugin e(fx)clipse. Para instalar o plugin acesse no eclipse Help -> Eclipse marketplace e depois pesquisar por 'e(fx)clipse' e instalar esse pacote.
* (Opctional) Instalar o Scene Builder 2.0 ou posterior.
* Para informacoes mais detalhadas vide http://code.makery.ch/library/javafx-8-tutorial/pt/part1/

### Como executar
* Importar projeto para workspace do eclipse
* Executar projeto no eclipse

### Como criar uma operação
* Criar uma classe java dentro do pacote 'operation'
* A classe deve ter um construtor que recebe um objeto PetriNet e se necessario um outro parametro dependendo do algoritmo.
* A classe deve ter um metodo, que sera chamado pelo Controller, que contera o algoritmo e retorna uma string.
