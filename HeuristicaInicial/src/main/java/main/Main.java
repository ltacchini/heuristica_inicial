package main;

import excel.module.SheetReader;

public class Main {
	
	public static void main(String[] args) throws Exception{
				
		SheetReader reader = new SheetReader("instancias/pedidos-2018-1-mod.xls");
		reader.read("Pedidos");
		
		reader.readAulas("Aulas");
	
	}
}
