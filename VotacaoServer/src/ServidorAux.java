import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class ServidorAux implements Runnable{
	
	private BufferedReader in;
	private PrintStream out;
	private boolean inicializado;
	private boolean executando;
	private Socket socket;
	private Thread thread;

	public ServidorAux(Socket socket) throws Exception{
		this.socket = socket;
		this.inicializado = false;
		this.executando = false;
		
		open();
	}
	
	private void open() throws Exception{
		//Cria Buffer de Entrada in e Saida out de dados
		try{
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintStream(socket.getOutputStream());
		inicializado = true;
		}catch(Exception e){
			close();
			throw e;
		}
		
	}
	
	private void close(){
		//Este metodo ira fechar a conexao TCP com a Urna
		if(in != null){
			try{
				in.close(); //Fecha o Buffer de entrada
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		if(out != null){
			try{
				out.close(); //Fecha o Buffer de saida
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		try{
			socket.close(); //Fecha a porta 
		}catch(Exception e){
			System.out.println(e);
		}
		
		in = null;
		out = null;
		inicializado = false;
		executando = false;
		socket = null;
		thread = null;
	}
	
	public void start(){
		//Inicializar a thread da Urna no servidor
		if(!inicializado || executando){
			return;
		}
		executando = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() throws Exception{
		//Parar a thread da Urna no servidor
		executando = false;
		
		if(thread != null){
			thread.join();
		}
	}
	
	@Override
	public void run(){
		Candidato candidatos = new Candidato();
		//Este loop aguarda a Urna realizar uma solicitacao
		while(executando){
			try{
			socket.setSoTimeout(2500);
			String mensagem = in.readLine();//Recebe uma mensagem da Urna

			JSONObject pacote = new JSONObject(mensagem);//Transforma a mensagem em um Json
			
			//Apenas aceita as mensagens code seguidas de 999 ou 888
			if(pacote.getString("code").equals("999")){
				//enviar mensagem para o cliente
				JSONObject pacote2 = candidatos.enviaCandidatos();
				out.println(pacote2.toString()); //Envia um Json com os candidatos
				
			}
			if(pacote.getString("code").equals("888")){
				//recebe candidatos
				candidatos.salvaCandidatos(pacote.getString("file"));//Salva os candidatos em nosso arquivo de dados
			}
		}catch(SocketTimeoutException e){
		}catch(Exception e){
			break;
		}
	}
		//Caso um cliente seja desconectado iremos interromper a conexao fechando a porta do socket
		System.out.println("Um cliente desconectou.");
		close();
	}

}
