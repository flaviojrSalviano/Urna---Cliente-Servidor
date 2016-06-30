
public class Candidato{
   
    private String nomeCandidato;
    private String idCandidato;
    private String nomePartido;
    private int numeroVotos = 0;
   
    //Classe para representar cada candidato
    public Candidato(){
    	
    }
    
    public String getNome(){
    	return(nomeCandidato);
    }
    public String getId(){
    	return(idCandidato);
    }
    public String getPartido(){
    	return(nomePartido);
    }
    public int getVotos(){
    	return(numeroVotos);
    }
    public void setNome(String nome){
    	this.nomeCandidato = nome;
    }
    public void setId(String id){
    	this.idCandidato = id;
    }
    public void setPartido(String partido){
    	this.nomePartido = partido;
    }
    public void setVoto(){
    	this.numeroVotos++;
    }    
    public void setVotoTotal(int votos){
    	this.numeroVotos = votos;
    }
}



