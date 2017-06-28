package co.chatsdk.core.dao;

import java.util.List;


// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.chatsdk.core.NM;
import co.chatsdk.core.interfaces.CoreEntity;
import timber.log.Timber;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONException;
import org.json.JSONObject;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BUser implements CoreEntity {

    @Id
    private Long id;
    private String entityID;
    private Integer authenticationType;
    private String messageColor;
    private java.util.Date lastOnline;
    private java.util.Date lastUpdated;
    private Boolean online;
    private String metadata;

    @ToMany(referencedJoinProperty = "userId")
    private List<BLinkedAccount> linkedAccounts;

    @Transient
    private static final String TAG = BUser.class.getSimpleName();
    //@Transient
    //private static final boolean DEBUG = Debug.BUser;
    @Transient
    private static final String USER_PREFIX = "user";
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1441154471)
    private transient BUserDao myDao;

    @Keep
    public BUser(Long id) {
        this.id = id;
    }

    @Generated(hash = 932829735)
    public BUser(Long id, String entityID, Integer authenticationType, String messageColor, java.util.Date lastOnline, java.util.Date lastUpdated,
            Boolean online, String metadata) {
        this.id = id;
        this.entityID = entityID;
        this.authenticationType = authenticationType;
        this.messageColor = messageColor;
        this.lastOnline = lastOnline;
        this.lastUpdated = lastUpdated;
        this.online = online;
        this.metadata = metadata;
    }

    @Generated(hash = 497069028)
    public BUser() {
    }

    public String[] getCacheIDs(){
        return new String[]{entityID != null ? entityID : ""};
    }

    /** Get a link account of the user by type.
     * @return BLinkedAccount if found or otherwise null
     */
    public BLinkedAccount getAccountWithType(int type){
        for (BLinkedAccount account : getLinkedAccounts())
        {
            if (account.getType() == type)
                return account;
        }
        return null;
    }

    public List<BUser> getContacts() {
        List<BUser> contactList = new ArrayList<>();
        List<ContactLink> contactLinks;
        // For some reason the default ContactLinks do not persist, have to find in DB
        contactLinks = DaoCore.fetchEntitiesWithProperty(ContactLink.class,
                ContactLinkDao.Properties.LinkOwnerBUserDaoId, this.getId());
        for (ContactLink contactLink : contactLinks){
            contactList.add(contactLink.getBUser());
        }

        return contactList;
    }

    @Keep
    public void addContact(BUser newContact) {
        if (newContact.equals(this))
            return;

        // Retrieve contacts
        List contacts = getContacts();
        // Check if user is already in contact list
        if ( contacts.contains(newContact)) return;

        ContactLink contactLink = new ContactLink();
        // Set link owner
        contactLink.setLinkOwnerBUser(this);
        contactLink.setLinkOwnerBUserDaoId(this.getId());
        // Set contact
        contactLink.setBUser(newContact);
        contactLink.setUserId(newContact.getId());
        // insert contact link entity into DB
        daoSession.insertOrReplace(contactLink);

    }

    private FollowerLink fetchFollower(BUser follower, int type){
        return DaoCore.fetchEntityWithProperties(FollowerLink.class,
                new Property[]{FollowerLinkDao.Properties.UserId, FollowerLinkDao.Properties.LinkOwnerBUserDaoId, FollowerLinkDao.Properties.Type},
                follower.getId(), getId(),  type);
    }

    public List<BUser> getFollowers() {
        List<BUser> users = new ArrayList<BUser>();

        List<FollowerLink> followers = DaoCore.fetchEntitiesWithProperties(FollowerLink.class,
                new Property[]{FollowerLinkDao.Properties.LinkOwnerBUserDaoId, FollowerLinkDao.Properties.Type},
                getId(), FollowerLink.Type.FOLLOWER);

        for (FollowerLink f : followers)
        {
            if (f!=null)
                users.add(f.getBUser());
        }

        return users;
    }

    public List<BUser> getFollows() {
        List<BUser> users = new ArrayList<BUser>();

        List<FollowerLink> followers = DaoCore.fetchEntitiesWithProperties(FollowerLink.class,
                new Property[]{FollowerLinkDao.Properties.LinkOwnerBUserDaoId, FollowerLinkDao.Properties.Type},
                getId(), FollowerLink.Type.FOLLOWS);

        for (FollowerLink f : followers)
        {
            if (f!=null)
                users.add(f.getBUser());
        }

        return users;
    }

    public FollowerLink fetchOrCreateFollower(BUser follower, int type) {

        FollowerLink follows = fetchFollower(follower, type);

        if (follows== null)
        {
            follows = new FollowerLink();

            follows.setLinkOwnerBUser(this);
            follows.setBUser(follower);
            follows.setType(type);

            follows = DaoCore.createEntity(follows);
        }

        return follows;
    }

    public boolean isFollowing(BUser user){
        return fetchFollower(user, FollowerLink.Type.FOLLOWER) != null;
    }

    public boolean follows(BUser user){
        return fetchFollower(user, FollowerLink.Type.FOLLOWS) != null;
    }
    
    public void setMetaPictureUrl(String imageUrl) {
        setMetadataString(DaoDefines.Keys.PictureURL, imageUrl);
    }

    public String getMetaPictureUrl() {
        return metaStringForKey(DaoDefines.Keys.PictureURL);
    }

    public String getThumbnailPictureURL() {
        return metaStringForKey(DaoDefines.Keys.PictureURLThumbnail);
    }

    public void setMetaPictureThumbnail(String thumbnailUrl) {
        setMetadataString(DaoDefines.Keys.PictureURLThumbnail, thumbnailUrl);
    }

    public void setMetaName(String name) {
        setMetadataString(DaoDefines.Keys.Name, name);
    }

    public String getMetaName() {
        return metaStringForKey(DaoDefines.Keys.Name);
    }

    public void setMetaEmail(String email) {
        setMetadataString(DaoDefines.Keys.Email, email);
    }

    public String getMetaEmail() {
        return metaStringForKey(DaoDefines.Keys.Email);
    }

    public String metaStringForKey(String key){
        return (String) metaMap().get(key);
    }

    public void setMetadataString(String key, String value){
        Map<String, Object> map = metaMap();
        map.put(key, value);
        
        setMetaMap(map);
        DaoCore.updateEntity(this);
    }

    /**
     * Setting the metadata, The Map will be converted to a Json String.
     **/
    public void setMetaMap(Map<String, Object> metadata){
        metadata = updateMetaDataFormat(metadata);
        
        this.metadata = new JSONObject(metadata).toString();
    }
    
    @Deprecated
    /**
     * This is for maintaining compatibility with older chat versions, It will be removed in a few versions.
     **/
    private Map<String, Object> updateMetaDataFormat(Map<String, Object> map){
        
        Map<String, Object> newData = new HashMap<>();

        String newKey, value;
        for (String key : map.keySet())
        {
            if (map.get(key) instanceof Map)
            {
                value = (String) ((Map) map.get(key)).get(DaoDefines.Keys.Value);
                newKey = (String) ((Map) map.get(key)).get(DaoDefines.Keys.Key);
                newData.put(newKey, value);
                
                //if (DEBUG) Timber.i("convertedData, Key: %s, Value: %s", newKey, value);
            }
            else 
                newData.put(key, map.get(key));
        }
        
        return newData;
    }

    /**
     * Converting the metadata json to a map object
     **/
    public Map<String, Object> metaMap(){
        if (StringUtils.isEmpty(metadata))
            return new HashMap<>();

        try {
            return JsonHelper.toMap(new JSONObject(metadata));
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.e(e.getCause(), "Cant parse metadata json to map. Meta: %s", metadata);

            return new HashMap<>();
        }
    }

    
    
    
    public boolean hasThread(BThread thread){
        UserThreadLink data =
                DaoCore.fetchEntityWithProperties(UserThreadLink.class,
                        new Property[]{UserThreadLinkDao.Properties.ThreadId, UserThreadLinkDao.Properties.UserId}, thread.getId(), getId());

        return data != null;
    }

    public String getPushChannel(){
        if (entityID == null)
            return "";

        return USER_PREFIX + (entityID.replace(":","_"));
    }

//    public Map<String, String> getUserIndexMap(){
//        Map<String, String> values = new HashMap<String, String>();
//        values.put(DaoDefines.Keys.Name, processForQuery(getMetaName()));
//        values.put(DaoDefines.Keys.Email, processForQuery(getMetaEmail()));
//
//        String phoneNumber = metaStringForKey(DaoDefines.Keys.Phone);
//        if (DaoDefines.IndexUserPhoneNumber && StringUtils.isNotBlank(phoneNumber))
//            values.put(DaoDefines.Keys.Phone, processForQuery(phoneNumber));
//
//        return values;
//    }

    public boolean isMe(){
        return getId().longValue() == NM.currentUser().getId().longValue();
    }

    public String toString() {
        return String.format("BUser, id: %s meta: %s", id, getMetadata());
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getEntityID() {
        return this.entityID;
    }


    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }


    public Integer getAuthenticationType() {
        return this.authenticationType;
    }


    public void setAuthenticationType(Integer AuthenticationType) {
        this.authenticationType = AuthenticationType;
    }


    public String getMessageColor() {
        return this.messageColor;
    }


    public void setMessageColor(String messageColor) {
        this.messageColor = messageColor;
    }


    public java.util.Date getLastOnline() {
        return this.lastOnline;
    }


    public void setLastOnline(java.util.Date lastOnline) {
        this.lastOnline = lastOnline;
    }


    public java.util.Date getLastUpdated() {
        return this.lastUpdated;
    }


    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public Boolean getOnline() {
        return this.online;
    }


    public void setOnline(Boolean Online) {
        this.online = Online;
    }


    public String getMetadata() {
        return this.metadata;
    }


    public void setMetadata(String Metadata) {
        this.metadata = Metadata;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 579014559)
    public List<BLinkedAccount> getLinkedAccounts() {
        if (linkedAccounts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BLinkedAccountDao targetDao = daoSession.getBLinkedAccountDao();
            List<BLinkedAccount> linkedAccountsNew = targetDao._queryBUser_LinkedAccounts(id);
            synchronized (this) {
                if (linkedAccounts == null) {
                    linkedAccounts = linkedAccountsNew;
                }
            }
        }
        return linkedAccounts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1903600659)
    public synchronized void resetLinkedAccounts() {
        linkedAccounts = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1611990536)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBUserDao() : null;
    }




    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */



    /** Resets a to-many relationship, making the next get call to query for a fresh result. */


}
