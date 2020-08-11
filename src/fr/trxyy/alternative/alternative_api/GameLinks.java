package fr.trxyy.alternative.alternative_api;

public class GameLinks {

	public String BASE_URL;
	public String JSON_URL;
	public String JSON_NAME;
	public String MAINTENANCE;
	public String IGNORE_LIST;
	public String DELETE_LIST;
	public String CUSTOM_FILES_URL;
	public GameEngine engine;

	public GameLinks(String baseUrl, String jsonName,GameEngine engine) {
		if (baseUrl.endsWith("/")) {
			this.BASE_URL = baseUrl;
		} else {
			this.BASE_URL = baseUrl + "/";
		}
		this.engine = engine;
		this.JSON_URL = baseUrl + jsonName;
		this.JSON_NAME = jsonName;
		this.IGNORE_LIST = baseUrl + "ignore.cfg";
		this.DELETE_LIST = baseUrl + "delete.cfg";
		this.CUSTOM_FILES_URL = baseUrl + "files/";
		this.MAINTENANCE = baseUrl + "status.cfg";
	}

	public String getBaseUrl() {
		return this.BASE_URL;
	}
	
	public String getJsonName() {
		return this.JSON_NAME;
	}

	public String getMaintenanceUrl() {
		return this.MAINTENANCE;
	}

	public String getJsonUrl() {
		return this.JSON_URL;
	}

	public String getIgnoreListUrl() {
		return this.IGNORE_LIST;
	}

	public String getDeleteListUrl() {
		return this.DELETE_LIST;
	}

	public String getCustomFilesUrl() {
		if (engine.getGameStyle().isForge())
		{
			return  this.CUSTOM_FILES_URL+"Forge_"+engine.getGameVersion().getVersion()+"/";
		}
		return this.CUSTOM_FILES_URL;
	}

}
