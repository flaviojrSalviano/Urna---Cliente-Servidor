
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Porta do servidor
		int portaServer = 40003;	
		System.out.println("Inicializando servidor...");
		Servidor servidor;
		try {
			servidor = new Servidor(portaServer);
			servidor.start();
			System.out.println("Servidor inicializado...");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
