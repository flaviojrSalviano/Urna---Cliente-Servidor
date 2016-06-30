# Urna Cliente-Servidor

Configuracoes

1. O projeto foi desenvolvido utilizando Eclipse.
2. A versao do Java mais antiga testada foi java 6.
3. Executar o projeto VotacaoServer pelo eclipse ou crie um executavel do projeto.
		.Neste caso abrir o PROMPT entrar na pasta que voce salvou o executavel e executar o arquivo .jar gerado, da seguinte forma: java -jar nomeDoExecutavel.jar 
		.Lembrando que é necessario que o arquivo DATA_FILE esteja no mesmo diretorio do executavel.
4. Com o servidor executando inicie o projeto cliente da mesma forma que o servidor, caso o servidor não esteja local voce precisa trocar o IP que se encontra no metodo Main da Classe Cliente.

Como Usar a Urna?
Para utilizar a urna voce precisa clicar no botao op999 para carregar os candidatos.
Duplo clique sobre um candidato e ele podera ser votado.
No campo de texto voce pode buscar o candidato pelo nome.
Votos em branco ou nulos deverao ser votados pelo botao correspondente.
Apos concluir todas as votacoes clicar no botao op888 para enviar todos os votos ao servidor e clique em Fim.

Todos os votos estarao salvos no arquivo DATA_FILE que devera ser incluido na mesma pasta que o servidor.