package domain;

import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Asignador {
	public Map<Aula,BitSet> disponibilidad;
			
	public Asignador(Map<Aula,BitSet> disp) {
		disponibilidad = disp;
	}
		
	// Ver si esta aula no fue asignada a otra materia.
	// Tengo este aula y esta materia, y quiero que la asignes.
	// Si hay un horario no disponible, no se puede preasignar!!!!!!!!
	public Asignacion asignar(Clase clase,Aula aula) {
			    	    	
//    	BitSet bitset = disponibilidad.get(aula);
//    	if(bitset == null) {
//    		// Todos los dias de la semana, de lunes a domingo,de 0 a 24 con intervalos de 30 min.
//    		bitset = new BitSet(24*2*7);
//    		disponibilidad.put(aula, bitset);
//    	}
    	int desde = obtenerIndice(clase.diaSemana, clase.horaDesde);
    	int hasta = obtenerIndice(clase.diaSemana, clase.horaHasta);
    	
    	if(!sePuedeAsignar(clase,aula))  
    		throw new RuntimeException("No se puede asignar, ese rango horario ya se encuentra asignado: " + clase + ", " + aula);
    	
    	disponibilidad.get(aula).set(desde, hasta);
    	return new Asignacion(clase,aula);
	}
		
	public boolean sePuedeAsignar(Clase clase, Aula aula) {

    	int desde = obtenerIndice(clase.diaSemana, clase.horaDesde);
    	int hasta = obtenerIndice(clase.diaSemana, clase.horaHasta);

    	BitSet intervaloClase = disponibilidad.get(aula).get(desde,hasta);
    	
    	// Si el intervalo correspondiente a ese dia está vacío, significa que el aula está disponible en ese horario.
    	return intervaloClase.isEmpty(); 
	}

	public void validarCapacidad(Clase clase, Aula aula) {
		// Validar si la capacidad del aula es compatible con la clase que se quiere asignar.
    	if(!clase.puedeUsar(aula))
    		throw new RuntimeException("Preasignación incorrecta: capacidad no valida");
	}

	private int obtenerIndice(DiaSemana dia, Date hora) {
		return dia.ordinal()*48 + hora.getHours()*2 + (hora.getMinutes() > 0 ? 1 : 0);
	}
	
	public static Map<Aula,BitSet> crearDisponibilidad(Set<Aula> aulas){
		Map<Aula,BitSet> ret = new HashMap<Aula,BitSet>();
		// Todos los dias de la semana, de lunes a domingo,de 0 a 24 con intervalos de 30 min.
		for(Aula a: aulas) {
			ret.put(a, new BitSet(24*2*7));
		}
		return ret;
	}
	
	public String verDisponibilidad() {
		String ret = "";
		
		return ret;
	}
	
}
	
