package us.instantbay.automatethis.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import us.instantbay.automatethis.Event.Event;

public class ListenerManager {
	
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	public ArrayList<Listener> getListeners(){
		return this.listeners;
	}
	
	public void fireEvent(Event e) {
		for (Listener listener : this.getListeners()) {
			if (listener instanceof Listener) {
				for (Method method : listener.getClass().getMethods()) {
					for (Parameter param : method.getParameters()) {
						String eventClassName = param.getType().getCanonicalName().toString();
						String targetEventClassName = e.getClass().getCanonicalName().toString();
						if (eventClassName == targetEventClassName) {
							try {
								Constructor<?> constructor;
								constructor = listener.getClass().getConstructor();
								Object obj = constructor.newInstance();
								
								switch (e.getEventPriority()) {
									case LOW:
										method.invoke(obj, e);
										break;
									case MEDIUM:
										method.invoke(obj, e);
										break;
									case HIGH:
										method.invoke(obj, e);
										break;
									case HIGHEST:
										method.invoke(obj, e);
										break;
									default: 
										method.invoke(obj, e);
										break;
								}
							} 
							catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
								e1.printStackTrace();
							}
						}
					}
;				}
			}
		}
	}

}
