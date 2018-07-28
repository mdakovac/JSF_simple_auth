package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class Message {
	public static void Display(String m) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, m, ""));
	}
}
