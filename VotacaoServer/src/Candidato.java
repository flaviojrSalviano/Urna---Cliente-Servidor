
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;


public class Candidato {
	int codVotacao = 0;
	String nomeCandidato = "";
	String partido = "";
	int numVotos = 0;
	
	public JSONObject enviaCandidatos() throws IOException{
		JSONObject candidatos = new JSONObject();
	      BufferedReader buffRead = new BufferedReader(new FileReader("DATA_FILE"));//Abre arquivo de dados
	      String linha = "";
	      ArrayList<String> todos = new ArrayList<String>();//Todos os candidatos 
			while (true) {
				if (linha != null) {
					todos.add(linha);

				} else
					break;
				linha = buffRead.readLine();
				
			}
			buffRead.close();
		candidatos.put("file", todos.toString());//Apos receber os candidatos incluir dentro do json
		
		return candidatos;
	}
	
	public void salvaCandidatos(String votos) throws IOException{
		boolean aux = true;
		int cod = 0;
		int vot = 0;
		//salva os votos 
		votos = votos.replace("[", "");
		votos = votos.replace("]", "");
		List<String> candidatos = new ArrayList<String>(Arrays.asList(votos.split(",")));
		
		ArrayList<Candidato> allCandidatos = listaCandidatos();
		
		for(String allC : candidatos){
			List<String> uCandidatos = new ArrayList<String>(Arrays.asList(allC.split(";")));
			for(String u : uCandidatos){
				
				//Imprime os candidatos recebidos da Urna
				if(aux){
					System.out.println("candidato: "+u);
					cod = Integer.parseInt(u.replace(" ", ""));
					aux = false;
				}else{
					System.out.println("VOTOS: "+u);
					vot = Integer.parseInt(u.replace(" ", ""));
			        for (int i = 0; i < allCandidatos.size(); i++) {
			            if (allCandidatos.get(i).codVotacao == cod) {
			            	allCandidatos.get(i).numVotos += vot;//Realiza a soma dos votos
			            }
			        }
					aux = true;
				}
			}
		}
		System.out.println("CALCULANDO VOTOS...");
		atualizaVotacao(allCandidatos);
	}
	
	public ArrayList<Candidato> listaCandidatos() throws IOException{
	      BufferedReader buffRead = new BufferedReader(new FileReader("DATA_FILE"));
	      String linha = "";
	      ArrayList<Candidato> todos = new ArrayList<Candidato>();
			while (true) {
				if (linha != null) {
					if(linha != ""){
						List<String> uCandidatos = new ArrayList<String>(Arrays.asList(linha.split(";")));
						Candidato c = new Candidato();
						c.codVotacao = Integer.parseInt(uCandidatos.get(0));
						c.nomeCandidato = uCandidatos.get(1);
						c.partido = uCandidatos.get(2);
						c.numVotos = Integer.parseInt(uCandidatos.get(3));
						
						todos.add(c);
					}

				} else
					break;
				linha = buffRead.readLine();
				
			}
			buffRead.close();

		return todos;
	}
	
	public void atualizaVotacao(ArrayList<Candidato> votacao){
		String quebraLinha = System.getProperty("line.separator");//Pega quebra de linha do SO..para funcionar no Linux e Windows
		String votos = "";
		//Imprime o total de votos recebidos por cada candidato
        for (int i = 0; i < votacao.size(); i++) {
            System.out.print("PARTIDO: "+votacao.get(i).partido);
            System.out.println(" - CODIGO: "+votacao.get(i).codVotacao);
            System.out.println("NOME: "+votacao.get(i).nomeCandidato);
            System.out.println("TOTAL DE VOTOS: "+votacao.get(i).numVotos);
            System.out.println("____________________________________________________________________________");
            votos += votacao.get(i).codVotacao+";"+votacao.get(i).nomeCandidato+";"+votacao.get(i).partido+";"+votacao.get(i).numVotos+quebraLinha;
        }
        
        //Salva os votos no arquivo de dados
		try {
			OutputStream bytes;
			bytes = new FileOutputStream("DATA_FILE", false);
			OutputStreamWriter chars = new OutputStreamWriter(bytes);
			BufferedWriter strings = new BufferedWriter(chars);
			strings.write("");
			strings.flush();
			strings.write(votos);
			strings.close();
			chars.close();
			bytes.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
