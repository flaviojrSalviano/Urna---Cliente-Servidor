
import java.io.BufferedReader;		
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.awt.FlowLayout;	 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import java.awt.event.*;
import javax.swing.JButton;

import org.json.JSONObject;

public class Cliente extends JFrame implements Runnable{
	//***********************************
	//Lista de variáveis locais da classe Cliente
	private boolean inicializado;
	private boolean executando;
	private BufferedReader in;
	private PrintStream out;
	private Socket socket;
	private Thread thread;
	private ArrayList<Candidato> candidatos;
	private ArrayList<Candidato> todos = new ArrayList<Candidato>();
	
    private final JLabel label1;
    private final JLabel label2; 
    private final JLabel label3;
    private final JTextField NomeCand;
    private final DefaultListModel listModel;
    private final JList Jlist;
    private final JScrollPane scrollpane;
    private final JButton votar;
    private final JButton votarbranco;
    private final JButton votarnulo;
    private final JButton op999;
    private final JButton op888 ;
    private final JButton fim ;
    private int marca = 0;
    private String stringError = "Antes de executar essa ação, carregue a lista de Candidatos";
    private int votosNulos = 0;
    private int votosBrancos = 0;
    private boolean flag = false, flag1 = false;
    
    //************************************
	
	public Cliente (String endereco, int porta) throws Exception{
		super( "Urna Eletrônica" );

		inicializado = false;
		executando = false;
		candidatos = new ArrayList<Candidato>();
		
		open(endereco, porta);//Cria uma conexao com o IP e Porta
	      
		  //*******************
	      //parte responsável por criar a interface gráfica
		 setDefaultCloseOperation(0);
	      setLayout(new FlowLayout() ); 
	      setResizable(false);
	      setSize(400,325);
	      setLocationRelativeTo(null);
	      label1 = new JLabel( "Digite o nome ou número de seu Candidato:" );
	      label1.setToolTipText( "Digite o Nome ou parte do nome do candidato que deseja votar" );
	      add( label1 );
	      NomeCand = new JTextField(30);
	      add( NomeCand );
	      label2 = new JLabel( " Possiveis candidatos: " );
	      label2.setToolTipText( " Selecione o candidato em que desejar votar" );
	      
	      label3 = new JLabel( "Candidatos:" );
	      add(label3);
	      listModel = new DefaultListModel();
	      Jlist = new JList(listModel);
	      scrollpane = new JScrollPane(Jlist);
	      add(scrollpane);
	      
	      //************************
	      
	      //************************
	      //botões da interface gráfica
	      //A classe interna ActionListener fica ouvinto os botões, esperando que sejam clicados,
	      //e quando clicados, executa o que está dentro de actionPerformed.
	      votar = new JButton("votar");//computa voto para o cadidato selecionado
	      		votar.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e){
	                  
	                                            boolean marca0 = false;
	                                           
	                                            if(marca == 1 && flag){
	                                            	 
	 	                                            String string;
	 	                                            for(Candidato candidato : candidatos){
	 	                                            	
	 	                                        		 string = candidato.getNome()+" "+candidato.getId()+" "+candidato.getPartido();
	 	                                        	     if((string.toLowerCase()).equals(NomeCand.getText().toLowerCase())){
	 	                                        	    		candidato.setVoto();
	 	        	                                            JOptionPane.showMessageDialog( null, "Voto computado com sucesso");
	 	        	                                            marca0 = true;
	 	        	                                            flag1 = true;
		                                                 }
	 	                                        	    
	 	                                            }
	 	                                           if(!marca0)
	 	                                           			JOptionPane.showMessageDialog( null, "Candidato inexistente");
	 
	                                            }else{
	                                                JOptionPane.showMessageDialog( null, stringError);
	                                            }
						}
					}
			);
	      //A classe interna ActionListener fica ouvinto os botões, esperando que sejam clicados,
	      //e quando clicados, executa o que está dentro de actionPerformed.		
	      votarnulo = new JButton("nulo");//computa voto nulo
	           	votarnulo.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e){
	                                            if(marca == 1){
	                                            	votosNulos++;
	                                            	 flag1 = true;
	                                            	JOptionPane.showMessageDialog( null, "Voto computado com sucesso");
												}else{
	                                                JOptionPane.showMessageDialog( null, stringError);
	                                            }
						}
					}
			);
	      //A classe interna ActionListener fica ouvinto os botões, esperando que sejam clicados,
	      //e quando clicados, executa o que está dentro de actionPerformed.
	      votarbranco = new JButton("branco");//computa voto em branco
	           	votarbranco.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e){
	                                            if(marca == 1){
	                                            	votosBrancos++;
	                                            	 flag1 = true;
	                                            	JOptionPane.showMessageDialog( null, "Voto computado com sucesso");
	                                            }else{
	                                                JOptionPane.showMessageDialog( null, stringError);
	                                            }
						}
					}
			);
	            //A classe interna ActionListener fica ouvinto os botões, esperando que sejam clicados,
	  	      //e quando clicados, executa o que está dentro de actionPerformed.
	      op888 = new JButton("op888");//envia os dados da urna para o servidor, usando o método "send" com a mensagem "888"
	      							   
	      							  
	            	op888.addActionListener(
					new ActionListener(){
				
						public void actionPerformed(ActionEvent e){
	                                            if(marca == 1){
	                                            	 String mensagem = "888";
		                                             try{	
		                                            	 	if(flag1){
		                                            	 		send(mensagem);//Envia a mensagem 888 para o Servidor com os votos
		                                            	 		JOptionPane.showMessageDialog( null, "Dados enviados ao servidor");
		                                            	 		}else{
		                                            	 			JOptionPane.showMessageDialog( null, "Realize ao menos um voto antes de enviar os dados ao servidor");
		                                            	 		}
		                                            	 	}catch(Exception ex){
			                                 				System.out.println(e);
			                                             }	 
		                                            
	                                            }else{
	                                                JOptionPane.showMessageDialog( null, stringError);
	                                            }
						}
					}
			);
	      //***************************************
	      //A classe interna ActionListener fica ouvinto os botões, esperando que sejam clicados,
	      //e quando clicados, executa o que está dentro de actionPerformed.     	
	      op999 = new JButton("op999");//botão para mostrar a lista de candidatos recebida do servidor pelo método "run",
	      							   //e envia a mensagem "999" ao servidor pelo método "send"
	
	           	op999.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e){
	     
	                                             String mensagem = "999";
	                                             try{
	                                             marca = 1;	 
	                                             send(mensagem);//Envia a mensagem 999 para o servidor
	                                             JOptionPane.showMessageDialog( null, "Lista carregada com sucesso");
	                                             }catch(Exception ex){
	                                 				System.out.println(e);
	                                             }	                                			
	                                             while(true){
		                                            	if(flag){ 
		                                            	String string;
			                                            	for(Candidato candidato : candidatos){
			                                            		//Recebe lista de candidatos
			                                        			if(!candidato.getNome().equals("Branco") && !candidato.getNome().equals("Nulo")){
			                                        			 string = candidato.getNome()+" "+candidato.getId()+" "+candidato.getPartido();
			                                        			 listModel.addElement(string);
			                                        			}
			                                        			
			                                        		}
			                                            	break;
	                                            	}
	                                            }
	                                             
	                                  }
					}
			);
	      
	            fim = new JButton("FIM");//botão para mostrar a lista de candidatos
				   //por enquanto a lista eh recebido como parametro do servidor
				   //mas deveremos implementar o servido em si, no entanto, esse parte será usada
	            		fim.addActionListener(
	            					new ActionListener(){
	            							public void actionPerformed(ActionEvent e){
	            										try{
	            										 stop();
			                                             close();
			                                             }catch(Exception ex){
			                                 				System.out.println(e);
			                                             }	
	            										
	            							}
	            					}
	            				);
	      add( votar);
	      add( votarbranco);
	      add( votarnulo);
	      add( op888);
	      add( op999);
	      add( fim);
	      
	      //*********************** fim da parte dos botões
	      
	      //******************
	      NomeCand.getDocument().addDocumentListener(new TextFieldHandler());
	      Jlist.addMouseListener(new MouseHandler());
	      //******************
	}
	//método utilizada para filtrar a lista de candidatos, baseado no que está escrito no textfield
	 public void filterList(String textBusca){

	      ArrayList<Candidato> Filterlist = new ArrayList<Candidato>();
	      String string;
	      listModel.removeAllElements();
	      for(Candidato candidato : candidatos){
	    	  if(candidato.getNome().toLowerCase().contains(textBusca.toLowerCase()) && (!candidato.getNome().equals("Branco") && !candidato.getNome().equals("Nulo"))){
	    		  Filterlist.add(candidato);
	    	  }
	      }
	      for(Candidato Itenlist : Filterlist){
	 	 	   string = Itenlist.getNome()+" "+Itenlist.getId()+" "+Itenlist.getPartido();
	           listModel.addElement(string);
	      }
	      
	  
	    }
	  
	   //método ouvinte do textField
	   private class TextFieldHandler implements DocumentListener
	   {    
	        @Override
	        public void insertUpdate(DocumentEvent e){
	           filterList(NomeCand.getText());
	        }
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	           filterList(NomeCand.getText()); 
	        }
	        @Override
	        public void changedUpdate(DocumentEvent e) {}

	   }
	   
	   //método ouvinte do mouse
	   private class MouseHandler implements MouseListener {
	       @Override
	       public void mouseClicked(MouseEvent e){
	          int count = 0;
	          String string;
	          count = e.getClickCount();
	          if(count == 2){
	          string = (String)listModel.getElementAt(Jlist.getSelectedIndex());
	          NomeCand.setText(string);
	       }
	    } 
	       @Override
	       public void mouseEntered(MouseEvent e) {}
	       @Override   
	       public void mouseExited(MouseEvent e){} 
	       @Override     
	       public void mousePressed(MouseEvent e) {}
	       @Override      
	       public void mouseReleased(MouseEvent e) {}
	   }
	   //retorna o numero de votos nulos
	   public int getVotosNulos(){
		   return (votosNulos);
	   }
	   //retorna o numero de votos em branco
	   public int getVotosBrancos(){
		   return(votosBrancos);
	   }

	   
	   
	 //Esse método envia para o servidor os dados da votação
	private JSONObject votos() throws IOException{
		JSONObject votos = new JSONObject();
		//PREENCHE O ARRAYLIST COM O COD DO CANDIDATO E QUANTIDADE DE VOTOS
	      ArrayList<String> Allvotos = new ArrayList<String>();

			for(Candidato candidato : candidatos){
				if(candidato.getNome().equals("Nulo"))candidato.setVotoTotal(getVotosNulos());//nó da lista representando os votos nulos
				if(candidato.getNome().equals("Branco"))candidato.setVotoTotal(getVotosBrancos());//nó da lista representando os votos e branco
				Allvotos.add(candidato.getId()+";"+String.valueOf(candidato.getVotos()));
				
			}
	      
	      //MONTA JSON PARA ENVIAR OS VOTOS
	      votos.put("code", "888");
	      votos.put("file", Allvotos.toString());
		
		return votos;
	}
	//inicia o socket TCP do client(inicia a aplicação cliente)
	private void open(String endereco, int porta) throws Exception{
		try{
		socket = new Socket(endereco, porta);
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintStream(socket.getOutputStream());
		inicializado = true;
		}catch(Exception e){
			System.out.println(e);
			close();
			throw e;
		}
	}
	//finaliza o socket TCP do cliente(finaliza a aplicação cliente)
	private void close(){
		
		if(in != null){
			try{
				in.close(); //Fecha o buffer de entrada
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		if(out != null){
			try{
				out.close(); //Fecha o buffer de saida
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		if(socket != null){
			try{
				socket.close();//Finaliza o socket de comunicacao
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		in = null;
		out = null;
		inicializado = false;
		executando = false;
		socket = null;
		thread = null;
		setVisible(false);
	}	
	//inicia a thread da aplicação
	public void start(){
		if(!inicializado || executando){
			return;
		}
		
		executando = true;
		thread = new Thread(this);
		thread.start();
	}
	//pausa a thread da aplicação
	public void stop() throws Exception{
		executando = false;
		
		if(thread != null){
			thread.join();
		}
		
	}
	//flag para indicar se a aplicação está executando
	public boolean isExecutando(){
		return executando;
	}
	//método utlizada para enviar mensagem ao servidor
	//a mensagem "888" contém os dados da votação da urna, e a mensagem "999" solicita ao servidor a lista de candidatos
	public void send(String mensagem) throws Exception{
		JSONObject pacote = new JSONObject();
		
		//SE MENSAGEM 999 O CLIENTE SOLICITA LISTA DE CANDIDATOS
		if(mensagem.equals("999")){
			pacote.put("code", "999");
			pacote.put("file", "");
		}else if(mensagem.equals("888")){
			//SE MENSAGEM 888 CLIENTE ENVIA VOTOS
				pacote = votos();
			}
		out.println(pacote);//Envia votos para o servidor
	}
	//método que executa o socket e a thread da aplicação cliente
	//esse método recebe do servidor a lista de candidatos, após enviarmos a mensagem "999" solicitando a lista
	//Após receber a resposta do servidor, nno formato JSON, os informações de cada candidato são extraídas
	//e um array do tipo candidato é montado e posteriormente atribuído a uma variável local do mesmo tipo, Array de candidatos
	@Override
	public void run(){
		while(executando){
			try{
				socket.setSoTimeout(2500);
				String mensagem = in.readLine();//recebe mensagem
				
					if(mensagem == null){
						break;
					}
					if(marca==1){
					JSONObject pacote = new JSONObject(mensagem);//Converte a mensagem em Json
				
					//Recebe a lista de candidatos
						if(!pacote.getString("file").isEmpty()){

							String rec = pacote.getString("file");
							rec = rec.replace("[", "");
							rec = rec.replace("]", "");
						
							//Primeiro quebro a String para pega cada linha
							List<String> candidatos = new ArrayList<String>(Arrays.asList(rec.split(",")));
								for(String allC : candidatos){
										//Em cada linha eu tenho os atributos do Candidato
										//Os atributos estao separados por ;
					
										List<String> uCandidatos = new ArrayList<String>(Arrays.asList(allC.split(";")));
		
										Candidato c = new Candidato();
										if(!uCandidatos.get(0).equals(null) && !uCandidatos.get(0).equals("")){
											//Pega cada atributo da linha atual
											c.setId(uCandidatos.get(0));
											c.setNome(uCandidatos.get(1));
											c.setPartido(uCandidatos.get(2));
							
											todos.add(c);//Incluindo cada candidato dentro do ArrayList
										}
								}
	
								//atribui a lista recebida do servidor para uma auxiliar da classe Cliente
								setListaCandidatos(todos);
								flag = true;
								
					
					
				}else{
					System.out.println("NENHUM CANDIDATO NA LISTA");
				}
			}else{
				JOptionPane.showMessageDialog( null, stringError);
			}
						
			}catch(SocketTimeoutException e){
				
			}catch(Exception e){
				System.out.println(e);
				break;
			}
		}

		close();
	}
	//atribui a lista recebido do servidor à variável local candidato, do tipo Array de canidatos
	public void setListaCandidatos(ArrayList<Candidato> listaCandidatos){
		this.candidatos = listaCandidatos;		
		
	}
	//função main da aplicação cliente, inicializa a thread, a aplicação e a interface gráfica
	public static void main(String[] args) throws Exception{
	
	//Endereco IP
	String enderecoIp = "127.0.0.1";//Trocar pelo IP onde o servidor esta Hospedado
	//Porta para conexao
	int porta = 40003;	

	System.out.println("Iniciando conexao com "+enderecoIp);
	Cliente client = new Cliente(enderecoIp, porta);
	client.start();
	
	System.out.println("Conexão estabelecida com sucesso.");
	System.out.println("Inicializando interface gráfica.");
 
		   	client.setVisible(true);

	}	  	   
}
