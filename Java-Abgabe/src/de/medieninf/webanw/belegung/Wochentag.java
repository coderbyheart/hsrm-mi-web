package de.medieninf.webanw.belegung;

public enum Wochentag {

	Montag(0), 
	Dienstag(1), 
	Mittwoch(2), 
	Donnerstag(3), 
	Freitag(4), 
	Samstag(5), 
	Sonntag(6);

	private final int id;
	
	private Wochentag(int id) {
		this.id = id;
	}
	
	public String toString() {
		switch (this) {
		case Montag:
			return "Montag";
		case Dienstag:
			return "Dienstag";
		case Mittwoch:
			return "Mittwoch";
		case Donnerstag:
			return "Donnerstag";
		case Freitag:
			return "Freitag";
		case Samstag:
			return "Samstag";
		case Sonntag:
			return "Sonntag";
		default:
			throw new RuntimeException("Wochentag::toString kann nicht sein");
		}
	}
	
	public int toIntValue() {
		return id;
	}
	
	public static Wochentag genWochentag(int id) {
		switch (id) {
		case 0 : return Montag;
		case 1 : return Dienstag;
		case 2 : return Mittwoch;
		case 3 : return Donnerstag;
		case 4 : return Freitag;
		case 5 : return Samstag;
		case 6 : return Sonntag;
		default: throw new RuntimeException("" + id + " steht nicht für einen Wochentag");
		}
	}
	
};
