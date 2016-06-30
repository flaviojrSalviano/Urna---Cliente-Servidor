import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Servidor implements Runnable{
	
	private boolean inicializado;
	private boolean executando;
	private ServerSocket server;
	private List<ServidorAux> lista_aux_server;//Esta lista recebe cada cliente conectado, para manter o controle do servidor
	private Thread thread;
	
	public Servidor(int porta) throws Exception{
		lista_aux_server = new ArrayList<ServidorAux>();
		
		inicializado = false;
		executando = false;
		
		open(porta);
	}
	
	private void open(int porta) throws Exception{
		//Cria um socket com a porta passada no Main
		server = new ServerSocket(porta);
		inicializado = true;
	}
	
	private void close(){
		//Este loop ira para as threads de cada Urna
		for(ServidorAux aux : lista_aux_server){
			try{
				aux.stop();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		//Depois de parar as threads iremos fechar a porta do servidor
		try{
			server.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		server = null;
		thread = null;
		inicializado = false;
		executando = false;
	}
	
	public void start(){
		//Inicializa a thread
		if(!inicializado || executando){
			return;
		}
		executando = true;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public void stop() throws Exception{
		//Para a thread
		executando = false;
		
		if(thread != null){
			thread.join();
		}
	}
	
	@Override
	public void run(){
		//Este metodo entra em um loop para receber cada conexao no servidor
		System.out.println("Aguardando conexao.");
		
		while(executando){
			try{
				server.setSoTimeout(2500);
				
				Socket socket = server.accept();
				
				System.out.println("Conexao estabelecida.");
				//Cria uma nova thread para cada Urna conectada
				ServidorAux aux = new ServidorAux(socket);
				aux.start(); //Inicializa a thread
				
				lista_aux_server.add(aux);//Adiciona a Urna na lista de Urnas
				
			}catch(SocketTimeoutException e){
				
			}catch(Exception e){

				break;
			}
		}
		close();//Para o servidor
	}
}
