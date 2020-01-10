package com.redhat.management.approval;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Request implements Serializable {

    static final long serialVersionUID = 1L;

    private String requester;
    private String name;
    private String createdTime;
    private String id;
    private String parentId;
    private String tenantId;
    private String randomAccessKey;
    private String groupRef;
    private String groupName;

    private RequestPacket packet;
    private User user;
    
    public Request(Map<String, Object> maps, RequestPacket packet) {
        this.packet = packet;
        this.requester = maps.get("requester_name").toString();
        this.id = maps.get("id").toString();
        this.parentId = (maps.get("parent_id") == null) ? this.id : maps.get("parent_id").toString();
        this.tenantId = maps.get("tenant_id").toString();
        this.name = maps.get("name").toString();
        this.createdTime = maps.get("created_at").toString();
        this.groupRef = maps.get("group_ref").toString();
        this.randomAccessKey = maps.get("random_access_key").toString();
        this.groupName = maps.get("group_name").toString();
        this.user = getRHIdentity().getUser();
    }
    
    public Map<String, Object> getContent() {
        return this.packet.getContent();
    }

    public void setContent(Map<String, Object> content) {
        this.packet.setContent(content);
    }

    public Map<String, Object> getContext() {
        return this.packet.getContext();
    }

    public void setContext(Map<String, Object> context) {
        this.packet.setContext(context);
    }

    public String getRequester() {
        return this.requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public RequestPacket getRequestPacket() {
        return this.packet;
    }
    
    public void setRequestPacket(RequestPacket packet) {
        this.packet = packet;
    }
    
    public String getRandomAccessKey() {
        return this.randomAccessKey;
    }

    public void setRandomAccessKey(String randomAccessKey) {
        this.randomAccessKey = randomAccessKey;
    }

    public String getGroupRef() {
        return this.groupRef;
    }

    public void setGroupRef(String groupRef) {
        this.groupRef = groupRef;
    }

    public String toString() {
        return "Request: " + "\n name: " + this.name
                + "\n id: " + this.id
                + "\n tenant id: " + this.tenantId;
    }

    public String getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String id) {
        this.parentId = id;
    }

    public String getOriginalUrl() {
        return packet.getOriginalUrl();
    }
    
    public String getIdentityEmail() {
        return user.getEmail();
    }
    
    public String getIdentityFullName() {
        return user.getFirst_name() + " " + user.getLast_name();
    }
    
    public RHIdentity getRHIdentity() {
        return toRHIdentity(packet.getEncodedIdentity());
    }

    // Used by BPMN
    public String createSysadminIdentity() {
        RHIdentity rhid = getRHIdentity();

        rhid.getUser().setEmail("sysadmin");
        rhid.getUser().setFirst_name("sysadmin");
        rhid.getUser().setLast_name("sysadmin");
        String id = "x-rh-identity=" + createEncodedIdentity(rhid);
        return id;
    }

    private String createEncodedIdentity(RHIdentity id) {
        ObjectMapper Obj = new ObjectMapper();
        Obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String encoded = "";

        try {
            String jsonStr = Obj.writeValueAsString(id);
            byte[] bytes = jsonStr.getBytes("UTF-8");
            encoded = Base64.getEncoder().encodeToString(bytes);
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace(); 
        }

        return encoded.replace("=", "");
    }  

    private RHIdentity toRHIdentity(String encodedContext) {
        System.out.println("getIdentity: encoded context = " + encodedContext);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        RHIdentity id = new RHIdentity();

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedContext);
            String jsonStr = new String(decodedBytes);
            System.out.println("Decoded identity: " + jsonStr);
            id = mapper.readValue(jsonStr, RHIdentity.class);
            System.out.println("getIdentity: " + id);
        } catch (IOException e) {
              // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return id;
    }

}
