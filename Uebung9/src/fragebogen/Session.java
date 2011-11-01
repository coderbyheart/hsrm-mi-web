package fragebogen;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * Verwaltet den aktuellen Stand des Fragebogens
 * 
 * @author Markus Tacker <m@tacker.org>
 */
@ManagedBean(name = "fragebogen")
@SessionScoped
public class Session {
	SortedMap<String, SortedMap<String, Integer>> fragen;
	Set<String> questions;
	HashMap<String, String> antworten = new HashMap<String, String>();

	int currentQuestionNo = 0;
	private int maxPoints = 0;
	String currentQuestion;
	private String currentAnswer;

	String xmlFile;
	private boolean complete = false;

	public Session() {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		setXmlFile(context.getInitParameter("fragebogen"));

		InputStream is = context.getResourceAsStream(getXmlFile());
		fragen = Reader.getFragebogen(is);
		for(String q: fragen.keySet()) {
			int max = 0;
			for(String a: fragen.get(q).keySet()) {
				int ap = fragen.get(q).get(a);
				if (ap > max) max = ap;
			}
			maxPoints += max;
		}
		questions = fragen.keySet();
		reset();
	}

	public boolean nextQuestion() {
		currentQuestionNo++;
		currentQuestion = null;
		int i = 0;
		for (String q : questions) {
			i++;
			if (i < currentQuestionNo)
				continue;
			currentQuestion = q;
			break;
		}
		return currentQuestion != null ? true : false;
	}

	public int getCurrentQuestionNo() {
		return currentQuestionNo;
	}

	public String getCurrentQuestion() {
		return currentQuestion;
	}

	public ArrayList<String> getCurrentAnswers() {
		ArrayList<String> currentAnswers = new ArrayList<String>();
		for (String a : fragen.get(currentQuestion).keySet())
			currentAnswers.add(a);
		return currentAnswers;
	}

	public void setXmlFile(String xmlFile) {
		this.xmlFile = xmlFile;
	}

	public String getXmlFile() {
		return xmlFile;
	}

	public String answer() {
		if (complete) {
			// FIXME: Bei reload, wird das nicht abgefangen
			System.out.println("abort answer");
			return "/fragebogen/ergebnis";
		}
		antworten.put(getCurrentQuestion(), getCurrentAnswer());
		if (nextQuestion()) {
			return "/fragebogen/start";
		} else {
			complete = true;
			return "/fragebogen/ergebnis";
		}
	}

	public void setCurrentAnswer(String currentAnswer) {
		this.currentAnswer = currentAnswer;
	}

	public String getCurrentAnswer() {
		return currentAnswer;
	}

	public String reset() {
		complete = false;
		currentQuestionNo = 0;
		nextQuestion();
		return "/fragebogen/start";
	}

	public DataModel<Antwort> getResult() {
		ListDataModel<Antwort> result = new ListDataModel<Antwort>();
		ArrayList<Antwort> list = new ArrayList<Antwort>();
		result.setWrappedData(list);
		for (String q : antworten.keySet()) {
			String a = antworten.get(q);
			Antwort aw = new Antwort(q, a, fragen.get(q).get(a));
			list.add(aw);
		}
		return result;
	}
	
	public int getSum() {
		int sum = 0;
		for (String q : antworten.keySet()) {
			String a = antworten.get(q);
			try {
				sum += fragen.get(q).get(a);
			} catch (NullPointerException e) {
			}
		}
		return sum;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}

	public int getMaxPoints() {
		return maxPoints;
	}
}
