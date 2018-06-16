package smoDwaSMO;
/**
 * @author Dariusz Pierzchala
 * 
 * Description: Description: Klasa gniazda obsługi obiektów - zgłoszeń 
 */

import java.util.LinkedList;

import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;
import dissimlab.monitors.MonitoredVar;
import dissimlab.simcore.BasicSimObj;
import dissimlab.simcore.SimEventSemaphore;
import dissimlab.simcore.SimControlException;



public class Smo extends BasicSimObj
{
    private LinkedList <Zgloszenie> kolejka;
    private SmoBis smoBis;
	SimEventSemaphore semafor;
	private boolean wolne = true;
    public RozpocznijObsluge rozpocznijObsluge;
    public ZakonczObsluge zakonczObsluge;
    public MonitoredVar MVczasy_obslugi;
    public MonitoredVar MVczasy_oczekiwania;
    public MonitoredVar MVdlKolejki;
    public MonitoredVar MVutraconeZgl;
    public double givenVariance;
	
    public Smo(SmoBis smo, SimEventSemaphore semafor, double givenVariance) throws SimControlException
    {
        // Utworzenie wewnętrznej listy w kolejce
        this.givenVariance = givenVariance;
        kolejka = new LinkedList <Zgloszenie>();
        // Nastepne SMO
        smoBis = smo;
        // Deklaracja zmiennych monitorowanych
        MVczasy_obslugi = new MonitoredVar("Czasy obsługi", givenVariance);
        MVczasy_oczekiwania = new MonitoredVar("Czasy oczekiwania", givenVariance);
        MVdlKolejki = new MonitoredVar("Długość kolejki", givenVariance);
        MVutraconeZgl = new MonitoredVar("Utracone zgłoszenia", givenVariance);
        this.semafor = semafor;
    }

    // Wstawienie zgłoszenia do kolejki
    public int dodaj(Zgloszenie zgl)
    {
        kolejka.add(zgl);
        MVdlKolejki.setValue(kolejka.size());
        return kolejka.size();
    }

    // Pobranie zgłoszenia z kolejki
    public Zgloszenie usun()
    {
    	Zgloszenie zgl = (Zgloszenie) kolejka.removeFirst();
        MVdlKolejki.setValue(kolejka.size());
        return zgl;
    }

    // Pobranie zgłoszenia z kolejki
    public boolean usunWskazany(Zgloszenie zgl)
    {
    	Boolean b= kolejka.remove(zgl);
        MVdlKolejki.setValue(kolejka.size());
        return b;
    }
    
    public int liczbaZgl()
    {
        return kolejka.size();
    }

	public boolean isWolne() {
		return wolne;
	}

	public void setWolne(boolean wolne) {
		this.wolne = wolne;
	}
	
    public SmoBis getSmoBis() {
		return smoBis;
	}

	public void setSmoBis(SmoBis smo2) {
		this.smoBis = smo2;
	}
	
	public SimEventSemaphore getSemafor() {
		return semafor;
	}

	public void setSemafor(SimEventSemaphore semafor) {
		this.semafor = semafor;
	}

	@Override
	public void reflect(IPublisher publisher, INotificationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean filter(IPublisher publisher, INotificationEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}