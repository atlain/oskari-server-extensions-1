-- add map layer; 
INSERT INTO oskari_maplayer(type, name, groupId, 
                            minscale, maxscale, 
                            url, username, password, srs_name, version,
                             locale) 
  VALUES('wfslayer', 'elf_ad_nlsfi', 905, 
         120000, 1, 
         'http://elf-wfs.maanmittauslaitos.fi/elf-wfs/services/elf-lod0ad', null, null, 'urn:ogc:def:crs:EPSG::3857', '2.0.0','{fi:{name:"AD Osoitteet - nls.fi", subtitle:""},sv:{name:"Ad Adresser - nls.fi", subtitle:""},en:{name:"AD Address- nls.fi", subtitle:""}}');
          
 
          
-- link to inspire theme; 
INSERT INTO oskari_maplayer_themes(maplayerid, 
                                   themeid) 
  VALUES((SELECT MAX(id) FROM oskari_maplayer), 
         (SELECT id FROM portti_inspiretheme WHERE locale LIKE '%Address%')); 
          
          
-- add template model stuff; 
INSERT INTO portti_wfs_template_model(name, description, type, request_template, response_template) 
VALUES (
    'ELF ad', 'ELF ad PoC', 'mah taip', 
    '/fi/nls/oskari/fe/input/format/gml/inspire/ad/nls_fi_wfs_template.xml', 
    '/fi/nls/oskari/fe/input/format/gml/ad/ELF_generic_AD.groovy');          
 
-- add wfs specific layer data; 
INSERT INTO portti_wfs_layer ( 
    maplayer_id, 
    layer_name, 
    gml_geometry_property, gml_version, gml2_separator, 
    max_features, 
    feature_namespace, 
    properties, 
    feature_type, 
    selected_feature_params, 
    feature_params_locales, 
    geometry_type, 
    selection_sld_style_id, get_map_tiles, get_feature_info, tile_request, wms_layer_id, 
    feature_element, feature_namespace_uri, 
    geometry_namespace_uri, 
    get_highlight_image, 
    wps_params, 
    tile_buffer, 
    job_type, 
    wfs_template_model_id) 
    VALUES ( (select max(id) from oskari_maplayer), 
      'ELF_AD_nls_fi',  
       'geom', '3.2.1', false, 
        5000, 
       'elf-lod0ad', 
       '', 
       '{"default" : "*geometry:Geometry,beginLifespanVersion:String,endLifespanVersion:String,localType:String"}', 
       '{}', 
       '{}', 
       '2d', 
       NULL, true, true, false, NULL, 
    'Address', 'http://www.locationframework.eu/schemas/Addresses/MasterLoD0/1.0', 
    '', 
    true, '{}', '{ "default" : 1, "oskari_custom" : 1}', 
    'oskari-feature-engine', (select max(id) from portti_wfs_template_model)); 
     
-- add wfs layer styles; 
INSERT INTO portti_wfs_layer_style (name,sld_style) VALUES(
    'oskari-feature-engine',
    '/fi/nls/oskari/fe/output/style/inspire/ad/nls_fi.xml'
);
 
-- link wfs layer styles; 
INSERT INTO portti_wfs_layers_styles (wfs_layer_id,wfs_layer_style_id) VALUES(
    (select max(id) from portti_wfs_layer),
    (select max(id) from portti_wfs_layer_style));
     
 
-- setup permissions for guest user;
INSERT INTO oskari_resource(resource_type, resource_mapping) values ('maplayer', 'wfslayer+http://elf-wfs.maanmittauslaitos.fi/elf-wfs/services/elf-lod0ad+elf_ad_fgifi');
 
-- permissions;
-- adding permissions to roles with id 10110, 2, and 3;
 
-- give view_layer permission for the resource to ROLE 10110 (guest);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'VIEW_LAYER', '10110');
 
-- give view_layer permission for the resource to ROLE 1 (guest);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'VIEW_LAYER', '1');
 
 
-- give view_layer permission for the resource to ROLE 2 (user);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'VIEW_LAYER', '2');
 
-- give publish permission for the resource to ROLE 2 (user);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'PUBLISH', '2');
 
 
-- give publish permission for the resource to ROLE 3 (admin);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'PUBLISH', '3');
 
-- give view_published_layer permission for the resource to ROLE 10110 (guest);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'VIEW_PUBLISHED', '10110');
 
-- give view_published_layer permission for the resource to ROLE 2 (user);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'VIEW_PUBLISHED', '2');
 
 
-- give view_published_layer permission for the resource to ROLE 10110 (guest);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) values
((SELECT MAX(id) FROM oskari_resource), 'ROLE', 'VIEW_PUBLISHED', '1');
