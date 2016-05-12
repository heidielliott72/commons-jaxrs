package edu.psu.swe.commons.jaxrs.hateoas.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import edu.psu.swe.commons.jaxrs.AtomLink;

public class HateoasModel {
	private List<AtomLink> links_ = new ArrayList<AtomLink>();

	@XmlElement(name="links")
	public List<AtomLink> getLinks() {
		return links_;
	}

	public void setLinks(List<AtomLink> links_) {
		this.links_ = links_;
	}
	
}
