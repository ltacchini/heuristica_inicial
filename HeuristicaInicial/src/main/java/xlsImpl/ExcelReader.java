package xlsImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import domain.logic.Asignacion;
import domain.logic.*;

public class ExcelReader {
	File archivo;
	FileInputStream input;
	Workbook workbook;
	
	public ExcelReader(String path) {
		archivo = new File (path);
		
		try {
			input = new FileInputStream(archivo);
			workbook = WorkbookFactory.create(new File(path));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}     

	}
	
	public List<Clase> readClases(String nombreHoja) {		
		List<Clase> clases = new ArrayList<Clase>();

        Sheet sheet = workbook.getSheet(nombreHoja);

        DataFormatter dataFormatter = new DataFormatter();
        
        for (Row row: sheet) {
            for(Cell cell: row) {
                Asignacion asignacion = construirAsignacion(dataFormatter, cell);
                Clase clase = asignacion.getClase();
                clases.add(clase);
                System.out.println(clase);
            }
        }
        try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return clases;
	}
	
	public List<Aula> readAulas(String nombreHoja) {		
		List<Aula> aulas = new ArrayList<Aula>();

        Sheet sheet = workbook.getSheet(nombreHoja);
        
        for (Row row: sheet) {
            for(Cell cell: row) {
                Aula aula = construirAula(cell);
                aulas.add(aula);
            }
        }
        try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
        System.out.println(aulas.get(0));
		return aulas;
	}
	

	public List<Asignacion> readAsignaciones(String nombreHoja) {		
		List<Asignacion> asignaciones = new ArrayList<Asignacion>();

        Sheet sheet = workbook.getSheet(nombreHoja);
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row: sheet) {
            for(Cell cell: row) {
                Asignacion asignacion = construirAsignacion(dataFormatter, cell);
                asignaciones.add(asignacion);
            }
        }
        try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
        System.out.println(asignaciones.get(0));
		return asignaciones;
	}
	

	private Asignacion construirAsignacion(DataFormatter dataFormatter, Cell cell) {
		String nombre = "";
		Date horaDesde = null;
		Date horaHasta = null;
		DiaSemana diaSemana = null;
		int kantInscriptos = 0;
		String nombreAula = "";
		String nombreEdificio = "";
		
		if(cell.getRowIndex() >= 2) { 
			
			if(cell.getColumnIndex() == 0) 
			   	nombre = cell.getStringCellValue();
			
		    if(cell.getColumnIndex() == 3)
		    	diaSemana = DiaSemana.parse(cell.getStringCellValue());
		    
		    if(cell.getColumnIndex() == 4)
		    	getCellValue(cell,nombreEdificio);
		    
		    if(cell.getColumnIndex() == 6) 
		    	getCellValue(cell,nombreAula);
		    
		    if(cell.getColumnIndex() == 7) 
		    	horaDesde = cell.getDateCellValue();
		    
		    if(cell.getColumnIndex() == 8)
		    	horaHasta = cell.getDateCellValue();
		 
		    if(cell.getColumnIndex() == 11) 
		    	kantInscriptos = (int) cell.getNumericCellValue();		    
		}
		
		Aula aula = new Aula(nombreEdificio,nombreAula,0);
		// TODO VERIFICAR QUE EL AULA SEA VALIDA!!!!!!!!!!!!!.
		Clase clase = new Clase(cell.getRowIndex(),nombre,horaDesde,horaHasta,diaSemana,kantInscriptos);
		return new Asignacion(clase,aula);
	}	
	
	private Aula construirAula(Cell cell) {
		String edificio = "";
		String nombre = "";
		int capacidad = 0;
		if(cell.getRowIndex() >= 2) {
			if(cell.getColumnIndex() == 0)
			   	edificio = getCellValue(cell,edificio);
	 
		    if(cell.getColumnIndex() == 1)
		    	edificio = getCellValue(cell,edificio);
		   
		    if(cell.getColumnIndex() == 2) 
		    	capacidad = (int) cell.getNumericCellValue();
		}
		return new Aula(edificio,nombre,capacidad);
	}
	
	private String getCellValue(Cell c, String s) {
		if(c.getCellTypeEnum().getCode() == Cell.CELL_TYPE_NUMERIC) {
			s = String.valueOf((int) c.getNumericCellValue());
			return s;
		}
		if(c.getCellTypeEnum().getCode() == Cell.CELL_TYPE_STRING) {
			s = c.getStringCellValue();
			return s;
		}
		return null;
	}
}

