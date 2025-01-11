package org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions;

public class ContainerException extends Exception {

	private Integer id;
	private String akteur;

	public ContainerException (Integer id) {
		this.id = id;
	}

	public ContainerException (String akteur) {
		this.akteur = akteur;
	}

	/**
	 * Einfaches Exception-Handling. Orientierung an die Lösung Übungsaufgabe Nr. 3-2 besser...
	 */
	@Override
	public void printStackTrace() {
		if (this.id  != null) {
			System.out.println("Die User Story ist bereits vorhanden!");
		} else if (this.akteur != null) {
			System.out.println("Der Akteur '" + this.akteur + "' ist bereits bekannt!");
		}
		else {
			System.out.println(this.getMessage());
		}
	}
}
