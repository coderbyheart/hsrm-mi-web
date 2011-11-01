package fragebogen;

public class Antwort {
	private String frage;
	private String antwort;
	private Integer punkte;

	public Antwort(String q, String a, Integer p) {
		frage = q;
		antwort = a;
		punkte = p;
	}

	public String getAntwort() {
		return antwort;
	}

	public Integer getPunkte() {
		return punkte;
	}

	public String getFrage() {
		return frage;
	}
}
