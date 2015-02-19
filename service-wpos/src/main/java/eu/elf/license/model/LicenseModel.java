package eu.elf.license.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Single License model that the user can subscribe (or has subscribed?)
 */
public class LicenseModel {

    private String id;
    private String name;
    private String description;
    private boolean isRestricted; // !allowAllRoles
    private List<String> roles = new ArrayList<String>(); // only used if(isRestricted)
    private List<LicenseParam> params = new ArrayList<LicenseParam>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void clearParams() {
        params.clear();
    }

    public void addParam(final LicenseParam param) {
        params.add(param);
    }

    public List<LicenseParam> getParams() {
        return params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRestricted() {
        return isRestricted;
    }

    public void setRestricted(boolean isRestricted) {
        this.isRestricted = isRestricted;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void cleartRoles() {
        roles.clear();
    }

    public void addtRole(final String rolename) {
        roles.add(rolename);
    }
}