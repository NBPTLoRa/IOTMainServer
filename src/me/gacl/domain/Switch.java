package me.gacl.domain;

public class Switch {
	private String light;
	private String war;
	private String link_war;
	
	public String getLight() {
		return light;
	}
	public void setLight(String light) {
		this.light = light;
	}
	public String getWar() {//
		return war;
	}
	public void setWar(String war) {
		this.war = war;
	}
	public String getLink_war() {
		return link_war;
	}
	public void setLink_war(String link_war) {
		this.link_war = link_war;
	}
	
    public String toString() {
        return light+","+war+","+link_war;
    }
}
