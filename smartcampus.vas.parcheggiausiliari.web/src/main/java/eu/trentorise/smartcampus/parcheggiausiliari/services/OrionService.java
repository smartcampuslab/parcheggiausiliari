package eu.trentorise.smartcampus.parcheggiausiliari.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

@Service
public class OrionService {

	private static final Logger logger = Logger.getLogger("OrionService");

	private Client client;

	@Value("${orion.type}")
	private String entityType;

	@PostConstruct
	private void init() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		client = Client.create(clientConfig);
	}

	public void insert(Parking o) {
		client.asyncResource("http://orion:1026/NGSI10/updateContext")
				.type("application/json").accept("application/json")
				.post(convert(o));
		logger.info("orion insert DONE");
	}

	public void insert(Street o) {
		client.asyncResource("http://orion:1026/NGSI10/updateContext")
				.type("application/json").accept("application/json")
				.post(convert(o));
		logger.info("orion insert DONE");
	}

	private OrionInsert convert(Street o) {
		OrionInsert payload = new OrionInsert();

		List<ContextElement> entities = new ArrayList<ContextElement>();
		entities.add(new ContextElement(entityType, o.getId(), Arrays.asList(
				new Tuple("name", o.getName()),
				new Tuple("slotOccupied", String.valueOf(o
						.getSlotsOccupiedOnPaying())),
				new Tuple("position", o.getPosition()[0] + ","
						+ o.getPosition()[1]),
				new Tuple("agency", o.getAgency()))));
		payload.setContextElements(entities);
		logger.info("street converted");
		return payload;
	}

	private OrionInsert convert(Parking o) {
		OrionInsert payload = new OrionInsert();

		List<ContextElement> entities = new ArrayList<ContextElement>();
		entities.add(new ContextElement(entityType, o.getId(), Arrays.asList(
				new Tuple("name", o.getName()),
				new Tuple("slotOccupied", String.valueOf(o
						.getSlotsOccupiedOnTotal())),
				new Tuple("position", o.getPosition()[0] + ","
						+ o.getPosition()[1]),
				new Tuple("agency", o.getAgency()))));
		payload.setContextElements(entities);
		logger.info("parking converted");
		return payload;
	}

	class OrionInsert {
		private List<ContextElement> contextElements;
		private String updateAction = "APPEND";

		public List<ContextElement> getContextElements() {
			return contextElements;
		}

		public void setContextElements(List<ContextElement> contextElements) {
			this.contextElements = contextElements;
		}

		public String getUpdateAction() {
			return updateAction;
		}

	}

	class Tuple {
		private String e1;
		private String e2;

		public Tuple(String e1, String e2) {
			this.e1 = e1;
			this.e2 = e2;
		}

		public String getE1() {
			return e1;
		}

		public String getE2() {
			return e2;
		}

	}

	class ContextElement {
		private String type;
		private String isPattern = "false";
		private String id;
		private List<OrionAttribute> attributes;

		public ContextElement(String type, String id, List<Tuple> attrs) {
			this.type = type;
			this.id = id;
			attributes = new ArrayList<OrionAttribute>();
			for (Tuple t : attrs) {
				attributes.add(new OrionAttribute(t.getE1(), "", t.getE2()));
			}
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getIsPattern() {
			return isPattern;
		}

		public void setIsPattern(String isPattern) {
			this.isPattern = isPattern;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<OrionAttribute> getAttributes() {
			return attributes;
		}

		public void setAttributes(List<OrionAttribute> attributes) {
			this.attributes = attributes;
		}

	}

	class OrionAttribute {
		private String name;
		private String type;
		private String value;

		public OrionAttribute(String name, String type, String value) {
			this.name = name;
			this.type = type;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}
