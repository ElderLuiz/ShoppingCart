package Exceptions;

public class DbException extends RuntimeException{
	
	//Implemente tratamento de exceções no seu código, impedindo-o que ele “quebre” ou 
	//pare de funcionar, fazendo verificações e criando exceções personalizadas.
	
	private static final long serialVersionUID = 1L;
	
	public DbException(String msg) {
		super(msg);
	}

}
