package eu.elf.oskari.search;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.search.channel.ELFGeoLocatorSearchChannel;
import fi.nls.oskari.util.ResponseHelper;
import org.json.JSONArray;
import org.json.JSONObject;

@OskariActionRoute("GetLocationType")
public class GetLocationTypeHandler extends ActionHandler {

    private JSONObject elfLocationTypes = null;
    private JSONObject elfNameLanguages = null;


    @Override
    public void init() {
        ELFGeoLocatorSearchChannel elfchannel = new ELFGeoLocatorSearchChannel();
        elfchannel.init();
        elfLocationTypes = elfchannel.getElfLocationTypes();
        elfNameLanguages = elfchannel.getElfNameLanguages();
    }

    public void handleAction(final ActionParameters params)
            throws ActionException {

        JSONArray result = new JSONArray();
        try {
            result.put(elfLocationTypes);
            result.put(elfNameLanguages);
        } catch (Exception e) {
            throw new ActionException("Failed to get ELF Location Types");
        }
        ResponseHelper.writeResponse(params, result);
    }

}
